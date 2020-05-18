package restPractice;

import org.testng.Assert;

import files.Payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {
		
		//Print No of courses returned by API
		
		JsonPath js = new JsonPath(Payload.mockResponse());
		int no_courses = js.getInt("courses.size()");
		System.out.println(no_courses);
		
		//Print Purchase Amount
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println(purchaseAmount);
		
		//Print Title of the first course
		String title = js.get("courses[1].title");
		System.out.println(title);
		
		//Print All course titles and their respective Prices
			for(int i=0;i<no_courses;i++) {
				String courseTitle = js.getString("courses["+i+"].title");
				int price = js.getInt("courses["+i+"].price");
				System.out.println(courseTitle +" - " + price);
			}
			
			//Print no of copies sold by RPA Course
			
			for(int i=0;i<no_courses;i++) {
				
				String expectedTitle = js.getString("courses["+i+"].title");
					if(expectedTitle.equalsIgnoreCase("rpa")) {
						
						int copiesSold = js.getInt("courses["+i+"].copies");
						System.out.println("No. of cpoies sold by RPA course:"+copiesSold);
						break;
					}				
			}
			
			//Verify if Sum of all Course prices matches with Purchase Amount
			
			int sum=0;
			for(int i=0;i<no_courses;i++) {
				
				sum = sum + (js.getInt("courses["+i+"].price") * js.getInt("courses["+i+"].copies"));
				
			}
			
			System.out.println("Total of all courses:" +sum);
			Assert.assertEquals(purchaseAmount, sum);
		
	}
		
}
