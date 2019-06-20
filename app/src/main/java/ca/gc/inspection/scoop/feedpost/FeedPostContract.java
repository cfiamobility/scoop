package ca.gc.inspection.scoop.feedpost;

import android.graphics.Bitmap;

import org.json.JSONException;

import ca.gc.inspection.scoop.MySingleton;
import ca.gc.inspection.scoop.profilecomment.ProfileCommentContract;
import ca.gc.inspection.scoop.profilepost.ProfilePostContract;
import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;


public interface FeedPostContract extends ProfilePostContract {

    interface View extends ProfilePostContract.View {
        String getFeedType();

        interface Adapter extends ProfilePostContract.View.Adapter {
        }

        interface ViewHolder extends ProfilePostContract.View.ViewHolder {
            FeedPostContract.View.ViewHolder setPostImageFromString(String image);
        }
    }


    interface Presenter extends ProfilePostContract.Presenter {

        interface AdapterAPI extends ProfilePostContract.Presenter.AdapterAPI {
            void setAdapter(FeedPostContract.View.Adapter adapter);
        }

        interface ViewHolderAPI extends ProfilePostContract.Presenter.ViewHolderAPI {
        }
    }

}

