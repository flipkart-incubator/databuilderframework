package examples.bloghomepagebuilder.builders;

import io.appform.databuilderframework.annotations.DataBuilderClassInfo;
import io.appform.databuilderframework.engine.DataBuilder;
import io.appform.databuilderframework.engine.DataBuilderContext;
import io.appform.databuilderframework.engine.DataBuilderException;
import io.appform.databuilderframework.model.Data;
import examples.bloghomepagebuilder.data.ApiAuthValid;
import examples.bloghomepagebuilder.data.HomePageRequest;

@DataBuilderClassInfo(produces = ApiAuthValid.class, consumes = HomePageRequest.class)
public class ApiAuthChecker extends DataBuilder {
    @Override
    public Data process(DataBuilderContext context) throws DataBuilderException {
        HomePageRequest request = context.getDataSet().accessor().get(HomePageRequest.class);
        //Do actual validation....
        return new ApiAuthValid(true, request.getUserAuthToken());
    }
}
