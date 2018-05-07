package michaellin.lab_assignment_9;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jimmy on 11/5/2017.
 */

public class DatabaseAdapter {

    String TAG = DatabaseAdapter.class.getSimpleName();

    String DB_NAME = "students.db";
    int DB_VERSION = 2;

    String TABLE_STUDENTS = "table_students";
    String COLUMN_STUDENT_ID = "student_id";
    String COLUMN_STUDENT_NAME = "student_name";
    String COLUMN_STUDENT_GRADE = "student_grade";

    String CREATE_TABLE_STUDENTS = "CREATE TABLE " + TABLE_STUDENTS + " (" + COLUMN_STUDENT_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_STUDENT_NAME + " TEXT, " + COLUMN_STUDENT_GRADE + " TEXT )";

    Context context_buffer;
    SQLiteDatabase database;
    static DatabaseAdapter adapter_instance;

    private DatabaseAdapter(Context context)
    {
        this.context_buffer = context;
        database = new DatabaseHelper(this.context_buffer, DB_NAME, null, DB_VERSION).getWritableDatabase();
    }

    public static DatabaseAdapter getAdapterInstance(Context context)
    {
        if(adapter_instance == null)
            adapter_instance = new DatabaseAdapter(context);

        return adapter_instance;
    }

    public Cursor getAllCursors()
    {
        Cursor cursor = database.query(TABLE_STUDENTS, new String[]{COLUMN_STUDENT_ID, COLUMN_STUDENT_NAME, COLUMN_STUDENT_GRADE}, null, null, null, null, null, null);
        return cursor;
    }

    public Cursor getCursor(String query)
    {
        Cursor cursor = database.query(TABLE_STUDENTS, new String[]{COLUMN_STUDENT_ID, COLUMN_STUDENT_NAME, COLUMN_STUDENT_GRADE}, COLUMN_STUDENT_NAME + " LIKE '%" + query + "%'", null, null, null, null, null);
        return cursor;
    }

    public Cursor getCount()
    {
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + TABLE_STUDENTS, null);
        return cursor;
    }

    public boolean insert(int id, String name, String grade)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STUDENT_ID, id);
        contentValues.put(COLUMN_STUDENT_NAME, name);
        contentValues.put(COLUMN_STUDENT_GRADE, grade);

        return database.insert(TABLE_STUDENTS, null, contentValues) > 0;
    }

    public long insert(ContentValues contentValues)
    {
        return database.insert(TABLE_STUDENTS, null, contentValues);
    }

    public boolean delete(int id)
    {
        return database.delete(TABLE_STUDENTS, COLUMN_STUDENT_ID + " = " + id, null) > 0;
    }

    public int delete(String clause, String[] values)
    {
        return database.delete(TABLE_STUDENTS, clause, values);
    }

    public boolean modify(int id, String value)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STUDENT_NAME, value);

        return database.update(TABLE_STUDENTS, contentValues, COLUMN_STUDENT_ID + " = " + id, null) > 0;
    }

    public int update(ContentValues contentValues, String s, String[] strings)
    {
        return database.update(TABLE_STUDENTS, contentValues, s, strings);
    }

    public List<Student> getAllStudents()
    {
        List<Student> student_list = new ArrayList<Student>();

        Cursor cursor = database.query(TABLE_STUDENTS, new String[]{COLUMN_STUDENT_ID, COLUMN_STUDENT_NAME}, null, null, null, null, null, null);
        if(cursor != null && cursor.getCount() > 0)
        {
            Student student = new Student(cursor.getInt(0), cursor.getString(1));
            student_list.add(student);
        }

        cursor.close();
        return student_list;
    }

    private class DatabaseHelper extends SQLiteOpenHelper
    {
        public DatabaseHelper(Context context, String database_name, SQLiteDatabase.CursorFactory factory, int version)
        {
            super(context, database_name, factory, version);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onConfigure(SQLiteDatabase database)
        {
            super.onConfigure(database);
            database.setForeignKeyConstraintsEnabled(true);
        }

        @Override
        public void onCreate(SQLiteDatabase database)
        {
            database.execSQL(CREATE_TABLE_STUDENTS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int old_ver, int new_ver)
        {
            switch(old_ver)
            {
                case 1: database.execSQL("ALTER TABLE " + TABLE_STUDENTS + " ADD COLUMN " + COLUMN_STUDENT_GRADE + " TEXT"); break;
                default: break;
            }
        }
    }
}
