package com.valeron.androidgraph;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.valeron.androidgraph.model.InputValuesModel;

import org.mariuszgromada.math.mxparser.Function;

public class ControlPanelFragment extends Fragment {


    private EditText mLeftBorderET;
    private EditText mRightBorderET;
    private EditText mEpsET;
    private EditText mFunctionET;
    private Button mCalcButton;
    private InputValuesModel mValuesModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_control_panel, container, false);
        mLeftBorderET =v.findViewById(R.id.leftBorderET);
        mRightBorderET = v.findViewById(R.id.rightBorderET);
        mFunctionET = v.findViewById(R.id.funcET);
        mEpsET = v.findViewById(R.id.epsET);
        mCalcButton = v.findViewById(R.id.calcButton);
        mValuesModel = InputValuesModel.getInstance();

        mCalcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fooText = mFunctionET.getText().toString();
                String epsText = mEpsET.getText().toString();
                String lbText = mLeftBorderET.getText().toString();
                String rbText = mRightBorderET.getText().toString();
                if(!fooText.isEmpty() && !epsText.isEmpty() && !lbText.isEmpty() && !rbText.isEmpty()){
                    Function function = new Function(fooText);
                    mValuesModel.setFunction(function);
                    mValuesModel.setEps(Double.parseDouble(epsText));
                    mValuesModel.setLeftBorder(Double.parseDouble(lbText));
                    mValuesModel.setRightBorder(Double.parseDouble(rbText));
                    mValuesModel.changed();
                }else{
                    Toast.
                            makeText(ControlPanelFragment.this.getContext(), "Ошибка: все поля должны быть заполнены!", Toast.LENGTH_SHORT).
                            show();
                }
            }
        });

       /* Function function = new Function(mFunctionET.getText().toString());
        mValuesModel.setFunction(function);
        mValuesModel.setEps(Double.parseDouble(mEpsET.getText().toString()));
        mValuesModel.setLeftBorder(Double.parseDouble(mLeftBorderET.getText().toString()));
        mValuesModel.setRightBorder(Double.parseDouble(mRightBorderET.getText().toString()));*/

        return v;
    }
}
