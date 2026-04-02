package com.thai27.trang_tin_tuc_v5_be.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

@Service
public class JWTUtil {

    @Value("${jwt.private-key}")
    private String privateKeyValue;

    @Value("${jwt.public-key}")
    private String publicKeyValue;

    public PrivateKey loadPrivateKey() throws Exception {
        String keyContent;
        if (privateKeyValue.startsWith("file:")) {
            Path path = Paths.get(privateKeyValue.replace("file:", ""));
            keyContent = Files.readString(path);
        }
        else {
            keyContent = privateKeyValue;
        }
        keyContent = keyContent
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(keyContent);
        return KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(decoded));
    }

    public PublicKey loadPublicKey() throws Exception {
        String keyContent;
        if (publicKeyValue.startsWith("file:")) {
            Path path = Paths.get(publicKeyValue.replace("file:", ""));
            keyContent = Files.readString(path);
        }
        else {
            keyContent = publicKeyValue;
        }
        keyContent = keyContent
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(keyContent);
        return KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(decoded));
    }

    private static final int expireInMs = 86400000;

    public String generate(Authentication userData) throws Exception {
        return Jwts
                .builder().subject(Objects.requireNonNull(userData.getPrincipal()).toString()).issuer("thai27")
                .claim("roles", userData.getAuthorities())
                .claim("username", userData.getPrincipal())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expireInMs))
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
        return Jwts.parser()
                .verifyWith(loadPublicKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
