package kz.milairis.admin.product;

import kz.milairis.common.entity.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@WithUserDetails(value = "anuar123@mail.ru")
public class ProductControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Test
    public void testProductsFirstPage() throws Exception {
        this.mockMvc.perform(get("/products"))
                .andExpect(status().is3xxRedirection())
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/products/page/1?sortField=name&sortDir=asc&categoryId=0"))
                .andExpect(handler().handlerType(ProductController.class))
                .andExpect(handler().methodName("listFirstPage"));
    }

    @Test
    public void testUpdateProductIsEnabledStatus() throws Exception {
        int id = 1;
        boolean enabled = true;
        String status = "enabled";
        String url = "/products/" + id + "/enabled/" + enabled;
        String message = "The Product ID " + id + " has been " + status;

        this.mockMvc.perform(get(url))
                .andExpect(status().is3xxRedirection())
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/products/page/1?sortField=name&sortDir=asc&categoryId=0"))
                .andExpect(handler().handlerType(ProductController.class))
                .andExpect(handler().methodName("updateProductEnabledStatus"))
                .andExpect(flash().attribute("message", message));
    }

    @Test
    public void testUpdateProductIsDisabledStatus() throws Exception {
        int id = 1;
        boolean enabled = false;
        String status = "disabled";
        String url = "/products/" + id + "/enabled/" + enabled;
        String message = "The Product ID " + id + " has been " + status;

        this.mockMvc.perform(get(url))
                .andExpect(status().is3xxRedirection())
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/products/page/1?sortField=name&sortDir=asc&categoryId=0"))
                .andExpect(handler().handlerType(ProductController.class))
                .andExpect(handler().methodName("updateProductEnabledStatus"))
                .andExpect(flash().attribute("message", message));
    }

    @Test
    public void testGetEditProduct() throws Exception {
        int id = 1;
        String pageTitle = "Edit Product (ID: " + id + ")";

        this.mockMvc.perform(get("/products/edit/" + id))
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(handler().handlerType(ProductController.class))
                .andExpect(handler().methodName("editProduct"))
                .andExpect(model().attribute("pageTitle", pageTitle));
    }

    @Test
    public void testGetEditProductWithException() throws Exception {
        int id = -1;

        this.mockMvc.perform(get("/products/edit/" + id))
                .andExpect(status().is3xxRedirection())
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/products/page/1?sortField=name&sortDir=asc&categoryId=0"))
                .andExpect(handler().handlerType(ProductController.class))
                .andExpect(handler().methodName("editProduct"))
                .andExpect(flash().attribute("message", "Could not find any product with ID " + id));
    }

    @Test
    public void testViewProductDetails() throws Exception {
        int id = 1;

        this.mockMvc.perform(get("/products/detail/" + id))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ProductController.class))
                .andExpect(handler().methodName("viewProductDetails"));
    }

    @Test
    public void testViewProductDetailsWithException() throws Exception {
        int id = -1;

        this.mockMvc.perform(get("/products/detail/" + id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products/page/1?sortField=name&sortDir=asc&categoryId=0"))
                .andExpect(handler().handlerType(ProductController.class))
                .andExpect(handler().methodName("viewProductDetails"))
                .andExpect(flash().attribute("message", "Could not find any product with ID " + id));
    }
}
