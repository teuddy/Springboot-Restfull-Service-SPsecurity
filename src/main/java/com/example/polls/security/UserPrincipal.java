package com.example.polls.security;

import com.example.polls.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

//  esta clase tendra instancias que se usaran en UserDetailsService para ser retornadas
// la informacion alojada en esta clase spirng security la utilizara para autenticaion y autorizacion
/*al utilizar UserDetails traucido a detalles de usuario o mejro dicho los datos del usuario
*hacemos un esquema de los detalles del usuario para que spring security pueda usarla*/
public class UserPrincipal implements UserDetails {

    private Long id;

    private String name;

    private String username;

    @JsonIgnore
    private String email;

    @JsonIgnore
    private String password;


    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long id, String name, String username, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }



    public static UserPrincipal create(User user){
        //--------Que es un GrantedAuthority?----------
        //-- son permisos o derechos , normalmente expresados en String, luego esos string dejaran identificar
        //los permisos y ver si tienen grant acces para acceder a algo


        //---------Que son Roles?-----------------------
        //los roles son permisos con una convencion al nombrarlos..
        //se dice que un rol es un GrantedAuthority que empieza con un prefijo Role_


        /*But still: a role is just an authority with a special ROLE_ prefix. So in Spring security 3
        @PreAuthorize("hasRole('ROLE_XYZ')") is the same as
         @PreAuthorize("hasAuthority('ROLE_XYZ')") and in Spring security
         4 @PreAuthorize("hasRole('XYZ')") is the same as
         @PreAuthorize("hasAuthority('ROLE_XYZ')").*/

        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role ->
                        new SimpleGrantedAuthority(role.getName().name())
                ).collect(Collectors.toList());


        return new UserPrincipal(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
