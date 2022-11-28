package okhttp3;

import config.Provider;
import dto.ContactDto;
import dto.ContactDtoResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class DeleteContactsByIdTests {
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoic29ua2EwNEBnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTY2OTgyMTQ4NSwiaWF0IjoxNjY5MjIxNDg1fQ.cxWDwZl0JPXkt44ee_c1knwF7eZEnmVlE4dWZnTG5qc";
    String id;
    @BeforeMethod
    public void addNewContact() throws IOException {
        ContactDto contact = ContactDto.builder()
                .name("Sonya")
                .lastName("Kabzon")
                .email("sonya@gmail.com")
                .phone("983478238483")
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
        String [] all =mess.split("ID: ");
        id = all[1];

    }
    @Test
    public void deleteContactByIdSuccess() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/"+id)
                .addHeader("Authorization", token)
                .delete().build();
        Response response = Provider.getInstance().getClient().newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());

        ContactDtoResponse contactDtoResponse = Provider.getInstance().getGson().fromJson(response.body().string(), ContactDtoResponse.class);
        Assert.assertEquals(contactDtoResponse.getMessage(),"Contact was deleted!");
    }
}
