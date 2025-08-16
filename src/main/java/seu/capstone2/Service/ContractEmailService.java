package seu.capstone2.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import seu.capstone2.Api.ApiException;
import seu.capstone2.Model.Bid;
import seu.capstone2.Model.Company;
import seu.capstone2.Model.ProjectBid;
import seu.capstone2.Repository.BidRepository;
import seu.capstone2.Repository.CompanyRepository;
import seu.capstone2.Repository.ProjectBidRepository;

@Service
@RequiredArgsConstructor
public class ContractEmailService {

    private final BidRepository bidRepository;
    private final ProjectBidRepository projectBidRepository;
    private final CompanyRepository companyRepository;
    private final JavaMailSender mailSender;
    private final ContractService contractService;

    public void emailContractPdf(Integer bidId, String toEmail) {
        Bid bid = bidRepository.findBidById(bidId);
        if (bid == null) {
            throw new ApiException("Bid not found");
        }
        ProjectBid pb = projectBidRepository.findProjectBidById(bid.getProjectBidId());
        if (pb == null) throw new ApiException("ProjectBid not found");

        Company owner = companyRepository.findCompanyById(pb.getCompanyId());
        if (owner == null) throw new ApiException("Owner company not found");
        if (owner.getEmail() == null || owner.getEmail().isBlank()) {
            throw new ApiException("Owner company email not found");
        }


        byte[] pdfBytes = contractService.generateContractPdf(bidId);


        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(owner.getEmail());
            helper.setTo(toEmail);
            helper.setSubject("Construction Project Contract");
            helper.setText("Please find attached the contract for the awarded project.", false);
            helper.addAttachment("Contract.pdf", new ByteArrayResource(pdfBytes));

            mailSender.send(message);
        } catch (Exception e) {
            throw new ApiException("Failed to send email: " + e.getMessage());
        }
    }
}


