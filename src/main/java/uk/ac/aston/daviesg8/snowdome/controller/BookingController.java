package uk.ac.aston.daviesg8.snowdome.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.ac.aston.daviesg8.snowdome.model.entity.Client;
import uk.ac.aston.daviesg8.snowdome.model.entity.Lesson;
import uk.ac.aston.daviesg8.snowdome.model.exception.ClientNotLoggedInException;
import uk.ac.aston.daviesg8.snowdome.service.BookingService;
import uk.ac.aston.daviesg8.snowdome.service.ClientService;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Set;

/**
 * This controller class is responsible for handling requests related to booking
 */
@Controller
public class BookingController extends AbstractAuthenticatedController {

    private final BookingService bookingService;

    @Autowired
    protected BookingController(ClientService clientService, BookingService bookingService) {
        super(clientService);
        this.bookingService = bookingService;
    }

    /**
     * Extracts all of the currently selected lessons from the session, deletes all existing bookings for the logged in
     * client and replaces them with the new ones.
     *
     * @param httpSession the http session containing client and selected lessons
     * @return redirect
     * @throws ClientNotLoggedInException the session is not authenticated
     */
    @RequestMapping(value = "/lessons/selected/finalise", method = RequestMethod.POST)
    public String finaliseSelectedLessons(HttpSession httpSession) throws ClientNotLoggedInException {
        super.checkLoggedIn(httpSession);

        Client client = (Client) httpSession.getAttribute("client");
        Set<Lesson> selectedLessons = (Set<Lesson>) httpSession.getAttribute("selectedLessons");

        if (selectedLessons == null) {
            selectedLessons = Collections.emptySet();
        }

        bookingService.deleteExistingBookingForClient(client);

        bookingService.addNewClientBookings(client, selectedLessons);

        return "redirect:/lessons/selected";
    }

}
