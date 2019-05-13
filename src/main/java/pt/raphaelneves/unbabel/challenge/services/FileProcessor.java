/**
 * This class is used to handle the incoming file containing all logs from translation events.
 * Here you can read the file, extract its lines and convert each line into a valid data model used by the application.
 * @author Raphael Neves
 **/
package pt.raphaelneves.unbabel.challenge.services;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import pt.raphaelneves.unbabel.challenge.models.MetricResponse;
import pt.raphaelneves.unbabel.challenge.models.Translation;

public class FileProcessor {

    /**
     * Load the file into the application context
     * @param filePath The file absolute path
     * @return File The loaded file
     * @throws RuntimeException When the filePath is not defined
     * @throws RuntimeException When the file is not found
     */
    public File loadFileFrom(String filePath) {
        if(Objects.isNull(filePath)) {
            throw new RuntimeException("Must specify the file path to be processed");
        }
        File file = new File(filePath);
        if(!file.exists()) {
            throw new RuntimeException("File not found");
        }
        return file;
    }

    /**
     * Read all lines from loaded file
     * @param file The loaded incoming file
     * @return List<String> A list containing all the lines from the file as String
     * @throws IOException When something went wrong while reading the file lines
     */
    public List<String> extractFileLines(File file) {
        List<String> fileLines;
        try {
            fileLines = Files.readAllLines(Paths.get(file.getAbsolutePath()));
        } catch (IOException e) {
            throw new RuntimeException("Error while reading file lines.");
        }
        return fileLines;
    }

    /**
     * Convert the extracted lines from the file into a {@link pt.raphaelneves.unbabel.challenge.models.Translation} collection
     * @param lines The extracted lines from the incoming file
     * @return List<Translation> A list of Translation objects
     * @throws RuntimeException When the file is empty
     */
    public List<Translation> convertFileLines(List<String> lines) {
        if(Objects.isNull(lines) || lines.isEmpty()) {
            throw new RuntimeException("Error while converting the file");
        }
        return lines.stream().map(this::convertFileLineToObject).collect(Collectors.toList());
    }


    /**
     * Convert a single line into a {@link pt.raphaelneves.unbabel.challenge.models.Translation} object
     * @param line The file line to be converted
     * @return Translation The line converted into a Translation object
     * @throws IOException When something went wrong while deserializing the information
     */
    private Translation convertFileLineToObject(String line) {
        ObjectMapper mapper = new ObjectMapper();
        Translation translation;
        try {
            translation = mapper.readValue(line, Translation.class);
        } catch (IOException e) {
            throw new RuntimeException("Error while converting the file line " + line);
        }
        return translation;
    }

    /**
     * Create a file on user.home folder containing the result from the average event duration calculation
     * @param metrics A collection of extracted metrics
     * @return String The full path of the created file
     * @throws IOException When something went wrong while creating the file
     */
    public String createOutputFile(List<MetricResponse> metrics, String pathToSaveFile) {
        File responseFile = new File(pathToSaveFile);
        metrics.forEach(metric -> {
            try {
                FileUtils.write(responseFile, String.format("%s\n", metric.toString()), Charset.defaultCharset(), true);
            } catch (IOException e) {
                throw new RuntimeException("Unable to create the response file");
            }
        });
        return responseFile.getAbsolutePath();
    }

}
