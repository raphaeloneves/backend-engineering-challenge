/**
 * This class represents the incoming data as a valid data model from the file passed as system parameter. *
 * It uses the Project Lombok to avoid boilerplate code, such as Setters and Getters, and also provide an
 * out of the box Builder class.
 * @author Raphael Neves
 **/

package pt.raphaelneves.unbabel.challenge.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Translation implements Serializable {


    /**
     * The timestamp of when translation event has occurred
     */
    private LocalDateTime timestamp;
    /**
     * The translation event identifier
     * Receive a @JsonProperty annotation to help Jackson's Serialization/Deserialization process,
     * since the file attribute is different from class attribute
     */
    @JsonProperty("translation_id")
    private String translationId;

    /**
     * The source language
     * Receive a @JsonProperty annotation to help Jackson's Serialization/Deserialization process,
     * since the file attribute is different from class attribute
     */
    @JsonProperty("source_language")
    private String sourceLanguage;

    /**
     * The target language
     * Receive a @JsonProperty annotation to help Jackson's Serialization/Deserialization process,
     * since the file attribute is different from class attribute
     */
    @JsonProperty("target_language")
    private String targetLanguage;

    /**
     * The client who's requesting the translation
     * Receive a @JsonProperty annotation to help Jackson's Serialization/Deserialization process,
     * since the file attribute is different from class attribute
     */
    @JsonProperty("client_name")
    private String clientName;

    /**
     * The event that will be executed
     * Receive a @JsonProperty annotation to help Jackson's Serialization/Deserialization process,
     * since the file attribute is different from class attribute
     */
    @JsonProperty("event_name")
    private String eventName;

    /**
     * The translation event duration
     */
    private Long duration;

    /**
     * The amount of word that have been translated
     * Receive a @JsonProperty annotation to help Jackson's Serialization/Deserialization process,
     * since the file attribute is different from class attribute
     */
    @JsonProperty("nr_words")
    private Integer numberWords;


    /**
     * Convert the timestamp as String to a valid LocalDateTime format
     * @param timestamp The timestamp as String
     * @return The timestamp converted into LocalDateTime
     */
    public void setTimestamp(String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.timestamp = LocalDateTime.parse(timestamp.substring(0, timestamp.lastIndexOf(".")), formatter);
    }


    /**
     * Use the translation id to distinguish if two Translation objects are equal.
     * @param o The Translation object to be compared
     * @return boolean If the objects are equal
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Translation that = (Translation) o;
        return translationId.equals(that.translationId);
    }

    /**
     * Use the translation id as hashcode when used on hash tables
     */
    @Override
    public int hashCode() {
        return Objects.hash(translationId);
    }
}
