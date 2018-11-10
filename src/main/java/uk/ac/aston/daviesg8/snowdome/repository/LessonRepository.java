package uk.ac.aston.daviesg8.snowdome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.aston.daviesg8.snowdome.model.entity.Lesson;

import java.util.Optional;

/**
 * This repository is used to interact with the 'lessons' table
 */
@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    Optional<Lesson> findByLessonid(String lessonId);

}
