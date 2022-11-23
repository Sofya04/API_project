package okhttp3;

import config.Provider;
import dto.AuthRequestDto;
import dto.AuthResponseDto;
import dto.ErrorDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;

public class LoginTests{

    @Test
    public void loginSuccess() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder().username("sonka04@gmail.com")
                .password("Sonka04$").build();
        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(auth), Provider.getInstance().getJson());
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .post(body).build();
        Response response = Provider.getInstance().getClient().newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(),200);

        AuthResponseDto responseDto = Provider.getInstance().getGson().fromJson(response.body().string(), AuthResponseDto.class);
        System.out.println(responseDto.getToken());//eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoic29ua2EwNEBnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTY2OTgyMTQ4NSwiaWF0IjoxNjY5MjIxNDg1fQ.cxWDwZl0JPXkt44ee_c1knwF7eZEnmVlE4dWZnTG5qc
    }
    @Test
    public void loginWrongEmail() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder().username("sonka04gmail.com")
                .password("Sonka04$").build();
        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(auth), Provider.getInstance().getJson());
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .post(body).build();
        Response response = Provider.getInstance().getClient().newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),401);

        ErrorDto errorDto = Provider.getInstance().getGson().fromJson(response.body().string(), ErrorDto.class);
        Object message = errorDto.getMessage();
        Assert.assertEquals(message, "Login or Password incorrect");
        Assert.assertEquals(errorDto.getStatus(), 401);
    }
}
