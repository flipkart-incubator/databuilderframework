package examples.bloghomepagebuilder.data;

import com.flipkart.databuilderframework.model.DataAdapter;

public class BlogId extends DataAdapter<BlogId> {
    private final String id;

    public BlogId(String id) {
        super(BlogId.class);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
