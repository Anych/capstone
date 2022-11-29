package kz.milairis.admin.brand.controller;

import kz.milairis.admin.FileUploadUtil;
import kz.milairis.admin.brand.exception.BrandNotFoundException;
import kz.milairis.admin.brand.service.BrandService;
import kz.milairis.admin.category.service.CategoryService;
import kz.milairis.admin.paging.PagingAndSortingHelper;
import kz.milairis.admin.paging.PagingAndSortingParam;
import kz.milairis.common.entity.Brand;
import kz.milairis.common.entity.Category;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/brands")
public class BrandController {
	private final String defaultRedirectURL = "redirect:/brands/page/1?sortField=name&sortDir=asc";
	private final BrandService brandService;
	private final CategoryService categoryService;

	public BrandController(BrandService brandService, CategoryService categoryService) {
		this.brandService = brandService;
		this.categoryService = categoryService;
	}

	@GetMapping("")
	public String listFirstPage() {
		return defaultRedirectURL;
	}

	@GetMapping("/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listBrands", moduleURL = "/brands") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum
	) {
		brandService.listByPage(pageNum, helper);
		return "brands/brands";
	}

	@GetMapping("/new")
	public String newBrand(Model model) {
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();

		model.addAttribute("listCategories", listCategories);
		model.addAttribute("brand", new Brand());
		model.addAttribute("pageTitle", "Create New Brand");

		return "brands/brand_form";
	}

	@PostMapping("/save")
	public String saveBrand(Brand brand, @RequestParam("fileImage") MultipartFile multipartFile,
							RedirectAttributes ra) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
			brand.setLogo(fileName);

			Brand savedBrand = brandService.save(brand);
			String uploadDir = "../brand-logos/" + savedBrand.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

		} else {
			brandService.save(brand);
		}

		ra.addFlashAttribute("message", "The brand has been saved successfully.");
		return defaultRedirectURL;
	}

	@GetMapping("/edit/{id}")
	public String editBrand(@PathVariable(name = "id") Integer id, Model model,
							RedirectAttributes ra) {
		try {
			Brand brand = brandService.get(id);
			List<Category> listCategories = categoryService.listCategoriesUsedInForm();

			model.addAttribute("brand", brand);
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("pageTitle", "Edit Brand (ID: " + id + ")");

			return "brands/brand_form";
		} catch (BrandNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return defaultRedirectURL;
		}
	}

	@GetMapping("/delete/{id}")
	public String deleteBrand(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			brandService.delete(id);
			String brandDir = "../brand-logos/" + id;
			FileUploadUtil.removeDir(brandDir);

			redirectAttributes.addFlashAttribute("message",
					"The brand ID " + id + " has been deleted successfully");
		} catch (BrandNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}

		return defaultRedirectURL;
	}
}
