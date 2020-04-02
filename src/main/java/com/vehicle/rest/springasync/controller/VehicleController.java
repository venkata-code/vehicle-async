/**
* @author Venkata
* @date 02-Apr-2020
*/
/**
 * 
 */
package com.vehicle.rest.springasync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.vehicle.rest.springasync.domain.ApiRequest;
import com.vehicle.rest.springasync.domain.ApiResponse;
import com.vehicle.rest.springasync.domain.VehicleData;
import com.vehicle.rest.springasync.service.VehicleService;

/**
 * @author Venkata
 *
 */
@RestController("/vehicle-api/v1")
public class VehicleController {
	
	private final VehicleService service;

	@Autowired
	public VehicleController(final VehicleService service) {
		this.service = service;
	}

	@PostMapping("/vehicles/vehicle")
	@ResponseBody
	public DeferredResult<ResponseEntity<ApiResponse>> vehicleDetails(@RequestBody final ApiRequest request) {

		return service.execute(request);
		
	}

}
