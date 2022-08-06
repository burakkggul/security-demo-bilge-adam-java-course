package tr.com.burakgul.securitydemo.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import tr.com.burakgul.securitydemo.auth.TokenManager;
import tr.com.burakgul.securitydemo.model.dto.LoginDTO;
import tr.com.burakgul.securitydemo.model.dto.UserDTO;
import tr.com.burakgul.securitydemo.model.entity.User;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;

    private final TokenManager tokenManager;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserDetailService userDetailService;

    private final ModelMapper modelMapper;

    public HttpHeaders login(LoginDTO loginDTO) {
        if (loginDTO != null && loginDTO.getUsername() != null && loginDTO.getPassword() != null) {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer "+this.tokenManager.generateToken(loginDTO.getUsername()));
            return headers;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hatalı giriş isteği.");
        }
    }

    public UserDTO signUp(@RequestBody UserDTO userDTO){
        try {
            User user = this.modelMapper.map(userDTO,User.class);
            user.setPassword(this.bCryptPasswordEncoder.encode(userDTO.getPassword()));
            UserDTO savedUserDto = userDetailService.save(user);
            savedUserDto.setPassword(null);
            return savedUserDto;
        }catch (Exception e){
            this.LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }
}
