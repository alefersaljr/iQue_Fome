package br.com.alexandre_salgueirinho.ique_fome;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MeusDados extends AppCompatActivity {

    //region Inicialização de componentes da tela e banco de dados

    private TextView textViewNome, textViewSobrenome, textViewTelefone, textViewCelular;
    private TextView textViewTelefone_Data, textViewCelular_Data;
    private TextView textViewEmail, textViewTipoUsuario, textViewIndicado, textViewCep;
    private TextView textViewEmail_Data, textViewTipoUsuario_Data, textViewIndicado_Data, textViewCep_Data;
    private TextView textViewCidade, textViewRua, textViewNumero, textViewOffice;
    private TextView textViewCidade_Data, textViewRua_Data, textViewNumero_Data, textViewOffice_Data;
    private ImageView perfil_image;
    private Button botaoHistoricoPedidos;
    public static String userID, tipoUsuario;

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbUsuarios;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_dados);

        //region Atribuição dos componentes da tela e banco de dados

        //Inicializando atributos da tela
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
        botaoHistoricoPedidos = findViewById(R.id.botaoHistoricoPedidos);

        //Inicializando atributos do banco de dados
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbUsuarios = firebaseDatabase.getReference("USUARIOS");

        Intent i = getIntent();
        tipoUsuario = i.getStringExtra("tipoUsuario");
        userID = mAuth.getCurrentUser().getUid();

        //endregion

        loadUserInformation();

        botaoHistoricoPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MeusDados.this, "Em atualização. Por favor, aguarde.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadUserInformation() {
        dbUsuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()) {
                    Usuario usuario = usuarioSnapshot.getValue(Usuario.class);

                    if (usuario.getUsuarioId().equals(userID)) {

                        //Atribuir valores
                        textViewSobrenome.setText(usuario.getSobrenome() + ",");
                        textViewNome.setText(usuario.getNome());
                        textViewTelefone_Data.setText(usuario.getTelefone());
                        textViewCelular_Data.setText(usuario.getCelular());
                        textViewEmail_Data.setText(usuario.getEmail());
                        textViewTipoUsuario_Data.setText(usuario.getTipoUsuario());

                        if (usuario.getTipoUsuario().equals("Cliente")) {

                            //tornar visivel
                            textViewIndicado.setVisibility(View.VISIBLE);
                            textViewIndicado_Data.setVisibility(View.VISIBLE);

                            //tornar não visivel
                            textViewCep.setVisibility(View.GONE);
                            textViewCep_Data.setVisibility(View.GONE);
                            textViewCidade.setVisibility(View.GONE);
                            textViewCidade_Data.setVisibility(View.GONE);
                            textViewRua.setVisibility(View.GONE);
                            textViewRua_Data.setVisibility(View.GONE);
                            textViewNumero.setVisibility(View.GONE);
                            textViewNumero_Data.setVisibility(View.GONE);
                            textViewOffice.setVisibility(View.GONE);
                            textViewOffice_Data.setVisibility(View.GONE);

                            //Atribuir valores
                            textViewIndicado_Data.setText(usuario.getIndicado());

                        } else if (usuario.getTipoUsuario().equals("Restaurante")) {

                            //tornar visivel
                            textViewCep.setVisibility(View.VISIBLE);
                            textViewCep_Data.setVisibility(View.VISIBLE);
                            textViewCidade.setVisibility(View.VISIBLE);
                            textViewCidade_Data.setVisibility(View.VISIBLE);
                            textViewRua.setVisibility(View.VISIBLE);
                            textViewRua_Data.setVisibility(View.VISIBLE);
                            textViewNumero.setVisibility(View.VISIBLE);
                            textViewNumero_Data.setVisibility(View.VISIBLE);
                            textViewOffice.setVisibility(View.VISIBLE);
                            textViewOffice_Data.setVisibility(View.VISIBLE);

                            //tornar não visivel
                            textViewIndicado.setVisibility(View.GONE);
                            textViewIndicado_Data.setVisibility(View.GONE);

                            //Atribuir valores
                            textViewCep_Data.setText(usuario.getCEP());
                            textViewCidade_Data.setText(usuario.getCidade());
                            textViewRua_Data.setText(usuario.getRua());
                            textViewNumero_Data.setText(usuario.getComplemento());
                            textViewOffice_Data.setText(usuario.getCargo());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(perfil_image);
            }
        }
    }

    //region Criação do menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Intent i = getIntent();
        tipoUsuario = i.getStringExtra("tipoUsuario");

        if (tipoUsuario.equals("Cliente")) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
        } else if (tipoUsuario.equals("Restaurante")) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_restaurante, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (tipoUsuario.equals("Cliente")) {
            switch (item.getItemId()) {
                case R.id.menuCompartilhar:
                    Intent intentCompartilhar = new Intent(getApplicationContext(), ShareActivity.class);
                    intentCompartilhar.putExtra("tipoUsuario", tipoUsuario);
                    startActivity(intentCompartilhar);
                    break;

                case R.id.menuLogout:
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    break;

                case R.id.menuMeusDados:
                    break;

                case R.id.menuCarrinho:
                    Intent intentCarrinho = new Intent(getApplicationContext(), CarrinhoActivity.class);
                    intentCarrinho.putExtra("tipoUsuario", tipoUsuario);
                    Toast.makeText(getApplicationContext(), "Em desenvolvimento, aguarde.", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.menuHistorico:
                    Intent intentHistorico = new Intent(getApplicationContext(), HistoricoListaActivity.class);
                    intentHistorico.putExtra("tipoUsuario", tipoUsuario);
                    Toast.makeText(getApplicationContext(), "Em desenvolvimento, aguarde.", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.menuTelaInicial:
                    Intent intentTelaInicial = new Intent(getApplicationContext(), ProfileActivity.class);
                    intentTelaInicial.putExtra("tipoUsuario", tipoUsuario);
                    Toast.makeText(getApplicationContext(), "Em desenvolvimento, aguarde.", Toast.LENGTH_SHORT).show();
                    break;
            }

        } else if (tipoUsuario.equals("Restaurante")) {
            switch (item.getItemId()) {
                case R.id.menuLogout_Restaurante:
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    break;

                case R.id.menuTelaInicial_Restaurante:
                    Intent intentTelaInicial_Restaurante = new Intent(getApplicationContext(), RestauranteTelaInicialActivity.class);
                    intentTelaInicial_Restaurante.putExtra("tipoUsuario", tipoUsuario);
                    Toast.makeText(getApplicationContext(), "Em desenvolvimento, aguarde.", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.menuCompartilhar_Restaurante:
                    Intent intentCompartilhar_Restaurante = new Intent(getApplicationContext(), RestauranteTelaInicialActivity.class);
                    intentCompartilhar_Restaurante.putExtra("tipoUsuario", tipoUsuario);
                    Toast.makeText(getApplicationContext(), "Em desenvolvimento, aguarde.", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.menuMeusDados_Restaurante:
                    Intent intentMeusDados_Restaurante = new Intent(getApplicationContext(), RestauranteTelaInicialActivity.class);
                    intentMeusDados_Restaurante.putExtra("tipoUsuario", tipoUsuario);
                    Toast.makeText(getApplicationContext(), "Em desenvolvimento, aguarde.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        return true;
    }

    //endregion

}
