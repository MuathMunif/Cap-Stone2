package seu.capstone2.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import seu.capstone2.Api.ApiResponse;
import seu.capstone2.Model.Contractor;
import seu.capstone2.Service.ContractorService;


import java.util.Objects;

@RestController
@RequestMapping("/api/v1/contractor")
@RequiredArgsConstructor
public class ContractorController {
    private final ContractorService contractorService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllContractors() {
        return ResponseEntity.status(200).body(contractorService.findAllContractors());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addContractor(@Valid @RequestBody Contractor contractor , Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(Objects.requireNonNull(errors.getFieldError()).getDefaultMessage()));
        }
        contractorService.addContractor(contractor);
        return ResponseEntity.status(200).body(new ApiResponse("Contractor added successfully"));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateContractor(@PathVariable Integer id, @Valid@RequestBody Contractor contractor , Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(Objects.requireNonNull(errors.getFieldError()).getDefaultMessage()));
        }
        contractorService.updateContractor(id, contractor);
        return ResponseEntity.status(200).body(new ApiResponse("Contractor updated successfully"));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteContractor(@PathVariable Integer id) {
        contractorService.deleteContractor(id);
        return ResponseEntity.status(200).body(new ApiResponse("Contractor deleted successfully"));
    }

}
