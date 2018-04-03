package greeting;

import greeting.service.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.io.UnsupportedEncodingException;

@RestController
public class GreetingController {

	@Autowired
	private GreetingService greetingService;

	@RequestMapping("/")
	public ResponseEntity<?> index(@RequestParam(value="token") String token) throws Exception{
		try {
			return new ResponseEntity<>(greetingService.decodeTokenToGreeting(token),
					HttpStatus.OK);
		} catch (UnsupportedEncodingException exception) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		} catch (JWTVerificationException exception) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}