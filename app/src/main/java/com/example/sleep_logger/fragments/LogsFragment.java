package com.example.sleep_logger.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.sleep_logger.AppDatabase;
import com.example.sleep_logger.R;
import com.example.sleep_logger.databinding.FragmentLogsBinding;
import com.example.sleep_logger.databinding.LogListItemBinding;
import com.example.sleep_logger.models.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LogsFragment extends Fragment {

    public LogsFragment() {
        // Required empty public constructor
    }

    ArrayList<Log> mLogs = new ArrayList<>();
    AppDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    FragmentLogsBinding binding;
    LogsAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLogsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new LogsAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        db = Room.databaseBuilder(getActivity(), AppDatabase.class, "logs-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

//        db.logDao().nukeTable();
//        Log testLog = new Log();
//        Log testLog2 = new Log(7.5, 68, 180);
//        db.logDao().insertAll(testLog2);

        loadAndDisplayData();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.add_log){
            mListener.gotoAddLog();
            return true;
        } else if(item.getItemId() == R.id.visualize) {
            mListener.gotoVisualizeLogs();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void loadAndDisplayData(){
        mLogs.clear();
        mLogs.addAll(db.logDao().getAll());
        adapter.notifyDataSetChanged();
    }

    class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.LogsViewHolder>{
        @NonNull
        @Override
        public LogsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LogListItemBinding itemBinding = LogListItemBinding.inflate(getLayoutInflater(), parent, false);
            return new LogsViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull LogsViewHolder holder, int position) {
            holder.setupUI(mLogs.get(position));
        }

        @Override
        public int getItemCount() {
            return mLogs.size();
        }

        class LogsViewHolder extends RecyclerView.ViewHolder{
            LogListItemBinding itemBinding;
            Log mLog;
            public LogsViewHolder(LogListItemBinding itemBinding) {
                super(itemBinding.getRoot());
                this.itemBinding = itemBinding;
            }

            public void setupUI(Log item){
                this.mLog = item;
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                itemBinding.textViewLogDate.setText(sdf.format(new Date(mLog.getDate().getTime())));
                itemBinding.textViewHoursSlept.setText(String.format("%.2f Hours Slept", mLog.getSleepHours()));
                itemBinding.textViewSleepQuality.setText("Sleep Quality: " + mLog.getQuality() + " out of 5");
                itemBinding.textViewWeight.setText(String.format("%.2f", mLog.getWeight()));
                itemBinding.textViewMinutesExercised.setText(String.format("%.2f", mLog.getExerciseMinutes()));

                itemBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.logDao().delete(mLog);
                        loadAndDisplayData();
                    }
                });
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Logs");
    }

    LogsListener mListener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof LogsListener) {
            mListener = (LogsListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement LogsListener");
        }
    }

    public interface LogsListener{
        void gotoAddLog();
        void gotoVisualizeLogs();
    }
}