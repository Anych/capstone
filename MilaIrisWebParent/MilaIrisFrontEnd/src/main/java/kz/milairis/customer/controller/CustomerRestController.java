package kz.milairis.customer.controller;

import kz.milairis.customer.service.CustomerService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {

	private final CustomerService service;

	public CustomerRestController(CustomerService service) {
		this.service = service;
	}

	@PostMapping("/customers/check_unique_email")
	public String checkDuplicateEmail(@Param("email") String email) {
		return service.isEmailUnique(email) ? "OK" : "Duplicated";
	}
}
