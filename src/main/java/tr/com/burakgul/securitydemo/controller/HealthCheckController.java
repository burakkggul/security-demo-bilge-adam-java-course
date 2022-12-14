package tr.com.burakgul.securitydemo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthCheckController {

    @GetMapping("/healthCheck")
    public ResponseEntity<String> healthCheck(){
        return ResponseEntity.ok("OK");
    }
}
