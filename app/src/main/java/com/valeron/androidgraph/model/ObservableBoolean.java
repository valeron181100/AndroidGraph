package com.valeron.androidgraph.model;

import java.util.ArrayList;
import java.util.Observable;

public class ObservableBoolean {

    private boolean val;

    private ArrayList<ValueChangedListener> mListeners;

    public ObservableBoolean(boolean val){
        mListeners = new ArrayList<>();
        this.val = val;
    }

    public void addValueChangedListener(ValueChangedListener listener){
        mListeners.add(listener);
    }


    public void setVal(boolean val) {
        boolean oldVal = this.val;
        this.val = val;
        for (ValueChangedListener p:
             mListeners) {
            p.changed(oldVal, this.val);
        }
    }

    public boolean getVal() {
        return val;
    }

    public interface ValueChangedListener{
        void changed(boolean oldVal, boolean newVal);
    }
}
