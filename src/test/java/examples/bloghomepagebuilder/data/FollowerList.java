package examples.bloghomepagebuilder.data;

import com.flipkart.databuilderframework.model.DataAdapter;

import java.util.List;

public class FollowerList extends DataAdapter<FollowerList> {
    private List<String> follower;

    public FollowerList(List<String> follower) {
        super(FollowerList.class);
        this.follower = follower;
    }

    public List<String> getFollower() {
        return follower;
    }
}
