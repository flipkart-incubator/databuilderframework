package examples.bloghomepagebuilder.builders;

import com.flipkart.databuilderframework.annotations.DataBuilderClassInfo;
import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataBuilderException;
import com.flipkart.databuilderframework.engine.DataSetAccessor;
import com.flipkart.databuilderframework.model.Data;
import examples.bloghomepagebuilder.data.*;

@DataBuilderClassInfo(produces = HomePageResponse.class, consumes = {UserDetails.class, BlogPost.class, PostList.class, FollowerList.class, RecommendedTags.class})
public class HomePageBuilder extends DataBuilder {

    public HomePageBuilder() {

    }

    @Override
    public Data process(DataBuilderContext context) throws DataBuilderException {
        final DataSetAccessor dataSetAccessor = context.getDataSet().accessor();
        return new HomePageResponse(
                dataSetAccessor.get(UserDetails.class).get().getUserName(),
                dataSetAccessor.get(BlogPost.class).get().getTitle(),
                dataSetAccessor.get(BlogPost.class).get().getBody(),
                dataSetAccessor.get(FollowerList.class).get().getFollower(),
                dataSetAccessor.get(PostList.class).get().getPostTitles(),
                dataSetAccessor.get(RecommendedTags.class).get().getTags()
        );
    }
}
