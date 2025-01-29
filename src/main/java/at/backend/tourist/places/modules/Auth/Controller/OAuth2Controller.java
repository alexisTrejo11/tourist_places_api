package at.backend.tourist.places.modules.Auth.Controller;

import at.backend.tourist.places.modules.User.Model.User;
import at.backend.tourist.places.modules.User.Repository.UserRepository;
import at.backend.tourist.places.core.Utils.Response.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class OAuth2Controller {

    @Autowired
    private UserRepository userService;

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute User user) {
        user.setProvider("local");
        userService.save(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/error")
    public Result<String> error(HttpServletRequest request) {
        OAuth2AuthenticationException exception =
                (OAuth2AuthenticationException) request.getAttribute("org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationException");
        if (exception != null) {
            return Result.failure("OAuth2 Authentication failed: " + exception.getMessage());
        }
        return Result.failure("OAuth2 error occurred");
    }
}