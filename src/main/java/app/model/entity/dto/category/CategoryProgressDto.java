package app.model.entity.dto.category;

import app.model.entity.dto.activity.ActivityEntryDto;
import app.model.entity.user.ProgressLevel;
import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryProgressDto {
    private String categoryName;   // "education", "physical" и т.н.
    private ProgressLevel level;   // BEGINNER / INTERMEDIATE / ADVANCED / MASTER
    private int points;            // educationPoints и т.н.
    private List<ActivityEntryDto> activities; // всички логнати активити за тази категория
}
