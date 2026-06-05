package com.example.calculatrice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button boutonCalculatrice;
    private Button boutonJeu;
    private Button boutonHighscores;

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

        boutonCalculatrice = findViewById(R.id.bouton_calculatrice);
        boutonCalculatrice.setOnClickListener(v -> {
            Intent intent = new Intent(this, CalculatriceActivity.class);
            startActivity(intent);
        });

        boutonJeu = findViewById(R.id.bouton_jeu);
        boutonJeu.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        });

        boutonHighscores = findViewById(R.id.bouton_highscores);
        boutonHighscores.setOnClickListener(v -> {
            Intent intent = new Intent(this, HighScoreActivity.class);
            startActivity(intent);
        });
    }
}