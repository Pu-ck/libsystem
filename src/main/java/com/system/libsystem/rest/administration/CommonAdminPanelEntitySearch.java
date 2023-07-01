package com.system.libsystem.rest.administration;

import com.system.libsystem.entities.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CommonAdminPanelEntitySearch<R extends JpaRepository<E, Long>, E> {

    private final UserService userService;
    private final R repository;

    protected List<E> getAdminPanelEntities(HttpServletRequest httpServletRequest) {
        userService.validateIfUserIsEnabledByServletRequest(httpServletRequest);
        return repository.findAll();
    }

    protected List<E> getAdminPanelEntityById(Long id, HttpServletRequest httpServletRequest, RuntimeException exception) {
        userService.validateIfUserIsEnabledByServletRequest(httpServletRequest);
        if (id == null) {
            return getAdminPanelEntities(httpServletRequest);
        }
        final List<E> entities = new ArrayList<>();
        final E entity = repository.findById(id).orElseThrow(() -> exception);
        entities.add(entity);
        return entities;
    }

}
