package com.example.crypt_app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.NavUtils;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.crypt_app.R;

public class TeamActivity extends BaseActivity {
    AppCompatImageView btnBack,btnLanguage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        btnBack = findViewById(R.id.btnBack);


        btnLanguage = findViewById(R.id.btnLanguage);
        registerForContextMenu(btnLanguage); // Attacher le menu contextuel au bouton btnLanguage

        btnLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Afficher le menu contextuel de sélection de la langue
                openContextMenu(view);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retourner à la page parent (HomeActivity)
                NavUtils.navigateUpFromSameTask(TeamActivity.this);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_language, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnEnglish:
                // Changer la langue en anglais
                setNewLocale("en");
                break;
            case R.id.btnFrench:
                // Changer la langue en français
                setNewLocale("fr");
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void setNewLocale(String lang) {
        LocaleManager.setNewLocale(this, lang);
        // Redémarrer l'activité pour appliquer la nouvelle langue
        recreate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Vous pouvez ajouter d'autres éléments de menu ici si vous le souhaitez
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Vous pouvez ajouter d'autres actions de menu ici si vous le souhaitez
        return super.onOptionsItemSelected(item);
    }
}