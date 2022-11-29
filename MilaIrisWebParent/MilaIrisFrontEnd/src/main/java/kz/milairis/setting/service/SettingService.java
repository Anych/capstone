package kz.milairis.setting.service;

import kz.milairis.common.entity.setting.Setting;
import kz.milairis.common.entity.setting.SettingCategory;
import kz.milairis.setting.EmailSettingBag;
import kz.milairis.setting.repository.SettingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {
    private final SettingRepository repo;

    public SettingService(SettingRepository repo) {
        this.repo = repo;
    }

    public List<Setting> getGeneralSettings() {
        return repo.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
    }

    public EmailSettingBag getEmailSettings() {
        List<Setting> settings = repo.findByCategory(SettingCategory.MAIL_SERVER);
        settings.addAll(repo.findByCategory(SettingCategory.MAIL_TEMPLATES));

        return new EmailSettingBag(settings);
    }
}
