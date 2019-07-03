package ca.gc.inspection.scoop.feedpost;

import ca.gc.inspection.scoop.profilepost.ProfilePostContract;

public interface FeedPostContract extends ProfilePostContract {
    /**
     * A contract between the View (layer) and Presenter for the viewing feed post
     * action. This contract communicates the how the View and Presenter will
     * interact with each other.
     *
     * Since RecyclerView is being used in this package, we must additionally specify the interaction
     * between sub-Views (Adapter/ViewHolder) and the Presenter (via AdapterAPI/ViewHolderAPI).
     *
     * View = FeedPostFragment - is the main View responsible for creating the Presenter and Adapter
     *      View.Adapter = FeedPostAdapter is the subview responsible for creating ViewHolders
     *      View.ViewHolder = FeedPostViewHolder is the subview which displays an individual profile comment
     *
     * Presenter = FeedPostPresenter - there's only one object which implements:
     *      Presenter.AdapterAPI - used by the Adapter to call Presenter methods
     *      Presenter.ViewHolderAPI - used by the ViewHolders to call Presenter methods
     *
     * If communication is required between the View layer and Presenter layer, interface methods must be used.
     *      - if the Presenter needs the View and vice-versa, the interface should be passed in.
     * If communication is required within the View only or Presenter only, the object itself should be passed in
     * to avoid leaking access to internal methods in the contract.
     *
     * See PostCommentContract for inheritance hierarchy for Posts/Comments
     */

    interface View extends ProfilePostContract.View {
        /**
         * Implemented by the main View (ie. FeedPostFragment).
         * Methods specified here in the View but not in the nested View.Adapter and View.ViewHolder interfaces
         * explain how the Presenter is to communicate with the main View only.
         */

        String getFeedType();

        interface Adapter extends ProfilePostContract.View.Adapter {
        }

        interface ViewHolder extends ProfilePostContract.View.ViewHolder {
            FeedPostContract.View.ViewHolder setPostImageFromString(String image);
        }
    }


    interface Presenter extends ProfilePostContract.Presenter {

        void loadDataFromDatabase(String feedType);

        interface AdapterAPI extends ProfilePostContract.Presenter.AdapterAPI {
            void setAdapter(FeedPostContract.View.Adapter adapter);
            void onBindViewHolderAtPosition(
                    FeedPostContract.View.ViewHolder postCommentViewHolder, int i);
        }

        interface ViewHolderAPI extends ProfilePostContract.Presenter.ViewHolderAPI {
        }
    }

}

