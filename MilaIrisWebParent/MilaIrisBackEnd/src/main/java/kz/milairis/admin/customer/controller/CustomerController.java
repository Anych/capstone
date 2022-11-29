package kz.milairis.admin.customer.controller;

import kz.milairis.admin.customer.service.CustomerService;
import kz.milairis.admin.paging.PagingAndSortingHelper;
import kz.milairis.admin.paging.PagingAndSortingParam;
import kz.milairis.common.entity.Country;
import kz.milairis.common.entity.user.Customer;
import kz.milairis.common.exception.CustomerNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerController {
	private final String defaultRedirectURL = "redirect:/customers/page/1?sortField=firstName&sortDir=asc";

	private final CustomerService service;

	public CustomerController(CustomerService service) {
		this.service = service;
	}

	@GetMapping("")
	public String listFirstPage() {
		return defaultRedirectURL;
	}

	@GetMapping("/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listCustomers", moduleURL = "/customers") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum) {

		service.listByPage(pageNum, helper);

		return "customers/customers";
	}

	@GetMapping("/{id}/enabled/{status}")
	public String updateCustomerEnabledStatus(@PathVariable("id") Integer id,
											  @PathVariable("status") boolean enabled,
											  RedirectAttributes redirectAttributes) {
		service.updateCustomerEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The Customer ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);

		return defaultRedirectURL;
	}

	@GetMapping("/detail/{id}")
	public String viewCustomer(@PathVariable("id") Integer id, Model model,
							   RedirectAttributes redirectAttributes) {
		try {
			Customer customer = service.get(id);
			model.addAttribute("customer", customer);

			return "customers/customer_detail_modal";
		} catch (CustomerNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return defaultRedirectURL;
		}
	}

	@GetMapping("/edit/{id}")
	public String editCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		try {
			Customer customer = service.get(id);
			List<Country> countries = service.listAllCountries();

			model.addAttribute("listCountries", countries);
			model.addAttribute("customer", customer);
			model.addAttribute("pageTitle", String.format("Edit Customer (ID: %d)", id));

			return "customers/customer_form";

		} catch (CustomerNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return defaultRedirectURL;
		}
	}

	@PostMapping("/save")
	public String saveCustomer(Customer customer, RedirectAttributes redirectAttributes)
			throws CustomerNotFoundException {
		service.save(customer);
		redirectAttributes.addFlashAttribute(
				"message", "The customer ID " + customer.getId() + " has been updated successfully.");
		return defaultRedirectURL;
	}

	@GetMapping("/delete/{id}")
	public String deleteCustomer(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);
			redirectAttributes.addFlashAttribute(
					"message", "The customer ID " + id + " has been deleted successfully.");

		} catch (CustomerNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}

		return defaultRedirectURL;
	}
}
