package com.scit45.kiminomenyuwa.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	//로그인 없이 접근 가능 경로
	private static final String[] PUBLIC_URLS = {
		"/"               //TODO 작업을 위해 HOME 화면 아래 모든 페이지 접근 허가 중
		// 프로젝트 완성시 접근허가 경로 지정해야함
		, "/user/joinForm"
		, "/user/idCheck"
		, "/images/**"
		, "/user/join"
		, "/css/**"
		, "/js/**"
		, "/user/uploadProfile"
		, "/files/**"
		, "/user/**"
		, "/get-random-menu"
		, "/**"
	};

	@Bean
	protected SecurityFilterChain config(HttpSecurity http) throws Exception {
		http
			//요청에 대한 권한 설정
			.authorizeHttpRequests(author -> author
				.requestMatchers(PUBLIC_URLS).permitAll()   //모두 접근 허용
				.anyRequest().authenticated()               //그 외의 모든 요청은 인증 필요
			)
			//HTTP Basic 인증을 사용하도록 설정
			.httpBasic(Customizer.withDefaults())
			//폼 로그인 설정
			.formLogin(formLogin -> formLogin
				.loginPage("/user/loginForm")         //로그인폼 페이지 경로
				.usernameParameter("id")                //폼의 ID 파라미터 이름(name="")
				.passwordParameter("password")          //폼의 비밀번호 파라미터 이름(name="")
				.loginProcessingUrl("/user/login")    //로그인폼 제출하여 처리할 경로
				.defaultSuccessUrl("/")                 //로그인 성공 시 이동할 경로
				.permitAll()                            //로그인 페이지는 모두 접근 허용
			)
			//로그아웃 설정
			.logout(logout -> logout
				.logoutUrl("/user/logout")            //로그아웃 처리 경로
				.logoutSuccessUrl("/")                  //로그아웃 성공 시 이동할 경로
			);

		http
			.cors(AbstractHttpConfigurer::disable)
			.csrf(AbstractHttpConfigurer::disable);

		return http.build();
	}

	//비밀번호 암호화를 위한 인코더를 빈으로 등록
	@Bean
	public BCryptPasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
