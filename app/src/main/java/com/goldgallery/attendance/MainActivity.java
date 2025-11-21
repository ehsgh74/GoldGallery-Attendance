package com.goldgallery.attendance;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        
        // تنظیم جهت راست به چپ
        getWindow().getDecorView().setLayoutDirection(android.view.View.LAYOUT_DIRECTION_RTL);
        
        navigationView.setNavigationItemSelectedListener(this);
        
        // نمایش فرگمنت پیش‌فرض
        if (savedInstanceState == null) {
            showFragment(new LeaveFragment());
            navigationView.setCheckedItem(R.id.nav_leave);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_leave) {
            fragment = new LeaveFragment();
            setTitle("ثبت مرخصی");
        } else if (id == R.id.nav_delay) {
            fragment = new DelayFragment();
            setTitle("ثبت تاخیر");
        } else if (id == R.id.nav_exit) {
            fragment = new ExitFragment();
            setTitle("ثبت خروج زودهنگام");
        } else if (id == R.id.nav_reports) {
            fragment = new ReportsFragment();
            setTitle("گزارش عملکرد");
        } else if (id == R.id.nav_employees) {
            fragment = new EmployeesFragment();
            setTitle("مدیریت پرسنل");
        }

        if (fragment != null) {
            showFragment(fragment);
        }

        drawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            } else {
                drawerLayout.openDrawer(GravityCompat.END);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    }
