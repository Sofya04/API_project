package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.ContactDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class AddNewContactsRA {
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoic29ua2EwNEBnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTY2OTgyMTQ4NSwiaWF0IjoxNjY5MjIxNDg1fQ.cxWDwZl0JPXkt44ee_c1knwF7eZEnmVlE4dWZnTG5qc";

    @BeforeMethod
    public void setUp(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com/";
        RestAssured.basePath = "v1";
    }

    @Test
    public void addNewContactSuccess(){
        int i = new Random().nextInt(1000)+1000;
        ContactDto contact = ContactDto.builder()
                .name("Sonya")
                .lastName("Kabzon")
                .email("sonya"+i+"@gmail.com")
                .phone("98347823"+i)
                .address("Rehovot")
                .description("Colleague").build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message",containsString("Contact was added!"));
    }
    @Test
    public void addNewContactUnauthorized(){
        ContactDto contact = ContactDto.builder()
                .name("Irina")
                .lastName("Sheyk")
                .email("irina@gmail.com")
                .phone("98347823")
                .address("Rehovot")
                .description("Colleague").build();
        given()
                .header("Authorization","fkdfk")
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(401);
                //.assertThat().body("message",containsString("Contact was added!"));
    }
    @Test
    public void addNewContactWrongUsernameWithoutSpecialSymbol(){
        ContactDto contact = ContactDto.builder()
                .name("Irina")
                .lastName("Sheyk")
                .email("irinagmail.com")
                .phone("98347823")
                .address("Rehovot")
                .description("Colleague").build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.email",containsString("must be a well-formed email address"));
    }
    @Test
    public void addNewContactWrongUsernameLackOfSymbols(){
        ContactDto contact = ContactDto.builder()
                .name("Irina")
                .lastName("Sheyk")
                .email("i.com")
                .phone("98347823")
                .address("Rehovot")
                .description("Colleague").build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.email",containsString("must be a well-formed email address"));
    }
    @Test
    public void addNewContactWrongPhoneNotOnlyDigits(){
        ContactDto contact = ContactDto.builder()
                .name("Irina")
                .lastName("Sheyk")
                .email("i.com")
                .phone("9834djshd7483")
                .address("Rehovot")
                .description("Colleague").build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.phone",containsString("Phone number must contain only digits!"));
    }
    @Test
    public void addNewContactWrongPhoneLackOfSymbols(){
        ContactDto contact = ContactDto.builder()
                .name("Irina")
                .lastName("Sheyk")
                .email("i.com")
                .phone("9834")
                .address("Rehovot")
                .description("Colleague").build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.phone",containsString("And length min 10, max 15!"));
    }
}
