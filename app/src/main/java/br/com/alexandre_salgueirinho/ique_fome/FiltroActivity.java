package br.com.alexandre_salgueirinho.ique_fome;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FiltroActivity extends AppCompatActivity {

    private TextView textTituloiQueFome, textViewFiltro, textViewValorMinimo, textViewValorMaximo;
    private EditText textViewValorMinimo_Data, textViewValorMaximo_Data;
    public static Spinner spinnerTipoComida;
    private Button botaoOK;

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);

        textTituloiQueFome = findViewById(R.id.textTituloiQueFome);
        textViewFiltro = findViewById(R.id.textViewFiltro);
        textViewValorMinimo = findViewById(R.id.textViewValorMinimo);
        textViewValorMinimo_Data = findViewById(R.id.textViewValorMinimo_Data);
        textViewValorMaximo = findViewById(R.id.textViewValorMaximo);
        textViewValorMaximo_Data = findViewById(R.id.textViewValorMaximo_Data);
        spinnerTipoComida = findViewById(R.id.spinnerTipoComida);
        botaoOK = findViewById(R.id.botaoOK);

        //Inicializando atributos do banco de dados
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbUsuarios = firebaseDatabase.getReference("USUARIOS");

        ArrayAdapter adapterTipoComida = ArrayAdapter.createFromResource(this, R.array.tipo_comida, R.layout.configuracao_combobox);
        spinnerTipoComida.setAdapter(adapterTipoComida);


    }

    //region Criação do menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //resgate do tipo de usuário
        final String[] tipoUsuario = new String[1];

        dbUsuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()) {
                    Usuario usuario = usuarioSnapshot.getValue(Usuario.class);
                    tipoUsuario[0] = usuario.getTipoUsuario();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        if (tipoUsuario[0].equals("Cliente")) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
        }else if(tipoUsuario[0].equals("Restaurante")){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_restaurante, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final String[] tipoUsuario = new String[1];

        dbUsuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()) {
                    Usuario usuario = usuarioSnapshot.getValue(Usuario.class);
                    tipoUsuario[0] = usuario.getTipoUsuario();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        if (tipoUsuario[0].equals("Cliente")) {
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

        }else if(tipoUsuario[0].equals("Restaurante")){
            switch (item.getItemId()) {
                case R.id.menuLogout_Restaurante:
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    break;

                case R.id.menuTelaInicial_Restaurante:
                    Toast.makeText(getApplicationContext(), "Em desenvolvimento, aguarde.", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.menuCompartilhar_Restaurante:
                    Toast.makeText(getApplicationContext(), "Em desenvolvimento, aguarde.", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.menuMeusDados_Restaurante:
                    break;
            }
        }
        return true;
    }

    //endregion
}
