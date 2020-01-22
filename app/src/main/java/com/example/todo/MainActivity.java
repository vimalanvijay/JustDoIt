package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //pass in done boolean into todoclass when the tick mark is clicked
    //if not send done=false
    //if done is false take it and display it on to-do view else dislpay on done view

    private FloatingActionButton addTodo;
    private LinearLayout list;
    private EditText todoText;
    private ListView listViewTodo;
    private ProgressBar timeProgress;
    private TextView timeProgressText;
    private ProgressBar progressBar;
    private TextView progressText;
    private Toolbar toolbar;

    private String defaultText="";

    private DatabaseReference database;
    String uid =FirebaseAuth.getInstance().getUid();
    private ArrayList<todo> todoArrayList =new ArrayList<>();

    private int done;
    private static int total;
    private int progress;

    int j,k;

    @Override
    protected void onStart() {
        super.onStart();
        total=todoArrayList.size();

        //read from database
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                todoArrayList.clear();

                for(DataSnapshot snapshot :dataSnapshot.child("todo").getChildren()){
                    todo ToDo=snapshot.getValue(todo.class);
                    todoArrayList.add(ToDo);
                }
                todoList adapter=new todoList(MainActivity.this, todoArrayList);
                listViewTodo.setAdapter(adapter);

                for(int i=0;i<todoArrayList.size();i++) {
                    boolean isDone = todoArrayList.get(i).isDone();
                    if (isDone) {
                        j++;
                    }
                    k++;
                }

                done=j;
                total=k;
                overviewPanel(j,k);
                j=0;k=0;
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String uid=user.getUid();
        database= FirebaseDatabase.getInstance().getReference("users").child(uid);

        list = findViewById(R.id.todo_layout);
        addTodo = findViewById(R.id.addbutton);
        todoText= findViewById(R.id.todotext);
        listViewTodo=findViewById(R.id.listViewTodo);
        timeProgress =findViewById(R.id.timeBar);
        timeProgressText =findViewById(R.id.timeText);
        progressBar=findViewById(R.id.progressBar);
        progressText=findViewById(R.id.progressText);

        toolbar=findViewById(R.id.mytoolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        addToDo();//on click lister to to add task



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_to_do_list,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logoutButton:
                logoutUser();
                return true;
            case R.id.settings:
                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logoutUser(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }

    private void addToDo() {
        addTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textEntered=todoText.getText().toString();

                if(!textEntered.equals("") && !textEntered.equals(" ")){

                    //send to database

                    String id=database.push().getKey();
                    todo ToDo=new todo(id,textEntered,false);
                    database.child("todo").child(id).setValue(ToDo);

                    todoText.setText(defaultText);
                    Toast.makeText(getApplicationContext(),"Task added!",Toast.LENGTH_SHORT).show();
                    InputMethodManager inputManager = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }else{
                    Toast.makeText(getApplicationContext(),"Task cannot be empty!",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void overviewPanel(int d,int t){
        SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.getDefault());
        String time = sdf.format(new Date());

        int timeNow=Integer.parseInt(time);

        int timeleft=((timeNow*100)/(24));

        int progress=100-timeleft;



        String temp=String.valueOf(progress);

        timeProgress.setProgress(progress);
        timeProgressText.setText(temp+"%");


        if(t!=0){
            int taskProgress=(d*100)/t;
            String temp2=String.valueOf(taskProgress);

            progressBar.setProgress(taskProgress);
            progressText.setText(temp2+"%");
        }else{
            progressBar.setProgress(0);
            progressText.setText("0%");

        }


    }

}
