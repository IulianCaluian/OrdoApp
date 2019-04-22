package ordo.azurewebsites.net.ordo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static ordo.azurewebsites.net.ordo.database.ItemOrderDbSchema.*;

public class ItemOrderBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "itemOrderBase.db";

    public ItemOrderBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + ItemOrderTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                ItemOrderTable.Cols.UUID + ", " +
                ItemOrderTable.Cols.TITLE + ", " +
                ItemOrderTable.Cols.DATE + 
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
