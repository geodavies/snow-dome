package uk.ac.aston.daviesg8.snowdome.controller;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import uk.ac.aston.daviesg8.snowdome.model.exception.ClientAlreadyExistsException;
import uk.ac.aston.daviesg8.snowdome.model.exception.ClientNotFoundException;
import uk.ac.aston.daviesg8.snowdome.service.ClientService;

/**
 * This controller class is responsible for handling requests related to the client
 */
@Controller
public class ClientController {

  private final ClientService clientService;

  @Autowired
  public ClientController(ClientService clientService) {
    this.clientService = clientService;
  }

  /**
   * Display login page
   *
   * @return login page view
   */
  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String login() {
    return "/login";
  }

  /**
   * Accepts a username and password of which is first checked to make sure the client exists in the
   * database and if they are then add them to the session. If no client is found then reject the
   * request.
   *
   * @param httpSession the http session to populate with the client
   * @param username the client username
   * @param password the client password
   * @return redirect to login page or available lessons
   * @throws ClientNotFoundException no client could be found with the given credentials
   */
  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public String login(HttpSession httpSession,
      @RequestParam("username") String username,
      @RequestParam("password") String password) throws ClientNotFoundException {

    clientService.addClientToSession(httpSession, username, password);

    return "redirect:/lessons/available";
  }

  /**
   * Accepts a username and password and first checks if the username has already been registered in
   * the database. If it has then the request is rejected, otherwise create the new client and add
   * them to the current session. If for whatever reason after adding the client it can't be found
   * then an exception is thrown.
   *
   * @param httpSession the http session to add client to
   * @param username the new client username
   * @param password the new client password
   * @return redirect to register or lessons
   * @throws ClientAlreadyExistsException a client already exists with the same username
   * @throws ClientNotFoundException the newly created client can't be found in the database
   */
  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public String register(HttpSession httpSession,
      @RequestParam("username") String username,
      @RequestParam("password") String password)
      throws ClientAlreadyExistsException, ClientNotFoundException {

    clientService.registerNewClient(username, password);

    clientService.addClientToSession(httpSession, username, password);

    return "redirect:/lessons/available";
  }

  /**
   * Checks whether the provided username has an associated client in the database. If it does then
   * return 204 otherwise return 404.
   *
   * @param username the username to check
   * @return client exists=204, client doesn't exist=404
   */
  @RequestMapping(value = "/clientExists/{username}", method = RequestMethod.GET)
  public ResponseEntity clientExists(@PathVariable("username") String username) {

    boolean clientExists = clientService.checkClientExists(username);
    if (clientExists) {
      return new ResponseEntity(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Logs the user out by invalidating their http session
   *
   * @param httpSession the session to invalidate
   * @return redirect to login
   */
  @RequestMapping(value = "/logout", method = RequestMethod.GET)
  public String logout(HttpSession httpSession) {
    clientService.logOutClient(httpSession);
    return "redirect:/login";
  }

}
