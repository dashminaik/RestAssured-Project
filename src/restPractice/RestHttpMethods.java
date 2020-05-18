package restPractice;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;

import files.Payload;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.restassured.specification.RequestSpecification;

public class RestHttpMethods {	
	
	@Test
	public void getMethod() {
		
		RestAssured.baseURI = "http://restapi.demoqa.com";
		RestAssured.basePath = "/utilities/weather/city/Pune";
		
		Response response =  RestAssured.given().basePath(basePath).get().then()
							.statusCode(200).and().contentType(ContentType.JSON)
							.extract().response();
		
		System.out.println("Response:"+response.asString());
		System.out.println("Status Line:"+response.statusLine());
		System.out.println("Header:"+response.getHeaders());		
	}
	
	@Test
	public void getWeather() {
		
	//	RestAssured.baseURI = "http://restapi.demoqa.com";
	//	RestAssured.basePath = "/utilities/weather/city/Pune";
		
		RestAssured.baseURI="http://webservice.toscacloud.com";
		RestAssured.basePath="/training/api/Coffees/f90500b5-1280-4338-dab1-cf0a55c12dca";
		
		RequestSpecification request = RestAssured.given();
	/*	ValidatableResponse response = request.get().then().assertThat().statusCode(200).and()
										.contentType(ContentType.JSON);
										
		ValidatableResponse response = given().get().then().assertThat()
										.statusCode(200).and().contentType(ContentType.JSON);
		System.out.println(response.log().all());	
		*/
		
		Response response = given().spec(request).when().get();
		//System.out.println(response.contentType(ContentType.JSON));
		System.out.println("Response" +response.getBody().asString());
				
		JsonPath path = response.jsonPath();
		List<String> coffees = path.getList("Name");
			for (String coffee : coffees) {
				System.out.println(coffee);
			}		
	}
	
	@Test
	public void fetchCoffeeBasedOnIdPathParam() {
		
		int Id= 11;
		//String city = "Bangalore";
		RestAssured.baseURI="http://webservice.toscacloud.com";
		RestAssured.basePath="/training/api/Coffees";
		
		//RestAssured.baseURI = "https://reqres.in";
		//RestAssured.basePath = "/api/users";
				
		RequestSpecification request = RestAssured.given();
		request.pathParam("key", "77646baf-094f-20c5-71b7-b61223d9f8de");
		//request.param("Id", 2);    --- gives error
		request.pathParam("Id", Id);		
		
		//Response resp = given().spec(request).when().get("/{key}/{Id}");
		Response resp = given().spec(request).get("/{Id}").then().assertThat().statusCode(200).extract().response();	
				
		System.out.println("Response: " +resp.getBody().asString());
		/*System.out.println(resp.getContentType());
		System.out.println(resp.getSessionId());
		System.out.println(resp.getStatusCode());
		System.out.println(resp.statusCode());
		*/
	}
	
	@Test
	public void fetchUserBasedOnIdQueryParam() {
		
		RestAssured.baseURI="https://reqres.in";
		RestAssured.basePath="/api/users";
		
		RequestSpecification request = RestAssured.given();
		request.queryParam("page", 2);
		request.queryParam("id", 5);
		//request.param("page", 2);    // works fine
		//request.param("id", 4);
		
		
	/*	RequestSpecification request = RestAssured.given()
									  .queryParam("page", 2)
									  .queryParam("id", 4);
		*/
		Response  response = given().spec(request).when().get().then().assertThat().statusCode(200).extract().response();
		System.out.println(response.getHeaders());
		System.out.println("Response:"+response.getBody().asString());
							
	}
	
	@Test
	public void createCoffee() throws ParseException {
		
		RestAssured.baseURI = "http://webservice.toscacloud.com";
		RestAssured.basePath = "/training/api/Coffees/984c9c7f-1153-0881-b11c-9ce23c9bf341";
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("Id", "15");
		map.put("Name", "Indian Chai and snanks");
		map.put("Description", "Testing purpose snaks chai and coffee");
		
		//RequestSpecification request = RestAssured.given().accept(ContentType.JSON).body(map);
		//RequestSpecification request = RestAssured.given().header("Content-Type", "application/json").body(map);
		RequestSpecification request = RestAssured.given().header("Content-Type", "application/json").accept(ContentType.JSON);
		
		Response resp = given().spec(request).body(map).when().post().then().extract().response();
	//	Response resp = given().post();    // this gave error because I did not pass request
		
		//ValidatableResponse response = resp.then().statusCode(200).and().body("Name", equalTo("Indian Chai and snanks")).log().all();
		System.out.println(resp.getStatusCode());
		System.out.println(resp.asString());
		System.out.println("Response:" +resp.getBody().asString());
		
		// First get the JsonPath object instance from the Response interface
		JsonPath path = resp.jsonPath();   // for parsing json
		System.out.println("Path:"+path);
		String name = path.get("Name");
		Assert.assertTrue(name.equalsIgnoreCase("Indian Chai and snanks"));
		System.out.println(name);
		
		//this will print the object
		//System.out.println(response.body("Name", equalTo("Indian Chai and snanks")));		
		
		// to fetch value from the json object
	/*	JSONParser parser = new JSONParser();
		JSONObject response = (JSONObject) parser.parse(resp.getBody().asString());
		System.out.println("Name:" +response.get("Name"));
		*/
		
		//get created coffee
		/*int id=15;
		request.pathParam("Id", id);
		resp = given().spec(request).get("/{Id}").then().statusCode(200).extract().response();
		System.out.println(resp.getBody().asString());	
		*/
		
	/*	resp = given().spec(request).when().get();
		System.out.println(resp.getBody().asString());
		JsonPath path = resp.jsonPath();

		List<String> coffees = path.getList("Name");
			for (String coffee : coffees) {
				System.out.println(coffee);
			}
		*/
		
	}

	@Test
	public void deleteCoffee() {
		
		int Id=15;
		RestAssured.baseURI = "http://webservice.toscacloud.com";
		RestAssured.basePath = "/training/api/Coffees/f90500b5-1280-4338-dab1-cf0a55c12dca";
		
		RequestSpecification request = RestAssured.given().pathParam("Id", Id);
		Response resp = given().spec(request).when().delete("/{Id}");
		System.out.println(resp.getStatusCode());	
		
	}
	
	
	@Test
	public void mapsApiFunctionality() {
		
		//scenario : create address, update the address and then delete it
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		//RestAssured.basePath = "/maps/api/place/add/json";
		
		RequestSpecification request = RestAssured.given().header("Content-Type", "application/json").accept(ContentType.JSON)
									   .queryParam("key", "qaclick123");
		
		Response response = given().spec(request).body(Payload.addFile()).log().all().when().post("/maps/api/place/add/json").then().statusCode(200).extract().response();
		System.out.println("Response:" +response.asString());	
		
		JsonPath responseValidator = response.jsonPath();
		String placeId = responseValidator.getString("place_id");
		System.out.println(placeId);
		
		
		//update address
		
		Map<String, String> payloadMap = new HashMap<>();
		payloadMap.put("place_id", placeId);
		payloadMap.put("address", "70 Summer walk, USA");
		payloadMap.put("key", "qaclick123");
		
		
		response = given().spec(request).body(payloadMap).log().all().when().put("/maps/api/place/update/json").then()
					.statusCode(200).and().body("msg", equalTo("Address successfully updated")).extract().response();
		
		System.out.println("Updated address:" +response.asString());	
		
		//verify if the address got updated or not
		
		response = given().spec(request).queryParam("place_id", placeId).log().all().when().get("/maps/api/place/get/json")
					.then().statusCode(200).and().body("address", equalTo("70 Summer walk, USA")).extract().response();
		
		System.out.println("Get address" +response.asString());
		
		JsonPath js_address = response.jsonPath();
		String actual_address = js_address.getString("address");
		System.out.println("Actual address:" +actual_address);	
		
	}	
	
}
