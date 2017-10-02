package com.example.siddhartha.soding;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DATABASE_NAME = "userdatabase";

    private TextView textViewViewUser;
    private EditText editTextName, editTextDescription;
    private SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewViewUser = (TextView) findViewById(R.id.textViewViewUsers);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);

        findViewById(R.id.buttonAddUser).setOnClickListener(this);
        textViewViewUser.setOnClickListener(this);

        //creating a database
        mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        createEmployeeTable();
    }

    //this method will create the table
    //as we are going to call this method everytime we will launch the application
    //I have added IF NOT EXISTS to the SQL
    //so it will only create the table when the table is not already created
    private void createEmployeeTable() {
        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS Users (\n" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "    name varchar(200) NOT NULL,\n" +
                        "    description varchar(200) NOT NULL,\n" +
                        "    created_at datetime NOT NULL,\n" +
                        "    updated_at datetime NOT NULL\n" +
                        ");"
        );
    }

    //this method will validate the name and description
    //dept does not need validation as it is a spinner and it cannot be empty
    private boolean inputsAreCorrect(String name, String description) {
        if (name.isEmpty()) {
            editTextName.setError("Please enter a name");
            editTextName.requestFocus();
            return false;
        }

        if (description.isEmpty()) {
            editTextDescription.setError("Please enter description");
            editTextDescription.requestFocus();
            return false;
        }
        return true;
    }

    //In this method we will do the create operation
    private void addUser() {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        //getting the current time for joining date
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String insertingDate = sdf.format(cal.getTime());

        //validating the inptus
        if (inputsAreCorrect(name, description)) {
            String insertSQL = "INSERT INTO Users \n" +
                    "(name, description, created_at, updated_at)\n" +
                    "VALUES \n" +
                    "(?, ?, ?, ?);";

            //using the same method execsql for inserting values
            //this time it has two parameters
            //first is the sql string and second is the parameters that is to be binded with the query
            mDatabase.execSQL(insertSQL, new String[]{name, description, insertingDate, insertingDate});

            Toast.makeText(this, "User Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddUser:
                addUser();
                break;
            case R.id.textViewViewUsers:
                startActivity(new Intent(this, UserListActivity.class));
                break;
        }
    }
}
