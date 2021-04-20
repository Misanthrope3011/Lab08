package pollub.ism.lab06;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import pollub.ism.lab06.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<CharSequence> adapter;
    private ActivityMainBinding binding;
    MagazynBazaDanych bazaDanych;
    String wybraneWarzywoNazwa = "";
    Integer wybraneWarzywoIlosc = 0;

    public enum OperacjaMagazynowa {SKLADUJ, WYDAJ};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bazaDanych = new MagazynBazaDanych(this);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adapter = ArrayAdapter.createFromResource(this, R.array.Asortyment, android.R.layout.simple_dropdown_item_1line);
        binding.spinner.setAdapter(adapter);
        binding.przyciskSkladuj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zmienStan(OperacjaMagazynowa.SKLADUJ); // <---
            }
        });

        binding.przyciskWydaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zmienStan(OperacjaMagazynowa.WYDAJ); // <---
            }
        });

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                wybraneWarzywoNazwa = adapter.getItem(i).toString(); // <---
                aktualizuj();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Nie będziemy implementować, ale musi być
            }
        });

    }

        private void zmienStan(OperacjaMagazynowa operacja){

        Integer zmianaIlosci = null, nowaIlosc = null;


        try {
            zmianaIlosci = Integer.parseInt(binding.edycjaIlosc.getText().toString());
        }catch(NumberFormatException ex){
            return;
        }finally {
            binding.edycjaIlosc.setText("");
        }

        switch (operacja){
            case SKLADUJ: nowaIlosc = wybraneWarzywoIlosc + zmianaIlosci; break;
            case WYDAJ:
                if(wybraneWarzywoIlosc - zmianaIlosci >= 0) {
                    nowaIlosc = wybraneWarzywoIlosc - zmianaIlosci;
                }
                else { nowaIlosc = wybraneWarzywoIlosc;
                    Toast.makeText(this, "Nie ma tyle na magazynie", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        bazaDanych.zmienStanMagazynu(wybraneWarzywoNazwa, nowaIlosc);

        aktualizuj();

    }

    private void aktualizuj(){
        wybraneWarzywoIlosc = bazaDanych.podajIlosc(wybraneWarzywoNazwa);
        binding.tekstStanMagazynu.setText(getString(R.string.welcome_messages, wybraneWarzywoNazwa, wybraneWarzywoIlosc));


    }
}