package uk.ac.aston.daviesg8.snowdome.controller;

import org.springframework.mock.web.MockHttpSession;
import uk.ac.aston.daviesg8.snowdome.model.entity.Client;
import uk.ac.aston.daviesg8.snowdome.model.entity.Lesson;

import java.sql.Timestamp;
import java.time.Instant;

public abstract class AbstractControllerTest {

    protected final String TEST_CLIENT_USERNAME = "testUser";
    protected final String TEST_CLIENT_PASSWORD = "testPassword";

    protected MockHttpSession getNewMockHttpSession(boolean withLoggedInClient) {
        Client client = new Client();
        client.setClientid(1);
        client.setUsername(TEST_CLIENT_USERNAME);
        client.setPassword(TEST_CLIENT_PASSWORD);

        MockHttpSession mockHttpSession = new MockHttpSession();
        if (withLoggedInClient) {
            mockHttpSession.setAttribute("client", client);
        }
        return mockHttpSession;
    }

    protected Lesson createTestLesson(String lessonId, String description) {
        Lesson selectedLesson = new Lesson();
        selectedLesson.setLessonid(lessonId);
        selectedLesson.setDescription(description);
        selectedLesson.setLevel(1);
        selectedLesson.setStartDateTime(Timestamp.from(Instant.now()));
        selectedLesson.setEndDateTime(Timestamp.from(Instant.now()));
        return selectedLesson;
    }
}
