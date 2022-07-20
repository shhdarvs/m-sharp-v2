package com.example.msharp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class EditorActivity extends AppCompatActivity {
    private DrawerLayout drawerlayout;
    private NavigationView navigationview;
    private TreeNode root;
    private Context context = this;
    public int nodeCount = 1;
    private static final String FILE_NAME = "test.txt";
    private String myProgramName;

    @Override
    public void onBackPressed() {

                Functions fun = new Functions();

                fun.writeProgramToFile(myProgramName,context,root); //save the program to a file

                ArrayList<String> FileNames = fun.loadFileNamesToArray(context); //load the file names

                fun.saveFileNamesWithNewFile(myProgramName,context,FileNames); //save list of files with newly created file included

                Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(startIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ArrayList<String> load = getIntent().getStringArrayListExtra("loadedLines");
        setContentView(R.layout.activity_editor);


        drawerlayout = findViewById(R.id.drawer_layout);
        navigationview = findViewById(R.id.navigationView);


        DraggableTreeView draggableTreeView = (DraggableTreeView) findViewById(R.id.dtv);
        root = new TreeNode(this);
        Context context = this;
        //draggableTreeView.setKeyboard(keyboard);
        BottomNavigationView botNav = findViewById(R.id.bottom_navigation);
        botNav.setOnNavigationItemSelectedListener(navListener);


        try {
            ArrayList<String> programName = getIntent().getStringArrayListExtra("programName");
            //Toast.makeText(context, loadedLines.get(0), Toast.LENGTH_LONG).show();
            myProgramName = programName.get(0);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(myProgramName + ".m#");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_backarror);
            ArrayList<String> loadedCode = new ArrayList<>();
            //load existing list of file names;
            FileInputStream fis = null;


            //load code

            try {
                Functions fun = new Functions();
                fis = openFileInput(myProgramName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String text;
                while ((text = br.readLine()) != null) {
                    loadedCode.add(text);
                }
                for (int x = 0; x < loadedCode.size(); x++) {
                    //output.add(root.data + ";" + Integer.toString(root.type) + ";" + Integer.toString(root.ID) + ";" + Integer.toString(root.getParent().ID));

                    String[] temp = loadedCode.get(x).split(";");
                    if (Integer.parseInt(temp[3]) == 0) //root is parent
                    {
                        root.addChild(new TreeNode(temp[0], Integer.parseInt(temp[1]), Integer.parseInt(temp[2])));
                    } else {


                        root.addChildToID(Integer.parseInt(temp[3]), new TreeNode(temp[0], Integer.parseInt(temp[1]), Integer.parseInt(temp[2])));

                    }

                    nodeCount++;
                }

            } catch (FileNotFoundException e) {

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

        } finally {

        }

        /*
        TreeNode item = new TreeNode("Item 1");
        TreeNode item2 = new TreeNode("Item 2");

        TreeNode subitem = new TreeNode("Sub Item 2");
        subitem.addChild(new TreeNode("Sub Sub Item 1"));
        item.addChild(subitem);
        item.addChild(new TreeNode("Sub Item 1"));
        root.addChild(new TreeNode("Item 3"));
        root.addChild(new TreeNode("Item 9"));
        root.addChild(new TreeNode("Item 10"));
        root.addChild(new TreeNode("Item 11"));
        root.addChild(new TreeNode("Item 12"));
        root.addChild(item2);
        root.addChild(item);
        */


        final SimpleTreeViewAdapter adapter = new SimpleTreeViewAdapter(this, root);
        draggableTreeView.setAdapter(adapter);



        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                DraggableTreeView draggableTreeView = (DraggableTreeView) findViewById(R.id.dtv);
                switch (item.getItemId()) {
                    case R.id.stmIf:
                        //item.setChecked(true);
                        root.addChild(new TreeNode("if COND", 1, nodeCount));
                        nodeCount++;
                        //root.addChild(new TreeNode("ifEnd", 1, nodeCount));
                        //nodeCount++;
                        draggableTreeView.setAdapter(adapter);
                        return true;

                    case R.id.stmAss:
                        //item.setChecked(true);
                        root.addChild(new TreeNode("let VAR = EXP", 2, nodeCount));
                        nodeCount++;
                        draggableTreeView.setAdapter(adapter);
                        return true;

                    case R.id.stmWhile:
                        item.setChecked(true);
                        root.addChild(new TreeNode("while COND",3,nodeCount));
                        nodeCount++;
                        //root.addChild(new TreeNode("whileEnd",3,nodeCount));
                        //nodeCount++;
                        draggableTreeView.setAdapter(adapter);
                        return true;


                    case R.id.stmWrite:
                        //item.setChecked(true);
                        root.addChild(new TreeNode("print EXP",5,nodeCount));
                        nodeCount++;
                        draggableTreeView.setAdapter(adapter);
                        return true;

                    case R.id.stmIfEnd:
                        //item.setChecked(true);
                        root.addChild(new TreeNode("ifEnd", 6, nodeCount));
                        nodeCount++;
                        draggableTreeView.setAdapter(adapter);
                        return true;

                    case R.id.stmWhileEnd:
                        //item.setChecked(true);
                        root.addChild(new TreeNode("whileEnd", 7, nodeCount));
                        nodeCount++;
                        draggableTreeView.setAdapter(adapter);
                        return true;

                }


                return false;
            }

        });


    }

    private void treeNodeData(TreeNode x, ArrayList<String> program)
    {
        if(x.getChildren().size() == 0)
        {
            if(x.ID != 0)
            {
                program.add(x.data.toString());
            }
        }
        else
        {
            if(x.ID != 0)
            {
                program.add(x.data.toString());
            }
            ArrayList<TreeNode> kids = x.getChildren();
            for(int y = 0; y < kids.size(); y++)
            {
                treeNodeData(kids.get(y),program);
            }
        }
    }

    private ArrayList<String> getProgram(TreeNode root)
    {
        ArrayList<String> program = new ArrayList<>();
        treeNodeData(root,program);
        return program;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch(item.getItemId())
                    {
                        case R.id.console:
                            Functions fun = new Functions();
                            //save functionm later
                            fun.writeProgramToFile(myProgramName,context,root);
                            //save the file name

                            ArrayList<String> FileNames = fun.loadFileNamesToArray(context);
                            //load existing list of file names;


                            //save new list, now with new file name, with it at top of the list
                           fun.saveFileNamesWithNewFile(myProgramName,context,FileNames);

                            ///////////////////////////////////////////////////////////////////////////
                            ArrayList<String> program = getProgram(root);
                            Intent startIntent = new Intent(getApplicationContext(), ConsoleActivity.class);
                            Bundle b = new Bundle();
                            b.putStringArrayList("program", program); //bundle the code

                            ArrayList<String> programName = new ArrayList<String>();

                            programName.add(myProgramName);
                            Bundle p = new Bundle();


                            p.putStringArrayList("programName", programName); //bundle the name

                            startIntent.putExtras(b);
                            startIntent.putExtras(p);
                            startActivity(startIntent);
                            break;
                    }
                    return true;
                }
            };

}