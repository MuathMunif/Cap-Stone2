package seu.capstone2.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seu.capstone2.Api.ApiExcpection;
import seu.capstone2.Model.Bid;
import seu.capstone2.Model.Company;
import seu.capstone2.Model.ProjectBid;
import seu.capstone2.Model.User;
import seu.capstone2.Repository.BidRepository;
import seu.capstone2.Repository.CompanyRepository;
import seu.capstone2.Repository.ProjectBidRepository;
import seu.capstone2.Repository.UserRepository;


import java.util.List;

@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;
    private final ProjectBidRepository projectBidRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final ContractEmailService contractEmailService;


    public List<Bid> getAllBids() {
        return bidRepository.findAll();
    }

    public void addBid(Bid bid) {
        ProjectBid projectBid = projectBidRepository.findProjectBidById(bid.getProjectBidId());
        Company company = companyRepository.findCompanyById(bid.getContractorCompanyId());
        User user = userRepository.findUserById(bid.getSubmittedByUserId());

        if (projectBid == null) {
            throw new ApiExcpection("Project bid not found");
        }
        if (company == null) {
            throw new ApiExcpection("Company not found");
        }
        if (user == null) {
            throw new ApiExcpection("User not found");
        }
        if (bidRepository.existsBidsByProjectBidIdAndSubmittedByUserId(bid.getProjectBidId(), bid.getSubmittedByUserId())) {
            throw new ApiExcpection("You already submitted a bid for this ProjectBid");
        }

        if (!"OPEN".equals(projectBid.getStatus())) {
            throw new ApiExcpection("ProjectBid is not OPEN");
        }
        if (projectBid.getDeadline().isBefore(java.time.LocalDate.now())) {
            throw new ApiExcpection("ProjectBid deadline has passed");
        }
        bid.setStatus("PENDING");
        bidRepository.save(bid);
    }

    public void updateBid(Integer id , Bid bid) {
        Bid oldBid = bidRepository.findBidById(id);
        if (oldBid == null) {
            throw new ApiExcpection("Bid not found");
        }
        if (!oldBid.getStatus().equals("PENDING")) {
            throw new ApiExcpection("You can't update a bid with status " + oldBid.getStatus());
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
            throw new ApiExcpection("Bid not found");
        }
        if (!"PENDING".equals(oldBid.getStatus())) {
            throw new ApiExcpection("Only bids with status 'PENDING' can be deleted");
        }
        bidRepository.delete(oldBid);
    }

    //Extra

    //find All bids by Contractor company Id
    public List<Bid> getBidsByContractorId(Integer ContractorId) {
        Company company = companyRepository.findCompanyById(ContractorId);
        if (company == null) {
            throw new ApiExcpection("Company not found");
        }
        List<Bid> bids = bidRepository.findBidByContractorCompanyId(ContractorId);
        if (bids.isEmpty()) {
            throw new ApiExcpection("No bids found");
        }
        return bids;
    }


    // find All bids by project Bid ID
    public List<Bid> getAllBidsByProjectBidId(Integer projectBidId) {
        ProjectBid projectBid = projectBidRepository.findProjectBidById(projectBidId);
        if (projectBid == null) {
            throw new ApiExcpection("Project bid not found");
        }
        List<Bid> bids = bidRepository.findBidByProjectBidId(projectBidId);
        if (bids.isEmpty()) {
            throw new ApiExcpection("No bids found");
        }
        return bids;
    }

    //find All bids by status
    public List<Bid> getAllBidsByStatus(String status) {
        if (!status.matches("PENDING|ACCEPTED|REJECTED")) {
            throw new ApiExcpection("Invalid status");
        }
        List<Bid> bids = bidRepository.findBidByStatusContains(status);
        if (bids.isEmpty()) {
            throw new ApiExcpection("No bids found for status: " + status);
        }
        return bids;
    }


    //accept one Bid and reject other bids
    public void acceptBid(Integer bidId , Integer userId) {
        Bid bid = bidRepository.findBidById(bidId);
        if(bid == null) {
            throw new ApiExcpection("Bid not found");
        }
        ProjectBid projectBid = projectBidRepository.findProjectBidById(bid.getProjectBidId());
        if (projectBid == null) {
            throw new ApiExcpection("Project bid not found");
        }
        if (!projectBid.getCreatedByUserId().equals(userId)) {
            throw new ApiExcpection("You are not authorized to accept this bid");
        }
        if (projectBid.getDeadline().isBefore(java.time.LocalDate.now())) {
            projectBid.setStatus("CLOSED"); // todo check
            throw new ApiExcpection("ProjectBid deadline has passed");
        }
        if (!"OPEN".equals(projectBid.getStatus())) {
            throw new ApiExcpection("ProjectBid is not OPEN");
        }
        if (!"PENDING".equals(bid.getStatus())) {
            throw new ApiExcpection("Bid is not PENDING");
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


    //reject bid
    public void rejectBid(Integer bidId , Integer userId) {
        Bid bid = bidRepository.findBidById(bidId);
        if(bid == null) {
            throw new ApiExcpection("Bid not found");
        }
        ProjectBid projectBid = projectBidRepository.findProjectBidById(bid.getProjectBidId());
        if (projectBid == null) {
            throw new ApiExcpection("Project bid not found");
        }
        if (!projectBid.getCreatedByUserId().equals(userId)) {
            throw new ApiExcpection("You are not authorized to reject this bid");
        }
        if (!"PENDING".equals(bid.getStatus())) {
            throw new ApiExcpection("ProjectBid is not PENDING");
        }
        bid.setStatus("REJECTED");
        bidRepository.save(bid);
    }


    public List<Bid> getBidsByProjectBidIdAndStatus(Integer projectBidId , String status) {
        List<Bid> bids = bidRepository.findBidByProjectBidIdAndStatusContains(projectBidId, status);
        if (bids.isEmpty()) {
            throw new ApiExcpection("No bids found");
        }
        return bids;
    }


    //Extra test accept and send email
    public void acceptBidAndEmail(Integer bidId, Integer actingUserId) {
        acceptBid(bidId, actingUserId);
        Bid bid = bidRepository.findBidById(bidId);
        if (bid == null) {
            throw new ApiExcpection("Bid not found");
        }
        Company contractor = companyRepository.findCompanyById(bid.getContractorCompanyId());
        if (contractor == null) {
            throw new ApiExcpection("Contractor company not found");
        }
        contractEmailService.emailContractPdf(bidId, contractor.getEmail());
    }
}
