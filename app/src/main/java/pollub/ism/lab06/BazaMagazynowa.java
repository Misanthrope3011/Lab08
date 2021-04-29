package pollub.ism.lab06;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {PozycjaMagazynowa.class, HistoriaTransakcji.class}, version = 4, exportSchema = false)
public abstract class BazaMagazynowa extends RoomDatabase {

    public static final String NAZWA_BAZY = "Warzywniak1";
    public abstract PozycjaMagazynowaDAO pozycjaMagazynowaDAO();
    public abstract Transackcje transackcje();
}