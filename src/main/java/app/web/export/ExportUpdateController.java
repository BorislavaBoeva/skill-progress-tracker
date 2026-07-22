package app.web.export;

import app.model.dto.export.ExportResponseDto;
import app.model.dto.export.ExportUpdateRequestDto;
import app.model.dto.user.AuthenticationUserDetails;
import app.service.export.ExportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/exportRecord/update/{id}")
public class ExportUpdateController {
    private final ExportService exportService;

    @Autowired
    public ExportUpdateController(ExportService exportService) {
        this.exportService = exportService;
    }

    @ModelAttribute("record")
    public ExportResponseDto loadRecord(@PathVariable UUID id,
                                        @AuthenticationPrincipal AuthenticationUserDetails principal) {
        return exportService.getRecordByIdOrThrow(id, principal.getId());
    }

    @GetMapping
    public ModelAndView showUpdateForm(@ModelAttribute("record") ExportResponseDto record) {
        return new ModelAndView("updateRecords");
    }

    // Save button
    @PutMapping
    public ModelAndView updateRecord(@PathVariable UUID id,
                                     @Valid @ModelAttribute("updateDto") ExportUpdateRequestDto updateDto,
                                     BindingResult bindingResult,
                                     @ModelAttribute("record") ExportResponseDto record,
                                     @AuthenticationPrincipal AuthenticationUserDetails principal) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("updateRecords");
        }

        exportService.updateRecord(id, updateDto, principal.getId());
        return new ModelAndView("redirect:/exportRecord/details/" + id);
    }
}