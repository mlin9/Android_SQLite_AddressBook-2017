package michaellin.lab_assignment_9;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edit_new_student_string, edit_student_id, edit_new_student, edit_student_address;
    private TextView text_students;
    private Button button_add_students, button_remove_students, button_modify_students;

    private DatabaseAdapter database_adapter;

    private List<Student> students;

    int id_counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database_adapter=DatabaseAdapter.getAdapterInstance(this);
        students = database_adapter.getAllStudents();

        edit_new_student_string =(EditText)findViewById(R.id.editTextNewToDoString);
        edit_student_id=(EditText)findViewById(R.id.editTextToDoId);
        edit_new_student=(EditText)findViewById(R.id.editTextNewToDo);
        edit_student_address=(EditText)findViewById(R.id.editTextPlace);

        text_students=(TextView)findViewById(R.id.textViewToDos);


        button_add_students=(Button)findViewById(R.id.buttonAddToDo);
        button_remove_students=(Button)findViewById(R.id.buttonRemoveToDo);
        button_modify_students=(Button)findViewById(R.id.buttonModifyToDo);

        button_modify_students.setOnClickListener(this);
        button_remove_students.setOnClickListener(this);
        button_add_students.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setNewList();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonAddToDo: addNewToDo(); break;
            case R.id.buttonRemoveToDo: removeToDo(); break;
            case R.id.buttonModifyToDo: modifyToDo(); break;
            default: break;
        }
    }

    private void setNewList(){
        text_students.setText(getToDoListString());
    }

    private void addNewToDo(){
        database_adapter.insert(id_counter, edit_new_student_string.getText().toString(), edit_student_address.getText().toString());
        setNewList();
        id_counter++;
    }

    private void removeToDo(){
        database_adapter.delete(Integer.parseInt(edit_student_id.getText().toString()));
        setNewList();
    }

    private void modifyToDo(){
        int id=Integer.parseInt(edit_student_id.getText().toString());
        String newToDO=edit_new_student.getText().toString();
        database_adapter.modify(id,newToDO);
        setNewList();
    }



    private String getToDoListString(){
        students=database_adapter.getAllStudents();
        if(students!=null && students.size()>0){
            StringBuilder stringBuilder=new StringBuilder("");
            for(Student toDo:students){
                stringBuilder.append(toDo.getId()+", "+toDo.getStudent()+", "+toDo.getGrade()+"\n");
            }
            return stringBuilder.toString();
        }else {
            return "No students";
        }
    }
}
