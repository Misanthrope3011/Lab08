package pollub.ism.lab06;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import pollub.ism.lab06.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<CharSequence> adapter;
    private ActivityMainBinding binding;
    String wybraneWarzywoNazwa = "";
    Integer wybraneWarzywoIlosc = 0;
    public enum OperacjaMagazynowa {SKLADUJ, WYDAJ};
    private BazaMagazynowa bazaDanych;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bazaDanych = Room.databaseBuilder(getApplicationContext(), BazaMagazynowa.class, BazaMagazynowa.NAZWA_BAZY)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();

        if(bazaDanych.pozycjaMagazynowaDAO().size() == 0){
            String[] asortyment = getResources().getStringArray(R.array.Asortyment);
            for(String nazwa : asortyment){
                PozycjaMagazynowa pozycjaMagazynowa = new PozycjaMagazynowa();
                pozycjaMagazynowa.NAME = nazwa; pozycjaMagazynowa.QUANTITY = 0;
                bazaDanych.pozycjaMagazynowaDAO().insert(pozycjaMagazynowa);
            }
        }
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
        boolean isValidValue = true;
        Integer zmianaIlosci = null, nowaIlosc = null;

        try {
            zmianaIlosci = Integer.parseInt(binding.edycjaIlosc.getText().toString());
        }catch(NumberFormatException ex){
            return;
        }finally {
            binding.edycjaIlosc.setText("");
        }
        ZoneId z = ZoneId.of( "UTC+2" ) ; // Or get the JVM’s current default time zone: ZoneId.systemDefault()
        ZonedDateTime zdt = ZonedDateTime.now(z);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

        switch (operacja){
            case SKLADUJ: nowaIlosc = wybraneWarzywoIlosc + zmianaIlosci; break;
            case WYDAJ:  if(wybraneWarzywoIlosc - zmianaIlosci >= 0) {
                nowaIlosc = wybraneWarzywoIlosc - zmianaIlosci;
            }
            else {
                isValidValue = false;
                nowaIlosc = wybraneWarzywoIlosc;
                Toast.makeText(this, "Nie ma tyle na magazynie", Toast.LENGTH_SHORT).show();
            }
                break;
        }
        HistoriaTransakcji historiaTransakcji = new HistoriaTransakcji(zdt.format(formatter),wybraneWarzywoIlosc, nowaIlosc, wybraneWarzywoNazwa);

        if(isValidValue) {
            bazaDanych.transackcje().insert(historiaTransakcji);
        }

        bazaDanych.pozycjaMagazynowaDAO().updateQuantityByName(wybraneWarzywoNazwa,nowaIlosc);

        aktualizuj();
    }

    private void aktualizuj(){
        StringBuilder stringBuilder = new StringBuilder();
        wybraneWarzywoIlosc = bazaDanych.pozycjaMagazynowaDAO().findQuantityByName(wybraneWarzywoNazwa);
        binding.tekstStanMagazynu.setText("Stan magazynu dla " + wybraneWarzywoNazwa + " wynosi: " + wybraneWarzywoIlosc);

        for (HistoriaTransakcji result : bazaDanych.transackcje().selectAllUpdates(wybraneWarzywoNazwa)) {
            stringBuilder.append(result.nazwa_warzywa).append(" ").append(result.data_transakcji).append(" ")
                    .append(result.stara_ilosc).append(" ").append(result.nowa_ilosc).append("\n");
        }
        if(bazaDanych.transackcje().selectAllUpdates(wybraneWarzywoNazwa).size() > 0)
            binding.tekstJednostka.setText(String.valueOf(bazaDanych.transackcje().selectAllUpdates(wybraneWarzywoNazwa).get(bazaDanych.transackcje().selectAllUpdates(wybraneWarzywoNazwa).size() - 1).data_transakcji));
        binding.wyswietlDane.setText(stringBuilder.toString());
    }
}