package com.example.sleep_logger.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.example.sleep_logger.AppDatabase;
import com.example.sleep_logger.R;
import com.example.sleep_logger.databinding.FragmentChartBinding;
import com.example.sleep_logger.databinding.FragmentLogVisualizerBinding;
import com.example.sleep_logger.models.Log;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogVisualizerFragment extends Fragment {

    public LogVisualizerFragment() {
        // Required empty public constructor
    }

    static ArrayList<Log> mLogs = new ArrayList<>();
    AppDatabase db;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentLogVisualizerBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLogVisualizerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    DemoCollectionAdapter demoCollectionAdapter;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = Room.databaseBuilder(getActivity(), AppDatabase.class, "logs-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        mLogs.addAll(db.logDao().getAll());

        demoCollectionAdapter = new DemoCollectionAdapter(this);
        binding.pager.setAdapter(demoCollectionAdapter);
        new TabLayoutMediator(binding.tabLayout, binding.pager,
                (tab, position) -> tab.setText(getCurrentChartText(position))
        ).attach();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.cancel();
            }
        });
    }

    String getCurrentChartText(int i) {
        switch (i) {
            case 0:
                return "Hours Slept";
            case 1:
                return "Sleep Quality";
            case 2:
                return "Minutes Exercised";
            case 3:
                return "Weight";
            default:
                return "ERROR";
        }
    }

    public class DemoCollectionAdapter extends FragmentStateAdapter {
        public DemoCollectionAdapter(Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment = new ChartFragment();
            Bundle args = new Bundle();
            args.putInt(ChartFragment.ARG_CHART, position);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }

    public static class ChartFragment extends Fragment {
        public static final String ARG_CHART = "chart";

        FragmentChartBinding binding;
        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            binding = FragmentChartBinding.inflate(inflater, container, false);
            return binding.getRoot();
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            Bundle args = getArguments();
            Integer currentGraphIndex = args.getInt(ARG_CHART);

            if(currentGraphIndex != null){
                switch(currentGraphIndex){
                    case 0:
                        createGraphByHoursSlept();
                        break;
                    case 1:
                        createGraphBySleepQuality();
                        break;
                    case 2:
                        createGraphByMinutesExercised();
                        break;
                    case 3:
                        createGraphByWeight();
                        break;
                    default:
                        android.util.Log.d("test", "onViewCreated on pager: something broke");
                }
            }
        }

        void createGraphByHoursSlept() {
            Cartesian cartesian = AnyChart.line();
            cartesian.animation(true);
            cartesian.padding(10d, 20d, 5d, 20d);
            cartesian.crosshair().enabled(true);
            cartesian.crosshair()
                    .yLabel(true)
                    .yStroke((Stroke) null, 2, null, (String) null, (String) null);
            cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
            cartesian.title("Sleep Trends by Hours Slept");
            cartesian.yAxis(0).title("Hours Slept");
            cartesian.xAxis(0).title("Time").labels().padding(5d, 5d, 5d, 5d);

            List<DataEntry> hoursData = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            for (Log log:mLogs) {
                hoursData.add(new ValueDataEntry(sdf.format(new Date(log.getDate().getTime())), log.getSleepHours()));
            }

            Set set = Set.instantiate();
            set.data(hoursData);
            Line hoursLine = cartesian.line(hoursData);
            hoursLine.hovered().markers().enabled(true);
            hoursLine.hovered().markers()
                    .type(MarkerType.CIRCLE)
                    .size(4d);
            hoursLine.tooltip()
                    .position("right")
                    .anchor(Anchor.LEFT_CENTER)
                    .offsetX(5d)
                    .offsetY(5d);

            binding.anyChartView.setChart(cartesian);
        }

        void createGraphBySleepQuality() {
            Cartesian cartesian = AnyChart.line();
            cartesian.animation(true);
            cartesian.padding(10d, 20d, 5d, 20d);
            cartesian.crosshair().enabled(true);
            cartesian.crosshair()
                    .yLabel(true)
                    .yStroke((Stroke) null, 2, null, (String) null, (String) null);
            cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
            cartesian.title("Sleep Trends by Sleep Quality");
            cartesian.yAxis(0).title("Sleep Quality");
            cartesian.xAxis(0).title("Time").labels().padding(5d, 5d, 5d, 5d);

            List<DataEntry> qualityData = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            for (Log log:mLogs) {
                qualityData.add(new ValueDataEntry(sdf.format(new Date(log.getDate().getTime())), log.getQuality()));
            }

            Set set = Set.instantiate();
            set.data(qualityData);
            Line qualityLine = cartesian.line(qualityData);
            qualityLine.hovered().markers().enabled(true);
            qualityLine.hovered().markers()
                    .type(MarkerType.CIRCLE)
                    .size(4d);
            qualityLine.tooltip()
                    .position("right")
                    .anchor(Anchor.LEFT_CENTER)
                    .offsetX(5d)
                    .offsetY(5d);

            binding.anyChartView.setChart(cartesian);
        }

        void createGraphByMinutesExercised() {
            Cartesian cartesian = AnyChart.line();
            cartesian.animation(true);
            cartesian.padding(10d, 20d, 5d, 20d);
            cartesian.crosshair().enabled(true);
            cartesian.crosshair()
                    .yLabel(true)
                    .yStroke((Stroke) null, 2, null, (String) null, (String) null);
            cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
            cartesian.title("Sleep Trends by Minutes Exercised");
            cartesian.yAxis(0).title("Minutes Exercised");
            cartesian.xAxis(0).title("Time").labels().padding(5d, 5d, 5d, 5d);

            List<DataEntry> minutesData = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            for (Log log:mLogs) {
                minutesData.add(new ValueDataEntry(sdf.format(new Date(log.getDate().getTime())), log.getExerciseMinutes()));
            }

            Set set = Set.instantiate();
            set.data(minutesData);
            Line minutesLine = cartesian.line(minutesData);
            minutesLine.hovered().markers().enabled(true);
            minutesLine.hovered().markers()
                    .type(MarkerType.CIRCLE)
                    .size(4d);
            minutesLine.tooltip()
                    .position("right")
                    .anchor(Anchor.LEFT_CENTER)
                    .offsetX(5d)
                    .offsetY(5d);

            binding.anyChartView.setChart(cartesian);
        }
        void createGraphByWeight() {
            Cartesian cartesian = AnyChart.line();
            cartesian.animation(true);
            cartesian.padding(10d, 20d, 5d, 20d);
            cartesian.crosshair().enabled(true);
            cartesian.crosshair()
                    .yLabel(true)
                    .yStroke((Stroke) null, 2, null, (String) null, (String) null);
            cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
            cartesian.title("Sleep Trends by Weight");
            cartesian.yAxis(0).title("Weight");
            cartesian.xAxis(0).title("Time").labels().padding(5d, 5d, 5d, 5d);

            List<DataEntry> weightData = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            for (Log log:mLogs) {
                weightData.add(new ValueDataEntry(sdf.format(new Date(log.getDate().getTime())), log.getWeight()));
            }

            Set set = Set.instantiate();
            set.data(weightData);
            Line weightLine = cartesian.line(weightData);
            weightLine.hovered().markers().enabled(true);
            weightLine.hovered().markers()
                    .type(MarkerType.CIRCLE)
                    .size(4d);
            weightLine.tooltip()
                    .position("right")
                    .anchor(Anchor.LEFT_CENTER)
                    .offsetX(5d)
                    .offsetY(5d);

            binding.anyChartView.setChart(cartesian);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Log Visualizations");
    }

    LogVisualizerListener mListener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof LogsFragment.LogsListener) {
            mListener = (LogVisualizerListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement LogVisualizerListener");
        }
    }

    public interface LogVisualizerListener{
        void cancel();
    }
}
