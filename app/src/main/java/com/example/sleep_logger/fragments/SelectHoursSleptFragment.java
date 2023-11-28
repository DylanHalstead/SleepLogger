package com.example.sleep_logger.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.sleep_logger.databinding.FragmentSelectHoursSleptBinding;

import java.util.Arrays;
import java.util.stream.DoubleStream;

public class SelectHoursSleptFragment extends Fragment {
    double[] hourSections = DoubleStream.iterate(0.5, d -> d + 0.5).limit(30).toArray();

    public SelectHoursSleptFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Select Hours Slept");
    }

    FragmentSelectHoursSleptBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSelectHoursSleptBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, Arrays.stream(hourSections).mapToObj(d -> String.format("%.1f hours", d)).toArray(String[]::new));
        binding.listView.setAdapter(adapter);
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.sendHoursSlept(hourSections[position]);
                mListener.cancel();
            }
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.cancel();
            }
        });
    }

    SelectHoursSleptListener mListener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof SelectHoursSleptListener) {
            mListener = (SelectHoursSleptListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SelectHoursSleptListener");
        }
    }

    public interface SelectHoursSleptListener{
        void cancel();
        void sendHoursSlept(Double hoursSlept);
    }
}