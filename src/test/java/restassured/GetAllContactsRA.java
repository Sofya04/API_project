package restassured;

import com.jayway.restassured.RestAssured;
import dto.ContactDto;
import dto.GetAllContactsDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.jayway.restassured.RestAssured.given;

public class GetAllContactsRA {
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoic29ua2EwNEBnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTY2OTgyMTQ4NSwiaWF0IjoxNjY5MjIxNDg1fQ.cxWDwZl0JPXkt44ee_c1knwF7eZEnmVlE4dWZnTG5qc";

    @BeforeMethod
    public void setUp(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com/";
        RestAssured.basePath = "v1";
    }

    @Test
     public void getAllContactsSuccess(){
       GetAllContactsDto contactsDto = given()
                .header("Authorization",token)
                .when()
                .get("contacts")
                .then()
                .assertThat().statusCode(200)
                .extract()
               .response()
                .as(GetAllContactsDto.class);
        List<ContactDto> list = contactsDto.getContacts();
        for(ContactDto contactDto: list){
            System.out.println(contactDto.toString());
            System.out.println("****************");
        }

    }
    @Test
    public void getAllContactsUnauthorized(){
        given()
                .header("Authorization","dfdn")
                .when()
                .get("contacts")
                .then()
                .assertThat().statusCode(401);
    }
}
