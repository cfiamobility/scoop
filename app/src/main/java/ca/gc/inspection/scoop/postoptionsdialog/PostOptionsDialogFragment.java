package ca.gc.inspection.scoop.postoptionsdialog;

import android.content.Context;
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
import android.widget.Toast;

import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.postcomment.PostComment;
import ca.gc.inspection.scoop.util.NetworkUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


/**
 * - The PostOptionsDialogFragment is the options menu that appears when the user click the menu button of a post
 * - This is the View for the Post Options Dialog action case
 */
public class PostOptionsDialogFragment extends BottomSheetDialogFragment implements PostOptionsDialogContract.View {

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

        setPresenter(new PostOptionsDialogPresenter(this));

        // initializing all of the buttons
        Button saveButton = view.findViewById(R.id.dialog_post_options_btn_save);
        Button shareButton = view.findViewById(R.id.dialog_post_options_btn_share);
        Button deleteButton = view.findViewById(R.id.dialog_post_options_btn_delete);
        Button reportButton = view.findViewById(R.id.dialog_post_options_btn_report);

//        ImageView share = view.findViewById(R.id.dialog_post_options_img_share);
//        share.setVisibility(View.VISIBLE);

        // onClick listener for saving a post
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
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("BUTTON PRESSED", "Share");
                dismiss();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("BUTTON PRESSED", "Delete");
                dismiss();
            }
        });
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("BUTTON PRESSED", "Report");
                dismiss();
            }
        });

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
