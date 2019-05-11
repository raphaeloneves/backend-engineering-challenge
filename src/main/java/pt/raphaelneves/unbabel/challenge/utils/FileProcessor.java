/**
 * This class is used to handle the incoming file containing all logs from translation events.
 * Here you can read the file, extract its lines and convert each line into a valid data model used by the application.
 * @author Raphael Neves
 **/
package pt.raphaelneves.unbabel.challenge.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import pt.raphaelneves.unbabel.challenge.models.Translation;

public class FileProcessor {

    /**
     * Load the file into the application context
     * @param filePath The file absolute path
     * @return File The loaded file
     * @throws RuntimeException When the filePath is not defined
     * @throws RuntimeException When the file is not found
     */
    public File readFileFrom(String filePath) {
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
     * Read each file line that will be converted into a {@link pt.raphaelneves.unbabel.challenge.models.Translation} collection
     * @param file The loaded incoming file
     * @return List<Translation> A list of Translation objects
     * @throws IOException When something went wrong while reading the file lines
     */
    public List<Translation> extractItemsFrom(File file) {
        List<Translation> translations;
        try {
            List<String> lines = Files.readAllLines(Paths.get(file.getAbsolutePath()));
            translations = lines.stream().map(this::convertFileLineInObject).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error while processing the file " + file.getName());
        }
        return translations;
    }


    /**
     * Convert the line extracted from the file in a {@link pt.raphaelneves.unbabel.challenge.models.Translation} object
     * @param line The line extracted from the file
     * @return Translation The line converted in a Translation object
     * @throws IOException When something went wrong while deserializing the information
     */
    private Translation convertFileLineInObject(String line) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Translation translation;
        try {
            translation = mapper.readValue(line, Translation.class);
        } catch (IOException e) {
            throw new RuntimeException("Error while converting the file line " + line);
        }
        return translation;
    }
}
