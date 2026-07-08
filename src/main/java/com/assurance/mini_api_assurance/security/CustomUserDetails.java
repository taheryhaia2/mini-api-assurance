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

    // TODO: Pour une V2, implémenter un vrai système d'expiration de compte
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // TODO: Pour une V2, implémenter un système de verrouillage après N tentatives échouées
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // TODO: Pour une V2, implémenter une expiration de mot de passe (ex: tous les 90 jours)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // TODO: Pour une V2, implémenter une activation de compte par email
    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return user;
    }
}