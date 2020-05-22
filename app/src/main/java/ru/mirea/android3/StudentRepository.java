package ru.mirea.android3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class StudentRepository {
    private static StudentRepository instance;
    SQLiteDatabase database;
    List<Student> students = new ArrayList<>();
    DBHelper helper;
    private StudentRepository(Context context) {
        helper = new DBHelper(context, 2);
        database = helper.getWritableDatabase();
        database.delete("Students", null, null);
        for(int i = 0; i < 5; i++) {
            ContentValues values = new ContentValues();
            values.put("FirstName", "FName "+(i+1));
            values.put("LastName", "LName "+(i+1));
            values.put("MiddleName", "MName "+(i+1));
            values.put("AddingDate", Calendar.getInstance().getTime().toString());
            database.insert("Students", null, values);
        }
        updateRepository();
    }

    public static StudentRepository createInstance(Context context) {
        if(instance == null) {
            instance = new StudentRepository(context);
            return instance;
        }
        return instance;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void updateRepository() {
        students.clear();
        database = helper.getWritableDatabase();
        String order = "ID ASC";
        Cursor cursor = database.query("Students", null,null, null, null, null, order, null);
        if(cursor.moveToFirst()) {
            int idColumn = cursor.getColumnIndex("ID");
            int firstNameColumn = cursor.getColumnIndex("FirstName");
            int lastNameColumn = cursor.getColumnIndex("LastName");
            int middleNameColumn = cursor.getColumnIndex("MiddleName");
            int dateColumn = cursor.getColumnIndex("AddingDate");
            do {
                Student student = new Student();
                student.setId(cursor.getInt(idColumn));
                student.setFirstName(cursor.getString(firstNameColumn));
                student.setMiddleName(cursor.getString(middleNameColumn));
                student.setLastName(cursor.getString(lastNameColumn));
                student.setAddingDate(cursor.getString(dateColumn));
                students.add(student);
            }while(cursor.moveToNext());
        }
        helper.close();
    }
}
