package seu.capstone2.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seu.capstone2.Model.Bid;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Integer> {

    Bid findBidById(Integer id);


    List<Bid> findBidByContractorId(Integer contractorId);

    List<Bid> findBidByProjectBidId(Integer projectBidId);

    List<Bid> findBidByStatusContains(String status);

    List<Bid> findBidByProjectBidIdAndStatusContains(Integer projectBidId, String status);

    boolean existsByContractorIdAndStatus(Integer contractorId, String status);

    boolean existsByProjectBidIdAndContractorId(Integer projectBidId, Integer contractorId);

}
