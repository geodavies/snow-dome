package uk.ac.aston.daviesg8.snowdome.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anySet;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import uk.ac.aston.daviesg8.snowdome.model.entity.Lesson;
import uk.ac.aston.daviesg8.snowdome.model.exception.LessonNotFoundException;
import uk.ac.aston.daviesg8.snowdome.model.exception.MaximumSelectedLessonsExceededException;
import uk.ac.aston.daviesg8.snowdome.repository.LessonRepository;

public class LessonServiceTest {

  private LessonService lessonService;
  private LessonRepository mockLessonRepository;

  @Before
  public void setup() {
    this.mockLessonRepository = mock(LessonRepository.class);
    this.lessonService = new LessonService(mockLessonRepository);
  }

  @Test
  public void getAllLessonsTwoResults() {
    Lesson mockLesson1 = mock(Lesson.class);
    Lesson mockLesson2 = mock(Lesson.class);

    when(mockLessonRepository.findAll())
        .thenReturn(Arrays.asList(mockLesson1, mockLesson2));

    List<Lesson> actualLessons = lessonService.getAllLessons();

    verify(mockLessonRepository, times(1)).findAll();
    assertEquals(2, actualLessons.size());
    assertEquals(mockLesson1, actualLessons.get(0));
    assertEquals(mockLesson2, actualLessons.get(1));
  }

  @Test
  public void getAllLessonsNoResults() {
    when(mockLessonRepository.findAll())
        .thenReturn(Collections.emptyList());

    List<Lesson> actualLessons = lessonService.getAllLessons();

    verify(mockLessonRepository, times(1)).findAll();
    assertEquals(0, actualLessons.size());
  }

  @Test
  public void addLessonToSessionOneLesson()
      throws LessonNotFoundException, MaximumSelectedLessonsExceededException {
    Lesson mockLesson = mock(Lesson.class);
    when(mockLessonRepository.findByLessonid(eq("testLessonId1")))
        .thenReturn(Optional.of(mockLesson));
    HttpSession mockHttpSession = mock(HttpSession.class);
    when(mockHttpSession.getAttribute(eq("selectedLessons"))).thenReturn(null);
    doNothing().when(mockHttpSession).setAttribute(eq("selectedLessons"), any());

    lessonService.addLessonToSession(mockHttpSession, "testLessonId1");

    verify(mockLessonRepository, times(1)).findByLessonid(eq("testLessonId1"));
    verify(mockHttpSession, times(1)).getAttribute(eq("selectedLessons"));
    ArgumentCaptor<Set<Lesson>> lessonArgumentCaptor = ArgumentCaptor.forClass(Set.class);
    verify(mockHttpSession, times(1))
        .setAttribute(eq("selectedLessons"), lessonArgumentCaptor.capture());
    Set<Lesson> addedLessons = lessonArgumentCaptor.getValue();
    assertEquals(1, addedLessons.size());
    assertTrue(addedLessons.contains(mockLesson));
  }

  @Test
  public void addLessonToSessionTwoLessons()
      throws LessonNotFoundException, MaximumSelectedLessonsExceededException {
    Lesson mockLesson1 = mock(Lesson.class);
    Lesson mockLesson2 = mock(Lesson.class);
    when(mockLessonRepository.findByLessonid(eq("testLessonId2")))
        .thenReturn(Optional.of(mockLesson2));
    Set<Lesson> existingLessons = new HashSet<>();
    existingLessons.add(mockLesson1);
    HttpSession mockHttpSession = mock(HttpSession.class);
    when(mockHttpSession.getAttribute(eq("selectedLessons"))).thenReturn(existingLessons);
    doNothing().when(mockHttpSession).setAttribute(eq("selectedLessons"), any());

    lessonService.addLessonToSession(mockHttpSession, "testLessonId2");

    verify(mockLessonRepository, times(1)).findByLessonid(eq("testLessonId2"));
    verify(mockHttpSession, times(1)).getAttribute(eq("selectedLessons"));
    ArgumentCaptor<Set<Lesson>> lessonArgumentCaptor = ArgumentCaptor.forClass(Set.class);
    verify(mockHttpSession, times(1))
        .setAttribute(eq("selectedLessons"), lessonArgumentCaptor.capture());
    Set<Lesson> addedLessons = lessonArgumentCaptor.getValue();
    assertEquals(2, addedLessons.size());
    assertTrue(addedLessons.contains(mockLesson1));
    assertTrue(addedLessons.contains(mockLesson2));
  }

  @Test
  public void addLessonToSessionMaximumExceeded() throws LessonNotFoundException {
    Lesson mockLesson1 = mock(Lesson.class);
    Lesson mockLesson2 = mock(Lesson.class);
    Lesson mockLesson3 = mock(Lesson.class);
    Lesson mockLesson4 = mock(Lesson.class);
    when(mockLessonRepository.findByLessonid(eq("testLessonId4")))
        .thenReturn(Optional.of(mockLesson4));
    Set<Lesson> existingLessons = new HashSet<>(
        Arrays.asList(mockLesson1, mockLesson2, mockLesson3));
    HttpSession mockHttpSession = mock(HttpSession.class);
    when(mockHttpSession.getAttribute(eq("selectedLessons"))).thenReturn(existingLessons);
    doNothing().when(mockHttpSession).setAttribute(eq("selectedLessons"), any());

    try {
      lessonService.addLessonToSession(mockHttpSession, "testLessonId4");
      fail("Expected MaximumSelectedLessonsExceededException but wasn't thrown");
    } catch (MaximumSelectedLessonsExceededException ignored) {
    }

    verify(mockLessonRepository, times(1)).findByLessonid(eq("testLessonId4"));
    verify(mockHttpSession, times(1)).getAttribute(eq("selectedLessons"));
    verify(mockHttpSession, times(0)).setAttribute(eq("selectedLessons"), anySet());
  }


  @Test
  public void addLessonToSessionLessonAlreadyInSession()
      throws LessonNotFoundException, MaximumSelectedLessonsExceededException {
    Lesson mockLesson = mock(Lesson.class);
    when(mockLessonRepository.findByLessonid(eq("testLessonId1")))
        .thenReturn(Optional.of(mockLesson));
    HttpSession mockHttpSession = mock(HttpSession.class);
    Set<Lesson> existingLessons = new HashSet<>();
    existingLessons.add(mockLesson);
    when(mockHttpSession.getAttribute(eq("selectedLessons"))).thenReturn(existingLessons);
    doNothing().when(mockHttpSession).setAttribute(eq("selectedLessons"), any());

    lessonService.addLessonToSession(mockHttpSession, "testLessonId1");

    verify(mockLessonRepository, times(1)).findByLessonid(eq("testLessonId1"));
    verify(mockHttpSession, times(1)).getAttribute(eq("selectedLessons"));
    ArgumentCaptor<Set<Lesson>> lessonArgumentCaptor = ArgumentCaptor.forClass(Set.class);
    verify(mockHttpSession, times(1))
        .setAttribute(eq("selectedLessons"), lessonArgumentCaptor.capture());
    Set<Lesson> addedLessons = lessonArgumentCaptor.getValue();
    assertEquals(1, addedLessons.size());
    assertTrue(addedLessons.contains(mockLesson));
  }

  @Test
  public void addLessonToSessionLessonNotFound() throws MaximumSelectedLessonsExceededException {
    when(mockLessonRepository.findByLessonid(eq("testLessonId1"))).thenReturn(Optional.empty());
    HttpSession mockHttpSession = mock(HttpSession.class);
    when(mockHttpSession.getAttribute(eq("selectedLessons"))).thenReturn(null);
    doNothing().when(mockHttpSession).setAttribute(eq("selectedLessons"), any());

    try {
      lessonService.addLessonToSession(mockHttpSession, "testLessonId1");
      fail("Expected LessonNotFoundException to be thrown but wasn't");
    } catch (LessonNotFoundException ignored) {
    }

    verify(mockLessonRepository, times(1)).findByLessonid(eq("testLessonId1"));
    verify(mockHttpSession, times(0)).getAttribute(eq("selectedLessons"));
    verify(mockHttpSession, times(0)).setAttribute(eq("selectedLessons"), any());
  }

  @Test
  public void removeLessonFromSessionOneLesson() {
    HttpSession mockHttpSession = mock(HttpSession.class);
    Lesson mockLesson = mock(Lesson.class);
    when(mockLesson.getLessonid()).thenReturn("testLessonId1");
    Set<Lesson> existingLessons = new HashSet<>();
    existingLessons.add(mockLesson);
    when(mockHttpSession.getAttribute(eq("selectedLessons"))).thenReturn(existingLessons);
    doNothing().when(mockHttpSession).setAttribute(eq("selectedLessons"), any());

    lessonService.removeLessonFromSession(mockHttpSession, "testLessonId1");

    verify(mockHttpSession, times(1)).getAttribute(eq("selectedLessons"));
    ArgumentCaptor<Set<Lesson>> lessonSetArgumentCaptor = ArgumentCaptor.forClass(Set.class);
    verify(mockHttpSession, times(1))
        .setAttribute(eq("selectedLessons"), lessonSetArgumentCaptor.capture());
    assertEquals(0, lessonSetArgumentCaptor.getValue().size());
  }

  @Test
  public void removeLessonFromSessionLessonNotSelected() {
    HttpSession mockHttpSession = mock(HttpSession.class);
    Lesson mockLesson = mock(Lesson.class);
    when(mockLesson.getLessonid()).thenReturn("testLessonId2");
    Set<Lesson> existingLessons = new HashSet<>();
    existingLessons.add(mockLesson);
    when(mockHttpSession.getAttribute(eq("selectedLessons"))).thenReturn(existingLessons);
    doNothing().when(mockHttpSession).setAttribute(eq("selectedLessons"), any());

    lessonService.removeLessonFromSession(mockHttpSession, "testLessonId1");

    verify(mockHttpSession, times(1)).getAttribute(eq("selectedLessons"));
    ArgumentCaptor<Set<Lesson>> lessonSetArgumentCaptor = ArgumentCaptor.forClass(Set.class);
    verify(mockHttpSession, times(1))
        .setAttribute(eq("selectedLessons"), lessonSetArgumentCaptor.capture());
    Set<Lesson> newSelectedLessons = lessonSetArgumentCaptor.getValue();
    assertEquals(1, newSelectedLessons.size());
    for (Lesson lesson : newSelectedLessons) {
      assertEquals(mockLesson, lesson);
    }
  }

  @Test
  public void removeLessonFromSessionZeroLessons() {
    HttpSession mockHttpSession = mock(HttpSession.class);
    when(mockHttpSession.getAttribute(eq("selectedLessons"))).thenReturn(Collections.emptySet());
    doNothing().when(mockHttpSession).setAttribute(eq("selectedLessons"), any());

    lessonService.removeLessonFromSession(mockHttpSession, "testLessonId1");

    verify(mockHttpSession, times(1)).getAttribute(eq("selectedLessons"));
    ArgumentCaptor<Set<Lesson>> lessonSetArgumentCaptor = ArgumentCaptor.forClass(Set.class);
    verify(mockHttpSession, times(1))
        .setAttribute(eq("selectedLessons"), lessonSetArgumentCaptor.capture());
    Set<Lesson> newSelectedLessons = lessonSetArgumentCaptor.getValue();
    assertEquals(0, newSelectedLessons.size());
  }

}
