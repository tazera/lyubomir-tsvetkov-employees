package com.example.demo.service;

import com.example.demo.model.EmployeeProject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class CsvLoadService {

    private static final List<DateTimeFormatter> DATE_FORMATS = Arrays.asList(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("dd.MM.yyyy"),
            DateTimeFormatter.ofPattern("MMM dd, yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
    );

    public static LocalDate parseFlexible(String input) {
        if (input == null || input.isEmpty() || input.equalsIgnoreCase("NULL")) {
            return null;
        }

        for (DateTimeFormatter formatter : DATE_FORMATS) {
            try {
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException ignored) {
                // Try next format
            }
        }
        throw new IllegalArgumentException("Unparseable date: " + input);
    }




    // Load data from csv file uploaded via MultipartFile
    public List<EmployeeProject> loadCsv(MultipartFile file) throws Exception {
        return parse(new BufferedReader(new InputStreamReader(file.getInputStream())));
    }

    // Load data from the csv file located in resources
    public List<EmployeeProject> loadCsvFromResource(String resourceName) throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream(resourceName);
        if (is == null) throw new IllegalArgumentException("Resource not found: " + resourceName);
        return parse(new BufferedReader(new InputStreamReader(is)));
    }

    /* Helper method to parse a csv file */
    private List<EmployeeProject> parse(BufferedReader br) throws IOException {
        List<EmployeeProject> rows = new ArrayList<>();
        String line;
        boolean firstLine = true;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (firstLine) { firstLine = false; continue; } // skip header
            if (line.isEmpty() || line.startsWith("#")) continue; // skip blanks / comments

            String[] c = line.split("\\s*,\\s*");
            if (c.length < 4) continue;  // malformed – skip

            long empId    = Long.parseLong(c[0]);
            long project  = Long.parseLong(c[1]);
            LocalDate from = parseFlexible(c[2]);

            // Treat NULL or empty DateTo as an open interval
            LocalDate to = parseFlexible(c[3]);

            rows.add(new EmployeeProject(empId, project, from, to));
        }
        return rows;
    }
}
