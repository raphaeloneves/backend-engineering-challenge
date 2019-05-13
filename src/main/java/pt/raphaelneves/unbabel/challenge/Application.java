/**
 * Entrypoint class to the challenge suggested by Unbabel.
 * This class will trigger all processes related to metric extraction.
 * @author Raphael Neves
 **/

package pt.raphaelneves.unbabel.challenge;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import pt.raphaelneves.unbabel.challenge.models.MetricResponse;
import pt.raphaelneves.unbabel.challenge.models.Translation;
import pt.raphaelneves.unbabel.challenge.services.MetricService;
import pt.raphaelneves.unbabel.challenge.services.FileProcessor;

public class Application {

    public static void main(String[] args) {
        FileProcessor fileProcessor = new FileProcessor();
        MetricService metricService = new MetricService();

        System.out.println("#######################################");
        System.out.println("So, let's try this challenge, buddy?");
        System.out.println("#######################################");

        Scanner scanner = new Scanner(System.in);

        System.out.println("1- Enter the absolute path from your events file:");
        String filePath = scanner.next();

        System.out.println("2- Enter the window size to extract the performance metrics (in minutes):");
        Integer windowSize = Integer.parseInt(scanner.next());

        File fileToProcess = fileProcessor.loadFileFrom(filePath);
        List<String> fileLines = fileProcessor.extractFileLines(fileToProcess);
        List<Translation> translations = fileProcessor.convertFileLines(fileLines);

        List<MetricResponse> metrics = metricService.calculateAverageEventDuration(translations, windowSize);
        String outputFile = fileProcessor.createOutputFile(metrics);

        metrics.forEach(System.out::println);
        System.out.println(String.format("The above report was exported to the file: %s", outputFile));
    }
}
