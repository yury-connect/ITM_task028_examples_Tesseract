package dev.it_mentor.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
//@NoArgsConstructor
//@EqualsAndHashCode(exclude = "id")
//@ToString
//@Getter
@Data
@Table(name = "texttes")
public class RecognizedText {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "rt_info", columnDefinition = "TEXT")
    private String fileInfo;

    @Column(name = "rt_text", columnDefinition = "TEXT")
    private String text;

    @Column(name = "rt_date")
    @CreationTimestamp
    private LocalDateTime recognitionDate;
}

