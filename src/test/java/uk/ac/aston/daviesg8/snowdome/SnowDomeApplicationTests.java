package uk.ac.aston.daviesg8.snowdome;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.aston.daviesg8.snowdome.controller.BookingController;
import uk.ac.aston.daviesg8.snowdome.controller.ClientController;
import uk.ac.aston.daviesg8.snowdome.controller.IndexController;
import uk.ac.aston.daviesg8.snowdome.controller.LessonController;
import uk.ac.aston.daviesg8.snowdome.repository.ClientRepository;
import uk.ac.aston.daviesg8.snowdome.repository.LessonBookedRepository;
import uk.ac.aston.daviesg8.snowdome.repository.LessonRepository;
import uk.ac.aston.daviesg8.snowdome.service.BookingService;
import uk.ac.aston.daviesg8.snowdome.service.ClientService;
import uk.ac.aston.daviesg8.snowdome.service.LessonService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SnowDomeApplicationTests {

  @Autowired
  private ClientController clientController;

  @Autowired
  private IndexController indexController;

  @Autowired
  private LessonController lessonController;

  @Autowired
  private BookingController bookingController;

  @Autowired
  private ClientRepository clientRepository;

  @Autowired
  private LessonRepository lessonRepository;

  @Autowired
  private LessonBookedRepository lessonBookedRepository;

  @Autowired
  private ClientService clientService;

  @Autowired
  private LessonService lessonService;

  @Autowired
  private BookingService bookingService;

  @Test
  public void contextLoads() {
    assertNotNull(clientController);
    assertNotNull(indexController);
    assertNotNull(lessonController);
    assertNotNull(bookingController);
    assertNotNull(clientRepository);
    assertNotNull(lessonRepository);
    assertNotNull(lessonBookedRepository);
    assertNotNull(clientService);
    assertNotNull(lessonService);
    assertNotNull(bookingService);
  }

}
