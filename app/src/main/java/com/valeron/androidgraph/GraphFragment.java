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
import java.util.Map;

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
                if(!mValuesModel.getSolveMethod().equals(SolveMethod.Newton)) {
                    mGraph.getViewport().setXAxisBoundsManual(true);
                    mGraph.getViewport().setMinX(mValuesModel.getLeftBorder() - AXIS_PADDING);
                    mGraph.getViewport().setMaxX(mValuesModel.getRightBorder() + AXIS_PADDING);

                    ArrayList<DataPoint> points = new ArrayList<>();
                    for (double i = mValuesModel.getLeftBorder() - AXIS_PADDING; i < mValuesModel.getRightBorder() + AXIS_PADDING; i += 0.001) {
                        points.add(
                                new DataPoint(i, mValuesModel.getFunction().calculate(i))
                        );
                    }
                    series = new LineGraphSeries<>(points.toArray(new DataPoint[points.size()]));

                    mGraph.getSeries().clear();

                    double x0 = 0;
                    try {
                        x0 = new GraphSolver(mValuesModel.getFunction(), mValuesModel.getLeftBorder(), mValuesModel.getRightBorder(), mValuesModel.getEps())
                                .solve(mValuesModel.getSolveMethod()).get("x");
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    LineGraphSeries<DataPoint> xSeries = new LineGraphSeries<>(new DataPoint[]{
                            new DataPoint(x0, mValuesModel.getFunction().calculate(x0))
                    });

                    if (x0 < mValuesModel.getLeftBorder() || x0 > mValuesModel.getRightBorder()) {
                        Toast.makeText(GraphFragment.this.getContext(), "Найденный корень не входит в заданный интервал.", Toast.LENGTH_SHORT)
                                .show();
                        xSeries.setColor(Color.parseColor("red"));
                    } else {
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
            }
        });

        mValuesModel.addValuesChangeListener(new InputValuesModel.InputValuesChangeListener() {
            @Override
            public void changed() {
                if(mValuesModel.getSolveMethod().equals(SolveMethod.Newton)){
                    mGraph.getViewport().setXAxisBoundsManual(true);
                    mGraph.getViewport().setMinX(mValuesModel.getLeftBorder() - AXIS_PADDING);
                    mGraph.getViewport().setMaxX(mValuesModel.getRightBorder() + AXIS_PADDING);
                    Map<String, Double> resMap = null;
                    try {
                        resMap = new GraphSolver(mValuesModel.getFunction(), mValuesModel.getFunction1(), mValuesModel.getLeftBorder()
                                ,mValuesModel.getRightBorder(), mValuesModel.getEps()).solve(SolveMethod.Newton);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    ArrayList<DataPoint> points1 = new ArrayList<>();
                    ArrayList<DataPoint> points2 = new ArrayList<>();
                    for (double i = mValuesModel.getLeftBorder() - AXIS_PADDING; i < mValuesModel.getRightBorder() + AXIS_PADDING; i += 0.001) {
                        points1.add(
                                new DataPoint(i, mValuesModel.getFunction().calculate(i,resMap.get("y")))
                        );
                        points2.add(
                          new DataPoint(i, mValuesModel.getFunction1().calculate(i, resMap.get("y")))
                        );
                    }

                    LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(points1.toArray(new DataPoint[points1.size()]));
                    LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(points2.toArray(new DataPoint[points2.size()]));

                    LineGraphSeries<DataPoint> xSeries = new LineGraphSeries<>(new DataPoint[]{
                            new DataPoint(resMap.get("x"), 0)
                    });

                    if (resMap.get("x") < mValuesModel.getLeftBorder() || resMap.get("x") > mValuesModel.getRightBorder()) {
                        Toast.makeText(GraphFragment.this.getContext(), "Найденный корень не входит в заданный интервал.", Toast.LENGTH_SHORT)
                                .show();
                        xSeries.setColor(Color.parseColor("red"));
                    } else {
                        xSeries.setColor(Color.parseColor("green"));
                    }

                    xSeries.setDrawDataPoints(true);
                    xSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
                        @Override
                        public void onTap(Series series, DataPointInterface dataPoint) {
                            Toast.makeText(GraphFragment.this.getContext(), "(" + dataPoint.getX() + ", " + dataPoint.getY() + ")", Toast.LENGTH_SHORT).show();
                        }
                    });

                    mGraph.getSeries().clear();


                    series1.setColor(Color.parseColor("#C71585"));
                    series1.setThickness(5);
                    series2.setThickness(5);
                    mGraph.addSeries(series1);
                    mGraph.addSeries(series2);
                    mGraph.addSeries(xSeries);
                    mGraph.setScaleX(1.0f);
                    mGraph.setScaleY(1.0f);
                }
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
