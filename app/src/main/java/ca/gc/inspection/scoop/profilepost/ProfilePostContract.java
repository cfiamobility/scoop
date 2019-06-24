package ca.gc.inspection.scoop.profilepost;

import ca.gc.inspection.scoop.profilecomment.ProfileCommentContract;

public interface ProfilePostContract extends ProfileCommentContract {
    /**
     * A contract between the View (layer) and Presenter for the viewing a profile post
     * action. This contract communicates the how the View and Presenter will
     * interact with each other.
     *
     * Since RecyclerView is being used in this package, we must additionally specify the interaction
     * between sub-Views (Adapter/ViewHolder) and the Presenter (via AdapterAPI/ViewHolderAPI).
     *
     * View = ProfilePostFragment - is the main View responsible for creating the Presenter and Adapter
     *      View.Adapter = ProfilePostAdapter is the subview responsible for creating ViewHolders
     *      View.ViewHolder = ProfilePostViewHolder is the subview which displays an individual profile comment
     *
     * Presenter = ProfilePostPresenter - there's only one object which implements:
     *      Presenter.AdapterAPI - used by the Adapter to call Presenter methods
     *      Presenter.ViewHolderAPI - used by the ViewHolders to call Presenter methods
     *
     * If communication is required between the View layer and Presenter layer, interface methods must be used.
     *      - if the Presenter needs the View and vice-versa, the interface should be passed in.
     * If communication is required within the View only or Presenter only, the object itself should be passed in
     * to avoid leaking access to internal methods in the contract.
     *
     * ProfileCommentContract is the base contract and is extended by ProfilePostContract which
     * is further extended by FeedPostContract.
     */

    interface View extends ProfileCommentContract.View {
        /**
         * Implemented by the main View (ie. ProfilePostFragment).
         * Methods specified here in the View but not in the nested View.Adapter and View.ViewHolder interfaces
         * explain how the Presenter is to communicate with the main View only.
         */

        interface Adapter extends ProfileCommentContract.View.Adapter {
        }

        interface ViewHolder extends ProfileCommentContract.View.ViewHolder {
            ViewHolder setPostTitle(String postTitle);
            ViewHolder setCommentCount(String commentCount);
        }
    }

    interface Presenter extends ProfileCommentContract.Presenter {
        interface AdapterAPI extends ProfileCommentContract.Presenter.AdapterAPI {
            void setAdapter(ProfilePostContract.View.Adapter adapter);
        }

        interface ViewHolderAPI extends ProfileCommentContract.Presenter.ViewHolderAPI {
        }
    }
}