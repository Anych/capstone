package kz.milairis.setting.controller;

import kz.milairis.common.entity.Country;
import kz.milairis.common.entity.State;
import kz.milairis.common.entity.StateDTO;
import kz.milairis.setting.repository.StateRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StateRestController {

	private final StateRepository repo;

	public StateRestController(StateRepository repo) {
		this.repo = repo;
	}

	@GetMapping("/settings/list_states_by_country/{id}")
	public List<StateDTO> listByCountry(@PathVariable("id") Integer countryId) {
		List<State> listStates = repo.findByCountryOrderByNameAsc(new Country(countryId));
		List<StateDTO> result = new ArrayList<>();

		for (State state : listStates) {
			result.add(new StateDTO(state.getId(), state.getName()));
		}

		return result;
	}

}