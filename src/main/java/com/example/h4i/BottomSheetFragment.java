package com.example.h4i;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.ViewModelProvider;

import com.example.h4i.model.Priority;
import com.example.h4i.model.SharedViewModel;
import com.example.h4i.model.Task;
import com.example.h4i.model.TaskViewModel;
import com.example.h4i.util.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;
import java.util.Date;

public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener{
    private EditText enterTodo;
    private ImageButton calendarButton;
    private ImageButton priorityButton;
    private ImageButton timeButton;
    private RadioGroup priorityRadioGroup;
    private RadioButton selectedRadioButton;
    private int selectedButtonId;
    private ImageButton saveButton;
    private CalendarView calendarView;
    private Group calendarGroup;
    private Date dueDate;
    Calendar calendar=Calendar.getInstance();
    private SharedViewModel sharedViewModel;
    private boolean isEdit;
    private Priority priority;
    private MaterialTimePicker picker;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    public BottomSheetFragment() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.bottom_sheet, container, false);
        createNotificationChannel();
        calendarGroup=view.findViewById(R.id.calendar_group);
        calendarView=view.findViewById(R.id.calendar_view);
        calendarButton=view.findViewById(R.id.today_calendar_button);
        enterTodo=view.findViewById(R.id.enter_todo_et);
        saveButton=view.findViewById(R.id.save_todo_button);
        priorityButton=view.findViewById(R.id.priority_todo_button);
        timeButton=view.findViewById(R.id.time_todo_button);
        priorityRadioGroup=view.findViewById(R.id.radioGroup_priority);

        Chip todayChip=view.findViewById(R.id.today_chip);
        todayChip.setOnClickListener(this);
        Chip tomorrowChip=view.findViewById(R.id.tomorrow_chip);
        tomorrowChip.setOnClickListener(this);
        Chip nextWeekChip=view.findViewById(R.id.next_week_chip);
        nextWeekChip.setOnClickListener(this);

        return view;
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name="TodoReminderChannel";
            String description="Chanel for Alarm Manager";
            int importance= NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel=new NotificationChannel("H4i",name,importance);
            channel.setDescription(description);


            NotificationManager notificationManager=getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharedViewModel.getSelectedItem().getValue()!=null){
            isEdit=sharedViewModel.getIsEdit();
            Task task=sharedViewModel.getSelectedItem().getValue();
            enterTodo.setText(task.getTask());
            Log.d("MY","onViewCreated: "+isEdit);
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel=new ViewModelProvider(requireActivity())
                .get(SharedViewModel.class);

        calendarButton.setOnClickListener(v -> {
            calendarGroup.setVisibility(
                    calendarGroup.getVisibility()==View.GONE?View.VISIBLE : View.GONE);
            Utils.hideSofKeyboard(v);
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar.clear();
                calendar.set(year,month,dayOfMonth);
                dueDate=calendar.getTime();
                // Log.d("Cal","onViewCreated:==> month"+(month+1)+", dayOfMonth "+dayOfMonth );
            }
        });

        priorityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideSofKeyboard(v);
                priorityRadioGroup.setVisibility(
                        priorityRadioGroup.getVisibility()==View.GONE?View.VISIBLE:View.GONE
                );
                priorityRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    if(priorityRadioGroup.getVisibility()==View.VISIBLE){
                        selectedButtonId=checkedId;
                        selectedRadioButton=view.findViewById(selectedButtonId);
                        if(selectedRadioButton.getId()== R.id.radioButton_high){
                            priority=Priority.HIGH;
                        } else if(selectedRadioButton.getId()== R.id.radioButton_med){
                            priority=Priority.MEDIUM;
                        }else if(selectedRadioButton.getId()== R.id.radioButton_low){
                            priority=Priority.LOW;
                        }else{
                            priority=Priority.LOW;
                        }
                    }else{
                        priority=Priority.LOW;
                    }
                });
            }
        });
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task=enterTodo.getText().toString().trim();
                if(!TextUtils.isEmpty(task)&&dueDate!=null&&priority!=null){
                    Task myTask=new Task(task, priority,
                            dueDate,Calendar.getInstance().getTime(),
                            false);
                    setAlarm();
                    if(isEdit){
                        Task updateTask=sharedViewModel.getSelectedItem().getValue();
                        updateTask.setTask(task);
                        updateTask.setDateCreated(Calendar.getInstance().getTime());
                        updateTask.setPriority(priority);
                        updateTask.setDueDate(dueDate);
                        TaskViewModel.update(updateTask);
                        sharedViewModel.setIsEdit(false);
                        Log.d("TIME","isEdit: "+task);
                    }else {
                        TaskViewModel.insert(myTask);
                   }
                    enterTodo.setText("");

                }else {
                    Snackbar.make(saveButton, R.string.empty_field,Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });

    }

    private void setAlarm() {
        alarmManager=(AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(getActivity(),AlarmReceiver.class);
        pendingIntent=PendingIntent.getBroadcast(getActivity(),0,intent,0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,pendingIntent);
        Toast.makeText(getActivity(),"Reminder set successfully",Toast.LENGTH_SHORT).show();
    }

    private void showTimePicker() {
        picker=new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Alarm Time")
                .build();

        picker.show(getActivity().getSupportFragmentManager(), "H4i");
        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(picker.getHour()>12){

                }
                calendar.set(Calendar.HOUR_OF_DAY,picker.getHour());
                calendar.set(Calendar.MINUTE,picker.getMinute());
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);

            }
        });

    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if(id== R.id.today_chip){
            calendar.add(Calendar.DAY_OF_YEAR,0);
            dueDate=calendar.getTime();
            Log.d("TIME","onClick: "+dueDate.toString());
        }else if(id== R.id.tomorrow_chip){
            calendar.add(Calendar.DAY_OF_YEAR,1);
            dueDate=calendar.getTime();
            Log.d("TIME","onClick: "+dueDate.toString());
        }
        else if (id== R.id.next_week_chip){
            calendar.add(Calendar.DAY_OF_YEAR,7);
            dueDate=calendar.getTime();
            Log.d("TIME","onClick: "+dueDate.toString());
        }

    }
}