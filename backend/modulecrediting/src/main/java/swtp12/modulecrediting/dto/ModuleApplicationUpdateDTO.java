package swtp12.modulecrediting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ModuleApplicationUpdateDTO {
    private Boolean delete;
    private Long id;

    private String name;
    private String university;
    private Integer points;
    private String pointSystem;

    @Override
    public String toString() {
        return "ModuleApplicationUpdateDTO{" +
                "delete=" + delete +
                ", id=" + id +
                ", moduleName='" + name + '\'' +
                ", university='" + university + '\'' +
                ", points=" + points +
                ", pointSystem='" + pointSystem + '\'' +
                '}';
    }
}
