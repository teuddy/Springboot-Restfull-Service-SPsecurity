package com.example.polls.security;


import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {
    //Spring security proveee una anotacion llamada @AuthenticationPrincipal pra acceder a el ussuario autenticado concurrente  en los controllers
}
