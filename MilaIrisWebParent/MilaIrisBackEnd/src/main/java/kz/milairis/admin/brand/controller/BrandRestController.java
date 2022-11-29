package kz.milairis.admin.brand.controller;

import kz.milairis.admin.brand.exception.BrandNotFoundException;
import kz.milairis.admin.brand.exception.BrandNotFoundRestException;
import kz.milairis.admin.brand.service.BrandService;
import kz.milairis.admin.brand.CategoryDTO;
import kz.milairis.common.entity.Brand;
import kz.milairis.common.entity.Category;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/brands")
public class BrandRestController {
	private final BrandService service;

	public BrandRestController(BrandService service) {
		this.service = service;
	}

	@PostMapping("/check_unique")
	public String checkUnique(@Param("id") Integer id, @Param("name") String name) {
		return service.checkUnique(id, name);
	}

	@GetMapping("/{id}/categories")
	public List<CategoryDTO> listCategoriesByBrand(@PathVariable(name = "id") Integer brandId) throws BrandNotFoundRestException {
		List<CategoryDTO> listCategories = new ArrayList<>();

		try {
			Brand brand = service.get(brandId);
			Set<Category> categories = brand.getCategories();

			for (Category category : categories) {
				CategoryDTO dto = new CategoryDTO(category.getId(), category.getName());
				listCategories.add(dto);
			}

			return listCategories;
		} catch (BrandNotFoundException e) {
			throw new BrandNotFoundRestException();
		}
	}
}
