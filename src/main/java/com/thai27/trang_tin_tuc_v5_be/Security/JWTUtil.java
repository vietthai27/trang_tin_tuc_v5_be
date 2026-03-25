package com.thai27.trang_tin_tuc_v5_be.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Service
public class JWTUtil {

    @Value("${jwt.private-key}")
    private String privateKey;

    @Value("${jwt.public-key}")
    private String publicKey;

    private static final int expireInMs = 86400000;

    public String generate(Authentication userData) throws Exception {
        return Jwts
                .builder()
                .setSubject(userData.getPrincipal().toString())
                .setIssuer("thai27")
                .claim("roles",userData.getAuthorities())
                .claim("username", userData.getPrincipal())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireInMs))
                .signWith(loadPrivateKey(), SignatureAlgorithm.RS256).compact();
    }

    public boolean isTokenValid(String token) throws Exception {
        return getUsername(token) != null && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) throws Exception {
        return getClaims(token).getExpiration().before(new Date());
    }

    public String getUsername(String token) throws Exception {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    public Claims getClaims(String token) throws Exception {
        return Jwts.parserBuilder().setSigningKey(loadPublicKey()).build().parseClaimsJws(token).getBody();
    }

    public PrivateKey loadPrivateKey() throws Exception {
        String key = privateKey
                .replaceAll("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    public PublicKey loadPublicKey() throws Exception {
        String key = publicKey
                .replaceAll("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    public String readKey(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        return new String(resource.getInputStream().readAllBytes());
    }
}
