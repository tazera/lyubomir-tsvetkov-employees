package com.example.demo.controller;

import com.example.demo.dto.FileUploadRequestDTO;
import com.example.demo.dto.TeamDTO;
import com.example.demo.dto.mapper.TeamMapper;
import com.example.demo.exception.CsvParseException;
import com.example.demo.model.EmployeeProject;
import com.example.demo.model.Team;
import com.example.demo.service.CsvLoadService;
import com.example.demo.service.TeamCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TeamTestController {

    @Autowired
    private CsvLoadService csvLoadService;
    @Autowired
    private TeamCalculatorService teamCalculatorService;
    @Autowired
    private TeamMapper teamMapper;

    @PostMapping("/teams")
    public ResponseEntity<List<TeamDTO>> testCalculateTeams(@RequestParam("file") MultipartFile file) throws Exception {
        // Create and validate the request DTO
        FileUploadRequestDTO requestDTO = new FileUploadRequestDTO(file);
        if (!requestDTO.isValid()) {
            throw new CsvParseException("Invalid file upload. Please provide a valid CSV file.");
        }

        // Process the file and return results
        try {
            // Load list of EmployeeProject from CSV
            List<EmployeeProject> assignments = csvLoadService.loadCsv(file);
            // Calculate teams
            List<Team> teams = teamCalculatorService.calculateTeams(assignments);
            List<TeamDTO> teamDTOs = teamMapper.toDtoList(teams);
            return ResponseEntity.ok(teamDTOs);
        } catch (Exception e) {
            if (e instanceof CsvParseException) {
                throw e;
            }
            throw new CsvParseException("Error processing CSV file: " + e.getMessage(), e);
        }
    }

    @GetMapping("/teams/from-resource")
    public ResponseEntity<List<TeamDTO>> testCalculateTeamsFromResource() throws Exception {
        try {
            List<EmployeeProject> assignments = csvLoadService.loadCsvFromResource("data/employees.csv");
            List<Team> teams = teamCalculatorService.calculateTeams(assignments);
            List<TeamDTO> teamDTOs = teamMapper.toDtoList(teams);
            return ResponseEntity.ok(teamDTOs);
        } catch (Exception e) {
            // Convert generic exceptions to our specific exception with a better message
            if (e instanceof CsvParseException) {
                throw e;
            }
            throw new CsvParseException("Error processing embedded CSV file: " + e.getMessage(), e);
        }
    }
}