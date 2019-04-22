package ordo.azurewebsites.net.ordo.database;

public class ItemOrderDbSchema {
    public static final class ItemOrderTable {
        public static final String NAME = "item_orders";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
        }
    }

}
