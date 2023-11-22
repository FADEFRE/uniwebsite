package swtp12.modulecrediting.repository;

import java.time.LocalDate;

public interface ApplicationProjection {
    Long getId();
    String getFullStatus();
    LocalDate getCreationDate();
    LocalDate getDecisionDate();
}
