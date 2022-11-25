package kz.milairis.admin.product;

import kz.milairis.common.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @MockBean
    private ProductRepository repo;

    @InjectMocks
    private ProductService service;

    @Test
    public void testCheckUniqueInNewModeReturnDuplicate() {
        Integer id = null;
        String name = "Acer Swift 3 Thin & Light Laptop";

        Product product = new Product(id, name);

        Mockito.when(repo.findByName(name)).thenReturn(product);

        String result = service.checkUnique(id, name);

        assertThat(result).isEqualTo("Duplicate");
    }

    @Test
    public void testCheckUniqueInNewModeReturnOK() {
        Integer id = null;
        String name = "Some String That can be Name of Product";

        Product product = new Product(id, name);

        Mockito.when(repo.findByName(name)).thenReturn(null);

        String result = service.checkUnique(id, name);

        assertThat(result).isEqualTo("OK");
    }
}
