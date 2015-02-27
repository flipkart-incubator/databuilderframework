package examples.bloghomepagebuilder.builders;


import com.flipkart.databuilderframework.annotations.DataBuilderClassInfo;
import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataBuilderException;
import com.flipkart.databuilderframework.engine.DataSetAccessor;
import com.flipkart.databuilderframework.model.Data;
import examples.bloghomepagebuilder.data.BlogId;
import examples.bloghomepagebuilder.data.BlogPost;
import examples.bloghomepagebuilder.data.UserDetails;

@DataBuilderClassInfo(produces = BlogPost.class, consumes = {UserDetails.class, BlogId.class})
public class BlogPostSource extends DataBuilder {
    @Override
    public Data process(DataBuilderContext context) throws DataBuilderException {
        final DataSetAccessor accessor = context.getDataSet().accessor();
        UserDetails userDetails = accessor.get(UserDetails.class);
        BlogId blogId = accessor.get(BlogId.class);
        //Get blog for this user and id
        return new BlogPost("A data depndent frame work",
                "Blah .. blah and some more blah".getBytes());
    }
}
