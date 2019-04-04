package ca.gc.inspection.scoop;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class edit_profile_screen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
	// UI Declarations
	AutoCompleteTextView positionET, buildingET, divisionET;
	EditText firstNameET, lastNameET, cityET, linkedinET, twitterET, facebookET;
	Spinner provinceSpinner;
	Button changeProfilePicBTN;
	CircleImageView mPreviewImageView;

	// UI Support Declarations
	ArrayAdapter<CharSequence> spinnerAdapter;
	ArrayAdapter<String> buildingAdapter, positionAdapter, divisionAdapter;

	// Application Side Variable Declarations
	String firstNameText, lastNameText, positionText, divisionText, buildingText, cityText, provinceText, linkedinText, twitterText, facebookText;
	ArrayList<String> positionAutoComplete, buildingsAutoComplete, cityAL, provinceAL, divisionsAutoComplete;
	HashMap<String, String> positionObjects, buildingsObjects, divisionsObjects;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_profile_screen);

		// EditText Definitions
		firstNameET = findViewById(R.id.firstNameET);
		lastNameET = findViewById(R.id.lastNameET);
		positionET = findViewById(R.id.positionET);
		divisionET = findViewById(R.id.divisionET);
		buildingET = findViewById(R.id.buildingET);
		cityET = findViewById(R.id.cityET);
		provinceSpinner = findViewById(R.id.provincesSPIN);
		linkedinET = findViewById(R.id.linkedinET);
		twitterET = findViewById(R.id.twitterET);
		facebookET = findViewById(R.id.facebookET);

		// Button Definition
		changeProfilePicBTN = findViewById(R.id.chooseprofileBTN);
		changeProfilePicBTN.setOnClickListener(changeprofile);

		// ImageView Definition
		mPreviewImageView = findViewById(R.id.profile_image);

		// Province Spinner Definition
		spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.provinces_array, android.R.layout.simple_spinner_item);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		provinceSpinner.setAdapter(spinnerAdapter);
		provinceSpinner.setOnItemSelectedListener(this);

		//Getting the userID
		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("ca.gc.inspection.scoop", Context.MODE_PRIVATE);

		// DEFAULT TO BE CHANGED TO ""
		String userID = sharedPreferences.getString("userID", "922f7667-6996-46bc-8be4-be9c2f993c1f");

		// If userID is unavailable.
		if (userID.equals("")) {
			finish();
		}

		// Searches for user's info to autofill the edittext
		initialFill(userID);

		// Sets up the Position, Office Address and Division AutoCompletes
		autoComplete();
	}

	// Mandatory Methods for AdapterView.OnItemSelected - OnNothingSelected is Default Spinner Selection set to AB
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// Default selection
		provinceSpinner.setSelection(0);
	}

	// Fills the known edittext with previous information the user has provided
	private void initialFill(final String userID) {

		// URL TO BE CHANGED - userID passed as a parameter to NodeJS
		String URL = "http://10.0.2.2:3000/edituser/getinitial/" + userID;

		// Request to set up Volley.Method.GET
		RequestQueue requestQueue = Volley.newRequestQueue(this);

		// Requesting response be sent back as a JSON Object
		JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,  new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
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
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.i("error", error.toString());
			}
		});
		// Request submitted
		requestQueue.add(getRequest);
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


					// URL TO BE CHANGED - position entered passed to NodeJS as a parameter
					String URL = "http://10.0.2.2:3000/edituser/positionchanged/" + positionChangedCapped;

					// Request to set up the get method
					RequestQueue requestQueue = Volley.newRequestQueue(edit_profile_screen.this);

					// Asking for an array from response (will send back 3 objects in an array)
					JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
						@Override
						public void onResponse(JSONArray response) {
							try {
								// Position map/arraylist redefined every time text is changed
								positionObjects = new HashMap<>();
								positionAutoComplete = new ArrayList<>(); // Arraylist used for setting in the array adapter

								// loops through every object
								for (int i = 0; i < response.length(); i++) {
									// Gathers info from the object - positionid and positionname
									String positionid = response.getJSONObject(i).getString("positionid");
									String positionname = response.getJSONObject(i).getString("positionname");

									// places the objects into the map/arraylist
									positionObjects.put(positionid, positionname);
									positionAutoComplete.add(positionname);
								}

								// array adapter to set the autocomplete menu
								positionAdapter = new ArrayAdapter<>(edit_profile_screen.this, android.R.layout.simple_dropdown_item_1line, positionAutoComplete);
								positionET.setAdapter(positionAdapter);
								// Text changed when finished;
								positionText = positionET.getText().toString();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Log.i("error", error.toString());
						}
					});
					requestQueue.add(getRequest);
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

					// URL TO BE CHANGED - address passed as parameter to nodeJS
					String URL = "http://10.0.2.2:3000/edituser/addresschanged/" + addressChangedCapped;

					// Request for get method
					RequestQueue requestQueue = Volley.newRequestQueue(edit_profile_screen.this);

					// Asking for a JSONArray
					JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
						@Override
						public void onResponse(JSONArray response) {
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
								buildingAdapter = new ArrayAdapter<>(edit_profile_screen.this, android.R.layout.simple_dropdown_item_1line, buildingsAutoComplete);
								buildingET.setAdapter(buildingAdapter);
								buildingText = buildingET.getText().toString();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Log.i("error", error.toString());
						}
					});
					// Submitting the request
					requestQueue.add(getRequest);
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

					// Inputted division is passed as a parameter to NodeJS
					String URL = "http://10.0.2.2:3000/edituser/divisionchanged/" + divisionChangedCapped;

					// Request for the get method
					RequestQueue requestQueue = Volley.newRequestQueue(edit_profile_screen.this);

					// Asking for a JSONArray back
					JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
						@Override
						public void onResponse(JSONArray response) {
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
								divisionAdapter = new ArrayAdapter<>(edit_profile_screen.this, android.R.layout.simple_dropdown_item_1line, divisionsAutoComplete);
								divisionET.setAdapter(divisionAdapter);
								divisionText = divisionET.getText().toString();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {

						}
					});
					// submitting the request
					requestQueue.add(jsonObjectRequest);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				// extra measure to ensure the correct text is being assigned to the variable
				divisionText = divisionET.getText().toString();
			}
		});
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
			AlertDialog.Builder builder = new AlertDialog.Builder(edit_profile_screen.this);
			builder.setTitle("Upload or Take a Photo");
			builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent choosePictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
					choosePictureIntent.setType("image/*");
					startActivityForResult(Intent.createChooser(choosePictureIntent, "Select Picture"), MyCamera.CHOOSE_PIC_REQUEST_CODE);

				}
			});
			builder.setNegativeButton("Take a Picture", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
						requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MyCamera.MY_CAMERA_PERMISSION_CODE);
					} else {
						takePicture();
					}
				}
			});
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	};

	public static Uri mMediaUri;
	public void takePicture() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			File photoFile = null;
			try {
				photoFile = MyCamera.createImageFile(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (photoFile != null) {
				mMediaUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.android.fileprovider", photoFile);
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
				startActivityForResult(takePictureIntent, MyCamera.TAKE_PIC_REQUEST_CODE);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == MyCamera.CHOOSE_PIC_REQUEST_CODE) {
				if (data == null) {
					Toast.makeText(getApplicationContext(), "Image cannot be null!", Toast.LENGTH_LONG).show();
				} else {
					try {
						Uri uri = data.getData();

						String realPath = ImageFilePath.getPath(edit_profile_screen.this, data.getData());
						Log.i("path", realPath);


						Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

						Bitmap newBitmap = MyCamera.imageOrientationValidator(bitmap, realPath);
						mPreviewImageView.setImageBitmap(newBitmap);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				File f = new File(MyCamera.currentPhotoPath);
				Uri contentUri = Uri.fromFile(f);
				mediaScanIntent.setData(contentUri);
				this.sendBroadcast(mediaScanIntent);

				int targetW = mPreviewImageView.getWidth();
				int targetH = mPreviewImageView.getHeight();

				BitmapFactory.Options bmOptions = new BitmapFactory.Options();
				bmOptions.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(MyCamera.currentPhotoPath, bmOptions);

				int photoW = bmOptions.outWidth;
				int photoH = bmOptions.outHeight;

				int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

				bmOptions.inJustDecodeBounds = false;
				bmOptions.inSampleSize = scaleFactor;
				bmOptions.inPurgeable = true;

				Bitmap bitmap = BitmapFactory.decodeFile(MyCamera.currentPhotoPath, bmOptions);
				Bitmap newBitmap = MyCamera.imageOrientationValidator(bitmap, MyCamera.currentPhotoPath);
				mPreviewImageView.setImageBitmap(newBitmap);
			}

		} else if (resultCode != RESULT_CANCELED) {
			Toast.makeText(getApplicationContext(), "Cancelled!", Toast.LENGTH_LONG).show();
		}
	}
		@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == MyCamera.MY_CAMERA_PERMISSION_CODE) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
				takePicture();
			} else {
				Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
			}
		}
	}
}
