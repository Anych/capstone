package kz.milairis.admin.brand;

import kz.milairis.admin.brand.controller.BrandController;
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
public class BrandControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testBrandsFirstPage() throws Exception {
        this.mockMvc.perform(get("/brands"))
                .andExpect(status().is3xxRedirection())
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/brands/page/1?sortField=name&sortDir=asc"))
                .andExpect(handler().handlerType(BrandController.class))
                .andExpect(handler().methodName("listFirstPage"));
    }
}