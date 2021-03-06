package br.com.alexandre_salgueirinho.ique_fome;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    //region Instancia dos elementos da tela

    //Dados padrão dos usuários
    private EditText editTextNome, editTextSobreNome, editTextTelefone, editTextCelular, editTextEmail, editTextPassword;
    public static EditText editTextIndicado;
    public static Spinner spinnerUser;
    public static Button buttonRegisterer;
    private TextView textViewSignin;

    //Dados de usuário do tipo Restaurante
    public static EditText editTextCep, editTextCidade, editTextRua, editTextNumero;
    public static Spinner spinnerOffice;

    public static String userID, tipoUsuario;

    //Conexão com banco e progressBar
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbUsuarios;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //region Inicialização dos componentes da tela e banco

        //Inicializando os componentes da tela - User padrão
        editTextNome = findViewById(R.id.editTextNome);
        editTextSobreNome = findViewById(R.id.editTextSobreNome);
        editTextTelefone = findViewById(R.id.editTextTelefone);
        editTextCelular = findViewById(R.id.editTextCelular);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextIndicado = findViewById(R.id.editTextIndicado);
        buttonRegisterer = findViewById(R.id.buttonSignUp);
        textViewSignin = findViewById(R.id.textViewLogin);

        ArrayAdapter adapterUser = ArrayAdapter.createFromResource(this, R.array.tipo_usuario, R.layout.configuracao_combobox);
        spinnerUser = findViewById(R.id.spinnerUser);
        spinnerUser.setAdapter(adapterUser);

        //Iniciando os componentes da tela - User Restaurante
        editTextCep = findViewById(R.id.editTextCep);
        editTextCidade = findViewById(R.id.editTextCidade);
        editTextRua = findViewById(R.id.editTextRua);
        editTextNumero = findViewById(R.id.editTextNumero);

        ArrayAdapter adapterOffice = ArrayAdapter.createFromResource(this, R.array.tipo_cargo, R.layout.configuracao_combobox);
        spinnerOffice = findViewById(R.id.spinnerOffice);
        spinnerOffice.setAdapter(adapterOffice);

        //Iniciando compornentes de banco e progressBar
        progressBar = findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbUsuarios = firebaseDatabase.getReference("USUARIOS");

        //endregion

        buttonRegisterer.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);

        addListenerOnSpinnerItemSelection();
    }

    private void addListenerOnSpinnerItemSelection() {
        spinnerUser = findViewById(R.id.spinnerUser);
        spinnerUser.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        spinnerOffice = findViewById(R.id.spinnerOffice);
        spinnerOffice.setOnItemSelectedListener(new CustomOnItemSelectedListener2());
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (password.isEmpty()) {
            editTextPassword.setError("É necessário informar uma senha");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("A senha deve possuir pelo menos 6 digitos");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String id = mAuth.getCurrentUser().getUid();
                String nome = editTextNome.getText().toString().trim();
                String sobrenome = editTextSobreNome.getText().toString().trim();
                String telefone = editTextTelefone.getText().toString().trim();
                String celular = editTextCelular.getText().toString().trim();
                String tipoUsuario = spinnerUser.getItemAtPosition(spinnerUser.getLastVisiblePosition()).toString().trim();

                String CEP = "", cidade = "", rua = "", complemento = "", cargo = "", indicado = "";

                //Inicializa as variaveis de acordo com o tipoUsuario
                if (tipoUsuario.equals("Cliente")) {
                    indicado = editTextIndicado.getText().toString().trim();
                }
                if (tipoUsuario.equals("Restaurante")) {
                    CEP = editTextCep.getText().toString().trim();
                    cidade = editTextCidade.getText().toString().trim();
                    rua = editTextRua.getText().toString().trim();
                    complemento = editTextNumero.getText().toString().trim();
                    cargo = spinnerOffice.getSelectedItem().toString();
                }

                //region Validação de campos

                //Validações de campo
                if (nome.isEmpty()) {
                    editTextNome.setError("É necessário informar um Nome");
                    editTextNome.requestFocus();
                    return;
                }

                if (sobrenome.isEmpty()) {
                    editTextSobreNome.setError("É necessário informar um Sobrenome");
                    editTextSobreNome.requestFocus();
                    return;
                }

                if (telefone.isEmpty()) {
                    editTextTelefone.setError("É necessário informar um Telefone");
                    editTextTelefone.requestFocus();
                    return;
                }
                if (!Patterns.PHONE.matcher(telefone).matches()) {
                    editTextTelefone.setError("Favor informar um Telefone válido");
                    editTextTelefone.requestFocus();
                    return;
                }

                if (celular.isEmpty()) {
                    editTextCelular.setError("É necessário informar um Celular");
                    editTextCelular.requestFocus();
                    return;
                }
                if (!Patterns.PHONE.matcher(celular).matches()) {
                    editTextCelular.setError("Favor informar um Celular válido");
                    editTextCelular.requestFocus();
                    return;
                }

                if ((mAuth.getCurrentUser().getEmail()).isEmpty()) {
                    editTextEmail.setError("É necessário informar um email");
                    editTextEmail.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher((mAuth.getCurrentUser().getEmail())).matches()) {
                    editTextEmail.setError("Favor informar um email válido");
                    editTextEmail.requestFocus();
                    return;
                }

                if (tipoUsuario.equals("Restaurante")) {
                    if (CEP.isEmpty()) {
                        editTextCep.setError("É necessário informar um CEP");
                        editTextCep.requestFocus();
                        return;
                    }

                    if (cidade.isEmpty()) {
                        editTextCidade.setError("É necessário informar uma Cidade");
                        editTextCidade.requestFocus();
                        return;
                    }

                    if (rua.isEmpty()) {
                        editTextRua.setError("É necessário informar uma Rua");
                        editTextRua.requestFocus();
                        return;
                    }
                }

                //endregion

                //caso a validação seja feita com sucesso, vamos mostrar uma barra de progresso
                progressBar.setVisibility(View.VISIBLE);

                Usuario usuario = new Usuario(id, nome, sobrenome, telefone, celular, mAuth.getCurrentUser().getEmail(), tipoUsuario, indicado, CEP, cidade, rua, complemento, cargo);

                dbUsuarios.child(id).setValue(usuario);
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    finish();
                    getUserType();
                    //usuário é cadastrado com sucesso e logado, iniciaremos a profile activity aqui
                    Toast.makeText(getApplicationContext(), "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "Este email já está cadastrado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSignUp:
                registerUser();
                break;

            case R.id.textViewLogin:
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    private void getUserType() {
        dbUsuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()) {
                    Usuario usuario = usuarioSnapshot.getValue(Usuario.class);
                    userID = mAuth.getCurrentUser().getUid();

                    if (usuario.getUsuarioId().equals(userID)) {
                        tipoUsuario = usuario.getTipoUsuario();
                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        intent.putExtra("tipoUsuario", tipoUsuario);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
