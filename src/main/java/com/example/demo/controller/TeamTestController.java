package com.example.demo.controller;

import com.example.demo.model.EmployeeProject;
import com.example.demo.model.Team;
import com.example.demo.service.CsvLoadService;
import com.example.demo.service.TeamCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @PostMapping("/teams")
    public ResponseEntity<List<Team>> testCalculateTeams(@RequestParam("file") MultipartFile file) {
        try {
            // Load list of EmployeeProject from CSV
            List<EmployeeProject> assignments = csvLoadService.loadCsv(file);
            // Calculate teams
            List<Team> teams = teamCalculatorService.calculateTeams(assignments);
            return ResponseEntity.ok(teams);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/teams/from-resource")
    public ResponseEntity<List<Team>> testCalculateTeamsFromResource() {
        try {
            List<EmployeeProject> assignments = csvLoadService.loadCsvFromResource("data/employees.csv");
            List<Team> teams = teamCalculatorService.calculateTeams(assignments);
            return ResponseEntity.ok(teams);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}