package kz.milairis.admin.brand;

import kz.milairis.admin.brand.repository.BrandRepository;
import kz.milairis.common.entity.Brand;
import kz.milairis.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class BrandRepositoryTests {
	
	@Autowired
	private BrandRepository repo;
	
	@Test
	public void testCreateBrand1() {
		Category laptops = new Category(6);
		Brand asus = new Brand("Asus1");
		asus.getCategories().add(laptops);
		
		Brand savedBrand = repo.save(asus);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateBrand2() {
		Category cellphones = new Category(4);
		Category tablets = new Category(7);
		
		Brand apple = new Brand("Apple1");
		apple.getCategories().add(cellphones);
		apple.getCategories().add(tablets);
		
		Brand savedBrand = repo.save(apple);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateBrand3() {
		Brand samsung = new Brand("Samsung1");
		
		samsung.getCategories().add(new Category(29));	// category memory
		samsung.getCategories().add(new Category(24));	// category internal hard drive
		
		Brand savedBrand = repo.save(samsung);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testFindAll() {
		Iterable<Brand> brands = repo.findAll();
		brands.forEach(System.out::println);
		
		assertThat(brands).isNotEmpty();
	}
	
	@Test
	public void testGetById() {
		Brand brand = repo.findById(1).get();
		
		assertThat(brand.getName()).isEqualTo("Canon");
	}
	
	@Test
	public void testUpdateName() {
		String newName = "Samsung Electronics";
		Brand samsung = repo.findById(2).get();
		samsung.setName(newName);
		
		Brand savedBrand = repo.save(samsung);
		assertThat(savedBrand.getName()).isEqualTo(newName);
	}
	
	@Test
	public void testDelete() {
		Integer id = 2;
		repo.deleteById(id);
		
		Optional<Brand> result = repo.findById(id);
		
		assertThat(result.isEmpty());
	}
}
