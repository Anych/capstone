package kz.milairis.admin.user.controller;

import kz.milairis.admin.FileUploadUtil;
import kz.milairis.admin.security.userdetail.MilaIrisUserDetails;
import kz.milairis.admin.user.UserService;
import kz.milairis.common.entity.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final UserService service;

    public AccountController(UserService service) {
        this.service = service;
    }

    @GetMapping("")
    public String viewDetails(@AuthenticationPrincipal MilaIrisUserDetails loggedUser,
                              Model model) {
        String email = loggedUser.getUsername();
        User user = service.getByEmail(email);
        model.addAttribute("user", user);

        return "users/account_form";

    }

    @PostMapping("/update")
    public String saveDetails(User user, RedirectAttributes redirectAttributes,
                              @AuthenticationPrincipal MilaIrisUserDetails loggedUser,
                              @RequestParam("image") MultipartFile multipartFile) throws IOException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            user.setPhotos(fileName);
            User savedUser = service.updateAccount(user);

            String uploadDir = "user-photos/" + savedUser.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        } else {
            if (user.getPhotos().isEmpty()) user.setPhotos(null);
            service.updateAccount(user);
        }

        loggedUser.setFirstName(user.getFirstName());
        loggedUser.setLastName(user.getLastName());

        redirectAttributes.addFlashAttribute("message", "Your account details have been updated.");

        return "redirect:/account";
    }
}
