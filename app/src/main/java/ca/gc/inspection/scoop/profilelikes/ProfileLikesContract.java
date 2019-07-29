package ca.gc.inspection.scoop.profilelikes;


import ca.gc.inspection.scoop.base.BaseView;
import ca.gc.inspection.scoop.editpost.EditPostData;
import ca.gc.inspection.scoop.profilecomment.ProfileCommentContract;


public interface ProfileLikesContract extends ProfileCommentContract {
    /**
     * A contract between the View (layer) and Presenter for the viewing a profile post
     * action. This contract communicates the how the View and Presenter will
     * interact with each other.
     *
     * Since RecyclerView is being used in this package, we must additionally specify the interaction
     * between sub-Views (Adapter/ViewHolder) and the Presenter (via AdapterAPI/ViewHolderAPI).
     *
     * View = ProfileLikesFragment - is the main View responsible for creating the Presenter and Adapter
     *      View.Adapter = ProfileLikesAdapter is the subview responsible for creating ViewHolders
     *      View.ViewHolder = ProfileLikesViewHolder is the subview which displays an individual profile comment
     *
     * Presenter = ProfileLikesPresenter - there's only one object which implements:
     *      Presenter.AdapterAPI - used by the Adapter to call Presenter methods
     *      Presenter.ViewHolderAPI - used by the ViewHolders to call Presenter methods
     *
     * If communication is required between the View layer and Presenter layer, interface methods must be used.
     *      - if the Presenter needs the View and vice-versa, the interface should be passed in.
     * If communication is required within the View only or Presenter only, the object itself should be passed in
     * to avoid leaking access to internal methods in the contract.
     *
     * ProfileCommentContract is the base contract and is extended by ProfileLikesContract which
     * is further extended by FeedPostContract.
     */

    interface View extends BaseView<Presenter> {
        /**
         * Implemented by the main View (ie. ProfileLikesFragment).
         * Methods specified here in the View but not in the nested View.Adapter and View.ViewHolder interfaces
         * explain how the Presenter is to communicate with the main View only.
         */

        void onLoadedDataFromDatabase();

        interface Adapter extends ProfileCommentContract.View.Adapter {
        }

        interface ViewHolder extends ProfileCommentContract.View.ViewHolder {
            ViewHolder setPostTitle(String postTitle);
            ViewHolder setCommentCount(String commentCount);
        }
    }

    interface Presenter extends ProfileCommentContract.Presenter {
        interface AdapterAPI extends ProfileCommentContract.Presenter.AdapterAPI {
            void setAdapter(ProfileLikesContract.View.Adapter adapter);
            void onBindViewHolderAtPosition(
                    ProfileLikesContract.View.ViewHolder postCommentViewHolder, int i);
        }

        interface ViewHolderAPI extends ProfileCommentContract.Presenter.ViewHolderAPI {
            EditPostData getEditPostData(int i);
        }
    }
}
