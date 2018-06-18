package com.example.polls.security;

import com.example.polls.models.User;
import com.example.polls.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service// anotacion que se desprende de @component indica que aqui se hac capa de negocio, bussiness logic
public class CustomUserDetailsService implements UserDetailsService {// Es usada para cargar cargar datos especificos del usuario.


    @Autowired
    private UserRepository userRepository;// User Entity Dao

    /*EntityManager
T       he purpose of the EntityManager is to interact with the persistence context. The persistence context will then manage entity instances and their associated lifecycle. This was covered in my blog post on the JPA Entity Lifecycle.*/
    @Override
    @Transactional
    // Una Transaccion es una interaccion con una estructura de datos compleja.
    /*that does @Transactional mean?
     One of the key points about @Transactional is that there are two separate concepts to consider, each with it’s own scope and life cycle:

     the persistence context
     the database transaction
     The transactional annotation itself defines the scope of a single database transaction. The database transaction happens inside the scope of a persistence context.

     The persistence context is in JPA the EntityManager, implemented internally using an Hibernate Session (when using Hibernate as the persistence provider).

     The persistence context is just a synchronizer object that tracks the state of a limited set of Java objects and makes sure that changes on those objects are eventually persisted back into the database.

     This is a very different notion than the one of a database transaction. One Entity Manager can be used across several database transactions, and it actually often is.*/
    //-----------------------------------------------------------------------------------------------------------


    //Este metodo localiza al usuario basado en su nombre de usuario.
    //Spring internamente usa el objeto retornado UserDetils(UserPrincipal) para verificaro contrasena y roles..
    //Este metodo se usa para el login..
    public UserDetails loadUserByUsername(String UsernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(UsernameOrEmail,UsernameOrEmail).orElseThrow(()->
                    new UsernameNotFoundException("could not find user with username or email" + UsernameOrEmail)
        );

        return UserPrincipal.create(user);// si se encontro el usuario pues creame todos los detalles de ese usuario.
    }


    // This method is used by JWTAuthenticationFilter
    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );

        return UserPrincipal.create(user);
    }
}


