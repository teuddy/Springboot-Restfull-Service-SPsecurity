package com.example.polls.config;


import com.example.polls.security.UserPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class AuditingConfig {

    @Bean
    public AuditorAware<Long> auditorProvider(){
        return new Claseencargadadeagarrarelusuarioconcurrenteparasaberquienhizotaloperacion();  //leugo se envia ese usuario actual para saber quien fue el que hizo el cambio
    }

}


class Claseencargadadeagarrarelusuarioconcurrenteparasaberquienhizotaloperacion implements  AuditorAware<Long>{// se agarra ell usuario actual

    @Override
    public Optional<Long> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();// me devuelve el usuario actualmente autenticado

        if(authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken){
            return Optional.empty();
        }
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return Optional.ofNullable(userPrincipal.getId());
    }
}

