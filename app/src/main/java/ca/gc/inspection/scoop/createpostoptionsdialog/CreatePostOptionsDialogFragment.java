package ca.gc.inspection.scoop.createpostoptionsdialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;

import ca.gc.inspection.scoop.CertifiedType;
import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.R;


/**
 * - The PostOptionsDialogFragment is the options menu that appears when the user click the menu button of a post
 * - This is the View for the Post Options Dialog action case
 */
public class CreatePostOptionsDialogFragment extends BottomSheetDialogFragment {

    //UI Declarations
    Button createCommunityPostButton, createOfficialPostBCPButton;
    TableRow createCommunityPostTR, createOfficialPostBCPTR;

    private CreatePostOptionsDialogReceiver.CreateCommunityPostReceiver mCreateCommunityPostReceiver;
    private CreatePostOptionsDialogReceiver.CreateOfficialPostBPCReceiver mCreateOfficialPostBPCReceiver;

    /**
     * Empty Constructor for Fragment
     */
    public CreatePostOptionsDialogFragment(){
    }

    /**
     * Allows the View layer to pass an instance of itself to this class. The receiver provides access to
     * callback methods for clicking different options.
     *
     * @param createCommunityPostReceiver
     */
    public void setCreateCommunityPostReceiver(
            CreatePostOptionsDialogReceiver.CreateCommunityPostReceiver createCommunityPostReceiver) {
        mCreateCommunityPostReceiver = createCommunityPostReceiver;
    }

    /**
     * Allows the View layer to pass an instance of itself to this class. The receiver provides access to
     * callback methods for clicking different options.
     *
     * @param createOfficialPostBPCReceiver
     */
    public void setCreateOfficialPostBPCReceiver(
            CreatePostOptionsDialogReceiver.CreateOfficialPostBPCReceiver createOfficialPostBPCReceiver) {
        mCreateOfficialPostBPCReceiver = createOfficialPostBPCReceiver;
    }


    /**
     * Initializes the buttons and tablerows for the options of the fragment and hides options/sets listeners based on
     * the Config.certifiedType
     * @param inflater inflates the layout
     * @param container contains the layout
     * @param savedInstanceState
     * @return fragment view of options dialog box
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_create_post_options, container, false);
        // to get bundle from ProfilePostFragment
        super.onCreate(savedInstanceState);

        // initializing all of the buttons
        createCommunityPostButton = view.findViewById(R.id.dialog_create_post_btn);
        createOfficialPostBCPButton = view.findViewById(R.id.dialog_create_post_official_bcp_btn);

        // initializing all of the rows
        createCommunityPostTR = view.findViewById(R.id.dialog_create_post_tr);
        createOfficialPostBCPTR = view.findViewById(R.id.dialog_create_post_official_bcp_tr);

        setupCreateCommunityPostOption();

        setupCreateOfficialPostBCPOption();

        return view;
    }

    private void setupCreateCommunityPostOption() {
        createCommunityPostButton.setOnClickListener((View v) -> {
            if (mCreateCommunityPostReceiver != null) {
                mCreateCommunityPostReceiver.createCommunityPost();
            }
            dismiss();
        });
    }

    private void setupCreateOfficialPostBCPOption() {
        if (Config.certifiedType == CertifiedType.BCP) {
            showCreateOfficialPostOption();
            createOfficialPostBCPButton.setOnClickListener((View v) -> {
                if (mCreateOfficialPostBPCReceiver != null) {
                    mCreateOfficialPostBPCReceiver.createOfficialPostBCP();
                }
                dismiss();
            });
        }
        else {
            hideOfficialPostBCPOption();
        }
    }

    private void showCreateOfficialPostOption() {
        if (createOfficialPostBCPButton != null) {
            createOfficialPostBCPButton.setVisibility(View.VISIBLE);
        }
        if (createOfficialPostBCPTR != null) {
            createOfficialPostBCPTR.setVisibility(View.VISIBLE);
        }
    }

    private void hideOfficialPostBCPOption() {
        if (createOfficialPostBCPButton != null) {
            createOfficialPostBCPButton.setVisibility(View.GONE);
        }
        if (createOfficialPostBCPTR != null) {
            createOfficialPostBCPTR.setVisibility(View.GONE);
        }
    }

}
