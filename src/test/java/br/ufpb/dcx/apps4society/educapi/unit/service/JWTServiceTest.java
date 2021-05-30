package br.ufpb.dcx.apps4society.educapi.unit.service;

import br.ufpb.dcx.apps4society.educapi.domain.User;
import br.ufpb.dcx.apps4society.educapi.dto.user.UserLoginDTO;
import br.ufpb.dcx.apps4society.educapi.repositories.UserRepository;
import br.ufpb.dcx.apps4society.educapi.response.LoginResponse;
import br.ufpb.dcx.apps4society.educapi.services.JWTService;
import br.ufpb.dcx.apps4society.educapi.services.exceptions.InvalidUserException;
import br.ufpb.dcx.apps4society.educapi.unit.domain.builder.UserBuilder;
import br.ufpb.dcx.apps4society.educapi.util.Messages;
import io.jsonwebtoken.Jwts;
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
     * Format String to token format adding "Bearer " before the String
     * Example:
     *      FormatTo.token("any String");
     *      // return "Bearer any String"
     *
     * @param token any String
     * @return token formatted
     */
    private String formatToToken(String token) {
        return "Bearer " + token;
    }

    /**
     * Extract the content of the token
     * @param token any String encrypted using JWTs with value "it's a token key"
     * @return subject in the token body
     */
    private String extractSubjectFromToken(String token) {
        return Jwts.parser().setSigningKey("it's a token key").parseClaimsJws(token).getBody().getSubject();
    }

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
        Mockito.when(this.userRepository.findByEmailAndPassword(this.userLoginDTO.getEmail(), this.userLoginDTO.getPassword()))
                .thenReturn(this.userOptional);

        LoginResponse response = this.service.authenticate(this.userLoginDTO);
        String subject = extractSubjectFromToken(response.getToken());

        assertNotNull(response.getToken());
        assertEquals(userLoginDTO.getEmail(), subject);
    }

    /**
     * Test exception in the authenticate method.
     *
     * Verifying if an exception is thrown when try to authenticate without configuring an user in DB
     */
    @Test
    public void authenticateUnregisterUserTest() {
        assertThrows(InvalidUserException.class, () -> {
            this.service.authenticate(this.userLoginDTO);
        });
    }

    /**
     * Test the recoverUser method with a valid token
     *
     * Verifying if the email returned by recoverUser is equals to the email contained in the token
     * @throws InvalidUserException if the UserLoginDTO not correctly mocked
     */
    @Test
    public void recoverUserTest() throws InvalidUserException {
        Mockito.when(this.userRepository.findByEmailAndPassword(this.userLoginDTO.getEmail(), this.userLoginDTO.getPassword()))
                .thenReturn(this.userOptional);

        LoginResponse response = this.service.authenticate(userLoginDTO);
        String token = formatToToken(response.getToken());

        String userRecovered = this.service.recoverUser(token).get();

        assertEquals(this.userLoginDTO.getEmail(), userRecovered);
    }

    /**
     * Test the recoverUser method with a null token
     *
     * Verifying if an exception is thrown when try to recoverUser with a null param
     */
    @Test
    public void revocerUserWithNullTokenTest() {
        assertThrows(SecurityException.class, () -> {
            this.service.recoverUser(null);
        });
    }

    /**
     * Test the recoverUser method with an expired token
     *
     * Verifying if an exception is thrown and have the correctly message when try to recoverUser with an expired token
     */
    @Test
    public void revocerUserWithExpiredTokenTest() {
        Exception exception = assertThrows(SecurityException.class, () -> {
            this.service.recoverUser(invalidToken);
        });
        assertEquals(Messages.TOKEN_INVALID_OR_EXPIRED, exception.getMessage());
    }
}
