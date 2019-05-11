package pt.raphaelneves.unbabel.challenge.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import pt.raphaelneves.unbabel.challenge.models.Translation;

public class FileProcessor {

    public File readFileFrom(String filePath) {
        File file = new File(filePath);
        if(!file.exists()) {
            throw new RuntimeException("File not found");
        }
        return file;
    }

    public List<Translation> extractItemsFrom(File file) {
        List<Translation> translations = null;
        try {
            List<String> lines = Files.readAllLines(Paths.get(file.getAbsolutePath()));
            translations = lines.stream().map(this::convertFileLineInObject).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return translations;
    }

    public Translation convertFileLineInObject(String line) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Translation translation = null;
        try {
            translation = mapper.readValue(line, Translation.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return translation;
    }
}
