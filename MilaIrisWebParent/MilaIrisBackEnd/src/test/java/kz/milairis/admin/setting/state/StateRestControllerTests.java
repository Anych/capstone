package kz.milairis.admin.setting.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import kz.milairis.admin.setting.country.CountryRepository;
import kz.milairis.common.entity.Country;
import kz.milairis.common.entity.State;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class StateRestControllerTests {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	CountryRepository countryRepo;

	@Autowired
	StateRepository stateRepo;

	@Test
	@WithMockUser(username = "Anuar", password = "something", roles = "Admin")
	public void testListByCountries() throws Exception {
		Integer countryId = 1;
		String url = "/states/list_by_country/" + countryId;

		MvcResult result = mockMvc.perform(get(url))
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn();

		String jsonResponse = result.getResponse().getContentAsString();
		State[] states = objectMapper.readValue(jsonResponse, State[].class);

		assertThat(states).hasSizeGreaterThan(0);
	}

	@Test
	@WithMockUser(username = "Anuar", password = "something", roles = "Admin")
	public void testCreateState() throws Exception {
		String url = "/states/save";
		Integer countryId = 126;
		Country country = countryRepo.findById(countryId).get();
		State state = new State("Aqtobe", country);

		MvcResult result = mockMvc.perform(post(url).contentType("application/json")
						.content(objectMapper.writeValueAsString(state))
						.with(csrf()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		String response = result.getResponse().getContentAsString();
		Integer stateId = Integer.parseInt(response);
		Optional<State> findById = stateRepo.findById(stateId);

		assertThat(findById.isPresent());
	}

	@Test
	@WithMockUser(username = "Anuar", password = "something", roles = "Admin")
	public void testUpdateState() throws Exception {
		String url = "/states/save";
		Integer stateId = 126;
		String stateName = "Shymkent";

		State state = stateRepo.findById(stateId).get();
		state.setName(stateName);

		mockMvc.perform(post(url).contentType("application/json")
						.content(objectMapper.writeValueAsString(state))
						.with(csrf()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(String.valueOf(stateId)));

		Optional<State> findById = stateRepo.findById(stateId);
		assertThat(findById.isPresent());

		State updatedState = findById.get();
		assertThat(updatedState.getName()).isEqualTo(stateName);

	}

	@Test
	@WithMockUser(username = "Anuar", password = "something", roles = "Admin")
	public void testDeleteState() throws Exception {
		Integer stateId = 1;
		String uri = "/states/delete/" + stateId;

		mockMvc.perform(get(uri)).andExpect(status().isOk());

		Optional<State> findById = stateRepo.findById(stateId);

		assertThat(findById).isNotPresent();
	}
}
