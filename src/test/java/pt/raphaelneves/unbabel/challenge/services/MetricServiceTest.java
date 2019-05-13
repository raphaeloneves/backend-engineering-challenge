package pt.raphaelneves.unbabel.challenge.services;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pt.raphaelneves.unbabel.challenge.models.MetricResponse;
import pt.raphaelneves.unbabel.challenge.models.Translation;

@DisplayName("Test cases to extract events performance metric")
public class MetricServiceTest {

    static MetricService metricService;
    static FileProcessor fileProcessor;

    @BeforeAll
    static void loadTestedServiceAndAuxiliarServices() {
        metricService = new MetricService();
        fileProcessor = new FileProcessor();
    }

    @Test
    @DisplayName("Throw exception if the translation list is empty")
    void calculateAverageEventDuration_emptyTranslationCollection() {
        Assertions.assertThrows(RuntimeException.class, () -> metricService.calculateAverageEventDuration(Collections.EMPTY_LIST, Integer.MAX_VALUE));
    }

    @Test
    @DisplayName("Throw exception if the translation list has not been defined")
    void calculateAverageEventDuration_translationCollectionDoesNotExist() {
        Assertions.assertThrows(RuntimeException.class, () -> metricService.calculateAverageEventDuration(null, Integer.MAX_VALUE));
    }

    @Test
    @DisplayName("Throw exception if the window size is a negative number")
    void calculateAverageEventDuration_negativeWindowSize() {
        List<Translation> translations = Collections.singletonList(Translation.builder().build());
        Assertions.assertThrows(RuntimeException.class, () -> metricService.calculateAverageEventDuration(translations, Integer.MIN_VALUE));
    }

    @Test
    @DisplayName("Throw exception if the window size has not been defined")
    void calculateAverageEventDuration_windowSizeDoesNotExist() {
        List<Translation> translations = Collections.singletonList(Translation.builder().build());
        Assertions.assertThrows(RuntimeException.class, () -> metricService.calculateAverageEventDuration(translations, null));
    }

    @Test
    @DisplayName("Validate the average duration from two events that have occurred at the same timestamp")
    void calculateAverageEventDuration_singleTimestamp() {
        List<Translation> translations = loadTranslationFromFilePath("single_timestamp.json");
        List<MetricResponse> metrics = metricService.calculateAverageEventDuration(translations, Integer.MAX_VALUE);
        Assertions.assertNotEquals(null, metrics);
        Assertions.assertNotEquals(Boolean.TRUE, metrics.isEmpty());
        Assertions.assertEquals(1, metrics.size());
        Assertions.assertEquals(15.0, metrics.get(0).getAverageDeliveryTime());
    }

    @Test
    @DisplayName("Validate the average duration from multiple events with window size equals to 10 minutes")
    void calculateAverageEventDuration_multipleTimestampsWith10MinutesWindowsSize() {
        List<Translation> translations = loadTranslationFromFilePath("full_events.json");
        List<MetricResponse> metrics = metricService.calculateAverageEventDuration(translations, 10);
        Assertions.assertNotEquals(null, metrics);
        Assertions.assertNotEquals(Boolean.TRUE, metrics.isEmpty());
        Assertions.assertEquals(2, metrics.size());
        Assertions.assertEquals(22.0, metrics.get(0).getAverageDeliveryTime());
        Assertions.assertEquals(12.0, metrics.get(1).getAverageDeliveryTime());
    }

    @Test
    @DisplayName("Validate the average duration from multiple events with window size equals to 45 minutes")
    void calculateAverageEventDuration_multipleTimestampsWith45MinutesWindowsSize() {
        List<Translation> translations = loadTranslationFromFilePath("full_events.json");
        List<MetricResponse> metrics = metricService.calculateAverageEventDuration(translations, 45);
        Assertions.assertNotEquals(null, metrics);
        Assertions.assertNotEquals(Boolean.TRUE, metrics.isEmpty());
        Assertions.assertEquals(4, metrics.size());
        Assertions.assertEquals(22.0, metrics.get(0).getAverageDeliveryTime());
        Assertions.assertEquals(12.0, metrics.get(1).getAverageDeliveryTime());
        Assertions.assertEquals(20.8, metrics.get(2).getAverageDeliveryTime());
        Assertions.assertEquals(56.4, metrics.get(3).getAverageDeliveryTime());
    }

    @Test
    @DisplayName("Order the translation event list with the newest event on top and the oldest on bottom")
    void orderTranslationEventsByTimestamp() {
        Translation oldestTranslationOnTheList = Translation.builder().translationId("O").build();
        Translation newestTranslationOnTheList = Translation.builder().translationId("F").build();

        List<Translation> translations = loadTranslationFromFilePath("full_events.json");
        metricService.orderTranslationEventsByTimestamp(translations);

        Assertions.assertEquals(oldestTranslationOnTheList, translations.get(translations.size() - 1));
        Assertions.assertEquals(newestTranslationOnTheList, translations.get(0));
    }

    @Test
    @DisplayName("Extract translation events within the window size of 10 minutes (from 2018-12-26 18:53:58 to 2018-12-26 19:03:58)")
    void extractEventsWithinWindowSize_10MinutesWindowSize() {
        List<Translation> translations = loadTranslationFromFilePath("full_events.json");
        metricService.orderTranslationEventsByTimestamp(translations);
        List<Translation> extractedTranslations = metricService.extractEventsWithinWindowSize(translations, 10);
        Assertions.assertEquals(3, extractedTranslations.size());
    }

    @Test
    @DisplayName("Extract translation events within the window size of 45 minutes (from 2018-12-26 18:18:58 to 2018-12-26 19:03:58)")
    void extractEventsWithinWindowSize_45MinutesWindowSize() {
        List<Translation> translations = loadTranslationFromFilePath("full_events.json");
        metricService.orderTranslationEventsByTimestamp(translations);
        List<Translation> extractedTranslations = metricService.extractEventsWithinWindowSize(translations, 45);
        Assertions.assertEquals(13, extractedTranslations.size());
    }

    @Test
    @DisplayName("Define the initial value for the extraction interval")
    void getInitialTimestampFromWindowSize() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime initialTimestamp = metricService.getInitialTimestampFromWindowSize(now, 10);
        LocalDateTime nowMinusTen = now.minusMinutes(10);
        Assertions.assertEquals(nowMinusTen, initialTimestamp);
    }

    @Test
    @DisplayName("Calculate the average processing time with window size of 45 minutes")
    void calculateAverage() {
        List<Translation> translations = loadTranslationFromFilePath("full_events.json");
        metricService.orderTranslationEventsByTimestamp(translations);
        List<Translation> dataSet = metricService.extractEventsWithinWindowSize(translations, 45);
        Map<String, List<Long>> group = metricService.groupEventsByTimestampAsIndexAndDurationAsValue(dataSet);
        List<MetricResponse> metricResponses = metricService.calculateAverage(group);
        Assertions.assertNotEquals(null, metricResponses);
        Assertions.assertNotEquals(Boolean.TRUE, metricResponses.isEmpty());
        Assertions.assertEquals(4, metricResponses.size());
        Assertions.assertEquals(22.0, metricResponses.get(0).getAverageDeliveryTime());
        Assertions.assertEquals(20.8, metricResponses.get(2).getAverageDeliveryTime());
    }

    @Test
    @DisplayName("Group all events by timestamp and store their duration as a collection (single timestamp)")
    void groupEventsByTimestampAsIndexAndDurationAsValue_withSingleTimestamp() {
        List<Translation> translations = loadTranslationFromFilePath("single_timestamp.json");
        Map<String, List<Long>> groupedEvents = metricService.groupEventsByTimestampAsIndexAndDurationAsValue(translations);
        Assertions.assertNotEquals(null, groupedEvents);
        Assertions.assertNotEquals(Boolean.TRUE, groupedEvents.isEmpty());
        Assertions.assertEquals(1, groupedEvents.size());
        Assertions.assertEquals(2, groupedEvents.get("2018-12-26 18:11:00").size());
    }

    @Test
    @DisplayName("Group all events by timestamp and store their duration as a collection (multiple timestamp)")
    void groupEventsByTimestampAsIndexAndDurationAsValue_withMultipleTimestamps() {
        List<Translation> translations = loadTranslationFromFilePath("full_events.json");
        Map<String, List<Long>> groupedEvents = metricService.groupEventsByTimestampAsIndexAndDurationAsValue(translations);
        Assertions.assertNotEquals(null, groupedEvents);
        Assertions.assertNotEquals(Boolean.TRUE, groupedEvents.isEmpty());
        Assertions.assertEquals(7, groupedEvents.size());
        Assertions.assertEquals(2, groupedEvents.get("2018-12-26 18:11:00").size());
        Assertions.assertEquals(5, groupedEvents.get("2018-12-26 18:37:00").size());
        Assertions.assertEquals(3, groupedEvents.get("2018-12-26 18:15:00").size());
    }

    @Test
    @DisplayName("Convert a LocalDateTime of pattern yyyy-MM-dd HH:mm:ss into a string of pattern yyyy-MM-dd HH:mm:00 to be used as bucket index")
    void convertTimestampAsIndex() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse("2018-12-26 18:23:19", formatter);
        String index = metricService.convertTimestampAsIndex(dateTime);
        Assertions.assertEquals("2018-12-26 18:23:00", index);
    }

    List<Translation> loadTranslationFromFilePath(String filePath) {
        URL fileUrl = getClass().getClassLoader().getResource(filePath);
        File loadedFile = fileProcessor.loadFileFrom(fileUrl.getPath());
        List<String> lines = fileProcessor.extractFileLines(loadedFile);
        return fileProcessor.convertFileLines(lines);
    }
}
