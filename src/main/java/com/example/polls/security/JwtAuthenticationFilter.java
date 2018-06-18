package com.example.polls.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//FILTRO QUE AGARRARA EL JWT

public class JwtAuthenticationFilter  extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider JwtTokenProvider; // clase que validara , generara Jwt Tokens

    @Autowired
    private CustomUserDetailsService customUserDetailsService;// servicio  que se encarga de SignUp an user y cargar usuarios para authentificacion


    @Override
    protected void doFilterInternal(HttpServletRequest Request, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {//

        try{

            String jwt = getJwtFromRequest(Request);
            if(StringUtils.hasText(jwt)  && JwtTokenProvider.validateToken(jwt)){
                Long userid = JwtTokenProvider.getUserIdFromJWT(jwt);// obtener el id de ese token

                UserDetails userDetails = customUserDetailsService.loadUserById(userid);//buscar ese usuario con el id del jwt token en la base de datos

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(Request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }catch (Exception hey){

        }
        filterChain.doFilter(Request,httpServletResponse);
    }




    private String getJwtFromRequest(HttpServletRequest request){
        //Que es Bearer?
        //cuando el usuario hace un request al servidor por un token enviando un usuario y contrasena
        //el servidor retorna dos cosas un access token y un refresh token
        //el acces token es un token que se tendra que poner a todos los headers para identificarse como un usuario en concreto.
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7,bearerToken.length());// se obtiene el bearer y se retorna
        }
        return null;
    }
}
