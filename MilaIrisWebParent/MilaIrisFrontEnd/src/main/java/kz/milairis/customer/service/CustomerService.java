package kz.milairis.customer.service;

import kz.milairis.common.entity.Country;
import kz.milairis.common.entity.user.Customer;
import kz.milairis.customer.repository.CustomerRepository;
import kz.milairis.setting.repository.CountryRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CustomerService {
	private final CountryRepository countryRepo;
	private final CustomerRepository customerRepo;
	final
	PasswordEncoder passwordEncoder;

	public CustomerService(CountryRepository countryRepo, CustomerRepository customerRepo,
						   PasswordEncoder passwordEncoder) {
		this.countryRepo = countryRepo;
		this.customerRepo = customerRepo;
		this.passwordEncoder = passwordEncoder;
	}

	public List<Country> listAllCountries() {
		return countryRepo.findAllByOrderByNameAsc();
	}

	public boolean isEmailUnique(String email) {
		Customer customer = customerRepo.findByEmail(email);
		return customer == null;
	}

	public void registerCustomer(Customer customer) {
		encodePassword(customer);
		customer.setEnabled(false);
		customer.setCreatedTime(new Date());

		String randomCode = RandomString.make(64);
		customer.setVerificationCode(randomCode);

		customerRepo.save(customer);
	}

	private void encodePassword(Customer customer) {
		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);
	}

	public boolean verify(String verificationCode) {
		Customer customer = customerRepo.findByVerificationCode(verificationCode);

		if (customer == null || customer.isEnabled()) {
			return false;
		} else {
			customerRepo.enable(customer.getId());
			return true;
		}
	}
}
