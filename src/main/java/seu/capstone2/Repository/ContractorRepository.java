package seu.capstone2.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seu.capstone2.Model.Contractor;

public interface ContractorRepository extends JpaRepository<Contractor, Integer> {

    Contractor findContractorById(Integer id);
}
