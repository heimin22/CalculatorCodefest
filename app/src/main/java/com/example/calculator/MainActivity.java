package com.example.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button historyBtn, clearBtn, equalsBtn;
    TextView displayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        InitializeComponents();
    }

    /**
     * Initializes components
     * */
    private void InitializeComponents()
    {
        InitializeNumberButtons();
        InitializeSpecialButtons();
    }

    /**
     * set each on click listener for special buttons
     * @historyBtn = go to history activity
     * @clearBtn = clear display
     * @equalsBtn = eval the current text on display
     * */
    private void InitializeSpecialButtons()
    {
        historyBtn = findViewById(R.id.historyButton);
        historyBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        clearBtn = findViewById(R.id.clearButton);
        clearBtn.setOnClickListener(view -> {
            // TODO: clear display view
        });

        equalsBtn = findViewById(R.id.equalsButton);
        equalsBtn.setOnClickListener(view -> {
            // TODO: evaluate
        });
    }

    /**
     * set each on click listener for number buttons (1-9)
     * */
    private void InitializeNumberButtons()
    {
        int[] numberIds = {
                R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5,
                R.id.button6, R.id.button7, R.id.button8, R.id.button9
        };

        View.OnClickListener numberClickListener = view -> {
            Button _button = (Button) view;
            String currentValue = displayView.getText().toString();
            displayView.setText(currentValue + _button.getText());
        };

        for (int id : numberIds)
        {
            findViewById(id).setOnClickListener(numberClickListener);
        }
    }
}