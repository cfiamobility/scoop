package ca.gc.inspection.scoop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class mainScreen extends AppCompatActivity {

    static Button createPost;

    private DrawerLayout drawerLayout;
    static BottomNavigationView bottomNavigationView;
    MenuItem previousMenuItem;

    static FragmentManager manager;

    public void createPost (View view) {
        startActivity(new Intent(view.getContext(), CreatePostScreen.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        manager = getSupportFragmentManager();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        // set out custom toolbar_menu as the default action bar
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // initializing create post button
        createPost = findViewById(R.id.createPost);

        // initializing drawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);

        // initializing NavigationView
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.saved_posts:
                        startActivity(new Intent(getApplicationContext(), savedPostScreen.class));
                        break;
                    case R.id.edit_profile:
                        startActivity(new Intent(getApplicationContext(), editProfileScreen.class));
                        break;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), settingsScreen.class));
                        break;
                    case R.id.info:
                        startActivity(new Intent(getApplicationContext(), infoScreen.class));
                        break;
                    case R.id.logout:
                        Toast.makeText(mainScreen.this, "LOGOUT", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawers();
                return false;
            }
        });

        // enabling the app bar's home button for navigation drawer
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_account_circle_white_24dp);

        // inflating the bottom navigation bar
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int returned = TabFragment.setNavigation(menuItem.getItemId());

                if (previousMenuItem != null) {
                    previousMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(true);
                }
                switch (returned) {
                    case 0:
                        bottomNavigationView.getMenu().getItem(returned).setChecked(true);
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
                manager.popBackStack();
                previousMenuItem = bottomNavigationView.getMenu().getItem(returned);
                return false;
            }
        });
    }

	@Override
	public void onBackPressed() {
    	if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
    		getSupportFragmentManager().popBackStack();
	    } else {
		    super.onBackPressed();
	    }
	}

	public static void otherUserClicked(FragmentManager fragmentManager, String userid) {
        if (userid != null) {
            Fragment otherUserFragment = new OtherUserFragment();
            Fragment tabFragment = fragmentManager.findFragmentById(R.id.tabFragment);

            Bundle bundle = new Bundle();
            bundle.putString("userid", userid);
            otherUserFragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.detach(tabFragment);
            fragmentTransaction.replace(R.id.main, otherUserFragment);
            fragmentTransaction.addToBackStack("tag");
            createPost.setVisibility(View.INVISIBLE);
            fragmentTransaction.commit();
        }
    }

    // inflate the search action on the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.search:
                startActivity(new Intent(getApplicationContext(), searchActivity.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
