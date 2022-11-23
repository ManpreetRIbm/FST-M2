package project;

import static org.hamcrest.CoreMatchers.equalTo;

import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import static io.restassured.RestAssured.given;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import org.testng.annotations.Test;
import static io.restassured.RestAssured.oauth2;

public class GitHubProjectTest {
	
	RequestSpecification requestSpec;
	ResponseSpecification responseSpec;
	
	String key;
	int id=74124756;
	
	@BeforeClass
	public void setUp()
	{
		requestSpec = new RequestSpecBuilder()
							.setContentType(ContentType.JSON)
							.setAuth(oauth2("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"))
						    .setBaseUri("https://api.github.com")
						    .build();
		
		responseSpec = new ResponseSpecBuilder()
                // Check response content type
                .expectContentType("application/json")
                // Build response specification
                .build();
	}
	
	@Test(priority=1)
	public void addSSHKey()
	{
		//post request
		String reqBody = "{\"title\": \"TestAPIKey\", \"key\": \"ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCLkp/6qqprkUG+rE5u0AodzlFN081JO8zIVh6+zGl6kTjPVe0/W5xx1uDXytJJMdu+t6nf2hLHx7+E50C3poainWscCHlCPZpRhKfrobSOsac6UBLT7+gHneXBQ3cLVMvJVH4hB6knUohe1wDFC7pIzO/8olKdmAyPsaR6mx3bGS3SA8RQpADVL/NZkkUABrS40uS4J6p26vW+izhxuFt61iJPA4SVkDP6+YPbDKGkO7NL6QPYsrzSmqDFQs0dhBFoAFWJL+GHODrLFwGX1xLcCWlsQhyjRcoylf90ln5Ugpwb+qUB9RBZ4nGlgreSxQ+2pKSBwzq29ZiETzzVIFCJ\"}";
		
		Response response = given().spec(requestSpec).body(reqBody).when().post("/user/keys");

		System.out.println(response.getBody().asString());
		
		//Extract id from response 
        id = response.then().extract().path("id");;
		
		// Assertion
        response.then().log().all().statusCode(201);
		
	}
	
	@Test(priority=2)
	public void getKey()
	{
		//get request
		Response response = given().spec(requestSpec).pathParam("keyid", id).when().get("/user/keys/{keyid}");
		
		//Print the response 
		response.then().log().body();
		Reporter.log(response.asPrettyString());
		
		//Assertion
		response.then().log().all().statusCode(200);
	}

	@Test(priority=3)
	public void deleteKey()
	{
		//delete request 
		Response response = given().spec(requestSpec).pathParam("keyid", id).when().delete("/user/keys/{keyid}");
		
		//Print the response 
		response.then().log().body();
		Reporter.log(response.asPrettyString());
		
		//Assertion
		response.then().log().all().statusCode(204);
	}
}
