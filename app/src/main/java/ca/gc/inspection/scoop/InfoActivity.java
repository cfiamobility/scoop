package ca.gc.inspection.scoop;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class InfoActivity extends AppCompatActivity implements InfoContract.View {

    private InfoContract.Presenter mPresenter;

    // when the back button is pressed
    public void finishActivity(View view) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        setPresenter(new InfoPresenter(this));

        // set the system status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_dark));

    }

    @Override
    public void setPresenter(@NonNull InfoContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
