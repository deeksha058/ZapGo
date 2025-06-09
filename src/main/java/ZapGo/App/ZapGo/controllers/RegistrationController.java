package ZapGo.App.ZapGo.controllers;

import ZapGo.App.ZapGo.models.LoginRequest;
import ZapGo.App.ZapGo.models.RegistrationRequest;
import ZapGo.App.ZapGo.services.LoginService;
import ZapGo.App.ZapGo.services.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/public/registration")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final LoginService loginService;

    @PostMapping("")
    public String register(@RequestBody RegistrationRequest registerRequest) {

        return registrationService.register(registerRequest);
    }

    @GetMapping(path = "/confirm")
    public String confirm(@RequestParam("token") String token) {

        return registrationService.confirmToken(token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String response = loginService.loginUser(request);
        return ResponseEntity.ok(response);
    }
}