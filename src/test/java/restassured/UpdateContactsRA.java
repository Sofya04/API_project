package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.Argument;
import dto.ContactDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class UpdateContactsRA {
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
    public void updateContactSuccess(){
        int i = new Random().nextInt(1000)+1000;

        ContactDto contact = ContactDto.builder()
                .id(id)
                .name("Irina")
                .lastName("Sheyk")
                .email("irina"+i+"@hollywood.com")
                .phone("9834557483"+i)
                .address("Los Angeles")
                .description("Colleague").build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(200);
    }
    @Test
    public void updateContactWrongEmailWithoutSpecialSymbol(){
        ContactDto contact = ContactDto.builder()
                .id(id)
                .name("Irina")
                .lastName("Sheyk")
                .email("irinahollywood.com")
                .phone("9834557483")
                .address("Los Angeles")
                .description("Colleague").build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.email", containsString("must be a well-formed email address"));
    }
    @Test
    public void updateContactWrongEmailLackOfSymbols(){
        ContactDto contact = ContactDto.builder()
                .id(id)
                .name("Irina")
                .lastName("Sheyk")
                .email("i.com")
                .phone("9834557483")
                .address("Los Angeles")
                .description("Colleague").build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.email", containsString("must be a well-formed email address"));
    }
    @Test
    public void updateContactWrongPhoneLackOfSymbols(){
        ContactDto contact = ContactDto.builder()
                .id(id)
                .name("Irina")
                .lastName("Sheyk")
                .email("irina@hollywood.com")
                .phone("983455749")
                .address("Los Angeles")
                .description("Colleague").build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.phone", containsString("And length min 10, max 15!"));
    }
    @Test
    public void updateContactWrongPhoneTooMuchSymbols(){
        ContactDto contact = ContactDto.builder()
                .id(id)
                .name("Irina")
                .lastName("Sheyk")
                .email("irina@hollywood.com")
                .phone("9834557495749302")
                .address("Los Angeles")
                .description("Colleague").build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.phone", containsString("And length min 10, max 15!"));
    }
    @Test
    public void updateContactWrongPhoneWithLetters(){
        ContactDto contact = ContactDto.builder()
                .id(id)
                .name("Irina")
                .lastName("Sheyk")
                .email("irinahollywood.com")
                .phone("98345hfud5749")
                .address("Los Angeles")
                .description("Colleague").build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.phone", containsString("Phone number must contain only digits!"));
    }

}
