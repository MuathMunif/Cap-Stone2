package seu.capstone2.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seu.capstone2.Api.ApiExcpection;
import seu.capstone2.Model.Company;
import seu.capstone2.Repository.CompanyRepository;
import seu.capstone2.Repository.ProjectBidRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final ProjectBidRepository projectBidRepository;



    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }



    public void addCompany(Company company) {
        companyRepository.save(company);
    }

    public void updateCompany(Integer id, Company company) {
        Company old = companyRepository.findCompanyById(id);
        if (old == null) {
            throw new ApiExcpection("Company not found");
        }
        old.setName(company.getName());
        old.setCity(company.getCity());
        old.setPhone(company.getPhone());
        old.setEmail(company.getEmail());
        companyRepository.save(old);
    }

    public void deleteCompany(Integer id) {
        Company company = companyRepository.findCompanyById(id);
        if (company == null) {
            throw new ApiExcpection("Company not found");
        }
        boolean hasActiveProjects = projectBidRepository.existsByCompanyIdAndStatus(id,"AWARDED");
        if (hasActiveProjects) {
            throw new ApiExcpection("Cannot delete a company with active projects");
        }

        companyRepository.delete(company);
    }
}
