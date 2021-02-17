package examples.bloghomepagebuilder.builders;

import io.appform.databuilderframework.annotations.DataBuilderClassInfo;
import io.appform.databuilderframework.engine.DataBuilder;
import io.appform.databuilderframework.engine.DataBuilderContext;
import io.appform.databuilderframework.engine.DataBuilderException;
import io.appform.databuilderframework.model.Data;
import com.google.common.collect.ImmutableList;
import examples.bloghomepagebuilder.data.ApiAuthValid;
import examples.bloghomepagebuilder.data.FollowerList;
import examples.bloghomepagebuilder.data.UserDetails;

@DataBuilderClassInfo(produces = FollowerList.class, consumes = {ApiAuthValid.class, UserDetails.class})
public class GetFollowers extends DataBuilder {
    @Override
    public Data process(DataBuilderContext context) throws DataBuilderException {
        UserDetails userDetails = context.getDataSet().accessor().get(UserDetails.class);
        //Do something and get follower list
        return new FollowerList(ImmutableList.of("Gaurav Prasad", "Ajay Singh", "Gokulvanan Velan"));
    }
}
