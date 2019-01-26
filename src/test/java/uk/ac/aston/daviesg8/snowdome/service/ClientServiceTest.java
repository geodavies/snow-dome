package uk.ac.aston.daviesg8.snowdome.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import uk.ac.aston.daviesg8.snowdome.model.entity.Client;
import uk.ac.aston.daviesg8.snowdome.model.exception.ClientAlreadyExistsException;
import uk.ac.aston.daviesg8.snowdome.model.exception.ClientNotFoundException;
import uk.ac.aston.daviesg8.snowdome.repository.ClientRepository;

import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public class ClientServiceTest {

    private final String TEST_CLIENT_USERNAME = "testUser";
    private final String TEST_CLIENT_PASSWORD = "testPassword";
    private ClientService clientService;
    private ClientRepository mockClientRepository;

    @Before
    public void setup() {
        mockClientRepository = mock(ClientRepository.class);
        clientService = new ClientService(mockClientRepository);
    }

    @Test
    public void addClientToSessionSuccess() throws ClientNotFoundException {
        Client testClient = new Client();
        testClient.setClientid(1);
        testClient.setUsername(TEST_CLIENT_USERNAME);
        testClient.setPassword(TEST_CLIENT_PASSWORD);

        when(mockClientRepository.findByUsernameAndPassword(eq(TEST_CLIENT_USERNAME), eq(TEST_CLIENT_PASSWORD)))
                .thenReturn(Optional.of(testClient));

        HttpSession mockHttpSession = mock(HttpSession.class);
        doNothing().when(mockHttpSession).setAttribute(eq("client"), same(testClient));

        clientService.addClientToSession(mockHttpSession, TEST_CLIENT_USERNAME, TEST_CLIENT_PASSWORD);

        verify(mockClientRepository, times(1)).findByUsernameAndPassword(eq(TEST_CLIENT_USERNAME), eq(TEST_CLIENT_PASSWORD));
        verify(mockHttpSession, times(1)).setAttribute(eq("client"), same(testClient));
    }

    @Test
    public void addClientToSessionClientNotFound() {
        when(mockClientRepository.findByUsernameAndPassword(eq(TEST_CLIENT_USERNAME), eq(TEST_CLIENT_PASSWORD)))
                .thenReturn(Optional.empty());

        HttpSession mockHttpSession = mock(HttpSession.class);
        doNothing().when(mockHttpSession).setAttribute(eq("client"), any(Client.class));

        try {
            clientService.addClientToSession(mockHttpSession, TEST_CLIENT_USERNAME, TEST_CLIENT_PASSWORD);
            fail("Expected ClientNotFoundException to be thrown but wasn't");
        } catch (ClientNotFoundException ignored) {
        }

        verify(mockClientRepository, times(1)).findByUsernameAndPassword(eq(TEST_CLIENT_USERNAME), eq(TEST_CLIENT_PASSWORD));
        verify(mockHttpSession, times(0)).setAttribute(eq("client"), any(Client.class));
    }

    @Test
    public void isClientLoggedInTrue() {
        Client mockClient = mock(Client.class);
        HttpSession mockHttpSession = mock(HttpSession.class);
        when(mockHttpSession.getAttribute(eq("client")))
                .thenReturn(mockClient);

        assertTrue(clientService.isClientLoggedIn(mockHttpSession));
        verify(mockHttpSession, times(1)).getAttribute(eq("client"));
    }

    @Test
    public void isClientLoggedInFalse() {
        HttpSession mockHttpSession = mock(HttpSession.class);
        when(mockHttpSession.getAttribute(eq("client")))
                .thenReturn(null);

        assertFalse(clientService.isClientLoggedIn(mockHttpSession));
        verify(mockHttpSession, times(1)).getAttribute(eq("client"));
    }

    @Test
    public void registerNewClientSuccess() throws ClientAlreadyExistsException {
        when(mockClientRepository.findByUsername(eq(TEST_CLIENT_USERNAME)))
                .thenReturn(Optional.empty());

        when(mockClientRepository.save(any(Client.class)))
                .thenReturn(null);

        clientService.registerNewClient(TEST_CLIENT_USERNAME, TEST_CLIENT_PASSWORD);

        verify(mockClientRepository, times(1)).findByUsername(eq(TEST_CLIENT_USERNAME));
        ArgumentCaptor<Client> clientArgumentCaptor = ArgumentCaptor.forClass(Client.class);
        verify(mockClientRepository, times(1)).save(clientArgumentCaptor.capture());
        Client capturedClient = clientArgumentCaptor.getValue();
        assertNull(capturedClient.getClientid());
        assertEquals(TEST_CLIENT_USERNAME, capturedClient.getUsername());
        assertEquals(TEST_CLIENT_PASSWORD, capturedClient.getPassword());
    }

    @Test
    public void registerNewClientClientAlreadyExists() {
        when(mockClientRepository.findByUsername(eq(TEST_CLIENT_USERNAME)))
                .thenReturn(Optional.of(mock(Client.class)));

        when(mockClientRepository.save(any(Client.class)))
                .thenReturn(null);

        try {
            clientService.registerNewClient(TEST_CLIENT_USERNAME, TEST_CLIENT_PASSWORD);
            fail("Expected ClientAlreadyExistsException to be thrown but wasn't");
        } catch (ClientAlreadyExistsException ignored) {
        }

        verify(mockClientRepository, times(1)).findByUsername(eq(TEST_CLIENT_USERNAME));
        verify(mockClientRepository, times(0)).save(any(Client.class));
    }

    @Test
    public void checkUserExistsTrue() {
        when(mockClientRepository.findByUsername(eq(TEST_CLIENT_USERNAME)))
                .thenReturn(Optional.of(mock(Client.class)));

        boolean exists = clientService.checkClientExists(TEST_CLIENT_USERNAME);

        assertTrue(exists);
    }

    @Test
    public void checkUserExistsFalse() {
        when(mockClientRepository.findByUsername(eq(TEST_CLIENT_USERNAME)))
                .thenReturn(Optional.empty());

        boolean exists = clientService.checkClientExists(TEST_CLIENT_USERNAME);

        assertFalse(exists);
    }

    @Test
    public void logOutClient() {
        HttpSession mockHttpSession = mock(HttpSession.class);
        doNothing().when(mockHttpSession).invalidate();

        clientService.logOutClient(mockHttpSession);

        verify(mockHttpSession, times(1)).invalidate();
    }

}
