package ca.gc.inspection.scoop.profilecomment;

import ca.gc.inspection.scoop.base.BaseView;
import ca.gc.inspection.scoop.postcomment.PostCommentContract;

/**
 * A contract between the View (layer) and Presenter for the replying to a post
 * action. This contract communicates the how the View and Presenter will
 * interact with each other.
 *
 * Since RecyclerView is being used in this package, we must additionally specify the interaction
 * between sub-Views (Adapter/ViewHolder) and the Presenter (via AdapterAPI/ViewHolderAPI).
 *
 * View = PostCommentFragment - is the main View responsible for creating the Presenter and Adapter
 *      View.Adapter = PostCommentAdapter is the subview responsible for creating ViewHolders
 *      View.ViewHolder = PostCommentViewHolder is the subview which displays an individual profile comment
 *
 * Presenter = PostCommentPresenter - there's only one object which implements:
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

public interface ProfileCommentContract extends PostCommentContract {

    interface View extends BaseView<Presenter> {
        /**
         * Implemented by the main View (ie. PostCommentFragment).
         * Methods specified here in the View but not in the nested View.Adapter and View.ViewHolder interfaces
         * explain how the Presenter is to communicate with the main View only.
         */

        interface Adapter extends PostCommentContract.View.Adapter {
        }

        interface ViewHolder extends PostCommentContract.View.ViewHolder {
            ViewHolder setPostTitle(String postTitle);
        }
    }

    interface Presenter extends PostCommentContract.Presenter {

        interface AdapterAPI extends PostCommentContract.Presenter.AdapterAPI {
            void setAdapter(ProfileCommentContract.View.Adapter adapter);
        }

        interface ViewHolderAPI extends PostCommentContract.Presenter.ViewHolderAPI {
        }
    }
}
