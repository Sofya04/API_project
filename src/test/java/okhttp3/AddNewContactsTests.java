package okhttp3;

import config.Provider;
import dto.ContactDto;
import dto.ContactDtoResponse;
import dto.ErrorDto;
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
        ContactDtoResponse contactDtoResponse = Provider.getInstance().getGson().fromJson(response.body().string(), ContactDtoResponse.class);

        Assert.assertTrue(contactDtoResponse.getMessage().toString().contains("Contact was added!"));
        String mess = contactDtoResponse.getMessage();
        String[] all = mess.split("ID: ");
        String id = all[1];
        System.out.println(id);
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
        //must be a well-formed email address
        ErrorDto errorDto = Provider.getInstance().getGson().fromJson(response.body().string(), ErrorDto.class);
        Object message = errorDto.getMessage();
        Assert.assertTrue(message.toString().contains("Phone number must contain only digits!"));

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
    @Test(enabled = false)//bug
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
        ErrorDto errorDto = Provider.getInstance().getGson().fromJson(response.body().string(), ErrorDto.class);
        Object message = errorDto.getMessage();
        System.out.println(errorDto.getMessage());
//        Assert.assertEquals(message, "User already exists");
//        Assert.assertEquals(errorDto.getStatus(), 409);
    }
}
