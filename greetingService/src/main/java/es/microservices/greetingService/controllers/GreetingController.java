package es.microservices.greetingService.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.microservices.greetingService.services.RestCallService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api
@RestController
@RequestMapping("/greeting")
public class GreetingController {

	private static final Logger logger = LoggerFactory.getLogger(GreetingController.class);
	
	@Value("${clock.time.service.url}")
	private String clockTimeServiceUrl;
	
	@Autowired
	private RestCallService restCallService;

	
	@RequestMapping(method=RequestMethod.GET, path="/hello")
	@ApiOperation(value="getTime", response=String.class)
	@ApiResponses(value={
			@ApiResponse(code=200, message="Sucess", response=String.class),
			@ApiResponse(code = 500, message = "Failure")
	})
	public String greeting(@RequestParam(name="name", required=false) String pName,
							@RequestParam(name="directCall", required=false, defaultValue ="false") Boolean pDirectCall) {
		// Name
		String name = (pName != null) ? pName : "unknown";
		
		// Get time (direct call)
		String localTime = null;
		
		if (pDirectCall) {
			// Direct call
			localTime = restCallService.getRESTCallDirect("http://localhost:9001/" + clockTimeServiceUrl);
		} else {
			// Ribbon balanced configuration
			localTime = restCallService.getRESTCallBalanced("http://clock-Service/" + clockTimeServiceUrl);
		}
		
		String response = "Hello " + name + ". The time is: " + localTime;
		logger.info(response);
		return response;
	}

}
