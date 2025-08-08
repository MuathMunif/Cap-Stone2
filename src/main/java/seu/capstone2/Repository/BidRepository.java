package seu.capstone2.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seu.capstone2.Model.Bid;

@Repository
public interface BidRepository extends JpaRepository<Bid, Integer> {

    Bid findBidById(Integer id);

    boolean existsBidsByProjectBidIdAndSubmittedByUserId(Integer projectBidId, Integer userId);
}
