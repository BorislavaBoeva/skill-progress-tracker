package app.web.export;

import app.exception.ExportNotFoundException;
import app.model.dto.export.ExportResponseDto;
import app.model.dto.export.ExportStatus;
import app.model.dto.export.ExportUpdateRequestDto;
import app.model.dto.user.AuthenticationUserDetails;
import app.service.export.ExportFileService;
import app.service.export.ExportService;
import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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

    // 0) CREATE — triggered by "Export Your Data" button on home.html
    @GetMapping
    public ModelAndView createExport(@AuthenticationPrincipal AuthenticationUserDetails principal) {
        ExportResponseDto created = exportFileService.createExport(principal.getId());
        return new ModelAndView("redirect:/exportRecord/details/" + created.getId());
    }

    // 1) DETAILS PAGE
    @GetMapping("/details/{id}")
    public ModelAndView showDetails(@PathVariable UUID id,
                                    @AuthenticationPrincipal AuthenticationUserDetails principal) {
        ExportResponseDto record = fetchOrThrow(id, principal.getId());

        ModelAndView mav = new ModelAndView("exportRecord");
        mav.addObject("record", record);
        return mav;
    }

    // 2) UPDATE PAGE (GET)
    @GetMapping("/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable UUID id,
                                       @AuthenticationPrincipal AuthenticationUserDetails principal) {
        ExportResponseDto record = fetchOrThrow(id, principal.getId());

        ModelAndView mav = new ModelAndView("updateRecords");
        mav.addObject("record", record);
        return mav;
    }

    // 3) UPDATE (PUT)
    @PutMapping("/update/{id}")
    public ModelAndView updateRecord(@PathVariable UUID id,
                                     @Valid @ModelAttribute ExportUpdateRequestDto updateDto,
                                     BindingResult bindingResult,
                                     @AuthenticationPrincipal AuthenticationUserDetails principal) {
        if (bindingResult.hasErrors()) {
            ModelAndView mav = new ModelAndView("updateRecords");
            mav.addObject("record", fetchOrThrow(id, principal.getId()));
            return mav;
        }

        try {
            exportService.update(id, updateDto, principal.getId());
        } catch (FeignException.NotFound e) {
            return new ModelAndView("redirect:/exportRecord/update/" + id + "?error=Update failed");
        }

        return new ModelAndView("redirect:/exportRecord/update/" + id + "?updated=true");
    }

    // 4) RETRY
    @PutMapping("/retry/{id}")
    public ModelAndView retryRecord(@PathVariable UUID id,
                                    @AuthenticationPrincipal AuthenticationUserDetails principal) {
        ExportStatus newStatus;
        try {
            exportFileService.generateCsv(principal.getId());
            newStatus = ExportStatus.SUCCEEDED;
        } catch (Exception e) {
            newStatus = ExportStatus.FAILED;
        }

        exportService.retry(id, newStatus, principal.getId());
        return new ModelAndView("redirect:/exportRecord/details/" + id);
    }

    // 5) DELETE
    @PostMapping("/delete/{id}")
    public ModelAndView deleteRecord(@PathVariable UUID id,
                                     @AuthenticationPrincipal AuthenticationUserDetails principal) {
        exportService.delete(id, principal.getId());
        return new ModelAndView("redirect:/users/progress");
    }

    // 6) DOWNLOAD — regenerates on-demand
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> download(@PathVariable UUID id,
                                           @AuthenticationPrincipal AuthenticationUserDetails principal) {
        ExportResponseDto record = fetchOrThrow(id, principal.getId());
        byte[] content = exportFileService.generateCsv(principal.getId());

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + record.getFileName() + "\"")
                .header("Content-Type", "text/csv")
                .body(content);
    }

    private ExportResponseDto fetchOrThrow(UUID id, UUID userId) {
        try {
            return exportService.getById(id, userId);
        } catch (FeignException.NotFound e) {
            throw new ExportNotFoundException("Export record not found");
        }
    }
}