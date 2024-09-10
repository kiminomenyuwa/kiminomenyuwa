package com.scit45.kiminomenyuwa.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.scit45.kiminomenyuwa.domain.entity.UserEntity;
import com.scit45.kiminomenyuwa.domain.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticatedUserDetailsService implements UserDetailsService {

	private final BCryptPasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 전달받은 아이디로 회원정보 DB에서 조회
		// 없으면 예외
		UserEntity userEntity = userRepository.findById(username)
			.orElseThrow(() -> new EntityNotFoundException("회원정보가 없습니다."));

		// 있으면 그 정보로 UserDetails 객체 생성하여 리턴
		AuthenticatedUser user = AuthenticatedUser.builder()
			.id(username)
			.password(userEntity.getPasswordHash())
			.name(userEntity.getName())
			.roleName(userEntity.getRole().toString()) // Enum 타입이기 때문에 toString() 메서드 사용
			.enabled(userEntity.getEnabled())
			.build();
		log.debug("인증정보 : {}", user);

		return user;
	}

}
