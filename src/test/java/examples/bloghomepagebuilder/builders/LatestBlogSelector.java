package examples.bloghomepagebuilder.builders;

import io.appform.databuilderframework.annotations.DataBuilderClassInfo;
import io.appform.databuilderframework.engine.DataBuilder;
import io.appform.databuilderframework.engine.DataBuilderContext;
import io.appform.databuilderframework.engine.DataBuilderException;
import io.appform.databuilderframework.model.Data;
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
