package ca.gc.inspection.scoop.notif;

import android.graphics.Bitmap;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;

public interface NotificationsContract {

    /**
     * interface to be implemented by the NotificationsFragment class
     */
    interface View extends BaseView<NotificationsContract.Presenter> {
        void onLoadedDataFromDatabase();

        void showNoNotifications();
        void hideNoNotifications();
        void showTodaySection();
        void hideTodaySection();
        void showRecentSection();
        void hideRecentSection();
//        void hideLoadingPanel();
        void requestTodayFocus();
        void requestRecentFocus();

        interface ViewHolder {
//            PostCommentContract.View.ViewHolder setPostText(String postText);
//            PostCommentContract.View.ViewHolder setPostTextWithFormat(String postText, TextFormat textFormat);
//            PostCommentContract.View.ViewHolder setUserName(String userName);
//            PostCommentContract.View.ViewHolder setLikeCount(String likeCount);
//            PostCommentContract.View.ViewHolder setDate(String date);
//            PostCommentContract.View.ViewHolder setLikeState(LikeState likeState);
//            PostCommentContract.View.ViewHolder setUserImageFromString(String image);
//            PostCommentContract.View.ViewHolder hideDate();
//            PostCommentContract.View.ViewHolder setSavedState(Boolean savedState);


            NotificationsContract.View.ViewHolder setActionType(String actionType);
            NotificationsContract.View.ViewHolder setActivityType(String activityType);
            NotificationsContract.View.ViewHolder setTime(String time);
            NotificationsContract.View.ViewHolder hideTime() ;
            NotificationsContract.View.ViewHolder setFullName(String fullName);
            NotificationsContract.View.ViewHolder setImage(Bitmap bitmap);
            NotificationsContract.View.ViewHolder hideImage();
            NotificationsContract.View.ViewHolder setUserImageFromString(String image);
        }

        interface Adapter {
            void refreshAdapter();
        }
    }
    /**
     * interface to be implemented by the NotificationsPresenter class
     */
    interface Presenter extends BasePresenter {
        void loadDataFromDatabase();

        interface AdapterAPI {
            void setAdapter(NotificationsContract.View.Adapter adapter);
            void onBindViewHolderAtPosition(
                    NotificationsContract.View.ViewHolder notificationsViewHolder, int i);
            int getItemCount();
        }

        interface ViewHolderAPI {
        }

    }
}



