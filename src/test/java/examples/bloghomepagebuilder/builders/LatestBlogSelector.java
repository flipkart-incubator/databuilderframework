package examples.bloghomepagebuilder.builders;

import com.flipkart.databuilderframework.annotations.DataBuilderClassInfo;
import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataBuilderException;
import com.flipkart.databuilderframework.model.Data;
import examples.bloghomepagebuilder.data.BlogId;
import examples.bloghomepagebuilder.data.PostList;

@DataBuilderClassInfo(produces = BlogId.class, consumes = PostList.class)
public class LatestBlogSelector extends DataBuilder{
    @Override
    public Data process(DataBuilderContext context) throws DataBuilderException {
        PostList postList = context.getDataSet().accessor().get(PostList.class);
        //Select the appropriate blog
        return new BlogId("blog-123");
    }
}
