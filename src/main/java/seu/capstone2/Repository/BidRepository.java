package seu.capstone2.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seu.capstone2.Model.Bid;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Integer> {

    Bid findBidById(Integer id);

    boolean existsBidsByProjectBidIdAndSubmittedByUserId(Integer projectBidId, Integer userId);

    List<Bid> findBidByContractorCompanyId(Integer customerId);

    List<Bid> findBidByProjectBidId(Integer projectBidId);

    List<Bid> findBidByStatusContains(String status);

    List<Bid> findBidByProjectBidIdAndStatusContains(Integer projectBidId, String status);



}
