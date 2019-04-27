package com.vijaya.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.vijaya.sqlite.databinding.ActivityEmployerBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EmployerActivity extends AppCompatActivity {

    private static final String TAG = "EmployerActivity";
    private ActivityEmployerBinding binding;
    private String currentID = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_employer);

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

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
        ContentValues values = new ContentValues();
        values.put(SampleDBContract.Employer.COLUMN_NAME, binding.nameEditText.getText().toString());
        values.put(SampleDBContract.Employer.COLUMN_DESCRIPTION, binding.descEditText.getText().toString());

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((new SimpleDateFormat("MM/dd/yyyy")).parse(
                    binding.foundedEditText.getText().toString()));
            long date = calendar.getTimeInMillis();
            values.put(SampleDBContract.Employer.COLUMN_FOUNDED_DATE, date);
        } catch (Exception e) {
            Log.e(TAG, "Error", e);
            Toast.makeText(this, "Date is in the wrong format", Toast.LENGTH_LONG).show();
            return;
        }
        long newRowId = database.insert(SampleDBContract.Employer.TABLE_NAME, null, values);

        Toast.makeText(this, "The new Row Id is " + newRowId, Toast.LENGTH_LONG).show();
    }

    private void readFromDB() {
        String name = binding.nameEditText.getText().toString();
        String desc = binding.descEditText.getText().toString();
        long date = 0;

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((new SimpleDateFormat("MM/dd/yyyy")).parse(
                    binding.foundedEditText.getText().toString()));
            date = calendar.getTimeInMillis();
        } catch (Exception e) {
        }

        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getReadableDatabase();

        String[] projection = {
                SampleDBContract.Employer._ID,
                SampleDBContract.Employer.COLUMN_NAME,
                SampleDBContract.Employer.COLUMN_DESCRIPTION,
                SampleDBContract.Employer.COLUMN_FOUNDED_DATE
        };

        String selection =
                SampleDBContract.Employer.COLUMN_NAME + " like ? AND " +
//                        SampleDBContract.Employer.COLUMN_FOUNDED_DATE + " > ? AND " +
                        SampleDBContract.Employer.COLUMN_DESCRIPTION + " like ?";

        String[] selectionArgs = {"%" + name, "%" + desc + "%"};

        Cursor cursor = database.query(
                SampleDBContract.Employer.TABLE_NAME,     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                             // don't group the rows
                null,                              // don't filter by row groups
                null                              // don't sort
        );
        //Check if returns anything
        if (cursor.getCount() >0){
            cursor.moveToPosition(0);
            Log.d("hello", cursor.getString(
                    cursor.getColumnIndexOrThrow(SampleDBContract.Employer._ID)
            ));
            currentID = cursor.getString(
                    cursor.getColumnIndexOrThrow(SampleDBContract.Employer._ID));
            Log.d("hello", cursor.getString(
                    cursor.getColumnIndexOrThrow(SampleDBContract.Employer.COLUMN_NAME)
            ));
        }
        else{
            currentID = null;
        }
        binding.recycleView.setAdapter(new SampleRecyclerViewCursorAdapter(this, cursor));
        //Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
    }
    private void deletefromDB() {
        if (currentID != null){
            SQLiteDatabase database = new SampleDBSQLiteHelper(this).getReadableDatabase();
            database.delete(SampleDBContract.Employer.TABLE_NAME , "_ID=?" ,new String[]{currentID});
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

                String[] projection = {
                        SampleDBContract.Employer._ID,
                        SampleDBContract.Employer.COLUMN_NAME,
                        SampleDBContract.Employer.COLUMN_DESCRIPTION,
                        SampleDBContract.Employer.COLUMN_FOUNDED_DATE
                };

                String selection =
                        SampleDBContract.Employer._ID + " = ? ";

                String[] selectionArgs = {currentID};

                Cursor cursor = database.query(
                        SampleDBContract.Employer.TABLE_NAME,     // The table to query
                        projection,                               // The columns to return
                        selection,                                // The columns for the WHERE clause
                        selectionArgs,                            // The values for the WHERE clause
                        null,                             // don't group the rows
                        null,                              // don't filter by row groups
                        null                              // don't sort
                );
                cursor.moveToPosition(0);
                binding.nameEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow(SampleDBContract.Employer.COLUMN_NAME)));
                binding.descEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow(SampleDBContract.Employer.COLUMN_DESCRIPTION)));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(cursor.getLong(
                        cursor.getColumnIndexOrThrow(SampleDBContract.Employer.COLUMN_FOUNDED_DATE)));
                binding.foundedEditText.setText(new SimpleDateFormat("MM/dd/yyyy").format(calendar.getTime()));
                binding.editButton.setText("Apply Changes");
            }
            else
            {
                String newName =  binding.nameEditText.getText().toString();
                String newDesc = binding.descEditText.getText().toString();
                String newFoundDate = binding.foundedEditText.getText().toString();
                SQLiteDatabase database = new SampleDBSQLiteHelper(this).getReadableDatabase();
                //Apply changes
                ContentValues values = new ContentValues();
                values.put(SampleDBContract.Employer.COLUMN_NAME, binding.nameEditText.getText().toString());
                values.put(SampleDBContract.Employer.COLUMN_DESCRIPTION, binding.descEditText.getText().toString());
                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime((new SimpleDateFormat("MM/dd/yyyy")).parse(
                            binding.foundedEditText.getText().toString()));
                    long date = calendar.getTimeInMillis();
                    values.put(SampleDBContract.Employer.COLUMN_FOUNDED_DATE, date);
                } catch (Exception e) {
                    Log.e(TAG, "Error", e);
                    Toast.makeText(this, "Date is in the wrong format", Toast.LENGTH_LONG).show();
                    return;
                }
                database.update(SampleDBContract.Employer.TABLE_NAME,  values ,"_id=?" , new String[]{currentID});
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
        database.execSQL("delete from "+ SampleDBContract.Employer.TABLE_NAME);
        Toast.makeText(this, "Deleted All Successfully", Toast.LENGTH_LONG).show();
    }
}