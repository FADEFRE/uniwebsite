package swtp12.modulecrediting.repository.projection;

import java.time.LocalDate;

public interface ApplicationProjection {
    Long getId();
    String getFullStatus();
    LocalDate getCreationDate();
    LocalDate getDecisionDate();
}
