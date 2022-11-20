package kz.milairis.admin.setting;

import kz.milairis.common.entity.Setting;
import kz.milairis.common.entity.SettingCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SettingRepository extends CrudRepository<Setting, String> {
    public List<Setting> findByCategory(SettingCategory category);
}
