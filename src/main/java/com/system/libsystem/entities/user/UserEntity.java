package com.system.libsystem.entities.user;

import com.system.libsystem.util.UserType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
public class UserEntity implements UserDetails {

    @Id
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_id_seq")
    private Long id;

    @Column(nullable = false, unique = true)
    private Long cardNumber;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private int orderedBooks;

    @Column(nullable = false)
    private int borrowedBooks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.getUserType().name()));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}