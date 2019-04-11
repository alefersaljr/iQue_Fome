package br.com.alexandre_salgueirinho.ique_fome;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        progressBar = findViewById(R.id.progressbar);

        //Inicializando atributos do banco de dados
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbUsuarios = firebaseDatabase.getReference("USUARIOS");

        findViewById(R.id.textViewSignup).setOnClickListener(this);
        findViewById(R.id.textViewMissPassword).setOnClickListener(this);
        findViewById(R.id.buttonLogin).setOnClickListener(this);

    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum lenght of password should be 6");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    finish();
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewSignup:
                finish();
                startActivity(new Intent(this, SignUpActivity.class));
                break;

            case R.id.textViewMissPassword:
                finish();
                startActivity(new Intent(this, EsqueciMinhaSenha.class));
                break;

            case R.id.buttonLogin:
                userLogin();
                break;

        }
    }

//    //region Criação do menu
//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//
//        //resgate do tipo de usuário
//        final String[] tipoUsuario = new String[1];
//
//        dbUsuarios.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()) {
//                    Usuario usuario = usuarioSnapshot.getValue(Usuario.class);
//                    tipoUsuario[0] = usuario.getTipoUsuario();
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//
//        if (tipoUsuario[0].equals("Cliente")) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
//        }else if(tipoUsuario[0].equals("Restaurante")){
//            MenuInflater inflater = getMenuInflater();
//            inflater.inflate(R.menu.menu_restaurante, menu);
//        }
        return true;
    }
//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//
//        final String[] tipoUsuario = new String[1];
//
//        dbUsuarios.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()) {
//                    Usuario usuario = usuarioSnapshot.getValue(Usuario.class);
//                    tipoUsuario[0] = usuario.getTipoUsuario();
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//
//        if (tipoUsuario[0].equals("Cliente")) {
            switch (item.getItemId()) {
                case R.id.menuCompartilhar:
                    startActivity(new Intent(getApplicationContext(), ShareActivity.class));
                    break;

                case R.id.menuLogout:
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    break;

                case R.id.menuMeusDados:
                    break;

                case R.id.menuCarrinho:
                    Toast.makeText(getApplicationContext(), "Em desenvolvimento, aguarde.", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.menuHistorico:
                    Toast.makeText(getApplicationContext(), "Em desenvolvimento, aguarde.", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.menuTelaInicial:
                    Toast.makeText(getApplicationContext(), "Em desenvolvimento, aguarde.", Toast.LENGTH_SHORT).show();
                    break;
            }
//
//        }else if(tipoUsuario[0].equals("Restaurante")){
//            switch (item.getItemId()) {
//                case R.id.menuLogout_Restaurante:
//                    FirebaseAuth.getInstance().signOut();
//                    finish();
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                    break;
//
//                case R.id.menuTelaInicial_Restaurante:
//                    Toast.makeText(getApplicationContext(), "Em desenvolvimento, aguarde.", Toast.LENGTH_SHORT).show();
//                    break;
//
//                case R.id.menuCompartilhar_Restaurante:
//                    Toast.makeText(getApplicationContext(), "Em desenvolvimento, aguarde.", Toast.LENGTH_SHORT).show();
//                    break;
//
//                case R.id.menuMeusDados_Restaurante:
//                    break;
//            }
//        }
        return true;
    }
//
//    //endregion
}