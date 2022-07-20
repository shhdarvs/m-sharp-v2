package com.example.msharp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

/*
The console activity. An array of raw but syntactically correct code is given to it by the editor activity.
It is responsible for then parsing the code, building the AST and executing it.
 */
public class ConsoleActivity extends AppCompatActivity {
    /*Initalize the variable arrays for the program to make use of. */
    public static Map<String, String> Strings = new Hashtable();
    public static Map<String, Integer> Numbers = new Hashtable();
    public static Map<String, String> Bools = new Hashtable();
    public int lineCount = 1;
    Context context = ConsoleActivity.this;

    /*Controls what happens when the play icon is pressed. */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.play_code:

                /*Retrieve the code from the editor activity. */
                ArrayList<String> loadedLines = getIntent().getStringArrayListExtra("program");

                Functions fun = new Functions();

                /*First check no code blocks were left unchanged from their default state. The one major error that can't be caught in the editor. */
                if (!fun.checkNoCodeUnchanged(loadedLines)) {

                    TextView textView = (TextView) findViewById(R.id.simpleTextView);
                    textView.setText("Program still contains unchanged code blocks.");
                    break;
                }

                /*Get the textview the code will print outputs to, reset it to blank. */
                TextView textView = (TextView) findViewById(R.id.simpleTextView);
                textView.setText("");


                /*  Initialize parser */
                Parser parser = new Parser(textView, context);

                /* Initialize new AST for the program */
                Program program = new Program();

                /*Bools to govern if code needs to be parsed by this function, or if they are parsed by an if or while statement. */
                Boolean needToSkipIf = false;
                Boolean needToSkipWhile = false;

                /*Initalize scope tracking ints*/
                int scopeWhile = 0;
                int scopeIf = 0;


                    /*Parse the program, costruct an AST. */
                    for (int x = 0; x < loadedLines.size(); x++) {
                        String line = loadedLines.get(x);
                        String Tokens[] = line.split(" ");

                        if (!needToSkipIf && !needToSkipWhile) {
                            try {
                                parser.ParseTokens(Tokens, line, Numbers, Strings, Bools, program, x, loadedLines);
                            } catch (Exception e) {
                            /*The one other error that cant be caught in the editor is if an if/while scope is opened but never closed.
                              They are caught here. */
                                textView.setText(e.getMessage());
                                break;
                            }
                        }
                        if (Tokens[0].equals("ifEnd")) {
                            scopeIf--;
                            if (scopeIf == 0) {
                                needToSkipIf = false;
                            }

                        }
                        if (Tokens[0].equals("whileEnd")) {
                            scopeWhile--;
                            if (scopeWhile == 0) {
                                needToSkipWhile = false;
                            }

                        }
                        if (Tokens[0].equals("if")) {
                            scopeIf++;
                            needToSkipIf = true;
                        }
                        if (Tokens[0].equals("while")) {
                            scopeWhile++;
                            needToSkipWhile = true;
                        }
                    }





                    program.Run(textView);




                /*The program has been parsed, now it is run. */

                break;

        }
        return super.onOptionsItemSelected(item);
    }


    /*Admin for the top task bar */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_console, menu);
        return true;
    }

    /*On creation admin for the app to know what is where. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_console);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Console");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_backarror);
        BottomNavigationView botNav = findViewById(R.id.bottom_navigation);
        botNav.setOnNavigationItemSelectedListener(navListener);
        botNav.getMenu().getItem(0).setChecked(false);
        botNav.getMenu().getItem(1).setChecked(true);
    }

    /*Functionality to move to the code editor when it is pressed in the navigation bar. */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch(item.getItemId())
                    {

                        case R.id.code:

                            ArrayList<String> programName = getIntent().getStringArrayListExtra("programName");

                            Bundle b = new Bundle();
                            b.putStringArrayList("programName", programName);

                            Intent startIntent = new Intent(getApplicationContext(), EditorActivity.class);
                            startIntent.putExtras(b);
                            startActivity(startIntent);
                            break;
                    }
                    return true;
                }
            };
}