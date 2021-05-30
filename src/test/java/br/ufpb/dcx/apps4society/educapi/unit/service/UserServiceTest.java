package br.ufpb.dcx.apps4society.educapi.unit.service;

import br.ufpb.dcx.apps4society.educapi.domain.User;
import br.ufpb.dcx.apps4society.educapi.dto.user.UserDTO;
import br.ufpb.dcx.apps4society.educapi.dto.user.UserRegisterDTO;
import br.ufpb.dcx.apps4society.educapi.repositories.UserRepository;
import br.ufpb.dcx.apps4society.educapi.services.JWTService;
import br.ufpb.dcx.apps4society.educapi.services.UserService;
import br.ufpb.dcx.apps4society.educapi.services.exceptions.InvalidUserException;
import br.ufpb.dcx.apps4society.educapi.services.exceptions.UserAlreadyExistsException;
import br.ufpb.dcx.apps4society.educapi.unit.domain.builder.UserBuilder;
import br.ufpb.dcx.apps4society.educapi.util.Messages;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class have only unit tests reference to UserService
 *
 * @author Enos Tetéo
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private final UserRegisterDTO userRegisterDTO = UserBuilder.anUser().buildUserRegisterDTO();
    private final Optional<User> userOptional = UserBuilder.anUser().buildOptionalUser();

    @Mock
    private UserRepository userRepository;
    @Mock
    private JWTService jwtService;
    @InjectMocks
    private UserService service;

    /**
     * Test the insert method
     *
     * Verifying if when insert an UserRegisterDTO, the response has contain the data the UserRegisterDTO
     *
     * @throws UserAlreadyExistsException if have an UserRegisterDTO mocked with the equals emails
     */
    @Test
    public void insertAUserTest() throws UserAlreadyExistsException {
        UserDTO response = service.insert(this.userRegisterDTO);

        assertEquals(response.getName(), this.userRegisterDTO.getName());
        assertEquals(response.getEmail(), this.userRegisterDTO.getEmail());
        assertEquals(response.getPassword(), this.userRegisterDTO.getPassword());
    }

    /**
     * Test the insert method with already email registered
     *
     * Verifying if when insert an UserRegisterDTO with email registered in the system, the response is UserAlreadyExistsException
     */
    @Test
    public void insertAUserAlreadyExistTest() {
        Mockito.when(this.userRepository.findByEmail(this.userRegisterDTO.getEmail())).thenReturn(this.userOptional);

        Exception exception = assertThrows(UserAlreadyExistsException.class, () -> {
            service.insert(this.userRegisterDTO);
        });

        assertEquals(Messages.USER_ALREADY_EXISTS, exception.getMessage());
    }


    /**
     * Test the find method
     *
     * Verifying if when find an User using your user token, the response is User
     *
     * @throws InvalidUserException if have not an UserDTO mocked with the email used in the test
     */
    @Test
    public void findAUserTest() throws InvalidUserException {
        Mockito.when(jwtService.recoverUserEmailByToken("token valid")).thenReturn(Optional.of("teste@teste.com"));
        Mockito.when(userRepository.findByEmail("teste@teste.com")).thenReturn(userOptional);

        User response = service.find("token valid");

        assertEquals(userOptional.get().getEmail(), response.getEmail());
        assertEquals(userOptional.get().getName(), response.getName());
        assertEquals(userOptional.get().getPassword(), response.getPassword());
    }

    /**
     * Test the find method with a null token
     *
     * Verifying if when use a find method with a null token, the response is InvalidUserException
     */
    @Test
    public void findAInvalidUserTest() {
        Exception exception = assertThrows(InvalidUserException.class, () -> {
            service.find(null);
        });
        assertEquals(Messages.INVALID_USER_CHECK_THE_TOKEN, exception.getMessage());
    }

    /**
     * Test the find method with a user that is not registered in system
     *
     * Verifying if when use a find method with a token valid and email not registered in the system, the response is Exception
     */
    @Test
    public void findANotRegisteredUserTest() throws InvalidUserException {
        Mockito.when(jwtService.recoverUserEmailByToken("token valid")).thenReturn(Optional.of("teste@teste.com"));

        Exception exception = assertThrows(InvalidUserException.class, () -> {
            User response = service.find("token valid");
        });
        assertEquals(Messages.INVALID_USER_CHECK_THE_EMAIL, exception.getMessage());
    }

    /**
     * Test the update method
     *
     * Verifying if when update an User, the response has contain the UserDTO edited
     *
     * @throws InvalidUserException if have not mocked the find method
     */
    @Test
    public void updateAUserTest() throws InvalidUserException {
        Mockito.when(jwtService.recoverUserEmailByToken("token valid")).thenReturn(Optional.of("teste@teste.com"));
        Mockito.when(userRepository.findByEmail("teste@teste.com")).thenReturn(userOptional);

        userRegisterDTO.setName("testUpdate");
        userRegisterDTO.setEmail("testUpdate@mail.com");
        userRegisterDTO.setPassword("testPass");

        assertNotEquals(userOptional.get().getName(), userRegisterDTO.getName());
        assertNotEquals(userOptional.get().getEmail(), userRegisterDTO.getEmail());
        assertNotEquals(userOptional.get().getPassword(), userRegisterDTO.getPassword());

        UserDTO response = service.update("token valid", userRegisterDTO);


        assertEquals("testUpdate", response.getName());
        assertEquals("testUpdate@mail.com", response.getEmail());
        assertEquals("testPass", response.getPassword());

        assertEquals(userOptional.get().getId(), response.getId());
    }

    /**
     * Test the delete method
     *
     * Verifying if when delete an User, the response has contain the UserDTO deleted
     *
     * @throws InvalidUserException
     */
    @Test
    public void deleteAUserTest() throws InvalidUserException {
        Mockito.when(jwtService.recoverUserEmailByToken("token valid")).thenReturn(Optional.of("teste@teste.com"));
        Mockito.when(userRepository.findByEmail("teste@teste.com")).thenReturn(userOptional);

        UserDTO response = service.delete("token valid");

        assertEquals(userOptional.get().getName(), response.getName());
        assertEquals(userOptional.get().getEmail(), response.getEmail());
        assertEquals(userOptional.get().getPassword(), response.getPassword());
        assertEquals(userOptional.get().getId(), response.getId());

    }

}
