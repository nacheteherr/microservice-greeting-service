package es.microservices.greetingService.services;

public interface RestCallService {

	public String getRESTCallDirect(String uri);
	
	public String getRESTCallBalanced(String uri);
	
}
