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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    //Dados padrão dos usuários
    private EditText editTextNome, editTextSobreNome, editTextTelefone, editTextCelular, editTextEmail, editTextPassword, editTextIndicado;
    private Spinner spinnerUser;
    private Button buttonRegisterer;
    private TextView textViewSignin;

    //Dados de usuário do tipo Restaurante
    private EditText editTextCep, editTextCidade, editTextRua, editTextNumero;
    private Spinner spinnerOffice;


    //Conexão com banco e progressBar
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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
        firebaseAuth = FirebaseAuth.getInstance();


        buttonRegisterer.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);


//        addListnerOnButton();
        addListenerOnSpinnerItemSelection();


    }

    private void addListenerOnSpinnerItemSelection() {
        spinnerUser = findViewById(R.id.spinnerUser);
        spinnerUser.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

//    private void addListnerOnButton() {
//        spinnerUser = findViewById(R.id.spinnerUser);
//    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("É necessário informar um email");
            editTextEmail.requestFocus();
//            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Favor informar um email válido");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("É necessário informar uma senha");
            editTextPassword.requestFocus();
//            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6){
            editTextPassword.setError("A senha deve possuir pelo menos 6 digitos");
            editTextPassword.requestFocus();
            return;
        }

        //caso a validação seja feita com sucesso, vamos mostrar uma barra de progresso
        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               progressBar.setVisibility(View.GONE);
               if(task.isSuccessful()){
                   finish();
                   startActivity(new Intent(SignUpActivity.this, ProfileActivity.class));
                    //usuário é cadastrado com sucesso e logado, iniciaremos a profile activity aqui
                    Toast.makeText(getApplicationContext(), "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                }else {
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
        switch (view.getId()){
            case R.id.buttonSignUp:
                registerUser();
                break;

            case R.id.textViewLogin:
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}
