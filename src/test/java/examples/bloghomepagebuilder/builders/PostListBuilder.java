package examples.bloghomepagebuilder.builders;

import com.flipkart.databuilderframework.annotations.DataBuilderClassInfo;
import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataBuilderException;
import com.flipkart.databuilderframework.model.Data;
import com.google.common.collect.ImmutableList;
import examples.bloghomepagebuilder.data.PostList;
import examples.bloghomepagebuilder.data.UserDetails;

@DataBuilderClassInfo(produces = PostList.class, consumes = UserDetails.class)
public class PostListBuilder extends DataBuilder {
    @Override
    public Data process(DataBuilderContext context) throws DataBuilderException {
        return new PostList(ImmutableList.of("Foxtrot Blah Blah", "Ranger Blah Blah"));
    }
}
