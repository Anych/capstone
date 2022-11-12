package kz.milairis.admin.brand;

import kz.milairis.common.entity.Brand;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {

    public Long countById(Integer id);
}
