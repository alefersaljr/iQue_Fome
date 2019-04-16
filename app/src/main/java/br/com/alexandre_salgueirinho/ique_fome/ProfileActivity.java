package br.com.alexandre_salgueirinho.ique_fome;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101;
    public static String userID = "", tipoUsuario = "";

    TextView textView;
    ImageView imageView;
    EditText editText;
    Uri uriProfileImage;
    ProgressBar progressBar;
    String profileImageUrl = "";
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editText = findViewById(R.id.editTextDisplayName);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressbar);
        textView = findViewById(R.id.textViewVerified);

        //Inicializando atributos do banco de dados
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbUsuarios = firebaseDatabase.getReference("USUARIOS");

        Intent i = getIntent();
        tipoUsuario = i.getStringExtra("tipoUsuario");

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });

        loadUserInformation();

        findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });

        dbUsuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()) {
                    Usuario usuario = usuarioSnapshot.getValue(Usuario.class);
                    tipoUsuario = usuario.getTipoUsuario();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void loadUserInformation() {
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(imageView);
            }

            if (user.getDisplayName() != null) {
                editText.setText(user.getDisplayName());
            }

            if (user.isEmailVerified()) {
                textView.setText("Email Verified");
            } else {
                textView.setText("Email Not Verified (Click to Verify)");
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ProfileActivity.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }
    }

    private void saveUserInformation() {

        String displayName = editText.getText().toString();

        if (displayName.isEmpty()) {
            editText.setError("Name required");
            editText.requestFocus();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null && profileImageUrl != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();

            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                imageView.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {

        final StorageReference ref = FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");
        UploadTask uploadTask = ref.putFile(uriProfileImage);

        if (uriProfileImage != null) {
            progressBar.setVisibility(View.VISIBLE);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        Uri downloadUri = task.getResult();
                        profileImageUrl = String.valueOf(downloadUri);

                        Log.i("TEST", String.valueOf(downloadUri));
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }

    //region Criação do menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Intent intent = getIntent();
        tipoUsuario = intent.getStringExtra("tipoUsuario");

        if (tipoUsuario != null) {
            if (tipoUsuario.equals("Cliente")) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu, menu);
            } else if (tipoUsuario.equals("Restaurante")) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_restaurante, menu);
            }
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