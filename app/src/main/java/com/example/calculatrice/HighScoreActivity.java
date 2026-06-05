package com.example.calculatrice;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HighScoreActivity extends AppCompatActivity {

    private ListView scoresList;
    private ScoreDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_high_score);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new ScoreDatabaseHelper(this);
        scoresList = findViewById(R.id.scores_list);

        findViewById(R.id.back_button).setOnClickListener(v -> finish());

        TextView languageButton = findViewById(R.id.language_button);
        languageButton.setOnClickListener(v -> toggleLanguage());

        loadScores();
    }

    private void loadScores() {
        List<ScoreDatabaseHelper.Score> scores = dbHelper.getTopScores(10);
        List<String> scoreStrings = new ArrayList<>();

        for (int i = 0; i < scores.size(); i++) {
            ScoreDatabaseHelper.Score s = scores.get(i);
            scoreStrings.add((i + 1) + ".   " + s.getName() + "   —   " + s.getScore() + " pts");
        }

        if (scoreStrings.isEmpty()) {
            scoreStrings.add(getString(R.string.no_scores));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.item_score,
                scoreStrings
        );

        scoresList.setAdapter(adapter);
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

    @Override
    protected void onResume() {
        super.onResume();
        loadScores();
    }
}
