package com.example.msharp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.BufferedReader;
import java.io.FileInputStream;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView MainRecyclerView;
    private MainActivityAdapter MainAdapter;
    private RecyclerView.LayoutManager MainLayoutManager;
    private static final String FILE_NAME = "test.txt";
    public ArrayList<SavedItem> File_names = new ArrayList<>();
    private Context context;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.new_program:
            {
                final String[] programTitle = new String[1];
                FileInputStream fis = null;
                final boolean[] makeNewProgram = {true};
                try {


                    final Handler[] handler = {new Handler() {
                        @Override
                        public void handleMessage(Message mesg) {
                            throw new RuntimeException();
                        }
                    }};

                    AlertDialog.Builder alert = new AlertDialog.Builder(context);

                    alert.setTitle("Please enter program title:");


                    // Set an EditText view to get user input
                    final EditText input = new EditText(context);
                    alert.setView(input);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String value = input.getText().toString();

                            programTitle[0] = value;

                            handler[0].sendMessage(handler[0].obtainMessage());

                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                            handler[0].sendMessage(handler[0].obtainMessage());
                            makeNewProgram[0] = false;
                        }
                    });

                    alert.show();

                    try{
                        Looper.loop();
                    }
                    catch (RuntimeException e) {}

                    boolean programNameNull = programTitle[0].equals("");

                    if(!programNameNull) {


                        if (makeNewProgram[0]) {
                            Functions fun = new Functions();

                            ArrayList<String> existingPrograms = fun.loadFileNamesToArray(context);

                            boolean programExists = false;

                            for (int x = 0; x < existingPrograms.size(); x++) {
                                if (existingPrograms.get(x).equals(programTitle[0])) {
                                    programExists = true;
                                }
                            }

                            if (!programExists) {
                                ArrayList<String> programName = new ArrayList<String>();

                                programName.add(programTitle[0]);
                                Bundle b = new Bundle();
                                b.putStringArrayList("programName", programName);
                                ArrayList<Integer> newFile = new ArrayList<>();
                                newFile.add(1);
                                Bundle n = new Bundle();
                                n.putIntegerArrayList("newFile", newFile);

                                Intent startIntent = new Intent(getApplicationContext(), EditorActivity.class);
                                startIntent.putExtras(b);
                                startIntent.putExtras(n);
                                startActivity(startIntent);
                            } else {
                                Toast toast = Toast.makeText(context, "A program with this name already exists.", Toast.LENGTH_LONG);
                                toast.show();
                            }

                        }
                    }
                    else
                    {
                        Toast toast = Toast.makeText(context, "Invalid program name.", Toast.LENGTH_LONG);
                        toast.show();
                    }


                    //           } catch (FileNotFoundException e) {
                    //               e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }




            }
            }


        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        ArrayList<String> FileNames = new ArrayList<>();
        //load existing list of file names;
        FileInputStream fis = null;

        try {
            fis = openFileInput("program_names.ms");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {
                FileNames.add(text);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Programs");

        for(int x = 0; x < FileNames.size(); x++)
        {
            SavedItem temp = new SavedItem(FileNames.get(x));

            File_names.add(temp);
        }

        //load(); //load saved files names

        buildRecyclerView();




/*
        final FloatingActionButton editorActivityBtn = (FloatingActionButton)findViewById(R.id.newProjectFAB);
        editorActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] programTitle = new String[1];
                FileInputStream fis = null;
                final boolean[] makeNewProgram = {true};
                try {


                    final Handler[] handler = {new Handler() {
                        @Override
                        public void handleMessage(Message mesg) {
                            throw new RuntimeException();
                        }
                    }};

                    AlertDialog.Builder alert = new AlertDialog.Builder(context);

                    alert.setTitle("Please enter program title:");


                    // Set an EditText view to get user input
                    final EditText input = new EditText(context);
                    alert.setView(input);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String value = input.getText().toString();

                            programTitle[0] = value;

                            handler[0].sendMessage(handler[0].obtainMessage());

                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                            handler[0].sendMessage(handler[0].obtainMessage());
                            makeNewProgram[0] = false;
                        }
                    });

                    alert.show();

                    try{
                        Looper.loop();
                    }
                    catch (RuntimeException e) {}

                    boolean programNameNull = programTitle[0].equals("");

                    if(!programNameNull) {


                        if (makeNewProgram[0]) {
                            Functions fun = new Functions();

                            ArrayList<String> existingPrograms = fun.loadFileNamesToArray(context);

                            boolean programExists = false;

                            for (int x = 0; x < existingPrograms.size(); x++) {
                                if (existingPrograms.get(x).equals(programTitle[0])) {
                                    programExists = true;
                                }
                            }

                            if (!programExists) {
                                ArrayList<String> programName = new ArrayList<String>();

                                programName.add(programTitle[0]);
                                Bundle b = new Bundle();
                                b.putStringArrayList("programName", programName);

                                Intent startIntent = new Intent(getApplicationContext(), activity_editor.class);
                                startIntent.putExtras(b);
                                startActivity(startIntent);
                            } else {
                                Toast toast = Toast.makeText(context, "A program with this name already exists.", Toast.LENGTH_LONG);
                                toast.show();
                            }

                        }
                    }
                    else
                    {
                        Toast toast = Toast.makeText(context, "Invalid program name.", Toast.LENGTH_LONG);
                        toast.show();
                    }


     //           } catch (FileNotFoundException e) {
     //               e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }




            }
        });*/
    }

    public void buildRecyclerView() {
        MainRecyclerView = findViewById(R.id.recyclerView);
        //MainRecyclerView.setHasFixedSize(true);
        MainLayoutManager = new LinearLayoutManager(this);
        MainAdapter = new MainActivityAdapter(File_names);
        MainRecyclerView.setLayoutManager(MainLayoutManager);
        MainRecyclerView.setAdapter(MainAdapter);

        MainAdapter.setOnItemClickListener(new MainActivityAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                ArrayList<String> programName = new ArrayList<String>();
                programName.add(File_names.get(position).getText());
                Bundle b = new Bundle();
                b.putStringArrayList("programName", programName);
                Intent startIntent = new Intent(getApplicationContext(), EditorActivity.class);
                startIntent.putExtras(b);
                startActivity(startIntent);
            }

            @Override
            public void onEditClicked(int position) {
                Functions fun = new Functions();
                ArrayList<String> fileNames = fun.loadFileNamesToArray(context);
                ArrayList<String> currentCode = fun.loadCodeIntoArrayFromFile(fileNames.get(position), context);
                String oldName;
                final boolean[] makeNewProgram = new boolean[1];
                makeNewProgram[0] = true;
                final String[] newName = new String[1];
                String lineSeparator = System.getProperty("line.separator");


                final Handler[] handler = {new Handler() {
                    @Override
                    public void handleMessage(Message mesg) {
                        throw new RuntimeException();
                    }
                }};
                //prompt user for new name
                AlertDialog.Builder alert = new AlertDialog.Builder(context);

                alert.setTitle("Edit program name:");


                // Set an EditText view to get user input
                final EditText input = new EditText(context);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();

                        newName[0] = value; //new name for the file

                        handler[0].sendMessage(handler[0].obtainMessage());

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.

                        makeNewProgram[0] = false;

                        handler[0].sendMessage(handler[0].obtainMessage());
                    }
                });

                alert.show();



                try{
                    Looper.loop();
                }
                catch (RuntimeException e) {}


                if (makeNewProgram[0]) { //didn't cancel
                    boolean programNameNull = newName[0].equals(""); //new name not blank
                if(!programNameNull) {






                        boolean programExists = false;

                        for (int x = 0; x < fileNames.size(); x++) {
                            if (fileNames.get(x).equals(newName[0])) {
                                programExists = true;
                            }
                        }

                        if (!programExists) { //ain't reusing an existing name
                            FileOutputStream fos = null;
                            String file = fileNames.get(position);
                            fileNames.remove(position);
                            fileNames.add(position, newName[0]); //add updated name to the list



                            //save new list, now with new file name, with it at top of the list
                            try {
                                fos = openFileOutput("program_names.ms", MODE_PRIVATE);


                                //write the test
                                for(int x = 0; x < fileNames.size(); x++)
                                {
                                    String temp = fileNames.get(x);

                                    fos.write(temp.getBytes());
                                    fos.write(lineSeparator.getBytes());


                                }





                                //Toast.makeText(context, "Saved to " + getFilesDir() + "/" + myProgramName, Toast.LENGTH_LONG).show();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            finally {
                                if (fos != null)
                                {
                                    try {
                                        fos.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            //write our code to the new filename

                            fun.writeProgramToFile(newName[0],context,currentCode);
                            MainAdapter.notifyItemChanged(position);
                            File_names.clear();
                            for(int x = 0; x < fileNames.size(); x++)
                            {
                                SavedItem temp = new SavedItem(fileNames.get(x));

                                File_names.add(temp);
                            }

                            buildRecyclerView();

                            try {
                                fos = openFileOutput(file, MODE_PRIVATE);
                                fos.write("".getBytes());
                                fos.close();
                            }
                            catch (Exception e)
                            {

                            }

                            //load(); //load saved files names



                        } else {
                            Toast toast = Toast.makeText(context, "A program with this name already exists.", Toast.LENGTH_LONG);
                            toast.show();
                        }


                }
                else
                {
                    Toast toast = Toast.makeText(context, "Invalid program name.", Toast.LENGTH_LONG);
                    toast.show();
                }
                }
            }


            @Override
            public void onDeleteClicked(final int position) {

                /*Ask user if they want to delete. If ok, delete, else cancel.*/
                final Handler[] handler = {new Handler() {
                    @Override
                    public void handleMessage(Message mesg) {
                        throw new RuntimeException();
                    }
                }};
                //prompt user for new name
                AlertDialog.Builder alert = new AlertDialog.Builder(context);

                alert.setTitle("Are you sure you want to delete?");




                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        //String fileToDelete = File_names.get(position).getText();
                        ArrayList<String> FileNames = new ArrayList<>();
                        String lineSeparator = System.getProperty("line.separator");
                        //load existing list of file names;
                        FileOutputStream fos = null;
                        FileInputStream fis = null;

                        try {
                            fis = openFileInput("program_names.ms");
                            InputStreamReader isr = new InputStreamReader(fis);
                            BufferedReader br = new BufferedReader(isr);
                            StringBuilder sb = new StringBuilder();
                            String text;
                            while ((text = br.readLine()) != null) {
                                FileNames.add(text);
                            }
                            String file = FileNames.get(position);
                            fos = openFileOutput(file, MODE_PRIVATE);
                            fos.write("".getBytes());
                            fos.close();
                            FileNames.remove(position);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fis != null) {
                                try {
                                    fis.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }



                        //save new list, now with new file name, with it at top of the list
                        try {
                            fos = openFileOutput("program_names.ms", MODE_PRIVATE);
                            for(int x = 0; x < FileNames.size(); x++)
                            {
                                String temp = FileNames.get(x);

                                fos.write(temp.getBytes());
                                fos.write(lineSeparator.getBytes());


                            }
                            //Toast.makeText(context, "Saved to " + getFilesDir() + "/" + myProgramName, Toast.LENGTH_LONG).show();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        finally {
                            if (fos != null)
                            {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        File_names.remove(position);
                        MainAdapter.notifyItemRemoved(position);



                        handler[0].sendMessage(handler[0].obtainMessage());

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.



                        handler[0].sendMessage(handler[0].obtainMessage());
                    }
                });

                alert.show();



                try{
                    Looper.loop();
                }
                catch (RuntimeException e) {}




            }


        });

    }



    public void load()
    {
        FileInputStream fis = null;

        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String text;
            while ((text = br.readLine()) != null) {
                SavedItem temp = new SavedItem(text);
                File_names.add(temp);
            }

        } catch (FileNotFoundException e) {

            //e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}