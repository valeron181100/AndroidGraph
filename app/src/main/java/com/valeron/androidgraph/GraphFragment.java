package com.valeron.androidgraph;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.ColorRes;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.valeron.androidgraph.logic.GraphSolver;
import com.valeron.androidgraph.logic.SolveMethod;
import com.valeron.androidgraph.model.InputValuesModel;

import java.util.ArrayList;

public class GraphFragment extends Fragment {

    private GraphView mGraph;
    private InputValuesModel mValuesModel;
    public static final int AXIS_PADDING = 2;
    private LineGraphSeries<DataPoint> series;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_graph, container, false);
        mValuesModel = InputValuesModel.getInstance();
        mGraph = v.findViewById(R.id.graph);
        mValuesModel.addValuesChangeListener(new InputValuesModel.InputValuesChangeListener() {
            @Override
            public void changed() {
                mGraph.getViewport().setXAxisBoundsManual(true);
                mGraph.getViewport().setMinX(mValuesModel.getLeftBorder() - AXIS_PADDING);
                mGraph.getViewport().setMaxX(mValuesModel.getRightBorder() + AXIS_PADDING);
                ArrayList<DataPoint> points = new ArrayList<>();
                for (double i = mValuesModel.getLeftBorder() - AXIS_PADDING; i < mValuesModel.getRightBorder() + AXIS_PADDING; i+= 0.001) {
                    points.add(
                            new DataPoint(i, mValuesModel.getFunction().calculate(i))
                    );
                }
                series = new LineGraphSeries<>(points.toArray(new DataPoint[points.size()]));
                mGraph.getSeries().clear();

                double x0 = new GraphSolver(mValuesModel.getFunction(), mValuesModel.getLeftBorder(), mValuesModel.getRightBorder(), mValuesModel.getEps())
                .solve(mValuesModel.getSolveMethod());

                LineGraphSeries<DataPoint> xSeries = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(x0, mValuesModel.getFunction().calculate(x0))
                });

                if(x0 < mValuesModel.getLeftBorder() || x0 > mValuesModel.getRightBorder()){
                    Toast.makeText(GraphFragment.this.getContext(), "Найденный корень не входит в заданный интервал.", Toast.LENGTH_SHORT)
                            .show();
                    xSeries.setColor(Color.parseColor("red"));
                }else{
                    xSeries.setColor(Color.parseColor("green"));
                }

                xSeries.setDrawDataPoints(true);
                xSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
                    @Override
                    public void onTap(Series series, DataPointInterface dataPoint) {
                        Toast.makeText(GraphFragment.this.getContext(), "(" + dataPoint.getX() + ", " + dataPoint.getY() + ")", Toast.LENGTH_SHORT).show();
                    }
                });

                mGraph.addSeries(series);
                mGraph.addSeries(xSeries);
                mGraph.setScaleX(1.0f);
                mGraph.setScaleY(1.0f);
            }
        });




        series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });

        mGraph.addSeries(series);
        mGraph.getViewport().setScalableY(true);
        mGraph.getViewport().setScalableY(true);

        return v;
    }

}
