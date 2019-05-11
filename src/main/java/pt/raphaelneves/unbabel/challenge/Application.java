package pt.raphaelneves.unbabel.challenge;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import pt.raphaelneves.unbabel.challenge.models.Translation;
import pt.raphaelneves.unbabel.challenge.services.MetricService;
import pt.raphaelneves.unbabel.challenge.utils.FileProcessor;

public class Application {

    public static void main(String[] args) {
        FileProcessor fileProcessor = new FileProcessor();

        System.out.println("#######################################");
        System.out.println("So, let's try this challenge, buddy?");
        System.out.println("#######################################");

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the absolute path from your event file:");
        String filePath = scanner.next();

        System.out.println("Enter the window size to extract the performance metrics (in minutes):");
        Integer windowSize = Integer.parseInt(scanner.next());

        File fileToProcess = fileProcessor.readFileFrom(filePath);
        List<Translation> translations = fileProcessor.extractItemsFrom(fileToProcess);

        MetricService metricService = new MetricService();
        metricService.calculateAverageResponse(translations, windowSize);

    }
}
