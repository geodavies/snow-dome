package uk.ac.aston.daviesg8.snowdome.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import uk.ac.aston.daviesg8.snowdome.model.entity.Lesson;
import uk.ac.aston.daviesg8.snowdome.model.exception.ClientNotLoggedInException;
import uk.ac.aston.daviesg8.snowdome.model.exception.LessonNotFoundException;
import uk.ac.aston.daviesg8.snowdome.model.exception.MaximumSelectedLessonsExceededException;
import uk.ac.aston.daviesg8.snowdome.service.ClientService;
import uk.ac.aston.daviesg8.snowdome.service.LessonService;

/**
 * This controller class is responsible for handling all requests relating to lessons
 */
@Controller
public class LessonController extends AbstractAuthenticatedController {

  private final LessonService lessonService;

  @Autowired
  public LessonController(ClientService clientService, LessonService lessonService) {
    super(clientService);
    this.lessonService = lessonService;
  }

  /**
   * Displays the lessons page and injects all available lessons available in the database.
   *
   * @param httpSession the http session to check client logged in
   * @param modelMap the model to add lessons to
   * @return lessons page view
   * @throws ClientNotLoggedInException the client is not logged in
   */
  @RequestMapping(value = "/lessons/available", method = RequestMethod.GET)
  public String getAllLessons(HttpSession httpSession, ModelMap modelMap)
      throws ClientNotLoggedInException {
    super.checkLoggedIn(httpSession);

    List<Lesson> allLessons = lessonService.getAllLessons();
    modelMap.addAttribute("lessons", allLessons);

    return "/availableLessons";
  }

  /**
   * Handles a post request to select a single lesson. This will first check that the lesson is in
   * the database and then add it to the current session. However if the client has already selected
   * 3 lessons then the new one will not be added.
   *
   * @param httpSession the http session to check client logged in and to add the selected lesson
   * to
   * @param lessonId the lesson to select
   * @return redirect to the selected lessons page
   * @throws ClientNotLoggedInException the client isn't logged in
   * @throws LessonNotFoundException the selected lesson could not be found in the database
   * @throws MaximumSelectedLessonsExceededException the client has already selected 3 lessons
   */
  @RequestMapping(value = "/lessons/select", method = RequestMethod.POST)
  public String postSelectLesson(HttpSession httpSession, @RequestParam("lessonId") String lessonId)
      throws ClientNotLoggedInException, LessonNotFoundException, MaximumSelectedLessonsExceededException {
    super.checkLoggedIn(httpSession);

    lessonService.addLessonToSession(httpSession, lessonId);

    return "redirect:/lessons/selected";
  }

  /**
   * Handles a post request to cancel a single lesson. This will remove any instances of that lesson
   * from the current session.
   *
   * @param httpSession the http session to check client logged in and remove selected lesson from
   * @param lessonId the selected lesson to cancel
   * @return redirect to the selected lessons page
   * @throws ClientNotLoggedInException the client isn't logged in
   */
  @RequestMapping(value = "/lessons/cancel", method = RequestMethod.POST)
  public String postCancelLesson(HttpSession httpSession, @RequestParam("lessonId") String lessonId)
      throws ClientNotLoggedInException {
    super.checkLoggedIn(httpSession);

    lessonService.removeLessonFromSession(httpSession, lessonId);

    return "redirect:/lessons/selected";
  }

  /**
   * Displays all of the currently selected lessons
   *
   * @param httpSession to check if the client is logged in and to retrieve the selected lessons
   * @param modelMap the model to add selected lessons to
   * @return selected lessons page vieww
   * @throws ClientNotLoggedInException the client is not logged in
   */
  @RequestMapping(value = "/lessons/selected", method = RequestMethod.GET)
  public String getSelectedLessons(HttpSession httpSession, ModelMap modelMap)
      throws ClientNotLoggedInException {
    super.checkLoggedIn(httpSession);

    Set<Lesson> selectedLessons = (Set<Lesson>) httpSession.getAttribute("selectedLessons");
    if (selectedLessons == null) {
      modelMap.addAttribute("lessons", new HashSet<>());
    } else {
      modelMap.addAttribute("lessons", selectedLessons);
    }

    return "/selectedLessons";
  }

}
