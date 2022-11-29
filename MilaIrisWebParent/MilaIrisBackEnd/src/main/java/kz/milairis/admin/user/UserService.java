package kz.milairis.admin.user;

import kz.milairis.admin.paging.PagingAndSortingHelper;
import kz.milairis.admin.user.repository.RoleRepository;
import kz.milairis.admin.user.repository.UserRepository;
import kz.milairis.common.entity.user.Role;
import kz.milairis.common.entity.user.User;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class UserService {
    public static final int USERS_PER_PAGE = 4;

    private final UserRepository userRepo;

    private final RoleRepository roleRepo;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User getByEmail(String email) {
        return userRepo.getUserByEmail(email);
    }

    public List<User> listAll() {
        return (List<User>) userRepo.findAll(Sort.by("firstName").ascending());
    }

    public void listByPage(int pageNum, PagingAndSortingHelper helper) {
        helper.listEntities(pageNum, USERS_PER_PAGE, userRepo);
    }

    public List<Role> listRoles() {
        return (List<Role>) roleRepo.findAll();
    }

    public User save(User user) throws UserNotFoundException {
        boolean isUpdatingUser = (user.getId() != null);

        if (isUpdatingUser) {
            User existingUser = userRepo.findById(user.getId()).orElseThrow(
                    () -> new UserNotFoundException("Something went wrong with user ID: " + user.getId())
            );

            if (user.getPassword().isEmpty()) {
                user.setPassword(Objects.requireNonNull(existingUser).getPassword());
            } else {
                encodePassword(user);
            }

        } else {
            encodePassword(user);
        }

        return userRepo.save(user);
    }

    public User updateAccount(User userInForm) {
        User userInDB = userRepo.findById(userInForm.getId()).orElseThrow(
                );

        if (!userInForm.getPassword().isEmpty()) {
            Objects.requireNonNull(userInDB).setPassword(userInForm.getPassword());
            encodePassword(userInDB);
        }

        if (userInForm.getPhotos() != null) {
            Objects.requireNonNull(userInDB).setPhotos(userInForm.getPhotos());
        }

        Objects.requireNonNull(userInDB).setFirstName(userInForm.getFirstName());
        userInDB.setLastName(userInForm.getLastName());

        return userRepo.save(userInDB);
    }

    private void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public boolean isEmailUnique(Integer id, String email) {
        User userByEmail = userRepo.getUserByEmail(email);

        if (userByEmail == null) return true;

        boolean isCreatingNew = (id == null);

        if (isCreatingNew) {
            return false;
        } else {
            return Objects.equals(userByEmail.getId(), id);
        }
    }

    public User get(Integer id) throws UserNotFoundException {
        return userRepo.findById(id).orElseThrow(
                () -> new UserNotFoundException("Could not find any user with ID " + id)
        );
    }

    public void delete(Integer id) throws UserNotFoundException {
        Long countById = userRepo.countById(id);
        if (countById == null || countById == 0) {
            throw new UserNotFoundException("Could not find any user with ID " + id);
        }

        userRepo.deleteById(id);
    }

    public void updateUserEnabledStatus(Integer id, boolean enabled) {
        userRepo.updateEnabledStatus(id, enabled);
    }
}
