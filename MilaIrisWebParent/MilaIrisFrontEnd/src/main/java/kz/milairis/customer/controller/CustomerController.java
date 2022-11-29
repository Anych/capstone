package kz.milairis.customer.controller;

import kz.milairis.Utility;
import kz.milairis.common.entity.Country;
import kz.milairis.common.entity.user.Customer;
import kz.milairis.customer.service.CustomerService;
import kz.milairis.setting.EmailSettingBag;
import kz.milairis.setting.service.SettingService;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class CustomerController {
	private final CustomerService customerService;
	private final SettingService settingService;

	public CustomerController(CustomerService customerService, SettingService settingService) {
		this.customerService = customerService;
		this.settingService = settingService;
	}

	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		List<Country> listCountries = customerService.listAllCountries();

		model.addAttribute("listCountries", listCountries);
		model.addAttribute("pageTitle", "Customer Registration");
		model.addAttribute("customer", new Customer());

		return "register/register_form";
	}

	@PostMapping("/create_customer")
	public String createCustomer(Customer customer, Model model,
								 HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		customerService.registerCustomer(customer);
		sendVerificationEmail(request, customer);

		model.addAttribute("pageTitle", "Registration Succeeded!");

		return "/register/register_success";
	}

	private void sendVerificationEmail(HttpServletRequest request, Customer customer)
			throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

		String toAddress = customer.getEmail();
		String subject = emailSettings.getCustomerVerifySubject();
		String content = emailSettings.getCustomerVerifyContent();

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);

		content = content.replace("[[name]]", customer.getFullName());

		String verifyURL = Utility.getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();

		content = content.replace("[[URL]]", verifyURL);

		helper.setText(content, true);

		mailSender.send(message);
	}

	@GetMapping("/verify")
	public String verifyAccount(@Param("code") String code) {
		boolean verified = customerService.verify(code);

		if (verified) {
			return "register/verify_success";
		} else {
			return "register/verify_fail";
		}
	}
}
