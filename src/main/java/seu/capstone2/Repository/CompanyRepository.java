package seu.capstone2.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seu.capstone2.Model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    Company findCompanyById(Integer id);

    boolean existsByCrNumber(String crNumber);

    boolean existsByCreatedByUserId(Integer createdByUserId);

}
