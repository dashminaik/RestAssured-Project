package restPractice;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.Payload;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static org.hamcrest.Matchers.*; 
import static io.restassured.RestAssured.*;

public class LibraryAPI {
	
	
	@BeforeTest
	public void setEndpoint() {
		
		RestAssured.baseURI = "http://216.10.245.166";
		RestAssured.basePath = "/Library";
	}
	
	@Test
	public void createBook() {
		
		Payload payloadObject = new Payload();
		
		RequestSpecification request = RestAssured.given().header("Content-Type", "application/json").accept(ContentType.JSON);
										
		Response response = given().spec(request).body(payloadObject.addBook()).when().get("/Addbook.php")
										.then().statusCode(200).body("Msg", equalTo("successfully added")).extract().response();
		
		System.out.println("Response:" +response.asString());
		System.out.println("status code:" +response.getStatusCode());
		
		JsonPath js = response.jsonPath();
		String id = js.getString("ID");
		System.out.println("ID:" +id);
		
	}
	
	@Test(dataProvider="BooksData")
	public void createBookDynamically(String isbn, String aisle) {
		
		Payload payloadObject = new Payload();
		
		RequestSpecification request = RestAssured.given().header("Content-Type", "application/json").accept(ContentType.JSON);
										
		/*Response response = given().spec(request).body(payloadObject.addBook_DynamicValues("das","3333")).when().get("/Addbook.php")
										.then().statusCode(200).body("Msg", equalTo("successfully added")).extract().response();
		*/
		
		Response response = given().spec(request).body(payloadObject.addBook_DynamicValues(isbn,aisle)).when().get("/Addbook.php")
				.then().statusCode(200).body("Msg", equalTo("successfully added")).extract().response();

		
		
		System.out.println("Response:" +response.asString());
		System.out.println("status code:" +response.getStatusCode());
		
		JsonPath js = response.jsonPath();
		String id = js.getString("ID");
		System.out.println("ID:" +id);	
		
		
	}
	
	@Test(dataProvider="BooksData")
	public void deleteBook(String isbn, String aisle) {
		
		Payload payloadObject = new Payload();
		
		RequestSpecification request = RestAssured.given().header("Content-Type", "application/json").accept(ContentType.JSON);
		
		Response response = given().spec(request).body(payloadObject.deleteBook(isbn,aisle)).when().delete("DeleteBook.php")
							.then().statusCode(200).body("msg", equalTo("book is successfully deleted")).extract().response();

		System.out.println("Response:" +response.asString());
		System.out.println("status code:" +response.getStatusCode());
		
		
	}
	
	@DataProvider(name = "BooksData")
	public Object getData() {
		
		return new Object[][] {
			{"das", "21"}, {"das","31"}, {"das","45"}
			
		};		
	}
	
}
