package ca.gc.inspection.scoop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private MainContract.Presenter mPresenter;

    // UI Declarations
    public static FloatingActionButton createPost;
    private DrawerLayout drawerLayout;
    static BottomNavigationView bottomNavigationView;
    MenuItem previousMenuItem;

    // Fragment Manager declaration
    static FragmentManager manager;

    @Override
    public void setPresenter(@NonNull MainContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("userid", Config.currentUser);

        // Defining the fragment manager
        manager = getSupportFragmentManager();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        setPresenter(new MainPresenter(this));

        // set the system status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_dark));

        // set out custom menu_action_bar as the default action bar
        final Toolbar toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);

        // initializing create Post button
        createPost = findViewById(R.id.activity_main_fbtn_create_post);
        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), CreatePostActivity.class));
            }
        });

        // initializing drawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);

        // initializing NavigationView
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // Setting the on clicks for the menu bar.
                switch (menuItem.getItemId()) {
                    case R.id.saved_posts:
                        startActivity(new Intent(getApplicationContext(), SavedPostActivity.class));
                        break;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        break;
                    case R.id.info:
                        startActivity(new Intent(getApplicationContext(), InfoActivity.class));
                        break;
                    case R.id.logout:
                        // clear the shared preferences
                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("ca.gc.inspection.scoop", Context.MODE_PRIVATE);
                        sharedPreferences.edit().clear().apply();

                        // clearing the config variables
                        Config.token = "";
                        Config.currentUser = "";

                        // bring the user back to the splash screen page
                        startActivity(new Intent(getApplicationContext(), SplashScreenActivity.class));
                        finish();
                        break;
                }
                drawerLayout.closeDrawers();
                return false;
            }
        });

        // enabling the app bar's home button for navigation drawer
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_account_circle_blue_24dp);

        // inflating the bottom navigation bar
        bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // Method that handles whenever you pressed one of the four tabs on the bottom nav bar

                // Goes to TabFragment to handle which fragment shows up
                int returned = TabFragment.setNavigation(menuItem.getItemId());

                // Setting the title of the action bar and changing which tab shows up as highlighted
                if (previousMenuItem != null) {
                    previousMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(true);
                }
                switch (returned) {
                    case 0:
                        // Highlight tab
                        bottomNavigationView.getMenu().getItem(returned).setChecked(true);
                        // Sets title
                        getSupportActionBar().setTitle("Community");
                        break;
                    case 1:
                        bottomNavigationView.getMenu().getItem(returned).setChecked(true);
                        getSupportActionBar().setTitle("Official");
                        break;
                    case 2:
                        bottomNavigationView.getMenu().getItem(returned).setChecked(true);
                        getSupportActionBar().setTitle("Notifications");
                        break;
                    case 3:
                        bottomNavigationView.getMenu().getItem(returned).setChecked(true);
                        getSupportActionBar().setTitle("Profile");
                        break;
                }

                // Pops the entire fragment stack to return you to tab fragment
                for (int i = 0; i < manager.getBackStackEntryCount(); ++i) {
                    manager.popBackStack();
                }

                // Setting the variable for next click
                previousMenuItem = bottomNavigationView.getMenu().getItem(returned);
                return false;
            }
        });
    }

    /**
     * Method called when another user is pressed
     * @param userid
     */
	public static void otherUserClicked(String userid) {
        if (userid != null) {
            // Initializing the other user's fragment
            Fragment otherUserFragment = new OtherUserFragment();

            // Passing the userid to the other user fragment using bundles
            Bundle bundle = new Bundle();
            bundle.putString("userid", userid);
            otherUserFragment.setArguments(bundle);

            // Fragment transaction, adds the other user's profile fragment onto the framelayout
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.add(R.id.main, otherUserFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
	    getSupportActionBar().setTitle(OtherUserFragment.title);
        super.onBackPressed();
    }

    // inflate the search action on the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_action_bar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.search:
                startActivity(new Intent(getApplicationContext(), SearchActivity.class)); // when the search icon is tapped
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
