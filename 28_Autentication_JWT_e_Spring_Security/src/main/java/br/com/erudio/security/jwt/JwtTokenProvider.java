package br.com.erudio.security.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.erudio.data.dto.security.TokenDTO;
import br.com.erudio.exception.InvalidJwtAuthenticationException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtTokenProvider {

	@Value("${security.jwt.token.secret-key:secret}")
	private String secretkey = "secret";
	
	@Value("${security.jwt.token.expire-lenght:3600000}")
	private long validiityInMilliseconds = 3600000; // 1h
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	Algorithm algorithm = null;
	
	@PostConstruct
	protected void init() {
		secretkey= Base64.getEncoder().encodeToString(secretkey.getBytes());
		algorithm = Algorithm.HMAC256(secretkey.getBytes());
	}
	
	public TokenDTO createAccessToken(String username, List<String> roles) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + validiityInMilliseconds);
		String accessToken = getAccessToken(username, roles, now, validity);
		String refreshToken = getRefreshToken(username, roles, now);
		return new TokenDTO(username, true, now, validity, accessToken, refreshToken);
	}

	//carrega a URL do site(sistema) até a porta 8080(por exemplo) - token com 3h de validade
	private String getRefreshToken(String username, List<String> roles, Date now) {
		Date refreshTokenValidity = new Date(now.getTime() + validiityInMilliseconds * 3);
		return JWT.create()
				.withClaim("roles", roles)
				.withIssuedAt(now)
				.withExpiresAt(refreshTokenValidity)
				.withSubject(username)
				.sign(algorithm);
	}

	private String getAccessToken(String username, List<String> roles, Date now, Date validity) {
		String issueUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
		return JWT.create()
				.withClaim("roles", roles)
				.withIssuedAt(now)
				.withExpiresAt(validity)
				.withSubject(username)
				.withIssuer(issueUrl)
				.sign(algorithm);
	}
	
	public Authentication getAuthentication(String token) {
		DecodedJWT decodedJWT = decodedToken(token);
		UserDetails userDetails = this.userDetailsService
				.loadUserByUsername(decodedJWT.getSubject());
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	private DecodedJWT decodedToken(String token) {
		Algorithm alg = Algorithm.HMAC256(secretkey.getBytes());
		JWTVerifier verifier = JWT.require(alg).build();
		DecodedJWT decodedJWT = verifier.verify(token);
		return decodedJWT;
	}
	
	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		
		if(StringUtils.isEmpty(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring("Bearer ".length());
		}else {
			throw new InvalidJwtAuthenticationException("Invalid JWT Token");
		}
	}
	
	public boolean validateToken(String token) {
		DecodedJWT decodedJWT = decodedToken(token);
		try {
			if(decodedJWT.getExpiresAt().before(new Date())) {
				return false;
			}
			return true;
		} catch (Exception e) {
			throw new InvalidJwtAuthenticationException("Expired or Invalid JWT Token");
		}
		
	}
	
}
