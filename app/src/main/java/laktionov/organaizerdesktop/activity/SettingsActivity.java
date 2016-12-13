package laktionov.organaizerdesktop.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import laktionov.organaizerdesktop.R;

/**
 * Created by mardo on 08.12.2016.
 */

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private CheckBox cbBlueTheme;
    private CheckBox cbGreenTheme;
    private CheckBox cbOceanTheme;
    public static final String APP_THEME = "THEME";
    SharedPreferences sharedPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        int theme = sharedPref.getInt(APP_THEME, R.style.AppTheme);
        setTheme(theme);

        Log.i("THEME", theme + "");

        setContentView(R.layout.activity_settings);

        cbBlueTheme = (CheckBox) findViewById(R.id.cb_blueThem);
        cbGreenTheme = (CheckBox) findViewById(R.id.cb_greenThem);
        cbOceanTheme = (CheckBox) findViewById(R.id.cb_oceanThem);

        findViewById(R.id.cb_blueThem).setOnClickListener(this);
        findViewById(R.id.cb_greenThem).setOnClickListener(this);
        findViewById(R.id.cb_oceanThem).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.cb_blueThem:

                if (cbBlueTheme.isChecked()) {

                    sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(APP_THEME, R.style.AppTheme);
                    editor.apply();
                }
                finish();
                startActivity(new Intent(this, SettingsActivity.class));
                break;

            case R.id.cb_greenThem:

                if (cbGreenTheme.isChecked()) {

                    sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(APP_THEME, R.style.AppTheme2);
                    editor.apply();
                }
                finish();
                startActivity(new Intent(this, SettingsActivity.class));
                break;

            case R.id.cb_oceanThem:

                if (cbOceanTheme.isChecked()) {

                    sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(APP_THEME, R.style.AppTheme3);
                    editor.apply();
                }
                finish();
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
    }


}
