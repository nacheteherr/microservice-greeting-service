package es.microservices.greetingService.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import es.microservices.greetingService.configuration.ClockServiceConfiguration;
import es.microservices.greetingService.services.RestCallService;

@Service
@RibbonClient(name = "clock-service", configuration = ClockServiceConfiguration.class)
public class RestCallServiceImpl implements RestCallService {

	@LoadBalanced
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Autowired
    private RestTemplate restTemplate;
	
	
	@Override
	public String getRESTCallDirect(String uri) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(uri, String.class);		
	}

	@HystrixCommand(fallbackMethod = "fallbackGetRESTCallBalanced")
	@Override
	public String getRESTCallBalanced(String uri) {
		return restTemplate.getForObject(uri, String.class);
	}

	public String fallbackGetRESTCallBalanced(String uri) {
        return "Fallback: Error getting REST service";
    }
}
