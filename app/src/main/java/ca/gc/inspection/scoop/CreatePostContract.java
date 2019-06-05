package ca.gc.inspection.scoop;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

public interface CreatePostContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

        static Map<String, String> getPostParams(String userId, String title, String text, String imageBitmap) {
            Map<String, String>  params = new HashMap<>();
            params.put("userid", userId); // post test user
            params.put("activitytype", Integer.toString(Config.postType));
            params.put("posttitle", title);
            params.put("posttext", text);
            params.put("postimage", imageBitmap);
            return params;
        }

        static Map<String,String> getPostHeaders() {
            // inserting the token into the response header that will be sent to the server
            Map<String, String> header = new HashMap<>();
            header.put("authorization", Config.token);
            return header;
        }
    }
}
