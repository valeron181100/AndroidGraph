package com.valeron.androidgraph.model;

import org.mariuszgromada.math.mxparser.Function;

import java.util.ArrayList;

public class InputValuesModel {

    private double mLeftBorder;
    private double mRightBorder;
    private Function mFunction;
    private double mEps;
    private static InputValuesModel instance;
    private ArrayList<InputValuesChangeListener> mListeners;

    private InputValuesModel(){
        mListeners = new ArrayList<>();
    }

    public static InputValuesModel getInstance() {
        if(instance == null)
            instance = new InputValuesModel();
        return instance;
    }

    public void addValuesChangeListener(InputValuesChangeListener listener){
        mListeners.add(listener);
    }

    public double getLeftBorder() {
        return mLeftBorder;
    }

    public void setLeftBorder(double leftBorder) {
        mLeftBorder = leftBorder;
    }

    public double getRightBorder() {
        return mRightBorder;
    }

    public void setRightBorder(double rightBorder) {
        mRightBorder = rightBorder;
    }

    public Function getFunction() {
        return mFunction;
    }

    public void setFunction(Function function) {
        mFunction = function;
    }

    public double getEps() {
        return mEps;
    }

    public void setEps(double eps) {
        mEps = eps;
    }

    public void changed(){
        for (InputValuesChangeListener listener : mListeners) {
            listener.changed();
        }
    }

    public static interface InputValuesChangeListener{
        void changed();
    }
}
