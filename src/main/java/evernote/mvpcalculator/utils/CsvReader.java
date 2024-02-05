package evernote.mvpcalculator.utils;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import evernote.mvpcalculator.exception.NoFileExtensionException;
import evernote.mvpcalculator.exception.WrongFileExtensionException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvReader {

    @Value("${file.row.delimiter}")
    private Character rowDelimiter;

    private static final String REQUIRED_EXTENSION = "csv";

    public List<List<String[]>> readList(List<MultipartFile> csvFiles) throws IOException, CsvException {
        List<List<String[]>> rows = new ArrayList<>();
        for (MultipartFile csvFile : csvFiles) {
            rows.add(read(csvFile));
        }
        return rows;
    }

    public List<String[]> read(MultipartFile csvFile) throws IOException, CsvException {
        String extension = FilenameUtils.getExtension(csvFile.getOriginalFilename());
        if (extension == null) {
            throw new NoFileExtensionException(REQUIRED_EXTENSION);
        }
        if (!extension.equals(REQUIRED_EXTENSION)) {
            throw new WrongFileExtensionException(REQUIRED_EXTENSION, extension);
        }
        Reader reader = new InputStreamReader(csvFile.getInputStream());
        try (CSVReader csvReader = new CSVReaderBuilder(reader)
                .withCSVParser(new CSVParserBuilder()
                        .withSeparator(rowDelimiter)
                        .build()
                ).build()) {
            return csvReader.readAll();
        }
    }
}
