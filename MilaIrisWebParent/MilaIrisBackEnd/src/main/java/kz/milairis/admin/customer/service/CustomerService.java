package kz.milairis.admin.customer.service;

import kz.milairis.admin.customer.repository.CustomerRepository;
import kz.milairis.admin.paging.PagingAndSortingHelper;
import kz.milairis.admin.setting.country.CountryRepository;
import kz.milairis.common.entity.Country;
import kz.milairis.common.entity.user.Customer;
import kz.milairis.common.exception.CustomerNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class CustomerService {
	public static final int CUSTOMERS_PER_PAGE = 10;

	private final CustomerRepository customerRepo;
	private final CountryRepository countryRepo;
	private final PasswordEncoder passwordEncoder;

	public CustomerService(CustomerRepository customerRepo, CountryRepository countryRepo,
						   PasswordEncoder passwordEncoder) {
		this.customerRepo = customerRepo;
		this.countryRepo = countryRepo;
		this.passwordEncoder = passwordEncoder;
	}

	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, CUSTOMERS_PER_PAGE, customerRepo);
	}

	public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
		customerRepo.updateEnabledStatus(id, enabled);
	}

	public Customer get(Integer id) throws CustomerNotFoundException {
		return customerRepo.findById(id).orElseThrow(
				() -> new CustomerNotFoundException("Could not find any customers with ID " + id)
		);
	}

	public List<Country> listAllCountries() {
		return countryRepo.findAllByOrderByNameAsc();
	}

	public boolean isEmailUnique(Integer id, String email) {
		Customer existCustomer = customerRepo.findByEmail(email);

		return existCustomer == null || Objects.equals(existCustomer.getId(), id);
	}

	public void save(Customer customerInForm) throws CustomerNotFoundException {
		Customer customerInDB = customerRepo.findById(customerInForm.getId()).orElseThrow(
				() -> new CustomerNotFoundException("Could not find any customers with")
		);

		if (!customerInForm.getPassword().isEmpty()) {
			String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
			customerInForm.setPassword(encodedPassword);
		} else {
			customerInForm.setPassword(Objects.requireNonNull(customerInDB).getPassword());
		}

		customerInForm.setEnabled(Objects.requireNonNull(customerInDB).isEnabled());
		customerInForm.setCreatedTime(customerInDB.getCreatedTime());
		customerInForm.setVerificationCode(customerInDB.getVerificationCode());

		customerRepo.save(customerInForm);
	}

	public void delete(Integer id) throws CustomerNotFoundException {
		Long count = customerRepo.countById(id);
		if (count == null || count == 0) {
			throw new CustomerNotFoundException("Could not find any customers with ID " + id);
		}

		customerRepo.deleteById(id);
	}
}
