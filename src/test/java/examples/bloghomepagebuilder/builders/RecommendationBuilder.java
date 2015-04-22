package examples.bloghomepagebuilder.builders;

import com.flipkart.databuilderframework.annotations.DataBuilderClassInfo;
import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataBuilderException;
import com.flipkart.databuilderframework.model.Data;
import com.google.common.collect.ImmutableList;
import examples.bloghomepagebuilder.data.RecommendedTags;
import examples.bloghomepagebuilder.data.UserDetails;

import java.util.List;

@DataBuilderClassInfo(produces = RecommendedTags.class, consumes = UserDetails.class)
public class RecommendationBuilder extends DataBuilder{
    @Override
    public Data process(DataBuilderContext context) throws DataBuilderException {
        return new RecommendedTags(getFollowersForUser(context.getDataSet().accessor().get(UserDetails.class).get().getUserName()));
    }

    private List<String> getFollowersForUser(final String userId) {
        //Generate reco
        return ImmutableList.of("orchestrator", "dfa", "graph", "java");
    }
}
