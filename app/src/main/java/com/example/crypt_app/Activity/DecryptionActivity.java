package com.example.crypt_app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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
import android.graphics.Color;
import android.widget.Toast;

import com.example.crypt_app.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DecryptionActivity extends BaseActivity {
    private LinearLayout btnTeam,crypt,btnLanguage;
    private FloatingActionButton home;
    private TextView txtCrypt,txtTeam,txtResult_decrypt;
    private EditText edit_text_encrypt, txtKey;
    private ImageView btnRefresh,btnClean,btn_Decrypt,btnPaste;
    private String keyString,nonEncryptedText;
    private int keys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decryption);

        crypt = findViewById(R.id.cryptBtn);
        txtCrypt = findViewById(R.id.txtCry);
        home = findViewById(R.id.btnhome);
        edit_text_encrypt = findViewById(R.id.txt_Text_Crypt);
        txtResult_decrypt = findViewById(R.id.txt_text_Decrypt);
        txtKey = findViewById(R.id.txtKey);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnClean =findViewById(R.id.btnClean);
        btn_Decrypt=findViewById(R.id.btnDecryped);
        btnTeam = findViewById(R.id.teamBtn);
        txtTeam = findViewById(R.id.txtteam);
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


        // Ajouter un TextWatcher pour le champ de saisie non crypté
        edit_text_encrypt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Ne rien faire avant que le texte change
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                keyString = txtKey.getText().toString();
                String encryptedText = charSequence.toString();
                // Vérifier si la clé est un nombre valide
                try {
                    keys = Integer.parseInt(keyString);
                    String decryptedText = decrypt(encryptedText, keys); // Utilisez le même décalage que pour le chiffrement
                    txtResult_decrypt.setText(decryptedText);
                } catch (NumberFormatException ex) {
                    // Gérer l'exception si la conversion échoue (par exemple, si la clé n'est pas un nombre valide)
                    txtResult_decrypt.setText(""); // Effacer le texte crypté s'il y a une erreur de clé
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
                    String encryptedText = decrypt(nonEncryptedText, keys);
                    txtResult_decrypt.setText(encryptedText);
                } catch (NumberFormatException ex) {
                    // Gérer l'exception si la conversion échoue (par exemple, si la clé n'est pas un nombre valide)
                    txtResult_decrypt.setText(""); // Effacer le texte crypté s'il y a une erreur de clé
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Ne rien faire après que le texte a changé
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Vérifier si la clé est un nombre valide
                keyString = txtKey.getText().toString();
                if (edit_text_encrypt.getText().toString().isEmpty() || keyString.isEmpty()) {
                    Toast.makeText(DecryptionActivity.this, "The key field and the text field must not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    keys = Integer.parseInt(keyString);
                } catch (NumberFormatException ex) {
                    // Gérer l'exception si la conversion échoue (par exemple, si la clé n'est pas un nombre valide)
                    txtResult_decrypt.setText(""); // Effacer le texte crypté s'il y a une erreur de clé
                    return; // Arrêter l'exécution du cryptage si la clé n'est pas valide
                }

                nonEncryptedText = edit_text_encrypt.getText().toString();
                txtResult_decrypt.setText(refresh_crypt(nonEncryptedText, keys));
            }
        });

        btnClean.setOnClickListener(new View.OnClickListener() {
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
                        edit_text_encrypt.setText(pasteData);
                        // Afficher le texte collé dans un Toast

                        Toast.makeText(DecryptionActivity.this, "Pasted text: " + pasteData, Toast.LENGTH_SHORT).show();
                    } else {
                        // Le presse-papiers est vide
                        Toast.makeText(DecryptionActivity.this, "Clipboard is empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Le presse-papiers est vide
                    Toast.makeText(DecryptionActivity.this, "Clipboard is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_Decrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Vérifier si la clé est un nombre valide
                keyString = txtKey.getText().toString();
                if (edit_text_encrypt.getText().toString().isEmpty() || keyString.isEmpty()) {
                    Toast.makeText(DecryptionActivity.this, "The key field and the text field must not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    keys = Integer.parseInt(keyString);
                } catch (NumberFormatException ex) {
                    // Gérer l'exception si la conversion échoue (par exemple, si la clé n'est pas un nombre valide)
                    txtResult_decrypt.setText(""); // Effacer le texte crypté s'il y a une erreur de clé
                    return; // Arrêter l'exécution du cryptage si la clé n'est pas valide
                }

                nonEncryptedText = edit_text_encrypt.getText().toString();
                txtResult_decrypt.setText(refresh_crypt(nonEncryptedText, keys));
            }
        });
        crypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DecryptionActivity.this,CryptActivity.class));
                txtCrypt.setTextColor(Color.rgb(255, 94, 0));
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DecryptionActivity.this,HomeActivity.class));
            }
        });

        btnTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DecryptionActivity.this, TeamActivity.class));
                txtTeam.setTextColor(Color.rgb(255, 94, 0));
            }
        });
    }

    // Julius Caesar decryption function
    private String decrypt(String crypt_text, int shift) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < crypt_text.length(); i++) {
            char currentChar = crypt_text.charAt(i);

            if (Character.isLetter(currentChar)) {
                char base = Character.isLowerCase(currentChar) ? 'a' : 'A';
                char decryptedChar = (char) (((currentChar - base - shift + 26) % 26) + base);
                result.append(decryptedChar);
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
            rlt =decrypt(txt,k);

        }
        return rlt;
    }
    private void Clear() {
        edit_text_encrypt.setText("");
        txtResult_decrypt.setText("");
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