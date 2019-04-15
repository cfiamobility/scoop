package ca.gc.inspection.scoop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class mainScreen extends AppCompatActivity {

    private Button createPost;

    static ViewPager viewPager;

    // fragments
    communityFeedScreen communityFragment;
    officialFeedScreen officialFragment;
    NotificationsScreen notificationFragment;
    profileScreen profileFragment;

    MenuItem previousMenuItem;

    private DrawerLayout drawerLayout;

    public void createPost (View view) {
        startActivity(new Intent(view.getContext(), CreatePostScreen.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        // set out custom toolbar_menu as the default action bar
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // initializing create post button
        createPost = findViewById(R.id.createPost);

        // initializing viewPage
        viewPager = findViewById(R.id.frame_layout);

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
                        // clear the shared preferences
                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("ca.gc.inspection.scoop", Context.MODE_PRIVATE);
                        sharedPreferences.edit().clear().apply();

                        // clearing the config variables
                        Config.token = "";
                        Config.currentUser = "";

                        // bring the user back to the splash screen page
                        startActivity(new Intent(getApplicationContext(), splashScreen.class));
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
        actionBar.setHomeAsUpIndicator(R.drawable.ic_account_circle_white_24dp);

        // inflating the bottom navigation bar
        final BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.community:
                        viewPager.setCurrentItem(0);
                        createPost.setVisibility(View.VISIBLE);
                        getSupportActionBar().setTitle("Community");
                        break;
                    case R.id.official:
                        viewPager.setCurrentItem(1);
                        createPost.setVisibility(View.VISIBLE);
                        getSupportActionBar().setTitle("Official");
                        break;
                    case R.id.notifications:
                        viewPager.setCurrentItem(2);
                        createPost.setVisibility(View.INVISIBLE);
                        getSupportActionBar().setTitle("Notifications");
                        break;
                    case R.id.profile:
                        viewPager.setCurrentItem(3);
                        createPost.setVisibility(View.INVISIBLE);
                        getSupportActionBar().setTitle("Profile");
                        break;
                }
                return false;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (previousMenuItem != null) {
                    previousMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(i).setChecked(true);
                previousMenuItem = bottomNavigationView.getMenu().getItem(i);

                // setting the title of the page every time you switch tabs
                if (i == 0) {
                    getSupportActionBar().setTitle("Community");
                } else if (i == 1) {
                    getSupportActionBar().setTitle("Official");
                } else if (i == 2) {
                    getSupportActionBar().setTitle("Notifications");
                } else {
                    getSupportActionBar().setTitle("Profile");
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter adapter = new viewPagerAdapter(getSupportFragmentManager());
        communityFragment =new communityFeedScreen();
        officialFragment = new officialFeedScreen();
        notificationFragment = new NotificationsScreen();
        profileFragment =  new profileScreen();
        adapter.addFragment(communityFragment);
        adapter.addFragment(officialFragment);
        adapter.addFragment(notificationFragment);
        adapter.addFragment(profileFragment);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
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
