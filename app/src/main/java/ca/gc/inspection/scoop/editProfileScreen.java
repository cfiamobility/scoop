package ca.gc.inspection.scoop;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

public class editProfileScreen extends AppCompatActivity {

    // method for the back button
    public void finishActivity(View view) {
        finish();
    }

    // when the change profile picture text is tapped
    public void changeProfilePicture (View view) {

        // custom dialog
        Dialog dialog = new Dialog(editProfileScreen.this);
        dialog.setContentView(R.layout.custom_dialog_change_profile_picture);

        // initializing buttons from alert dialog
        Button takePhotoButton = dialog.findViewById(R.id.takePhotoButton);
        Button photoGalleryButton = dialog.findViewById(R.id.photoGalleryButton);

        // so the buttons are shown
        takePhotoButton.setEnabled(true);
        photoGalleryButton.setEnabled(true);

        // show the custom dialog
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // when the soft keyboard is open tapping anywhere else will close the keyboard
        findViewById(R.id.edit_profile_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });
    }
}
