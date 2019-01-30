package uk.ac.aston.daviesg8.snowdome.service;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anySet;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import uk.ac.aston.daviesg8.snowdome.model.entity.Client;
import uk.ac.aston.daviesg8.snowdome.model.entity.Lesson;
import uk.ac.aston.daviesg8.snowdome.repository.LessonBookedRepository;

public class BookingServiceTest {

  private final String TEST_CLIENT_USERNAME = "testUser";
  private final String TEST_CLIENT_PASSWORD = "testPassword";
  private BookingService bookingService;
  private LessonBookedRepository mockLessonBookedRepository;

  @Before
  public void setup() {
    this.mockLessonBookedRepository = mock(LessonBookedRepository.class);
    this.bookingService = new BookingService(mockLessonBookedRepository);
  }

  @Test
  public void deleteExistingBookingForClientSuccess() {
    Client client = getTestClient();

    doNothing().when(mockLessonBookedRepository).deleteAllByClientid(eq(client.getClientid()));

    bookingService.deleteExistingBookingsForClient(client);

    verify(mockLessonBookedRepository, times(1)).deleteAllByClientid(eq(client.getClientid()));
  }

  @Test
  public void addNewClientBookingsSuccess() {
    Client client = getTestClient();
    Lesson lessonToBook = new Lesson();
    lessonToBook.setLessonid("testLessonId1");
    lessonToBook.setDescription("testLessonDescription1");
    lessonToBook.setLevel(1);
    lessonToBook.setStartDateTime(Timestamp.from(Instant.now()));
    lessonToBook.setEndDateTime(Timestamp.from(Instant.now()));

    Set<Lesson> lessonsToBook = new HashSet<>();
    lessonsToBook.add(lessonToBook);

    when(mockLessonBookedRepository.saveAll(anySet())).thenReturn(null);

    bookingService.addNewClientBookings(client, lessonsToBook);

    verify(mockLessonBookedRepository, times(1)).saveAll(anySet());
  }

  @Test
  public void addNewClientBookingsZeroLessonsSelected() {
    Client client = getTestClient();

    Set<Lesson> lessonsToBook = Collections.emptySet();

    when(mockLessonBookedRepository.saveAll(anySet())).thenReturn(null);

    bookingService.addNewClientBookings(client, lessonsToBook);

    verify(mockLessonBookedRepository, times(1)).saveAll(anySet());
  }

  private Client getTestClient() {
    Client client = new Client();
    client.setClientid(1);
    client.setUsername(TEST_CLIENT_USERNAME);
    client.setPassword(TEST_CLIENT_PASSWORD);
    return client;
  }

}
