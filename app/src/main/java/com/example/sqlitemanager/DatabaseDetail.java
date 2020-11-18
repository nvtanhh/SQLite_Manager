package com.example.sqlitemanager;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;

import com.example.sqlitemanager.listViewAdapter.TableListViewAdapter;
import com.example.sqlitemanager.listViewAdapter.TableRecyclerAdapter;
import com.example.sqlitemanager.model.DBTableEntry;
import com.example.sqlitemanager.model.SqliteDAO;
import com.example.sqlitemanager.model.SqliteResponse;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.example.sqlitemanager.model.DatabaseEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class DatabaseDetail extends AppCompatActivity implements TableRecyclerAdapter.Listener {
    DatabaseEntry databaseEntry;
    SqliteDAO dao;
    private List<DBTableEntry> tables;
    private TableListViewAdapter adapter;
    public static int currentTableIndex = 0;
    private RecyclerView recyclerView;
    private RecyclerView mTableLayoutRecyclerView;
    private TableRecyclerAdapter mTableRecyclerAdapter;
    private DBTableEntry curentTable;
    private DBTableEntry queryTables;
    private boolean queryMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_detail);

        getDatabase();

        setupToolbar();
        setupFloatingButton();
        setupListTableScrollView();

        setupTableStageArea();

        setupAddTableButton();
        setupAddColumnButton();
        setupQueryButton();
    }


    private void setupTableStageArea() {
        DBTableEntry item;
        if (queryMode) {
            item = queryTables;
        } else {
            curentTable = tables.get(currentTableIndex);
            curentTable.fetchAllData(dao);
            item = curentTable;
        }


        ((TextView) findViewById(R.id.current_table_name)).setText("Table: " + item.name);


//        column names
        ColumnNameView columnView = findViewById(R.id.sqlite_manager_table_layout_header);
        columnView.setData(item.columnNames);

//      content rows
        mTableLayoutRecyclerView = findViewById(R.id.sqlite_manager_table_layout_recycler_view); // table showing area
        mTableLayoutRecyclerView.setLayoutManager(new LinearLayoutManager(this));  // set layout

        mTableRecyclerAdapter = new TableRecyclerAdapter(item.rows);
        mTableRecyclerAdapter.setListener(this);
        mTableLayoutRecyclerView.setAdapter(mTableRecyclerAdapter);

    }

    private void setupListTableScrollView() {
        // Set up the RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
//        recyclerView.setHasFixedSize(true);

//        set up layout
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);

//      set up adapter
        tables = DBTableEntry.initTableEntryList(dao); // load all current database
        if (curentTable == null) {
            curentTable = tables.get(currentTableIndex);
        }

        adapter = new TableListViewAdapter(tables, new TableListViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DBTableEntry item) {
                selectTable(item);
            }

            @Override
            public void onLongClick(DBTableEntry item) {
                showDeleteTableDialog(item);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void showDeleteTableDialog(final DBTableEntry table) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure delete table " + table.name);
        builder.setPositiveButton("Query", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dao.delete(table.name);
                tables.remove(table);
                currentTableIndex = 0;
                adapter.notifyDataSetChanged();
                setupTableStageArea();
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

    private void setupAddTableButton() {
        View view = findViewById(R.id.add_table);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });
    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter table name");
// Set up the input
        final EditText input = new EditText(this);
        input.setHint("Table name");
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
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createNewTable(input.getText().toString());
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

    private void createNewTable(String tableName) {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (id INTEGER PRIMARY KEY AUTOINCREMENT)";
        dao.execQuery(sql);
        DBTableEntry newTable = new DBTableEntry(tableName);
        if (!dao.checkForTableExists(tableName)) {
            showToast("Create table ERROR!!!");
        } else if (tables.contains(newTable)) {
            showToast("Duplicate table!!!");
        } else {
            tables.add(0, newTable);
            selectTable(newTable);
        }
    }

    private void showToast(String mess) {
        Toast.makeText(getApplicationContext(), mess, Toast.LENGTH_SHORT).show();
    }

    private void getDatabase() {
        databaseEntry = (DatabaseEntry) getIntent().getSerializableExtra("db");
        dao = new SqliteDAO(getApplicationContext());
        dao.open(databaseEntry.name + ".db");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        currentTableIndex = 0;
        dao.close();
    }


    private void selectTable(DBTableEntry item) {
        hideClearQueryButton();
        currentTableIndex = tables.indexOf(item);
        curentTable = item;
        adapter.notifyDataSetChanged();
        setupTableStageArea();
    }

//    private void refreshState() {
//        setupTableStageArea();
//        adapter.notifyDataSetChanged();
//    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(databaseEntry.name);
        setSupportActionBar(toolbar);


        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }


    }

    private void setupFloatingButton() {
        ExtendedFloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddNewRecordDialog(null);
            }
        });

//        FloatingActionButton fab_search = findViewById(R.id.fab_search);
    }


    private void setupAddColumnButton() {
        ExtendedFloatingActionButton fab = findViewById(R.id.fab_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddNewColumnDialog();
            }
        });
    }


    private void setupQueryButton() {
        FloatingActionButton fab = findViewById(R.id.fab_query);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showQueryDialog("");
            }
        });
    }

    private void showQueryDialog(String old) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Query sql");
        final EditText input = new EditText(this);
        input.setText(old);
        builder.setView(input);
        builder.setPositiveButton("Query", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doQuery(input.getText().toString());
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

    private void doQuery(String sql) {
        queryTables = new DBTableEntry(sql);
        SqliteResponse response = queryTables.runQuery(dao, sql);
        if (response.isQuerySuccess()) {
            queryMode = true;
            showClearQueryButton();
            setupTableStageArea();
        } else {
            showToast(response.getThrowable().toString());
        }


    }

    private void showClearQueryButton() {
        ((TextView) findViewById(R.id.current_table_name)).setVisibility(View.GONE);

        View container = findViewById(R.id.query_container);
        View button = findViewById(R.id.ic_query_clear);
        TextView text = findViewById(R.id.query_text);

        container.setVisibility(View.VISIBLE);

        text.setText(queryTables.name);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideClearQueryButton();
                queryMode = false;
                queryTables = null;
                setupTableStageArea();
            }
        });
    }

    private void hideClearQueryButton() {
        (findViewById(R.id.current_table_name)).setVisibility(View.VISIBLE);
        findViewById(R.id.query_container).setVisibility(View.GONE);
    }


    private void showAddNewColumnDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogView = getLayoutInflater().inflate(R.layout.add_column_form, null);
        builder.setView(dialogView);
//        final AlertDialog alert = builder.create();

        final RadioGroup group = dialogView.findViewById(R.id.radioGroup);
        final EditText colName = dialogView.findViewById(R.id.col_name);
        TextView tile = dialogView.findViewById(R.id.dialog_title);
        tile.setText("Add new column for table: " + curentTable.name);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int Selectedid = group.getCheckedRadioButtonId();
                RadioButton radioButton = dialogView.findViewById(Selectedid);
                createNewCol(colName.getText().toString(), radioButton.getText().toString());
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

    private void createNewCol(String colName, String type) {
        String sql = String.format("ALTER TABLE %s ADD COLUMN %s %s", curentTable.name, colName, type);
        System.out.println("sql: " + sql);
        dao.execQuery(sql);
        setupTableStageArea();
        setupTableStageArea();
    }


    private void showAddNewRecordDialog(final String[] row) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add record for table " + curentTable.name);
// Set up the input
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText[] inputs = new EditText[curentTable.columnNames.length];
        for (int i = 0; i < curentTable.columnNames.length; i++) {
            final EditText input = new EditText(this);
            input.setTypeface(input.getTypeface(), Typeface.NORMAL);
            input.setHint(curentTable.columnNames[i]);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            inputs[i] = input;
            layout.addView(input);
        }
        if (row != null) {
            for (int i = 0; i < row.length; i++) {
                inputs[i].setText(row[i]);
            }
        }
        builder.setView(layout);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                insertNewRecord(inputs);
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

    private void updateRows(EditText[] inputs) {
    }

    private void insertNewRecord(EditText[] inputs) {
        ContentValues insertValues = new ContentValues();
        for (int i = 0; i < inputs.length; i++) {
            insertValues.put(curentTable.columnNames[i], inputs[i].getText().toString());
        }
        try {
            dao.insert(curentTable.name, insertValues);
        } catch (Exception e) {
            showToast("Insert error!");
        }
        setupTableStageArea();
    }


    @Override
    public void onColumnValueClicked(String[] row) {
        showAddNewRecordDialog(row);
    }
}