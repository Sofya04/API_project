package okhttp3;

import config.Provider;
import dto.ContactDto;
import dto.GetAllContactsDto;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class GetAllContactsTests {
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoic29ua2EwNEBnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTY2OTgyMTQ4NSwiaWF0IjoxNjY5MjIxNDg1fQ.cxWDwZl0JPXkt44ee_c1knwF7eZEnmVlE4dWZnTG5qc";

    @Test
    public void getAllContactsSuccess() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization",token)
                .get()
                .build();

        Response response = Provider.getInstance().getClient().newCall(request).execute();

        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(),200);
        GetAllContactsDto contactsDto = Provider.getInstance().getGson().fromJson(response.body().string(), GetAllContactsDto.class);

        List<ContactDto>list = contactsDto.getContacts();
        for(ContactDto contactDto: list){
            System.out.println(contactDto.toString());
            System.out.println("****************");
        }
    }
    @Test
    public void getAllContactsUnauthorized() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization","odijfodj")
                .get()
                .build();
       Response response = Provider.getInstance().getClient().newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),401);

    }

}
