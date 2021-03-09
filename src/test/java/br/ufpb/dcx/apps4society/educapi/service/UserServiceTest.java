package br.ufpb.dcx.apps4society.educapi.service;

import br.ufpb.dcx.apps4society.educapi.domain.User;
import br.ufpb.dcx.apps4society.educapi.domain.builder.UserBuilder;
import br.ufpb.dcx.apps4society.educapi.domain.builder.UserRegisterDTOBuilder;
import br.ufpb.dcx.apps4society.educapi.dto.user.UserDTO;
import br.ufpb.dcx.apps4society.educapi.dto.user.UserRegisterDTO;
import br.ufpb.dcx.apps4society.educapi.repositories.UserRepository;
import br.ufpb.dcx.apps4society.educapi.services.JWTService;
import br.ufpb.dcx.apps4society.educapi.services.UserService;
import br.ufpb.dcx.apps4society.educapi.services.exceptions.UserAlreadyExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private UserService service;

    private UserRegisterDTO userRegisterDTO = UserRegisterDTOBuilder.getInstance().build();
    private Optional<User> userOptional = Optional.ofNullable(UserBuilder.getInstance().build());


    @Test
    public void insertAUserTest() throws UserAlreadyExistsException {
        UserDTO response = service.insert(this.userRegisterDTO);
        Assertions.assertNull(response.getId()); // Como não é conectado ao banco de dados, o ID não é gerado
        Assertions.assertEquals(response.getName(), this.userRegisterDTO.getName());
        Assertions.assertEquals(response.getEmail(), this.userRegisterDTO.getEmail());
        Assertions.assertEquals(response.getPassword(), this.userRegisterDTO.getPassword());
    }

    @Test
    public void insertAUserAlreadyExistTest(){
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> {
            Mockito.when(this.userRepository.findByEmail(this.userRegisterDTO.getEmail())).thenReturn(this.userOptional);
            service.insert(this.userRegisterDTO);
        }, "There is already a user with this e-mail registered in the system!");
    }
}
