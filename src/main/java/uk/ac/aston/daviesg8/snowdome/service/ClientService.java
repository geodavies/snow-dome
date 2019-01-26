package uk.ac.aston.daviesg8.snowdome.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.aston.daviesg8.snowdome.model.entity.Client;
import uk.ac.aston.daviesg8.snowdome.model.exception.ClientAlreadyExistsException;
import uk.ac.aston.daviesg8.snowdome.model.exception.ClientNotFoundException;
import uk.ac.aston.daviesg8.snowdome.repository.ClientRepository;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.Optional;

/**
 * This service class is responsible for handling any client related logic
 */
@Slf4j
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Finds the client from the database for a given username and password and adds it to the current session. If the
     * client can't be found then an exception is thrown.
     *
     * @param httpSession the http session to add client to
     * @param username    the username of the client
     * @param password    the password of the client
     * @throws ClientNotFoundException the client wasn't found in the database
     */
    public void addClientToSession(HttpSession httpSession, String username, String password) throws ClientNotFoundException {
        log.info("Client logging in with credentials: {}:{}", username, password);
        log.info("Checking if credentials match those stored in database");
        Optional<Client> optionalClient = clientRepository.findByUsernameAndPassword(username, password);

        if (optionalClient.isPresent()) {
            log.info("Client found");
            httpSession.setAttribute("client", optionalClient.get());
        } else {
            log.info("No client found");
            throw new ClientNotFoundException();
        }
    }

    /**
     * Checks if the client already exists and if they don't then it inserts them into the database. If the client
     * already exists then an exception is thrown.
     *
     * @param username the username of the client
     * @param password the password of the client
     * @throws ClientAlreadyExistsException a client with that username already exists
     */
    @Transactional
    public void registerNewClient(String username, String password) throws ClientAlreadyExistsException {
        log.info("Registering new Client with credentials: {}:{}", username, password);
        log.info("Checking if Client already exists with username: {}", username);

        Optional<Client> optionalClient = clientRepository.findByUsername(username);

        if (optionalClient.isPresent()) {
            log.info("Client already exists with username: {}", username);
            throw new ClientAlreadyExistsException();
        } else {
            log.info("No existing client with username: {}", username);
        }

        log.info("Creating new Client with credentials {}:{}", username, password);
        Client newClient = new Client();
        newClient.setUsername(username);
        newClient.setPassword(password);

        log.info("Persisting new Client to database");
        clientRepository.save(newClient);
    }

    /**
     * Checks if a client exists with the given username
     *
     * @param username the username to check for
     * @return boolean true=client exists, false=client doesn't exist
     */
    public boolean checkClientExists(String username) {
        log.info("Checking if Client already exists with username: {}", username);

        Optional<Client> optionalClient = clientRepository.findByUsername(username);

        return optionalClient.isPresent();
    }

    /**
     * Logs the user out by invalidating their http session.
     *
     * @param httpSession the http session to invalidate
     */
    public void logOutClient(HttpSession httpSession) {
        log.info("Logging out Client");
        httpSession.invalidate();
    }

    /**
     * Checks for a given http session whether it contains a client.
     *
     * @param httpSession the http session to check
     * @return boolean: true=logged in, false=not logged in
     */
    public boolean isClientLoggedIn(HttpSession httpSession) {
        Client client = (Client) httpSession.getAttribute("client");
        return client != null;
    }
}
