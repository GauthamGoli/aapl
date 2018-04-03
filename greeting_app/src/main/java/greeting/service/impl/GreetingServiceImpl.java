package greeting.service.impl;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import greeting.dtos.GreetingDTO;
import greeting.service.GreetingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GreetingServiceImpl implements GreetingService {

	private static final String greeting = "Greetings %s!";

	@Value("${secret.key}")
	private String secretKey;

	@Override
	public GreetingDTO decodeTokenToGreeting(String token) throws Exception{
		Algorithm algorithm = Algorithm.HMAC256(secretKey);
		JWTVerifier verifier = JWT.require(algorithm).build();
		DecodedJWT jwt = verifier.verify(token);
		Map<String, Claim> claims = jwt.getClaims();
		Claim claim = claims.get("username");
		String username = claim.asString();
		return new GreetingDTO(String.format(greeting, username));
	}
}