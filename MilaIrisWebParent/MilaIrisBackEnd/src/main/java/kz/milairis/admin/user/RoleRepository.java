package kz.milairis.admin.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import kz.milairis.common.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

}
