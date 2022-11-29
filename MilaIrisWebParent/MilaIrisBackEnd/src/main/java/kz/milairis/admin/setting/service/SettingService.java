package kz.milairis.admin.setting.service;

import kz.milairis.admin.setting.GeneralSettingBag;
import kz.milairis.admin.setting.repository.SettingRepository;
import kz.milairis.common.entity.setting.Setting;
import kz.milairis.common.entity.setting.SettingCategory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingService {

    private final SettingRepository repo;

    public SettingService(SettingRepository repo) {
        this.repo = repo;
    }

    public List<Setting> listAllSettings() {
        return (List<Setting>) repo.findAll();
    }

    public GeneralSettingBag getGeneralSettings() {
        List<Setting> settings = new ArrayList<>();

        List<Setting> generalSettings = repo.findByCategory(SettingCategory.GENERAL);
        List<Setting> currencySettings = repo.findByCategory(SettingCategory.CURRENCY);

        settings.addAll(generalSettings);
        settings.addAll(currencySettings);

        return new GeneralSettingBag(settings);
    }

    public void saveAll(Iterable<Setting> settings) {
        repo.saveAll(settings);
    }

    public List<Setting> getMailServerSettings() {
        return repo.findByCategory(SettingCategory.MAIL_SERVER);
    }

    public List<Setting> getMailTemplateSettings() {
        return repo.findByCategory(SettingCategory.MAIL_TEMPLATES);
    }
}
