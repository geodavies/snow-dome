package uk.ac.aston.daviesg8.snowdome.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.aston.daviesg8.snowdome.model.entity.Client;
import uk.ac.aston.daviesg8.snowdome.model.entity.Lesson;
import uk.ac.aston.daviesg8.snowdome.model.entity.LessonBooked;
import uk.ac.aston.daviesg8.snowdome.repository.LessonBookedRepository;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This service class is responsible for handling all booking related functions
 */
@Slf4j
@Service
public class BookingService {

    private final LessonBookedRepository lessonBookedRepository;

    @Autowired
    public BookingService(LessonBookedRepository lessonBookedRepository) {
        this.lessonBookedRepository = lessonBookedRepository;
    }

    /**
     * Deletes and existing lesson booking for a give client
     *
     * @param client the client to delete booking for
     */
    @Transactional
    public void deleteExistingBookingsForClient(Client client) {
        log.info("Deleting all existing bookings for client with ID: {}", client.getClientid());
        lessonBookedRepository.deleteAllByClientid(client.getClientid());
    }

    /**
     * Adds bookings for the client for each selected lesson provided
     *
     * @param client          the client to add bookings for
     * @param selectedLessons the selected lessons to book
     */
    @Transactional
    public void addNewClientBookings(Client client, Set<Lesson> selectedLessons) {
        log.info("Adding new lesson bookings for client with ID: {}", client.getClientid());
        Set<LessonBooked> lessonsToBook = selectedLessons.stream()
                .map(lesson -> {
                    LessonBooked lessonBooked = new LessonBooked();
                    lessonBooked.setClientid(client.getClientid());
                    lessonBooked.setLessonid(lesson.getLessonid());
                    return lessonBooked;
                })
                .collect(Collectors.toSet());

        lessonBookedRepository.saveAll(lessonsToBook);
    }
}
