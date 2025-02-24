package com.example.ejercicio;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.ejercicio.entities.User;
import com.example.ejercicio.model.UserDAO;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private EditText etDocumento;
    private EditText etUsuario;
    private EditText etNombres;
    private EditText etApellidos;
    private EditText etContraseña;
    private ListView listUsers;
    SQLiteDatabase sqLiteDatabase;
    private Button btnGuardar;
    private Button btnListUsers;
    private Button btnBuscar;
    private  Button btnEliminar;
    private int documento;
    private String nombres;
    private String apellidos;
    private String usuario;
    private String pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        begin();
        btnGuardar.setOnClickListener(this::createUser);
        btnListUsers.setOnClickListener(this::listUserShow);
        btnBuscar.setOnClickListener(this::searchUser);
        btnEliminar.setOnClickListener(this::confirmDeleteUser);
    }
    private void createUser(View view) {
        catchData();
        User user = new User(this.documento,this.nombres, this.apellidos, this.usuario, this.pass);
        UserDAO dao = new UserDAO(this.context, view);
        dao.getInsertUser(user);
        listUserShow(view);
    }
    private void searchUser(View view) {
        String doc = etDocumento.getText().toString();
        if (doc.isEmpty()) {
            Toast.makeText(this, "Ingrese un documento para buscar", Toast.LENGTH_SHORT).show();
            return;
        }

        int document = Integer.parseInt(doc);
        UserDAO dao = new UserDAO(this.context, view);
        User user = dao.getUserByDocument(document);

        if (user != null) {
            etNombres.setText(user.getNames());
            etApellidos.setText(user.getLastNames());
            etUsuario.setText(user.getUser());
            etContraseña.setText(user.getPassword());
            Toast.makeText(this, "Usuario encontrado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
        }
    }
    private void confirmDeleteUser(View view) {
        String doc = etDocumento.getText().toString();
        if (doc.isEmpty()) {
            Toast.makeText(this, "Ingrese un documento para eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Eliminar Usuario")
                .setMessage("¿Estás seguro de eliminar este usuario?")
                .setPositiveButton("Sí", (dialog, which) -> deleteUser(view))
                .setNegativeButton("No", null)
                .show();
    }
    private void deleteUser(View view) {
        int document = Integer.parseInt(etDocumento.getText().toString());
        UserDAO dao = new UserDAO(this.context, view);
        boolean deleted = dao.deleteUser(document);

        if (deleted) {
            Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
            clearFields();
            listUserShow(view);
        } else {
            Toast.makeText(this, "No se pudo eliminar el usuario", Toast.LENGTH_SHORT).show();
        }
    }
    private void catchData(){
        //Validaciones de los datos. Expresiones regulares.
        this.documento = Integer.parseInt(etDocumento.getText().toString());
        this.nombres = etNombres.getText().toString();
        this.apellidos = etApellidos.getText().toString();
        this.usuario = etUsuario.getText().toString();
        this.pass = etContraseña.getText().toString();
    }
    private void listUserShow(View view){
        UserDAO userDao = new UserDAO(context,findViewById(R.id.lvLista));
        ArrayList<User> userList = userDao.getUsersList();
        ArrayAdapter<User> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,userList);
        listUsers.setAdapter(adapter);
    }
    private void clearFields() {
        etDocumento.setText("");
        etNombres.setText("");
        etApellidos.setText("");
        etUsuario.setText("");
        etContraseña.setText("");
    }
    private void begin(){
        this.context = this;
        this.btnEliminar= findViewById(R.id.btnLimpiar);
        this.btnBuscar = findViewById(R.id.btnBuscar);
        this.btnGuardar = findViewById(R.id.btnRegistrar);
        this.btnListUsers = findViewById(R.id.btnListar);
        this.etNombres = findViewById(R.id.etNombres);
        this.etApellidos = findViewById(R.id.etApellidos);
        this.etDocumento = findViewById(R.id.etDocumento);
        this.etUsuario = findViewById(R.id.etUsuario);
        this.etContraseña = findViewById(R.id.etContraseña);
        this.listUsers = findViewById(R.id.lvLista);
    }
}