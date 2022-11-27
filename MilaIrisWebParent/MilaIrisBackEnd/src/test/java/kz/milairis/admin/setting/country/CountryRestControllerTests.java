package kz.milairis.admin.setting.country;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.milairis.common.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@WithUserDetails(value = "anuar123@mail.ru")
public class CountryRestControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CountryRepository repo;

    @Test
    public void testGetCountriesList() throws Exception {
        String url = "/countries/list";

        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Country[] countries = objectMapper.readValue(jsonResponse, Country[].class);

        assertThat(countries).hasSizeGreaterThan(100);
    }

    @Test
    public void testCreateCountry() throws Exception {
        String url = "/countries/save";
        Country country = new Country(1, "Epam Republic", "ER");

        MvcResult result = mockMvc.perform(post(url).contentType("application/json")
                        .content(objectMapper.writeValueAsString(country))
                        .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();

        assertThat(result).isNotNull();
    }

    @Test
    public void testDeleteCountry() throws Exception {
        int countryId = 1;
        String uri = "/countries/delete/" + countryId;

        mockMvc.perform(delete(uri).with(csrf())).andExpect(status().isOk());

        Optional<Country> findById = repo.findById(1);

        assertThat(findById).isNotPresent();
    }
}
