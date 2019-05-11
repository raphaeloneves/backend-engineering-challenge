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

    public void calculateAverageResponse(List<Translation> translations, Integer windowSize) {
        orderTranslationByDateTime(translations);
        List<Translation> dataSet = extractDataWithinWindowSize(translations, windowSize);
        Map<String, List<Long>> group = groupTranslationDurationByDateKey(dataSet);
        calculate(group);
    }

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

    private String convertDataAsIndex(final LocalDateTime timestamp) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:00");
        return dateTimeFormatter.format(timestamp);
    }

    private List<Translation> extractDataWithinWindowSize(final List<Translation> translations, final Integer windowSize) {
        LocalDateTime dateTimeLimit = defineWindowLimitDateTime(translations.get(0).getTimestamp(), windowSize);
        return translations.stream()
                           .filter(translation -> translation.getTimestamp().isEqual(dateTimeLimit)
                                   || translation.getTimestamp().isAfter(dateTimeLimit))
                           .collect(Collectors.toList());
    }

    private LocalDateTime defineWindowLimitDateTime(final LocalDateTime higherDateTime, final Integer windowSize) {
        return higherDateTime.minusMinutes(windowSize);
    }

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
