package seu.capstone2.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import seu.capstone2.Model.Company;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    Company findCompanyById(Integer id);

    boolean existsByCrNumber(String crNumber);

    boolean existsByCreatedByUserId(Integer createdByUserId);

    @Query("select u from Company u where u.createdByUserId = ?1")
    List<Company>  getAllCompanyByCreatedByUserId(Integer createdByUserId);

}
