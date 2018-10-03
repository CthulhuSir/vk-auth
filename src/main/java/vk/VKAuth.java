package vk;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.users.UserXtrCounters;

import javax.servlet.http.HttpServlet;
import java.util.ArrayList;
import java.util.List;

public class VKAuth extends HttpServlet {

    private static TransportClient transportClient = HttpTransportClient.getInstance();
    private static VkApiClient vk = new VkApiClient(transportClient);

    private static final Integer APP_ID = 6708346;
    private static final String REDIRECT_URI = "http://localhost:8080/";
    private static final String CLIENT_SECRET = "nnRuCQEePOEdrnb2B2ID";
    private static final String API_VER = "5.85";
    private String code;
    private boolean codeStatus = false;

    private UserActor actor;

    public boolean isCodeSet() {
        return codeStatus;
    }

    public void setCode(String code) {
        this.code = code;
        codeStatus = true;
        setActor();
    }

    public String getAuthLink() {
        return ("https://oauth.vk.com/authorize?client_id=" + APP_ID + "&display=popup&redirect_uri=" + REDIRECT_URI + "&scope=friends&response_type=code&v=" + API_VER);
    }

    private void setActor(){
        UserAuthResponse authResponse = null;
        try {
            authResponse = vk.oauth()
                    .userAuthorizationCodeFlow(APP_ID, CLIENT_SECRET, REDIRECT_URI, code)
                    .execute();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            codeStatus = false;
        }

        actor = new UserActor(authResponse.getUserId(), authResponse.getAccessToken());

    }

    public String getUserName(Integer userID){
        String fullName = "";
        try {
            List<UserXtrCounters> users = vk.users().get(actor)
                    .userIds("" + userID)
                    .execute();

            fullName = users.get(0).getFirstName() + " " + users.get(0).getLastName();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            codeStatus = false;
        }
        return fullName;
    }

    public List<Integer> getFriendsIDs(Integer userID, Integer count){
        List<Integer> friendsIDs= new ArrayList<Integer>();

        try {

            List<Integer> response = vk.friends().get(actor)
                    .userId(userID)
                    .count(count)
                    .offset(count)
                    .execute()
                    .getItems();

            friendsIDs = response;

        } catch (ApiException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            codeStatus = false;
        }

        return friendsIDs;
    }

    public List<String> getFriendsNames(List<Integer> ids){
        List<String> names = new ArrayList<String>();
        for(Integer i : ids){

            try {

                List<UserXtrCounters> users = vk.users().get(actor)
                        .userIds("" + i)
                        .execute();

                names.add(users.get(0).getFirstName() + " " + users.get(0).getLastName());

            } catch (ApiException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                codeStatus = false;
            }

        }
        return names;
    }

    public  Integer getActorID(){
        return actor.getId();
    }


}
