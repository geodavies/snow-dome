package uk.ac.aston.daviesg8.snowdome.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.aston.daviesg8.snowdome.model.entity.Lesson;
import uk.ac.aston.daviesg8.snowdome.model.exception.LessonNotFoundException;
import uk.ac.aston.daviesg8.snowdome.model.exception.MaximumSelectedLessonsExceededException;
import uk.ac.aston.daviesg8.snowdome.repository.LessonRepository;

/**
 * This service class is responsible for handling all logic relating to lessons
 */
@Slf4j
@Service
public class LessonService {

  private final LessonRepository lessonRepository;

  @Autowired
  public LessonService(LessonRepository lessonRepository) {
    this.lessonRepository = lessonRepository;
  }

  /**
   * Retrieves all available lessons from the database.
   *
   * @return the lessons
   */
  public List<Lesson> getAllLessons() {
    log.info("Retrieving all lessons from the database");
    return lessonRepository.findAll();
  }

  /**
   * Finds the given lesson from the database and adds it to the selected lessons in the session. If
   * the lesson doesn't exist then an exception is thrown. If more than 3 lessons have been selected
   * then an exception will also be thrown.
   *
   * @param httpSession the session to add the lesson to
   * @param lessonId the id of the lesson to add
   * @throws LessonNotFoundException the lesson could not be found in the database
   * @throws MaximumSelectedLessonsExceededException more than 3 lessons selected
   */
  public void addLessonToSession(HttpSession httpSession, String lessonId)
      throws LessonNotFoundException, MaximumSelectedLessonsExceededException {
    log.info("Retrieving lesson from database with lessonId: {}", lessonId);
    Optional<Lesson> lesson = lessonRepository.findByLessonid(lessonId);

    if (lesson.isPresent()) {
      log.info("Lesson found, adding to client session");
      addLessonToSession(httpSession, lesson.get());
    } else {
      log.info("Could not find lesson with lessonId: {}", lessonId);
      throw new LessonNotFoundException();
    }
  }

  /**
   * Looks for all selected lessons currently in the session which match the provided lessonId and
   * removes them from the session.
   *
   * @param httpSession the session to remove lesson from
   * @param lessonId the lessonId of the lesson to remove from session
   */
  public void removeLessonFromSession(HttpSession httpSession, String lessonId) {
    log.info("Removing selected lesson from session with lessonId: {}", lessonId);
    Set<Lesson> currentlySelectedLessons = (Set<Lesson>) httpSession
        .getAttribute("selectedLessons");
    Set<Lesson> newSelectedLessons = currentlySelectedLessons.stream()
        .filter(lesson -> !lesson.getLessonid().equals(lessonId))
        .collect(Collectors.toSet());
    httpSession.setAttribute("selectedLessons", newSelectedLessons);
  }

  /**
   * Adds the lesson to the session. If there are currently none selected then it will create a new
   * set. This also checks the maximum limit on lessons per user.
   *
   * @param httpSession the session to add lesson to
   * @param lesson the lesson to add to the session
   * @throws MaximumSelectedLessonsExceededException more than 3 lessons selected
   */
  private void addLessonToSession(HttpSession httpSession, Lesson lesson)
      throws MaximumSelectedLessonsExceededException {
    Set<Lesson> selectedLessons = (Set<Lesson>) httpSession.getAttribute("selectedLessons");
    if (selectedLessons == null) {
      selectedLessons = new HashSet<>();
    } else if (selectedLessons.size() >= 3) {
      throw new MaximumSelectedLessonsExceededException();
    }
    selectedLessons.add(lesson);
    httpSession.setAttribute("selectedLessons", selectedLessons);
  }

}
