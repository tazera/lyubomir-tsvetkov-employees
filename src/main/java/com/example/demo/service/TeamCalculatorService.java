package com.example.demo.service;

import com.example.demo.model.EmployeeProject;
import com.example.demo.model.Team;
import com.example.demo.service.internal.Event;
import com.example.demo.service.internal.PairKey;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeamCalculatorService {

    private static final int EVENTS_PER_ASSIGNMENT = 2;

    /**
     * Computes a descending list of all employee pairs who have ever overlapped
     * together, along with their total days together.
     * The top result is the pair with the most days together.
     */
    public List<Team> calculateTeams(List<EmployeeProject> assignments) {
        Map<Long, List<EmployeeProject>> assignmentsByProject = groupByProject(assignments);
        Map<PairKey, Long> totals = new HashMap<>();

        for (List<EmployeeProject> projectAssignments : assignmentsByProject.values()) {
            List<Event> events = buildEvents(projectAssignments);
            accumulateProjectOverlap(events, totals);
        }
        return mapToSortedTeamList(totals);
    }


    // Method that group the employees by project
    private Map<Long, List<EmployeeProject>> groupByProject(List<EmployeeProject> assignments) {
        return assignments.stream()
                .collect(Collectors.groupingBy(EmployeeProject::getProjectId));
    }

    // Build and sort the sweep‑line events for one project.
    private List<Event> buildEvents(List<EmployeeProject> projectAssignments) {
        List<Event> events = new ArrayList<>(projectAssignments.size() * EVENTS_PER_ASSIGNMENT);
        for (EmployeeProject assignment : projectAssignments) {
            // Enter event on the start date (inclusive)
            // Leave event the day *after* the end date to count the end day inclusively

            events.add(new Event(assignment.getDateFrom(), assignment.getEmpId(), true));
            LocalDate endDate = Optional.ofNullable(assignment.getDateTo()).orElse(LocalDate.now());
            events.add(new Event(endDate.plusDays(1), assignment.getEmpId(), false));
        }
        events.sort(Comparator.comparing(e -> e.date));
        return events;
    }

    // Process one project’s event list and update the global totals map.
    private void accumulateProjectOverlap(List<Event> events, Map<PairKey, Long> totals) {
        Set<Long> activeEmployees = new HashSet<>();

        for (int i = 0; i < events.size() - 1; i++) {
            Event current = events.get(i);
            if (current.enter) {
                activeEmployees.add(current.employeeId);
            } else {
                activeEmployees.remove(current.employeeId);
            }

            long span = ChronoUnit.DAYS.between(current.date, events.get(i + 1).date);
            if (span > 0 && activeEmployees.size() > 1) {
                addSpanToActivePairs(activeEmployees, span, totals);
            }
        }
    }

    /**
     * Add <span> days to every unordered pair currently in the active set.
     */
    private void addSpanToActivePairs(Set<Long> activeEmployees, long span, Map<PairKey, Long> totals) {
        List<Long> ids = new ArrayList<>(activeEmployees);
        for (int i = 0; i < ids.size() - 1; i++) {
            for (int j = i + 1; j < ids.size(); j++) {
                PairKey key = new PairKey(ids.get(i), ids.get(j));
                totals.merge(key, span, Long::sum);
            }
        }
    }

    /**
     * Convert the total map to a sorted list of {@link Team} entities.
     */
    private List<Team> mapToSortedTeamList(Map<PairKey, Long> totals) {
        return totals.entrySet().stream()
                .map(entry -> {
                    Team team = new Team();
                    team.setFirstEmployeeId(entry.getKey().first);
                    team.setSecondEmployeeId(entry.getKey().second);
                    team.setTotalDuration(entry.getValue());
                    return team;
                })
                .sorted(Comparator.comparingLong(Team::getTotalDuration).reversed())
                .collect(Collectors.toList());
    }

}
