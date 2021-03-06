package laktionov.organaizerdesktop.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import laktionov.organaizerdesktop.R;
import laktionov.organaizerdesktop.activity.SettingsActivity;

/**
 * Created by mardo on 08.12.2016.
 */

public class CalculatorFragment extends Fragment implements View.OnClickListener {

    private EditText tvResult;
    private Double num1;
    private Double num2;
    private Double result = 0.;
    private String operation;
    private String decimalSeparator;
    private SharedPreferences sharedPref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        decimalSeparator = String.valueOf(symbols.getDecimalSeparator());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calculator, container, false);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int theme = sharedPref.getInt(SettingsActivity.APP_THEME, R.style.AppTheme);
        getActivity().setTheme(theme);

        tvResult = (EditText) root.findViewById(R.id.tvResult);

        root.findViewById(R.id.btn0).setOnClickListener(this);
        root.findViewById(R.id.btn1).setOnClickListener(this);
        root.findViewById(R.id.btn2).setOnClickListener(this);
        root.findViewById(R.id.btn3).setOnClickListener(this);
        root.findViewById(R.id.btn4).setOnClickListener(this);
        root.findViewById(R.id.btn5).setOnClickListener(this);
        root.findViewById(R.id.btn6).setOnClickListener(this);
        root.findViewById(R.id.btn7).setOnClickListener(this);
        root.findViewById(R.id.btn8).setOnClickListener(this);
        root.findViewById(R.id.btn9).setOnClickListener(this);
        root.findViewById(R.id.btn_add).setOnClickListener(this);
        root.findViewById(R.id.btn_sub).setOnClickListener(this);
        root.findViewById(R.id.btn_mult).setOnClickListener(this);
        root.findViewById(R.id.btn_div).setOnClickListener(this);
        root.findViewById(R.id.btn_dot).setOnClickListener(this);
        root.findViewById(R.id.btn_clear).setOnClickListener(this);
        root.findViewById(R.id.btn_eql).setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View view) {

        Button btn = (Button) view;
        Editable text = tvResult.getText();

        switch (btn.getId()) {
            case R.id.btn0:
            case R.id.btn1:
            case R.id.btn2:
            case R.id.btn3:
            case R.id.btn4:
            case R.id.btn5:
            case R.id.btn6:
            case R.id.btn7:
            case R.id.btn8:
            case R.id.btn9:

                if (text.toString().equals("0")) {
                    text.clear();
                }
                text.append(btn.getText());
                break;

            case R.id.btn_add:
            case R.id.btn_sub:
            case R.id.btn_mult:
            case R.id.btn_div:

                operation = btn.getText().toString();
                if (num1 == (result)) {
                    text.clear();
                    break;
                } else {
                    num1 = Double.parseDouble(tvResult.getText().toString());
                }
                text.clear();
                break;

            case R.id.btn_eql:

                num2 = Double.parseDouble(tvResult.getText().toString());
                switch (operation) {
                    case "+":
                        result = num1 + num2;
                        break;
                    case "-":
                        result = num1 - num2;
                        break;
                    case "/":
                        result = num1 / num2;
                        break;
                    case "x":
                        result = num1 * num2;
                        break;
                }
                num1 = result;
                tvResult.setText(String.format(Locale.getDefault(), "%.2f", result));
                break;

            case R.id.btn_dot:

                if (text.toString().contains(decimalSeparator)) {
                    break;
                }
                tvResult.append(decimalSeparator);
                break;

            case R.id.btn_clear:

                num1 = 0.;
                num2 = 0.;
                result = 0.;
                tvResult.setText("0");
                break;
        }
    }
}
