package com.dhill.ineptamazon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddEditShoppingItemActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "com.dhill.ineptamazon.EXTRA_ID";
    public static final String EXTRA_NAME =
            "com.dhill.ineptamazon.EXTRA_NAME";
    public static final String EXTRA_DESCRIPTION =
            "com.dhill.ineptamazon.EXTRA_DESCRIPTION";
    public static final String EXTRA_SUPPLY =
            "com.dhill.ineptamazon.EXTRA_SUPPLY";
    private EditText editTextName;
    private EditText editTextDescription;
    private NumberPicker numberPickerSupply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_shopping_item);
        editTextName = findViewById(R.id.edit_text_name);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerSupply = findViewById(R.id.number_picker_supply);
        numberPickerSupply.setMinValue(0);
        numberPickerSupply.setMaxValue(20);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Item");
            editTextName.setText(intent.getStringExtra(EXTRA_NAME));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            numberPickerSupply.setValue(intent.getIntExtra(EXTRA_SUPPLY, 1));

        } else {
            setTitle("Add Item");
        }

    }
    private void saveNote() {
        String name = editTextName.getText().toString();
        String description = editTextDescription.getText().toString();
        int supply = numberPickerSupply.getValue();
        if (name.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please give the product a name and description", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, name);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_SUPPLY, supply);
        setResult(RESULT_OK, data);
        int id=getIntent().getIntExtra(EXTRA_ID,-1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_shoppingitem_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_item:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
