package kz.milairis.admin.setting.country;

import kz.milairis.common.entity.Country;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryRestController {

	private final CountryRepository repo;

	public CountryRestController(CountryRepository repo) {
		this.repo = repo;
	}

	@GetMapping("/list")
	public List<Country> listAll() {
		return repo.findAllByOrderByNameAsc();
	}

	@PostMapping("/save")
	public String save(@RequestBody Country country) {
		Country savedCountry = repo.save(country);
		System.out.println(savedCountry + "saved");
		return String.valueOf(savedCountry.getId());
	}
	
	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
}
