package br.com.erudio.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.erudio.data.dto.security.AccountCredentialsDTO;
import br.com.erudio.data.dto.security.TokenDTO;
import br.com.erudio.repository.UserRepository;
import br.com.erudio.security.jwt.JwtTokenProvider;

@Service
public class AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private UserRepository repository;
	
	public ResponseEntity<TokenDTO> signIn(AccountCredentialsDTO credentials){
		//autentica
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));
		
		//recupera o usuário do BD
		var user = repository.findByUsername(credentials.getUsername());
		if(user == null) {
			throw new UsernameNotFoundException("Username " + credentials.getUsername() + " not found!");
		}
		
		//gerar o token de acesso
		var token = tokenProvider.createAccessToken(credentials.getUsername(), user.getRoles());

		return ResponseEntity.ok(token);
	}
	
	public ResponseEntity<TokenDTO> signIn(String username, String refreshToken){
		//recupera o usuário do BD
		var user = repository.findByUsername(username);
		TokenDTO token;
		if(user != null) {
			token = tokenProvider.refreshToken(refreshToken);
		}else {
			throw new UsernameNotFoundException("Username " + username + " not found!");
		}
		return ResponseEntity.ok(token);
	}
	
	public ResponseEntity<TokenDTO> refreshToken(String username, String refreshToken){
		//recupera o usuário do BD
		var user = repository.findByUsername(username);
		TokenDTO token;
		if(user != null) {
			token = tokenProvider.refreshToken(refreshToken);
		}else {
			throw new UsernameNotFoundException("Username " + username + " not found!");
		}
		return ResponseEntity.ok(token);
	}
	
}
