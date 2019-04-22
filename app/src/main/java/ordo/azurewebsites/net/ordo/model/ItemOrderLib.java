package ordo.azurewebsites.net.ordo.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ordo.azurewebsites.net.ordo.database.ItemOrderBaseHelper;
import ordo.azurewebsites.net.ordo.database.ItemOrderCursorWrapper;

import static ordo.azurewebsites.net.ordo.database.ItemOrderDbSchema.*;

public class ItemOrderLib {
    private static ItemOrderLib sItemOrderLib;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ItemOrderLib get(Context context){
        if(sItemOrderLib == null){
            sItemOrderLib = new ItemOrderLib(context);
        }
        return sItemOrderLib;
    }

    private ItemOrderLib(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new ItemOrderBaseHelper(mContext).getWritableDatabase();

    }

    public void addItemOrder(ItemOrder itemOrder){
        ContentValues values = getContentValues(itemOrder);
        mDatabase.insert(ItemOrderTable.NAME, null, values);
    }

    public void updateItemOrder(ItemOrder itemOrder) {
        String uuidString = itemOrder.getId().toString();
        ContentValues values = getContentValues(itemOrder);
        mDatabase.update(ItemOrderTable.NAME, values,
                ItemOrderTable.Cols.UUID + " = ?",
                new String[] { uuidString });

    }

    private ItemOrderCursorWrapper queryItemOrders(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ItemOrderTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new ItemOrderCursorWrapper(cursor);
    }


    public List<ItemOrder> getItemOrders(){

        List<ItemOrder> itemOrders = new ArrayList<>();
        ItemOrderCursorWrapper cursor = queryItemOrders(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                itemOrders.add(cursor.getItemOrder());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return itemOrders;
    }

    public ItemOrder getItemOrder (UUID id) {
        ItemOrderCursorWrapper cursor = queryItemOrders(
                ItemOrderTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getItemOrder();
        } finally {
            cursor.close();
        }
    }

    private static ContentValues getContentValues(ItemOrder itemOrder) {
        ContentValues values = new ContentValues();
        values.put(ItemOrderTable.Cols.UUID, itemOrder.getId().toString());
        values.put(ItemOrderTable.Cols.TITLE, itemOrder.getTitle());
        values.put(ItemOrderTable.Cols.DATE, itemOrder.getDate().getTime());
        return values;
    }
}
