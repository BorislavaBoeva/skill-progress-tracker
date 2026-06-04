package app.model.entity.dto.activity;

import app.model.entity.category.Category;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;
@Data
@Builder
public class ActivityDto {
    private UUID id;
    private String name;
    private UUID categoryId;
 }
