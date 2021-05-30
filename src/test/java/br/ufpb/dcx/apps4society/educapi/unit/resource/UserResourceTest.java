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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

        mockMvc.perform(get(uriBase + "/auth/users").characterEncoding("UTF-8")
                .header("Authorization", "token"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(user)));
    }

    /**
     * Test the find method with a token that return an InvalidUserException
     *
     * Verifying if when use a find method with an invalid token, the response is forbidden
     * @throws Exception if have not an find method in UserService mocked
     */
    @Test
    public void findAInvalidUserTest() throws Exception {
        Mockito.when(service.find("token")).thenThrow(new InvalidUserException(Messages.INVALID_USER_CHECK_THE_TOKEN));

        mockMvc.perform(get(uriBase + "/auth/users").characterEncoding("UTF-8")
                .header("Authorization", "token"))
                .andExpect(status().isForbidden());
    }

    /**
     * Test the insert method
     *
     * Verifying if when insert an json with an UserRegisterDTO,
     * the response has contain an json with the data the UserRegisterDTO
     *
     * @throws Exception if have not an find method in UserService mocked
     */
    @Test
    public void insertAnUserTest() throws Exception {
        Mockito.when(service.insert(any(UserRegisterDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post(uriBase + "/users").contentType("application/json").characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(userRegisterDTO))).andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(userDTO)));
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

        mockMvc.perform(post(uriBase + "/users").contentType("application/json").characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(userRegisterDTO))).andExpect(status().isNoContent());
    }

    /**
     * Test the update method
     *
     * Verifying if when update an json with an UserRegisterDTO and passing an token in the Autorization,
     * the response has contain an json with the UserDTO edited
     *
     * @throws Exception if have not mocked the update method in UserService
     */
    @Test
    public void updateAnUserTest() throws Exception {
        Mockito.when(service.update(any(), any(UserRegisterDTO.class))).thenReturn(userDTO);

        mockMvc.perform(put(uriBase + "/auth/users").contentType("application/json").characterEncoding("UTF-8")
                .header("Authorization", "token")
                .content(objectMapper.writeValueAsString(userRegisterDTO)))
                .andExpect(status().isOk()).andExpect(content()
                .string(objectMapper.writeValueAsString(userDTO)));
    }

    /**
     * Test the update method with a token that return an InvalidUserException
     *
     * Verifying if when update an json with an UserRegisterDTO and passing an invalid token in the Autorization,
     * the response has contain the status code is forbidden
     *
     * @throws Exception if have not mocked the update method in UserService
     */
    @Test
    public void updateAnUserWithInvalidTokenTest() throws Exception {
        Mockito.when(service.update(any(), any(UserRegisterDTO.class))).thenThrow(new InvalidUserException(Messages.INVALID_USER_CHECK_THE_TOKEN));

        mockMvc.perform(put(uriBase + "/auth/users").contentType("application/json").characterEncoding("UTF-8")
                .header("Authorization", "token")
                .content(objectMapper.writeValueAsString(userRegisterDTO)))
                .andExpect(status().isForbidden());
    }

    /**
     * Test the delete method
     *
     * Verifying if when delete an User parsing an Authorization with the token,
     * the response has contain the UserDTO deleted
     *
     * @throws Exception if have not mocked the delete method in UserService
     */
    @Test
    public void deleteAnUserTest() throws Exception {
        Mockito.when(service.delete("token")).thenReturn(userDTO);

        mockMvc.perform(delete(uriBase + "/auth/users").characterEncoding("UTF-8")
                .header("Authorization", "token"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(userDTO)));
    }

    /**
     * Test the delete method
     *
     * Verifying if when delete an User parsing an Authorization with the token invalid,
     * the response has contain the InvalidUserException
     *
     * @throws Exception if have not mocked the delete method in UserService
     */
    @Test
    public void deleteAnUserWithinvalidTokenTest() throws Exception {
        Mockito.when(service.delete("token")).thenThrow(new InvalidUserException(Messages.INVALID_USER_CHECK_THE_TOKEN));

        mockMvc.perform(delete(uriBase + "/auth/users").characterEncoding("UTF-8")
                .header("Authorization", "token"))
                .andExpect(status().isForbidden());
    }
}
