package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.jetbrains.annotations.NonNls;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    ListView historyListView;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        InitializeComponents();
        DisplayHistory();
    }

    private void DisplayHistory()
    {
        List<String> history = dbHelper.readHistoryTbl();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, history) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (view instanceof TextView) {
                    ((TextView) view).setTextColor(android.graphics.Color.parseColor("#121212"));
                }
                return view;
            }
        };

        historyListView.setAdapter(adapter);
    }

    private void InitializeComponents()
    {
        historyListView = findViewById(R.id.historyList);
        dbHelper = new DBHelper(this);
    }
}