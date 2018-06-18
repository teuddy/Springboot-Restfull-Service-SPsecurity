package com.example.polls.repositories;

import com.example.polls.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long> { //jpa iplementa todas las funciones de
    //crudrepository y paginationsortingrepository

    Optional<User> findByEmail(String email);// encuentra por email , devuelve un optional de tipo usuario

    Optional<User> findByUsernameOrEmail(String username, String email);// encuentra por nombre de usuario o email

    List<User> findByIdIn(List<Long> userIds);

    Optional<User> findByUsername(String username);// encuentral por nombre de usuario

    Boolean existsByUsername(String username);// existe un usuario con este nonmbre de ser cierto devuelveme true or false

    Boolean existsByEmail(String email);// existe este email? enviame true or false
}
