package examples.bloghomepagebuilder.builders;

import io.appform.databuilderframework.annotations.DataBuilderClassInfo;
import io.appform.databuilderframework.engine.DataBuilder;
import io.appform.databuilderframework.engine.DataBuilderContext;
import io.appform.databuilderframework.engine.DataBuilderException;
import io.appform.databuilderframework.model.Data;
import examples.bloghomepagebuilder.data.ApiAuthValid;
import examples.bloghomepagebuilder.data.UserDetails;

@DataBuilderClassInfo(produces = UserDetails.class, consumes = {ApiAuthValid.class})
public class UserDataExpander extends DataBuilder {

    @Override
    public Data process(DataBuilderContext context) throws DataBuilderException {
        ApiAuthValid apiAuthValid = context.getDataSet().accessor().get(ApiAuthValid.class);
        final String userId = apiAuthValid.getDecryptedUserId();
        //Make a DB call and get user details
        return new UserDetails("Santanu Sinha", 777, 0);
    }
}
