package ca.gc.inspection.scoop.displaypost;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.gc.inspection.scoop.Config;

import static ca.gc.inspection.scoop.Config.INTENT_ACTIVITY_ID_KEY;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;
import ca.gc.inspection.scoop.R;
import ca.gc.inspection.scoop.util.NetworkUtils;


public class DisplayPostActivity extends AppCompatActivity implements DisplayPostContract.View {

    private DisplayPostContract.Presenter mDisplayPostPresenter;
    private DisplayPostAdapter mAdapter;

    private String mActivityId;
    private EditText mAddCommentET;
    private Button mAddCommentButton;

    public void goBack (View view) {
        finish();
    }

    @Override
    public void setPresenter(@NonNull DisplayPostContract.Presenter presenter) {
        mDisplayPostPresenter = checkNotNull(presenter);
    }

    public boolean setActivityIdFromIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(INTENT_ACTIVITY_ID_KEY)) {
            mActivityId = intent.getStringExtra(INTENT_ACTIVITY_ID_KEY);
            return true;
        }
        else return false;
    }

    protected DisplayPostContract.Presenter getPresenter() {
        if (mDisplayPostPresenter == null)
            return new DisplayPostPresenter(this, NetworkUtils.getInstance(this));
        return mDisplayPostPresenter;
    }

    protected String getActivityId() {
        return mActivityId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_post);

        if (!setActivityIdFromIntent()) {
            Toast.makeText(getApplicationContext(), "Error displaying post", Toast.LENGTH_SHORT).show();
            finish();
        }

        setPresenter(getPresenter());
//        mDisplayPostPresenter.loadDataFromDatabase(mActivityId);

        // to prevent the soft keyboard from opening when the activity starts
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mAddCommentET = findViewById(R.id.activity_display_post_et_add_comment);

        // set the system status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_dark));

//        mAdapter = new DisplayPostAdapter(this, R.layout.activity_display_post, commentsList);
//        mRecyclerView.setAdapter(mAdapter);
//        setListViewHeightBasedOnChildren(listView); // this must be run after setting the mAdapter

        // when the soft keyboard is open tapping anywhere else will close the keyboard
        findViewById(R.id.activity_display_post_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

        /* Add Post/comment ticket AMD-96
         * This code is part of the add Post/comment ticket.
         * while displaying a Post, a user can add a comment by typing some text on the comment box and pressing the send button
         * this will initiate this code and will add the comment to the database. Upon succession, the comment box will be reset to empty
         * and the comment list will be updated to show the new comment that was just added
         */
        mAddCommentButton = findViewById(R.id.activity_display_post_btn_add_comment);
        mAddCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = mAddCommentET.getText().toString();
                if (commentText.isEmpty()){
                    Toast.makeText(DisplayPostActivity.this, "Please add comment message", Toast.LENGTH_SHORT).show();
                    Log.i("comment", commentText);
                } else {
                    mDisplayPostPresenter.addPostComment(Config.currentUser, commentText, getActivityId());

                    //clear comment box
                    mAddCommentET.getText().clear();

                    //hide keyboard
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
        });
    }

    /**
     * Sets the recycler view
     */
//    @Override
    public void setRecyclerView() {
        // setting up the recycler view
//        mRecyclerView = findViewById(R.id.fragment_community_feed_rv);
//        mRecyclerView.setHasFixedSize(true);
//
//        // setting the layout manager to the recycler view
//        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//
//        // using the custom adapter for the recycler view
//        mAdapter = new FeedPostAdapter(this, (FeedPostContract.Presenter.AdapterAPI) mFeedPostPresenter);
//        mRecyclerView.setAdapter(mAdapter);

    }

    // to set the height of the nested list view in the scroll view
//    public static void setListViewHeightBasedOnChildren (ListView listView) {
//        ListAdapter listAdapter = listView.getAdapter();
//        if (listAdapter == null) {
//            return;
//        }
//
//        int desiredWith = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
//        int totalHeight = 0;
//        View view = null;
//        for (int i = 0; i < listAdapter.getCount(); i++) {
//            view = listAdapter.getView(i, view, listView);
//            if (i == 0) {
//                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWith, ViewGroup.LayoutParams.WRAP_CONTENT));
//            }
//            view.measure(desiredWith, View.MeasureSpec.UNSPECIFIED);
//            totalHeight += view.getMeasuredHeight();
//        }
//
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() -1));
//        listView.setLayoutParams(params);
//    }

//    /** add Post/comment ticket AMD-96
//     * helper function for adding a comment.
//     * This function will clear the comment box, update the list view to load the new comment,
//     * and close the keyboard if it is up.
//     */
//    public void updateCommentList() {
//        // update comments list
//        //TODO: add new comment to commentsList on view Post ticket
//        // possible logic: when a new comment is posted and is added to the database, reload the commentsList
//        mAdapter.notifyDataSetChanged();
//        Log.i("comment list", "updated");
//    }
}
