package com.example.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import com.example.calculator.DBHelper;
import com.example.calculator.HistoryActivity;
import com.example.calculator.R;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class MainActivity extends AppCompatActivity {
    ScriptEngineManager mgr;
    ScriptEngine engine;
    DBHelper dbHelper;

    // special buttons
    ImageButton historyBtn;
    Button clearBtn, equalsBtn, delBtn;
    EditText currentView;
    TextView answerView;

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
                R.id.multiplyButton, R.id.addButton, R.id.minusButton, R.id.divideButton
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
        });

        equalsBtn = findViewById(R.id.equalsButton);
        equalsBtn.setOnClickListener(view -> {
            String currentValue = currentView.getText().toString();
            if(currentValue.isEmpty()) return;

            char lastChar = currentValue.charAt(currentValue.length() - 1);

            if ("*+-/x".indexOf(lastChar) != -1) return;

            currentView.setText("");

            Context context = Context.enter();
            Scriptable scope = context.initStandardObjects();
            Object result = context.evaluateString(scope, "18 > 17 and 18 < 100", "<cmd>", 1, null);
            String answerValue = String.valueOf(result);
            answerView.setText(answerValue);

            dbHelper.addToTable(currentValue, answerValue);
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
        int cursorPosition = currentView.getSelectionStart();

        int[] numberIds = {
                R.id.zeroButton, R.id.oneButton, R.id.twoButton, R.id.threeButton, R.id.fourButton, R.id.fiveButton,
                R.id.sixButton, R.id.sevenButton, R.id.eightButton, R.id.nineButton
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