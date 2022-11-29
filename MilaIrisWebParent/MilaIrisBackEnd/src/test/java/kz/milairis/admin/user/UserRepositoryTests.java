package kz.milairis.admin.user;

import kz.milairis.admin.user.repository.UserRepository;
import kz.milairis.common.entity.user.Role;
import kz.milairis.common.entity.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests {

    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateUser() {
        Role roleAdmin = entityManager.find(Role.class, 1);
        User userAdmin = new User("anuar123@mail.ru", "anuar1992", "Anuar", "Umarov");
        userAdmin.addRole(roleAdmin);

        User savedUser = repo.save(userAdmin);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateNewUserWithTwoRoles() {
        User userAnuar = new User("anuar123@gmail.com", "anuar1992", "Anuar", "Umarov");
        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);

        userAnuar.addRole(roleEditor);
        userAnuar.addRole(roleAssistant);

        User savedUser = repo.save(userAnuar);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers() {
        Iterable<User> listUsers = repo.findAll();
        listUsers.forEach(user -> System.out.println(user));
    }

    @Test
    public void testGetUserById() {
        User userAdmin = repo.findById(1).get();
        assertThat(userAdmin).isNotNull();
    }

    @Test
    public void testUpdateUserDetails() {
        User userAdmin = repo.findById(1).get();
        userAdmin.setEnabled(true);
        userAdmin.setEmail("anuar123@mail.ru");

        repo.save(userAdmin);
    }

    @Test
    public void testUpdateUserRoles() {
        User userAnuar = repo.findById(2).get();
        Role roleEditor = new Role(3);
        Role roleSalesPerson = new Role(2);

        userAnuar.getRoles().remove(roleEditor);
        userAnuar.addRole(roleSalesPerson);

        repo.save(userAnuar);
    }

    @Test
    public void testDeleteUser() {
        Integer userId = 2;
        repo.deleteById(userId);
    }

    @Test
    public void testGetUserByEmail() {
        String email = "botaonelove@yandex.ru";
        User user = repo.getUserByEmail(email);

        assertThat(user).isNotNull();
    }

    @Test
    public void testCountById() {
        Integer id = 1;
        Long countById = repo.countById(id);

        assertThat(countById).isNotNull().isGreaterThan(0);
    }

    @Test
    public void testDisableUser() {
        Integer id = 1;
        repo.updateEnabledStatus(id, false);
    }

    @Test
    public void testEnableUser() {
        Integer id = 12;
        repo.updateEnabledStatus(id, true);
    }

    @Test
    public void testListFirstPage() {
        int pageNumber = 0;
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repo.findAll(pageable);

        List<User> listUsers = page.getContent();

        assertThat(listUsers.size()).isEqualTo(pageSize);
    }

    @Test
    public void testSearchUsers() {
        String keyword = "anuar";

        int pageNumber = 0;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repo.findAll(keyword, pageable);

        List<User> listUsers = page.getContent();

        listUsers.forEach(user -> System.out.println(user));

        Assertions.assertThat(listUsers.size()).isGreaterThan(0);
    }
}
