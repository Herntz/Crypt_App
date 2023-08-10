package com.example.crypt_app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crypt_app.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CryptActivity extends BaseActivity {
    private FloatingActionButton btnhomme;
    private LinearLayout btnTeam, btnDecrypt,btnLanguage;
    private TextView txtTeam;
    private EditText edit_text_encrypt, txtKey;
    private TextView txtResult_encrypt;
    private ImageView btnRefresh,btnshare,btnclean,btn_crypt,btnCopy,btnPaste;
    private String keyString,nonEncryptedText;
    private int keys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypt);

        edit_text_encrypt = findViewById(R.id.txtCrypt);
        txtResult_encrypt = findViewById(R.id.txt_text_Encrypt);
        txtKey = findViewById(R.id.txtKey);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnclean =findViewById(R.id.btnClean);
        btnshare = findViewById(R.id.btnShare);
        btn_crypt=findViewById(R.id.btn_Crypts);
        btnCopy=findViewById(R.id.btnCopy);
        btnPaste=findViewById(R.id.btnPaste);


        btnLanguage = findViewById(R.id.btnLanguage);
        registerForContextMenu(btnLanguage); // Attacher le menu contextuel au bouton btnLanguage

        btnLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Afficher le menu contextuel de sélection de la langue
                openContextMenu(view);
            }
        });


        edit_text_encrypt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Ne rien faire avant que le texte change
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nonEncryptedText = charSequence.toString();
                keyString = txtKey.getText().toString();

                // Vérifier si la clé est un nombre valide
                try {
                    keys = Integer.parseInt(keyString);
                    String encryptedText = encrypt_Text(nonEncryptedText, keys);
                    txtResult_encrypt.setText(encryptedText);
                } catch (NumberFormatException ex) {
                    // Gérer l'exception si la conversion échoue (par exemple, si la clé n'est pas un nombre valide)
                    txtResult_encrypt.setText(""); // Effacer le texte crypté s'il y a une erreur de clé
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Ne rien faire après que le texte a changé
            }
        });
        txtKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Ne rien faire avant que le texte change
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nonEncryptedText = edit_text_encrypt.getText().toString();
                keyString = charSequence.toString();

                // Vérifier si la clé est un nombre valide
                try {
                    keys = Integer.parseInt(keyString);
                    String encryptedText = encrypt_Text(nonEncryptedText, keys);
                    txtResult_encrypt.setText(encryptedText);
                } catch (NumberFormatException ex) {
                    // Gérer l'exception si la conversion échoue (par exemple, si la clé n'est pas un nombre valide)
                    txtResult_encrypt.setText(""); // Effacer le texte crypté s'il y a une erreur de clé
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Ne rien faire après que le texte a changé
            }
        });

        // ...

        // ...
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Vérifier si la clé est un nombre valide
                keyString = txtKey.getText().toString();
                if (edit_text_encrypt.getText().toString().isEmpty() || keyString.isEmpty()) {
                    Toast.makeText(CryptActivity.this, "The key field and the text field must not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    keys = Integer.parseInt(keyString);
                } catch (NumberFormatException ex) {
                    // Gérer l'exception si la conversion échoue (par exemple, si la clé n'est pas un nombre valide)
                    txtResult_encrypt.setText(""); // Effacer le texte crypté s'il y a une erreur de clé
                    return; // Arrêter l'exécution du cryptage si la clé n'est pas valide
                }

                nonEncryptedText = edit_text_encrypt.getText().toString();
                txtResult_encrypt.setText(refresh_crypt(nonEncryptedText, keys));
            }
        });

// ...

        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    // Récupérer le texte crypté à partir du TextView
                    String encryptedText = txtResult_encrypt.getText().toString();
                if (txtResult_encrypt.getText().toString().isEmpty()){
                    Toast.makeText(CryptActivity.this, "The result field is empty " , Toast.LENGTH_SHORT).show();

                }else{
                    // Créer une intention pour partager le texte crypté
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, encryptedText);

                    // Afficher la boîte de dialogue pour choisir l'application de partage
                    startActivity(Intent.createChooser(shareIntent, "Partager le texte crypté via :"));
                }}

        });
        btnclean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Clear();
            }
        });
        btnPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Récupérer le gestionnaire de presse-papiers
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                // Vérifier si le presse-papiers contient des données
                if (clipboardManager != null && clipboardManager.hasPrimaryClip()) {
                    // Récupérer le contenu copié
                    CharSequence pasteData = clipboardManager.getPrimaryClip().getItemAt(0).getText();

                    // Vérifier si le contenu n'est pas vide
                    if (pasteData != null) {
                        // Afficher le texte collé dans un Toast
                        edit_text_encrypt.setText(pasteData);
                        Toast.makeText(CryptActivity.this, "Pasted text: " + pasteData, Toast.LENGTH_SHORT).show();
                    } else {
                        // Le presse-papiers est vide
                        Toast.makeText(CryptActivity.this, "Clipboard is empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Le presse-papiers est vide
                    Toast.makeText(CryptActivity.this, "Clipboard is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_crypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Vérifier si la clé est un nombre valide
                keyString = txtKey.getText().toString();
                try {
                    keys = Integer.parseInt(keyString);
                } catch (NumberFormatException ex) {
                    // Gérer l'exception si la conversion échoue (par exemple, si la clé n'est pas un nombre valide)
                    txtResult_encrypt.setText(""); // Effacer le texte crypté s'il y a une erreur de clé
                    return; // Arrêter l'exécution du cryptage si la clé n'est pas valide
                }

                nonEncryptedText = edit_text_encrypt.getText().toString();
                txtResult_encrypt.setText(refresh_crypt(nonEncryptedText, keys));
            }
        });
        btnCopy = findViewById(R.id.btnCopy);
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String encryptedText = txtResult_encrypt.getText().toString();
                if (!encryptedText.isEmpty()) {
                    // Copier le texte crypté dans le presse-papiers
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("encrypted_text", encryptedText);
                    clipboard.setPrimaryClip(clip);

                    // Afficher le toast "Texte copié"
                    Toast.makeText(CryptActivity.this, "Texte copié", Toast.LENGTH_SHORT).show();
                }
            }
        });



        btnhomme = findViewById(R.id.btnhome);
        btnhomme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CryptActivity.this, HomeActivity.class));
            }
        });

        btnTeam = findViewById(R.id.teamBtn);
        txtTeam = findViewById(R.id.txtteam);
        btnTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CryptActivity.this, TeamActivity.class));
                txtTeam.setTextColor(Color.rgb(255, 94, 0));
            }
        });

        btnDecrypt = findViewById(R.id.btnDecrypt);
        btnDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CryptActivity.this, DecryptionActivity.class));
            }
        });
    }

    private String encrypt_Text(String text_simple, int key) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text_simple.length(); i++) {
            char currentChar = text_simple.charAt(i);

            if (Character.isLetter(currentChar)) {
                char base = Character.isLowerCase(currentChar) ? 'a' : 'A';
                char encryptedChar = (char) (((currentChar - base + key) % 26) + base);
                result.append(encryptedChar);
            } else {
                result.append(currentChar);
            }
        }

        return result.toString();
    }
    private String refresh_crypt(String txt, int k){
        String rlt="";
        if (txtKey.getText().equals("") || edit_text_encrypt.getText().equals("")){
            Toast.makeText(this, "The key field and the text field must not be empty", Toast.LENGTH_SHORT).show();

        }else{
            rlt =encrypt_Text(txt,k);

        }
        return rlt;
    }
    private void Clear() {
        edit_text_encrypt.setText("");
        txtResult_encrypt.setText("");
        txtKey.setText("");
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
