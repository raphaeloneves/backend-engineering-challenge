package pt.raphaelneves.unbabel.challenge.models;

import java.io.Serializable;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
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

    private LocalDateTime timestamp;
    @JsonProperty("translation_id")
    private String translationId;
    @JsonProperty("source_language")
    private String sourceLanguage;
    @JsonProperty("target_language")
    private String targetLanguage;
    @JsonProperty("client_name")
    private String clientName;
    @JsonProperty("event_name")
    private String eventName;
    private Long duration;
    @JsonProperty("nr_words")
    private Integer numberWords;

    public void setTimestamp(String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.timestamp = LocalDateTime.parse(timestamp.substring(0, timestamp.lastIndexOf(".")), formatter);
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(translationId);
    }
}
