package seu.capstone2.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seu.capstone2.Model.ProjectBid;

@Repository
public interface ProjectBidRepository extends JpaRepository<ProjectBid, Integer> {

    ProjectBid findProjectBidById(Integer id);

}
