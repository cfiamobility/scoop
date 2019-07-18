package ca.gc.inspection.scoop.search;

import ca.gc.inspection.scoop.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import java.util.Objects;
import ca.gc.inspection.scoop.MainActivity;
import ca.gc.inspection.scoop.searchpeople.view.SearchPeopleFragment;
import ca.gc.inspection.scoop.searchpost.view.SearchPostFragment;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


public class SearchActivity extends AppCompatActivity implements SearchContract.View {
    /**
     * Main search activity which deals with taking user input from the search box,
     * saving and loading the most recent search,
     * and sending the raw queries to the current SearchContract.View.Fragment being displayed
     */

    private static final int AUTO_SEARCH_MIN_LENGTH = 4;
    private SearchContract.Presenter mPresenter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private SearchPagerAdapter mPagerAdapter;

    @Override
    public void setPresenter(@NonNull SearchContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setPresenter(new SearchPresenter(this));

        // set the system status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_dark));

        // when the soft keyboard is open tapping anywhere will close the keyboard
        findViewById(R.id.activity_search_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(
                            Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
                }
                return true;
            }
        });

        // setting up the custom toolbar in the activity
        Toolbar toolbar = findViewById(R.id.activity_search_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // setting up the tab layout
        mTabLayout = findViewById(R.id.activity_search_tl_search);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        setupViewPager();
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager() {
        // setting up the viewpager for the tab layout
        mViewPager = findViewById(R.id.activity_search_vp_search);
        mPagerAdapter = new SearchPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(new SearchPostFragment(), "Post");
        mPagerAdapter.addFragment(new SearchPeopleFragment(), "People");

        mViewPager.setAdapter(mPagerAdapter);

        mViewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(mTabLayout) {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_activity, menu);

        // to the search view automatically opens when the activity starts
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        searchMenuItem.expandActionView();

        // when the back button is tapped it will return the user back to the activity_main
        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                return true;
            }
        });

        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if (newText != null && !newText.isEmpty() && newText.length() >= AUTO_SEARCH_MIN_LENGTH) {
                            Log.d("Search activity", " " + newText.length() + " newtext: " + newText);
                            int currentItem = mViewPager.getCurrentItem();
                            ((SearchContract.View.Fragment) mPagerAdapter.getItem(currentItem)).searchQuery(newText);
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        Log.d("search activity", "fragment" + mViewPager.getCurrentItem() + " query: " + query);
                        if (query != null && !query.isEmpty()) {
                            int currentItem = mViewPager.getCurrentItem();
                            ((SearchContract.View.Fragment) mPagerAdapter.getItem(currentItem)).searchQuery(query);
                            return true;
                        }
                        return false;
                    }
                }
        );
        return super.onCreateOptionsMenu(menu);
    }
}
