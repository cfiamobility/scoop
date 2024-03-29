package ca.gc.inspection.scoop.postoptionsdialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.Toast;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.MainActivity;
import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.postcomment.PostCommentViewHolder;
import ca.gc.inspection.scoop.report.ReportDialogFragment;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static ca.gc.inspection.scoop.Config.INTENT_ACTIVITY_TYPE_KEY;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * - The PostOptionsDialogFragment is the options menu that appears when the user click the menu button of a post
 * - This is the View for the Post Options Dialog action case
 */
public class PostOptionsDialogFragment extends BottomSheetDialogFragment implements PostOptionsDialogContract.View {

    //UI Declarations
    Button editButton, shareButton, deleteButton, reportButton;
    TableRow editTR, shareTR, deleteTR, reportTR;
    ImageView editImage;

    //reference to the presenter
    private PostOptionsDialogContract.Presenter mPostOptionsDialogPresenter;
    // required as context may be null outside of fragment onCreateView
    private PostCommentViewHolder mViewHolder;
    private Context currContext;

    private PostOptionsDialogReceiver.DeleteCommentReceiver mDeleteCommentReceiver; // Interface implemented by DisplayPostFragment (used to refresh view after comment is deleted)
    private PostOptionsDialogReceiver.EditCommentReceiver mEditCommentReceiver;
    private PostOptionsDialogReceiver.EditPostReceiver mEditPostReceiver;

    private int position;

    /**
     * Invoked by the Presenter and stores a reference to itself (Presenter) after being constructed by the View
     * @param presenter Presenter to be associated with the View and accessed later
     */
    public void setPresenter (@NonNull PostOptionsDialogContract.Presenter presenter){
        mPostOptionsDialogPresenter = checkNotNull(presenter);
    }

    /**
     * Empty Constructor for Fragment
     */
    public PostOptionsDialogFragment(){
    }

    /**
     * Invoked by DisplayPostPresenter to pass a reference of itself to this class
     * @param deleteCommentReceiver
     */
    public void setDeleteCommentReceiver(PostOptionsDialogReceiver.DeleteCommentReceiver deleteCommentReceiver) {
        mDeleteCommentReceiver = deleteCommentReceiver;
    }

    /**
     * Allows the View layer to pass an instance of itself to this class. The receiver provides access to
     * callback methods for clicking different options.
     *
     * @param editCommentReceiver   ie. PostCommentViewHolder
     */
    public void setEditCommentReceiver(PostOptionsDialogReceiver.EditCommentReceiver editCommentReceiver) {
        mEditCommentReceiver = editCommentReceiver;
    }

    /**
     * Allows the View layer to pass an instance of itself to this class. The receiver provides access to
     * callback methods for clicking different options.
     *
     * @param editPostReceiver      ie. FeedPostViewHolder
     */
    public void setEditPostReceiver(PostOptionsDialogReceiver.EditPostReceiver editPostReceiver) {
        mEditPostReceiver = editPostReceiver;
    }


    /**
     * Initializes the buttons and tablerows for the options of the fragment and hides options/sets listeners based on
     * the data passed from the savedInstanceState
     * @param inflater inflates the layout
     * @param container contains the layout
     * @param savedInstanceState saved state that contains the activityid, posteride, viewholdertype, and savedstatus of current post that is displaying this fragment
     * @return fragment view of options dialog box
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_post_options, container, false);
        // to get bundle from ProfilePostFragment
        super.onCreate(savedInstanceState);

        //from ProfilePostFragment bundle
        //contains activity id and posterid of the specific post in the Recycler View in which its options menu was clicked
        String activityId = getArguments().getString("ACTIVITY_ID");
        String posterId = getArguments().getString("POSTER_ID");
        position = getArguments().getInt("POST_POSITION");
        String firstPosterId = getArguments().getString("FIRST_POSTER_ID");
        int activityType = getArguments().getInt(INTENT_ACTIVITY_TYPE_KEY);

        setPresenter(new PostOptionsDialogPresenter(this));

        // initializing all of the buttons
        shareButton = view.findViewById(R.id.dialog_post_options_btn_share);
        editButton = view.findViewById(R.id.dialog_post_options_btn_edit);
        deleteButton = view.findViewById(R.id.dialog_post_options_btn_delete);
        reportButton = view.findViewById(R.id.dialog_post_options_btn_report);

        // initializing all of the rows
        shareTR = view.findViewById(R.id.dialog_post_options_tr_share);
        editTR = view.findViewById(R.id.dialog_post_options_tr_edit);
        deleteTR = view.findViewById(R.id.dialog_post_options_tr_delete);
        reportTR = view.findViewById(R.id.dialog_post_options_tr_report);

        // need to initialize all the images
        editImage = view.findViewById(R.id.dialog_post_options_img_edit);

        // WILL ALWAYS BE SET - onClick listener for sharing a post
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("BUTTON PRESSED", "Share");
                dismiss();
            }
        });

        setupEditOption(posterId, activityType, activityId);

        if (getActivity().toString().contains("DisplayPostActivity") && firstPosterId!= null && firstPosterId.equals(Config.currentUser)){
            deleteButton.setVisibility(View.VISIBLE);
            deleteTR.setVisibility(View.VISIBLE);
            reportButton.setVisibility(View.VISIBLE);
            reportTR.setVisibility(View.VISIBLE);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("BUTTON PRESSED", "Delete");
                    currContext = getContext();
                    mPostOptionsDialogPresenter.deletePost(NetworkUtils.getInstance(getContext()), activityId, Config.currentUser);
                    dismiss();
                }
            });

            setReportOnClickListener(activityId, posterId);


            if (posterId.equals(Config.currentUser)){
                reportTR.setVisibility(View.GONE);
                reportButton.setVisibility(View.GONE);
            }

            //Checks if the post is the current users or other users and sets options based on this
            //Delete option for own user's posts/comments
            // Report option for other user's posts/comments
        } else if (posterId.equals(Config.currentUser)){
            reportTR.setVisibility(View.GONE);
            reportButton.setVisibility(View.GONE);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("BUTTON PRESSED", "Delete");
                    currContext = getContext();
                    mPostOptionsDialogPresenter.deletePost(NetworkUtils.getInstance(getContext()), activityId, Config.currentUser);

                    //getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());

                    dismiss();
                }
            });

        } else {
            deleteTR.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
            setReportOnClickListener(activityId, posterId);
        }

        return view;
    }

    /**
     * Shows or hides the edit option depending on where the dialog was opened from.
     * Sets the OnClickListener for the edit option.
     * @param posterId      userId of the user who created the post/comment which was clicked
     * @param activityType  Post or comment
     * @param activityId    Unique identifier of a post/comment
     */
    private void setupEditOption(String posterId, int activityType, String activityId) {
        if (posterId != null && posterId.equals(Config.currentUser) && !inProfileLikes(activityType)) {
            showEditOption();
            editTR.setOnClickListener(getEditOptionOnClickListener(activityType, activityId));
            editImage.setOnClickListener(getEditOptionOnClickListener(activityType, activityId));
            editButton.setOnClickListener(getEditOptionOnClickListener(activityType, activityId));
        }
        else {
            hideEditOption();
        }
    }

    /**
     * Helper method for setupEditOption. Provides the OnClickListener for the edit option.
     * @param activityType  Post or comment
     * @param activityId    Unique identifier of a post/comment
     * @return
     */
    public View.OnClickListener getEditOptionOnClickListener(int activityType, String activityId) {
        return v -> {
            if (activityType == Config.postType) {
                mEditPostReceiver.onEditPost(position);
            }
            else {
                mEditCommentReceiver.onEditComment(position, activityId);
            }
            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction().remove(PostOptionsDialogFragment.this).commit();
            }
        };
    }

    private boolean inProfileLikes(int activityType) {
        return (activityType == Config.commentType && getActivity().getClass().equals(MainActivity.class));
    }

    private void showEditOption() {
        editTR.setVisibility(View.VISIBLE);
        editButton.setVisibility(View.VISIBLE);
    }

    private void hideEditOption() {
        editTR.setVisibility(View.GONE);
        editButton.setVisibility(View.GONE);
    }


    /**
     * Displays toast message based on the response received from the database
     * @param message Message that is set in the Presenter
     */
    public void setDeleteResponseMessage(String message) {
        Toast.makeText(currContext,message,Toast.LENGTH_SHORT).show();
    }


    /**
     * This setter method is used for the purposes of referencing the respective viewholder with a PostOptionsDialogFragment
     * Allows access to setting the viewholders savedStatus variable for future access to update the UI when a post is saved
     * Also type of viewHolder is needed for differing between comments and posts
     * @param viewHolder from adapter that sets a PostOptionsListener
     */
    public void setViewHolder(PostCommentViewHolder viewHolder){
        mViewHolder = viewHolder;
    }


    private void setReportOnClickListener(String activityId, String posterId) {
        reportButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            ReportDialogFragment dialog = new ReportDialogFragment();

            bundle.putString("ACTIVITY_ID", activityId);
            bundle.putString("POSTER_ID", posterId);
            dialog.setArguments(bundle);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            dialog.show(ft, "ReportDialogFragment");
            Log.i("BUTTON PRESSED", "Report");
            dismiss();
        });
    }

    /**
     * Refreshes view which implements the DeleteCommentReceiver
     */
    public void refresh(){
        boolean isPost;
        if (position == 0){
            isPost = true;
        }
        else{
            isPost = false;
        }
        mDeleteCommentReceiver.onDeletePostComment(isPost);
    }
}
