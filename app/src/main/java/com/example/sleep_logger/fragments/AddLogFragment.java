package com.example.sleep_logger.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sleep_logger.AppDatabase;
import com.example.sleep_logger.databinding.FragmentAddLogBinding;
import com.example.sleep_logger.models.Log;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class AddLogFragment extends Fragment {

    LocalDate selectedDate = LocalDate.now();
    public void setSelectedDate(LocalDate selectedDate){
        this.selectedDate = selectedDate;
        setDateTime();
    }
    LocalTime selectedTime = LocalTime.now();
    public void setSelectedTime(LocalTime selectedTime){
        this.selectedTime = selectedTime;
        setDateTime();
    }
    void setDateTime() {
        LocalDateTime localDateTime = LocalDateTime.of(selectedDate, selectedTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d' 'uuuu' 'hh:mm a");
        binding.setTimeTextView.setText(localDateTime.format(formatter));
    }

    Double hoursSlept;
    public void setHoursSlept(Double hoursSlept) {
        this.hoursSlept = hoursSlept;
    }

    Integer sleepQuality;
    public void setSleepQuality(int sleepQuality) {
        this.sleepQuality = sleepQuality;
    }

    Integer exerciseMinutes;
    public void setExerciseMinutes(Integer exerciseMinutes) {
        this.exerciseMinutes = exerciseMinutes;
    }

    public AddLogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("New Log");
    }

    FragmentAddLogBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddLogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDateTime();
        if(hoursSlept != null) {
            binding.hoursSleptTextView.setText("Hours Slept: " + hoursSlept);
        }
        if(sleepQuality != null) {
            String qualityString;
            switch(sleepQuality){
                case 1:
                    qualityString = "Poor";
                    break;
                case 2:
                    qualityString = "Fair";
                    break;
                case 3:
                    qualityString = "Good";
                    break;
                case 4:
                    qualityString = "Very Good";
                    break;
                case 5:
                    qualityString = "Excellent";
                    break;
                default:
                    qualityString = "Error";
                    break;
            }
            binding.sleepQualityTextView.setText("Sleep Quality: " + qualityString);
        }
        if(exerciseMinutes != null) {
            binding.exerciseLengthTextView.setText("Exercise Minutes: " + exerciseMinutes);
        }

        binding.setTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getActivity().getSupportFragmentManager(), "DatePicker");
            }
        });

        binding.hoursSleptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToHoursSleptPicker();
            }
        });

        binding.sleepQualityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToSleepQualityPicker();
            }
        });

        binding.exerciseLengthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToExerciseLengthPicker();
            }
        });

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.cancel();
            }
        });

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hoursSlept == null) {
                    Toast.makeText(getActivity(), "Need to Select Hours Slept", Toast.LENGTH_LONG).show();
                    return;
                }
                if(sleepQuality == null) {
                    Toast.makeText(getActivity(), "Need to Select Sleep Quality", Toast.LENGTH_LONG).show();
                    return;
                }
                if(exerciseMinutes == null) {
                    Toast.makeText(getActivity(), "Need to Select Exercise Minutes", Toast.LENGTH_LONG).show();
                    return;
                }
                Double weight = Double.valueOf(binding.wieghtEditText.getText().toString());
                if(weight == null) {
                    Toast.makeText(getActivity(), "Enter Weight", Toast.LENGTH_LONG).show();
                    return;
                }

                Log newLog = new Log(Date.from(LocalDateTime.of(selectedDate, selectedTime).atZone(ZoneId.systemDefault()).toInstant()), hoursSlept, sleepQuality, exerciseMinutes, weight);
                AppDatabase db = Room.databaseBuilder(getActivity(), AppDatabase.class, "logs-db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build();
                db.logDao().insertAll(newLog);
                mListener.cancel();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("New Log");
    }

    AddLogListener mListener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof LogsFragment.LogsListener) {
            mListener = (AddLogListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement AddLogListener");
        }
    }

    public interface AddLogListener{
        void goToHoursSleptPicker();
        void goToSleepQualityPicker();
        void goToExerciseLengthPicker();
        void cancel();
    }
}