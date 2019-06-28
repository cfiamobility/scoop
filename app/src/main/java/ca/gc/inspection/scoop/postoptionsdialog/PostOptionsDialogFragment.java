package ca.gc.inspection.scoop.postoptionsdialog;

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

import ca.gc.inspection.scoop.R;

public class PostOptionsDialogFragment extends BottomSheetDialogFragment {

    private PostOptionsDialogPresenter mPostOptionsDialogPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_post_options, container, false);

        // initializing all of the buttons
        Button saveButton = view.findViewById(R.id.dialog_post_options_btn_save);
        Button shareButton = view.findViewById(R.id.dialog_post_options_btn_share);
        Button deleteButton = view.findViewById(R.id.dialog_post_options_btn_delete);
        Button reportButton = view.findViewById(R.id.dialog_post_options_btn_report);

        ImageView share = view.findViewById(R.id.dialog_post_options_img_share);
        share.setVisibility(View.VISIBLE);

        // onClick listeners for all the buttons
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("BUTTON PRESSED", "Save");
                mPostOptionsDialogPresenter.savePost();


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

}
