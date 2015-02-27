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
                        dataSetAccessor.get(UserDetails.class).getUserName(),
                        dataSetAccessor.get(BlogPost.class).getTitle(),
                        dataSetAccessor.get(BlogPost.class).getBody(),
                        dataSetAccessor.get(FollowerList.class).getFollower(),
                        dataSetAccessor.get(PostList.class).getPostTitles(),
                        dataSetAccessor.get(RecommendedTags.class).getTags()
                    );
    }
}
