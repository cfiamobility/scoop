package ca.gc.inspection.scoop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class displayPost extends AppCompatActivity {

    private ListView listView;

    public void goBack (View view) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        // to prevent the soft keyboard from opening when the activity starts
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        listView = findViewById(R.id.commentListView);

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

        listView.setAdapter(new postCommentsAdapter(this, R.layout.custom_viewing_post_comments, commentsList));
        setListViewHeightBasedOnChildren(listView); // this must be run after setting the adapter

        // when the soft keyboard is open tapping anywhere else will close the keyboard
        findViewById(R.id.viewingPostLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
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
