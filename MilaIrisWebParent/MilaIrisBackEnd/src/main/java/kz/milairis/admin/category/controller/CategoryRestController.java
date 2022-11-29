package kz.milairis.admin.category.controller;

import kz.milairis.admin.category.service.CategoryService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {

	private final CategoryService service;

	public CategoryRestController(CategoryService service) {
		this.service = service;
	}

	@PostMapping("/categories/check_unique")
	public String checkUnique(@Param("id") Integer id, @Param("name") String name,
							  @Param("alias") String alias) {
		return service.checkUnique(id, name, alias);
	}
}
