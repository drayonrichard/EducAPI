package br.ufpb.dcx.apps4society.educapi.unit.resource;

import br.ufpb.dcx.apps4society.educapi.domain.User;
import br.ufpb.dcx.apps4society.educapi.dto.user.UserDTO;
import br.ufpb.dcx.apps4society.educapi.dto.user.UserRegisterDTO;
import br.ufpb.dcx.apps4society.educapi.resources.UserResource;
import br.ufpb.dcx.apps4society.educapi.services.UserService;
import br.ufpb.dcx.apps4society.educapi.services.exceptions.InvalidUserException;
import br.ufpb.dcx.apps4society.educapi.services.exceptions.UserAlreadyExistsException;
import br.ufpb.dcx.apps4society.educapi.unit.domain.builder.UserBuilder;
import br.ufpb.dcx.apps4society.educapi.util.Messages;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class have only unit tests reference to UserResource
 *
 * @author Enos Tetéo
 */
@WebMvcTest(controllers = UserResource.class)
public class UserResourceTest {

    private final UserRegisterDTO userRegisterDTO = UserBuilder.anUser().buildUserRegisterDTO();
    private final String uriBase = "/v1/api";
    private final User user = UserBuilder.anUser().buildUser();
    private final Optional<User> optionalUser = UserBuilder.anUser().buildOptionalUser();
    private final UserDTO userDTO = UserBuilder.anUser().withId(1L).buildUserDTO();
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService service;

    /**
     * Test the find method
     *
     * Verifying if when find an User using your user token, the response is an json with the User data
     * @throws Exception if have not an token mocked with the token used in the test
     */
    @Test
    public void findAnUserTest() throws Exception {
        Mockito.when(service.find("token")).thenReturn(user);

        mockMvc.perform(get(uriBase + "/auth/users")
                .header("Authorization", "token"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(user)));
    }

    /**
     * Test the find method with a token that return an InvalidUserException
     *
     * Verifying if when use a find method with an invalid token, the response is forbidden
     * @throws Exception if have not an method find in UserService mocked
     */
    @Test
    public void findAInvalidUserTest() throws Exception {
        Mockito.when(service.find("token")).thenThrow(new InvalidUserException(Messages.INVALID_USER_CHECK_THE_TOKEN));

        mockMvc.perform(get(uriBase + "/auth/users")
                .header("Authorization", "token"))
                .andExpect(status().isForbidden());
    }

    /**
     * Test the insert method
     *
     * Verifying if when insert an json with an UserRegisterDTO,
     * the response has contain an json with the data the UserRegisterDTO
     *
     * @throws Exception if have not an method find in UserService mocked
     */
    @Test
    public void insertAnUserTest() throws Exception {
        Mockito.when(service.insert(any(UserRegisterDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post(uriBase + "/users").contentType("application/json")
                .content(objectMapper.writeValueAsString(userRegisterDTO))).andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(userDTO))).andReturn();
    }

    /**
     * Test the insert method with UserAlreadyExistsException
     *
     * Verifying if when insert an json with an UserRegisterDTO that already exist in the system,
     * the response is not content
     *
     * @throws Exception if have not an user mocked
     */
    @Test
    public void insertAnUserAlreadyExistTest() throws Exception {
        Mockito.when(service.insert(any(UserRegisterDTO.class))).thenThrow(new UserAlreadyExistsException(Messages.USER_ALREADY_EXISTS));

        mockMvc.perform(post(uriBase + "/users").contentType("application/json")
                .content(objectMapper.writeValueAsString(userRegisterDTO))).andExpect(status().isNoContent());
    }
}
