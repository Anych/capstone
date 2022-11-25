package kz.milairis.admin.category;

import kz.milairis.admin.brand.BrandRepository;
import kz.milairis.admin.brand.BrandService;
import kz.milairis.common.entity.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@WithUserDetails(value = "anuar123@mail.ru")
public class CategoryControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    public void testCategoriesFirstPage() throws Exception {
        this.mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(handler().handlerType(CategoryController.class))
                .andExpect(handler().methodName("listFirstPage"))
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(model().attribute("sortField", "name"))
                .andExpect(model().attribute("sortDir", "asc"))
                .andExpect(model().attribute("startCount", (long)1))
                .andExpect(model().attribute("endCount", (long)2))
                .andExpect(model().attribute("reverseSortDir", "desc"))
                .andExpect(model().attribute("moduleURL", "/categories"));
    }

    @Test
    public void testGetNewCategoriesPage() throws Exception {
        this.mockMvc.perform(get("/categories/new"))
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(handler().handlerType(CategoryController.class))
                .andExpect(handler().methodName("newCategory"))
                .andExpect(model().attribute("pageTitle", "Create New Category"));
    }

//    @Test
//    public void testSaveNewCategoriesPage() throws Exception {
//        this.mockMvc.perform(multipart("/categories/save").param("fileImage", "")
//                        .with(csrf()))
//                .andDo(print())
//                .andExpect(flash().attributeExists("The category has been saved successfully."));
//    }
}
