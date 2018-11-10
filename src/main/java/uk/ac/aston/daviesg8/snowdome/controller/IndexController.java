package uk.ac.aston.daviesg8.snowdome.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.ac.aston.daviesg8.snowdome.service.ClientService;

import javax.servlet.http.HttpSession;

/**
 * This controller class is responsible for handling all requests relating to the root index
 */
@Slf4j
@Controller
public class IndexController {

    private final ClientService clientService;

    @Autowired
    public IndexController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Resolves the index of the application and redirects to either the login page or lessons page based on whether or
     * not the client is logged in.
     *
     * @param httpSession the http session to check if client is logged in
     * @return redirect to login or lessons
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(HttpSession httpSession) {
        boolean clientOnSession = clientService.isClientLoggedIn(httpSession);

        if (clientOnSession) {
            return "redirect:/lessons";
        } else {
            return "redirect:/login";
        }
    }

}
