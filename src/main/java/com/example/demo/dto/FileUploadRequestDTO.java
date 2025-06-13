package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * DTO for file upload requests
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadRequestDTO {
    private MultipartFile file;

    public boolean isValid() {
        return Optional.ofNullable(file)
                .filter(f -> !f.isEmpty())
                .filter(f -> {
                    String filename = f.getOriginalFilename();
                    return filename != null && filename.toLowerCase().endsWith(".csv");
                }).isPresent();
    }
}
