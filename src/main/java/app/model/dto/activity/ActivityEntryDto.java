package app.model.dto.activity;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityEntryDto {
    private String activityName;
    private int hours;
}