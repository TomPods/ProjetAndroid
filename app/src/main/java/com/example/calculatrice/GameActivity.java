package com.example.calculatrice;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private TextView questionText;
    private EditText answerEdit;
    private TextView languageButton;
    private Button button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    private Button buttonDelete;
    private Button buttonSubmitKeyboard;
    private TextView scoreValue;
    private TextView livesValue;

    private int score = 0;
    private int lives = 3;
    private int currentAnswer;
    private Random random = new Random();
    private ScoreDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new ScoreDatabaseHelper(this);

        questionText = findViewById(R.id.question_text);
        answerEdit = findViewById(R.id.answer_edit);
        scoreValue = findViewById(R.id.score_value);
        livesValue = findViewById(R.id.lives_value);

        languageButton = findViewById(R.id.language_button);
        languageButton.setOnClickListener(v -> toggleLanguage());

        findViewById(R.id.bouton_retour).setOnClickListener(v -> finish());

        button0 = findViewById(R.id.button_0);
        button1 = findViewById(R.id.button_1);
        button2 = findViewById(R.id.button_2);
        button3 = findViewById(R.id.button_3);
        button4 = findViewById(R.id.button_4);
        button5 = findViewById(R.id.button_5);
        button6 = findViewById(R.id.button_6);
        button7 = findViewById(R.id.button_7);
        button8 = findViewById(R.id.button_8);
        button9 = findViewById(R.id.button_9);
        buttonDelete = findViewById(R.id.button_delete);
        buttonSubmitKeyboard = findViewById(R.id.button_submit_keyboard);

        generateQuestion();

        buttonSubmitKeyboard.setOnClickListener(v -> checkAnswer());

        button0.setOnClickListener(v -> appendToAnswer("0"));
        button1.setOnClickListener(v -> appendToAnswer("1"));
        button2.setOnClickListener(v -> appendToAnswer("2"));
        button3.setOnClickListener(v -> appendToAnswer("3"));
        button4.setOnClickListener(v -> appendToAnswer("4"));
        button5.setOnClickListener(v -> appendToAnswer("5"));
        button6.setOnClickListener(v -> appendToAnswer("6"));
        button7.setOnClickListener(v -> appendToAnswer("7"));
        button8.setOnClickListener(v -> appendToAnswer("8"));
        button9.setOnClickListener(v -> appendToAnswer("9"));
        buttonDelete.setOnClickListener(v -> deleteFromAnswer());
    }

    private void appendToAnswer(String digit) {
        String current = answerEdit.getText().toString();
        answerEdit.setText(current + digit);
        answerEdit.setSelection(answerEdit.getText().length());
    }

    private void deleteFromAnswer() {
        String current = answerEdit.getText().toString();
        if (current.length() > 0) {
            answerEdit.setText(current.substring(0, current.length() - 1));
            answerEdit.setSelection(answerEdit.getText().length());
        }
    }

    private void generateQuestion() {
        int type = random.nextInt(4);
        int a = random.nextInt(20) + 1;
        int b = random.nextInt(20) + 1;

        switch (type) {
            case 0:
                currentAnswer = a + b;
                questionText.setText(a + " + " + b + " = ?");
                break;
            case 1:
                if (a < b) { int temp = a; a = b; b = temp; }
                currentAnswer = a - b;
                questionText.setText(a + " − " + b + " = ?");
                break;
            case 2:
                currentAnswer = a * b;
                questionText.setText(a + " × " + b + " = ?");
                break;
            case 3:
                do { b = random.nextInt(a) + 1; } while (a % b != 0);
                currentAnswer = a / b;
                questionText.setText(a + " ÷ " + b + " = ?");
                break;
        }
        answerEdit.setText("");
        answerEdit.requestFocus();
    }

    private void checkAnswer() {
        try {
            String answerStr = answerEdit.getText().toString().trim();
            if (answerStr.isEmpty()) {
                Toast.makeText(this, R.string.enter_answer, Toast.LENGTH_SHORT).show();
                return;
            }
            int userAnswer = Integer.parseInt(answerStr);
            if (userAnswer == currentAnswer) {
                correctAnswer();
            } else {
                wrongAnswer();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.invalid_answer, Toast.LENGTH_SHORT).show();
        }
    }

    private void correctAnswer() {
        score += 10;
        scoreValue.setText(String.valueOf(score));
        Toast.makeText(this, R.string.correct, Toast.LENGTH_SHORT).show();
        generateQuestion();
    }

    private void wrongAnswer() {
        lives--;
        livesValue.setText(String.valueOf(lives));
        Toast.makeText(this, R.string.wrong, Toast.LENGTH_SHORT).show();
        if (lives <= 0) {
            gameOver();
        } else {
            generateQuestion();
        }
    }

    private void gameOver() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.game_over);
        builder.setMessage(getString(R.string.your_score) + " " + score);

        final EditText nameEdit = new EditText(this);
        nameEdit.setHint(R.string.enter_name);
        builder.setView(nameEdit);

        builder.setPositiveButton(R.string.save, (dialog, which) -> {
            String name = nameEdit.getText().toString().trim();
            if (!name.isEmpty()) {
                dbHelper.insertScore(name, score);
                Toast.makeText(GameActivity.this, R.string.score_saved, Toast.LENGTH_SHORT).show();
            }
            finish();
        });

        builder.setNegativeButton(R.string.cancel, (dialog, which) -> finish());
        builder.setCancelable(false);
        builder.show();
    }

    private void toggleLanguage() {
        Configuration config = getResources().getConfiguration();
        Locale currentLocale = config.locale;
        if (currentLocale.getLanguage().equals("fr")) {
            config.setLocale(Locale.ENGLISH);
        } else {
            config.setLocale(Locale.FRENCH);
        }
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        recreate();
    }
}
