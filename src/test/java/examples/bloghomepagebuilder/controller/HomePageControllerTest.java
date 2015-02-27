package examples.bloghomepagebuilder.controller;

import com.flipkart.databuilderframework.engine.*;
import com.flipkart.databuilderframework.model.DataDelta;
import com.flipkart.databuilderframework.model.DataFlow;
import com.flipkart.databuilderframework.model.DataFlowInstance;
import com.google.common.base.Stopwatch;
import examples.bloghomepagebuilder.builders.*;
import examples.bloghomepagebuilder.data.HomePageRequest;
import examples.bloghomepagebuilder.data.HomePageResponse;
import org.junit.Test;

import java.util.concurrent.Executors;


public class HomePageControllerTest {
    //private final DataFlowExecutor executor = new MultiThreadedDataFlowExecutor(Executors.newFixedThreadPool(10));
    private final DataFlow homePageDataFlow;

    public HomePageControllerTest() throws DataBuilderFrameworkException {
        homePageDataFlow = new DataFlowBuilder()
                                .withDataBuilder(new ApiAuthChecker())
                                .withDataBuilder(new PostListBuilder())
                                .withDataBuilder(new BlogPostSource())
                                .withDataBuilder(new GetFollowers())
                                .withDataBuilder(new LatestBlogSelector())
                                .withDataBuilder(new RecommendationBuilder())
                                .withDataBuilder(new UserDataExpander())
                                .withDataBuilder(new HomePageBuilder())
                                .withTargetData(HomePageResponse.class)
                                .build();
    }

    @Test
    public void testHomePage() throws Exception {
        runHomePageTest(new SimpleDataFlowExecutor());
    }

    @Test
    public void testHomePageMT() throws Exception {
        runHomePageTest(new MultiThreadedDataFlowExecutor(Executors.newFixedThreadPool(10)));
    }

    private void runHomePageTest(DataFlowExecutor executor) throws Exception {
        final HomePageRequest request = new HomePageRequest("2321312312", "2323454", "Blah".getBytes());
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        for(long i = 0; i < 100000; i++) {
            HomePageResponse response = executor.run(
                                            new DataFlowInstance(
                                                    Long.toString(i),
                                                    homePageDataFlow),
                                                    new DataDelta(request))
                                            .get(HomePageResponse.class);
            if(null == response) {
                throw new Exception("WTF!!");
            }
            //System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(response));
        }
        stopwatch.stop();
        System.out.println("Time taken: " + stopwatch.elapsedMillis());
    }
}
