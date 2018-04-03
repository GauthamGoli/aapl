package greeting.dtos;

public class GreetingDTO {
	private final String greeting;

	public GreetingDTO(String greeting) {
		this.greeting = greeting;
	}

	public String getGreeting() {
		return greeting;
	}
}