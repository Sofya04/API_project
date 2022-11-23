package okhttp3;

import config.Provider;
import dto.ContactDto;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class AddNewContactsTests {
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoic29ua2EwNEBnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTY2OTgyMTQ4NSwiaWF0IjoxNjY5MjIxNDg1fQ.cxWDwZl0JPXkt44ee_c1knwF7eZEnmVlE4dWZnTG5qc";

    @Test
    public void addNewContactSuccess() throws IOException {
        int i = new Random().nextInt(1000)+1000;
        ContactDto contact = ContactDto.builder()
                .name("Sonya")
                .lastName("Kabzon")
                .email("sonya"+i+"@gmail.com")
                .phone("98347823"+i)
                .address("Rehovot")
                .description("Colleague").build();
        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(contact), Provider.getInstance().getJson());
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", token)
                .post(body).build();
        Response response = Provider.getInstance().getClient().newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(),200);
    }
    @Test
    public void addNewContactFormatError() throws IOException {
        int i = new Random().nextInt(1000)+1000;
        ContactDto contact = ContactDto.builder()
                .name("Sonya")
                .lastName("Kabzon")
                .email("sonya"+i+"@gmail.com")
                .phone("98"+i)
                .address("Rehovot")
                .description("Colleague").build();
        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(contact), Provider.getInstance().getJson());
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", token)
                .post(body).build();
        Response response = Provider.getInstance().getClient().newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);
    }
    @Test
    public void addNewContactUnauthorized() throws IOException {
        int i = new Random().nextInt(1000)+1000;
        ContactDto contact = ContactDto.builder()
                .name("Sonya")
                .lastName("Kabzon")
                .email("sonya"+i+"@gmail.com")
                .phone("98347823"+i)
                .address("Rehovot")
                .description("Colleague").build();
        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(contact), Provider.getInstance().getJson());
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", "fdokfj")
                .post(body).build();
        Response response = Provider.getInstance().getClient().newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),401);
    }
    @Test
    public void addNewContactDuplicateContact() throws IOException {
        ContactDto contact = ContactDto.builder()
                .name("Volodymyr")
                .lastName("Zelenskyy")
                .email("vladimir1983@mail.com")
                .phone("3849198343879")
                .address("Kryvyi Rih")
                .description("My dear friend").build();
        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(contact), Provider.getInstance().getJson());
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", token)
                .post(body).build();
        Response response = Provider.getInstance().getClient().newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),409);
    }
}
