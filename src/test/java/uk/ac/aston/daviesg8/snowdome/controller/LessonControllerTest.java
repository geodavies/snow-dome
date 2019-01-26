package uk.ac.aston.daviesg8.snowdome.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.ac.aston.daviesg8.snowdome.model.entity.Lesson;
import uk.ac.aston.daviesg8.snowdome.repository.LessonRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LessonControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LessonRepository lessonRepository;

    @Test
    public void getLessonsLoggedInUser() throws Exception {
        this.mockMvc.perform(get("/lessons")
                .session(super.getNewMockHttpSession(true)))
                .andExpect(status().isOk())
                .andExpect(view().name("/lessons"));
    }

    @Test
    public void getLessonsNonLoggedInUser() throws Exception {
        this.mockMvc.perform(get("/lessons")
                .session(super.getNewMockHttpSession(false)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("You must be logged in to access that resource"));
    }

    @Test
    public void getLessonsWithLessonSelected() throws Exception {
        Lesson testLesson = super.createTestLesson("testLessonId1", "testLessonDescription1");

        Set<Lesson> selectedLessons = new HashSet<>(Collections.singletonList(testLesson));

        MockHttpSession mockHttpSession = super.getNewMockHttpSession(true);
        mockHttpSession.setAttribute("selectedLessons", selectedLessons);

        this.mockMvc.perform(get("/lessons")
                .session(super.getNewMockHttpSession(true)))
                .andExpect(status().isOk())
                .andExpect(view().name("/lessons"));
    }

    @Test
    public void selectLessonSuccess() throws Exception {
        Lesson testLesson = super.createTestLesson("testLessonId1", "testLessonDescription1");

        lessonRepository.save(testLesson);

        this.mockMvc.perform(post("/lessons/select")
                .param("lessonId", "testLessonId1")
                .session(super.getNewMockHttpSession(true)))
                .andExpect(redirectedUrl("/lessons/selected"));
    }

    @Test
    public void selectLessonMaximumExceeded() throws Exception {
        Lesson testLesson1 = super.createTestLesson("testLessonId1", "testLessonDescription1");
        Lesson testLesson2 = super.createTestLesson("testLessonId2", "testLessonDescription2");
        Lesson testLesson3 = super.createTestLesson("testLessonId3", "testLessonDescription3");
        Lesson testLesson4 = super.createTestLesson("testLessonId4", "testLessonDescription4");

        lessonRepository.save(testLesson4);

        Set<Lesson> selectedLessons = new HashSet<>(Arrays.asList(testLesson1, testLesson2, testLesson3));

        MockHttpSession mockHttpSession = super.getNewMockHttpSession(true);
        mockHttpSession.setAttribute("selectedLessons", selectedLessons);

        this.mockMvc.perform(post("/lessons/select")
                .param("lessonId", "testLessonId4")
                .session(mockHttpSession))
                .andExpect(status().isConflict())
                .andExpect(content().string("Maximum number of selected lessons exceeded"));
    }

    @Test
    public void selectLessonNonLoggedInUser() throws Exception {
        this.mockMvc.perform(post("/lessons/select")
                .param("lessonId", "testLessonId1")
                .session(super.getNewMockHttpSession(false)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("You must be logged in to access that resource"));
    }

    @Test
    public void selectLessonNotFound() throws Exception {
        this.mockMvc.perform(post("/lessons/select")
                .param("lessonId", "testLessonId1")
                .session(super.getNewMockHttpSession(true)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Lesson not found"));
    }

    @Test
    public void cancelLessonSuccess() throws Exception {
        Lesson testLesson = super.createTestLesson("testLessonId1", "testLessonDescription1");

        MockHttpSession httpSession = super.getNewMockHttpSession(true);
        Set<Lesson> selectedLessons = Collections.singleton(testLesson);
        httpSession.setAttribute("selectedLessons", selectedLessons);

        this.mockMvc.perform(post("/lessons/cancel")
                .param("lessonId", "testLessonId1")
                .session(httpSession))
                .andExpect(redirectedUrl("/lessons/selected"));
    }

    @Test
    public void cancelLessonNonLoggedInUser() throws Exception {
        this.mockMvc.perform(post("/lessons/cancel")
                .param("lessonId", "testLessonId1")
                .session(super.getNewMockHttpSession(false)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("You must be logged in to access that resource"));
    }

    @Test
    public void getSelectedLessonsLessonsSelected() throws Exception {
        MockHttpSession mockHttpSession = super.getNewMockHttpSession(true);
        Set<Lesson> lessons = new HashSet<>();
        lessons.add(mock(Lesson.class));
        mockHttpSession.setAttribute("selectedLessons", lessons);

        this.mockMvc.perform(get("/lessons/selected")
                .session(mockHttpSession))
                .andExpect(status().isOk())
                .andExpect(view().name("/selectedLessons"));
    }

    @Test
    public void getSelectedLessonsNoLessonsSelected() throws Exception {
        this.mockMvc.perform(get("/lessons/selected")
                .session(super.getNewMockHttpSession(true)))
                .andExpect(status().isOk())
                .andExpect(view().name("/selectedLessons"));
    }

    @Test
    public void getSelectedLessonsNonLoggedInUser() throws Exception {
        this.mockMvc.perform(get("/lessons/selected")
                .session(super.getNewMockHttpSession(false)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("You must be logged in to access that resource"));
    }

}
