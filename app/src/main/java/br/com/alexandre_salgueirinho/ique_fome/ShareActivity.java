package br.com.alexandre_salgueirinho.ique_fome;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ShareActivity extends AppCompatActivity {

    private TextView textViewShare_Text, textViewNome, textViewSobrenome;
    private EditText editTextNomeRestaurante, editTextNumeroEnvio;
    private Button botaoCompartilhar;
    private ImageView perfil_image;
    public static String userID, tipoUsuario;

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        perfil_image = findViewById(R.id.perfil_image);
        textViewNome = findViewById(R.id.textViewNome);
        textViewSobrenome = findViewById(R.id.textViewSobrenome);
        textViewShare_Text = findViewById(R.id.textViewShare_Text);
        editTextNomeRestaurante = findViewById(R.id.editTextNomeRestaurante);
        editTextNumeroEnvio = findViewById(R.id.editTextNumeroEnvio);
        botaoCompartilhar = findViewById(R.id.botaoCompartilhar);

        //Inicializando atributos do banco de dados
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbUsuarios = firebaseDatabase.getReference("USUARIOS");

        Intent i = getIntent();
        tipoUsuario = i.getStringExtra("tipoUsuario");
        userID = mAuth.getCurrentUser().getUid();

        loadUserInformation();

        botaoCompartilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String telefone;
                String restaurante;

                telefone = editTextNumeroEnvio.getText().toString().trim();
                restaurante = editTextNomeRestaurante.getText().toString().trim();

                if(telefone.isEmpty()){
                    editTextNumeroEnvio.setError("É necessário informar um numero de celular");
                    editTextNumeroEnvio.requestFocus();
                }
                if(telefone.length() < 11){
                    editTextNumeroEnvio.setError("É necessário informar um numero de celular com o DDD");
                    editTextNumeroEnvio.requestFocus();
                }
                if(restaurante.isEmpty()){
                    editTextNomeRestaurante.setError("É necessário informar um restaurante");
                    editTextNomeRestaurante.requestFocus();
                }

                String mensagem = "Ola teste da mensagem com o restautante " + restaurante + ".";

                mensagem = mensagem.replace(" ", "%20");

                String link = "http://api.whatsapp.com/send?phone=55" + telefone + "&text=" + mensagem;

                Uri uri = Uri.parse(link);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private void loadUserInformation() {
        dbUsuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()) {
                    Usuario usuario = usuarioSnapshot.getValue(Usuario.class);

                    if (usuario.getUsuarioId().equals(userID)) {

                        //Atribuir valores
                        textViewSobrenome.setText(usuario.getSobrenome() + ",");
                        textViewNome.setText(usuario.getNome());
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

    //region Criação do Menu

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
                    Intent intentMeusDados = new Intent(getApplicationContext(), MeusDados.class);
                    intentMeusDados.putExtra("tipoUsuario", tipoUsuario);
                    startActivity(intentMeusDados);
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
