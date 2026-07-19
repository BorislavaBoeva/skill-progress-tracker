package app.service.export.client;

import app.model.dto.export.ExportCreateRequestDto;
import app.model.dto.export.ExportResponseDto;
import app.model.dto.export.ExportStatus;
import app.model.dto.export.ExportUpdateRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "export-record",url = "${export-record.service.url}")
public interface ExportClient {

    String X_API_KEY = "X-API-Key";
    @GetMapping("/{id}")
    ResponseEntity<ExportResponseDto> getById(@PathVariable UUID id,
                                              @RequestParam UUID userId,
                                              @RequestHeader(X_API_KEY) String xApiKey);

    @GetMapping
    ResponseEntity<List<ExportResponseDto>> getAllByUser(@RequestParam UUID userId,
                                                         @RequestHeader(X_API_KEY) String xApiKey);

    @PostMapping
    ResponseEntity<ExportResponseDto> create(@RequestBody ExportCreateRequestDto createDto,
                                             @RequestHeader(X_API_KEY) String xApiKey);

    @PutMapping("/{id}")
    ResponseEntity<ExportResponseDto> update(@PathVariable UUID id,
                                             @RequestBody ExportUpdateRequestDto updateDto,
                                             @RequestParam UUID userId,
                                             @RequestHeader(X_API_KEY) String xApiKey);

    @PutMapping("/{id}/retry")
    ResponseEntity<ExportResponseDto> retry(@PathVariable UUID id,
                                            @RequestParam ExportStatus status,
                                            @RequestParam UUID userId,
                                            @RequestHeader(X_API_KEY) String xApiKey);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id,
                                @RequestParam UUID userId,
                                @RequestHeader(X_API_KEY) String xApiKey);
  }
