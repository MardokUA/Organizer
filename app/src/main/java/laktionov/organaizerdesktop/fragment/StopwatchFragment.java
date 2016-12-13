package laktionov.organaizerdesktop.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import laktionov.organaizerdesktop.R;
import laktionov.organaizerdesktop.activity.SettingsActivity;

/**
 * Created by mardo on 08.12.2016.
 */

public class StopwatchFragment extends Fragment implements View.OnClickListener {

    private TextView tvTimer;
    private Button btnStart;
    private Button btnStop;
    private Button btnRefresh;
    private Button btnSetTime;

    Handler handler;
    private Timer stowWatch;
    private StopwatchTimerTask stopwatchTask;

    private Long start;
    private Long stop;
    private Long result;
    private String time;

    private long ms;
    private long sec;
    private long minutes;
    private long hours;
    private SharedPreferences sharedPref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();

        if (savedInstanceState != null) {
            ms = savedInstanceState.getLong("time_ms");
            sec = savedInstanceState.getLong("time_sec");
            minutes = savedInstanceState.getLong("time_minutes");
            hours = savedInstanceState.getLong("time_hours");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong("time_ms", ms);
        outState.putLong("time_sec", sec);
        outState.putLong("time_minutes", minutes);
        outState.putLong("time_hours", hours);

        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stopwatch, container, false);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int theme = sharedPref.getInt(SettingsActivity.APP_THEME, R.style.AppTheme);
        getActivity().setTheme(theme);
        
        tvTimer = (TextView) root.findViewById(R.id.tvTimer);
        btnStart = (Button) root.findViewById(R.id.btn_start);
        btnStop = (Button) root.findViewById(R.id.btn_stop);
        btnRefresh = (Button) root.findViewById(R.id.btn_refresh);


        tvTimer.setText(String.format("%02d" + ":" + "%02d" + ":" + "%02d" + ":" + "%02d", hours, minutes, sec, ms));

        root.findViewById(R.id.btn_start).setOnClickListener(this);
        root.findViewById(R.id.btn_stop).setOnClickListener(this);
        root.findViewById(R.id.btn_refresh).setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_start:

                start = System.currentTimeMillis();
                stowWatch = new Timer();
                stopwatchTask = new StopwatchTimerTask();
                stowWatch.schedule(stopwatchTask, 0, 10);
                btnStart.setEnabled(false);
                break;

            case R.id.btn_stop:

                if (stowWatch != null) {
                    stowWatch.cancel();
                    btnStart.setEnabled(true);
                }
                break;

            case R.id.btn_refresh:

                stowWatch.cancel();
                ms = 0;
                sec = 0;
                minutes = 0;
                hours = 0;
                btnStart.setEnabled(true);
                tvTimer.setText("00:00:00:00");
                break;
        }

    }

    class StopwatchTimerTask extends TimerTask {
        @Override
        public void run() {
            stop = System.currentTimeMillis();
            result = stop - start;

            if (result < result.MAX_VALUE) {
                ms++;
            }
            if (ms % 100 == 0) {
                ms = 0;
                sec++;
            }
            if (sec % 60 == 0 && ms == 0) {
                sec = 0;
                minutes++;
            }
            if (minutes % 3600 == 0 && ms == 0 && sec == 0) {
                minutes = 0;
                hours++;
            }
            time = String.format("%02d" + ":" + "%02d" + ":" + "%02d" + ":" + "%02d", hours, minutes, sec, ms);
            getActivity().runOnUiThread(runStopWatch);
        }
    }

    Runnable runStopWatch = new Runnable() {
        @Override
        public void run() {
            tvTimer.setText(time);
        }
    };
}
