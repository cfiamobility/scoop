package ca.gc.inspection.scoop.feedpost;

import android.graphics.Bitmap;

import org.json.JSONException;

import ca.gc.inspection.scoop.profilecomment.ProfileCommentContract;
import ca.gc.inspection.scoop.profilepost.ProfilePostContract;
import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;


public interface FeedPostContract extends ProfilePostContract {

    interface View extends BaseView<Presenter> {
//        void setPostImage(Bitmap image, FeedPostViewHolder holder);
//        void hidePostImage(FeedPostViewHolder holder);
        String getFeedType();

        interface ViewHolder extends ProfilePostContract.View.ViewHolder {
        }
    }


    interface Presenter extends BasePresenter {
        String getFeedType();
        interface AdapterAPI extends ProfilePostContract.Presenter.AdapterAPI {

        }
    }

}

