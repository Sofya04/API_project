package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.ContactDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class DeleteContactByIdRA {
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoic29ua2EwNEBnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTY2OTgyMTQ4NSwiaWF0IjoxNjY5MjIxNDg1fQ.cxWDwZl0JPXkt44ee_c1knwF7eZEnmVlE4dWZnTG5qc";
    String id;
    @BeforeMethod
    public void setUp(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com/";
        RestAssured.basePath = "v1";

        ContactDto contact = ContactDto.builder()
                .name("Irina")
                .lastName("Sheyk")
                .email("irina@gmail.com")
                .phone("98347823858")
                .address("Los Angeles")
                .description("Colleague").build();

        String message = given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(200)
                .extract()
                .path("message");

        String[] all = message.split("ID: ");
        id = all[1];
        System.out.println(id);
    }
    @Test
    public void deleteContactByIdSuccess(){
        given()
                .header("Authorization",token)
                .when()
                .delete("contacts/"+id)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message", containsString("Contact was deleted!"));
    }
    @Test
    public void deleteContactByIdUnauthorized(){
        given()
                .header("Authorization","odkfodjf")
                .when()
                .delete("contacts/"+id)
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message", containsString("JWT strings must contain exactly 2 period characters"));
    }
    @Test
    public void deleteContactByIdFormatError(){
        given()
                .header("Authorization",token)
                .when()
                .delete("contacts/"+"1hfj")
                .then()
                .assertThat().statusCode(400);
    }

}
