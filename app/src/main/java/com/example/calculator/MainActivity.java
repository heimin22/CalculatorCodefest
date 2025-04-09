package com.example.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import com.example.calculator.DBHelper;
import com.example.calculator.HistoryActivity;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.mariuszgromada.math.mxparser.*;

public class MainActivity extends AppCompatActivity {
    ScriptEngineManager mgr;
    ScriptEngine engine;
    DBHelper dbHelper;

    // special buttons
    ImageButton historyBtn;
    Button clearBtn, equalsBtn, delBtn;
    EditText currentView;
    TextView answerView;

    String currentValue;

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
        engine = mgr.getEngineByName("rhino");
        currentView = findViewById(R.id.expression);
        answerView = findViewById(R.id.result);

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
                R.id.multiplyButton, R.id.addButton, R.id.minusButton, R.id.divideButton, R.id.decimalButton, R.id.percentButton, R.id.parenthesisButton
        };

        View.OnClickListener operatorClickListener = view -> {
            Button _button = (Button) view;
            String currentValue = currentView.getText().toString();
            String buttonText = _button.getText().toString();

            if (currentValue.isEmpty()) return;

            char lastChar = currentValue.charAt(currentValue.length() - 1);

            if ("*+-/x.".indexOf(lastChar) != -1) {
                return;
            }

            if(lastChar == '%') return;

            if (buttonText.equals(".")) {
                String[] number = currentValue.split("[+\\-*()÷×]");
                String lastNumber = number[number.length - 1];

                if (lastNumber.contains(".")) {
                    return;
                }
            }

            String newValue = currentValue + buttonText;
            currentView.setText(newValue);
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
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        clearBtn = findViewById(R.id.clearButton);
        clearBtn.setOnClickListener(view -> {
            currentView.setText("");
            answerView.setText("");
        });

        equalsBtn = findViewById(R.id.equalsButton);
        equalsBtn.setOnClickListener(view -> {
            currentValue = currentView.getText().toString();
            if(currentValue.isEmpty()) return;

            char lastChar = currentValue.charAt(currentValue.length() - 1);

            if ("*+-÷()×.".indexOf(lastChar) != -1) {
                Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
                return;
            }

            Expression expression = new Expression(currentValue);
            double result = expression.calculate();
            String convertedResult = Double.toString(result).contains(".0") ? Integer.toString((int) result) : Double.toString(result);

            currentView.setText(convertedResult);
            answerView.setText(convertedResult);

            dbHelper.addToTable(currentValue, convertedResult);
        });

        delBtn = findViewById(R.id.deleteButton);
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
                R.id.zeroButton, R.id.oneButton, R.id.twoButton, R.id.threeButton, R.id.fourButton, R.id.fiveButton,
                R.id.sixButton, R.id.sevenButton, R.id.eightButton, R.id.nineButton
        };

        View.OnClickListener numberClickListener = view -> {
            Button _button = (Button) view;
            String currentValue = currentView.getText().toString();
            String newValue = currentValue + _button.getText().toString();
            char lastChar = currentValue.charAt(currentValue.length() - 1);
            if(lastChar == '%') return;

            currentView.setText(newValue);

        };

        for (int id : numberIds)
        {
            findViewById(id).setOnClickListener(numberClickListener);
        }
    }
}