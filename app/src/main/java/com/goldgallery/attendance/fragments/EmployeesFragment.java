package com.goldgallery.attendance.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.goldgallery.attendance.DatabaseHelper;
import com.goldgallery.attendance.Employee;
import com.goldgallery.attendance.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class EmployeesFragment extends Fragment {

    private EditText etEmployeeName;
    private Button btnAddEmployee;
    private ListView listViewEmployees;
    private FloatingActionButton fabAdd;
    private DatabaseHelper dbHelper;
    private List<Employee> employeesList;
    private ArrayAdapter<Employee> adapter;

    public EmployeesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employees, container, false);
        
        dbHelper = new DatabaseHelper(getContext());
        initializeViews(view);
        setupEmployeesList();
        setupAddButton();
        
        return view;
    }

    private void initializeViews(View view) {
        etEmployeeName = view.findViewById(R.id.etEmployeeName);
        btnAddEmployee = view.findViewById(R.id.btnAddEmployee);
        listViewEmployees = view.findViewById(R.id.listViewEmployees);
        fabAdd = view.findViewById(R.id.fabAdd);
    }

    private void setupEmployeesList() {
        employeesList = dbHelper.getAllEmployees();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, employeesList);
        listViewEmployees.setAdapter(adapter);
    }

    private void setupAddButton() {
        btnAddEmployee.setOnClickListener(v -> {
            String employeeName = etEmployeeName.getText().toString().trim();
            if (!employeeName.isEmpty()) {
                long result = dbHelper.addEmployee(employeeName);
                if (result != -1) {
                    Toast.makeText(getContext(), "پرسنل با موفقیت اضافه شد", Toast.LENGTH_SHORT).show();
                    etEmployeeName.setText("");
                    refreshEmployeesList();
                } else {
                    Toast.makeText(getContext(), "خطا در افزودن پرسنل", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "لطفا نام پرسنل را وارد کنید", Toast.LENGTH_SHORT).show();
            }
        });

        fabAdd.setOnClickListener(v -> {
            etEmployeeName.setVisibility(View.VISIBLE);
            btnAddEmployee.setVisibility(View.VISIBLE);
        });
    }

    private void refreshEmployeesList() {
        employeesList.clear();
        employeesList.addAll(dbHelper.getAllEmployees());
        adapter.notifyDataSetChanged();
    }
                      }
