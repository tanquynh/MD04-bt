package ra.md04bt.service;
import ra.md04bt.entity.User;
import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User save(User user);

    User getUserById(Long id);

    User update(Long id, User user);

    boolean delete(Long id);
}
