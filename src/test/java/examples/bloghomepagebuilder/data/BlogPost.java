package examples.bloghomepagebuilder.data;

import com.flipkart.databuilderframework.model.DataAdapter;

public class BlogPost extends DataAdapter<BlogPost> {
    private final String title;
    private final byte[] body;


    public BlogPost(String title, byte[] body) {
        super(BlogPost.class);
        this.title = title;
        this.body = body;
    }


    public String getTitle() {
        return title;
    }

    public byte[] getBody() {
        return body;
    }
}
