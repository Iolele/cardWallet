package no.cardwallet.card.User;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {

    final
    UserRepository userRepository;

    public UserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User appUser = userRepository.findByEmail(email);
        if (appUser == null) {
            throw new UsernameNotFoundException(email);
        }
        return new UserDetail(appUser);
    }
}
