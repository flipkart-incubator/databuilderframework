package examples.bloghomepagebuilder.builders;

import com.flipkart.databuilderframework.annotations.DataBuilderClassInfo;
import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataBuilderException;
import com.flipkart.databuilderframework.model.Data;
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
