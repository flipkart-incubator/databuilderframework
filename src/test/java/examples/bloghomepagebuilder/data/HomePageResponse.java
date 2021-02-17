package examples.bloghomepagebuilder.data;

import io.appform.databuilderframework.model.DataAdapter;

import java.util.List;

public class HomePageResponse extends DataAdapter<HomePageResponse> {
    private final String userName;
    private final String title;
    private final byte[] latestBody;
    private final List<String> followers;
    private final List<String> posts;
    private final List<String> tags;

    public HomePageResponse(String userName, String title, byte[] latestBody, List<String> followers, List<String> posts, List<String> tags) {
        super(HomePageResponse.class);
        this.userName = userName;
        this.title = title;
        this.latestBody = latestBody;
        this.followers = followers;
        this.posts = posts;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public byte[] getLatestBody() {
        return latestBody;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public List<String> getPosts() {
        return posts;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getUserName() {
        return userName;
    }
}
