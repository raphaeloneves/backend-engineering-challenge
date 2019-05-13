package pt.raphaelneves.unbabel.challenge.services;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pt.raphaelneves.unbabel.challenge.models.MetricResponse;
import pt.raphaelneves.unbabel.challenge.models.Translation;

@DisplayName("Test cases for the FileProcessor class")
public class FileProcessorTest {

    static FileProcessor fileProcessor;
    static MetricService metricService;

    @BeforeAll
    static void createTestedServiceInstance() {
        fileProcessor = new FileProcessor();
        metricService = new MetricService();
    }

    @AfterAll
    static void deleteOutputFolderAndFiles() {
        File output = new File(String.format("%s/unbabel-challenge", System.getProperty("user.home")));
        Arrays.stream(output.listFiles()).forEach(File::delete);
        output.delete();
    }

    @Test
    @DisplayName("Throw exception if the file path has not been defined")
    void loadFileFrom_filePathNotDefined() {
        Assertions.assertThrows(RuntimeException.class, () -> fileProcessor.loadFileFrom(null));
    }

    @Test
    @DisplayName("Throw exception if the file does not exist")
    void loadFileFrom_nonExistingFile() {
        String filePath = "some_strange_file.json";
        Assertions.assertThrows(RuntimeException.class, () -> fileProcessor.loadFileFrom(filePath));
    }

    @Test
    @DisplayName("Load and existing file")
    void loadFileFrom_existingFile() {
        URL fileUrl = getClass().getClassLoader().getResource("single_event.json");
        File loadedFile = fileProcessor.loadFileFrom(fileUrl.getPath());
        Assertions.assertNotEquals(null, loadedFile);
        Assertions.assertEquals(Boolean.TRUE, loadedFile.exists());
        Assertions.assertEquals(Boolean.TRUE, loadedFile.isFile());
        Assertions.assertEquals("single_event.json", loadedFile.getName());
    }

    @Test
    @DisplayName("Read a file that contains one line")
    void extractFileLines_fileWithOneLine() {
        URL fileUrl = getClass().getClassLoader().getResource("single_event.json");
        File loadedFile = fileProcessor.loadFileFrom(fileUrl.getPath());
        List<String> lines = fileProcessor.extractFileLines(loadedFile);
        Assertions.assertNotEquals(null, lines);
        Assertions.assertEquals(1, lines.size());
    }

    @Test
    @DisplayName("Read a file that contains multiple lines")
    void extractFileLines_fileWithMultipleLine() {
        URL fileUrl = getClass().getClassLoader().getResource("full_events.json");
        File loadedFile = fileProcessor.loadFileFrom(fileUrl.getPath());
        List<String> fileLines = fileProcessor.extractFileLines(loadedFile);
        Assertions.assertNotEquals(null, fileLines);
        Assertions.assertEquals(21, fileLines.size());
    }

    @Test
    @DisplayName("Read an empty file")
    void extractFileLines_emptyFile() {
        URL fileUrl = getClass().getClassLoader().getResource("empty_file.json");
        File loadedFile = fileProcessor.loadFileFrom(fileUrl.getPath());
        List<String> fileLines = fileProcessor.extractFileLines(loadedFile);
        Assertions.assertNotEquals(null, fileLines);
        Assertions.assertEquals(Boolean.TRUE, fileLines.isEmpty());
    }

    @Test
    @DisplayName("Throw exception when converting an empty file")
    void convertFileLines_emptyFile() {
        URL fileUrl = getClass().getClassLoader().getResource("empty_file.json");
        File loadedFile = fileProcessor.loadFileFrom(fileUrl.getPath());
        List<String> fileLines = fileProcessor.extractFileLines(loadedFile);
        Assertions.assertThrows(RuntimeException.class, () -> fileProcessor.convertFileLines(fileLines));
    }

    @Test
    @DisplayName("Throw exception when no lines were specified")
    void convertFileLines_lineWereNotSpecified() {
        Assertions.assertThrows(RuntimeException.class, () -> fileProcessor.convertFileLines(null));
    }

    @Test
    @DisplayName("Throw exception when no lines were specified")
    void convertFileLines_convertLinesInTranslationObjects() {
        URL fileUrl = getClass().getClassLoader().getResource("full_events.json");
        File loadedFile = fileProcessor.loadFileFrom(fileUrl.getPath());
        List<String> fileLines = fileProcessor.extractFileLines(loadedFile);
        List<Translation> translations = fileProcessor.convertFileLines(fileLines);
        Assertions.assertNotEquals(null, translations);
        Assertions.assertNotEquals(Boolean.TRUE, translations.isEmpty());
        Assertions.assertEquals(21, translations.size());
        Assertions.assertEquals("fr", translations.get(0).getTargetLanguage());
        Assertions.assertEquals("en", translations.get(3).getSourceLanguage());
    }

    @Test
    @DisplayName("Throw exception while serializing/deserializing an invalid line")
    void convertFileLines_withInvalidLine() {
        URL fileUrl = getClass().getClassLoader().getResource("invalid_file_line.json");
        File loadedFile = fileProcessor.loadFileFrom(fileUrl.getPath());
        List<String> fileLines = fileProcessor.extractFileLines(loadedFile);
        Assertions.assertThrows(RuntimeException.class, () -> fileProcessor.convertFileLines(fileLines));
    }


    @Test
    @DisplayName("Create the output file containing the report result")
    void createOutputFile() {
        File destinationPath = new File(String.format("%s/unbabel-challenge", System.getProperty("user.home")));
        URL fileUrl = getClass().getClassLoader().getResource("full_events.json");
        File loadedFile = fileProcessor.loadFileFrom(fileUrl.getPath());
        List<String> fileLines = fileProcessor.extractFileLines(loadedFile);
        List<Translation> translations = fileProcessor.convertFileLines(fileLines);
        List<MetricResponse> metricResponses = metricService.calculateAverageEventDuration(translations, 10);

        Assertions.assertDoesNotThrow(() -> fileProcessor.createOutputFile(metricResponses));
        Assertions.assertEquals(Boolean.TRUE, destinationPath.exists());
        Assertions.assertEquals(Boolean.TRUE, destinationPath.isDirectory());
        Assertions.assertEquals(1, destinationPath.listFiles().length);
    }
}
