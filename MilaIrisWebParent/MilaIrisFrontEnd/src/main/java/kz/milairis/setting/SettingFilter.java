package kz.milairis.setting;

import kz.milairis.common.entity.setting.Setting;
import kz.milairis.setting.service.SettingService;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Component
public class SettingFilter implements Filter {

    private final SettingService service;

    public SettingFilter(SettingService service) {
        this.service = service;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String url = servletRequest.getRequestURL().toString();

        if (url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".png") ||
                url.endsWith(".jpg")) {
            chain.doFilter(request, response);
            return;
        }

        List<Setting> generalSettings = service.getGeneralSettings();

        generalSettings.forEach(setting -> request.setAttribute(setting.getKey(), setting.getValue()));

        chain.doFilter(request, response);

    }

}

