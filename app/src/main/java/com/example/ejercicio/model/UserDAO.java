package com.example.ejercicio.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;

import com.example.ejercicio.entities.User;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class UserDAO {
    //1. Declaración de atributos tipo Objeto
    private ManagerDataBaseUser dataBaseUser;
    private Context context;
    private View view;
    private User user;

    //2. Constructor
    public UserDAO(Context context, View view) {
        this.context = context;
        this.view = view;
        this.dataBaseUser = new ManagerDataBaseUser(this.context);
    }
    //2. Encapsulación de metodo
    private void insertUser(User user){
        try {
            SQLiteDatabase sqLiteDatabase = dataBaseUser.getWritableDatabase();
            if(sqLiteDatabase != null){
                final int STATUS = 1;
                ContentValues values = new ContentValues();
                values.put("use_document", user.getDocument());
                values.put("use_names", user.getNames());
                values.put("use_last_names", user.getLastNames());
                values.put("use_user", user.getUser());
                values.put("use_password", user.getPassword());
                values.put("use_password", user.getPassword());
                values.put("use_status", STATUS);
                long response = sqLiteDatabase.insert("users", null, values);
                Snackbar.make(this.view, "Se ha registrado el usuario: "+response, Snackbar.LENGTH_LONG).show();
            }else {
                Snackbar.make(this.view, "No se puede registrar el usuario: ", Snackbar.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.i("Error en la gestión de la base de datos", " "+e.getMessage().toString());
            Snackbar.make(this.view, "Error en la gestión de la base de datos"+e.getMessage().toString(), Snackbar.LENGTH_LONG).show();
            throw new RuntimeException(e);
        }
    }
    //3. Metodo de acceso a insertUser
    public void getInsertUser(User user){
        insertUser(user);
    }
    //4. Consulta de todos los usuarios.
    public ArrayList<User> getUsersList(){
        try {
            SQLiteDatabase sqLiteDatabase = dataBaseUser.getReadableDatabase();
            String query = "select * from users where use_status = 1;";
            ArrayList<User> listUsers = new ArrayList<>();
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            if (cursor.moveToFirst()){
                do {
                    User user1 = new User();
                    user1.setDocument(cursor.getInt(0));
                    user1.setNames(cursor.getString(1));
                    user1.setLastNames(cursor.getString(2));
                    user1.setUser(cursor.getString(3));
                    user1.setPassword(cursor.getString(4));
                    listUsers.add(user1);

                }while (cursor.moveToNext());
            }
            cursor.close();
            sqLiteDatabase.close();
            return listUsers;

        } catch (Exception e) {
            Log.i("Error en la consulta de la base de datos", " "+e.getMessage().toString());
            throw new RuntimeException(e);
        }
    }
    // Buscar usuario por documento
    public User getUserByDocument(int document) {
        User user = null;
        try {
            SQLiteDatabase sqLiteDatabase = dataBaseUser.getReadableDatabase();
            String query = "SELECT * FROM users WHERE use_document = ? AND use_status = 1;";
            Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{String.valueOf(document)});
            if (cursor.moveToFirst()) {
                user = new User();
                user.setDocument(cursor.getInt(0));
                user.setNames(cursor.getString(1));
                user.setLastNames(cursor.getString(2));
                user.setUser(cursor.getString(3));
                user.setPassword(cursor.getString(4));
            }
            cursor.close();
            sqLiteDatabase.close();
        } catch (Exception e) {
            Log.i("Error al buscar usuario", " "+e.getMessage().toString());
        }
        return user;
    }
    // Eliminar usuario por documento
    public boolean deleteUser(int document) {
        try {
            SQLiteDatabase sqLiteDatabase = dataBaseUser.getWritableDatabase();
            int rows = sqLiteDatabase.delete("users", "use_document = ?", new String[]{String.valueOf(document)});
            sqLiteDatabase.close();
            return rows > 0;
        } catch (Exception e) {
            Log.i("Error al eliminar usuario", " "+e.getMessage().toString());
            return false;
        }
    }

}
