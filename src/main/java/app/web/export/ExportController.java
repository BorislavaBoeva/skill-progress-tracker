package app.web.export;

import app.model.dto.export.ExportResponseDto;
import app.model.dto.export.ExportStatus;
import app.model.dto.export.ExportType;
import app.model.dto.user.AuthenticationUserDetails;
import app.service.export.ExportFileService;
import app.service.export.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/exportRecord")
public class ExportController {
    private final ExportService exportService;
    private final ExportFileService exportFileService;

    @Autowired
    public ExportController(ExportService exportService, ExportFileService exportFileService) {
        this.exportService = exportService;
        this.exportFileService = exportFileService;
    }

    //from "Export Your Data" page
    @GetMapping
    public ModelAndView createExport(@RequestParam(defaultValue = "CSV") ExportType type,
                                     @AuthenticationPrincipal AuthenticationUserDetails principal) {
        ExportResponseDto created = exportFileService.createExportRecord(principal.getId(), type);
        return new ModelAndView("redirect:/exportRecord/details/" + created.getId());
    }

    // Details view
    @GetMapping("/details/{id}")
    public ModelAndView showDetails(@PathVariable UUID id,
                                    @AuthenticationPrincipal AuthenticationUserDetails principal) {
        ExportResponseDto record = exportService.getRecordByIdOrThrow(id, principal.getId());
        ModelAndView modelAndView = new ModelAndView("exportRecord");
        modelAndView.addObject("record", record);
        return modelAndView;
    }

    @GetMapping("/history")
    public ModelAndView showHistory(@AuthenticationPrincipal AuthenticationUserDetails principal) {
        List<ExportResponseDto> recordsHistory = exportService.getHistory(principal.getId());
        ModelAndView modelAndView = new ModelAndView("exportHistory");
        modelAndView.addObject("recordsHistory", recordsHistory);
        return modelAndView;
    }

    // Try Again button
    @PutMapping("/retry/{id}")
    public ModelAndView retryRecord(@PathVariable UUID id,
                                    @AuthenticationPrincipal AuthenticationUserDetails principal) {
        ExportResponseDto record = exportService.getRecordByIdOrThrow(id, principal.getId());
        ExportStatus newStatus;
        try {
            exportFileService.generateFile(principal.getId(), record.getExportType());
            newStatus = ExportStatus.SUCCEEDED;
        } catch (Exception e) {
            newStatus = ExportStatus.FAILED;
        }
        exportService.retryRecord(id, newStatus, principal.getId());
        return new ModelAndView("redirect:/exportRecord/details/" + id);
    }

    // Export File Download button
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> download(@PathVariable UUID id,
                                           @AuthenticationPrincipal AuthenticationUserDetails principal) {
        ExportResponseDto record = exportService.getRecordByIdOrThrow(id, principal.getId());
        byte[] content = exportFileService.generateFile(principal.getId(), record.getExportType());
        String contentType = record.getExportType() == ExportType.PDF ? "application/pdf" : "text/csv";
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + record.getFileName() + "\"")
                .header("Content-Type", contentType)
                .body(content);
    }

    // Delete Record button
    @PostMapping("/delete/{id}")
    public ModelAndView deleteRecord(@PathVariable UUID id,
                                     @AuthenticationPrincipal AuthenticationUserDetails principal) {
        exportService.deleteRecord(id, principal.getId());
        return new ModelAndView("redirect:/users/progress");
    }
}