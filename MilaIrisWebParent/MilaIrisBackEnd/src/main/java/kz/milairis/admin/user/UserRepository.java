package kz.milairis.admin.user;

import kz.milairis.common.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("SELECT u from User u WHERE u.email = :email")
    public User getUsersByEmail(@Param("email") String email);
}
