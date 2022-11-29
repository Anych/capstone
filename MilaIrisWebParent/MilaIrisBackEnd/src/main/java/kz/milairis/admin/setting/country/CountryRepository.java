package kz.milairis.admin.setting.country;

import java.util.List;

import kz.milairis.common.entity.Country;
import org.springframework.data.repository.CrudRepository;

public interface CountryRepository extends CrudRepository<Country, Integer> {
    List<Country> findAllByOrderByNameAsc();
}
