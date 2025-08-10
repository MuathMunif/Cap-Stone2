package seu.capstone2.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seu.capstone2.Api.ApiExcpection;
import seu.capstone2.Model.Company;
import seu.capstone2.Model.ProjectBid;
import seu.capstone2.Repository.CompanyRepository;
import seu.capstone2.Repository.ProjectBidRepository;


import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectBidService {

    private final ProjectBidRepository projectBidRepository;
    private final CompanyRepository companyRepository;

    public List<ProjectBid> getAllProjectBids() {
        return projectBidRepository.findAll();
    }


    public void addProjectBid(ProjectBid projectBid) {
        Company company = companyRepository.findCompanyById(projectBid.getCompanyId());
        if (company == null) {
            throw new ApiExcpection("Company not found");
        }
        projectBidRepository.save(projectBid);
    }

    public void updateProjectBid(Integer id, ProjectBid projectBid) {
        ProjectBid projectBidToUpdate = projectBidRepository.findProjectBidById(id);
        if (projectBidToUpdate == null) {
            throw new ApiExcpection("Project bid not found");
        }
        if (!projectBidToUpdate.getStatus().equals("OPEN")) {
            throw new ApiExcpection("You can't update project bids with status" + projectBidToUpdate.getStatus());
        }
        projectBidToUpdate.setBudget(projectBid.getBudget());
        projectBidToUpdate.setDeadline(projectBid.getDeadline());
        projectBidToUpdate.setDescription(projectBid.getDescription());
        projectBidToUpdate.setTitle(projectBid.getTitle());
        projectBidToUpdate.setLocation(projectBid.getLocation());
        projectBidRepository.save(projectBidToUpdate);
    }

    public void deleteProjectBid(Integer id) {
        ProjectBid projectBidToDelete = projectBidRepository.findProjectBidById(id);
        if (projectBidToDelete == null) {
            throw new ApiExcpection("Project bid not found");
        }
        if (!projectBidToDelete.getStatus().equals("OPEN")) {
            throw new ApiExcpection("You can't delete project bid with status" + projectBidToDelete.getStatus());
        }
        projectBidRepository.delete(projectBidToDelete);
    }

    public List<ProjectBid> getProjectBidByCompanyId(Integer companyId) {
        Company company = companyRepository.findCompanyById(companyId);
        if (company == null) {
            throw new ApiExcpection("Company not found");
        }
        return projectBidRepository.getProjectBidByCompanyId(companyId);
    }

    public List<ProjectBid> getProjectByStatus(String status) {
        List<ProjectBid> projectBids =projectBidRepository.findProjectBidByStatus(status);
        if (projectBids.isEmpty()) {
            throw new ApiExcpection("Project bid not found");
        }
        return projectBids;
    }
}
