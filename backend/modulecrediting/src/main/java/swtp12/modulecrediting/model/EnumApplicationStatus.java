package swtp12.modulecrediting.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EnumApplicationStatus {
    NEU,
    FORMFEHLER,
    @JsonProperty("IN BEARBEITUNG") IN_BEARBEITUNG,
    STUDIENBÜRO,
    PRÜFUNGSAUSSCHUSS,
    ABGESCHLOSSEN
}
