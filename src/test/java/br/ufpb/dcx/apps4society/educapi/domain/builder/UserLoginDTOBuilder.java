package br.ufpb.dcx.apps4society.educapi.domain.builder;

import br.ufpb.dcx.apps4society.educapi.dto.user.UserLoginDTO;

public class UserLoginDTOBuilder {
    private String email = "user@educapi.com";
    private String password = "testpassword";

    public static UserLoginDTOBuilder getInstance() {
        return new UserLoginDTOBuilder();
    }

    public UserLoginDTOBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserLoginDTOBuilder password(String password){
        this.password = password;
        return this;
    }

    public UserLoginDTO build() {
        return new UserLoginDTO(this.email, this.password);
    }
}
