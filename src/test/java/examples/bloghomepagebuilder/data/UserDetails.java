package examples.bloghomepagebuilder.data;

import com.flipkart.databuilderframework.model.DataAdapter;

public class UserDetails extends DataAdapter<UserDetails> {
    private final String userName;
    private final int accessPermissionsFlags;
    private final long joinDate;

    public UserDetails(String userName, int accessPermissionsFlags, long joinDate) {
        super(UserDetails.class);
        this.userName = userName;
        this.accessPermissionsFlags = accessPermissionsFlags;
        this.joinDate = joinDate;
    }

    public String getUserName() {
        return userName;
    }

    public long getJoinDate() {
        return joinDate;
    }

    public int getAccessPermissionsFlags() {
        return accessPermissionsFlags;
    }
}
