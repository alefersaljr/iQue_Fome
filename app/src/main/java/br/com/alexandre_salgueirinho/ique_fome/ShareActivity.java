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



        loadUserInformation();

        botaoCompartilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String telefone;
                String restaurante;

                telefone = editTextNumeroEnvio.getText().toString().trim();
                restaurante = editTextNomeRestaurante.getText().toString().trim();

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
                for(DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()){
                    Usuario usuario = usuarioSnapshot.getValue(Usuario.class);

                    //Atribuir valores
                    textViewSobrenome.setText(usuario.getSobrenome() + ",");
                    textViewNome.setText(usuario.getNome());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        final FirebaseUser user = mAuth.getCurrentUser();
//        Usuario user = new Usuario();

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

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuCompartilhar:
                break;

            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.menuMeusDados:
                startActivity(new Intent(this, MeusDados.class));
                break;

            case R.id.menuCarrinho:
                Toast.makeText(getApplicationContext(),"Em desenvolvimento, aguarde.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menuHistorico:
                Toast.makeText(getApplicationContext(),"Em desenvolvimento, aguarde.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menuTelaInicial:
                Toast.makeText(getApplicationContext(),"Em desenvolvimento, aguarde.", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    //endregion
}
