package com.example.todo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class todoList extends ArrayAdapter<todo>{
    private Activity context;
    private static List<todo> ToDoList;

    public static int total;
    public static int progress;
    public static int done;

    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    String uid=user.getUid();
    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;

    public todoList(Activity context, List<todo> ToDoList) {
        super(context, R.layout.list_layout, ToDoList);
        this.context = context;
        this.ToDoList = ToDoList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);
        final CheckedTextView textView = listViewItem.findViewById(R.id.textViewTodo);
        final todo ToDo = ToDoList.get(position);

        textView.setText(ToDo.getText());
        if (ToDo.isDone()) {
            textView.setChecked(true);

        } else {
            textView.setChecked(false);
            textView.setCheckMarkDrawable(null);
        }

        //single onclick
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("todo").child(ToDo.getId()).child("done");

                if (textView.isChecked()) {
                    textView.setCheckMarkDrawable(null);
                    textView.setChecked(false);
                    databaseReference.setValue(false);

                } else {
                    textView.setChecked(true);
                    textView.setCheckMarkDrawable(R.drawable.checked);
                    databaseReference.setValue(true);
                    Toast.makeText(context, "YEY! GOOD WORK!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //long onclick
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showUpdateDialog(ToDo.getId(), ToDo.getText(), ToDo.isDone());
                return true;
            }
        });

        done=0;
        return listViewItem;
    }

    private void showUpdateDialog(String id, String text, final boolean done) {

        dialogBuilder=new AlertDialog.Builder(context);
        LayoutInflater inflater = context.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.updatedialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editText = dialogView.findViewById(R.id.editTodo);
        final Button buttonUpdate = dialogView.findViewById(R.id.updatebtn);
        final Button buttonDelete = dialogView.findViewById(R.id.deletebutton);
        final String id_1 = id;


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editedText = editText.getText().toString();
                boolean temp = done;

                if (!editedText.equals("") && !editedText.equals(" ")) {
                    updateTodo(id_1, editedText, temp);
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTodo(id_1);
            }
        });

        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void deleteTodo(String id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("todo").child(id);
        databaseReference.removeValue();
        Toast.makeText(context, "Task deleted!", Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();


    }

    private boolean updateTodo(String id, String text, boolean done) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("todo").child(id);
        todo Todo = new todo(id, text, done);
        databaseReference.setValue(Todo);
        Toast.makeText(this.context, "Task Successfully Changed!", Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();

        return true;
    }
}
