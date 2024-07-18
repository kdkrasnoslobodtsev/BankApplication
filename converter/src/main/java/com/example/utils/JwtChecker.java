package com.example.utils;

import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.clients.KeycloakClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class JwtChecker {
    private final KeycloakClient keycloakClient;

    public void verifyToken(String token) {
        try {
            String modulusBase64 = keycloakClient.getPublicKey("n");
            String exponentBase64 = keycloakClient.getPublicKey("e");

            byte[] modulusBytes = Base64.getUrlDecoder().decode(modulusBase64);
            byte[] exponentBytes = Base64.getUrlDecoder().decode(exponentBase64);

            BigInteger modulus = new BigInteger(1, modulusBytes);
            BigInteger exponent = new BigInteger(1, exponentBytes);

            RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PublicKey rsaPublicKey = factory.generatePublic(spec);

            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) rsaPublicKey, null);
            JWTVerifier verifier = JWT.require(algorithm).build();

            DecodedJWT jwt = verifier.verify(token.substring(7));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JWT token");
        }
    }
}
