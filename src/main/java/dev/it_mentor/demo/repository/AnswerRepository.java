package dev.it_mentor.demo.repository;

import dev.it_mentor.demo.model.RecognizedText;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface AnswerRepository extends JpaRepository<RecognizedText, UUID> {
}
