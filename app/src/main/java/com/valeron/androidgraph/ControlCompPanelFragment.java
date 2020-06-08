package com.valeron.androidgraph;


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

public class ControlCompPanelFragment extends Fragment {

    private EditText mLeftBorderET;
    private EditText mRightBorderET;
    private EditText mEpsET;
    private EditText mFirstFunctionET;
    private EditText mSecFunctionET;
    private Button mCalcButton;
    private InputValuesModel mValuesModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_control_comp_panel, container, false);

        mLeftBorderET =v.findViewById(R.id.leftBorderET);
        mRightBorderET = v.findViewById(R.id.rightBorderET);
        mFirstFunctionET = v.findViewById(R.id.firstFuncET);
        mSecFunctionET = v.findViewById(R.id.secFuncET);
        mEpsET = v.findViewById(R.id.epsET);
        mCalcButton = v.findViewById(R.id.calcButton);
        mValuesModel = InputValuesModel.getInstance();

        mCalcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foo1Text = "f(x,y)=" + mFirstFunctionET.getText().toString();
                String foo2Text = "f(x,y)=" + mSecFunctionET.getText().toString();
                String epsText = mEpsET.getText().toString();
                String lbText = mLeftBorderET.getText().toString();
                String rbText = mRightBorderET.getText().toString();
                if(!foo1Text.isEmpty() && !foo2Text.isEmpty() && !epsText.isEmpty() && !lbText.isEmpty() && !rbText.isEmpty()){
                    Function function1 = new Function(foo1Text);
                    Function function2 = new Function(foo2Text);
                    mValuesModel.setFunction(function1);
                    mValuesModel.setFunction1(function2);
                    mValuesModel.setEps(Double.parseDouble(epsText));
                    mValuesModel.setLeftBorder(Double.parseDouble(lbText));
                    mValuesModel.setRightBorder(Double.parseDouble(rbText));
                    mValuesModel.changed();
                }else{
                    Toast.
                            makeText(ControlCompPanelFragment.this.getContext(), "Ошибка: все поля должны быть заполнены!", Toast.LENGTH_SHORT).
                            show();
                }
            }
        });
        return v;
    }

}
