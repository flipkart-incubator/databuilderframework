package examples.bloghomepagebuilder.builders;

import com.flipkart.databuilderframework.annotations.DataBuilderClassInfo;
import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataBuilderException;
import com.flipkart.databuilderframework.model.Data;
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
