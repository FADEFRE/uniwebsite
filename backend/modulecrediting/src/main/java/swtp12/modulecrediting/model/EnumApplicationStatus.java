package swtp12.modulecrediting.model;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Enum for the status of an {@link Application}.
 * @see Application
 */
public enum EnumApplicationStatus {
    NEU,
    FORMFEHLER,
    @JsonProperty("IN BEARBEITUNG") IN_BEARBEITUNG,
    STUDIENBÜRO,
    PRÜFUNGSAUSSCHUSS,
    ABGESCHLOSSEN
}
