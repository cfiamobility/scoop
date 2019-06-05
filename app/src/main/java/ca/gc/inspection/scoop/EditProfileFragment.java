package ca.gc.inspection.scoop;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static ca.gc.inspection.scoop.util.StringUtils.capFirstLetter;

public class EditProfileFragment extends Fragment implements EditProfileContract.View, AdapterView.OnItemSelectedListener {

    private EditProfileContract.Presenter mPresenter;
    private EditProfileActivity editProfileActivity;

    // UI Declarations
    static AutoCompleteTextView positionET, buildingET, divisionET;
    static EditText firstNameET, lastNameET, cityET, linkedinET, twitterET, facebookET, instagramET;
    static Spinner provinceSpinner;
    static TextView changeProfilePicBTN;
    static CircleImageView previewProfilePic;
    static CircleImageView profilePreview;
    Button saveBTN;

    // UI Support Declarations
    static ArrayAdapter<CharSequence> spinnerAdapter;
    static ArrayAdapter<String> buildingAdapter, positionAdapter, divisionAdapter;

    @Override
    public void setPresenter(EditProfileContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public static EditProfileFragment newInstance() {
        return new EditProfileFragment();
    }

    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        editProfileActivity = (EditProfileActivity) getActivity();
        assert (editProfileActivity != null);

        // EditText Definitions
        firstNameET = root.findViewById(R.id.activity_edit_profile_et_first_name);
        lastNameET = root.findViewById(R.id.activity_edit_profile_et_last_name);
        positionET = root.findViewById(R.id.activity_edit_profile_et_position);
        divisionET = root.findViewById(R.id.activity_edit_profile_et_division);
        buildingET = root.findViewById(R.id.activity_edit_profile_et_building);
        cityET = root.findViewById(R.id.activity_edit_profile_et_city);
        provinceSpinner = root.findViewById(R.id.activity_edit_profile_spinner_provinces);
        linkedinET = root.findViewById(R.id.activity_edit_profile_et_linkedin);
        twitterET = root.findViewById(R.id.activity_edit_profile_et_twitter);
        facebookET = root.findViewById(R.id.activity_edit_profile_et_facebook);
        instagramET = root.findViewById(R.id.activity_edit_profile_et_instagram);
        profilePreview = root.findViewById(R.id.activity_create_post_img_profile);

        // Button Definition
        saveBTN = root.findViewById(R.id.activity_edit_profile_btn_save);
        saveBTN.setOnClickListener(save);

        // TextView Definition
        changeProfilePicBTN = root.findViewById(R.id.activity_edit_profile_txt_change_profile_picture);

        // ImageView Definition
        previewProfilePic = root.findViewById(R.id.activity_edit_profile_img_profile);

        // Province Spinner Definition
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.provinces_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinceSpinner.setAdapter(spinnerAdapter);
        provinceSpinner.setOnItemSelectedListener(this);

        return root;
    }

    public void setImageBitmap(Bitmap bitmap) {
        previewProfilePic.setImageBitmap(bitmap);
    }

    // Mandatory Methods for AdapterView.OnItemSelected - OnNothingSelected is Default Spinner Selection set to AB
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Default selection
        provinceSpinner.setSelection(0);
    }

    // The method that runs when save is pressed
    private View.OnClickListener save = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // changes the bitmap into a string encoded using base64
            String image = MyCamera.bitmapToString(bitmap);

            // putting all the edittext fields into a hashmap to be passed into nodejs
            if (!(firstNameET.getText().toString().isEmpty()) && !(lastNameET.getText().toString().isEmpty())) {
                Map<String, String> params = new HashMap<>();
                params.put("userid", userID);
                params.put("firstname", firstNameET.getText().toString());
                params.put("lastname", lastNameET.getText().toString());
                params.put("position", positionET.getText().toString());
                params.put("division", divisionET.getText().toString());
                params.put("building", buildingET.getText().toString());
                params.put("linkedin", linkedinET.getText().toString());
                params.put("twitter", twitterET.getText().toString());
                params.put("instagram", instagramET.getText().toString());
                params.put("facebook", facebookET.getText().toString());
                params.put("city", cityET.getText().toString());
                params.put("province", provinceSpinner.getSelectedItem().toString());
                params.put("image", image);
                EditUserController.updateUserInfo(editProfileActivity, params);
            } else {
                editProfileActivity.makeToast(getResources().getString(R.string.invalidNameEntry), Toast.LENGTH_SHORT);
            }
        }
    };

    // on item click listener for the office address edittext
    private AdapterView.OnItemClickListener autoItemSelectedListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // getting the city and province texts set from the arraylist
            cityText = cityAL.get(position);
            provinceText = provinceAL.get(position);

            // Setting the edit text and spinner
            cityET.setText(cityText);
            int spinnerSelection = spinnerAdapter.getPosition(provinceText);
            provinceSpinner.setSelection(spinnerSelection);
        }
    };

    // Method for to setup the front end of autocomplete text view for positions
    public static void positionAutoSetup(JSONArray response, Context context) {
        try {
            // Position map/arraylist redefined every time text is changed
            positionObjects = new HashMap<>();
            positionAutoComplete = new ArrayList<>(); // Arraylist used for setting in the array adapter

            // loops through every object
            for (int i = 0; i < response.length(); i++) {
                // Gathers info from the object - positionid and positionname
                String positionid = response.getJSONObject(i).getString("positionid");
                String positionname = response.getJSONObject(i).getString("positionname");
                Log.i("position", positionname);
                // places the objects into the map/arraylis
                positionObjects.put(positionid, positionname);
                positionAutoComplete.add(positionname);
            }

            //array adapter to set the autocomplete menu
            positionAdapter = new ArrayAdapter<>(context.getApplicationContext(), android.R.layout.simple_dropdown_item_1line, positionAutoComplete);
            positionET.setAdapter(positionAdapter);
            // Text changed when finished;
            positionText = positionET.getText().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method for to setup the front end of autocomplete text view for building / city / province
    public static void addressAutoSetup(JSONArray response, Context context) {
        try {
            // map/arraylists redefined everytime to clear it
            buildingsObjects = new HashMap<>();
            buildingsAutoComplete = new ArrayList<>();
            cityAL = new ArrayList<>();
            provinceAL = new ArrayList<>();

            // loops through the object
            for (int i = 0; i < response.length(); i++) {
                // gathering info from the object
                String buildingid = response.getJSONObject(i).getString("buildingid");
                String buildingaddress = response.getJSONObject(i).getString("address");
                String buildingcity = response.getJSONObject(i).getString("city");
                String buildingprovince = response.getJSONObject(i).getString("province");
                // placing the info into variables
                buildingsObjects.put(buildingid, buildingaddress);
                buildingsAutoComplete.add(buildingaddress);
                cityAL.add(buildingcity);
                provinceAL.add(buildingprovince);
            }

            // Setting the adapter
            buildingAdapter = new ArrayAdapter<>(context.getApplicationContext(), android.R.layout.simple_dropdown_item_1line, buildingsAutoComplete);
            buildingET.setAdapter(buildingAdapter);
            buildingText = buildingET.getText().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method for to setup the front end of autocomplete text view for divisions
    public static void divisionAutoSetup(JSONArray response, Context context) {
        try {
            // map/arraylists redefined every time to clear it
            divisionsObjects = new HashMap<>();
            divisionsAutoComplete = new ArrayList<>();

            // Loops throught the JSON Array
            for (int i = 0; i < response.length(); i++) {
                // getting the info from the array
                String divisionid = response.getJSONObject(i).getString("divisionid");
                String divisionname = response.getJSONObject(i).getString("division_en");
                // setting the info into variables
                divisionsObjects.put(divisionid, divisionname);
                divisionsAutoComplete.add(divisionname);
            }

            // Setting up the adapter
            divisionAdapter = new ArrayAdapter<>(context.getApplicationContext(), android.R.layout.simple_dropdown_item_1line, divisionsAutoComplete);
            divisionET.setAdapter(divisionAdapter);
            divisionText = divisionET.getText().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to fill the existing boxes with information in the database
    // Runs when edit profile is clicked

    /**
     * @param response
     */
    public static void initialFill(JSONObject response) {
        try {
            // If the user id was correct/matching
            if (response.get("userid").toString().equals(userID)) {
                // Name gets and sets
                firstNameText = response.get("firstname").toString();
                lastNameText = response.get("lastname").toString();
                firstNameET.setText(firstNameText);
                lastNameET.setText(lastNameText);

                // Profile picture setting and gtting
                bitmap = MyCamera.stringToBitmap(response.get("profileimage").toString());
                previewProfilePic.setImageBitmap(bitmap);

                // If user has already inputted a position
                if (!response.get("positionid").toString().equals("null")) {
                    positionText = response.get("positionname").toString();
                    positionET.setText(positionText);
                }
                // If user has already inputted a division
                if (!response.get("divisionid").toString().equals("null")) {
                    divisionText = response.get("division_en").toString();
                    divisionET.setText(divisionText);
                }// If user has already inputted an office address
                if (!response.get("buildingid").toString().equals("null")) {
                    buildingText = response.get("address").toString();
                    buildingET.setText(buildingText);
                }
                // If user has already inputted an office address
                if (!response.get("city").toString().equals("null")) {
                    cityText = response.get("city").toString();
                    cityET.setText(cityText);
                }
                // If user has already inputted an office address
                if (!response.get("province").toString().equals("null")) {
                    provinceText = response.get("province").toString();
                    int spinnerSelection = spinnerAdapter.getPosition(provinceText);
                    provinceSpinner.setSelection(spinnerSelection);
                }
                // If the user has already inputted a facebook url
                if (!response.get("facebook").toString().equals("null")) {
                    facebookText = response.get("facebook").toString();
                    facebookET.setText(facebookText);
                }
                // if the user has already inputted a twitter url
                if (!response.get("twitter").toString().equals("null")) {
                    twitterText = response.get("twitter").toString();
                    twitterET.setText(twitterText);
                }
                // If the user has alreaddy inputted a linkedin url
                if (!response.get("linkedin").toString().equals("null")) {
                    linkedinText = response.get("linkedin").toString();
                    linkedinET.setText(linkedinText);
                }
                // If the user has already inputted an instagram url
                if (!response.get("instagram").toString().equals("null")) {
                    instagramText = response.get("instagram").toString();
                    instagramET.setText(instagramText);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to establish/set up the auto complete text views for position, division and building
    private void autoComplete() {

        // Autocomplete for position edittext
        positionET.addTextChangedListener(new TextWatcher() {
            // Mandatory Method
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            // Runs whenever the text is changed
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Only submits request if the entered text is >= 3 characters for specificity
                if (positionET.getText().length() >= 2) {
                    // Text inputted converted to have a cap letter to start ie. "start" -> "Start"
                    String positionChanged = positionET.getText().toString();
                    String positionChangedCapped = capFirstLetter(positionChanged);
                    EditUserController.positionAutoComplete(getApplicationContext(), positionChangedCapped);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // extra measure to ensure text variable is set when the user is done typing
                positionText = positionET.getText().toString();
            }
        });


        // Autocomplete for office address text
        buildingET.addTextChangedListener(new TextWatcher() {
            // Mandatory method
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            // Runs everytime the edit text is changed
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // Only runs when there is >= 1 character(S) in the edittext
                if (buildingET.getText().length() >= 2) {
                    // Text inputted converted to have a cap letter to start ie. "start" -> "Start"
                    String addressChanged = buildingET.getText().toString();
                    String addressChangedCapped = capFirstLetter(addressChanged);
                    EditUserController.addressAutoComplete(getApplicationContext(), addressChangedCapped);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Extra measure to ensure the office address is set
                buildingText = buildingET.getText().toString();
            }
        });
        // Setting the onitemclicklistener - if a suggestion is clicked, it fills in the city and province inputs
        buildingET.setOnItemClickListener(autoItemSelectedListener);

        // Autocomplete for the division edittext
        divisionET.addTextChangedListener(new TextWatcher() {
            // Mandatory Method
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            // Runs everytime the edittext is changed
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Only runs if the edittext has 2 or more characters in it
                if (divisionET.getText().toString().length() >= 2) {
                    // Text inputted converted to have a cap letter to start ie. "start" -> "Start"
                    String divisionChanged = divisionET.getText().toString();
                    String divisionChangedCapped = capFirstLetter(divisionChanged);
                    EditUserController.divisionAutoComplete(getApplicationContext(), divisionChangedCapped);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // extra measure to ensure the correct text is being assigned to the variable
                divisionText = divisionET.getText().toString();
            }
        });
    }
}
