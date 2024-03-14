package swtp12.modulecrediting.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import swtp12.modulecrediting.model.Application;

/**
 * DTO for creating or updating an {@link Application}
 */
@Getter
@Setter
@NoArgsConstructor
public class ApplicationDTO {
    private String courseLeipzig;
    private List<ModulesConnectionDTO> modulesConnections;
}
