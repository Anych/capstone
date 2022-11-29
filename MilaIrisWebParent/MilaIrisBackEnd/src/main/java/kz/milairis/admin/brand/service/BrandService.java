package kz.milairis.admin.brand.service;

import kz.milairis.admin.brand.exception.BrandNotFoundException;
import kz.milairis.admin.brand.repository.BrandRepository;
import kz.milairis.admin.paging.PagingAndSortingHelper;
import kz.milairis.common.entity.Brand;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BrandService {
	public static final int BRANDS_PER_PAGE = 10;

	private final BrandRepository repo;

	public BrandService(BrandRepository repo) {
		this.repo = repo;
	}

	public List<Brand> listAll() {
		return repo.findAll();
	}

	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, BRANDS_PER_PAGE, repo);
	}

	public Brand save(Brand brand) {
		return repo.save(brand);
	}

	public Brand get(Integer id) throws BrandNotFoundException {
		return repo.findById(id).orElseThrow(
				() -> new BrandNotFoundException("Could not find any brand with ID " + id)
		);
	}

	public void delete(Integer id) throws BrandNotFoundException {
		Long countById = repo.countById(id);

		if (countById == null || countById == 0) {
			throw new BrandNotFoundException("Could not find any brand with ID " + id);
		}

		repo.deleteById(id);
	}

	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		Brand brandByName = repo.findByName(name);

		if (isCreatingNew) {
			if (brandByName != null) return "Duplicate";
		} else {
			if (brandByName != null && !Objects.equals(brandByName.getId(), id)) {
				return "Duplicate";
			}
		}

		return "OK";
	}
}
