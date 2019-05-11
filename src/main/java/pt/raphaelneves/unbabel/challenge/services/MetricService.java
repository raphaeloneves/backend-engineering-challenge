/**
 * This class is used to calculate the average duration of a translation event and its subprocesses.
 * @author Raphael Neves
 **/

package pt.raphaelneves.unbabel.challenge.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import pt.raphaelneves.unbabel.challenge.models.MetricResponse;
import pt.raphaelneves.unbabel.challenge.models.Translation;

public class MetricService {

    /**
     * Calculate the average translation duration based on a window size interval
     * @param translations A list of translations coming from the processed file
     * @param windowSize The window size to define the extraction interval
     */
    public void calculateAverageResponse(List<Translation> translations, Integer windowSize) {
        orderTranslationByDateTime(translations);
        List<Translation> dataSet = extractDataWithinWindowSize(translations, windowSize);
        Map<String, List<Long>> group = groupTranslationDurationByDateKey(dataSet);
        calculate(group);
    }

    /**
     * Receive a map grouping all translation event duration into indexes as timestamp.
     * and calculate the average duration for each index inside the Map
     * The timestamp key format is defined as "yyyy-MM-dd HH:ss:00".
     * E.g.: 2019-09-09 10:10:00
     * @param group A map grouping the translation event durantion by the timestamp key
     */
    private void calculate(final Map<String, List<Long>> group) {
        group.forEach((k, v) -> {
            OptionalDouble average = v.stream().mapToLong(a -> a).average();
            MetricResponse response = MetricResponse.builder()
                                                    .timestamp(k)
                                                    .averageDeliveryTime(average.orElse(0))
                                                    .build();
            System.out.println(response);
        });
    }

    /**
     * Create a map with the translation events timestamp as bucket index and group all
     * the translation event duration value from each translation object within the window size interval
     * @param translations The list containing translation objects within the window size
     * @return Map<String, List<Long>> The map containing the translation event duration grouped by timestamp
     */
    private Map<String, List<Long>> groupTranslationDurationByDateKey(final List<Translation> translations) {
        Map<String, List<Long>> indexes = new HashMap<>();
        translations.forEach(translation -> {
            String key = convertDataAsIndex(translation.getTimestamp());
            List<Long> dataList = indexes.get(key);
            if (Objects.isNull(dataList)) {
                dataList = new ArrayList<>();
            }
            dataList.add(translation.getDuration());
            indexes.put(key, dataList);
        });
        return indexes;
    }

    /**
     * Convert a translation timestamp as a string key to be used as aggregator in a hash table
     * The key format is defined as "yyyy-MM-dd HH:ss:00".
     * E.g.: 2019-09-09 10:10:00
     * @param timestamp The LocalDateTime to be converted as index
     * @return String The timestamp as String on format "yyyy-MM-dd HH:ss:00"
     */
    private String convertDataAsIndex(final LocalDateTime timestamp) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:00");
        return dateTimeFormatter.format(timestamp);
    }


    /**
     * Extract all translation events that have been occurred within the interval defined by the window size
     * @param translations A translation object list
     * @param windowSize The window size in minutes
     * @return List<Translation> The translations that have occurred within the window size interval
     */
    private List<Translation> extractDataWithinWindowSize(final List<Translation> translations, final Integer windowSize) {
        LocalDateTime dateTimeLimit = defineWindowLimitDateTime(translations.get(0).getTimestamp(), windowSize);
        return translations.stream()
                           .filter(translation -> translation.getTimestamp().isEqual(dateTimeLimit)
                                   || translation.getTimestamp().isAfter(dateTimeLimit))
                           .collect(Collectors.toList());
    }


    /**
     * Define the datetime limit defined by the window size.
     * E.g:
     * Newest event timestamp: 2019-08-08 10:10:56
     * Window size: 10 minutes
     * The interval to extract the metrics will be from 2019-08-08 10:00:56 to 2019-08-08 10:10:56
     * @param dateTime The LocalDateTime from the newest event in the translation list
     * @param windowSize The window size in minutes
     * @return LocalDateTime The initial timestamp for the extraction interval
     */
    private LocalDateTime defineWindowLimitDateTime(final LocalDateTime dateTime, final Integer windowSize) {
        return dateTime.minusMinutes(windowSize);
    }


    /**
     * Sort the translation list to get the newest event on top.
     * @param translations A list of Translation objects
     */
    private void orderTranslationByDateTime(List<Translation> translations) {
        translations.sort((t1, t2) -> {
            if (t1.getTimestamp().isAfter(t2.getTimestamp())) {
                return -1;
            }
            if (t1.getTimestamp().isBefore(t2.getTimestamp())) {
                return 1;
            }
            return 0;
        });
    }
}
