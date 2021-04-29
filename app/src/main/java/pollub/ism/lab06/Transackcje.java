package pollub.ism.lab06;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface Transackcje {

    @Insert  //Automatyczna kwerenda wystarczy
    public void insert(HistoriaTransakcji historiaTransakcji);

    @Update
        //Automatyczna kwerenda wystarczy
    void update(HistoriaTransakcji historiaTransakcji);

    @Query("SELECT transakcja_id,nazwa_warzywa,data_transakcji, stara_ilosc,nowa_ilosc FROM HISTORIA WHERE nazwa_warzywa= :wybraneWarzywoNazwa") //Nasza kwerenda
    List<HistoriaTransakcji> selectAllUpdates(String wybraneWarzywoNazwa);
/*
    @Query("UPDATE Warzywniak SET QUANTITY = :wybraneWarzywoNowaIlosc WHERE NAME= :wybraneWarzywoNazwa")
    void updateQuantityByName(String wybraneWarzywoNazwa, int wybraneWarzywoNowaIlosc);
*/
    @Query("SELECT COUNT(*) FROM HISTORIA") //Ile jest rekordów w tabeli
    int size();

}
