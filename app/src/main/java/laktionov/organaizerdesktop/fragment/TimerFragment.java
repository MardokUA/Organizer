package laktionov.organaizerdesktop.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import laktionov.organaizerdesktop.R;
import laktionov.organaizerdesktop.receiver.AlarmReceiver;

/**
 * Created by mardo on 11.12.2016.
 */

public class TimerFragment extends Fragment implements View.OnClickListener, TimePickerFragment.OnTimeSetListener {
    private TextView tvTimer;
    private Button btnStart;
    private Button btnStop;
    private Button btnRefresh;

    private Button btnSetTime;

    Handler handler;
    private Timer stowWatch;
    private TimerFragment.StopwatchTimerTask stopwatchTask;

    private Long start;
    private Long stop;

    private Long result;
    private String time;

    private long ms;
    private long sec;
    private long min;
    private long hrs;
    private Long targetTime;
    private long stopTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();

        stopTime = getActivity().getPreferences(Context.MODE_PRIVATE).getLong("stopTime", 0);

        if (savedInstanceState != null) {
            ms = savedInstanceState.getLong("time_ms");
            sec = savedInstanceState.getLong("time_sec");
            min = savedInstanceState.getLong("time_minutes");
            hrs = savedInstanceState.getLong("time_hours");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong("time_ms", ms);
        outState.putLong("time_sec", sec);
        outState.putLong("time_minutes", min);
        outState.putLong("time_hours", hrs);

        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_timer, container, false);

        tvTimer = (TextView) root.findViewById(R.id.tvTimerTime);
        btnStart = (Button) root.findViewById(R.id.btn_startTimer);
        btnStop = (Button) root.findViewById(R.id.btn_stopTimer);
        btnRefresh = (Button) root.findViewById(R.id.btn_refreshTimer);
        btnSetTime = (Button) root.findViewById(R.id.btn_setTimer);


        tvTimer.setText(String.format("%02d" + ":" + "%02d" + ":" + "%02d" + ":" + "%02d", hrs, min, sec, ms));

        root.findViewById(R.id.btn_startTimer).setOnClickListener(this);
        root.findViewById(R.id.btn_stopTimer).setOnClickListener(this);
        root.findViewById(R.id.btn_refreshTimer).setOnClickListener(this);
        root.findViewById(R.id.btn_setTimer).setOnClickListener(this);

        btnStart.setEnabled(false);
        btnStop.setEnabled(false);
        btnRefresh.setEnabled(false);

        return root;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_startTimer:

                //Wrong stoptime set
                stopTime = System.currentTimeMillis() + targetTime;

                Log.i("SETTIME", stopTime+"");

                getActivity()
                        .getPreferences(Context.MODE_PRIVATE)
                        .edit()
                        .putLong("stopTime", stopTime)
                        .apply();

                stowWatch = new Timer();
                stopwatchTask = new StopwatchTimerTask();
                stowWatch.schedule(stopwatchTask, 0, 10);

                btnStart.setEnabled(false);
                break;

            case R.id.btn_stopTimer:

                if (stowWatch != null) {
                    stowWatch.cancel();
                    btnStart.setEnabled(true);
                }
                break;

            case R.id.btn_refreshTimer:

                stowWatch.cancel();
                ms = 0;
                sec = 0;
                min = 0;
                hrs = 0;
                tvTimer.setText("00:00:00:00");
                break;

            case R.id.btn_setTimer:
                new TimePickerFragment()
                        .setOnTimeSetListener(this)
                        .show(getFragmentManager(), "time_picker");

                btnStart.setEnabled(true);
                btnStop.setEnabled(true);
                btnRefresh.setEnabled(true);
                break;
        }

    }

    @Override
    public void onTimeSetup(int hours, int minute) {

        targetTime = (long) (hours * 3_600_000 + minute * 60 * 1000);

        this.hrs = hours;
        this.min = minute;
        this.sec = 0;
        this.ms = 0;

        tvTimer.setText(String.format(Locale.getDefault(), "%02d:%02d:00.00", hours, minute));


        Intent broadcastIntent = new Intent(getContext(), AlarmReceiver.class);

        PendingIntent piBroadcast = PendingIntent
                .getBroadcast(getContext(), 0, broadcastIntent, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager manager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC, stopTime, piBroadcast);
    }

    public class StopwatchTimerTask extends TimerTask {
        @Override
        public void run() {
            long millis = stopTime - System.currentTimeMillis();

            if (millis > 0) {
                /*
                * int seconds = (int) (milliseconds / 1000) % 60;
                * int minutes = (int) ((milliseconds / (1000*60)) % 60);
                * int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
                */

                ms = millis % 100;
                sec = (millis / 1000) % 60;
                min = (millis / (1000 * 60)) % 60;
                hrs = (millis / (1000 * 60 * 60)) % 24;
            } else {
                ms = sec = min = hrs = 0;
                cancel();

                Log.i("STOPTIME", stopTime+"-" + System.currentTimeMillis());
            }

            time = String.format(Locale.getDefault(), "%02d:%02d:%02d.%02d", hrs, min, sec, ms);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    tvTimer.setText(time);
                }
            });
        }
    }
}
