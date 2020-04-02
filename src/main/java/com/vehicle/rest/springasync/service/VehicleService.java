package com.vehicle.rest.springasync.service;

import java.rmi.ServerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vehicle.rest.springasync.domain.ApiRequest;
import com.vehicle.rest.springasync.domain.ApiResponse;
import com.vehicle.rest.springasync.domain.VehicleData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VehicleService {
	private static final Logger log = LoggerFactory.getLogger(VehicleService.class);

	@Autowired
	private DBServiceA dbServiceA;
	@Autowired
	private HttpServiceB httpServiceB;

	@Async
	public DeferredResult<ResponseEntity<ApiResponse>> execute(final ApiRequest request) {
		log.info("Started task with {} request", request);
		ApiResponse responseEntity = new ApiResponse();
		DeferredResult<ResponseEntity<ApiResponse>> response = new DeferredResult<ResponseEntity<ApiResponse>>();
	
		try {
			try {
				validation(request);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ObjectMapper Obj = new ObjectMapper();
			String jsonStr = Obj.writeValueAsString(request);
			VehicleData vechileData = new VehicleData();
			vechileData.setId(request.getId());
			vechileData.setYear(request.getYear());
			vechileData.setVin(request.getVin());
			vechileData.setMake(request.getMake());
	        vechileData.setModel(request.getModel());
	        vechileData.setTransmissionType(request.getTransmissionType());
			dbServiceA.SaveData(vechileData);
			
			// Displaying JSON String
			System.out.println("JSON Request" + jsonStr);
			String dbResult = dbServiceA.getData(request.getId().getMostSignificantBits());

			String httpResponse = httpServiceB.sendMessage(request.getVin()).toString();
			responseEntity.setBody(dbResult);
			responseEntity.setId(request.getId());
			responseEntity.setStatus(httpResponse);
			String jsonResponse = Obj.writeValueAsString(responseEntity);

			// Displaying JSON String
			System.out.println("JSON Response" + jsonResponse);
			log.info("ended task with {} request", jsonResponse);
			ResponseEntity<ApiResponse> apiresponseEntity=new ResponseEntity<ApiResponse>(responseEntity, HttpStatus.FOUND);
			response.setResult(apiresponseEntity);
		} catch (InterruptedException | JsonProcessingException |ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	private void validation(ApiRequest request) throws Exception {
		if (!request.getTransmissionType().equals("MANUAL") || !request.getTransmissionType().equals("AUTO")) {
            throw new Exception ("Invalid TranmissionTYpe");
		}

	}
}