package ca.gc.inspection.scoop;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class editProfileScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final int TAKE_PIC_REQUEST_CODE = 0;
    public static final int CHOOSE_PIC_REQUEST_CODE = 1;
    public static final int MEDIA_TYPE_IMAGE = 2;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    editProfileScreen profileScreen;

    // UI Declarations
    static AutoCompleteTextView positionET, buildingET, divisionET;
    static EditText firstNameET, lastNameET, cityET, linkedinET, twitterET, facebookET;
    static Spinner provinceSpinner;
    static TextView changeProfilePicBTN;
    static CircleImageView mPreviewImageView;
    static Button saveBTN;
    // UI Support Declarations
    static ArrayAdapter<CharSequence> spinnerAdapter;
    static ArrayAdapter<String> buildingAdapter, positionAdapter, divisionAdapter;

    // Application Side Variable Declarations
    static String firstNameText, lastNameText, positionText, divisionText, buildingText, cityText, provinceText, linkedinText, twitterText, facebookText, userID;
    static ArrayList<String> positionAutoComplete, buildingsAutoComplete, cityAL, provinceAL, divisionsAutoComplete;
    static HashMap<String, String> positionObjects, buildingsObjects, divisionsObjects;
    static Uri mMediaUri;

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

        profileScreen = new editProfileScreen();

        // EditText Definitions
        firstNameET = findViewById(R.id.firstNameEditText);
        lastNameET = findViewById(R.id.lastNameEditText);
        positionET = findViewById(R.id.positionEditText);
        divisionET = findViewById(R.id.divisionEditText);
        buildingET = findViewById(R.id.buildingEditText);
        cityET = findViewById(R.id.citEditText);
        provinceSpinner = findViewById(R.id.provincesSpinner);
        linkedinET = findViewById(R.id.linkedinEditText);
        twitterET = findViewById(R.id.twitterEditText);
        facebookET = findViewById(R.id.facebookEditText);

        // Button Definition
        saveBTN = findViewById(R.id.saveButton);
        saveBTN.setOnClickListener(save);

        // TextView Definition
        changeProfilePicBTN = findViewById(R.id.changeProfilePicture);
        changeProfilePicBTN.setOnClickListener(changeprofile);

        // ImageView Definition
        mPreviewImageView = findViewById(R.id.imageView);

        // Province Spinner Definition
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.provinces_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinceSpinner.setAdapter(spinnerAdapter);
        provinceSpinner.setOnItemSelectedListener(this);

        //Getting the userID
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("ca.gc.inspection.scoop", Context.MODE_PRIVATE);

        // DEFAULT TO BE CHANGED TO ""
        userID = sharedPreferences.getString("userID", "922f7667-6996-46bc-8be4-be9c2f993c1f");
        Log.i("userid", userID + "_");

        // If userID is unavailable.
        if (userID.equals("")) {
            finish();
        }

        // Searches for user's info to autofill the edittext
        EditUserController.initialFill(getApplicationContext());

        // Sets up the Position, Office Address and Division AutoCompletes
        autoComplete();

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

    // Mandatory Methods for AdapterView.OnItemSelected - OnNothingSelected is Default Spinner Selection set to AB
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Default selection
        provinceSpinner.setSelection(0);
    }

    public static void initialFill(JSONObject response) {
        try {
            // If the user id was correct/matching
            if (response.get("userid").toString().equals(userID)) {
                // Name gets and sets
                firstNameText = response.get("firstname").toString();
                lastNameText = response.get("lastname").toString();
                firstNameET.setText(firstNameText);
                lastNameET.setText(lastNameText);

                // If user has already inputted a position
                if (!response.get("positionid").toString().equals("null")) {
                    positionText = response.get("positionname").toString();
                    positionET.setText(positionText);
                }
                // If user has already inputted a division
                if (!response.get("divisionid").toString().equals("null")) {
                    divisionText = response.get("division_en").toString();
                    divisionET.setText(divisionText);
                }
                // If user has already inputted an office address
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void autoComplete() {

        // Autocomplete for position edittext
        positionET.addTextChangedListener(new TextWatcher() {
            // Mandatory Method
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            // Runs whenever the text is changed
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Only submits request if the entered text is >= 3 characters for specificity
                if (positionET.getText().length() >= 3) {
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            // Runs everytime the edit text is changed
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // Only runs when there is >= 1 character(S) in the edittext
                if (buildingET.getText().length() >= 1) {
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

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

    public static void positionAutoSetup(JSONArray response) {
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
            positionAdapter = new ArrayAdapter<>(MyApplication.getAppContext(), android.R.layout.simple_dropdown_item_1line, positionAutoComplete);
            positionET.setAdapter(positionAdapter);
            // Text changed when finished;
            positionText = positionET.getText().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addressAutoSetup(JSONArray response) {
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
            buildingAdapter = new ArrayAdapter<>(MyApplication.getAppContext(), android.R.layout.simple_dropdown_item_1line, buildingsAutoComplete);
            buildingET.setAdapter(buildingAdapter);
            buildingText = buildingET.getText().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void divisionAutoSetup(JSONArray response) {
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
            divisionAdapter = new ArrayAdapter<>(MyApplication.getAppContext(), android.R.layout.simple_dropdown_item_1line, divisionsAutoComplete);
            divisionET.setAdapter(divisionAdapter);
            divisionText = divisionET.getText().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to call for capitalizing the first letter of any string
    private String capFirstLetter(String word) {
        String newWord;
        if (word.length() > 1) {
            newWord = word.substring(0,1).toUpperCase() + word.substring(1);
            return newWord;
        } else if (word.length() == 1) {
            newWord = word.substring(0,1).toUpperCase();
            return newWord;
        } else {
            return null;
        }
    }

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

    private View.OnClickListener changeprofile = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(editProfileScreen.this);
            builder.setTitle("Upload or Take a Photo");
            builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent choosePictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    choosePictureIntent.setType("image/*");
                    startActivityForResult(Intent.createChooser(choosePictureIntent, "Select Picture"), CHOOSE_PIC_REQUEST_CODE);

                }
            });
            builder.setNegativeButton("Take a Picture", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_CAMERA_PERMISSION_CODE);
                    } else {
                        takePicture();
                    }
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };

    public void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                mMediaUri = FileProvider.getUriForFile(editProfileScreen.this, "com.example.android.fileprovider", photoFile);
                Log.i("info", mMediaUri.toString() + " _ extra");
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                startActivityForResult(takePictureIntent, TAKE_PIC_REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CHOOSE_PIC_REQUEST_CODE) {
                if (data == null) {
                    Toast.makeText(getApplicationContext(), "Image cannot be null!", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        Uri uri = data.getData();

                        String realPath = ImageFilePath.getPath(editProfileScreen.this, data.getData());
                        Log.i("path", realPath);


                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                        Bitmap newBitmap = imageOreintationValidator(bitmap, realPath);
                        mPreviewImageView.setImageBitmap(newBitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Log.i("path", currentPhotoPath);
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File f = new File(currentPhotoPath);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);

                int targetW = mPreviewImageView.getWidth();
                int targetH = mPreviewImageView.getHeight();

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
                Bitmap newBitmap = imageOreintationValidator(bitmap, currentPhotoPath);
                mPreviewImageView.setImageBitmap(newBitmap);
            }

        } else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "Cancelled!", Toast.LENGTH_LONG).show();
        }
    }
    String currentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                takePicture();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private Bitmap imageOreintationValidator(Bitmap bitmap, String path) {

        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL) + 5;
            Log.i("orientation", Integer.toString(orientation));
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    Log.i("90", "done");
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    Log.i("180", "done");
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    Log.i("270", "done");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private Bitmap rotateImage(Bitmap source, float angle) {

        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return bitmap;
    }

    private View.OnClickListener save = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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
                params.put("facebook", facebookET.getText().toString());
                params.put("city", cityET.getText().toString());
                params.put("province", provinceSpinner.getSelectedItem().toString());

                EditUserController.updateUserInfo(getApplicationContext(), params);
            } else {
                Toast.makeText(profileScreen, getResources().getString(R.string.invalidNameEntry), Toast.LENGTH_SHORT).show();
            }
        }
    };

}
