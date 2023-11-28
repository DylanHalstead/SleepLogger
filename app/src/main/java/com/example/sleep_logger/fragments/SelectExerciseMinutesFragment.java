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

import com.example.sleep_logger.databinding.FragmentSelectExerciseMinutesBinding;

import java.util.Arrays;
import java.util.stream.IntStream;

public class SelectExerciseMinutesFragment extends Fragment {
    int[] minuteSections = IntStream.iterate(30, i -> i + 30).limit(30).toArray();

    public SelectExerciseMinutesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Select Exercise Length (in Minutes)");
    }

    FragmentSelectExerciseMinutesBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSelectExerciseMinutesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, Arrays.stream(minuteSections).mapToObj(String::valueOf).toArray(String[]::new));
        binding.listView.setAdapter(adapter);
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.sendExerciseMinutes(Integer.valueOf(minuteSections[position]));
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

    SelectExerciseMinutesListener mListener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof SelectExerciseMinutesListener) {
            mListener = (SelectExerciseMinutesListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SelectExerciseMinutesListener");
        }
    }

    public interface SelectExerciseMinutesListener{
        void cancel();
        void sendExerciseMinutes(Integer exerciseMinutes);
    }
}