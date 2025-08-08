package seu.capstone2.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import seu.capstone2.Api.ApiResponse;
import seu.capstone2.Model.ProjectBid;
import seu.capstone2.Service.ProjectBidService;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/projectBid")
@RequiredArgsConstructor
public class ProjectBidController {
    private final ProjectBidService projectBidService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllProjectBids() {
        return ResponseEntity.status(200).body(projectBidService.getAllProjectBids());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProjectBid(@Valid @RequestBody ProjectBid projectBid , Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(Objects.requireNonNull(errors.getFieldError()).getDefaultMessage()));
        }
        projectBidService.addProjectBid(projectBid);
        return ResponseEntity.status(200).body(new ApiResponse("Project bid added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProjectBid(@PathVariable Integer id ,@Valid @RequestBody ProjectBid projectBid , Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(Objects.requireNonNull(errors.getFieldError()).getDefaultMessage()));
        }
        projectBidService.updateProjectBid(id, projectBid);
        return ResponseEntity.status(200).body(new ApiResponse("Project bid updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProjectBid(@PathVariable Integer id) {
        projectBidService.deleteProjectBid(id);
        return ResponseEntity.status(200).body(new ApiResponse("Project bid deleted successfully"));
    }

}
