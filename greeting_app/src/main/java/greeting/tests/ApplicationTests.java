package greeting.tests;

import com.auth0.jwt.exceptions.JWTVerificationException;
import greeting.GreetingController;
import greeting.dtos.GreetingDTO;
import greeting.service.GreetingService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@RunWith(SpringRunner.class)
@WebMvcTest(value = GreetingController.class, secure = false)
@TestPropertySource(properties="secret.key=secret")
public class ApplicationTests {

	private static final String validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImdhdXRoYW0ifQ.0A2LwjJkxNPVy1CF1ovlPCOPH7JBn79dgijHhS71FPs";
	private static final String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MjI3OTQ2NDF9.2pbCSUEvfZFmXU94WH8eTItRi0UtzhAq8XRUMpSBGdY";
	private static final String validResponse = "{\"greeting\": \"Greetings gautham!\"}";
	private static final String validGreeting = "Greetings gautham!";

	@Autowired
	private GreetingService greetingService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void shouldReturnValidResponseWhenSendingValidToken() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/?token="+validToken).accept(
				MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		JSONAssert.assertEquals(validResponse,
				result.getResponse().getContentAsString(), false);
	}

	@Test
	public void greetingServiceValidToken() throws Exception {
		GreetingDTO greetingDTO = this.greetingService
				.decodeTokenToGreeting(validToken);
		Assert.assertEquals(greetingDTO.getGreeting(), validGreeting);
	}

	@Test(expected = JWTVerificationException.class)
	public void greetingServiceExpiredToken() throws Exception {
		this.greetingService.decodeTokenToGreeting(invalidToken);
	}
}