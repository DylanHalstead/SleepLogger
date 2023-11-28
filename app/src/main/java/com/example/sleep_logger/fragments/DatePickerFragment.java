package com.example.sleep_logger.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.widget.DatePicker;

import java.time.LocalDate;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(requireContext(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        mListener.sendDate(LocalDate.of(year, month+1, day));
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getActivity().getSupportFragmentManager(), "TimePicker");
    }

    DatePickerListener mListener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof DatePickerListener) {
            mListener = (DatePickerListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement DatePickerListener");
        }
    }

    public interface DatePickerListener{
        void sendDate(LocalDate date);
    }
}
