package kz.milairis.admin.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.milairis.admin.category.controller.CategoryController;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
    ObjectMapper objectMapper;

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
        String url = "/categories/new";
        this.mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(handler().handlerType(CategoryController.class))
                .andExpect(handler().methodName("newCategory"))
                .andExpect(model().attribute("pageTitle", "Create New Category"));
    }

//    @Test
//    public void testSaveNewCategoriesPage() throws Exception {
//        String url = "/categories/save";
//        this.mockMvc.perform(multipart(url).content(objectMapper.writeValueAsString("asd"))
//                        .with(csrf()))
//                .andDo(print())
//                .andExpect(flash().attributeExists("The category has been saved successfully."));
//    }
}
