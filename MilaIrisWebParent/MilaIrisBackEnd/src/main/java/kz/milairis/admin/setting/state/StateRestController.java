package kz.milairis.admin.setting.state;

import kz.milairis.common.entity.Country;
import kz.milairis.common.entity.State;
import kz.milairis.common.entity.StateDTO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/states")
public class StateRestController {

	private final StateRepository repo;

	public StateRestController(StateRepository repo) {
		this.repo = repo;
	}

	@GetMapping("/list_by_country/{id}")
	public List<StateDTO> listByCountry(@PathVariable("id") Integer countryId) {
		List<State> listStates = repo.findByCountryOrderByNameAsc(new Country(countryId));
		List<StateDTO> result = new ArrayList<>();
		
		for (State state : listStates) {
			result.add(new StateDTO(state.getId(), state.getName()));
		}
		
		return result;
	}
	
	@PostMapping("/save")
	public String save(@RequestBody State state) {
		State savedState = repo.save(state);
		return String.valueOf(savedState.getId());
	}
	
	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
}
