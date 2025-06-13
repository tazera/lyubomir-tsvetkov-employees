package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Team information
 * Used to transfer data between the service and the controller
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamDTO {
    private long firstEmployeeId;
    private long secondEmployeeId;
    private long totalDuration;
}
