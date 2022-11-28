package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.AuthRequestDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class RegistrationTestsRA {
    @BeforeMethod
    public void setUp(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com/";
        RestAssured.basePath = "v1";
    }

    @Test
    public void registrationSuccess(){
        int i = new Random().nextInt(1000)+1000;
        AuthRequestDto auth = AuthRequestDto.builder().username("eliza"+i+"@yandex.ru")
                .password("Eliza12345$")
                .build();
        String token = given().body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post("user/registration/usernamepassword")
                .then().statusCode(200)
                .extract()
                .path("token");
        System.out.println(token);
    }
    @Test
    public void registrationWrongUserName(){
        AuthRequestDto auth = AuthRequestDto.builder().username("elizayandex.ru")
                .password("Eliza12345$")
                .build();

        given().body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post("user/registration/usernamepassword")
                .then().statusCode(400)
                .assertThat().body("message.username", containsString("must be a well-formed email address"));

    }
}
