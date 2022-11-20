package kz.milairis.admin.setting;

import kz.milairis.common.entity.Setting;
import org.springframework.data.repository.CrudRepository;

public interface SettingRepository extends CrudRepository<Setting, String> {
}
