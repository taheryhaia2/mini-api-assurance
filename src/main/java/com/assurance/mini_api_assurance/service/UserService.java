package com.assurance.mini_api_assurance.service;

import com.assurance.mini_api_assurance.domain.Role;
import com.assurance.mini_api_assurance.domain.User;
import com.assurance.mini_api_assurance.repository.UserRepository;
import com.assurance.mini_api_assurance.security.CustomUserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User creerUtilisateur(String username, String password, Role role) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Ce nom d'utilisateur existe déjà");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        return userRepository.save(user);
    }
    public CustomUserDetails loadUserByUsernameForAuth(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return new CustomUserDetails(user);
    }
}