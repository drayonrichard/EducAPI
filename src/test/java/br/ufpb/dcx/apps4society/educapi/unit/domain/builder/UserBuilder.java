package br.ufpb.dcx.apps4society.educapi.unit.domain.builder;

import br.ufpb.dcx.apps4society.educapi.domain.User;
import br.ufpb.dcx.apps4society.educapi.dto.user.UserLoginDTO;
import br.ufpb.dcx.apps4society.educapi.dto.user.UserRegisterDTO;

import java.util.Optional;

public class UserBuilder {

    private Long id = null;
    private String name = "User";
    private String email = "user@educapi.com";
    private String password = "testpassword";

    public static UserBuilder anUser() {
        return new UserBuilder();
    }

    public UserBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public Optional<User> buildOptionalUser() {
        return Optional.ofNullable(new User(this.id, this.name, this.email, this.password));
    }

    public UserRegisterDTO buildUserRegisterDTO() {
        return new UserRegisterDTO(this.name, this.email, this.password);
    }

    public UserLoginDTO buildUserLoginDTO() {
        return new UserLoginDTO(this.email, this.password);
    }

}
