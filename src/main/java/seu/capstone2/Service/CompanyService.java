package seu.capstone2.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seu.capstone2.Api.ApiExcpection;
import seu.capstone2.Model.Company;
import seu.capstone2.Model.User;
import seu.capstone2.Repository.CompanyRepository;
import seu.capstone2.Repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    public void addCompany(Company company) { //todo check
        User user = userRepository.findUserById(company.getCreatedByUserId());
        if (user == null) {
            throw new ApiExcpection("User not found");
        }
        if (user.getRole().equals("CONTRACTOR")) {
            company.setType("CONTRACTOR");
        } else if (user.getRole().equals("OWNER")) {
            company.setType("OWNER");
        } else {
            throw new ApiExcpection("Invalid user role for creating a company");
        }

        if (companyRepository.existsByCrNumber(company.getCrNumber())) {
            throw new ApiExcpection("CR number already exists");
        }
        companyRepository.save(company);
    }

    public void updateCompany(Integer id ,Company company) {
        Company oldCompany = companyRepository.findCompanyById(id);
        if (oldCompany == null) {
            throw new ApiExcpection("Company not found");
        }
        oldCompany.setName(company.getName());
        oldCompany.setCity(company.getCity());
        oldCompany.setPhone(company.getPhone());
        oldCompany.setCrNumber(company.getCrNumber());
        companyRepository.save(oldCompany);
    }

    public void deleteCompany(Integer id) {
        Company company = companyRepository.findCompanyById(id);
        if (company == null) {
            throw new ApiExcpection("Company not found");
        }
        companyRepository.delete(company);
    }



}
