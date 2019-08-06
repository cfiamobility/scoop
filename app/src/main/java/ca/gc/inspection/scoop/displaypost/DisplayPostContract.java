package ca.gc.inspection.scoop.displaypost;

import ca.gc.inspection.scoop.base.BasePresenter;
import ca.gc.inspection.scoop.base.BaseView;
import ca.gc.inspection.scoop.editcomment.EditCommentContract;
import ca.gc.inspection.scoop.editleavedialog.EditLeaveEventListener;
import ca.gc.inspection.scoop.feedpost.FeedPostContract;
import ca.gc.inspection.scoop.postcomment.PostCommentContract;

public interface DisplayPostContract extends FeedPostContract {
    /**
     * A contract between the View (layer) and Presenter for the viewing feed post
     * action. This contract communicates the how the View and Presenter will
     * interact with each other.
     *
     * Since RecyclerView is being used in this package, we must additionally specify the interaction
     * between sub-Views (Adapter/ViewHolder) and the Presenter (via AdapterAPI/ViewHolderAPI).
     *
     * The contract is similar to FeedPostContract, except we extend it to include the Activity as the outer View.
     * The interaction between the Fragment and Presenter is now within View.Fragment and Presenter.FragmentAPI.
     * This is nested within the interaction between the Activity and Presenter, as specified in the View and Presenter
     * interfaces.
     *
     * View = DisplayPostActivity - is the main View responsible for creating the Presenter and Fragment
     *      View.Fragment = DisplayPostFragment - the View responsible for creating the Adapter
     *          View.Fragment.Adapter = DisplayPostAdapter is the subview responsible for creating ViewHolders
     *          View.Fragment.PostViewHolder = DisplayPostViewHolder is the subview which displays the post
     *          View.Fragment.PostCommentViewHolder = DisplayPostCommentViewHolder is the subview which displays a post comment
     *
     * Presenter = DisplayPostPresenter - there's only one object which implements:
     *      Presenter.FragmentAPI - used by the Fragment to call Presenter methods
     *          Presenter.FragmentAPI.AdapterAPI - used by the Adapter to call Presenter methods
     *          Presenter.FragmentAPI.PostViewHolderAPI - used by the PostViewHolder to call Presenter methods
     *          Presenter.FragmentAPI.PostCommentViewHolderAPI - used by the PostCommentViewHolders to call Presenter methods
     *
     * If communication is required between the View layer and Presenter layer, interface methods must be used.
     *      - if the Presenter needs the View and vice-versa, the interface should be passed in.
     * If communication is required within the View only or Presenter only, the object itself should be passed in
     * to avoid leaking access to internal methods in the contract.
     *
     * See PostCommentContract for inheritance hierarchy for Posts/Comments
     */

    interface View extends BaseView<Presenter> {

        void onAddPostComment(boolean success);

        interface Fragment extends BaseView<Presenter.FragmentAPI> {

            void onLoadedDataFromDatabase();

            interface Adapter extends FeedPostContract.View.Adapter {
            }
        }
    }

    interface Presenter extends EditCommentContract.Presenter, EditLeaveEventListener.Presenter {

        void addPostComment(String currentUserId, String commentText, String activityId);

        interface FragmentAPI extends FeedPostContract.Presenter {
            void setFragmentView(DisplayPostContract.View.Fragment fragmentView);
            void loadDataFromDatabase(String activityId);

            interface AdapterAPI extends FeedPostContract.Presenter.AdapterAPI {
                void setAdapter(DisplayPostContract.View.Fragment.Adapter adapter);
                // Inherited onBindViewHolderAtPosition is overridden here to show that there are 2 different onBind methods:
                // One for the post and another for the comments on the post
                void onBindViewHolderAtPosition(
                        PostCommentContract.View.ViewHolder postCommentViewHolder, int i);
                void onBindViewHolder(
                        FeedPostContract.View.ViewHolder feedPostViewHolder);
            }
        }
    }
}
