package app.service.export;

import app.exception.ApplicationException;
import app.exception.ExportInProgressException;
import app.exception.ExportNotFoundException;
import app.model.dto.export.ExportCreateRequestDto;
import app.model.dto.export.ExportResponseDto;
import app.model.dto.export.ExportStatus;
import app.model.dto.export.ExportUpdateRequestDto;
import app.service.export.client.ExportClient;
import feign.FeignException;
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

    public ExportResponseDto getRecordById(UUID id, UUID userId) {
        return exportClient.getById(id, userId, validApiKey).getBody();
    }

    public ExportResponseDto createRecord(ExportCreateRequestDto createDto) {
        try {
            return exportClient.create(createDto, validApiKey).getBody();
        } catch (FeignException.Conflict e) {
            throw new ExportInProgressException("An export was already requested moments ago");
        }
    }

    public List<ExportResponseDto> getHistory(UUID userId) {
       List<ExportResponseDto> allRecordsByUser = exportClient.getHistory(userId, validApiKey).getBody();
       if (allRecordsByUser == null) {
           throw new ApplicationException("Failed to retrieve export history");
       }
        return allRecordsByUser;
    }

    public ExportResponseDto updateRecord(UUID id, ExportUpdateRequestDto updateDto, UUID userId) {
        return exportClient.update(id, updateDto, userId, validApiKey).getBody();
    }

    public void retryRecord(UUID id, ExportStatus status, UUID userId) {
        exportClient.retry(id, status, userId, validApiKey);
    }

    public void deleteRecord(UUID id, UUID userId) {
        exportClient.delete(id, userId, validApiKey);
    }

    public ExportResponseDto getRecordByIdOrThrow(UUID id, UUID userId) {
        try {
            return getRecordById(id, userId);
        } catch (FeignException.NotFound e) {
            throw new ExportNotFoundException("Export record not found");
        }
    }
}
