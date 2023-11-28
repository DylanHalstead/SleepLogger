package com.example.sleep_logger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.sleep_logger.fragments.AddLogFragment;
import com.example.sleep_logger.fragments.DatePickerFragment;
import com.example.sleep_logger.fragments.LogVisualizerFragment;
import com.example.sleep_logger.fragments.LogsFragment;
import com.example.sleep_logger.fragments.SelectExerciseMinutesFragment;
import com.example.sleep_logger.fragments.SelectHoursSleptFragment;
import com.example.sleep_logger.fragments.SelectSleepQualityFragment;
import com.example.sleep_logger.fragments.TimePickerFragment;

import java.time.LocalDate;
import java.time.LocalTime;

public class MainActivity extends AppCompatActivity implements LogsFragment.LogsListener, AddLogFragment.AddLogListener, DatePickerFragment.DatePickerListener, TimePickerFragment.TimePickerListener, SelectHoursSleptFragment.SelectHoursSleptListener, SelectSleepQualityFragment.SelectSleepQualityListener, SelectExerciseMinutesFragment.SelectExerciseMinutesListener, LogVisualizerFragment.LogVisualizerListener {

    final String ADD_LOG_TAG = "ADD_LOG_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, new LogsFragment())
                .commit();
    }

    @Override
    public void gotoAddLog() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, new AddLogFragment(), ADD_LOG_TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoVisualizeLogs() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, new LogVisualizerFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToHoursSleptPicker() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, new SelectHoursSleptFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToSleepQualityPicker() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, new SelectSleepQualityFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToExerciseLengthPicker() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, new SelectExerciseMinutesFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void cancel() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void sendDate(LocalDate date) {
        AddLogFragment fragment = (AddLogFragment) getSupportFragmentManager().findFragmentByTag(ADD_LOG_TAG);
        if(fragment != null){
            fragment.setSelectedDate(date);
        }
    }

    @Override
    public void sendTime(LocalTime time) {
        AddLogFragment fragment = (AddLogFragment) getSupportFragmentManager().findFragmentByTag(ADD_LOG_TAG);
        if(fragment != null){
            fragment.setSelectedTime(time);
        }
    }

    @Override
    public void sendHoursSlept(Double hoursSlept) {
        AddLogFragment fragment = (AddLogFragment) getSupportFragmentManager().findFragmentByTag(ADD_LOG_TAG);
        if(fragment != null){
            fragment.setHoursSlept(hoursSlept);
        }
    }

    @Override
    public void sendSleepQuality(int sleepQuality) {
        AddLogFragment fragment = (AddLogFragment) getSupportFragmentManager().findFragmentByTag(ADD_LOG_TAG);
        if(fragment != null){
            fragment.setSleepQuality(sleepQuality);
        }
    }

    @Override
    public void sendExerciseMinutes(Integer exerciseMinutes) {
        AddLogFragment fragment = (AddLogFragment) getSupportFragmentManager().findFragmentByTag(ADD_LOG_TAG);
        if(fragment != null){
            fragment.setExerciseMinutes(exerciseMinutes);
        }
    }
}