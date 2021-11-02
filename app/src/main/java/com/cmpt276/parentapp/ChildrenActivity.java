package com.cmpt276.parentapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cmpt276.model.Child;
import com.cmpt276.model.Options;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ChildrenActivity extends AppCompatActivity {

    private Options options;

    public static Intent getIntent(Context context){
        return new Intent(context, ChildrenActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children);
        options = Options.getInstance(this);

        listItemClick();
        setUpAddBtn();
        populateList();
    }


    private void setUpAddBtn() {
        Button addBtn = (Button) findViewById(R.id.addBtn);
        addBtn.setText("Add new child");

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create dialog to edit children
                AlertDialog.Builder builder = new AlertDialog.Builder(ChildrenActivity.this);
                LinearLayout dialogLayout = new LinearLayout(ChildrenActivity.this);

                //TO DO: Reformat UI with XML
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(5, 5, 5, 5);

                dialogLayout.setOrientation(LinearLayout.VERTICAL);

                builder.setTitle(R.string.addBtnTitle);

                // displays the user input bar
                final EditText nameInput = new EditText(ChildrenActivity.this);
                final EditText ageInput = new EditText(ChildrenActivity.this);
                nameInput.setLayoutParams(params);
                ageInput.setLayoutParams(params);

                nameInput.setInputType(InputType.TYPE_CLASS_TEXT);
                nameInput.setHint(R.string.nameHint);
                builder.setView(nameInput);

                ageInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                ageInput.setHint(R.string.ageHint);
                builder.setView(ageInput);

                dialogLayout.addView(nameInput);
                dialogLayout.addView(ageInput);
                builder.setView(dialogLayout);

                // Set up the buttons to add or exit
                builder.setPositiveButton(R.string.addText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int choice) {
                        options.addChild(new Child(nameInput.getText().toString(), Integer.parseInt(ageInput.getText().toString())));

                        Options.saveChildListInPrefs(ChildrenActivity.this, options.getChildList());
                        Options.saveStringListInPrefs(ChildrenActivity.this, options.getChildListToString());
                        populateList();
                    }
                });
                builder.setNegativeButton(R.string.cancelText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int choice) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    //Populate list view with name and age of children
    private void populateList(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.children_list, options.getChildListToString());

        ListView childrenListView = (ListView) findViewById(R.id.childrenListView);
        childrenListView.setAdapter(adapter);
    }

    //Click handling for children list view
    private void listItemClick(){
        if (options.getChildList().size() == 0)
            return;
        else {
            ListView childrenListView = (ListView) findViewById(R.id.childrenListView);
            childrenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View childClicked, int index, long position) {

                    //Create dialog to edit children
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChildrenActivity.this);
                    LinearLayout dialogLayout = new LinearLayout(ChildrenActivity.this);

                    //TO DO: Reformat UI with XML
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(5, 5, 5, 5);

                    dialogLayout.setOrientation(LinearLayout.VERTICAL);

                    builder.setTitle(R.string.addBtnTitle);

                    final EditText nameInput = new EditText(ChildrenActivity.this);
                    final EditText ageInput = new EditText(ChildrenActivity.this);
                    nameInput.setLayoutParams(params);
                    ageInput.setLayoutParams(params);

                    nameInput.setInputType(InputType.TYPE_CLASS_TEXT);
                    nameInput.setText(options.getChildList().get(index).getName());
                    builder.setView(nameInput);

                    ageInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                    ageInput.setText(String.valueOf(options.getChildList().get(index).getAge()));
                    builder.setView(ageInput);

                    dialogLayout.addView(nameInput);
                    dialogLayout.addView(ageInput);// displays the user input bar
                    builder.setView(dialogLayout);

                    // Set up the buttons to save or exit
                    builder.setPositiveButton(R.string.saveText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int choice) {
                            String newName = nameInput.getText().toString();
                            int newAge = Integer.parseInt(ageInput.getText().toString());
                            options.editChild(index, newName, newAge);
                            Options.saveChildListInPrefs(ChildrenActivity.this, options.getChildList());
                            Options.saveStringListInPrefs(ChildrenActivity.this, options.getChildListToString());
                            populateList();
                        }
                    });
                    builder.setNegativeButton(R.string.cancelText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int choice) {
                            dialog.cancel();
                        }
                    });

                    builder.setNeutralButton(R.string.deleteText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int choice) {
                            options.removeChild(index);
                            Options.saveChildListInPrefs(ChildrenActivity.this, options.getChildList());
                            Options.saveStringListInPrefs(ChildrenActivity.this, options.getChildListToString());
                            populateList();
                        }
                    });

                    builder.show();
                }
            });
        }
    }

}