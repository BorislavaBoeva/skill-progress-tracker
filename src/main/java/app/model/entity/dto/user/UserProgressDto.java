package app.model.entity.dto.user;

import app.model.entity.dto.category.CategoryProgressDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProgressDto {
    private UUID id;
    private List<CategoryProgressDto> categories;
}
