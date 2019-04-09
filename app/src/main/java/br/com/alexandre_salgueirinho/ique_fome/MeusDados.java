package br.com.alexandre_salgueirinho.ique_fome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MeusDados extends AppCompatActivity {

    private TextView textViewNome, textViewSobrenome, textViewTelefone, textViewCelular;
    private TextView textViewTelefone_Data, textViewCelular_Data;
    private TextView textViewEmail, textViewTipoUsuario, textViewIndicado, textViewCep;
    private TextView textViewEmail_Data, textViewTipoUsuario_Data, textViewIndicado_Data, textViewCep_Data;
    private TextView textViewCidade, textViewRua, textViewNumero, textViewOffice;
    private TextView textViewCidade_Data, textViewRua_Data, textViewNumero_Data, textViewOffice_Data;

    private ImageView perfil_image;

    private Button botaoHistoricoPedidos;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_dados);

        //Inicializando os campos da tela
        textViewNome = findViewById(R.id.textViewNome);
        textViewSobrenome = findViewById(R.id.textViewSobrenome);
        textViewTelefone = findViewById(R.id.textViewTelefone);
        textViewTelefone_Data = findViewById(R.id.textViewTelefone_Data);
        textViewCelular = findViewById(R.id.textViewCelular);
        textViewCelular_Data = findViewById(R.id.textViewCelular_Data);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewEmail_Data = findViewById(R.id.textViewEmail_Data);
        textViewTipoUsuario = findViewById(R.id.textViewTipoUsuario);
        textViewTipoUsuario_Data = findViewById(R.id.textViewTipoUsuario_Data);
        textViewIndicado = findViewById(R.id.textViewIndicado);
        textViewIndicado_Data = findViewById(R.id.textViewIndicado_Data);
        textViewCep = findViewById(R.id.textViewCep);
        textViewCep_Data = findViewById(R.id.textViewCep_Data);
        textViewCidade = findViewById(R.id.textViewCidade);
        textViewCidade_Data = findViewById(R.id.textViewCidade_Data);
        textViewRua = findViewById(R.id.textViewRua);
        textViewRua_Data = findViewById(R.id.textViewRua_Data);
        textViewNumero = findViewById(R.id.textViewNumero);
        textViewNumero_Data = findViewById(R.id.textViewNumero_Data);
        textViewOffice = findViewById(R.id.textViewOffice);
        textViewOffice_Data = findViewById(R.id.textViewOffice_Data);

        perfil_image = findViewById(R.id.perfil_image);


        mAuth = FirebaseAuth.getInstance();


        botaoHistoricoPedidos = findViewById(R.id.botaoHistoricoPedidos);

        botaoHistoricoPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MeusDados.this, "Em atualização. Por favor, aguarde.", Toast.LENGTH_SHORT).show();
            }
        });

        loadUserInformation();
    }

    private void loadUserInformation() {
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(perfil_image);
            }

            if(user.getEmail() != null){
                textViewEmail_Data.setText(user.getEmail());
            }
        }
    }
}
