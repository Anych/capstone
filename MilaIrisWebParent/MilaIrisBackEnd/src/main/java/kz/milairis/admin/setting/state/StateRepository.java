package kz.milairis.admin.setting.state;

import kz.milairis.common.entity.Country;
import kz.milairis.common.entity.State;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StateRepository extends CrudRepository<State, Integer> {

    public List<State> findByCountryOrderByNameAsc(Country country);
}
