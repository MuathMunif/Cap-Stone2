package seu.capstone2.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seu.capstone2.Api.ApiException;
import seu.capstone2.Model.*;
import seu.capstone2.Repository.*;


import java.util.List;

@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;
    private final ProjectBidRepository projectBidRepository;
    private final CompanyRepository companyRepository;
    private final ContractorRepository contractorRepository;
    private final ContractEmailService contractEmailService;

    public List<Bid> getAllBids() {
        return bidRepository.findAll();
    }

    public void addBid(Bid bid) {
        ProjectBid projectBid = projectBidRepository.findProjectBidById(bid.getProjectBidId());
        Contractor contractor = contractorRepository.findContractorById(bid.getContractorId());

        if (projectBid == null) {
            throw new ApiException("Project bid not found");
        }
        if (contractor == null) {
            throw new ApiException("Contractor not found");
        }
        if (bidRepository.existsByProjectBidIdAndContractorId(bid.getProjectBidId(), bid.getContractorId())) {
            throw new ApiException("You already submitted a bid for this ProjectBid");
        }

        if (!"OPEN".equals(projectBid.getStatus())) {
            throw new ApiException("ProjectBid is not OPEN");
        }
        if (projectBid.getDeadline().isBefore(java.time.LocalDate.now())) {
            throw new ApiException("ProjectBid deadline has passed");
        }
        bid.setStatus("PENDING");
        bidRepository.save(bid);
    }

    public void updateBid(Integer id , Bid bid) {
        Bid oldBid = bidRepository.findBidById(id);
        if (oldBid == null) {
            throw new ApiException("Bid not found");
        }
        if (!oldBid.getStatus().equals("PENDING")) {
            throw new ApiException("You can't update a bid with status " + oldBid.getStatus());
        }
        oldBid.setAmount(bid.getAmount());
        oldBid.setDescription(bid.getDescription());
        oldBid.setDurationInDays(bid.getDurationInDays());
        oldBid.setSubmissionDate(bid.getSubmissionDate());
        bidRepository.save(oldBid);
    }

    public void deleteBid(Integer id) {
        Bid oldBid = bidRepository.findBidById(id);
        if (oldBid == null) {
            throw new ApiException("Bid not found");
        }
        if (!"PENDING".equals(oldBid.getStatus())) {
            throw new ApiException("Only bids with status 'PENDING' can be deleted");
        }
        bidRepository.delete(oldBid);
    }

    // find All bids by contractorId
    public List<Bid> getBidsByContractorId(Integer contractorId) {
        Contractor contractor = contractorRepository.findContractorById(contractorId);
        if (contractor == null) {
            throw new ApiException("Contractor not found");
        }
        List<Bid> bids = bidRepository.findBidByContractorId(contractorId);
        if (bids.isEmpty()) {
            throw new ApiException("No bids found");
        }
        return bids;
    }

    // find All bids by project Bid ID
    public List<Bid> getAllBidsByProjectBidId(Integer projectBidId) {
        ProjectBid projectBid = projectBidRepository.findProjectBidById(projectBidId);
        if (projectBid == null) {
            throw new ApiException("Project bid not found");
        }
        List<Bid> bids = bidRepository.findBidByProjectBidId(projectBidId);
        if (bids.isEmpty()) {
            throw new ApiException("No bids found");
        }
        return bids;
    }

    // find All bids by status
    public List<Bid> getAllBidsByStatus(String status) {
        if (!status.matches("PENDING|ACCEPTED|REJECTED")) {
            throw new ApiException("Invalid status");
        }
        List<Bid> bids = bidRepository.findBidByStatusContains(status);
        if (bids.isEmpty()) {
            throw new ApiException("No bids found for status: " + status);
        }
        return bids;
    }

    // accept one Bid and reject other bids
    public void acceptBid(Integer bidId , Integer actingCompanyId) {
        Bid bid = bidRepository.findBidById(bidId);
        if(bid == null) {
            throw new ApiException("Bid not found");
        }
        ProjectBid projectBid = projectBidRepository.findProjectBidById(bid.getProjectBidId());
        if (projectBid == null) {
            throw new ApiException("Project bid not found");
        }
        if (!projectBid.getCompanyId().equals(actingCompanyId)) {
            throw new ApiException("You are not authorized to accept this bid");
        }
        if (projectBid.getDeadline().isBefore(java.time.LocalDate.now())) {
            projectBid.setStatus("CLOSED"); // todo check
            throw new ApiException("ProjectBid deadline has passed");
        }
        if (!"OPEN".equals(projectBid.getStatus())) {
            throw new ApiException("ProjectBid is not OPEN");
        }
        if (!"PENDING".equals(bid.getStatus())) {
            throw new ApiException("Bid is not PENDING");
        }
        bid.setStatus("ACCEPTED");
        bidRepository.save(bid);

        for (Bid other : bidRepository.findBidByProjectBidId(bid.getProjectBidId())) {
            if (!other.getId().equals(bidId) && other.getStatus().equals("PENDING")) {
                other.setStatus("REJECTED");
                bidRepository.save(other);
            }
        }

        projectBid.setStatus("AWARDED");
        projectBidRepository.save(projectBid);
    }

    // reject bid
    public void rejectBid(Integer bidId , Integer actingCompanyId) {
        Bid bid = bidRepository.findBidById(bidId);
        if(bid == null) {
            throw new ApiException("Bid not found");
        }
        ProjectBid projectBid = projectBidRepository.findProjectBidById(bid.getProjectBidId());
        if (projectBid == null) {
            throw new ApiException("Project bid not found");
        }
        if (!projectBid.getCompanyId().equals(actingCompanyId)) {
            throw new ApiException("You are not authorized to reject this bid");
        }
        if (!"PENDING".equals(bid.getStatus())) {
            throw new ApiException("ProjectBid is not PENDING");
        }
        bid.setStatus("REJECTED");
        bidRepository.save(bid);
    }

    public List<Bid> getBidsByProjectBidIdAndStatus(Integer projectBidId , String status) {
        List<Bid> bids = bidRepository.findBidByProjectBidIdAndStatusContains(projectBidId, status);
        if (bids.isEmpty()) {
            throw new ApiException("No bids found");
        }
        return bids;
    }

    // Extra test accept and send email
    public void acceptBidAndEmail(Integer bidId, Integer actingCompanyId) {
        acceptBid(bidId, actingCompanyId);
        Bid bid = bidRepository.findBidById(bidId);
        if (bid == null) {
            throw new ApiException("Bid not found");
        }
        Contractor contractor = contractorRepository.findContractorById(bid.getContractorId());
        if (contractor == null) {
            throw new ApiException("Contractor not found");
        }
        contractEmailService.emailContractPdf(bidId, contractor.getEmail());
    }
}
