package examples.bloghomepagebuilder.builders;

import com.flipkart.databuilderframework.annotations.DataBuilderClassInfo;
import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataBuilderException;
import com.flipkart.databuilderframework.model.Data;
import examples.bloghomepagebuilder.data.ApiAuthValid;
import examples.bloghomepagebuilder.data.UserDetails;

@DataBuilderClassInfo(produces = UserDetails.class, consumes = {ApiAuthValid.class})
public class UserDataExpander extends DataBuilder {

    @Override
    public Data process(DataBuilderContext context) throws DataBuilderException {
        ApiAuthValid apiAuthValid = context.getDataSet().accessor().get(ApiAuthValid.class).get();
        final String userId = apiAuthValid.getDecryptedUserId();
        //Make a DB call and get user details
        return new UserDetails("Santanu Sinha", 777, 0);
    }
}
