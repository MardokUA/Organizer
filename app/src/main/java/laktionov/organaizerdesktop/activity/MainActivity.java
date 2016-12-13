package laktionov.organaizerdesktop.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import laktionov.organaizerdesktop.R;
import laktionov.organaizerdesktop.fragment.CalculatorFragment;
import laktionov.organaizerdesktop.fragment.StopwatchFragment;
import laktionov.organaizerdesktop.fragment.TimerFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String[] mTitles;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private Fragment calculator;
    private Fragment stopwatch;
    private Fragment timer;

    private MenuItem russian;
    private MenuItem english;

    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        int theme = sharedPref.getInt(SettingsActivity.APP_THEME, R.style.AppTheme);
        setTheme(theme);

        Log.i("THEME", theme + "");

        setContentView(R.layout.activity_main_drawler);

        calculator = new CalculatorFragment();
        stopwatch = new StopwatchFragment();
        timer = new TimerFragment();

        mTitles = getResources().getStringArray(R.array.menu_item_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_menu);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setCheckedItem(R.id.mi_calculator);

        russian = (MenuItem) findViewById(R.id.mi_Russian);
        english = (MenuItem) findViewById(R.id.mi_english);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.mi_calculator:
                fragment = calculator;
                break;
            case R.id.mi_stopwatch:
                fragment = stopwatch;
                break;
            case R.id.mi_timer:
                fragment = timer;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

        mNavigationView.setCheckedItem(item.getItemId());
        getSupportActionBar().setTitle(item.getTitle());

        mDrawerLayout.closeDrawers();

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.app_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mi_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.mi_preference:
                break;
            case R.id.mi_Russian:
                item.setChecked(!item.isChecked());
                if (item.isChecked()) {

                    Toast.makeText(this, "Russian language enabled", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.mi_english:
                item.setChecked(!item.isChecked());
                if (item.isChecked()) {

                    Toast.makeText(this, "English language enabled", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
