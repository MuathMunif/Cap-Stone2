package seu.capstone2.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seu.capstone2.Api.ApiExcpection;
import seu.capstone2.Model.Company;
import seu.capstone2.Model.ProjectBid;
import seu.capstone2.Model.User;
import seu.capstone2.Repository.CompanyRepository;
import seu.capstone2.Repository.ProjectBidRepository;
import seu.capstone2.Repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectBidService {
    private final ProjectBidRepository projectBidRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public List<ProjectBid> getAllProjectBids() {
        return projectBidRepository.findAll();
    }

    public void addProjectBid(ProjectBid projectBid) {
        Company company = companyRepository.findCompanyById(projectBid.getCompanyId());
        User user = userRepository.findUserById(projectBid.getCreatedByUserId());
        if (company == null) {
            throw new ApiExcpection("Company not found");
        }
        if (user == null) {
            throw new ApiExcpection("User not found");
        }

        projectBidRepository.save(projectBid);
    }


    public void updateProjectBid(Integer id ,ProjectBid projectBid) {
        ProjectBid projectBidToUpdate = projectBidRepository.findProjectBidById(id);
        if (projectBidToUpdate == null) {
            throw new ApiExcpection("Project bid not found");
        }
        projectBidToUpdate.setBudget(projectBid.getBudget());
        projectBidToUpdate.setDeadline(projectBid.getDeadline());
        projectBidToUpdate.setDescription(projectBid.getDescription());
        projectBidToUpdate.setTitle(projectBid.getTitle());
        projectBidToUpdate.setLocation(projectBid.getLocation());
        projectBidToUpdate.setStatus(projectBid.getStatus());
        projectBidRepository.save(projectBidToUpdate);
    }

    public void deleteProjectBid(Integer id) {
        ProjectBid projectBidToDelete = projectBidRepository.findProjectBidById(id);
        if (projectBidToDelete == null) {
            throw new ApiExcpection("Project bid not found");
        }
        projectBidRepository.delete(projectBidToDelete);
    }
}
