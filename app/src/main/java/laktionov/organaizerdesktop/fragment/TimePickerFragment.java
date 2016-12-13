package laktionov.organaizerdesktop.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import laktionov.organaizerdesktop.R;

/**
 * Created by mardo on 11.12.2016.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private OnTimeSetListener onTimeSetListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        TimePickerDialog picker = new TimePickerDialog(getActivity(), this, 0, 1, true);
        picker.setTitle(getResources().getString(R.string.choose_time));

        return picker;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
        if (onTimeSetListener != null) {
            onTimeSetListener.onTimeSetup(hours, minutes);
        }
    }

    public TimePickerFragment setOnTimeSetListener(OnTimeSetListener onTimeSetListener) {
        this.onTimeSetListener = onTimeSetListener;
        return this;
    }

    /**
     * Contract.
     */
    public interface OnTimeSetListener {
        void onTimeSetup(int hours, int minute);
    }
}
