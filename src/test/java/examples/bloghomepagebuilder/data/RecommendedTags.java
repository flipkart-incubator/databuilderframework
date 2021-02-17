package examples.bloghomepagebuilder.data;

import io.appform.databuilderframework.model.DataAdapter;

import java.util.List;

public class RecommendedTags extends DataAdapter<RecommendedTags> {
    private List<String> tags;

    public RecommendedTags(List<String> tags) {
        super(RecommendedTags.class);
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }
}
