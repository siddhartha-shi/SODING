package com.example.siddhartha.soding;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private List<User> userList;
    private SQLiteDatabase mDatabase;
    private ListView listViewUsers;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        listViewUsers = (ListView) findViewById(R.id.listViewUsers);
        userList = new ArrayList<>();

        //opening the database
        mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);

        //this method will display the Users in the list
        showUsersFromDatabase();
    }

    private void showUsersFromDatabase() {

        //we used rawQuery(sql, selectionargs) for fetching all the Users
        Cursor cursorUsers = mDatabase.rawQuery("SELECT * FROM Users", null);

        //if the cursor has some data
        if (cursorUsers.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the User list
                userList.add(new User(
                        cursorUsers.getInt(0),
                        cursorUsers.getString(1),
                        cursorUsers.getString(2),
                        cursorUsers.getString(3),
                        cursorUsers.getString(4)
                ));
            } while (cursorUsers.moveToNext());
        }
        //closing the cursor
        cursorUsers.close();

        //creating the adapter object
        adapter = new UserAdapter(this, R.layout.list_layout_user, userList, mDatabase);

        //adding the adapter to listview
        listViewUsers.setAdapter(adapter);
    }
}