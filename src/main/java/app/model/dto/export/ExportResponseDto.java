package app.model.dto.export;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportResponseDto {
    private UUID id;
    private ExportType exportType;
    private String fileName;
    private String description;
    private LocalDateTime exportDate;
    private LocalDateTime updatedOn;
    private ExportStatus exportStatus;
    private UUID userId;
    private boolean deleted;
}
