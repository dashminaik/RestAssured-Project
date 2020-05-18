package restPractice;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import files.Payload;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class StaticJson {
	
	@BeforeTest
	public void setEndpoint() {
		
		RestAssured.baseURI = "http://216.10.245.166";
		RestAssured.basePath = "/Library";
	}
	
	@Test
	public void addBook() throws IOException {
		
		String path = "D:\\User Data\\Desktop\\Eclipse\\Workspace\\RestAssured Project\\payload\\jsonPayload.json";
			RequestSpecification request = RestAssured.given().header("Content-Type", "application/json").accept(ContentType.JSON);
											
			/*Response response = given().spec(request).body(payloadObject.addBook_DynamicValues("das","3333")).when().get("/Addbook.php")
											.then().statusCode(200).body("Msg", equalTo("successfully added")).extract().response();
			*/
			
			Response response = given().spec(request).body(GenerateStringFromResource(path)).when().get("/Addbook.php")
					.then().statusCode(200).body("Msg", equalTo("successfully added")).extract().response();

			
			
			System.out.println("Response:" +response.asString());
			System.out.println("status code:" +response.getStatusCode());
			
			JsonPath js = response.jsonPath();
			String id = js.getString("ID");
			System.out.println("ID:" +id);				
	}
	
	public String GenerateStringFromResource(String path) throws IOException {
		
		return new String (Files.readAllBytes(Paths.get(path)));	
		
	}
}
