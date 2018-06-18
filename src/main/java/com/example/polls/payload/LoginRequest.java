package com.example.polls.payload;


import javax.validation.constraints.NotBlank;

/*Un payload son lso datos los cuales seran enviados o recibidos entre el servidor y el cliente
* esta clase loginRequest es un payload porque se usara para que el el lciente envie datos(loginRequets) hasta el servidor.
*
*en un restaurante cuando un cliente hace un pedido y luego el pedido es recibido este le evuelve la comida en este ejemplo la comdia seria un Payload
* */
public class LoginRequest {

    @NotBlank
    private String usernameOremail;

    @NotBlank
    private String password;

    public String getUsernameOremail() {
        return usernameOremail;
    }

    public void setUsernameOremail(String usernameOremail) {
        this.usernameOremail = usernameOremail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
