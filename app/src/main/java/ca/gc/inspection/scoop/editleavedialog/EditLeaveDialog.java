package ca.gc.inspection.scoop.editleavedialog;


import ca.gc.inspection.scoop.R;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import static ca.gc.inspection.scoop.Config.INTENT_ACTIVITY_ID_KEY;

/**
 * Prompts the user if they want to leave their unsaved edits for a post or comment, thereby losing
 * their changes.
 */
public class EditLeaveDialog extends DialogFragment {
    public static final String TAG = "EditLeaveDialog";
    EditLeaveEventListener mEditLeaveEventListener;
    Button confirmBtn, cancelBtn;
    private String mActivityId;

    /**
     * Used by PostCommentViewHolder to pass the activityId to the EditLeaveDialog through the Bundle.
     *
     * @param activityId    unique identifier of post comment.
     * @return              EditLeaveDialog containing the activityId.
     */
    public static EditLeaveDialog newInstance(String activityId) {
        EditLeaveDialog editLeaveDialog = new EditLeaveDialog();

        // bundle arguments
        Bundle bundle = new Bundle();
        bundle.putString(INTENT_ACTIVITY_ID_KEY, activityId);
        editLeaveDialog.setArguments(bundle);

        return editLeaveDialog;
    }

    public EditLeaveDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, 0);
        if (savedInstanceState != null) {
            mActivityId = savedInstanceState.getString(INTENT_ACTIVITY_ID_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.dialog_lose_edits, container);

        confirmBtn = view.findViewById(R.id.dialog_lose_edits_confirm);
        cancelBtn = view.findViewById(R.id.dialog_lose_edits_cancel);

        confirmBtn.setOnClickListener(v -> {
            if (mEditLeaveEventListener != null) {
                mEditLeaveEventListener.confirmLeaveEvent(mActivityId);
            }
            dismiss();
        });

        cancelBtn.setOnClickListener(v -> {
            if (mEditLeaveEventListener != null) {
                mEditLeaveEventListener.cancelLeaveEvent();
            }
            dismiss();
        });

        return view;
    }

    /**
     * Allows the View layer to provide a reference itself into this class. The reference is
     * required so that the Dialog's options have a callback method.
     *
     * @param editLeaveEventListener    View layer object such as an activity where you can leave
     *                                  unsaved edits from.
     */
    public void setEditLeaveEventListener(
            EditLeaveEventListener editLeaveEventListener) {
        mEditLeaveEventListener = editLeaveEventListener;
    }
}
