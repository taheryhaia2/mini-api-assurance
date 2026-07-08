package com.assurance.mini_api_assurance.security;

import com.assurance.mini_api_assurance.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // TODO: For V2, implement a real account expiration system
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // TODO: For V2, implement account locking after N failed attempts
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // TODO: For V2, implement password expiration (e.g., every 90 days)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // TODO: For V2, implement email account activation
    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return user;
    }
}
