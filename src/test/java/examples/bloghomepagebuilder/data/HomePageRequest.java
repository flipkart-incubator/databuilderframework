package examples.bloghomepagebuilder.data;

import com.flipkart.databuilderframework.model.DataAdapter;

public class HomePageRequest extends DataAdapter<HomePageRequest>{
    private final String requestAuthToken;
    private final String userAuthToken;
    private byte[] body;

    public HomePageRequest(String authToken, String userAuthToken, byte[] body) {
        super(HomePageRequest.class);
        this.requestAuthToken = authToken;
        this.userAuthToken = userAuthToken;
        this.body = body;
    }

    public String getRequestAuthToken() {
        return requestAuthToken;
    }

    public String getUserAuthToken() {
        return userAuthToken;
    }

    public byte[] getBody() {
        return body;
    }
}
