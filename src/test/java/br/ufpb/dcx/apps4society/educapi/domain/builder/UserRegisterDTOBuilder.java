package br.ufpb.dcx.apps4society.educapi.domain.builder;

import br.ufpb.dcx.apps4society.educapi.dto.user.UserRegisterDTO;

public class UserRegisterDTOBuilder {

    private String name = "User";
    private String email = "user@educapi.com";
    private String password = "testpassword";

    public static UserRegisterDTOBuilder getInstance() {
        return new UserRegisterDTOBuilder();
    }

    public UserRegisterDTOBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserRegisterDTOBuilder name(String name) {
        this.name = name;
        return this;
    }

    public UserRegisterDTOBuilder password(String password){
        this.password = password;
        return this;
    }

    public UserRegisterDTO build() {
        return new UserRegisterDTO(this.name, this.email, this.password);
    }

}
