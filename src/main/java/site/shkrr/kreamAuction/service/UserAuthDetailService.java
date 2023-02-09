package site.shkrr.kreamAuction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import site.shkrr.kreamAuction.domain.user.UserRepository;
import site.shkrr.kreamAuction.exception.user.AuthLoadUserNotFoundException;
@RequiredArgsConstructor
@Service
public class UserAuthDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userRepository.findById(Long.parseLong(userId)).orElseThrow(()->new AuthLoadUserNotFoundException("인증 과정중 사용자를 찾을 수 없습니다."));
    }
}
