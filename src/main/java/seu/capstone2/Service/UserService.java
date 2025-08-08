package seu.capstone2.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seu.capstone2.Api.ApiExcpection;
import seu.capstone2.Model.User;
import seu.capstone2.Repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public void updateUser(Integer id ,User user) {
        User oldUser = userRepository.findUserById(id);
        if(oldUser == null) {
            throw new ApiExcpection("User not found");
        }
        oldUser.setName(user.getName());
        oldUser.setPassword(user.getPassword());
        oldUser.setEmail(user.getEmail());
        userRepository.save(oldUser);
    }


    public void deleteUser(Integer id) {
        User userToDelete = userRepository.findUserById(id);
        if(userToDelete == null) {
            throw new ApiExcpection("User with id " + id + " not found");
        }
        userRepository.delete(userToDelete);
    }
}
