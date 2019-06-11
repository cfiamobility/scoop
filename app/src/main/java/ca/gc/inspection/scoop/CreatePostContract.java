package ca.gc.inspection.scoop;

import java.util.HashMap;
import java.util.Map;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

public interface CreatePostContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

        void sendPostToDatabase(MySingleton singleton, final String userId, final String title, final String text, final String imageBitmap);
    }
}
