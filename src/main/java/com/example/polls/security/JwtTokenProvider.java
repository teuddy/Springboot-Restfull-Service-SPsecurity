package com.example.polls.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.util.Date;


//Aqui creamos el cuerpo de nuestro JWT
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;


    public String generateToken(Authentication authentication) {//Authentification extiende de UserPrincipal por eso se peude llamar a esa clase y obtener nuestro Principal.

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();// tomando la hora de el instante.
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);//la fecha de expiracion del token es igual a la hora actual mas el tiempo de expiracion


        return Jwts.builder()//creando el JWT CLAIMSjt
                .setSubject(Long.toString(userPrincipal.getId()))// el subject es informacion del usuario, en esta ocacion se le manda el id
                .setIssuedAt(new Date())//paso un error a la hora de
                .setExpiration(expiryDate)// cuando expira el token
                .signWith(SignatureAlgorithm.HS512, jwtSecret)// firmar wl jwt con una encriptaccion y tambien con nuestra firma
                .compact();//compactalo a un url string seguro
    }


    public Long getUserIdFromJWT(String token) {// El Payload o Tambien llamado JWT CLAIMS es la informacion que queremos transmitir acerca de nuestro token
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)// poner nuestro sello de que
                .parseClaimsJws(token)//pasame el claims del token que te pase
                .getBody();//obten el cuerpo del jwt

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {//Validemos el token
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}