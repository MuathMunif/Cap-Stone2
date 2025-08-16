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
    public ResponseEntity<?> addBid(@Valid @RequestBody Bid bid) {
        bidService.addBid(bid);
        return ResponseEntity.status(201).body(new ApiResponse("bid added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBid(@PathVariable Integer id ,@Valid @RequestBody Bid bid) {
        bidService.updateBid(id,bid);
        return ResponseEntity.status(201).body(new ApiResponse("bid updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBid(@PathVariable Integer id) {
        bidService.deleteBid(id);
        return ResponseEntity.status(200).body(new ApiResponse("bid deleted successfully"));
    }



    @GetMapping("/get-bids-by-contractorId/{contractorId}")
    public ResponseEntity<?> getBidsByContractorId(@PathVariable Integer contractorId) {
        return ResponseEntity.status(200).body(bidService.getBidsByContractorId(contractorId));
    }

    @GetMapping("/get-bids-by-project-bidId/{projectBidId}")
    public ResponseEntity<?> getBidsByProjectBidId(@PathVariable Integer projectBidId) {
        return ResponseEntity.status(200).body(bidService.getAllBidsByProjectBidId(projectBidId));
    }


    @PutMapping("/accept-bid/{bidId}/{actingCompanyId}")
    public ResponseEntity<?> acceptBid(@PathVariable Integer bidId, @PathVariable Integer actingCompanyId) {
        bidService.acceptBid(bidId,actingCompanyId);
        return ResponseEntity.status(200).body(new ApiResponse("bid accepted"));
    }

    @PutMapping("/reject-bid/{bidId}/{actingCompanyId}")
    public ResponseEntity<?> rejectBid(@PathVariable Integer bidId, @PathVariable Integer actingCompanyId) {
        bidService.rejectBid(bidId,actingCompanyId);
        return ResponseEntity.status(200).body(new ApiResponse("bid rejected"));
    }


    @GetMapping("/get-bid-by-status/{status}")
    public ResponseEntity<?> getBidByStatus(@PathVariable String status) {
        return ResponseEntity.status(200).body(bidService.getAllBidsByStatus(status));
    }

    @GetMapping("/get-bids-by-projectId-status/{projectId}/{status}")
    public ResponseEntity<?> getBidsByProjectIdAndStatus(@PathVariable Integer projectId, @PathVariable String status) {
        return ResponseEntity.status(200).body(bidService.getBidsByProjectBidIdAndStatus(projectId , status));
    }



    @PutMapping("/accept-email/{bidId}/{actingCompanyId}")
    public ResponseEntity<?> acceptAndEmailed(@PathVariable Integer bidId,@PathVariable Integer actingCompanyId) {
        bidService.acceptBidAndEmail(bidId, actingCompanyId);
        return ResponseEntity.status(200).body(new ApiResponse("bid accepted and emailed"));
    }
}
