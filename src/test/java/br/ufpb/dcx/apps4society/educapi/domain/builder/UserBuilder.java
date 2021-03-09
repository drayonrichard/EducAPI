package br.ufpb.dcx.apps4society.educapi.domain.builder;

import br.ufpb.dcx.apps4society.educapi.domain.User;

public class UserBuilder {

    private Long id = null;
    private String name = "User";
    private String email = "user@educapi.com";
    private String password = "testpassword";

    public static UserBuilder getInstance() {
        return new UserBuilder();
    }

    public UserBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder name(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder password(String password){
        this.password = password;
        return this;
    }

    public User build() {
        return new User(this.id, this.name, this.email, this.password);
    }

}
