package br.ufpb.dcx.apps4society.educapi.unit.service;

import br.ufpb.dcx.apps4society.educapi.domain.User;
import br.ufpb.dcx.apps4society.educapi.dto.user.UserLoginDTO;
import br.ufpb.dcx.apps4society.educapi.repositories.UserRepository;
import br.ufpb.dcx.apps4society.educapi.response.LoginResponse;
import br.ufpb.dcx.apps4society.educapi.services.JWTService;
import br.ufpb.dcx.apps4society.educapi.services.exceptions.InvalidUserException;
import br.ufpb.dcx.apps4society.educapi.unit.domain.builder.UserBuilder;
import br.ufpb.dcx.apps4society.educapi.unit.util.FormatTo;
import br.ufpb.dcx.apps4society.educapi.util.Messages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class have only unit tests related to JWTService.
 *
 * @author Enos Teteo
 */
@ExtendWith(MockitoExtension.class)
public class JWTServiceTest {

    private final UserLoginDTO userLoginDTO = UserBuilder.anUser().buildUserLoginDTO();
    private final Optional<User> userOptional = UserBuilder.anUser().buildOptionalUser();
    private final String invalidToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYWlhd2VlZUB0ZXN0LmNvbSIsImV4cCI6MTYxNTM" +
            "5OTkyN30.1qNJIgwjlnm6YcZuIDFLZrQLs58qOwLFkCtXOcaUD-fQZyTa4usOMVgGa19Em_e8WdoXfnaJSv9O-c8IRp-C9Q";
    @Mock
    UserRepository userRepository;
    @InjectMocks
    JWTService service;

    /**
     * Before each test, sets the value of the "TOKEN_KEY" field in JWTService
     *
     * If this value is not set, the TOKEN_KEY will be null and generate an SignatureException in tests
     */
    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(service, "TOKEN_KEY", "it's a token key");
    }

    /**
     * Test the authenticate method.
     *
     * Verifying if the received token has the correct email
     * @throws InvalidUserException if the UseLoginDTO is not correctly mocked
     */
    @Test
    public void authenticateTest() throws InvalidUserException {
        Mockito.when(this.userRepository.findByEmailAndPassword(this.userLoginDTO.getEmail(), this.userLoginDTO.getPassword())).thenReturn(this.userOptional);

        LoginResponse response = this.service.authenticate(this.userLoginDTO);

        assertNotNull(response.getToken());
    }


    /**
     * Test the function authenticate without user in DB.
     *
     * When use an user who hasn't in the DB, the function retun the InvalidUserException
     */
    @Test
    public void authenticateUnregisterUserTest() {
        InvalidUserException invalidUser = assertThrows(InvalidUserException.class, () -> {
            this.service.authenticate(this.userLoginDTO);
        });

        assertNotNull(invalidUser);
    }

    /**
     * Test the function recoverUser with a valid token
     *
     * When use an token valid, the function return an Optional object, who contain the e-mail encrypted in token
     * @throws InvalidUserException
     */
    @Test
    public void recoverUserTest() throws InvalidUserException {
        Mockito.when(this.userRepository.findByEmailAndPassword(this.userLoginDTO.getEmail(), this.userLoginDTO.getPassword()))
                .thenReturn(this.userOptional);

        LoginResponse response = this.service.authenticate(userLoginDTO);
        String token = FormatTo.token(response.getToken());

        String userRecovered = this.service.recoverUser(token).get();

        assertEquals(userRecovered, this.userLoginDTO.getEmail());
    }

    /**
     * Test the function recoverUser when pass the param null
     *
     * When use the param null, the function need return an SecurityExpection
     */
    @Test
    public void revocerUserWithNullTokenTest() {
        Exception exception = assertThrows(SecurityException.class, () -> {
            this.service.recoverUser(null);
        });
        assertNotNull(exception);
    }

    /**
     * Test the function recoverUser when pass in the an expired token
     *
     * When set an token expired, the function need return an SecurityExpection, with message informing who the token
     * is invalid or expired
     */
    @Test
    public void revocerUserWithExpiredTokenTest() {
        Exception exception = assertThrows(SecurityException.class, () -> {
            this.service.recoverUser(invalidToken);
        });
        assertEquals(Messages.TOKEN_INVALID_OR_EXPIRED, exception.getMessage());
    }
}
