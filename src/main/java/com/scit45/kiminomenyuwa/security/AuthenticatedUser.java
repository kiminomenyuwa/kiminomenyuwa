package com.scit45.kiminomenyuwa.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticatedUser implements UserDetails {

	private static final long serialVersionUID = 9565021436L;
	
	String id;			// 필수
	String password;	// 필수
	String roleName;	// 필수
	boolean enabled; 	// 멀정한 id인지 확인하는 변수
	String name;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// "ROLE_USER", "ROLE_ADMIN", "ROLE_MERCHANT"
		return Collections.singletonList(new SimpleGrantedAuthority(roleName));
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() { // Username이 id임에 주목!!!
		return id;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
}
