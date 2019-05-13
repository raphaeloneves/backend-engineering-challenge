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

        System.out.println("###########################################");
        System.out.println("Hello Unbabel friends, let's test this app?");
        System.out.println("###########################################");

        Scanner scanner = new Scanner(System.in);

        System.out.print("1- Enter the absolute path from the file to be analyzed: ");
        String filePath = scanner.next();

        System.out.print("2- Enter the window size to extract the events metrics (in minutes): ");
        Integer windowSize = Integer.parseInt(scanner.next());

        File fileToProcess = fileProcessor.loadFileFrom(filePath);
        List<String> fileLines = fileProcessor.extractFileLines(fileToProcess);
        List<Translation> translations = fileProcessor.convertFileLines(fileLines);

        List<MetricResponse> metrics = metricService.calculateAverageEventDuration(translations, windowSize);
        String outputFile = fileProcessor.createOutputFile(metrics);

        System.out.println("\nResult: \n");
        metrics.forEach(System.out::println);
        System.out.println(String.format("\n>>> The above report was exported to: %s <<<", outputFile));
    }
}
