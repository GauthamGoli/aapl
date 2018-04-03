package greeting.service;

import greeting.dtos.GreetingDTO;

public interface GreetingService {
	GreetingDTO decodeTokenToGreeting(String token) throws Exception;
}