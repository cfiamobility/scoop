package ca.gc.inspection.scoop.postoptionsdialog;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.Toast;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.postcomment.PostComment;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static ca.gc.inspection.scoop.postcomment.PostCommentFragment.startFragmentOrActivity;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * - The PostOptionsDialogFragment is the options menu that appears when the user click the menu button of a post
 * - This is the View for the Post Options Dialog action case
 */
public class PostOptionsDialogFragment extends BottomSheetDialogFragment implements PostOptionsDialogContract.View {

    //UI Declarations
    Button saveButton, shareButton, deleteButton, reportButton, unsaveButton;
    TableRow saveTR, shareTR, deleteTR, reportTR, unsaveTR;

    //reference to the presenter
    private PostOptionsDialogContract.Presenter mPostOptionsDialogPresenter;
    // required as context may be null outside of fragment onCreateView
    private Context currContext;

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
     *
     * @param inflater inflates the layout
     * @param container contains the layout
     * @param savedInstanceState saved state that contains the activity ID of current post that is displaying this fragment
     * @return fragment view of options dialog box
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_post_options, container, false);
        // to get bundle from ProfilePostFragment
        super.onCreate(savedInstanceState);

        //from ProfilePostFragment bundle
        //contains activity id and posterid of the specific post in the Recycler View in which its options menu was clicked
        final String activityid = getArguments().getString("ACTIVITY_ID");
        final String posterid = getArguments().getString("POSTER_ID");
        final String viewHolderType = getArguments().getString("VIEWHOLDER_TYPE");
        final Boolean savedStatus = getArguments().getBoolean("SAVED_STATUS");
        Log.i("POSTER_ID IN DIALOG FRAGMENT", posterid);
        Log.i("CURRENT USER", Config.currentUser);
        Log.i("SAVED STATUS IN DIALOG FRAGMENT", savedStatus.toString());

        setPresenter(new PostOptionsDialogPresenter(this));

        // initializing all of the buttons
        shareButton = view.findViewById(R.id.dialog_post_options_btn_share);
        saveButton = view.findViewById(R.id.dialog_post_options_btn_save);
        unsaveButton = view.findViewById(R.id.dialog_post_options_btn_unsave);
        deleteButton = view.findViewById(R.id.dialog_post_options_btn_delete);
        reportButton = view.findViewById(R.id.dialog_post_options_btn_report);

        // initializing all of the rows
        shareTR = view.findViewById(R.id.dialog_post_options_tr_share);
        saveTR = view.findViewById(R.id.dialog_post_options_tr_save);
        unsaveTR = view.findViewById(R.id.dialog_post_options_tr_unsave);
        deleteTR = view.findViewById(R.id.dialog_post_options_tr_delete);
        reportTR = view.findViewById(R.id.dialog_post_options_tr_report);

        // WILL ALWAYS BE SET - onClick listener for sharing a post
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("BUTTON PRESSED", "Share");
                dismiss();
            }
        });

        if (viewHolderType.contains("Comment")){
            saveTR.setVisibility(View.GONE);
            saveButton.setVisibility(View.GONE);
            unsaveTR.setVisibility(View.GONE);
            unsaveButton.setVisibility(View.GONE);
        }

        // Checks if the post is already saved
        if (savedStatus){
            saveTR.setVisibility(View.GONE);
            saveButton.setVisibility(View.GONE);

            unsaveButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Log.i("BUTTON PRESSED", "Save");
                    Log.i("activity id:", activityid);
                    // store context as a local variable and used as a param in setSaveResponseMessage(String message) method
                    currContext = getContext();
                    mPostOptionsDialogPresenter.savePost(NetworkUtils.getInstance(getContext()), activityid, Config.currentUser);
                    dismiss();
                }
            });
        } else {
            unsaveTR.setVisibility(View.GONE);
            unsaveButton.setVisibility(View.GONE);

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("BUTTON PRESSED", "Save");
                    Log.i("activity id:", activityid);
                    // store context as a local variable and used as a param in setSaveResponseMessage(String message) method
                    currContext = getContext();
                    mPostOptionsDialogPresenter.savePost(NetworkUtils.getInstance(getContext()), activityid, Config.currentUser);
                    dismiss();
                }
            });
        }


        //Checks if the post is the current users or other users and sets features based on this
        if (posterid.equals(Config.currentUser)){
            reportTR.setVisibility(View.GONE);
            reportButton.setVisibility(View.GONE);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("BUTTON PRESSED", "Delete");
                    dismiss();
                }
            });

        } else {
            deleteTR.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);

            reportButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("BUTTON PRESSED", "Report");
                    dismiss();
                }
            });
        }

        return view;
    }


    /**
     * Displays toast message based on the response received from the database
     * @param message Message that is set in the Presenter
     */
    public void setSaveResponseMessage(String message){
        Toast.makeText(currContext,message,Toast.LENGTH_SHORT).show();
    }





}
