package ordo.azurewebsites.net.ordo.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import ordo.azurewebsites.net.ordo.model.ItemOrder;

import static ordo.azurewebsites.net.ordo.database.ItemOrderDbSchema.*;

public class ItemOrderCursorWrapper extends CursorWrapper {
    public ItemOrderCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ItemOrder getItemOrder() {
        String uuidString = getString(getColumnIndex(ItemOrderTable.Cols.UUID));
        String title = getString(getColumnIndex(ItemOrderTable.Cols.TITLE));
        long date = getLong(getColumnIndex(ItemOrderTable.Cols.DATE));

        ItemOrder itemOrder = new ItemOrder(UUID.fromString(uuidString));
        itemOrder.setTitle(title);
        itemOrder.setDate(new Date(date));

        return itemOrder;
    }
}
