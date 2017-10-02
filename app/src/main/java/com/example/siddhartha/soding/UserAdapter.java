package com.example.siddhartha.soding;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Siddhartha on 03-Oct-17.
 * sid
 */

public class UserAdapter extends ArrayAdapter<User> {
    private Context mCtx;
    private int listLayoutRes;
    private List<User> userList;
    private SQLiteDatabase mDatabase;

    public UserAdapter(Context mCtx, int listLayoutRes, List<User> userList, SQLiteDatabase mDatabase) {
        super(mCtx, listLayoutRes, userList);

        this.mCtx = mCtx;
        this.listLayoutRes = listLayoutRes;
        this.userList = userList;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_layout_user, null);

        final User user = userList.get(position);


        TextView textViewName = view.findViewById(R.id.textViewName);
        TextView textViewDescription = view.findViewById(R.id.textViewDescription);
        TextView textViewCreatedAt = view.findViewById(R.id.textViewCreatedAt);
        TextView textViewUpdatedAt = view.findViewById(R.id.textViewUpdatedAt);


        textViewName.setText(user.getName());
        textViewDescription.setText(user.getDescription());
        textViewCreatedAt.setText(String.valueOf(user.getCreatedAt()));
        textViewUpdatedAt.setText(user.getUpdatedAt());


        Button buttonDelete = view.findViewById(R.id.buttonDeleteUser);
        Button buttonEdit = view.findViewById(R.id.buttonEditUser);

        //adding a clicklistener to button
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser(user);
            }
        });

        //the delete operation
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sql = "DELETE FROM Users WHERE id = ?";
                        mDatabase.execSQL(sql, new Integer[]{user.getId()});
                        reloadUsersFromDatabase();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }

    private void updateUser(final User user) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.dialog_update_user, null);
        builder.setView(view);

        final EditText editTextName = view.findViewById(R.id.editTextName);
        final EditText editTextDescription = view.findViewById(R.id.editTextDescription);

        editTextName.setText(user.getName());
        editTextDescription.setText(String.valueOf(user.getDescription()));

        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.buttonEditUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();

                //getting the current time for updated date
                Calendar cal = Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                String updatedAt = sdf.format(cal.getTime());

                if (name.isEmpty()) {
                    editTextName.setError("Name can't be blank");
                    editTextName.requestFocus();
                    return;
                }

                if (description.isEmpty()) {
                    editTextDescription.setError("Description can't be blank");
                    editTextDescription.requestFocus();
                    return;
                }

                String sql = "UPDATE Users \n" +
                        "SET name = ?, \n" +
                        "description = ?, \n" +
                        "updated_at = ? \n" +
                        "WHERE id = ?;\n";

                mDatabase.execSQL(sql, new String[]{name, description, updatedAt, String.valueOf(user.getId())});
                Toast.makeText(mCtx, "user Updated", Toast.LENGTH_SHORT).show();
                reloadUsersFromDatabase();

                dialog.dismiss();
            }
        });
    }

    private void reloadUsersFromDatabase() {
        Cursor cursorUsers = mDatabase.rawQuery("SELECT * FROM Users", null);
        if (cursorUsers.moveToFirst()) {
            userList.clear();
            do {
                userList.add(new User(
                        cursorUsers.getInt(0),
                        cursorUsers.getString(1),
                        cursorUsers.getString(2),
                        cursorUsers.getString(3),
                        cursorUsers.getString(4)
                ));
            } while (cursorUsers.moveToNext());
        }
        cursorUsers.close();
        this.notifyDataSetChanged();
    }
}