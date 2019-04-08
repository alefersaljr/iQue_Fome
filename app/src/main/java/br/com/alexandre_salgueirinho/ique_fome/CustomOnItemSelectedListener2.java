package br.com.alexandre_salgueirinho.ique_fome;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class CustomOnItemSelectedListener2 implements OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {

        if(parent.getItemAtPosition(pos).toString().equals("Operador") || parent.getItemAtPosition(pos).toString().equals("Gerente")){
            SignUpActivity.buttonRegisterer.setEnabled(true);
            SignUpActivity.buttonRegisterer.setBackgroundColor(0xFFE3CB63);
            SignUpActivity.buttonRegisterer.setTextColor(0xFFCC2A23);
        }
        else{
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