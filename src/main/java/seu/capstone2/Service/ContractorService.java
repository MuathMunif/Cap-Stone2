package seu.capstone2.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seu.capstone2.Api.ApiExcpection;
import seu.capstone2.Model.Contractor;
import seu.capstone2.Repository.BidRepository;
import seu.capstone2.Repository.ContractorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractorService {


    private final ContractorRepository contractorRepository;
    private final BidRepository bidRepository;


    public List<Contractor> findAllContractors() {
        return contractorRepository.findAll();
    }


    public void addContractor(Contractor contractor) {
        contractorRepository.save(contractor);
    }



    public void updateContractor(Integer id, Contractor contractor) {
        Contractor old = contractorRepository.findContractorById(id);
        if (old == null) {
            throw new ApiExcpection("Contractor not found");
        }
        old.setName(contractor.getName());
        old.setCity(contractor.getCity());
        old.setPhone(contractor.getPhone());
        old.setEmail(contractor.getEmail());

        contractorRepository.save(old);
    }



    public void deleteContractor(Integer id) {
        Contractor contractor = contractorRepository.findContractorById(id);
        if (contractor == null) {
            throw new ApiExcpection("Contractor not found");
        }
        boolean hasActiveBids = bidRepository.existsByContractorIdAndStatus(id,"ACCEPTED");

        if (hasActiveBids) {
            throw new ApiExcpection("Cannot delete contractor with active bids");
        }
        contractorRepository.delete(contractor);
    }


}
