package com.goldgallery.attendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "attendance.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_EMPLOYEES = "employees";
    private static final String TABLE_LEAVES = "leaves";
    private static final String TABLE_DELAYS = "delays";
    private static final String TABLE_EARLY_EXITS = "early_exits";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // جدول پرسنل
        db.execSQL("CREATE TABLE " + TABLE_EMPLOYEES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "is_active INTEGER DEFAULT 1)");

        // جدول مرخصی
        db.execSQL("CREATE TABLE " + TABLE_LEAVES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "employee_id INTEGER," +
                "start_date TEXT," +
                "end_date TEXT," +
                "shift_type TEXT," +
                "FOREIGN KEY(employee_id) REFERENCES employees(id))");

        // جدول تاخیر
        db.execSQL("CREATE TABLE " + TABLE_DELAYS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "employee_id INTEGER," +
                "delay_minutes INTEGER," +
                "actual_entry_time TEXT," +
                "date TEXT," +
                "FOREIGN KEY(employee_id) REFERENCES employees(id))");

        // جدول خروج زودهنگام
        db.execSQL("CREATE TABLE " + TABLE_EARLY_EXITS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "employee_id INTEGER," +
                "early_minutes INTEGER," +
                "actual_exit_time TEXT," +
                "date TEXT," +
                "FOREIGN KEY(employee_id) REFERENCES employees(id))");

        // اضافه کردن پرسنل پیش‌فرض
        addDefaultEmployees(db);
    }

    private void addDefaultEmployees(SQLiteDatabase db) {
        String[] employees = {
            "احسان قانعی زاده",
            "پگاه مصباح زاده", 
            "صدف لندرانی",
            "شادی نعمت زاده"
        };

        for (String employee : employees) {
            ContentValues values = new ContentValues();
            values.put("name", employee);
            db.insert(TABLE_EMPLOYEES, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEAVES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DELAYS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EARLY_EXITS);
        onCreate(db);
    }

    // روش‌های مدیریت پرسنل
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EMPLOYEES, null, "is_active=1", null, null, null, "name ASC");

        if (cursor.moveToFirst()) {
            do {
                Employee employee = new Employee();
                employee.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                employee.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                employees.add(employee);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return employees;
    }

    public long addEmployee(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        long id = db.insert(TABLE_EMPLOYEES, null, values);
        db.close();
        return id;
    }

    public boolean updateEmployee(int id, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", newName);
        int rowsAffected = db.update(TABLE_EMPLOYEES, values, "id=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean deleteEmployee(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_EMPLOYEES, "id=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    // روش‌های ثبت مرخصی
    public long addLeave(int employeeId, String startDate, String endDate, String shiftType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("employee_id", employeeId);
        values.put("start_date", startDate);
        values.put("end_date", endDate);
        values.put("shift_type", shiftType);
        long id = db.insert(TABLE_LEAVES, null, values);
        db.close();
        return id;
    }

    // روش‌های ثبت تاخیر
    public long addDelay(int employeeId, int delayMinutes, String actualTime, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("employee_id", employeeId);
        values.put("delay_minutes", delayMinutes);
        values.put("actual_entry_time", actualTime);
        values.put("date", date);
        long id = db.insert(TABLE_DELAYS, null, values);
        db.close();
        return id;
    }

    // روش‌های ثبت خروج زودهنگام
    public long addEarlyExit(int employeeId, int earlyMinutes, String actualTime, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("employee_id", employeeId);
        values.put("early_minutes", earlyMinutes);
        values.put("actual_exit_time", actualTime);
        values.put("date", date);
        long id = db.insert(TABLE_EARLY_EXITS, null, values);
        db.close();
        return id;
    }
                   }
