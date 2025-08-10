package seu.capstone2.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seu.capstone2.Api.ApiExcpection;
import seu.capstone2.Model.Bid;
import seu.capstone2.Model.Company;
import seu.capstone2.Model.ProjectBid;
import seu.capstone2.Model.Contractor;
import seu.capstone2.Repository.BidRepository;
import seu.capstone2.Repository.CompanyRepository;
import seu.capstone2.Repository.ProjectBidRepository;
import seu.capstone2.Repository.ContractorRepository;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final BidRepository bidRepository;
    private final ProjectBidRepository projectBidRepository;
    private final CompanyRepository companyRepository;
    private final ContractorRepository contractorRepository;

    public byte[] generateContractPdf(Integer acceptedBidId) {
        Bid bid = bidRepository.findBidById(acceptedBidId);
        if (bid == null) throw new ApiExcpection("Bid not found");
        if (!"ACCEPTED".equals(bid.getStatus())) throw new ApiExcpection("Bid is not ACCEPTED");

        ProjectBid pb = projectBidRepository.findProjectBidById(bid.getProjectBidId());
        if (pb == null) throw new ApiExcpection("ProjectBid not found");

        Company owner = companyRepository.findCompanyById(pb.getCompanyId());
        Contractor contractor = contractorRepository.findContractorById(bid.getContractorId());
        if (owner == null || contractor == null) throw new ApiExcpection("Company/Contractor data missing");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
        try {
            PdfWriter.getInstance(doc, baos);
            doc.open();

            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 11);

            // Title
            doc.add(new Paragraph("Project Execution Contract", titleFont));
            doc.add(new Paragraph(" "));

            // Parties
            doc.add(new Paragraph("Parties Information", headerFont));
            doc.add(new Paragraph("Owner: " +
                    (owner.getName() == null ? "-" : owner.getName()) +
                    " - CR: " + (owner.getCrNumber() == null ? "-" : owner.getCrNumber()) +
                    " - " + (owner.getCity() == null ? "-" : owner.getCity()), normalFont));
            doc.add(new Paragraph("Contractor: " +
                    (contractor.getName() == null ? "-" : contractor.getName()) +
                    " - CR: " + (contractor.getCrNumber() == null ? "-" : contractor.getCrNumber()) +
                    " - " + (contractor.getCity() == null ? "-" : contractor.getCity()), normalFont));
            doc.add(new Paragraph(" "));

            // Project Info
            doc.add(new Paragraph("Project Information", headerFont));
            doc.add(new Paragraph("Title: " + (pb.getTitle() == null ? "-" : pb.getTitle()), normalFont));
            doc.add(new Paragraph("Location: " + (pb.getLocation() == null ? "-" : pb.getLocation()), normalFont));
            doc.add(new Paragraph("Description: " + (pb.getDescription() == null ? "-" : pb.getDescription()), normalFont));
            doc.add(new Paragraph(" "));

            // Contract Value and Duration
            doc.add(new Paragraph("Value and Duration", headerFont));
            doc.add(new Paragraph("Contract Value: " + (bid.getAmount() == null ? "-" : bid.getAmount().toString()) + " SAR", normalFont));
            doc.add(new Paragraph("Execution Duration: " + (bid.getDurationInDays() == null ? "-" : bid.getDurationInDays() + " days"), normalFont));
            doc.add(new Paragraph(" "));

            // General Terms
            doc.add(new Paragraph("General Terms", headerFont));
            doc.add(new Paragraph(
                    "1) The contractor shall comply with specifications and schedules.\n" +
                            "2) Penalty for delay: 0.1% of contract value per day, up to a maximum of 10%.\n" +
                            "3) Both parties shall attempt to resolve disputes amicably first.\n" +
                            "4) The contract may not be transferred without the owner's consent.",
                    normalFont));
            doc.add(new Paragraph(" "));

            // Date & Signatures
            doc.add(new Paragraph("Issued on: " + LocalDate.now(), normalFont));
            doc.add(new Paragraph("Owner Signature: ____________     Contractor Signature: ____________", normalFont));

            doc.close();
        } catch (Exception e) {
            throw new ApiExcpection("Error generating contract PDF: " + e.getMessage());
        }

        return baos.toByteArray();
    }
}
