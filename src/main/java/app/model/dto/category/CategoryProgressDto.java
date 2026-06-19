package app.model.dto.category;

import app.model.dto.activity.ActivityEntryDto;
import app.model.entity.user.ProgressLevel;
import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryProgressDto {
    private String categoryName;
    private ProgressLevel level;
    private int points;
    private List<ActivityEntryDto> activities;
}
