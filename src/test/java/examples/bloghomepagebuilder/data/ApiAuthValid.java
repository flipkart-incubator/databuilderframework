package examples.bloghomepagebuilder.data;

import com.flipkart.databuilderframework.model.DataAdapter;

public class ApiAuthValid extends DataAdapter<ApiAuthValid> {
    private final boolean valid;
    private final String decryptedUserId;

    public ApiAuthValid(boolean valid, String decryptedUserId) {
        super(ApiAuthValid.class);
        this.valid = valid;
        this.decryptedUserId = decryptedUserId;
    }

    public boolean isValid() {
        return valid;
    }

    public String getDecryptedUserId() {
        return decryptedUserId;
    }
}
