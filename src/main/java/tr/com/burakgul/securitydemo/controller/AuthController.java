package tr.com.burakgul.securitydemo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.burakgul.securitydemo.model.dto.LoginDTO;
import tr.com.burakgul.securitydemo.model.dto.UserDTO;
import tr.com.burakgul.securitydemo.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO){
        return ResponseEntity.ok().headers(this.authService.login(loginDTO)).body("Login success");
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody UserDTO userDTO){
        return ResponseEntity.ok(this.authService.signUp(userDTO));
    }
}
