package br.ufpb.dcx.apps4society.educapi.service;

import br.ufpb.dcx.apps4society.educapi.domain.User;
import br.ufpb.dcx.apps4society.educapi.domain.builder.UserBuilder;
import br.ufpb.dcx.apps4society.educapi.dto.user.UserLoginDTO;
import br.ufpb.dcx.apps4society.educapi.repositories.UserRepository;
import br.ufpb.dcx.apps4society.educapi.response.LoginResponse;
import br.ufpb.dcx.apps4society.educapi.services.JWTService;
import br.ufpb.dcx.apps4society.educapi.services.exceptions.InvalidUserException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class JWTServiceTest {

    private final UserLoginDTO userLoginDTO = UserBuilder.anUser().buildUserLoginDTO();
    private final Optional<User> userOptional = UserBuilder.anUser().buildOptionalUser();

    @Mock
    UserRepository userRepository;

    @InjectMocks
    JWTService service;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(service, "TOKEN_KEY", "it's a token key");
    }

    @Test
    public void authenticateTest() throws InvalidUserException {
        Mockito.when(this.userRepository.findByEmailAndPassword(this.userLoginDTO.getEmail(), this.userLoginDTO.getPassword())).thenReturn(this.userOptional);

        LoginResponse response = this.service.authenticate(this.userLoginDTO);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getToken());
    }

    @Test
    public void authenticateUnregisterUserTest() {
        Assertions.assertThrows(InvalidUserException.class, () -> {
            this.service.authenticate(this.userLoginDTO);
        });
    }

    @Test
    public void recoverUserTest() throws InvalidUserException {
        Mockito.when(this.userRepository.findByEmailAndPassword(this.userLoginDTO.getEmail(), this.userLoginDTO.getPassword())).thenReturn(this.userOptional);

        LoginResponse response = this.service.authenticate(userLoginDTO);
        String token = "Bearer " + response.getToken();

        String userRecovered = this.service.recoverUser(token).get();

        Assertions.assertEquals(userRecovered, this.userLoginDTO.getEmail());
    }

    @Test
    public void revocerUserWithInvalidTokenTest() {
        Assertions.assertThrows(SecurityException.class, () -> {
            this.service.recoverUser("invalid token");
        });
    }
}
