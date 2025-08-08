package seu.capstone2.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import seu.capstone2.Api.ApiResponse;
import seu.capstone2.Model.Bid;
import seu.capstone2.Service.BidService;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/bid")
@RequiredArgsConstructor
public class BidController {
    private final BidService bidService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllBids() {
        return ResponseEntity.status(200).body(bidService.getAllBids());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addBid(@Valid @RequestBody Bid bid , Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(Objects.requireNonNull(errors.getFieldError()).getDefaultMessage()));
        }
        bidService.addBid(bid);
        return ResponseEntity.status(201).body(new ApiResponse("bid added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBid(@PathVariable Integer id ,@Valid @RequestBody Bid bid , Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(new ApiResponse(Objects.requireNonNull(errors.getFieldError()).getDefaultMessage()));
        }
        bidService.updateBid(id,bid);
        return ResponseEntity.status(201).body(new ApiResponse("bid updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBid(@PathVariable Integer id) {
        bidService.deleteBid(id);
        return ResponseEntity.status(200).body(new ApiResponse("bid deleted successfully"));
    }

}
