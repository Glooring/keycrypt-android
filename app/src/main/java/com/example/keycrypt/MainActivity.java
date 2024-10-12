package com.example.keycrypt;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    EditText etKey, etGeneratedPassword;
    CheckBox checkBoxAlphanumeric, checkBoxDecryption;
    NumberPicker numberPicker;
    Button btnSubmit;
    public static int passwordLength;

    // Arrays of possible characters for password decryption
    char[] charactersSet1 = {
            '`', '~', '1', '!', '2', '@', '3', '#', '4', '$', '5', '%', '6', '^', '7', '&', '8', '*', '9', '(', '0', ')', '-', '_', '=', '+',
            'q', 'Q', 'w', 'W', 'e', 'E', 'r', 'R', 't', 'T', 'y', 'Y', 'u', 'U', 'i', 'I', 'o', 'O', 'p', 'P', '[', '{', ']', '}', 'a', 'A',
            's', 'S', 'd', 'D', 'f', 'F', 'g', 'G', 'h', 'H', 'j', 'J', 'k', 'K', 'l', 'L', ';', ':', '\'', '"', '\\', '|', 'z', 'Z', 'x',
            'X', 'c', 'C', 'v', 'V', 'b', 'B', 'n', 'N', 'm', 'M', ',', '<', '.', '>', '/', '?'
    };

    char[] charactersSet2 = {
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'q', 'Q', 'w', 'W', 'e', 'E', 'r', 'R', 't', 'T', 'y', 'Y', 'u',
            'U', 'i', 'I', 'o', 'O', 'p', 'P', 'a', 'A', 's', 'S', 'd', 'D', 'f', 'F', 'g', 'G', 'h', 'H', 'j', 'J', 'k', 'K', 'l',
            'L', 'z', 'Z', 'x', 'X', 'c', 'C', 'v', 'V', 'b', 'B', 'n', 'N', 'm', 'M'
    };
    private final TextWatcher keyTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Check if we are in encryption mode and the generated password is not empty
            if (!checkBoxDecryption.isChecked() && !etGeneratedPassword.getText().toString().isEmpty()) {
                etGeneratedPassword.setText("");  // Clear the generated password
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private final TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Check if we are in decryption mode and the key is not empty
            if (checkBoxDecryption.isChecked() && !etKey.getText().toString().isEmpty()) {
                etKey.setText("");  // Clear the key field
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Force dark theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        // Initialize the UI components
        etKey = findViewById(R.id.et1);
        etGeneratedPassword = findViewById(R.id.text_view);
        checkBoxAlphanumeric = findViewById(R.id.checkBoxAlphanumeric);
        checkBoxDecryption = findViewById(R.id.checkBoxDecryption);
        numberPicker = findViewById(R.id.numberPicker);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Set input types for the key field (display the text normally)
        etKey.setInputType(InputType.TYPE_CLASS_TEXT);

        // Configure the number picker
        numberPicker.setMaxValue(50);
        numberPicker.setMinValue(1);
        numberPicker.setValue(25);
        passwordLength = numberPicker.getValue();

        // Set listener for number picker value change
        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> passwordLength = newVal);

        // Add TextWatcher to etKey and etGeneratedPassword
        etKey.addTextChangedListener(keyTextWatcher);
        etGeneratedPassword.addTextChangedListener(passwordTextWatcher);

        // Set up the listener for the Decryption Mode checkbox
        checkBoxDecryption.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateUIComponents(isChecked);
        });

        // Add the listener for checkBoxAlphanumeric changes
        checkBoxAlphanumeric.setOnCheckedChangeListener((buttonView, isChecked) -> {
            handleAlphanumericCheckChange();
        });

        // Initialize the UI components based on the initial mode
        updateUIComponents(checkBoxDecryption.isChecked());
    }

    // Method to update UI components based on mode
    private void updateUIComponents(boolean isDecryptionMode) {
        if (isDecryptionMode) {
            // Decryption Mode
            etKey.setText("");
            // etKey is not editable but clickable (for copying)
            etKey.setFocusable(false);
            etKey.setFocusableInTouchMode(false);
            etKey.setClickable(true);

            // etGeneratedPassword is editable
            etGeneratedPassword.setFocusable(true);
            etGeneratedPassword.setFocusableInTouchMode(true);
            etGeneratedPassword.setClickable(false);

            // NumberPicker is disabled but visible (grayed out)
            numberPicker.setEnabled(false); // Disables interaction but remains visible

            // Set up the OnClickListener for etKey to copy to clipboard
            etKey.setOnClickListener(v -> {
                String text = etKey.getText().toString();
                if (!text.isEmpty()) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Key", text);
                    clipboard.setPrimaryClip(clip);
                    ToastUtil.showToast(MainActivity.this, "Key copied to clipboard");

                    // Clear the text after copying
                    etKey.setText("");
                }
            });

            // Remove OnClickListener from etGeneratedPassword
            etGeneratedPassword.setOnClickListener(null);

        } else {
            etGeneratedPassword.setText("");
            // Encryption Mode

            // etKey is editable
            etKey.setFocusable(true);
            etKey.setFocusableInTouchMode(true);
            etKey.setClickable(false);

            // etGeneratedPassword is not editable but clickable (for copying)
            etGeneratedPassword.setFocusable(false);
            etGeneratedPassword.setFocusableInTouchMode(false);
            etGeneratedPassword.setClickable(true);

            // NumberPicker is enabled
            numberPicker.setEnabled(true); // Enables interaction again

            // Set up the OnClickListener for etGeneratedPassword to copy to clipboard
            etGeneratedPassword.setOnClickListener(v -> {
                String text = etGeneratedPassword.getText().toString();
                if (!text.isEmpty()) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Generated Password", text);
                    clipboard.setPrimaryClip(clip);
                    ToastUtil.showToast(MainActivity.this, "Password copied to clipboard");

                    // Clear the text after copying
                    etGeneratedPassword.setText("");
                }
            });

            // Remove OnClickListener from etKey
            etKey.setOnClickListener(null);
        }
    }

    private void handleAlphanumericCheckChange() {
        if (checkBoxDecryption.isChecked()) {
            // Decrypt mode is enabled
            if (!etKey.getText().toString().isEmpty()) {
                etKey.setText(""); // Clear etKey if empty
            }
        } else {
            // Decrypt mode is not enabled (encryption mode)
            if (!etGeneratedPassword.getText().toString().isEmpty()) {
                etGeneratedPassword.setText(""); // Clear etGeneratedPassword if empty
            }
        }
    }


    public void method1(View view) {
        char[] characterSet = checkBoxAlphanumeric.isChecked() ? charactersSet2 : charactersSet1;
        String key = etKey.getText().toString();
        String generatedPassword = etGeneratedPassword.getText().toString();


        if (checkBoxDecryption.isChecked()) {
            etKey.setText("");
            // Decryption mode
            if (!generatedPassword.isEmpty()) {
                // Validate the password
                if (!isValidInput(generatedPassword, characterSet)) {
                    ToastUtil.showToast(this, "Password contains invalid characters for the selected mode.");
                    return;
                }

                // Extract the key length from the last character of the password (same logic you were using before)
                int keyLength = getKeyLengthFromLastChar(characterSet, generatedPassword.charAt(generatedPassword.length() - 1));

                // Decrypt the password
                String decryptedKey = decryptPassword(characterSet, generatedPassword, keyLength, generatedPassword.length());

                if (decryptedKey == null) {
                    // Decryption failed due to invalid password
                    ToastUtil.showToast(this, "Invalid password.");
                } else {
                    etKey.setText(decryptedKey);
                }
            } else {
                ToastUtil.showToast(this, "Please enter a password to decrypt");
            }
        } else {
            etGeneratedPassword.setText("");
            // Encryption mode
            if (!key.isEmpty()) {
                // Check if the requested password length is less than or equal to the key length
                if (passwordLength <= key.length()) {
                    ToastUtil.showToast(this, "Password length must be greater than the key length.");
                    return;  // Exit the method early
                }
                // Validate the key
                if (!isValidInput(key, characterSet)) {
                    ToastUtil.showToast(this, "Key contains invalid characters for the selected mode.");
                    return;
                }

                String finalPassword = createPassword(characterSet, key, passwordLength);
                etGeneratedPassword.setText(finalPassword);
            } else {
                ToastUtil.showToast(this, "Please enter a key");
            }
        }
    }

    // Validation method to check if input contains only characters from the character set
    private boolean isValidInput(String input, char[] characterSet) {
        for (char c : input.toCharArray()) {
            if (findCharacterIndex(c, characterSet) == -1) {
                return false; // Character not found in character set
            }
        }
        return true; // All characters are valid
    }

    // Find the index of a character in a character set
    public int findCharacterIndex(char c, char[] characterSet) {
        return new String(characterSet).indexOf(c);
    }

    // Perform reverse shift during decryption
    public int reverseShift(int passwordCharIndex, int i, int length, char[] characterSet) {
        return (passwordCharIndex - i - length + characterSet.length) % characterSet.length;
    }

    // Decrypt the password
    public String decryptPassword(char[] characterSet, String password, int keyLength, int passwordLength) {
        StringBuilder decryptedKey = new StringBuilder();

        // Loop through each character except the last one, which is used for key length
        for (int i = 0; i < passwordLength - 1; i++) {
            char passwordChar = password.charAt(i);
            int passwordCharIndex = findCharacterIndex(passwordChar, characterSet);

            // Validate password character index
            if (passwordCharIndex == -1) {
                // Invalid character in password
                return null;
            }

            int originalIndex = reverseShift(passwordCharIndex, i, passwordLength, characterSet);
            decryptedKey.append(characterSet[originalIndex]);
        }
        // Check if keyLength is greater than the length of the decryptedKey
        if (keyLength > decryptedKey.length()) {
            // Invalid key length, return null
            return null;
        }
        // Return the key trimmed to the actual key length
        return decryptedKey.substring(0, keyLength);
    }


    // Determine the key length from the last character
    public int getKeyLengthFromLastChar(char[] characterSet, char lastChar) {
        return findCharacterIndex(lastChar, characterSet);
    }

    // Generate the password based on the encryption logic
    public String createPassword(char[] characterSet, String key, int length) {
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length - 1; i++) {
            int keyCharIndex = findCharacterIndex(key.charAt(i % key.length()), characterSet);
            int shiftIndex = (keyCharIndex + i + length) % characterSet.length;
            password.append(characterSet[shiftIndex]);
        }
        password.append(selectFinalCharacter(characterSet, key.length()));
        return password.toString();
    }

    // Select the last character based on the key length
    public char selectFinalCharacter(char[] characterSet, int keyLength) {
        for (int i = 0, matchCount = 0; i < characterSet.length; i++) {
            if (i % keyLength == 0 && ++matchCount == 2) {
                return characterSet[i];
            }
        }
        return characterSet[0];  // Fallback
    }
}
