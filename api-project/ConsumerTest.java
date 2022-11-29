package project;

import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.extension.ExtendWith;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.consumer.dsl.*;
import org.junit.jupiter.api.Test;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {
	//Create the headers
	Map<String,String> headers = new HashMap<String, String>();
	//Set resource path
	String resourcePath = "/api/users";
	
	//Create the contract
	@Pact(consumer = "UserConsumer", provider = "UserProvider")
	public RequestResponsePact createPact(PactDslWithProvider builder) {
		//Set the headers
		headers.put("Content-type", "application/json");
		
		//Create the body
		DslPart requestResponseBody = new PactDslJsonBody()
				.numberType("id") 
				.stringType("firstName")
				.stringType("lastName")
				.stringType("email");
		
		return builder.given("A request to create a user") 
			.uponReceiving("A request to create a user") 
				.method("POST")
				.path(resourcePath) 
				.headers(headers)
				.body(requestResponseBody)
			.willRespondWith() 
				.status(201)
				.body(requestResponseBody)
			.toPact();
		
	}
	
	@Test
	@PactTestFor(providerName = "UserProvider" , port ="8282")
	public void consumerTest()
	{
		//baseUri
		String baseUri = "http://localhost:8282"+resourcePath;
		
		//Request Body
        Map<String , Object> reqbody = new HashMap<String, Object>();
        reqbody.put("id", 123);
        reqbody.put("firstName", "Manpreet");
        reqbody.put("lastName", "Raj");
        reqbody.put("email", "manpreet@example.com");

        //generate Response
        given().headers(headers).body(reqbody)
        .when().post(baseUri)
        .then().statusCode(201).log().all();
		
	}
	
}
