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

import com.example.sleep_logger.databinding.FragmentSelectSleepQualityBinding;

public class SelectSleepQualityFragment extends Fragment {
    String[] moods = {"Poor", "Fair", "Good", "Very Good", "Excellent"};
    public SelectSleepQualityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Select Sleep Quality");
    }

    FragmentSelectSleepQualityBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSelectSleepQualityBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, moods);
        binding.listView.setAdapter(adapter);
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.sendSleepQuality(position+1);
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

    SelectSleepQualityListener mListener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof SelectHoursSleptFragment.SelectHoursSleptListener) {
            mListener = (SelectSleepQualityListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SelectSleepQualityListener");
        }
    }

    public interface SelectSleepQualityListener{
        void cancel();
        void sendSleepQuality(int sleepQuality);
    }
}