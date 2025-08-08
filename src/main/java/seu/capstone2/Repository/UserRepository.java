package seu.capstone2.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seu.capstone2.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserById(Integer id);
}
