package br.com.erudio.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;

import br.com.erudio.security.jwt.JwtTokenProvider;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenProvider tokenProvider;
	
	public SecurityConfig(JwtTokenProvider tokenProvider, PasswordEncoder passwordEncoder) {
		this.tokenProvider = tokenProvider;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		/*Explicando o Pbkdf2PasswordEncoder("", 8, 185000, 
		 *		Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256):
		 * "" - Salt vazio - gera automaticamente
		 * 8 - comprimento da chave (8 bytes)
		 * 185000 - número de vezes que o algoritmo será aplicado
		 * Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256 - Algoritmo de hash utilizado
		 */
		PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder("", 8, 185000, 
				Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
		
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put("pbkdf2", pbkdf2Encoder);
		DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
		
		passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
		return passwordEncoder;
	}

	
}
