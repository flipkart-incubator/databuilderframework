package examples.bloghomepagebuilder.builders;

import io.appform.databuilderframework.annotations.DataBuilderClassInfo;
import io.appform.databuilderframework.engine.DataBuilder;
import io.appform.databuilderframework.engine.DataBuilderContext;
import io.appform.databuilderframework.engine.DataBuilderException;
import io.appform.databuilderframework.model.Data;
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
