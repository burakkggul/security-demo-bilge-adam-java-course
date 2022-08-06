package tr.com.burakgul.securitydemo.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tr.com.burakgul.securitydemo.model.dto.UserDTO;
import tr.com.burakgul.securitydemo.model.entity.User;
import tr.com.burakgul.securitydemo.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Kullanıcı adı bulunamadı."));
    }

    public UserDTO save(User user) {
        User savedUser = this.userRepository.save(user);
        return this.modelMapper.map(savedUser,UserDTO.class);
    }
}
