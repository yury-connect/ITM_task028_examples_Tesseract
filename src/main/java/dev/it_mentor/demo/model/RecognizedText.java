package dev.it_mentor.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;


@Entity
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id", "recognitionDate"})
@ToString
@Getter
public class RecognizedText {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long id;

    @Column(name = "rt_text")
    private String text;

    @Column(name = "rt_date")
    private LocalDateTime recognitionDate;
}

