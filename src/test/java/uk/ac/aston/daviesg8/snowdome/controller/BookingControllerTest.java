package uk.ac.aston.daviesg8.snowdome.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.ac.aston.daviesg8.snowdome.model.entity.Lesson;
import uk.ac.aston.daviesg8.snowdome.model.entity.LessonBooked;
import uk.ac.aston.daviesg8.snowdome.repository.LessonBookedRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LessonBookedRepository lessonBookedRepository;

    @Before
    public void setup() {
        lessonBookedRepository.deleteAll();
    }

    @Test
    public void postFinaliseBookingsNonLoggedInUser() throws Exception {
        this.mockMvc.perform(post("/lessons/selected/finalise")
                .session(super.getNewMockHttpSession(false)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("You must be logged in to access that resource"));
    }

    @Test
    public void postFinaliseBookingsSuccess() throws Exception {
        Lesson selectedLesson = super.createTestLesson("testLessonId1", "testDescription1");
        Set<Lesson> selectedLessons = new HashSet<>();
        selectedLessons.add(selectedLesson);

        MockHttpSession mockHttpSession = super.getNewMockHttpSession(true);
        mockHttpSession.setAttribute("selectedLessons", selectedLessons);

        this.mockMvc.perform(post("/lessons/selected/finalise")
                .session(mockHttpSession))
                .andExpect(redirectedUrl("/lessons/selected"));

        List<LessonBooked> lessonsBooked = lessonBookedRepository.findAll();
        assertEquals(1, lessonsBooked.size());
        assertEquals(Integer.valueOf(1), lessonsBooked.get(0).getClientid());
        assertEquals("testLessonId1", lessonsBooked.get(0).getLessonid());
    }

    @Test
    public void postFinaliseBookingsTwoSelected() throws Exception {
        Lesson selectedLesson1 = super.createTestLesson("testLessonId1", "testDescription1");
        Lesson selectedLesson2 = super.createTestLesson("testLessonId2", "testDescription2");
        Set<Lesson> selectedLessons = new HashSet<>(Arrays.asList(selectedLesson1, selectedLesson2));

        MockHttpSession mockHttpSession = super.getNewMockHttpSession(true);
        mockHttpSession.setAttribute("selectedLessons", selectedLessons);

        this.mockMvc.perform(post("/lessons/selected/finalise")
                .session(mockHttpSession))
                .andExpect(redirectedUrl("/lessons/selected"));

        List<LessonBooked> lessonsBooked = lessonBookedRepository.findAll();
        assertEquals(2, lessonsBooked.size());
        assertEquals(Integer.valueOf(1), lessonsBooked.get(0).getClientid());
        assertEquals("testLessonId1", lessonsBooked.get(0).getLessonid());
        assertEquals(Integer.valueOf(1), lessonsBooked.get(1).getClientid());
        assertEquals("testLessonId2", lessonsBooked.get(1).getLessonid());
    }

    @Test
    public void postFinaliseBookingsZeroSelected() throws Exception {
        Set<Lesson> selectedLessons = new HashSet<>();

        MockHttpSession mockHttpSession = super.getNewMockHttpSession(true);
        mockHttpSession.setAttribute("selectedLessons", selectedLessons);

        this.mockMvc.perform(post("/lessons/selected/finalise")
                .session(mockHttpSession))
                .andExpect(redirectedUrl("/lessons/selected"));

        List<LessonBooked> lessonsBooked = lessonBookedRepository.findAll();
        assertEquals(0, lessonsBooked.size());
    }

    @Test
    public void postFinaliseBookingsNullSelected() throws Exception {
        MockHttpSession mockHttpSession = super.getNewMockHttpSession(true);
        mockHttpSession.setAttribute("selectedLessons", null);

        this.mockMvc.perform(post("/lessons/selected/finalise")
                .session(mockHttpSession))
                .andExpect(redirectedUrl("/lessons/selected"));

        List<LessonBooked> lessonsBooked = lessonBookedRepository.findAll();
        assertEquals(0, lessonsBooked.size());
    }

    @Test
    public void postFinaliseBookingsExistingBookingOverwritten() throws Exception {
        LessonBooked lessonBooked = new LessonBooked();
        lessonBooked.setLessonid("testLessonId1");
        lessonBooked.setClientid(1);

        lessonBookedRepository.save(lessonBooked);

        Lesson selectedLesson = super.createTestLesson("testLessonId2", "testDescription2");
        Set<Lesson> selectedLessons = new HashSet<>();
        selectedLessons.add(selectedLesson);

        MockHttpSession mockHttpSession = super.getNewMockHttpSession(true);
        mockHttpSession.setAttribute("selectedLessons", selectedLessons);

        this.mockMvc.perform(post("/lessons/selected/finalise")
                .session(mockHttpSession))
                .andExpect(redirectedUrl("/lessons/selected"));

        List<LessonBooked> lessonsBooked = lessonBookedRepository.findAll();
        assertEquals(1, lessonsBooked.size());
        assertEquals(Integer.valueOf(1), lessonsBooked.get(0).getClientid());
        assertEquals("testLessonId2", lessonsBooked.get(0).getLessonid());
    }

}
