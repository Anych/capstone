package kz.milairis.admin.product.controller;

import kz.milairis.admin.FileUploadUtil;
import kz.milairis.admin.brand.service.BrandService;
import kz.milairis.admin.category.service.CategoryService;
import kz.milairis.admin.paging.PagingAndSortingHelper;
import kz.milairis.admin.paging.PagingAndSortingParam;
import kz.milairis.admin.product.ProductSaveHelper;
import kz.milairis.admin.product.service.ProductService;
import kz.milairis.admin.security.userdetail.MilaIrisUserDetails;
import kz.milairis.common.entity.Brand;
import kz.milairis.common.entity.Category;
import kz.milairis.common.entity.product.Product;

import kz.milairis.common.exception.ProductNotFoundException;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final String defaultRedirectURL = "redirect:/products/page/1?sortField=name&sortDir=asc&categoryId=0";
    private final ProductService productService;
    private final BrandService brandService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, BrandService brandService,
                             CategoryService categoryService) {
        this.productService = productService;
        this.brandService = brandService;
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public String listFirstPage() {
        return defaultRedirectURL;
    }

    @GetMapping("/page/{pageNum}")
    public String listByPage(
            @PagingAndSortingParam(listName = "listProducts", moduleURL = "/products") PagingAndSortingHelper helper,
            @PathVariable(name = "pageNum") int pageNum, Model model,
            @Param("categoryId") Integer categoryId
    ) {

        productService.listByPage(pageNum, helper, categoryId);

        List<Category> listCategories = categoryService.listCategoriesUsedInForm();

        if (categoryId != null) model.addAttribute("categoryId", categoryId);
        model.addAttribute("listCategories", listCategories);

        return "products/products";
    }

    @GetMapping("/new")
    public String newProduct(Model model) {
        List<Brand> listBrands = brandService.listAll();

        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);

        model.addAttribute("product", product);
        model.addAttribute("listBrands", listBrands);
        model.addAttribute("pageTitle", "Create New Product");
        model.addAttribute("numberOfExistingExtraImages", 0);

        return "products/product_form";
    }

    @PostMapping("/save")
    public String saveProduct(Product product, RedirectAttributes ra,
                              @RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultipart,
                              @RequestParam(value = "extraImageMultipart", required = false)
                                  MultipartFile[] extraImageMultipart,
                              @RequestParam(name = "detailIDs", required = false) String[] detailIDs,
                              @RequestParam(name = "detailNames", required = false) String[] detailNames,
                              @RequestParam(name = "detailValues", required = false) String[] detailValues,
                              @RequestParam(name = "imageIDs", required = false) String[] imageIDs,
                              @RequestParam(name = "imageNames", required = false) String[] imageNames,
                              @AuthenticationPrincipal MilaIrisUserDetails loggedUser
    )
            throws IOException {

        if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
            if (loggedUser.hasRole("Salesperson")) {
                productService.saveProductPrice(product);
                ra.addFlashAttribute("message", "The product has been saved successfully.");
                return defaultRedirectURL;
            }
        }

        ProductSaveHelper.setMainImageName(mainImageMultipart, product);
        ProductSaveHelper.setExistingExtraImageNames(imageIDs, imageNames, product);
        ProductSaveHelper.setNewExtraImageNames(extraImageMultipart, product);
        ProductSaveHelper.setProductDetails(detailIDs, detailNames, detailValues, product);

        Product savedProduct = productService.save(product);

        ProductSaveHelper.saveUploadedImages(mainImageMultipart, extraImageMultipart, savedProduct);

        ProductSaveHelper.deleteExtraImagesWereRemovedOnForm(product);

        ra.addFlashAttribute("message", "The product has been saved successfully.");

        return defaultRedirectURL;
    }


    @GetMapping("/{id}/enabled/{status}")
    public String updateProductEnabledStatus(@PathVariable("id") Integer id,
                                             @PathVariable("status") boolean enabled,
                                             RedirectAttributes redirectAttributes) {
        productService.updateProductEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The Product ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return defaultRedirectURL;
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Integer id,
                                RedirectAttributes redirectAttributes) {
        try {
            productService.delete(id);
            String productExtraImagesDir = "../product-images/" + id + "/extras";
            String productImagesDir = "../product-images/" + id;

            FileUploadUtil.removeDir(productExtraImagesDir);
            FileUploadUtil.removeDir(productImagesDir);

            redirectAttributes.addFlashAttribute("message",
                    "The product ID " + id + " has been deleted successfully");
        } catch (ProductNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return defaultRedirectURL;
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id, Model model,
                              RedirectAttributes ra) {
        try {
            Product product = productService.get(id);
            List<Brand> listBrands = brandService.listAll();
            Integer numberOfExistingExtraImages = product.getImages().size();

            model.addAttribute("product", product);
            model.addAttribute("listBrands", listBrands);
            model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");
            model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);


            return "products/product_form";

        } catch (ProductNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());

            return defaultRedirectURL;
        }
    }

    @GetMapping("/detail/{id}")
    public String viewProductDetails(@PathVariable("id") Integer id, Model model,
                                     RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.get(id);
            model.addAttribute("product", product);

            return "products/product_detail_modal";

        } catch (ProductNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());

            return defaultRedirectURL;
        }
    }
}
