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
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenerationTime;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "recognized_text")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class RecognizedText {

    public RecognizedText (String fileInfo, String text) {
        this.fileInfo = fileInfo;
        this.text = text;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE) // Запрещаем сеттер
    private UUID id;

    @Column(name = "rt_info", columnDefinition = "TEXT")
    @EqualsAndHashCode.Include
    @ToString.Include
    private String fileInfo;

    @Column(name = "rt_text", columnDefinition = "TEXT")
    @EqualsAndHashCode.Include
    @ToString.Include
    private String text;

    @Column(name = "rt_date", updatable = false, columnDefinition = "TIMESTAMP(0)")
    @CreationTimestamp
    @Setter(AccessLevel.NONE) // Запрещаем сеттер
    @ColumnDefault("CURRENT_TIMESTAMP(0)")
    @ToString.Include
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

