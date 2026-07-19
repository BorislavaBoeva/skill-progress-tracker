package app.service.export;

import app.model.dto.export.ExportCreateRequestDto;
import app.model.dto.export.ExportResponseDto;
import app.model.dto.export.ExportStatus;
import app.model.dto.export.ExportUpdateRequestDto;
import app.service.export.client.ExportClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExportService {
    private final ExportClient exportClient;

    @Value("${export-record.service.api-key}")
    private String validApiKey;

    public ExportResponseDto getById(UUID id, UUID userId) {
        return exportClient.getById(id, userId, validApiKey).getBody();
    }

    public List<ExportResponseDto> getAllByUser(UUID userId) {
        return exportClient.getAllByUser(userId, validApiKey).getBody();
    }
    public ExportResponseDto create(ExportCreateRequestDto createDto) {
        return exportClient.create(createDto, validApiKey).getBody();
    }

    public ExportResponseDto update(UUID id, ExportUpdateRequestDto updateDto, UUID userId) {
        return exportClient.update(id, updateDto, userId, validApiKey).getBody();
    }

    public ExportResponseDto retry(UUID id, ExportStatus status, UUID userId) {
        return exportClient.retry(id, status, userId, validApiKey).getBody();
    }
       public void delete(UUID id, UUID userId) {
        exportClient.delete(id, userId, validApiKey);
    }
}
