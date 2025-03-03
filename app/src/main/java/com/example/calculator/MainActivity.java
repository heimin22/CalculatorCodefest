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
    ScriptEngineManager mgr;
    ScriptEngine engine;
    DBHelper dbHelper;

    // special buttons
    Button historyBtn, clearBtn, equalsBtn, delBtn;
    TextView currentView, prevView, answerView;

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
        mgr = new ScriptEngineManager();
        engine = mgr.getEngineByName("JavaScript");
        InitializeComponents();
    }

    /**
     * Initializes components
     * */
    private void InitializeComponents()
    {
        InitializeNumberButtons();
        InitializeSpecialButtons();
        InitializeOperatorButtons();
        dbHelper = new DBHelper(this);
    }

    /**
     * set each on click listener for operators
     * */
    private void InitializeOperatorButtons() {
        int[] operatorIds = {
                R.id.multiplyBtn, R.id.addBtn, R.id.subtractBtn, R.id.divideBtn
        };

        View.OnClickListener operatorClickListener = view -> {
            Button _button = (Button) view;
            String currentValue = currentView.getText().toString();

            if (currentValue.isEmpty()) return;

            char lastChar = currentValue.charAt(currentValue.length() - 1);

            if ("*+-/x".indexOf(lastChar) != -1) {
                return;
            }

            currentView.setText(_button.getText().toString());
        };

        for (int id : operatorIds) {
            findViewById(id).setOnClickListener(operatorClickListener);
        }
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
            currentView.setText("");
            prevView.setText("");
        });

        equalsBtn = findViewById(R.id.equalsButton);
        equalsBtn.setOnClickListener(view -> {
            String currentValue = currentView.getText().toString();
            if(currentValue.isEmpty()) return;

            char lastChar = currentValue.charAt(currentValue.length() - 1);

            if ("*+-/x".indexOf(lastChar) != -1) return;

            prevView.setText(currentValue);
            currentView.setText("");

            String answerValue = engine.eval(currentValue);
            answerView.setText(answerValue);

            dbHelper.addToTable(currentValue, answerValue);
        });

        delBtn = findViewById(R.id.delBtn);
        delBtn.setOnClickListener(view -> {
            String currentValue = currentView.getText().toString();
            if(currentValue.isEmpty()) return;

            String newValue = currentValue.substring(0, currentValue.length() - 1);
            currentView.setText(newValue);
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
            String currentValue = currentView.getText().toString();
            String newValue = currentValue + _button.getText().toString();
            currentView.setText(newValue);
        };

        for (int id : numberIds)
        {
            findViewById(id).setOnClickListener(numberClickListener);
        }
    }
}