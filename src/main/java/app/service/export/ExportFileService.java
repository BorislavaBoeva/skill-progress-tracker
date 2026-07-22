package app.service.export;

import app.exception.ApplicationException;
import app.exception.ExportGenerationException;
import app.model.dto.export.ExportCreateRequestDto;
import app.model.dto.export.ExportResponseDto;
import app.model.dto.export.ExportStatus;
import app.model.dto.export.ExportType;
import app.model.dto.skill.SkillProgressLogDto;
import app.service.skill.SkillProgressService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    public ExportResponseDto createExportRecord(UUID userId, ExportType type) {
        validateUserId(userId);
        validateType(type);
        ExportStatus status;
        try {
            generateFile(userId, type);
            status = ExportStatus.SUCCEEDED;
        } catch (Exception e) {
            status = ExportStatus.FAILED;
        }

        ExportCreateRequestDto createDto = buildCreateRequest(userId, type, status);
        return exportService.createRecord(createDto);
    }

    public byte[] generateFile(UUID userId, ExportType type) {
        validateUserId(userId);
        validateType(type);
        return switch (type) {
            case CSV -> generateCsv(userId);
            case PDF -> generatePdf(userId);
        };
    }

    public byte[] generateCsv(UUID userId) {
        validateUserId(userId);
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

    public byte[] generatePdf(UUID userId) {
        validateUserId(userId);
        List<SkillProgressLogDto> logs = skillProgressService.getLogsByUser(userId);

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            PDType1Font boldFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

            try (PDPageContentStream stream = new PDPageContentStream(document, page)) {
                float margin = 50;
                float y = page.getMediaBox().getHeight() - margin;
                float leading = 18;

                stream.beginText();
                stream.setFont(boldFont, 12);
                stream.newLineAtOffset(margin, y);
                stream.showText("Activity");
                stream.newLineAtOffset(200, 0);
                stream.showText("Description");
                stream.endText();
                y -= leading;

                stream.setFont(font, 11);
                for (SkillProgressLogDto log : logs) {
                    stream.beginText();
                    stream.newLineAtOffset(margin, y);
                    stream.showText(safe(log.getActivityName()));
                    stream.newLineAtOffset(200, 0);
                    stream.showText(safe(log.getDescription()));
                    stream.endText();
                    y -= leading;
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new ExportGenerationException("Failed to generate PDF export", e);
        }
    }

    private ExportCreateRequestDto buildCreateRequest(UUID userId, ExportType type, ExportStatus status) {
        String extension = type == ExportType.PDF ? ".pdf" : ".csv";
        return ExportCreateRequestDto.builder()
                .userId(userId)
                .fileName("progress_export_" + LocalDate.now() + extension)
                .description("Activity log export")
                .exportType(type)
                .exportStatus(status)
                .build();
    }
    private void validateUserId(UUID userId) {
        if (userId == null) {
            throw new ApplicationException("User ID cannot be null");
        }
    }

    private void validateType(ExportType type) {
        if (type == null) {
            throw new ApplicationException("Export type cannot be null");
        }
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

    private String safe(String value) {
        return value == null ? "" : value;
    }
}
