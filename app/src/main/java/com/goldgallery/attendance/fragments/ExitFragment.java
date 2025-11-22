package com.goldgallery.attendance.fragments;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.goldgallery.attendance.DatabaseHelper;
import com.goldgallery.attendance.Employee;
import com.goldgallery.attendance.R;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;
import java.util.List;

public class ExitFragment extends Fragment {

    private Spinner spinnerEmployees, spinnerEarlyMinutes;
    private TextInputEditText etExitTime, etDate;
    private Button btnSubmit;
    private DatabaseHelper dbHelper;

    public ExitFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exit, container, false);
        
        dbHelper = new DatabaseHelper(getContext());
        initializeViews(view);
        setupSpinners();
        setupTimePickers();
        setupSubmitButton();
        
        return view;
    }

    private void initializeViews(View view) {
        spinnerEmployees = view.findViewById(R.id.spinnerEmployees);
        spinnerEarlyMinutes = view.findViewById(R.id.spinnerEarlyMinutes);
        etExitTime = view.findViewById(R.id.etExitTime);
        etDate = view.findViewById(R.id.etDate);
        btnSubmit = view.findViewById(R.id.btnSubmit);
    }

    private void setupSpinners() {
        // اسپینر پرسنل
        List<Employee> employees = dbHelper.getAllEmployees();
        ArrayAdapter<Employee> employeeAdapter = new ArrayAdapter<>(
            getContext(), android.R.layout.simple_spinner_item, employees);
        employeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEmployees.setAdapter(employeeAdapter);

        // اسپینر دقیقه زودتر رفته
        String[] earlyMinutes = {"15", "30", "45", "60", "90", "120"};
        ArrayAdapter<String> earlyAdapter = new ArrayAdapter<>(
            getContext(), android.R.layout.simple_spinner_item, earlyMinutes);
        earlyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEarlyMinutes.setAdapter(earlyAdapter);
    }

    private void setupTimePickers() {
        Calendar calendar = Calendar.getInstance();
        
        etExitTime.setOnClickListener(v -> {
            TimePickerDialog timePicker = new TimePickerDialog(getContext(),
                    (view, hour, minute) -> {
                        String time = String.format("%02d:%02d", hour, minute);
                        etExitTime.setText(time);
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true);
            timePicker.show();
        });

        etDate.setOnClickListener(v -> {
            android.app.DatePickerDialog datePicker = new android.app.DatePickerDialog(getContext(),
                    (view, year, month, day) -> {
                        String date = String.format("%04d/%02d/%02d", year, month + 1, day);
                        etDate.setText(date);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePicker.show();
        });
    }

    private void setupSubmitButton() {
        btnSubmit.setOnClickListener(v -> {
            if (validateForm()) {
                Employee selectedEmployee = (Employee) spinnerEmployees.getSelectedItem();
                int earlyMinutes = Integer.parseInt(spinnerEarlyMinutes.getSelectedItem().toString());
                String exitTime = etExitTime.getText().toString();
                String date = etDate.getText().toString();

                long result = dbHelper.addEarlyExit(selectedEmployee.getId(), earlyMinutes, exitTime, date);
                
                if (result != -1) {
                    Toast.makeText(getContext(), "خروج زودهنگام با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
                    clearForm();
                } else {
                    Toast.makeText(getContext(), "خطا در ثبت خروج", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateForm() {
        if (spinnerEmployees.getSelectedItem() == null) {
            Toast.makeText(getContext(), "لطفا پرسنل را انتخاب کنید", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etExitTime.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "لطفا ساعت خروج را انتخاب کنید", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etDate.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "لطفا تاریخ را انتخاب کنید", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void clearForm() {
        etExitTime.setText("");
        etDate.setText("");
        spinnerEarlyMinutes.setSelection(0);
    }
          }
