package com.sinchan.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtility {

    @Value("jwt_secret")
    private String secret;

    private final String TOKEN_SUBJECT = "REACT TOKEN";
    private final String TOKEN_ISSUER = "KAMLESH-BAVISKAR" ;

    public String generateToken(String username)throws IllegalArgumentException, JWTCreationException {

        return JWT.create()
                .withSubject(TOKEN_SUBJECT)
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withIssuer(TOKEN_ISSUER)
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateToken(String token)throws JWTVerificationException {

        JWTVerifier tokenVerifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject(TOKEN_SUBJECT)
                .withIssuer(TOKEN_ISSUER)
                .build();

        DecodedJWT decodedJWTToken = tokenVerifier.verify(token);

        return decodedJWTToken.getClaim("username").asString();
    }
}
