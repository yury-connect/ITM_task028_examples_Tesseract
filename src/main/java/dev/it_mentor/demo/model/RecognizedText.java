package dev.it_mentor.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Делаем конструктор приватным
@EqualsAndHashCode(exclude = "id")
@Builder
@Table(name = "texttes")
public class RecognizedText {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "rt_info", columnDefinition = "TEXT")
    private String fileInfo;

    @Column(name = "rt_text", columnDefinition = "TEXT")
    private String text;

    @Column(name = "rt_date", updatable = false)
    @CreationTimestamp
    private LocalDateTime recognitionDate;

    public UUID getId() {
        return id;
    }

    public String getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(String fileInfo) {
        this.fileInfo = fileInfo;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getRecognitionDate() {
        return recognitionDate;
    }
}

