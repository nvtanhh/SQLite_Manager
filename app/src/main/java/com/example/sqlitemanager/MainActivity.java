package com.example.sqlitemanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.sqlitemanager.listViewAdapter.DatabaseCardViewAdapter;
import com.example.sqlitemanager.model.DatabaseEntry;
import com.example.sqlitemanager.model.SqliteDAO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.View;

import android.view.Menu;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<DatabaseEntry> databases;
    RecyclerView recyclerView;
    DatabaseCardViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        set up tool bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupFloatingButton();

        setupListDatabaseView();

    }

    private void setupListDatabaseView() {
        // Set up the RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

//        set up layout
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

//      set up adapter

        databases = DatabaseEntry.initProductEntryList(getApplicationContext()); // load all current database
        adapter = new DatabaseCardViewAdapter(databases, onClickItem());

        recyclerView.setAdapter(adapter);
    }

    private DatabaseCardViewAdapter.OnItemClickListener onClickItem() {
        return new DatabaseCardViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DatabaseEntry item) { // on click start new activity
//                Toast.makeText(getApplicationContext(), "Item Clicked", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, DatabaseDetail.class);
                intent.putExtra("db", item);
                startActivity(intent);
            }

            @Override
            public void onClickDelete(DatabaseEntry item) {
                System.out.println("delete " + item);
                showDeleteDialog(item);

            }
        };
    }

    private void showDeleteDialog(final DatabaseEntry item) {
        new AlertDialog.Builder(this).setTitle("Are you sure you want to delete?").setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteDB(item);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
    }

    private void deleteDB(DatabaseEntry item) {
        if (getApplicationContext().deleteDatabase(item.name + ".db")) {
            System.out.println("delete");
            databases.remove(item);
            adapter.notifyDataSetChanged();
        } else { // if error
            Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT);
        }

    }


    private void setupFloatingButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });
    }

    public void showInputDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter database name");
// Set up the input
        final EditText input = new EditText(this);
        input.setHint("Database name");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
//        final EditText input2 = new EditText(this);
//        input2.setHint("Description");
//        input.setInputType(InputType.TYPE_CLASS_TEXT);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(input);
//        layout.addView(input2);
        builder.setView(layout);
// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createNewDB(input.getText().toString());
//                desciptionInput = input2.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void createNewDB(String dbName) {
        if (!dbName.equals("")) {
            SqliteDAO dao = new SqliteDAO(getApplicationContext()).open(dbName + ".db");
            databases.add(new DatabaseEntry(dao.dbName, ""));
            dao.close();
            System.out.println("add new DB: ");
            adapter.notifyDataSetChanged();
        }
    }



}