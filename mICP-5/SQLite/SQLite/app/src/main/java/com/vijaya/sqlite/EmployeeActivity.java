package com.vijaya.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.vijaya.sqlite.databinding.ActivityEmployeeBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EmployeeActivity extends AppCompatActivity {

    private ActivityEmployeeBinding binding;
    private static final String TAG = "EmployeeActivity";
    private String currentID = null;
    SimpleCursorAdapter cursorAdapter;
    Cursor employerCursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee);

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        String[] queryCols = new String[]{"_id", SampleDBContract.Employer.COLUMN_NAME};
        String[] adapterCols = new String[]{SampleDBContract.Employer.COLUMN_NAME};
        int[] adapterRowViews = new int[]{android.R.id.text1};

        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getReadableDatabase();
        employerCursor = database.query(
                SampleDBContract.Employer.TABLE_NAME,     // The table to query
                queryCols,                                // The columns to return
                null,                             // The columns for the WHERE clause
                null,                          // The values for the WHERE clause
                null,                             // don't group the rows
                null,                              // don't filter by row groups
                null                              // don't sort
        );

        cursorAdapter = new SimpleCursorAdapter(
                this, android.R.layout.simple_spinner_item, employerCursor, adapterCols, adapterRowViews, 0);
        cursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.employerSpinner.setAdapter(cursorAdapter);

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDB();
            }
        });

        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readFromDB();
            }
        });
        binding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDB();
            }
        });
        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletefromDB();
            }
        });
        binding.deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAllfromDB();
            }
        });
    }

    private void saveToDB() {
        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getWritableDatabase();
        //database.execSQL(SampleDBContract.Employee.CREATE_TABLE);
        ContentValues values = new ContentValues();
        values.put(SampleDBContract.Employee.COLUMN_FIRSTNAME, binding.firstnameEditText.getText().toString());
        values.put(SampleDBContract.Employee.COLUMN_LASTNAME, binding.lastnameEditText.getText().toString());
        values.put(SampleDBContract.Employee.COLUMN_JOB_DESCRIPTION, binding.jobDescEditText.getText().toString());
        values.put(SampleDBContract.Employee.COLUMN_EMPLOYER_ID,
                ((Cursor) binding.employerSpinner.getSelectedItem()).getInt(0));

        Log.d("getINT", ((Cursor) binding.employerSpinner.getSelectedItem()).getInt(0) + "");
        Log.d("getColumnName", ((Cursor) binding.employerSpinner.getSelectedItem()).getColumnName(0));
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((new SimpleDateFormat("MM/dd/yyyy")).parse(
                    binding.dobEditText.getText().toString()));
            long date = calendar.getTimeInMillis();
            values.put(SampleDBContract.Employee.COLUMN_DATE_OF_BIRTH, date);

            calendar.setTime((new SimpleDateFormat("MM/dd/yyyy")).parse(
                    binding.employedEditText.getText().toString()));
            date = calendar.getTimeInMillis();
            values.put(SampleDBContract.Employee.COLUMN_EMPLOYED_DATE, date);
        } catch (Exception e) {
            Log.e(TAG, "Error", e);
            Toast.makeText(this, "Date is in the wrong format", Toast.LENGTH_LONG).show();
            return;
        }
        long newRowId = database.insert(SampleDBContract.Employee.TABLE_NAME, null, values);

        Toast.makeText(this, "The new Row Id is " + newRowId, Toast.LENGTH_LONG).show();
    }

    private void readFromDB() {
        String firstname = binding.firstnameEditText.getText().toString();
        String lastname = binding.lastnameEditText.getText().toString();

        SQLiteDatabase database  = new SampleDBSQLiteHelper(this).getReadableDatabase();
        //database.execSQL("DROP TABLE IF EXISTS " + SampleDBContract.Employee.TABLE_NAME);
        String[] selectionArgs = {"%" + firstname + "%", "%" + lastname + "%"};

        Cursor cursor = database.rawQuery(SampleDBContract.SELECT_EMPLOYEE_WITH_EMPLOYER, selectionArgs);
//        Cursor cursor = database.rawQuery(SampleDBContract.SELECT_EMPLOYEE, null);
        binding.recycleView.setAdapter(new SampleJoinRecyclerViewCursorAdapter(this, cursor));
        if (cursor.getCount() >0){
            cursor.moveToPosition(0);
            Log.d("hello", cursor.getString(
                    cursor.getColumnIndexOrThrow(SampleDBContract.Employee.ID)
            ));
            currentID = cursor.getString(
                    cursor.getColumnIndexOrThrow(SampleDBContract.Employee.ID));
            Log.d("hello", cursor.getString(
                    cursor.getColumnIndexOrThrow(SampleDBContract.Employee.COLUMN_FIRSTNAME)
            ));
            Log.d("hello", cursor.getString(
                    cursor.getColumnIndexOrThrow(SampleDBContract.Employee.COLUMN_LASTNAME)
            ));
        }
        else{
            currentID = null;
        }
    }
    private void deletefromDB() {
        if (currentID != null){
            SQLiteDatabase database = new SampleDBSQLiteHelper(this).getReadableDatabase();
            database.delete(SampleDBContract.Employee.TABLE_NAME , SampleDBContract.Employee.ID + "=?" ,new String[]{currentID});
            Toast.makeText(this, "Delete Successfully", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Delete Failed", Toast.LENGTH_LONG).show();
        }
    }
    private void editDB() {
        if (currentID != null) {
            if (binding.editButton.getText().toString().equals("Edit")){

//            SQLiteDatabase database = new SampleDBSQLiteHelper(this).getReadableDatabase();
//            database.delete(SampleDBContract.Employer.TABLE_NAME , "_ID=?" ,new String[]{currentID});
//            Toast.makeText(this, "Delete Successfully", Toast.LENGTH_LONG).show();
                SQLiteDatabase database = new SampleDBSQLiteHelper(this).getReadableDatabase();

                String[] selectionArgs = {currentID};
                Log.d("currentID", currentID);
                Cursor cursor = database.rawQuery(SampleDBContract.SELECT_EMPLOYEE_WITH_ID, selectionArgs);
                if (cursor.getCount() != 0){
                    cursor.moveToPosition(0);
                    binding.firstnameEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow(SampleDBContract.Employee.COLUMN_FIRSTNAME)));
                    binding.lastnameEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow(SampleDBContract.Employee.COLUMN_LASTNAME)));
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(cursor.getLong(
                            cursor.getColumnIndexOrThrow(SampleDBContract.Employee.COLUMN_DATE_OF_BIRTH)));
                    binding.dobEditText.setText(new SimpleDateFormat("MM/dd/yyyy").format(calendar.getTime()));
                    calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(cursor.getLong(
                            cursor.getColumnIndexOrThrow(SampleDBContract.Employee.COLUMN_EMPLOYED_DATE)));
                    binding.employedEditText.setText(new SimpleDateFormat("MM/dd/yyyy").format(calendar.getTime()));
                    binding.jobDescEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow(SampleDBContract.Employee.COLUMN_JOB_DESCRIPTION)));
                    //binding.employerSpinner.setSelection(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SampleDBContract.Employee.COLUMN_EMPLOYER_ID))));



                    int cpos = 0;
                    String retrieved_employer_id = cursor.getString(cursor.getColumnIndexOrThrow(SampleDBContract.Employee.COLUMN_EMPLOYER_ID));


                    selectionArgs = new String[]{retrieved_employer_id};
                    Log.d("retrieved_employer_id", retrieved_employer_id);
                    Cursor cursor2 = database.rawQuery(SampleDBContract.SELECT_EMPLOYER_WITH_ID, selectionArgs);
                    cursor2.moveToPosition(0);
                    String retrieved_employer_name = cursor2.getString(cursor2.getColumnIndexOrThrow(SampleDBContract.Employer.COLUMN_NAME));
                    Log.d("Employer Name", retrieved_employer_name);
                    for(int i = 0; i < employerCursor.getCount(); i++){
                        employerCursor.moveToPosition(i);
                        String temp = employerCursor.getString(1);
                        Log.d("temp", temp);
                        if (temp.contentEquals(retrieved_employer_name)){
                            Log.d("TAG", "Found match");
                            cpos = i;
                            break;
                        }
                    }
                    binding.employerSpinner.setSelection(cpos);
                    binding.editButton.setText("Apply Changes");
                }
            }
            else
            {
                SQLiteDatabase database = new SampleDBSQLiteHelper(this).getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(SampleDBContract.Employee.COLUMN_FIRSTNAME, binding.firstnameEditText.getText().toString());
                values.put(SampleDBContract.Employee.COLUMN_LASTNAME, binding.lastnameEditText.getText().toString());
                values.put(SampleDBContract.Employee.COLUMN_JOB_DESCRIPTION, binding.jobDescEditText.getText().toString());
                values.put(SampleDBContract.Employee.COLUMN_EMPLOYER_ID,
                        ((Cursor) binding.employerSpinner.getSelectedItem()).getInt(0));

                Log.d("getINT", ((Cursor) binding.employerSpinner.getSelectedItem()).getInt(0) + "");
                Log.d("getColumnName", ((Cursor) binding.employerSpinner.getSelectedItem()).getColumnName(0));

                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime((new SimpleDateFormat("MM/dd/yyyy")).parse(
                            binding.dobEditText.getText().toString()));
                    long date = calendar.getTimeInMillis();
                    values.put(SampleDBContract.Employee.COLUMN_DATE_OF_BIRTH, date);

                    calendar.setTime((new SimpleDateFormat("MM/dd/yyyy")).parse(
                            binding.employedEditText.getText().toString()));
                    date = calendar.getTimeInMillis();
                    values.put(SampleDBContract.Employee.COLUMN_EMPLOYED_DATE, date);
                } catch (Exception e) {
                    Log.e(TAG, "Error", e);
                    Toast.makeText(this, "Date is in the wrong format", Toast.LENGTH_LONG).show();
                    return;
                }
                database.update(SampleDBContract.Employee.TABLE_NAME,  values ,SampleDBContract.Employee.ID + "=?" , new String[]{currentID});
                binding.editButton.setText("Edit");
                Toast.makeText(this, "Edit Successfully", Toast.LENGTH_LONG).show();

            }
        }
        else {
            Toast.makeText(this, "Edit Failed", Toast.LENGTH_LONG).show();
        }

    }
    private void deleteAllfromDB(){
        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getReadableDatabase();
        database.execSQL("delete from "+ SampleDBContract.Employee.TABLE_NAME);
        Toast.makeText(this, "Deleted All Successfully", Toast.LENGTH_LONG).show();
    }
}