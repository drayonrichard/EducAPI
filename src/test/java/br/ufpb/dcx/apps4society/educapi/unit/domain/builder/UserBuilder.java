package br.ufpb.dcx.apps4society.educapi.unit.domain.builder;

import br.ufpb.dcx.apps4society.educapi.domain.User;
import br.ufpb.dcx.apps4society.educapi.dto.user.UserLoginDTO;
import br.ufpb.dcx.apps4society.educapi.dto.user.UserRegisterDTO;

import java.util.Optional;

/**
 * Create instances of UserBuilder, UserRegisterDTO, UserLoginDTO or OptionalUser classes
 * Can create an instances with custom or default data
 * This class used an adaptation of Test Data Builder pattern and Builder pattern
 *
 * Example 1:
 *      UserLoginDTO userLoginDTO = UserBuilder.anUser().buildUserLoginDTO();
 *      This returns UserLoginDTO with email = "user@educapi.com" and password = "testpassword";
 *
 * Example 2:
 *      UserLoginDTO userLoginDTO = UserBuilder.anUser().withEmail("foo@bar.com").buildUserLoginDTO();
 *      This returns UserLoginDTO with email = "foo@bar.com" and password = "testpassword";
 *
 * @author Enos Teteo
 */
public class UserBuilder {

    private Long id = null;
    private String name = "User";
    private String email = "user@educapi.com";
    private String password = "testpassword";

    /**
     * Init a new UserBuilder with default values that can personalized with others methods
     * Is similar to getInstance in the Builder pattern
     *
     * Note: id by default is null but you can change it using the function .withId("put your id").
     * @return a new User Builder
     */
    public static UserBuilder anUser() {
        return new UserBuilder();
    }

    /**
     * Changes the id of this UserBuilder
     * id is an identification, and in class User is only
     *
     * @param id type Long; example: 1L
     * @return UserBuilder with id changed
     */
    public UserBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Changes the email of this UserBuilder
     * email is any email with type String
     * But if you use to test, can use any string
     *
     * @param email type String; example: "foo@bar.com"
     * @return UserBuilder with email changed
     */
    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * Changes the name of this UserBuilder
     * name is your name or your object name
     *
     * @param name type String; Example: "test name"
     * @return UserBuilder with name changed
     */
    public UserBuilder withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Changes the password of this UserBuilder
     * password is an secret key usually used in authentication in systems
     *
     * @param password type String; Example: "testpass"
     * @return UserBuilder with password changed
     */
    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * Generate an Optional object containing an User using custom or default data
     * Example 1:
     *      UserBuilder.anUser().buildOptionalUser();
     *      This returns an Optional<User> with default data
     *
     * Example 2:
     *      UserBuilder.anUser().withName("Optional User").buildOptionalUser();
     *      This returns an Optional<User> with all data default, but with name "Optional User"
     *
     * @return Optional<User>
     */
    public Optional<User> buildOptionalUser() { return Optional.of(new User(this.id, this.name, this.email, this.password)); }

    /**
     * Generate an UserRegisterDTO object containing custom or default data
     * Example 1:
     *      UserBuilder.anUser().buildUserRegisterDTO();
     *      This returns an UserRegisterDTO with default data
     *
     * Example 2:
     *      UserBuilder.anUser().withName("User Register DTO").buildUserRegisterDTO();
     *      This returns an UserRegisterDTO with all data default, but with name "User Register DTO"
     *
     * @return UserRegisterDTO
     */
    public UserRegisterDTO buildUserRegisterDTO() {
        return new UserRegisterDTO(this.name, this.email, this.password);
    }

    /**
     * Generate an UserLoginDTO object containing custom or default data
     * Example 1:
     *      UserBuilder.anUser().buildUserLoginDTO();
     *      This returns an UserLoginDTO with default data
     *
     * Example 2:
     *      UserBuilder.anUser().withName("User Login DTO").buildUserLoginDTO();
     *      This returns an UserLoginDTO with all data default, but with name "User Login DTO"
     *
     * @return UserLoginDTO
     */
    public UserLoginDTO buildUserLoginDTO() {
        return new UserLoginDTO(this.email, this.password);
    }
}
