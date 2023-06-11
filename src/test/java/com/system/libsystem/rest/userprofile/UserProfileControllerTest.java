package com.system.libsystem.rest.userprofile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.libsystem.helpermodels.UserBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserProfileControllerTest {

    @Mock
    private UserProfileService userProfileService;

    @InjectMocks
    private UserProfileController userProfileController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userProfileController).build();
    }

    @Test
    void getUserProfileInformation_WithValidServletRequest_ReturnsProfileInformationJsonAndOkStatus() throws Exception {
        final List<String> expectedUserProfileInformation = Arrays.asList(
                "user4@gmail.com",
                "firstName",
                "lastName",
                "1123456784",
                "0",
                "0"
        );
        when(userProfileService.getUserProfileInformation(any(HttpServletRequest.class))).thenReturn(expectedUserProfileInformation);
        mockMvc.perform(get("/api/userprofile"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getJsonStringFromObject(expectedUserProfileInformation)));
    }

    @Test
    void getUserBooks_WithValidHttpServletRequest_ReturnsUserBooksJsonAndOkStatus() throws Exception {
        final String expectedUserBooksJson = "[{\"title\":\"Title 1\",\"borrowedBookId\":1,\"bookDetailsLink\":\"/books/1\"," +
                "\"borrowDate\":\"2023-05-02\",\"returnDate\":\"2023-07-02\",\"readyDate\":null,\"penalty\":\"0.00\"," +
                "\"affiliate\":\"Affiliate B\",\"status\":\"Returned\",\"extended\":true,\"accepted\":true," +
                "\"closed\":true}]";

        List<UserBook> expectedUserBooks = new ArrayList<>();
        expectedUserBooks.add(new UserBook("Title 1", 1L, "/books/1", "2023-05-02",
                "2023-07-02", null, "0.00", "Affiliate B", "Returned", true,
                true, true));

        final ObjectMapper objectMapper = new ObjectMapper();
        final String actualJson = objectMapper.writeValueAsString(expectedUserBooks);

        when(userProfileService.getUserBooks(any(HttpServletRequest.class))).thenReturn(expectedUserBooks);
        mockMvc.perform(get("/api/userprofile/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        assertEquals(expectedUserBooksJson, actualJson);
    }

    @Test
    void extendBookReturnDate_WithValidHttpServletRequestAndExtendBookRequest_ReturnsOkStatus() throws Exception {
        ExtendBookRequest extendBookRequest = new ExtendBookRequest();
        extendBookRequest.setBorrowedBookId(1L);
        extendBookRequest.setCardNumber(11234567890L);

        mockMvc.perform(put("/api/userprofile/books/1/extend-book")
                        .content(getJsonStringFromObject(extendBookRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void changeUserPassword_WithValidHttpServletRequestAndChangePasswordRequest_ReturnsOkStatus() throws Exception {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setOldPassword("oldPassword");
        changePasswordRequest.setNewPassword("newPassword");

        mockMvc.perform(put("/api/userprofile/change-password")
                        .content(getJsonStringFromObject(changePasswordRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    String getJsonStringFromObject(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

}
