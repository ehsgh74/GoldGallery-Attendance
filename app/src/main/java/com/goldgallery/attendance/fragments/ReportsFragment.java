package com.goldgallery.attendance.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.goldgallery.attendance.DatabaseHelper;
import com.goldgallery.attendance.Employee;
import com.goldgallery.attendance.R;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;

public class ReportsFragment extends Fragment {

    private Spinner spinnerEmployees;
    private TextInputEditText etStartDate, etEndDate;
    private Button btnGenerateReport;
    private TextView tvReportResult;
    private DatabaseHelper dbHelper;

    public ReportsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        
        dbHelper = new DatabaseHelper(getContext());
        initializeViews(view);
        setupSpinners();
        setupDatePickers();
        setupReportButton();
        
        return view;
    }

    private void initializeViews(View view) {
        spinnerEmployees = view.findViewById(R.id.spinnerEmployees);
        etStartDate = view.findViewById(R.id.etStartDate);
        etEndDate = view.findViewById(R.id.etEndDate);
        btnGenerateReport = view.findViewById(R.id.btnGenerateReport);
        tvReportResult = view.findViewById(R.id.tvReportResult);
    }

    private void setupSpinners() {
        List<Employee> employees = dbHelper.getAllEmployees();
        ArrayAdapter<Employee> employeeAdapter = new ArrayAdapter<>(
            getContext(), android.R.layout.simple_spinner_item, employees);
        employeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEmployees.setAdapter(employeeAdapter);
    }

    private void setupDatePickers() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        
        etStartDate.setOnClickListener(v -> {
            android.app.DatePickerDialog datePicker = new android.app.DatePickerDialog(getContext(),
                    (view, year, month, day) -> {
                        String date = String.format("%04d/%02d/%02d", year, month + 1, day);
                        etStartDate.setText(date);
                    },
                    calendar.get(java.util.Calendar.YEAR),
                    calendar.get(java.util.Calendar.MONTH),
                    calendar.get(java.util.Calendar.DAY_OF_MONTH));
            datePicker.show();
        });

        etEndDate.setOnClickListener(v -> {
            android.app.DatePickerDialog datePicker = new android.app.DatePickerDialog(getContext(),
                    (view, year, month, day) -> {
                        String date = String.format("%04d/%02d/%02d", year, month + 1, day);
                        etEndDate.setText(date);
                    },
                    calendar.get(java.util.Calendar.YEAR),
                    calendar.get(java.util.Calendar.MONTH),
                    calendar.get(java.util.Calendar.DAY_OF_MONTH));
            datePicker.show();
        });
    }

    private void setupReportButton() {
        btnGenerateReport.setOnClickListener(v -> {
            if (validateForm()) {
                Employee selectedEmployee = (Employee) spinnerEmployees.getSelectedItem();
                String startDate = etStartDate.getText().toString();
                String endDate = etEndDate.getText().toString();
                
                // نمایش گزارش نمونه
                String report = generateSampleReport(selectedEmployee, startDate, endDate);
                tvReportResult.setText(report);
                Toast.makeText(getContext(), "گزارش تولید شد", Toast.LENGTH_SHORT).show();
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

    private String generateSampleReport(Employee employee, String startDate, String endDate) {
        return "گزارش عملکرد " + employee.getName() + "\n\n" +
               "بازه زمانی: " + startDate + " تا " + endDate + "\n\n" +
               "📊 خلاصه عملکرد:\n" +
               "• تعداد روزهای مرخصی: ۲ روز\n" +
               "• مجموع تاخیرها: ۴۵ دقیقه\n" +
               "• خروج‌های زودهنگام: ۳ بار\n" +
               "• مجموع ساعت کاری: ۱۶۰ ساعت\n\n" +
               "📈 عملکرد کلی: خوب ✅";
    }
          }
