package com.goldgallery.attendance.fragments;

import android.app.DatePickerDialog;
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

public class LeaveFragment extends Fragment {

    private Spinner spinnerEmployees, spinnerShift;
    private TextInputEditText etStartDate, etEndDate;
    private Button btnSubmit;
    private DatabaseHelper dbHelper;

    public LeaveFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leave, container, false);
        
        dbHelper = new DatabaseHelper(getContext());
        initializeViews(view);
        setupSpinners();
        setupDatePickers();
        setupSubmitButton();
        
        return view;
    }

    private void initializeViews(View view) {
        spinnerEmployees = view.findViewById(R.id.spinnerEmployees);
        spinnerShift = view.findViewById(R.id.spinnerShift);
        etStartDate = view.findViewById(R.id.etStartDate);
        etEndDate = view.findViewById(R.id.etEndDate);
        btnSubmit = view.findViewById(R.id.btnSubmit);
    }

    private void setupSpinners() {
        // اسپینر پرسنل
        List<Employee> employees = dbHelper.getAllEmployees();
        ArrayAdapter<Employee> employeeAdapter = new ArrayAdapter<>(
            getContext(), android.R.layout.simple_spinner_item, employees);
        employeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEmployees.setAdapter(employeeAdapter);

        // اسپینر شیفت
        String[] shifts = {"شیفت صبح", "شیفت عصر", "تمام شیفت"};
        ArrayAdapter<String> shiftAdapter = new ArrayAdapter<>(
            getContext(), android.R.layout.simple_spinner_item, shifts);
        shiftAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerShift.setAdapter(shiftAdapter);
    }

    private void setupDatePickers() {
        Calendar calendar = Calendar.getInstance();
        
        etStartDate.setOnClickListener(v -> {
            DatePickerDialog datePicker = new DatePickerDialog(getContext(),
                    (view, year, month, day) -> {
                        String date = String.format("%04d/%02d/%02d", year, month + 1, day);
                        etStartDate.setText(date);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePicker.show();
        });

        etEndDate.setOnClickListener(v -> {
            DatePickerDialog datePicker = new DatePickerDialog(getContext(),
                    (view, year, month, day) -> {
                        String date = String.format("%04d/%02d/%02d", year, month + 1, day);
                        etEndDate.setText(date);
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
                String shiftType = spinnerShift.getSelectedItem().toString();
                String startDate = etStartDate.getText().toString();
                String endDate = etEndDate.getText().toString();

                long result = dbHelper.addLeave(selectedEmployee.getId(), startDate, endDate, shiftType);
                
                if (result != -1) {
                    Toast.makeText(getContext(), "مرخصی با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
                    clearForm();
                } else {
                    Toast.makeText(getContext(), "خطا در ثبت مرخصی", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateForm() {
        if (spinnerEmployees.getSelectedItem() == null) {
            Toast.makeText(getContext(), "لطفا پرسنل را انتخاب کنید", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etStartDate.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "لطفا تاریخ شروع را انتخاب کنید", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etEndDate.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "لطفا تاریخ پایان را انتخاب کنید", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void clearForm() {
        etStartDate.setText("");
        etEndDate.setText("");
        spinnerShift.setSelection(0);
    }
          }
