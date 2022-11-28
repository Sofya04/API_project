package okhttp3;

import config.Provider;
import dto.AuthRequestDto;
import dto.ErrorDto;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.security.PrivateKey;
import java.util.Random;

public class RegistrationTests {

    @Test
    public void registrationSuccess() throws IOException {
        int i = new Random().nextInt(1000)+1000;
        AuthRequestDto auth = AuthRequestDto.builder()
                .username("eliza"+i+"@yandex.ru")
                .password("Eliza12345$")
                .build();
        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(auth), Provider.getInstance().getJson());
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();
        Response response = Provider.getInstance().getClient().newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(),200);
    }
    @Test
    public void registrationWrongEmailOrPasswordFormat() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder()
                .username("elizayandex.ru")
                .password("Eliza12345$")
                .build();
        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(auth), Provider.getInstance().getJson());
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();
        Response response = Provider.getInstance().getClient().newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);

        ErrorDto errorDto = Provider.getInstance().getGson().fromJson(response.body().string(), ErrorDto.class);
        Assert.assertEquals(errorDto.getStatus(), 400);
        Assert.assertEquals(errorDto.getError(),"Bad Request");
        Object message = errorDto.getMessage();
        Assert.assertTrue(message.toString().contains("must be a well-formed email address"));


    }
    @Test
    public void registrationDuplicateUser() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder()
                .username("sonka04@gmail.com")
                .password("Sonka04$")
                .build();
        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(auth), Provider.getInstance().getJson());
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();
        Response response = Provider.getInstance().getClient().newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),409);
        ErrorDto errorDto = Provider.getInstance().getGson().fromJson(response.body().string(), ErrorDto.class);
        Object message = errorDto.getMessage();
        Assert.assertEquals(message, "User already exists");
        Assert.assertEquals(errorDto.getStatus(), 409);
    }

}
