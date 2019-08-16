package ca.gc.inspection.scoop.info;

import ca.gc.inspection.scoop.*;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/** Main activity for the info page. This holds all the buttons to navigate through different info
 * Scoop 101 - how to guide on what is "scoopable" - PDF that was cut up to work better for mobile use
 * PDF is from: http://merlin/about-the-cfia/building-for-the-future/how-to-submit-a-scoop/eng/1528132422705/1528132423064
 * Data policy - data policy template from a generator
 * Terms of use - TOS template from a generator
 * Open source lib - list of open source libraries used for the project
 */
public class InfoActivity extends AppCompatActivity {

    // when the back button is pressed
    public void finishActivity(View view) {
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        // set the system status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_dark));

        TextView scoop101 = findViewById(R.id.info_scoop_101);
        scoop101.setOnClickListener(v -> {
            startActivity(new Intent(v.getContext(), InfoScoop101.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        TextView dataPolicy = findViewById(R.id.info_data_policy);
        dataPolicy.setOnClickListener(v -> {
            startActivity(new Intent(v.getContext(), InfoDataPolicy.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        TextView termsOfUse = findViewById(R.id.info_tos);
        termsOfUse.setOnClickListener(v -> {
            startActivity(new Intent(v.getContext(), InfoTOS.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        TextView openSourceLibrary = findViewById(R.id.info_open_source_lib);
        openSourceLibrary.setOnClickListener(v -> {
            startActivity(new Intent(v.getContext(), InfoOpenSourceLibraries.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }
}
