package uk.ac.aston.daviesg8.snowdome.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.ac.aston.daviesg8.snowdome.model.entity.Client;
import uk.ac.aston.daviesg8.snowdome.repository.ClientRepository;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @Before
    public void setup() {
        clientRepository.deleteAll();
    }

    @Test
    public void getLogin() throws Exception {
        this.mockMvc.perform(get("/login")
                .session(super.getNewMockHttpSession(false)))
                .andExpect(status().isOk())
                .andExpect(view().name("/login"));
    }

    @Test
    public void postLoginSuccess() throws Exception {
        addTestClientToDatabase();

        this.mockMvc.perform(post("/login")
                .session(super.getNewMockHttpSession(false))
                .param("username", TEST_CLIENT_USERNAME)
                .param("password", TEST_CLIENT_PASSWORD))
                .andExpect(redirectedUrl("/lessons"));
    }

    @Test
    public void postLoginUserNotFound() throws Exception {
        this.mockMvc.perform(post("/login")
                .session(super.getNewMockHttpSession(false))
                .param("username", TEST_CLIENT_USERNAME)
                .param("password", TEST_CLIENT_PASSWORD))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Client not found"));
    }

    @Test
    public void getRegister() throws Exception {
        this.mockMvc.perform(get("/register")
                .session(super.getNewMockHttpSession(false)))
                .andExpect(status().isOk())
                .andExpect(view().name("/register"));
    }

    @Test
    public void postRegisterSuccess() throws Exception {
        this.mockMvc.perform(post("/register")
                .session(super.getNewMockHttpSession(false))
                .param("username", TEST_CLIENT_USERNAME)
                .param("password", TEST_CLIENT_PASSWORD))
                .andExpect(redirectedUrl("/lessons"));

        Optional<Client> clientOptional = clientRepository.findByUsernameAndPassword(TEST_CLIENT_USERNAME, TEST_CLIENT_PASSWORD);
        assertTrue(clientOptional.isPresent());
        assertEquals(TEST_CLIENT_USERNAME, clientOptional.get().getUsername());
        assertEquals(TEST_CLIENT_PASSWORD, clientOptional.get().getPassword());
    }

    @Test
    public void postRegisterClientAlreadyExists() throws Exception {
        addTestClientToDatabase();

        this.mockMvc.perform(post("/register")
                .session(super.getNewMockHttpSession(false))
                .param("username", TEST_CLIENT_USERNAME)
                .param("password", TEST_CLIENT_PASSWORD))
                .andExpect(status().isConflict())
                .andExpect(content().string("A client with that username already exists"));

        Optional<Client> clientOptional = clientRepository.findByUsernameAndPassword(TEST_CLIENT_USERNAME, TEST_CLIENT_PASSWORD);
        assertTrue(clientOptional.isPresent());
        assertEquals(TEST_CLIENT_USERNAME, clientOptional.get().getUsername());
        assertEquals(TEST_CLIENT_PASSWORD, clientOptional.get().getPassword());
    }

    @Test
    public void getLogout() throws Exception {
        this.mockMvc.perform(get("/logout")
                .session(super.getNewMockHttpSession(true)))
                .andExpect(redirectedUrl("/login"));
    }

    private void addTestClientToDatabase() {
        Client client = new Client();
        client.setUsername(TEST_CLIENT_USERNAME);
        client.setPassword(TEST_CLIENT_PASSWORD);

        clientRepository.save(client);
    }

}
