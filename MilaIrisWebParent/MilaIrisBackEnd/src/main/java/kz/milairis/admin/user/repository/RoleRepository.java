package kz.milairis.admin.user.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import kz.milairis.common.entity.user.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
}
