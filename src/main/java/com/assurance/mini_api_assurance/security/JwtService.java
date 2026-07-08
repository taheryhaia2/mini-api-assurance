package com.assurance.mini_api_assurance.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    // Spring lit la valeur du application.yml et l'injecte ici
    @Value("${jwt.secret}")
    private String secretKey;

    // 1. Génération du token
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername()) // Le "sujet" du token (qui est-ce ?)
                .issuedAt(new Date())               // Date de création
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Expire dans 24h
                .signWith(getKey())                 // On signe avec la clé secrète
                .compact();                         // On génère la chaîne de caractères finale
    }

    // 2. Extraction du username depuis le token
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // 3. Validation du token (est-il valide et non expiré ?)
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    // Méthode utilitaire pour décoder la clé Base64 du application.yml en objet cryptographique
    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}