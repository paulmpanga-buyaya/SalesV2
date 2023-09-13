package sales.kiwamirembe.com.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import sales.kiwamirembe.com.classes.Inventory;
import sales.kiwamirembe.com.classes.InventoryMovement;

public class InventoryDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventory_db";
    private static final int DATABASE_VERSION = 1;

    public static final String inventory_table = "inventory";
    public static final String column_inventory_id = "inventory_id";
    public static final String column_inventory_item_id = "inventory_item_id";
    public static final String column_inventory_item_name = "inventory_item_name";
    public static final String column_inventory_item_sku = "inventory_item_sku";
    public static final String column_inventory_item_price = "inventory_item_price";
    public static final String column_inventory_item_stock = "inventory_item_stock";
    public static final String column_inventory_item_web_synced = "inventory_item_web_synced";

    public static final String inventory_movements_table = "inventory_movement";
    public static final String column_inventory_movement_id = "inventory_movement_id";
    public static final String column_inventory_movement_item_id = "inventory_movement_item_id";
    public static final String column_inventory_movement_type = "inventory_movement_type";
    public static final String column_inventory_movement_quantity_changed = "inventory_movement_quantity_changed";
    public static final String column_inventory_movement_reason = "inventory_movement_reason";
    public static final String column_inventory_movement_timestamp = "inventory_movement_timestamp";
    public static final String column_inventory_movement_initiator = "inventory_movement_initiator";
    public static final String column_inventory_movement_source_location_id = "inventory_movement_source_location_id";
    public static final String column_inventory_movement_destination_location_id = "inventory_movement_destination_location_id";
    public static final String column_inventory_movement_web_synced = "inventory_movement_web_synced";


    public static final String create_inventory_movement_table = "create table inventory_movement (inventory_movement_id INTEGER PRIMARY KEY, inventory_movement_item_id INTEGER, inventory_movement_type text, " +
            "inventory_movement_quantity_changed INTEGER, inventory_movement_reason text, inventory_movement_timestamp real, inventory_movement_initiator text, inventory_movement_source_location_id INTEGER, " +
            "inventory_movement_destination_location_id INTEGER, inventory_movement_web_synced INTEGER)";
    public static final String drop_inventory_movement_table = "DROP TABLE IF EXISTS inventory_movement";

    public static final String create_inventory_table = "create table inventory (inventory_id INTEGER PRIMARY KEY, inventory_item_id INTEGER, inventory_item_name text, inventory_item_sku text, inventory_item_price INTEGER, inventory_item_stock INTEGER, inventory_item_web_synced INTEGER)";
    public static final String drop_inventory_table = "DROP TABLE IF EXISTS inventory";

    public InventoryDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_inventory_table);
        db.execSQL(create_inventory_movement_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(drop_inventory_table);
        db.execSQL(drop_inventory_movement_table);
        onCreate(db);
    }

    public boolean addInventoryMovement(InventoryMovement movement) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = createContentValuesFromInventoryMovement(movement);
        long result = db.insert(inventory_movements_table, null, contentValues);
        return result != -1;
    }

    public boolean addInventoryMovementWithId(InventoryMovement movement) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = createContentValuesFromInventoryMovementWithId(movement);
        long result = db.insert(inventory_movements_table, null, contentValues);
        return result != -1;
    }

    public List<InventoryMovement> getAllInventoryMovements() {
        List<InventoryMovement> movementsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(inventory_movements_table, null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                InventoryMovement movement = createInventoryMovementFromCursor(cursor);
                movementsList.add(movement);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return movementsList;
    }

    private InventoryMovement createInventoryMovementFromCursor(Cursor cursor) {
        InventoryMovement movement = new InventoryMovement();
        movement.setInventoryMovementItemId(cursor.getInt(cursor.getColumnIndexOrThrow(column_inventory_movement_item_id)));
        movement.setInventoryMovementType(cursor.getString(cursor.getColumnIndexOrThrow(column_inventory_movement_type)));
        movement.setInventoryMovementQuantityChanged(cursor.getInt(cursor.getColumnIndexOrThrow(column_inventory_movement_quantity_changed)));
        movement.setInventoryMovementReason(cursor.getString(cursor.getColumnIndexOrThrow(column_inventory_movement_reason)));
        movement.setInventoryMovementTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow(column_inventory_movement_timestamp)));
        movement.setInventoryMovementInitiator(cursor.getString(cursor.getColumnIndexOrThrow(column_inventory_movement_initiator)));
        movement.setInventoryMovementSourceLocationId(cursor.getInt(cursor.getColumnIndexOrThrow(column_inventory_movement_source_location_id)));
        movement.setInventoryMovementDestinationLocationId(cursor.getInt(cursor.getColumnIndexOrThrow(column_inventory_movement_destination_location_id)));
        movement.setInventoryMovementWebSynced(cursor.getInt(cursor.getColumnIndexOrThrow(column_inventory_movement_web_synced)));
        return movement;
    }

    private InventoryMovement createInventoryMovementFromCursorWithId(Cursor cursor) {
        InventoryMovement movement = new InventoryMovement();
        movement.setInventoryMovementId(cursor.getInt(cursor.getColumnIndexOrThrow(column_inventory_movement_id)));
        movement.setInventoryMovementItemId(cursor.getInt(cursor.getColumnIndexOrThrow(column_inventory_movement_item_id)));
        movement.setInventoryMovementType(cursor.getString(cursor.getColumnIndexOrThrow(column_inventory_movement_type)));
        movement.setInventoryMovementQuantityChanged(cursor.getInt(cursor.getColumnIndexOrThrow(column_inventory_movement_quantity_changed)));
        movement.setInventoryMovementReason(cursor.getString(cursor.getColumnIndexOrThrow(column_inventory_movement_reason)));
        movement.setInventoryMovementTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow(column_inventory_movement_timestamp)));
        movement.setInventoryMovementInitiator(cursor.getString(cursor.getColumnIndexOrThrow(column_inventory_movement_initiator)));
        movement.setInventoryMovementSourceLocationId(cursor.getInt(cursor.getColumnIndexOrThrow(column_inventory_movement_source_location_id)));
        movement.setInventoryMovementDestinationLocationId(cursor.getInt(cursor.getColumnIndexOrThrow(column_inventory_movement_destination_location_id)));
        movement.setInventoryMovementWebSynced(cursor.getInt(cursor.getColumnIndexOrThrow(column_inventory_movement_web_synced)));
        return movement;
    }

    private ContentValues createContentValuesFromInventoryMovement(InventoryMovement movement) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(column_inventory_movement_item_id, movement.getInventoryMovementItemId());
        contentValues.put(column_inventory_movement_type, movement.getInventoryMovementType());
        contentValues.put(column_inventory_movement_quantity_changed, movement.getInventoryMovementQuantityChanged());
        contentValues.put(column_inventory_movement_reason, movement.getInventoryMovementReason());
        contentValues.put(column_inventory_movement_timestamp, movement.getInventoryMovementTimestamp());
        contentValues.put(column_inventory_movement_initiator, movement.getInventoryMovementInitiator());
        contentValues.put(column_inventory_movement_source_location_id, movement.getInventoryMovementSourceLocationId());
        contentValues.put(column_inventory_movement_destination_location_id, movement.getInventoryMovementDestinationLocationId());
        contentValues.put(column_inventory_movement_web_synced, movement.getInventoryMovementWebSynced());
        return contentValues;
    }

    private ContentValues createContentValuesFromInventoryMovementWithId(InventoryMovement movement) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(column_inventory_movement_id, movement.getInventoryMovementId());
        contentValues.put(column_inventory_movement_item_id, movement.getInventoryMovementItemId());
        contentValues.put(column_inventory_movement_type, movement.getInventoryMovementType());
        contentValues.put(column_inventory_movement_quantity_changed, movement.getInventoryMovementQuantityChanged());
        contentValues.put(column_inventory_movement_reason, movement.getInventoryMovementReason());
        contentValues.put(column_inventory_movement_timestamp, movement.getInventoryMovementTimestamp());
        contentValues.put(column_inventory_movement_initiator, movement.getInventoryMovementInitiator());
        contentValues.put(column_inventory_movement_source_location_id, movement.getInventoryMovementSourceLocationId());
        contentValues.put(column_inventory_movement_destination_location_id, movement.getInventoryMovementDestinationLocationId());
        contentValues.put(column_inventory_movement_web_synced, movement.getInventoryMovementWebSynced());
        return contentValues;
    }

    public boolean addInventoryItem(Inventory inventoryItem) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = createContentValuesFromInventoryItem(inventoryItem);
        long result = db.insert(inventory_table, null, contentValues);
        return result != -1;
    }

    public List<Inventory> getAllInventoryItems() {
        List<Inventory> inventoryItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(inventory_table, null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Inventory inventoryItem = createInventoryItemFromCursor(cursor);
                inventoryItemList.add(inventoryItem);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return inventoryItemList;
    }

    public List<Inventory> getUnsyncedInventoryItems() {
        List<Inventory> unsyncedInventoryItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the selection criteria
        String selection = column_inventory_item_web_synced + " = ?";
        String[] selectionArgs = {"0"}; // 0 represents unsynced

        Cursor cursor = db.query(inventory_table, null, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Inventory inventoryItem = createInventoryItemFromCursor(cursor);
                unsyncedInventoryItems.add(inventoryItem);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return unsyncedInventoryItems;
    }

    public boolean updateInventoryItemSyncStatus(int inventoryItemID, int syncStatus) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(column_inventory_item_web_synced, syncStatus);

        int rowsAffected = db.update(inventory_table, values, column_inventory_item_id + " = ?",
                new String[]{String.valueOf(inventoryItemID)});

        // db.close();
        return rowsAffected > 0;
    }

    private Inventory createInventoryItemFromCursor(Cursor cursor) {
        Inventory inventoryItem = new Inventory();
        inventoryItem.inventory_id = cursor.getInt(cursor.getColumnIndexOrThrow(column_inventory_id));
        inventoryItem.inventory_item_id = cursor.getInt(cursor.getColumnIndexOrThrow(column_inventory_item_id));
        inventoryItem.inventory_item_name = cursor.getString(cursor.getColumnIndexOrThrow(column_inventory_item_name));
        inventoryItem.inventory_item_sku = cursor.getString(cursor.getColumnIndexOrThrow(column_inventory_item_sku));
        inventoryItem.inventory_item_price = cursor.getInt(cursor.getColumnIndexOrThrow(column_inventory_item_price));
        inventoryItem.inventory_item_stock = cursor.getInt(cursor.getColumnIndexOrThrow(column_inventory_item_stock));
        inventoryItem.inventory_item_web_synced = cursor.getInt(cursor.getColumnIndexOrThrow(column_inventory_item_web_synced));
        return inventoryItem;
    }

    private ContentValues createContentValuesFromInventoryItem(Inventory inventoryItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(column_inventory_item_id, inventoryItem.inventory_item_id);
        contentValues.put(column_inventory_item_name, inventoryItem.inventory_item_name);
        contentValues.put(column_inventory_item_sku, inventoryItem.inventory_item_sku);
        contentValues.put(column_inventory_item_price, inventoryItem.inventory_item_price);
        contentValues.put(column_inventory_item_stock, inventoryItem.inventory_item_stock);
        contentValues.put(column_inventory_item_web_synced, inventoryItem.inventory_item_web_synced);
        return contentValues;
    }

    public boolean reduceInventoryItemStock(String inventoryItemSku, int quantityToReduce) {
        SQLiteDatabase db = getWritableDatabase();

        // Get the current stock of the inventory item
        Cursor cursor = db.query(inventory_table, new String[]{column_inventory_item_stock},
                column_inventory_item_sku + " = ?", new String[]{String.valueOf(inventoryItemSku)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int currentStock = cursor.getInt(cursor.getColumnIndexOrThrow(column_inventory_item_stock));
            cursor.close();

            // Calculate the new stock after reduction
            int newStock = currentStock - quantityToReduce;

            // Update the stock in the database
            ContentValues values = new ContentValues();
            values.put(column_inventory_item_stock, newStock);

            int rowsAffected = db.update(inventory_table, values,
                    column_inventory_item_sku + " = ?", new String[]{String.valueOf(inventoryItemSku)});

            return rowsAffected > 0;
        }

        return false;
    }

    public boolean increaseInventoryItemStock(String inventoryItemSku, int quantityToIncrease) {
        SQLiteDatabase db = getWritableDatabase();

        // Get the current stock of the inventory item
        Cursor cursor = db.query(inventory_table, new String[]{column_inventory_item_stock},
                column_inventory_item_sku + " = ?", new String[]{String.valueOf(inventoryItemSku)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int currentStock = cursor.getInt(cursor.getColumnIndexOrThrow(column_inventory_item_stock));
            cursor.close();

            // Calculate the new stock after increase
            int newStock = currentStock + quantityToIncrease;

            // Update the stock in the database
            ContentValues values = new ContentValues();
            values.put(column_inventory_item_stock, newStock);

            int rowsAffected = db.update(inventory_table, values,
                    column_inventory_item_sku + " = ?", new String[]{String.valueOf(inventoryItemSku)});

            return rowsAffected > 0;
        }

        return false;
    }

    public List<InventoryMovement> getUnsyncedInventoryMovements() {
        List<InventoryMovement> unsyncedMovements = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the selection criteria
        String selection = column_inventory_movement_web_synced + " = ?";
        String[] selectionArgs = {"0"}; // 0 represents unsynced

        Cursor cursor = db.query(inventory_movements_table, null, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                InventoryMovement movement = createInventoryMovementFromCursor(cursor);
                unsyncedMovements.add(movement);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return unsyncedMovements;
    }

    public boolean updateInventoryItemStockBySku(String inventoryItemSku, int newStockValue) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(column_inventory_item_stock, newStockValue);

        int rowsAffected = db.update(inventory_table, values,
                column_inventory_item_sku + " = ?", new String[]{inventoryItemSku});

        return rowsAffected > 0;
    }


    public boolean exists(String table, String searchItem, String columnName) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = { columnName };
        String selection = columnName + " =?";
        String[] selectionArgs = { searchItem };
        String limit = "1";
        Cursor cursor = db.query(table, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public int numberOfRows(String table_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, table_name);
        //db.close();
        return numRows;
    }

    public int getItemStockBySku(String sku) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] columns = {column_inventory_item_stock};

        // Define the selection criteria
        String selection = column_inventory_item_sku + " = ?";
        String[] selectionArgs = {sku};

        Cursor cursor = db.query(
                inventory_table,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int stock = -1; // Default value if item not found

        if (cursor != null && cursor.moveToFirst()) {
            stock = cursor.getInt(cursor.getColumnIndexOrThrow(column_inventory_item_stock));
            cursor.close();
        }

        return stock;
    }

}
