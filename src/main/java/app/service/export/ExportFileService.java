package app.service.export;

import app.model.dto.export.ExportCreateRequestDto;
import app.model.dto.export.ExportResponseDto;
import app.model.dto.export.ExportStatus;
import app.model.dto.export.ExportType;
import app.model.dto.skill.SkillProgressLogDto;
import app.service.skill.SkillProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ExportFileService {
    private final SkillProgressService skillProgressService;
    private final ExportService exportService;

    @Autowired
    public ExportFileService(SkillProgressService skillProgressService, ExportService exportService) {
        this.skillProgressService = skillProgressService;
        this.exportService = exportService;
    }

    public ExportResponseDto createExport(UUID userId) {
        ExportStatus status;
        try {
            generateCsv(userId);
            status = ExportStatus.SUCCEEDED;
        } catch (Exception e) {
            status = ExportStatus.FAILED;
        }

        ExportCreateRequestDto createDto = buildCreateRequest(userId, status);
        return exportService.create(createDto);
    }

    public byte[] generateCsv(UUID userId) {
        List<SkillProgressLogDto> logs = skillProgressService.getLogsByUser(userId);
        StringBuilder csv = new StringBuilder();
        csv.append("Activity,Description\n");

        for (SkillProgressLogDto log : logs) {
            csv.append(escape(log.getActivityName()))
                    .append(",")
                    .append(escape(log.getDescription()))
                    .append("\n");
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private ExportCreateRequestDto buildCreateRequest(UUID userId, ExportStatus status) {
        return ExportCreateRequestDto.builder()
                .userId(userId)
                .fileName("progress_export_" + LocalDate.now() + ".csv")
                .description("Activity log export")
                .exportType(ExportType.CSV)
                .exportStatus(status)
                .build();
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        // CSV escaping: quote fields containing comma, quote, or newline
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
