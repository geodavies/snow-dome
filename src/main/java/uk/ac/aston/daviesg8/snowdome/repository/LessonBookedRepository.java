package uk.ac.aston.daviesg8.snowdome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.aston.daviesg8.snowdome.model.entity.LessonBooked;

/**
 * This repository is used to interact with the 'lessons_booked' table
 */
@Repository
public interface LessonBookedRepository extends JpaRepository<LessonBooked, Long> {

  void deleteAllByClientid(Integer clientid);

}
