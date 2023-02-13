package stackoverflowapp.controller;

import stackoverflowapp.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/required")
    public String getRequiredUsers() {
        userService.syncExternalUsers();
        userService.receiveRequiredUsers();
        return "List of required users received in console!";
    }
}
