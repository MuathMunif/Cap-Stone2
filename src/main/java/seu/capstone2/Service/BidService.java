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
        bidRepository.delete(oldBid);
    }
}
