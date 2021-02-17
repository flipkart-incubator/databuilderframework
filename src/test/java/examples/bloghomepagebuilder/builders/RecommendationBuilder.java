package examples.bloghomepagebuilder.builders;

import io.appform.databuilderframework.annotations.DataBuilderClassInfo;
import io.appform.databuilderframework.engine.DataBuilder;
import io.appform.databuilderframework.engine.DataBuilderContext;
import io.appform.databuilderframework.engine.DataBuilderException;
import io.appform.databuilderframework.model.Data;
import com.google.common.collect.ImmutableList;
import examples.bloghomepagebuilder.data.RecommendedTags;
import examples.bloghomepagebuilder.data.UserDetails;

import java.util.List;

@DataBuilderClassInfo(produces = RecommendedTags.class, consumes = UserDetails.class)
public class RecommendationBuilder extends DataBuilder{
    @Override
    public Data process(DataBuilderContext context) throws DataBuilderException {
        return new RecommendedTags(getFollowersForUser(context.getDataSet().accessor().get(UserDetails.class).getUserName()));
    }

    private List<String> getFollowersForUser(final String userId) {
        //Generate reco
        return ImmutableList.of("orchestrator", "dfa", "graph", "java");
    }
}
