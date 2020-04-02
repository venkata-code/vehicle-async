# vehicle-async
Asynchronous REST call's

`http://localhost:8080/vehicle-api/v1/vehicles/vehicle`
```
{
	"time": "2020-02-02T20:35:14.817"
}
```

When we now take a look in the console we should see two log lines similar to these:

The logger configuration only displays the time, thread name and the message for clarity. As you can see here both of these lines are created by the same thread.

So where do these lines come from? Let’s take a look at the controller to see where these requests are handled. The 'basic' request is mapped here:

### ResponseEntity implementation

A common approach in Spring REST API’s is to return ResponseEntity’s wrapping the result objects. This makes it easier to instead of a result return for example a 'not found' HTTP response. Other than this wrapper the implementation is very similar:

```
@PostMapping("/vehicles/vehicle")
	@ResponseBody
	public DeferredResult<ResponseEntity<ApiResponse>> vehicleDetails(@RequestBody final ApiRequest request) {
		return service.execute(request);
```


**IMPORTANT**

You still need to figure out what threading pattern fits your use case. Creating threads is relatively expensive and with an unbounded maximum your application server can run out of memory and crash.

This behavior, as well as the name, can be configured through a WebMvcConfigurer bean.

### DeferredResult implementation

So we can now more easily handle long running results. We can’t inform the executor that we are done. This is fine for simple results but in some situations we need more control. This is where the DeferredResult class comes in. DeferredResult is a Future that allows us to signal completion. Let’s take a look at the controller method:

```
@PostMapping("/vehicles/vehicle")
	@ResponseBody
	public DeferredResult<ResponseEntity<ApiResponse>> vehicleDetails(@RequestBody final ApiRequest request) {
		return service.execute(request);
	}
```


### Testing asynchronous controllers

One thing to keep in mind when you are testing controllers with async calls through MockMvc that the async aspect requires you to slightly alter your tests. How to set up the tests is demonstrated in SimpleControllerIntegrationTest. is tested through MockMvc like so:

```
@Test()
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
}
```
2. tested with invalid data as transmissonType as "MANUALAUTO".

```
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
```

3. tested with null values.

```
@Test(expected = NullPointerException.class)
	public void testNullpointerExcpetion() {
		ApiRequest request = new ApiRequest();
		ApiResponse response = new ApiResponse();
		DeferredResult<ResponseEntity<ApiResponse>> result = vehicleController.vehicleDetails(request);
		assertEquals("sucess", result, response);
	}
```

### A real example

If you have checked out the source you might also notice that we have another controller. This is based on a real life example where I used DeferredResults to aggregate results from a number of different REST API’s into a single result. It uses OkHttp’s Async capabilities to perform multiple requests in parallel.

An example of such a request would be:

```
POST http://localhost:8080/vehicle-api/v1/vehicles/vehicle

{
  "vin": "1A4AABBC5KD501999",
  "year": 2019,
  "make": "FCA",
  "model": "RAM"
  "transmissionType": "MANUAL",
}
```

The underlying service calls these endpoints and combines them into a single JSON result:

```
{
      "id": 046b6c7f,
      "status": 200
}
```

What is in my opinion really cool is that it’s as fast as the slowest API call demonstrating that such an asynchronous call brings benefits over doing the calls in sequence.

It also comes with an integration test that uses WireMock to create service stubs that are then called in parallel.

### Conclusion

Springboot makes it incredibly easy to handle long running processes from our controller. We can return with almost zero effort when we want to let spring handle the threading or we can used DeferredResults when we need to be in full control.

I hope you enjoyed this post as much as I enjoyed writing it. Feel free to play around with the example and please let me know if you have comments or questions!





