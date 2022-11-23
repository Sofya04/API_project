package okhttp3;

import config.Provider;
import dto.AuthRequestDto;
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
    }

}
