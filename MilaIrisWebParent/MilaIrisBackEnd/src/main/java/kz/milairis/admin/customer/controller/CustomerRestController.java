package kz.milairis.admin.customer.controller;

import kz.milairis.admin.customer.service.CustomerService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {
	private final CustomerService service;

	public CustomerRestController(CustomerService service) {
		this.service = service;
	}

	@PostMapping("/customers/check_email")
	public String checkDuplicateEmail(@Param("id") Integer id, @Param("email") String email) {
		if (service.isEmailUnique(id, email)) {
			return "OK";
		} else {
			return "Duplicated";
		}
	}
}
