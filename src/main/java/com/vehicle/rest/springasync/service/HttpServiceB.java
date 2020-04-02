/**
* @author Venkata
* @date 03-Apr-2020
*/
/**
 * 
 */
package com.vehicle.rest.springasync.service;

import java.rmi.ServerException;
import java.util.concurrent.CompletableFuture;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Venkata
 *
 */
@Service
public class HttpServiceB {
	private static final Logger log = LoggerFactory.getLogger(HttpServiceB.class);
	
	private final RestTemplate restTemplate;
	 
    public HttpServiceB(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }
    @Async
	public CompletableFuture<String> sendMessage(String message) throws InterruptedException, ServerException {
		log.debug(message);

		String url = String.format("https://api.github.com/vechile", message);
		HttpResponse results = restTemplate.getForObject(url, HttpResponse.class);
		// Artificial delay of 1s for demonstration purposes
		Thread.sleep(1000L);
		if (results == null) {
			throw new ServerException("Internal Server Exception");
		}
		String httpResponse = Integer.toString(results.getStatusLine().getStatusCode());

		log.debug(httpResponse);
		return CompletableFuture.completedFuture(httpResponse);

	}

}
