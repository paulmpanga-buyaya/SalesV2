package sales.kiwamirembe.com.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import sales.kiwamirembe.com.classes.Category;
import sales.kiwamirembe.com.classes.Item;
import sales.kiwamirembe.com.classes.Route;
import sales.kiwamirembe.com.classes.Unit;

public class ItemsDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "itemsDB_v2_.db";
    public static final int DATABASE_VERSION = 1;

    public static final String units_table = "units";
    public static final String column_unit_id = "unit_id";
    public static final String column_unit_name = "unit_name";
    public static final String column_unit_web_synced = "unit_web_synced";

    public static final String categories_table = "categories";
    public static final String column_category_id = "category_id";
    public static final String column_category_name = "category_name";
    public static final String column_category_sku = "category_sku";
    public static final String column_category_web_synced = "category_web_synced";

    public static final String items_table = "items";
    public static final String column_item_id = "item_id";
    public static final String column_item_name = "item_name";
    public static final String column_item_sku = "item_sku";
    public static final String column_item_price = "item_price";
    public static final String column_item_unit_id = "item_unit_id";
    public static final String column_item_category_id = "item_category_id";
    public static final String column_item_web_synced = "item_web_synced";

    public static final String routes_table = "routes";
    public static final String column_route_id = "route_id";
    public static final String column_route_name = "route_name";
    public static final String column_route_web_synced = "route_web_synced";

    public static final String create_units_table = "create table units (unit_id INTEGER PRIMARY KEY, unit_name text, unit_web_synced int)";
    private static final String drop_units_table = "DROP TABLE IF EXISTS units";

    public static final String create_categories_table = "create table categories (category_id INTEGER PRIMARY KEY, category_name text, category_sku text, category_web_synced int)";
    private static final String drop_categories_table = "DROP TABLE IF EXISTS categories";

    public static final String create_items_table = "create table items (item_id INTEGER PRIMARY KEY, item_name text, item_sku text, item_price real, item_unit_id int, item_category_id int, item_web_synced int)";
    private static final String drop_items_table = "DROP TABLE IF EXISTS items";

    public static final String create_routes_table = "create table routes (route_id INTEGER PRIMARY KEY, route_name text, route_web_synced int)";
    private static final String drop_routes_table = "DROP TABLE IF EXISTS routes";

    public Context context;

    public ItemsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_units_table);
        db.execSQL(create_categories_table);
        db.execSQL(create_items_table);
        db.execSQL(create_routes_table);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(drop_units_table);
        db.execSQL(drop_categories_table);
        db.execSQL(drop_items_table);
        db.execSQL(drop_routes_table);
        onCreate(db);
    }

    // Method to add a route to the database; this adds Route from the server
    public void addRouteWithId(Route route) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(column_route_id, route.getRoute_id());
        values.put(column_route_name, route.getRoute_name());
        values.put(column_route_web_synced, route.getRoute_web_synced());
        db.insert(routes_table, null, values);
        //db.close();
    }

    // Method to add a route to the database; this adds Route from the device
    public void addRouteWithoutId(Route route) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(column_route_name, route.getRoute_name());
        values.put(column_route_web_synced, route.getRoute_web_synced());
        db.insert(routes_table, null, values);
        //db.close();
    }

    // Method to retrieve a route by route_id from the database
    public Route getRouteById(int routeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(routes_table, null, column_route_id + " = ?",
                new String[]{String.valueOf(routeId)}, null, null, null);

        Route route = null;
        if (cursor != null && cursor.moveToFirst()) {
            route = new Route();
            route.setRoute_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_route_id)));
            route.setRoute_name(cursor.getString(cursor.getColumnIndexOrThrow(column_route_name)));
            route.setRoute_web_synced(cursor.getInt(cursor.getColumnIndexOrThrow(column_route_web_synced)));
            cursor.close();
        }

        //db.close();
        return route;
    }

    // Method to retrieve all routes from the database
    public List<Route> getAllRoutes() {
        List<Route> routeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(routes_table, null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Route route = new Route();
                route.setRoute_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_route_id)));
                route.setRoute_name(cursor.getString(cursor.getColumnIndexOrThrow(column_route_name)));
                route.setRoute_web_synced(cursor.getInt(cursor.getColumnIndexOrThrow(column_route_web_synced)));
                routeList.add(route);
                cursor.moveToNext();
            }
            cursor.close();
        }

        //db.close();
        return routeList;
    }

    // Method to add an item to the database
    public void addItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(column_item_id, item.getItem_id());
        values.put(column_item_name, item.getItem_name());
        values.put(column_item_sku, item.getItem_sku());
        values.put(column_item_price, item.getItem_price());
        values.put(column_item_unit_id, item.getItem_unit_id());
        values.put(column_item_category_id, item.getItem_category_id());
        values.put(column_item_web_synced, item.getItem_web_synced());
        db.insert(items_table, null, values);
        //db.close();
    }

    // Method to retrieve all items from the database
    public List<Item> getAllItems() {
        List<Item> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(items_table, null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Item item = new Item();
                item.setItem_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_item_id)));
                item.setItem_name(cursor.getString(cursor.getColumnIndexOrThrow(column_item_name)));
                item.setItem_sku(cursor.getString(cursor.getColumnIndexOrThrow(column_item_sku)));
                item.setItem_price(cursor.getFloat(cursor.getColumnIndexOrThrow(column_item_price)));
                item.setItem_unit_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_item_unit_id)));
                item.setItem_category_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_item_category_id)));
                item.setItem_web_synced(cursor.getInt(cursor.getColumnIndexOrThrow(column_item_web_synced)));
                itemList.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }

        //db.close();
        return itemList;
    }

    // Method to retrieve an item by item_id from the database
    public Item getItemById(int itemId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(items_table, null, column_item_id + " = ?",
                new String[]{String.valueOf(itemId)}, null, null, null);

        Item item = null;
        if (cursor != null && cursor.moveToFirst()) {
            item = new Item();
            item.setItem_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_item_id)));
            item.setItem_name(cursor.getString(cursor.getColumnIndexOrThrow(column_item_name)));
            item.setItem_sku(cursor.getString(cursor.getColumnIndexOrThrow(column_item_sku)));
            item.setItem_price(cursor.getFloat(cursor.getColumnIndexOrThrow(column_item_price)));
            item.setItem_unit_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_item_unit_id)));
            item.setItem_category_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_item_category_id)));
            item.setItem_web_synced(cursor.getInt(cursor.getColumnIndexOrThrow(column_item_web_synced)));
            cursor.close();
        }

        //db.close();
        return item;
    }

    // Method to retrieve an item by item_sku from the database
    public Item getItemBySku(String itemSku) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(items_table, null, column_item_sku + " = ?",
                new String[]{itemSku}, null, null, null);

        Item item = null;
        if (cursor != null && cursor.moveToFirst()) {
            item = new Item();
            item.setItem_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_item_id)));
            item.setItem_name(cursor.getString(cursor.getColumnIndexOrThrow(column_item_name)));
            item.setItem_sku(cursor.getString(cursor.getColumnIndexOrThrow(column_item_sku)));
            item.setItem_price(cursor.getFloat(cursor.getColumnIndexOrThrow(column_item_price)));
            item.setItem_unit_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_item_unit_id)));
            item.setItem_category_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_item_category_id)));
            item.setItem_web_synced(cursor.getInt(cursor.getColumnIndexOrThrow(column_item_web_synced)));
            cursor.close();
        }

        //db.close();
        return item;
    }

    // Method to retrieve all items with a particular category_id from the database
    public List<Item> getItemsByCategoryId(int categoryId) {
        List<Item> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = column_item_category_id + " = ?";
        String[] selectionArgs = {String.valueOf(categoryId)};
        Cursor cursor = db.query(items_table, null, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Item item = new Item();
                item.setItem_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_item_id)));
                item.setItem_name(cursor.getString(cursor.getColumnIndexOrThrow(column_item_name)));
                item.setItem_sku(cursor.getString(cursor.getColumnIndexOrThrow(column_item_sku)));
                item.setItem_price(cursor.getFloat(cursor.getColumnIndexOrThrow(column_item_price)));
                item.setItem_unit_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_item_unit_id)));
                item.setItem_category_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_item_category_id)));
                item.setItem_web_synced(cursor.getInt(cursor.getColumnIndexOrThrow(column_item_web_synced)));
                itemList.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }

        //db.close();
        return itemList;
    }

    // Method to add a category to the database
    public void addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(column_category_id, category.getCategory_id());
        values.put(column_category_name, category.getCategory_name());
        values.put(column_category_sku, category.getCategory_sku());
        values.put(column_category_web_synced, category.getCategory_web_synced());
        db.insert(categories_table, null, values);
        ////db.close();
    }

    // Method to retrieve a category by category_id from the database
    public Category getCategoryById(int categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(categories_table, null, column_category_id + " = ?",
                new String[]{String.valueOf(categoryId)}, null, null, null);

        Category category = null;
        if (cursor != null && cursor.moveToFirst()) {
            category = new Category();
            category.setCategory_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_category_id)));
            category.setCategory_name(cursor.getString(cursor.getColumnIndexOrThrow(column_category_name)));
            category.setCategory_sku(cursor.getString(cursor.getColumnIndexOrThrow(column_category_sku)));
            category.setCategory_web_synced(cursor.getInt(cursor.getColumnIndexOrThrow(column_category_web_synced)));
            cursor.close();
        }

        ////db.close();
        return category;
    }

    // Method to retrieve a category by category_sku from the database
    public Category getCategoryBySku(String categorySku) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(categories_table, null, column_category_sku + " = ?",
                new String[]{categorySku}, null, null, null);

        Category category = null;
        if (cursor != null && cursor.moveToFirst()) {
            category = new Category();
            category.setCategory_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_category_id)));
            category.setCategory_name(cursor.getString(cursor.getColumnIndexOrThrow(column_category_name)));
            category.setCategory_sku(cursor.getString(cursor.getColumnIndexOrThrow(column_category_sku)));
            category.setCategory_web_synced(cursor.getInt(cursor.getColumnIndexOrThrow(column_category_web_synced)));
            cursor.close();
        }

        ////db.close();
        return category;
    }

    // Method to retrieve all categories from the database
    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(categories_table, null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Category category = new Category();
                category.setCategory_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_category_id)));
                category.setCategory_name(cursor.getString(cursor.getColumnIndexOrThrow(column_category_name)));
                category.setCategory_sku(cursor.getString(cursor.getColumnIndexOrThrow(column_category_sku)));
                category.setCategory_web_synced(cursor.getInt(cursor.getColumnIndexOrThrow(column_category_web_synced)));
                categoryList.add(category);
                cursor.moveToNext();
            }
            cursor.close();
        }

        ////db.close();
        return categoryList;
    }

    // Method to add a unit to the database
    public void addUnit(Unit unit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(column_unit_id, unit.getUnit_id());
        values.put(column_unit_name, unit.getUnit_name());
        values.put(column_unit_web_synced, unit.getUnit_web_synced());
        db.insert(units_table, null, values);
        //db.close();
    }

    // Method to retrieve a unit by unit_id from the database
    public Unit getUnitById(int unitId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(units_table, null, column_unit_id + " = ?",
                new String[]{String.valueOf(unitId)}, null, null, null);

        Unit unit = null;
        if (cursor != null && cursor.moveToFirst()) {
            unit = new Unit();
            unit.setUnit_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_unit_id)));
            unit.setUnit_name(cursor.getString(cursor.getColumnIndexOrThrow(column_unit_name)));
            unit.setUnit_web_synced(cursor.getInt(cursor.getColumnIndexOrThrow(column_unit_web_synced)));
            cursor.close();
        }

        //db.close();
        return unit;
    }

    // Method to retrieve all units from the database
    public List<Unit> getAllUnits() {
        List<Unit> unitList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(units_table, null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Unit unit = new Unit();
                unit.setUnit_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_unit_id)));
                unit.setUnit_name(cursor.getString(cursor.getColumnIndexOrThrow(column_unit_name)));
                unit.setUnit_web_synced(cursor.getInt(cursor.getColumnIndexOrThrow(column_unit_web_synced)));
                unitList.add(unit);
                cursor.moveToNext();
            }
            cursor.close();
        }

        //db.close();
        return unitList;
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

    public int numberOfRows(String table_name){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, table_name);
        return numRows;
    }
}
