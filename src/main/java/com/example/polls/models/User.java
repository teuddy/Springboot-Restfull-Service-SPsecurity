package com.example.polls.models;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity//Esto es una entidad
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User  extends DateAudit{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// le indicamos a jpa que la basededatos debe asignarle una llave primaria usando una columda de identidad de la base de datos
    private Long id;

    @NotBlank// El campo no puede ser nulo y el largo del string debe ser mayor que sero
    //@NotNull // not null dice quue no puede ser nulo peor puede estar vacio.
    @Size(max = 40)
    private String name;

    @NotBlank
    @Size(max = 15)
    private String username;

    @NaturalId// ademas del @Id puedes definir que una columna es un Id Natural:
    @NotBlank
    @Size(max = 40)
    @Email// el email tiene que estar bien hecho. le decimos a jpa que no acepte tonterias de email mal escritos
    private String email;

    @NotBlank
    @Size(max = 50)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    /*Now when you load a University from the database, JPA loads its id, name, and address fields for you. But you have two options for students: to load it together with the rest of the fields (i.e. eagerly) or to load it on-demand (i.e. lazily) when you call the university's getStudents() method.
    When a university has many students it is not efficient to load all of its students with it when they are not needed. So in suchlike cases, you can declare that you want students to be loaded when they are actually needed. This is called lazy loading.*/
    @JoinTable(name = "user_roles",// nombre de la tabla es user-roles
            joinColumns = @JoinColumn(name = "user_id"),// le idicamos a los roles que se unira con la tabla roles por el @id y ese id se llama #user_id
            inverseJoinColumns = @JoinColumn(name = "role_id"))/////////////////////////////
    private Set<Roles> roles = new HashSet<>();

    public User(){

    }

    public User(String name, String username, String email,String password, Set<Roles> roles) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Roles> getRoles() {
        return roles;
    }

    public void setRoles(Set<Roles> roles) {
        this.roles = roles;
    }
}
