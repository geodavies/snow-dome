package uk.ac.aston.daviesg8.snowdome.controller;

import uk.ac.aston.daviesg8.snowdome.model.exception.ClientNotLoggedInException;
import uk.ac.aston.daviesg8.snowdome.service.ClientService;

import javax.servlet.http.HttpSession;

/**
 * This class contains shared functionality across all controllers which require client authentication
 */
public abstract class AbstractAuthenticatedController {

    protected final ClientService clientService;

    protected AbstractAuthenticatedController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Checks whether or not there is a client associated with the sessions. If the user is not logged in then a
     * ClientNotLoggedInException is thrown.
     *
     * @param httpSession the session to check
     * @throws ClientNotLoggedInException the client is not logged in
     */
    protected void checkLoggedIn(HttpSession httpSession) throws ClientNotLoggedInException {
        if (!clientService.isClientLoggedIn(httpSession)) {
            throw new ClientNotLoggedInException();
        }
    }

}
