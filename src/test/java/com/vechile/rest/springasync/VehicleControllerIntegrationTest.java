package com.vechile.rest.springasync;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.async.DeferredResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.vehicle.rest.springasync.controller.VehicleController;
import com.vehicle.rest.springasync.domain.ApiRequest;
import com.vehicle.rest.springasync.domain.ApiResponse;
import com.vehicle.rest.springasync.service.VehicleService;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class VehicleControllerIntegrationTest {
	private static final ObjectMapper MAPPER = new ObjectMapper();

	@InjectMocks
	private VehicleService service;
	@InjectMocks
	private VehicleController vehicleController;

	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(WireMockConfiguration.options().dynamicPort());

	@Before
	public void setup() throws Exception {

		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(vehicleController).build();

	}

	@Test
	public void testWithValidData() throws Exception {
		ApiRequest request = new ApiRequest();
		request.setVin("1A4AABBC5KD501999");
		request.setYear(2019);
		request.setMake("FCA");
		request.setModel("RAM");
		request.setTransmissionType("MANUAL");
		MvcResult resultActions = mockMvc.perform(post("/vehicles/vehicle").contentType(MediaType.APPLICATION_JSON)
				.content(MAPPER.writeValueAsString(request))).andExpect(request().asyncStarted()).andReturn();

		mockMvc.perform(asyncDispatch(resultActions)).andDo(print()).andExpect(status().is2xxSuccessful())
				.andExpect(jsonPath("$.responses[0].status").value(200))
				.andExpect(jsonPath("$.responses[0].duration").isNumber())
				.andExpect(jsonPath("$.responses[0].body.celsius").value(25.0))
				.andExpect(jsonPath("$.responses[1].body.celsius").value(15.0))
				.andExpect(jsonPath("$.responses[2].body.celsius").value(30.0))
				.andExpect(jsonPath("$.responses[3].body.celsius").value(10.0))
				.andExpect(jsonPath("$.responses[4].body.lat").value(52.087515))
				.andExpect(jsonPath("$.responses[5].body.lat").value(51.5285578))
				.andExpect(jsonPath("$.responses[6].body.lat").value(-33.873061))
				.andExpect(jsonPath("$.responses[7].body.lat").value(52.372333))
				.andExpect(jsonPath("$.duration", Matchers.lessThan(750))); // Should be around 500ms.
	}

	@Test()
	public void testInvalidTramissionType() {
		ApiRequest request = new ApiRequest();
		request.setVin("1A4AABBC5KD501999");
		request.setYear(2019);
		request.setMake("FCA");
		request.setModel("RAM");
		request.setTransmissionType("MANUALAUTO");
		ApiResponse response = new ApiResponse();
		DeferredResult<ResponseEntity<ApiResponse>> result = vehicleController.vehicleDetails(request);
		assertEquals("sucess", result, response);
	}

	@Test(expected = NullPointerException.class)
	public void testNullpointerExcpetion() {
		ApiRequest request = new ApiRequest();
		ApiResponse response = new ApiResponse();
		DeferredResult<ResponseEntity<ApiResponse>> result = vehicleController.vehicleDetails(request);
		assertEquals("sucess", result, response);
	}

}