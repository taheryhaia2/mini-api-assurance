package com.assurance.mini_api_assurance.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Récupérer le header "Authorization"
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // Si pas de header ou s'il ne commence pas par "Bearer ", on passe au filtre suivant
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extraire le token (on coupe "Bearer " qui fait 7 caractères)
        jwt = authHeader.substring(7);

        // 3. Extraire le username du token via notre JwtService
        username = jwtService.extractUsername(jwt);

        // 4. Si on a un username et que l'utilisateur n'est pas encore connecté dans le "contexte" Spring
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 5. Vérifier si le token est mathématiquement valide
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Créer l'objet d'authentification officiel de Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Écrire dans le "bloc-notes" de Spring : "Cet utilisateur est connecté !"
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 6. Laisser la requête continuer son chemin vers le Controller
        filterChain.doFilter(request, response);
    }
}