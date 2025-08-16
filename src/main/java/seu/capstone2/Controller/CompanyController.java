package seu.capstone2.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import seu.capstone2.Api.ApiResponse;
import seu.capstone2.Model.Company;
import seu.capstone2.Service.CompanyService;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/company")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllCompany(){
        return ResponseEntity.status(200).body(companyService.findAllCompanies());
    }


    @PostMapping("/add")
    public ResponseEntity<?> addCompany(@Valid @RequestBody Company company){
        companyService.addCompany(company);
        return ResponseEntity.status(200).body(new ApiResponse("Company added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCompany(@PathVariable int id ,@Valid @RequestBody Company company){
        companyService.updateCompany(id, company);
        return ResponseEntity.status(200).body(new ApiResponse("Company updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable int id){
        companyService.deleteCompany(id);
        return ResponseEntity.status(200).body(new ApiResponse("Company deleted successfully"));
    }


}
