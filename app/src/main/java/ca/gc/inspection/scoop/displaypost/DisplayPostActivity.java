package ca.gc.inspection.scoop.displaypost;

import ca.gc.inspection.scoop.*;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.gc.inspection.scoop.CommentController;
import ca.gc.inspection.scoop.Comments;
import ca.gc.inspection.scoop.Config;
import ca.gc.inspection.scoop.PostOptionsDialog;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


public class DisplayPostActivity extends AppCompatActivity implements DisplayPostContract.View {

    private DisplayPostContract.Presenter mPresenter;

    private ListView listView;
    private ImageView optionsMenu;
    private EditText addComment;
    private static DisplayPostCommentsAdapter adapter;

    /** add Post/comment ticket AMD-96
     * helper function for adding a comment.
     * This function will clear the comment box, update the list view to load the new comment,
     * and close the keyboard if it is up.
     */
    public static void updateCommentList() {
        // update comments list
        //TODO: add new comment to commentsList on view Post ticket
        // possible logic: when a new comment is posted and is added to the database, reload the commentsList
        adapter.notifyDataSetChanged();
        Log.i("comment list", "updated");
    }

    public void goBack (View view) {
        finish();
    }

    @Override
    public void setPresenter(@NonNull DisplayPostContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_post);

        setPresenter(new DisplayPostPresenter(this));

        // to prevent the soft keyboard from opening when the activity starts
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        addComment = findViewById(R.id.activity_display_post_et_add_comment);

        listView = findViewById(R.id.activity_display_post_lv_comments);

        optionsMenu = findViewById(R.id.activity_display_post_img_options);

        // set the system status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_dark));

        List<Comments> commentsList = new ArrayList<>();

        // test array list of comments for the custom adapter
        Comments comment2 = new Comments();
        comment2.setCommenterName("Timmy");
        comment2.setCommentText("Hello");
        comment2.setTimeStamp("20min");
        commentsList.add(comment2);

        Comments comment1 = new Comments();
        comment1.setCommenterName("Edison");
        comment1.setCommentText("Sup");
        comment1.setTimeStamp("15min");
        commentsList.add(comment1);

        adapter = new DisplayPostCommentsAdapter(this, R.layout.item_display_post_comments, commentsList);
        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView); // this must be run after setting the adapter

        // when the soft keyboard is open tapping anywhere else will close the keyboard
        findViewById(R.id.activity_display_post_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

        optionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostOptionsDialog bottomSheetDialog = new PostOptionsDialog();
                final Context context = v.getContext();
                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                bottomSheetDialog.show(fragmentManager, "bottomSheet");
            }
        });

        /** Add Post/comment ticket AMD-96
         * This code is part of the add Post/comment ticket.
         * while displaying a Post, a user can add a comment by typing some text on the comment box and pressing the send button
         * this will initiate this code and will add the comment to the database. Upon succession, the comment box will be reset to empty
         * and the comment list will be updated to show the new comment that was just added
         */
        Button sendComment = findViewById(R.id.activity_display_post_btn_add_comment);
        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = addComment.getText().toString();
                if (comment.isEmpty()){
                    Toast.makeText(DisplayPostActivity.this, "Please add comment message", Toast.LENGTH_SHORT).show();
                    Log.i("comment", comment);
                } else {
                    String tempOtherPostActivityId = "e6a40eb0-bd3f-42ec-8b8d-dd28112260be"; //TODO send real Post activityID when doing viewPost ticket
                    CommentController.sendCommentToDatabase(getApplicationContext(), Config.currentUser, comment, tempOtherPostActivityId);

                    //clear comment box
                    addComment.getText().clear();

                    //hide keyboard
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
        });
    }

    // to set the height of the nested list view in the scroll view
    public static void setListViewHeightBasedOnChildren (ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int desiredWith = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) {
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWith, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            view.measure(desiredWith, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() -1));
        listView.setLayoutParams(params);
    }
}
