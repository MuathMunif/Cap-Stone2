package seu.capstone2.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import seu.capstone2.Model.ProjectBid;

import java.util.List;

@Repository
public interface ProjectBidRepository extends JpaRepository<ProjectBid, Integer> {

    ProjectBid findProjectBidById(Integer id);

    @Query("select c FROM ProjectBid c where c.companyId = ?1")
    List<ProjectBid> getProjectBidByCompanyId(Integer companyId);

    List<ProjectBid> findProjectBidByStatus(String status);


    boolean existsByCompanyIdAndStatus(Integer companyId, String status);

}
