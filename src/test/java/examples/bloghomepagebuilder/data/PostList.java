package examples.bloghomepagebuilder.data;

import io.appform.databuilderframework.model.DataAdapter;

import java.util.List;

public class PostList extends DataAdapter<PostList> {
    private List<String> postTitles;

    public PostList(List<String> postTitles) {
        super(PostList.class);
        this.postTitles = postTitles;
    }

    public List<String> getPostTitles() {
        return postTitles;
    }
}
