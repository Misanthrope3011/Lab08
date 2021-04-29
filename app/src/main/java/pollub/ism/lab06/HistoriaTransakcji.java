package pollub.ism.lab06;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.time.LocalDateTime;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "HISTORIA")
public class HistoriaTransakcji {
    @PrimaryKey(autoGenerate = true)
    public int transakcja_id;
    public String data_transakcji;
    public int stara_ilosc;
    public int nowa_ilosc;
    public String nazwa_warzywa;

    public HistoriaTransakcji(String data_transakcji, int stara_ilosc, int nowa_ilosc, String nazwa_warzywa) {
        this.data_transakcji = data_transakcji;
        this.stara_ilosc = stara_ilosc;
        this.nowa_ilosc = nowa_ilosc;
        this.nazwa_warzywa = nazwa_warzywa;
    }
}
