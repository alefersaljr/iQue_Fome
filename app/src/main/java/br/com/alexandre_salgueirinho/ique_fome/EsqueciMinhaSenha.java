package br.com.alexandre_salgueirinho.ique_fome;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class EsqueciMinhaSenha extends AppCompatActivity implements View.OnClickListener{

    //Dados padrão dos usuários
    private EditText editTextEmailResetPass;
    private Button buttonSend;
    private TextView textViewBackLogin;

    //Conexão com banco e progressBar
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueci_minha_senha);

        //Inicializando os componentes da tela
        editTextEmailResetPass = findViewById(R.id.editTextEmailResetPass);
        buttonSend = findViewById(R.id.buttonSend);
        textViewBackLogin = findViewById(R.id.textViewBackLogin);

        //Iniciando compornentes de banco e progressBar
        progressBar = findViewById(R.id.progressbar);
        firebaseAuth = FirebaseAuth.getInstance();


        buttonSend.setOnClickListener(this);
        textViewBackLogin.setOnClickListener(this);

    }

    private void recuperacaoDeSenha() {
//        actionCodeSettings = {
//                url: 'https://www.example.com/?email=user@example.com', {
//                    packageName: 'com.example.android',
//                        installApp: true,
//                        minimumVersion: '12'
//                    },
//                    handleCodeInApp: true};
//        firebaseAuth.sendPasswordResetEmail('user@example.com', actionCodeSettings)
//                .then(function() {
//                // Password reset email sent.
//        }).catch(function(error) {
//            // Error occurred. Inspect error.code.
//        });
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.sendPasswordResetEmail(editTextEmailResetPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    Toast.makeText(EsqueciMinhaSenha.this, "Sua senha foi enviada por email", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(EsqueciMinhaSenha.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonSend:
                recuperacaoDeSenha();
                break;

            case R.id.textViewBackLogin:
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}