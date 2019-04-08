package br.com.alexandre_salgueirinho.ique_fome;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class CustomOnItemSelectedListener implements OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {

        if(parent.getItemAtPosition(pos).toString().equals("Restaurante")){
            SignUpActivity.editTextIndicado.setVisibility(View.GONE);
            SignUpActivity.editTextCep.setVisibility(View.VISIBLE);
            SignUpActivity.editTextCidade.setVisibility(View.VISIBLE);
            SignUpActivity.editTextRua.setVisibility(View.VISIBLE);
            SignUpActivity.editTextNumero.setVisibility(View.VISIBLE);
            SignUpActivity.spinnerOffice.setVisibility(View.VISIBLE);
        }
        else if(parent.getItemAtPosition(pos).toString().equals("Cliente")){
            SignUpActivity.editTextCep.setVisibility(View.GONE);
            SignUpActivity.editTextCidade.setVisibility(View.GONE);
            SignUpActivity.editTextRua.setVisibility(View.GONE);
            SignUpActivity.editTextNumero.setVisibility(View.GONE);
            SignUpActivity.spinnerOffice.setVisibility(View.GONE);
            SignUpActivity.editTextIndicado.setVisibility(View.VISIBLE);
            SignUpActivity.buttonRegisterer.setEnabled(true);
            SignUpActivity.buttonRegisterer.setBackgroundColor(0xFFE3CB63);
            SignUpActivity.buttonRegisterer.setTextColor(0xFFCC2A23);
        }else{
            SignUpActivity.editTextCep.setVisibility(View.GONE);
            SignUpActivity.editTextCidade.setVisibility(View.GONE);
            SignUpActivity.editTextRua.setVisibility(View.GONE);
            SignUpActivity.editTextNumero.setVisibility(View.GONE);
            SignUpActivity.spinnerOffice.setVisibility(View.GONE);
            SignUpActivity.editTextIndicado.setVisibility(View.GONE);
            SignUpActivity.buttonRegisterer.setEnabled(false);
            SignUpActivity.buttonRegisterer.setBackgroundColor(0xFFCCCCCC);
            SignUpActivity.buttonRegisterer.setTextColor(0xFF969696);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        Toast.makeText(arg0.getContext(), "É obrigatório informar um tipo de usuário", Toast.LENGTH_SHORT).show();
    }

}