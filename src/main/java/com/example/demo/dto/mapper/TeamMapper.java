package com.example.demo.dto.mapper;

import com.example.demo.dto.TeamDTO;
import com.example.demo.model.Team;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class to convert between Team domain objects and TeamDTO objects
 */
@Component
public class TeamMapper {

    /**
     * Converts a Team domain object to a TeamDTO
     *
     * @param team The Team domain object
     * @return The corresponding TeamDTO
     */
    public TeamDTO toDto(Team team) {
        if (team == null) {
            return null;
        }

        return TeamDTO.builder()
                .firstEmployeeId(team.getFirstEmployeeId())
                .secondEmployeeId(team.getSecondEmployeeId())
                .totalDuration(team.getTotalDuration())
                .build();
    }

    /**
     * Converts a list of Team domain objects to a list of TeamDTOs
     *
     * @param teams List of Team domain objects
     * @return List of corresponding TeamDTOs
     */
    public List<TeamDTO> toDtoList(List<Team> teams) {
        if (teams == null) {
            return null;
        }

        return teams.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Converts a TeamDTO to a Team domain object
     *
     * @param teamDTO The TeamDTO
     * @return The corresponding Team domain object
     */
    public Team toEntity(TeamDTO teamDTO) {
        if (teamDTO == null) {
            return null;
        }

        Team team = new Team();
        team.setFirstEmployeeId(teamDTO.getFirstEmployeeId());
        team.setSecondEmployeeId(teamDTO.getSecondEmployeeId());
        team.setTotalDuration(teamDTO.getTotalDuration());

        return team;
    }
}
