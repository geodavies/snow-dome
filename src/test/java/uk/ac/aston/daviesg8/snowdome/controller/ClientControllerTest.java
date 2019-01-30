package uk.ac.aston.daviesg8.snowdome.controller;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;
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
        .andExpect(redirectedUrl("/lessons/available"));
  }

  @Test
  public void postLoginUserNotFound() throws Exception {
    this.mockMvc.perform(post("/login")
        .session(super.getNewMockHttpSession(false))
        .param("username", TEST_CLIENT_USERNAME)
        .param("password", TEST_CLIENT_PASSWORD))
        .andExpect(status().isNotFound())
        .andExpect(content().string(containsString("Client not found")));
  }

  @Test
  public void postRegisterSuccess() throws Exception {
    this.mockMvc.perform(post("/register")
        .session(super.getNewMockHttpSession(false))
        .param("username", TEST_CLIENT_USERNAME)
        .param("password", TEST_CLIENT_PASSWORD))
        .andExpect(redirectedUrl("/lessons/available"));

    Optional<Client> clientOptional = clientRepository
        .findByUsernameAndPassword(TEST_CLIENT_USERNAME, TEST_CLIENT_PASSWORD);
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
        .andExpect(content().string(containsString("A client with that username already exists")));

    Optional<Client> clientOptional = clientRepository
        .findByUsernameAndPassword(TEST_CLIENT_USERNAME, TEST_CLIENT_PASSWORD);
    assertTrue(clientOptional.isPresent());
    assertEquals(TEST_CLIENT_USERNAME, clientOptional.get().getUsername());
    assertEquals(TEST_CLIENT_PASSWORD, clientOptional.get().getPassword());
  }

  @Test
  public void getClientExists204() throws Exception {
    addTestClientToDatabase();

    this.mockMvc.perform(get("/clientExists/" + TEST_CLIENT_USERNAME))
        .andExpect(status().isNoContent());
  }

  @Test
  public void getClientExists404() throws Exception {
    this.mockMvc.perform(get("/clientExists/" + TEST_CLIENT_USERNAME))
        .andExpect(status().isNotFound());
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
