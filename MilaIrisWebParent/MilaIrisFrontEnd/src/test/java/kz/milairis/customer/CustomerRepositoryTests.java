package kz.milairis.customer;

import kz.milairis.common.entity.Country;
import kz.milairis.common.entity.user.Customer;
import kz.milairis.customer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {

	@Autowired private CustomerRepository repo;
	@Autowired private TestEntityManager entityManager;
	
	@Test
	public void testCreateCustomer1() {
		Integer countryId = 126; // Kazakhstan
		Country country = entityManager.find(Country.class, countryId);
		
		Customer customer = new Customer();
		customer.setCountry(country);
		customer.setFirstName("John");
		customer.setLastName("Doe");
		customer.setPassword("password123");
		customer.setEmail("john_doe@gmail.com");
		customer.setPhoneNumber("777-777-77-77");
		customer.setAddressLine1("Nazarbayeva 777");
		customer.setCity("Almaty");
		customer.setState("Almaty");
		customer.setPostalCode("777777");
		customer.setCreatedTime(new Date());
		
		Customer savedCustomer = repo.save(customer);
		
		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateCustomer2() {
		Integer countryId = 126; // Kazakhstan
		Country country = entityManager.find(Country.class, countryId);
		
		Customer customer = new Customer();
		customer.setCountry(country);
		customer.setFirstName("Timur");
		customer.setLastName("Timurov");
		customer.setPassword("password456");
		customer.setEmail("timur_timurov@gmail.com");
		customer.setPhoneNumber("701-777-77-77");
		customer.setAddressLine1("Tokayeva, 777");
		customer.setAddressLine2("Baiterek disctrict");
		customer.setCity("Astana");
		customer.setState("Astana");
		customer.setPostalCode("050060");
		customer.setCreatedTime(new Date());
		
		Customer savedCustomer = repo.save(customer);
		
		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
	}	
	
	@Test
	public void testListCustomers() {
		Iterable<Customer> customers = repo.findAll();
		customers.forEach(System.out::println);
		
		assertThat(customers).hasSizeGreaterThan(1);
	}
	
	@Test
	public void testUpdateCustomer() {
		Integer customerId = 1;
		String lastName = "Dayrabayev";
		
		Customer customer = repo.findById(customerId).get();
		customer.setLastName(lastName);
		customer.setEnabled(true);
		
		Customer updatedCustomer = repo.save(customer);
		assertThat(updatedCustomer.getLastName()).isEqualTo(lastName);
	}
	
	@Test
	public void testGetCustomer() {
		Integer customerId = 2;
		Optional<Customer> findById = repo.findById(customerId);
		
		assertThat(findById).isPresent();
		
		Customer customer = findById.get();
		System.out.println(customer);
	}
	
	@Test
	public void testDeleteCustomer() {
		Integer customerId = 2;
		repo.deleteById(customerId);
		
		Optional<Customer> findById = repo.findById(customerId);		
		assertThat(findById).isNotPresent();		
	}
	
	@Test
	public void testFindByEmail() {
		String email = "john_doe@gmail.com";
		Customer customer = repo.findByEmail(email);
		
		assertThat(customer).isNotNull();
		System.out.println(customer);		
	}
	
	@Test
	public void testFindByVerificationCode() {
		String code = "code_123";
		Customer customer = repo.findByVerificationCode(code);
		
		assertThat(customer).isNull();
	}
	
	@Test
	public void testEnableCustomer() {
		Integer customerId = 1;
		repo.enable(customerId);
		
		Customer customer = repo.findById(customerId).get();
		assertThat(customer.isEnabled()).isTrue();
	}
}
