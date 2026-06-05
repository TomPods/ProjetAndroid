package com.example.calculatrice;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CalculatriceActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textView = findViewById(R.id.textView);

        findViewById(R.id.bouton_retour).setOnClickListener(v -> finish());

        Button bouton_c = findViewById(R.id.button_c);
        bouton_c.setOnClickListener(v -> textView.setText(""));

        Button bouton_effacer = findViewById(R.id.button_effacer);
        bouton_effacer.setOnClickListener(v -> {
            String s = textView.getText().toString();
            if (!s.isEmpty()) textView.setText(s.substring(0, s.length() - 1));
        });

        Button bouton_egal = findViewById(R.id.button_egal);
        bouton_egal.setOnClickListener(v -> calculer());

        Button bouton_virgule = findViewById(R.id.button_virgule);
        bouton_virgule.setOnClickListener(v -> textView.append("."));

        Button bouton_plus = findViewById(R.id.button_plus);
        bouton_plus.setOnClickListener(v -> textView.append("+"));

        Button bouton_moins = findViewById(R.id.button_moins);
        bouton_moins.setOnClickListener(v -> textView.append("-"));

        Button bouton_multiplier = findViewById(R.id.button_multiplier);
        bouton_multiplier.setOnClickListener(v -> textView.append("×"));

        Button bouton_diviser = findViewById(R.id.button_diviser);
        bouton_diviser.setOnClickListener(v -> textView.append("÷"));

        Button bouton_0 = findViewById(R.id.button_0);
        bouton_0.setOnClickListener(v -> textView.append("0"));
        Button bouton_1 = findViewById(R.id.button_1);
        bouton_1.setOnClickListener(v -> textView.append("1"));
        Button bouton_2 = findViewById(R.id.button_2);
        bouton_2.setOnClickListener(v -> textView.append("2"));
        Button bouton_3 = findViewById(R.id.button_3);
        bouton_3.setOnClickListener(v -> textView.append("3"));
        Button bouton_4 = findViewById(R.id.button_4);
        bouton_4.setOnClickListener(v -> textView.append("4"));
        Button bouton_5 = findViewById(R.id.button_5);
        bouton_5.setOnClickListener(v -> textView.append("5"));
        Button bouton_6 = findViewById(R.id.button_6);
        bouton_6.setOnClickListener(v -> textView.append("6"));
        Button bouton_7 = findViewById(R.id.button_7);
        bouton_7.setOnClickListener(v -> textView.append("7"));
        Button bouton_8 = findViewById(R.id.button_8);
        bouton_8.setOnClickListener(v -> textView.append("8"));
        Button bouton_9 = findViewById(R.id.button_9);
        bouton_9.setOnClickListener(v -> textView.append("9"));
    }

    private void calculer() {
        String expression = textView.getText().toString();
        if (expression.isEmpty()) return;
        try {
            double result = evaluerExpression(expression);
            if (Double.isNaN(result) || Double.isInfinite(result)) {
                textView.setText("Erreur");
                return;
            }
            String resultStr = (result == Math.floor(result) && Math.abs(result) < 1e15)
                    ? String.valueOf((long) result)
                    : String.valueOf(result);
            textView.setText(resultStr);
        } catch (Exception e) {
            Toast.makeText(this, "Expression invalide", Toast.LENGTH_SHORT).show();
        }
    }

    private double evaluerExpression(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Caractère inattendu: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('×')) x *= parseFactor();
                    else if (eat('÷')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = this.pos;
                if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Caractère inattendu: " + (char) ch);
                }
                return x;
            }
        }.parse();
    }
}
