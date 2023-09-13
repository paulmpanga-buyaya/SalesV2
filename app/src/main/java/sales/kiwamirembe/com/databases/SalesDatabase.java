package sales.kiwamirembe.com.databases;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import sales.kiwamirembe.com.classes.Cart;
import sales.kiwamirembe.com.classes.CartItem;
import sales.kiwamirembe.com.classes.Customer;
import sales.kiwamirembe.com.classes.Inventory;
import sales.kiwamirembe.com.classes.Sale;
import sales.kiwamirembe.com.classes.SaleItem;

public class SalesDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "salesDB.db";
    public static final int DATABASE_VERSION = 1;

    public static final String customers_table = "customers";
    public static final String column_customer_id = "customer_id";
    public static final String column_customer_business_name = "customer_business_name";
    public static final String column_customer_name = "customer_name";
    public static final String column_customer_email = "customer_email";
    public static final String column_customer_phone = "customer_phone";
    public static final String column_customer_web_synced = "customer_web_synced";

 /*   public static final String inventory_table = "inventory";
    public static final String column_inventory_id = "inventory_id";
    public static final String column_inventory_item_id = "inventory_item_id";
    public static final String column_inventory_item_name = "inventory_item_name";
    public static final String column_inventory_item_sku = "inventory_item_sku";
    public static final String column_inventory_item_price = "inventory_item_price";
    public static final String column_inventory_item_stock = "inventory_item_stock";
    public static final String column_inventory_item_web_synced = "inventory_item_web_synced";*/

    public static final String carts_table = "carts";
    public static final String column_cart_id = "cart_id";
    public static final String column_cart_customer_id = "cart_customer_id";
    public static final String column_cart_number = "cart_number";
    public static final String column_cart_user_id = "cart_user_id";

    public static final String cart_items_table = "cart_items";
    public static final String column_cart_item_id = "cart_item_id";
    public static final String column_cart_item_sku = "cart_item_sku";
    public static final String column_cart_item_name = "cart_item_name";
    public static final String column_cart_item_price = "cart_item_price";
    public static final String column_cart_item_quantity = "cart_item_quantity";
    public static final String column_cart_item_total = "cart_item_total";
    public static final String column_cart_item_discount = "cart_item_discount";

    public static final String sales_table = "sales";
    public static final String column_sale_id = "sale_id";
    public static final String column_sale_number = "sale_number";
    public static final String column_sale_customer_id = "sale_customer_id";
    public static final String column_sale_total = "sale_total";
    public static final String column_sale_date = "sale_date";
    public static final String column_sale_payment_status = "sale_payment_status";
    public static final String column_sale_web_synced = "sale_web_synced";

    public static final String sale_items_table = "sale_items";
    public static final String column_sale_item_id = "sale_item_id";
    public static final String column_sale_item_sale_number = "sale_item_sale_number";
    public static final String column_sale_item_name = "sale_item_name";
    public static final String column_sale_item_sku = "sale_item_sku";
    public static final String column_sale_item_price = "sale_item_price";
    public static final String column_sale_item_quantity = "sale_item_quantity";
    public static final String column_sale_item_total = "sale_item_total";
    public static final String column_sale_item_web_synced = "sale_item_web_synced";

    public static final String create_sale_items_table = "create table sale_items (sale_item_id INTEGER PRIMARY KEY, sale_item_sale_number text, sale_item_name text, " +
            "sale_item_sku text, sale_item_price real, sale_item_quantity INTEGER, sale_item_total real, sale_item_web_synced INTEGER)";
    public static final String drop_sale_items_table = "DROP TABLE IF EXISTS sale_items";

    public static final String create_sales_table = "create table sales (sale_id INTEGER PRIMARY KEY, sale_number text, sale_customer_id INTEGER, sale_total real, sale_date real, sale_payment_status INTEGER, sale_web_synced INTEGER)";
    public static final String drop_sales_table = "DROP TABLE IF EXISTS sales";

    public static final String create_cart_items_table = "create table cart_items (cart_item_id INTEGER PRIMARY KEY, cart_item_sku text, cart_item_name text, cart_item_price INTEGER, cart_item_quantity INTEGER, cart_item_total INTEGER, cart_item_discount INTEGER)";
    public static final String drop_cart_items_table = "DROP TABLE IF EXISTS cart_items";

    public static final String create_carts_table = "create table carts (cart_id INTEGER PRIMARY KEY, cart_customer_id INTEGER, cart_number text, cart_user_id INTEGER)";
    public static final String drop_carts_table = "DROP TABLE IF EXISTS carts";

   /* public static final String create_inventory_table = "create table inventory (inventory_id INTEGER PRIMARY KEY, inventory_item_id INTEGER, inventory_item_name text, inventory_item_sku text, inventory_item_price INTEGER, inventory_item_stock INTEGER, inventory_item_web_synced INTEGER)";
    public static final String drop_inventory_table = "DROP TABLE IF EXISTS inventory";*/

    public static final String create_customers_table = "create table customers (customer_id INTEGER PRIMARY KEY, customer_business_name text, customer_name text, customer_email text, customer_phone text, customer_web_synced int)";
    public static final String drop_customers_table = "DROP TABLE IF EXISTS customers";

    public SalesDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_customers_table);
       // db.execSQL(create_inventory_table);
        db.execSQL(create_carts_table);
        db.execSQL(create_cart_items_table);
        db.execSQL(create_sales_table);
        db.execSQL(create_sale_items_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(drop_customers_table);
        //db.execSQL(drop_inventory_table);
        db.execSQL(drop_carts_table);
        db.execSQL(drop_cart_items_table);
        db.execSQL(drop_sales_table);
        db.execSQL(drop_sale_items_table);
        onCreate(db);
    }

    public boolean addSaleItem(SaleItem saleItem) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = createContentValuesFromSaleItem(saleItem);
        long result = db.insert(sale_items_table, null, contentValues);
        return result != -1;
    }

    private ContentValues createContentValuesFromSaleItem(SaleItem saleItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(column_sale_item_sale_number, saleItem.getSale_item_sale_number());
        contentValues.put(column_sale_item_name, saleItem.getSale_item_name());
        contentValues.put(column_sale_item_sku, saleItem.getSale_item_sku());
        contentValues.put(column_sale_item_price, saleItem.getSale_item_price());
        contentValues.put(column_sale_item_quantity, saleItem.getSale_item_quantity());
        contentValues.put(column_sale_item_total, saleItem.getSale_item_total());
        contentValues.put(column_sale_item_web_synced, saleItem.getSale_item_web_synced());
        return contentValues;
    }

    public List<SaleItem> getSaleItemsBySaleNumber(String saleNumber) {
        List<SaleItem> saleItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = column_sale_item_sale_number + " = ?";
        String[] selectionArgs = { saleNumber };
        Cursor cursor = db.query(sale_items_table, null, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                SaleItem saleItem = createSaleItemFromCursor(cursor);
                saleItemList.add(saleItem);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return saleItemList;
    }

    private SaleItem createSaleItemFromCursor(Cursor cursor) {
        SaleItem saleItem = new SaleItem();
        saleItem.setSale_item_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_sale_item_id)));
        saleItem.setSale_item_sale_number(cursor.getString(cursor.getColumnIndexOrThrow(column_sale_item_sale_number)));
        saleItem.setSale_item_name(cursor.getString(cursor.getColumnIndexOrThrow(column_sale_item_name)));
        saleItem.setSale_item_sku(cursor.getString(cursor.getColumnIndexOrThrow(column_sale_item_sku)));
        saleItem.setSale_item_price(cursor.getFloat(cursor.getColumnIndexOrThrow(column_sale_item_price)));
        saleItem.setSale_item_quantity(cursor.getInt(cursor.getColumnIndexOrThrow(column_sale_item_quantity)));
        saleItem.setSale_item_total(cursor.getFloat(cursor.getColumnIndexOrThrow(column_sale_item_total)));
        saleItem.setSale_item_web_synced(cursor.getInt(cursor.getColumnIndexOrThrow(column_sale_item_web_synced)));
        return saleItem;
    }

    public boolean addSale(Sale sale) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = createContentValuesFromSale(sale);
        long result = db.insert(sales_table, null, contentValues);
        return result != -1;
    }

    private ContentValues createContentValuesFromSale(Sale sale) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(column_sale_number, sale.getSale_number());
        contentValues.put(column_sale_customer_id, sale.getSale_customer_id());
        contentValues.put(column_sale_total, sale.getSale_total());
        contentValues.put(column_sale_date, sale.getSale_date());
        contentValues.put(column_sale_payment_status, sale.getSale_payment_status());
        contentValues.put(column_sale_web_synced, sale.getSale_web_synced());
        return contentValues;
    }

    public List<Sale> getAllSales1() {
        List<Sale> saleList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(sales_table, null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Sale sale = createSaleFromCursor(cursor);
                saleList.add(sale);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return saleList;
    }

    public List<Sale> getAllSales() {
        List<Sale> saleList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(sales_table, null, null, null, null, null, "sale_date DESC");

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Sale sale = createSaleFromCursor(cursor);
                saleList.add(sale);
                cursor.moveToNext();
            }
            cursor.close();
        }

        db.close();
        return saleList;
    }


    public boolean doesSaleExist(String saleNumber) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = SalesDatabase.column_sale_number + " = ?";
        String[] selectionArgs = {saleNumber};

        Cursor cursor = db.query(SalesDatabase.sales_table, null, selection, selectionArgs, null, null, null);

        boolean exists = cursor != null && cursor.getCount() > 0;

        if (cursor != null) {
            cursor.close();
        }

        return exists;
    }

    public int getSaleItemCountBySaleNumber(String saleNumber) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = SalesDatabase.column_sale_item_sale_number + " = ?";
        String[] selectionArgs = {saleNumber};

        Cursor cursor = db.query(SalesDatabase.sale_items_table, null, selection, selectionArgs, null, null, null);

        int itemCount = cursor != null ? cursor.getCount() : 0;

        if (cursor != null) {
            cursor.close();
        }

        return itemCount;
    }

    public Sale getSaleByNumber(String saleNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Sale sale = null;

        Cursor cursor = db.query(sales_table,
                new String[]{
                        "sale_id", "sale_number", "sale_customer_id",
                        "sale_total", "sale_date", "sale_payment_status",
                        "sale_web_synced"
                },
                "sale_number = ?", new String[]{saleNumber},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int saleId = cursor.getInt(cursor.getColumnIndexOrThrow("sale_id"));
            int customerId = cursor.getInt(cursor.getColumnIndexOrThrow("sale_customer_id"));
            float total = cursor.getFloat(cursor.getColumnIndexOrThrow("sale_total"));
            long date = cursor.getLong(cursor.getColumnIndexOrThrow("sale_date"));
            int paymentStatus = cursor.getInt(cursor.getColumnIndexOrThrow("sale_payment_status"));
            int webSynced = cursor.getInt(cursor.getColumnIndexOrThrow("sale_web_synced"));

            sale = new Sale(saleId, saleNumber, customerId, total, date, paymentStatus, webSynced);

            cursor.close();
        }

        db.close();
        return sale;
    }



    private Sale createSaleFromCursor(Cursor cursor) {
        Sale sale = new Sale();
        sale.setSale_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_sale_id)));
        sale.setSale_number(cursor.getString(cursor.getColumnIndexOrThrow(column_sale_number)));
        sale.setSale_customer_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_sale_customer_id)));
        sale.setSale_total(cursor.getFloat(cursor.getColumnIndexOrThrow(column_sale_total)));
        sale.setSale_date(cursor.getLong(cursor.getColumnIndexOrThrow(column_sale_date)));
        sale.setSale_payment_status(cursor.getInt(cursor.getColumnIndexOrThrow(column_sale_payment_status)));
        sale.setSale_web_synced(cursor.getInt(cursor.getColumnIndexOrThrow(column_sale_web_synced)));
        return sale;
    }

    public boolean addCartItem(CartItem cartItem) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = createContentValuesFromCartItem(cartItem);
        long result = db.insert(cart_items_table, null, contentValues);
        return result != -1;
    }

    private ContentValues createContentValuesFromCartItem(CartItem cartItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(column_cart_item_sku, cartItem.getCart_item_sku());
        contentValues.put(column_cart_item_name, cartItem.getCart_item_name());
        contentValues.put(column_cart_item_price, cartItem.getCart_item_price());
        contentValues.put(column_cart_item_quantity, cartItem.getCart_item_quantity());
        contentValues.put(column_cart_item_total, cartItem.getCart_item_total());
        contentValues.put(column_cart_item_discount, cartItem.getCart_item_discount());
        return contentValues;
    }

    public CartItem getCartItemBySku(String sku) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(cart_items_table, null, column_cart_item_sku + " = ?",
                new String[]{sku}, null, null, null);

        CartItem cartItem = null;
        if (cursor != null && cursor.moveToFirst()) {
            cartItem = new CartItem();
            cartItem.setCart_item_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_cart_item_id)));
            cartItem.setCart_item_sku(cursor.getString(cursor.getColumnIndexOrThrow(column_cart_item_sku)));
            cartItem.setCart_item_name(cursor.getString(cursor.getColumnIndexOrThrow(column_cart_item_name)));
            cartItem.setCart_item_price(cursor.getFloat(cursor.getColumnIndexOrThrow(column_cart_item_price)));
            cartItem.setCart_item_quantity(cursor.getInt(cursor.getColumnIndexOrThrow(column_cart_item_quantity)));
            cartItem.setCart_item_total(cursor.getFloat(cursor.getColumnIndexOrThrow(column_cart_item_total)));
            cartItem.setCart_item_discount(cursor.getFloat(cursor.getColumnIndexOrThrow(column_cart_item_discount)));
            cursor.close();
        }

        return cartItem;
    }

    public boolean updateCartItemQuantityBySku(String sku, int adjustQuant, int adjustmentType) {
        SQLiteDatabase db = this.getWritableDatabase();

        int newQuantity;
        if (adjustmentType == -1) {
            newQuantity = getCartItemQuantityBySku(sku) - adjustQuant;
            if (newQuantity < 0) {
                newQuantity = 0;
            }
        } else if (adjustmentType == 1) {
            newQuantity = getCartItemQuantityBySku(sku) + adjustQuant;
        } else {
           // db.close();
            return false; // Invalid adjustment type
        }

        float itemPrice = getCartItemPriceBySku(sku); // Retrieve item price
        float newTotal = itemPrice * newQuantity; // Calculate new total

        ContentValues values = new ContentValues();
        values.put(column_cart_item_quantity, newQuantity);
        values.put(column_cart_item_total, newTotal); // Update total

        int rowsAffected = db.update(cart_items_table, values, column_cart_item_sku + " = ?",
                new String[]{sku});

       // db.close();
        return rowsAffected > 0;
    }

    public float getCartItemPriceBySku(String sku) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {column_cart_item_price};
        String selection = column_cart_item_sku + " = ?";
        String[] selectionArgs = {sku};
        Cursor cursor = db.query(cart_items_table, columns, selection, selectionArgs, null, null, null);

        float price = 0; // Default value if not found

        if (cursor != null && cursor.moveToFirst()) {
            price = cursor.getFloat(cursor.getColumnIndexOrThrow(column_cart_item_price));
            cursor.close();
        }

       // db.close();
        return price;
    }

    public float getTotalCartItemsSum() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {column_cart_item_total};
        Cursor cursor = db.query(cart_items_table, columns, null, null, null, null, null);

        float sum = 0;

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                sum += cursor.getFloat(cursor.getColumnIndexOrThrow(column_cart_item_total));
                cursor.moveToNext();
            }
            cursor.close();
        }

       // db.close();
        return sum;
    }

    @SuppressLint("Range")
    public float getTotal(){
        float sum = 0435;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT SUM(" + SalesDatabase.column_cart_item_total + ") as saleItemTotal FROM " + SalesDatabase.cart_items_table, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            sum = res.getFloat(res.getColumnIndex("cart_item_total"));
            res.moveToNext();
        }
        return sum;
    }

    public boolean clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(cart_items_table, null, null); // Delete all rows from cart_items_table
            db.delete(carts_table, null, null); // Delete all rows from cart_table
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            // db.close(); // It's not recommended to close the database here
        }
    }

    public boolean doesCartItemExist(String sku) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {column_cart_item_id};
        String selection = column_cart_item_sku + " = ?";
        String[] selectionArgs = {sku};
        Cursor cursor = db.query(cart_items_table, columns, selection, selectionArgs, null, null, null);

        boolean exists = cursor != null && cursor.getCount() > 0;

        if (cursor != null) {
            cursor.close();
        }

       // db.close();
        return exists;
    }

    public int getCartItemQuantityBySku(String sku) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(cart_items_table, new String[]{column_cart_item_quantity},
                column_cart_item_sku + " = ?", new String[]{sku}, null, null, null);

        int quantity = 0;
        if (cursor != null && cursor.moveToFirst()) {
            quantity = cursor.getInt(cursor.getColumnIndexOrThrow(column_cart_item_quantity));
            cursor.close();
        }

        return quantity;
    }

    public boolean updateCartItemQuantityAndTotalBySku(String sku, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Ensure that the new quantity is not negative
        if (newQuantity < 0) {
            newQuantity = 0;
        }

        float itemPrice = getCartItemPriceBySku(sku); // Retrieve item price
        float newTotal = itemPrice * newQuantity; // Calculate new total

        ContentValues values = new ContentValues();
        values.put(column_cart_item_quantity, newQuantity);
        values.put(column_cart_item_total, newTotal); // Update both quantity and total

        int rowsAffected = db.update(cart_items_table, values, column_cart_item_sku + " = ?",
                new String[]{sku});

        return rowsAffected > 0;
    }

    public boolean adjustCartItemPriceBySku(String sku, float newPrice) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(column_cart_item_price, newPrice);

        int rowsAffected = db.update(cart_items_table, values, column_cart_item_sku + " = ?",
                new String[]{sku});

       // db.close();
        return rowsAffected > 0;
    }

    public List<CartItem> getAllCartItems() {
        List<CartItem> cartItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(cart_items_table, null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                CartItem cartItem = new CartItem();
                cartItem.setCart_item_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_cart_item_id)));
                cartItem.setCart_item_sku(cursor.getString(cursor.getColumnIndexOrThrow(column_cart_item_sku)));
                cartItem.setCart_item_name(cursor.getString(cursor.getColumnIndexOrThrow(column_cart_item_name)));
                cartItem.setCart_item_price(cursor.getFloat(cursor.getColumnIndexOrThrow(column_cart_item_price)));
                cartItem.setCart_item_quantity(cursor.getInt(cursor.getColumnIndexOrThrow(column_cart_item_quantity)));
                cartItem.setCart_item_total(cursor.getFloat(cursor.getColumnIndexOrThrow(column_cart_item_total)));
                cartItem.setCart_item_discount(cursor.getFloat(cursor.getColumnIndexOrThrow(column_cart_item_discount)));
                cartItemList.add(cartItem);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return cartItemList;
    }


    public boolean addCart(Cart cart) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = createContentValuesFromCart(cart);
        long result = db.insert(carts_table, null, contentValues);
        return result != -1;
    }

    private ContentValues createContentValuesFromCart(Cart cart) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(column_cart_customer_id, cart.getCart_customer_id());
        contentValues.put(column_cart_number, cart.getCart_number());
        contentValues.put(column_cart_user_id, cart.getCart_user_id());
        return contentValues;
    }

    public Cart getCartById(int cartId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(carts_table, null, column_cart_id + " = ?",
                new String[]{String.valueOf(cartId)}, null, null, null);

        Cart cart = null;
        if (cursor != null && cursor.moveToFirst()) {
            cart = new Cart();
            cart.setCart_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_cart_id)));
            cart.setCart_customer_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_cart_customer_id)));
            cart.setCart_number(cursor.getString(cursor.getColumnIndexOrThrow(column_cart_number)));
            cart.setCart_user_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_cart_user_id)));
            cursor.close();
        }

        return cart;
    }

    public int getCustomerIdForCart(int cartId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = { column_cart_customer_id };
        String selection = column_cart_id + " = ?";
        String[] selectionArgs = { String.valueOf(cartId) };

        Cursor cursor = db.query(carts_table, columns, selection, selectionArgs, null, null, null);

        int customerId = -1; // Default value indicating no customer found
        if (cursor != null && cursor.moveToFirst()) {
            customerId = cursor.getInt(cursor.getColumnIndexOrThrow(column_cart_customer_id));
            cursor.close();
        }

        return customerId;
    }

    public boolean updateCartCustomerId(int cartId, int newCustomerId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(column_cart_customer_id, newCustomerId);

        String whereClause = column_cart_id + " = ?";
        String[] whereArgs = { String.valueOf(cartId) };

        int rowsAffected = db.update(carts_table, values, whereClause, whereArgs);
       // db.close();

        return rowsAffected > 0;
    }
    /*
    SalesDatabase salesDatabase = new SalesDatabase(context);
int cartId = 1; // Replace with the desired cart ID
int newCustomerId = 123; // Replace with the new customer ID
boolean updateSuccessful = salesDatabase.updateCartCustomerId(cartId, newCustomerId);
if (updateSuccessful) {
    // The cart's customer ID was updated successfully
} else {
    // The update was not successful
}

     */


   /* public boolean addInventoryItem(Inventory inventoryItem) {
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
    }*/

    public boolean addCustomer(Customer customer) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = createContentValuesFromCustomer(customer);
        long result = db.insert(customers_table, null, contentValues);
        return result != -1;
    }

    public boolean addCustomerWithId(Customer customer) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = createContentValuesFromCustomerWithId(customer);
        long result = db.insert(customers_table, null, contentValues);
        return result != -1;
    }

    private ContentValues createContentValuesFromCustomerWithId(Customer customer) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(column_customer_id, customer.getCustomer_id());
        contentValues.put(column_customer_business_name, customer.getCustomer_name());
        contentValues.put(column_customer_name, customer.getCustomer_name());
        contentValues.put(column_customer_email, customer.getCustomer_email());
        contentValues.put(column_customer_phone, customer.getCustomer_phone());
        contentValues.put(column_customer_web_synced, customer.getCustomer_web_synced());
        return contentValues;
    }

    private ContentValues createContentValuesFromCustomer(Customer customer) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(column_customer_business_name, customer.getCustomer_name());
        contentValues.put(column_customer_name, customer.getCustomer_name());
        contentValues.put(column_customer_email, customer.getCustomer_email());
        contentValues.put(column_customer_phone, customer.getCustomer_phone());
        contentValues.put(column_customer_web_synced, customer.getCustomer_web_synced());
        return contentValues;
    }

    public String getCustomerNameByCartId(int cartId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(carts_table, new String[]{column_cart_customer_id}, column_cart_id + " = ?",
                new String[]{String.valueOf(cartId)}, null, null, null);

        int customerId = -1;
        if (cursor != null && cursor.moveToFirst()) {
            customerId = cursor.getInt(cursor.getColumnIndexOrThrow(column_cart_customer_id));
            cursor.close();
        }

        if (customerId != -1) {
            Customer customer = getCustomerById(customerId);
            if (customer != null) {
                return customer.getCustomer_name();
            }
        }

        return null;
    }


    public Customer getCustomerById(int customerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(customers_table, null, column_customer_id + " = ?",
                new String[]{String.valueOf(customerId)}, null, null, null);

        Customer customer = null;
        if (cursor != null && cursor.moveToFirst()) {
            customer = new Customer();
            customer.setCustomer_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_customer_id)));
            customer.setCustomer_business_name(cursor.getString(cursor.getColumnIndexOrThrow(column_customer_business_name)));
            customer.setCustomer_name(cursor.getString(cursor.getColumnIndexOrThrow(column_customer_name)));
            customer.setCustomer_email(cursor.getString(cursor.getColumnIndexOrThrow(column_customer_email)));
            customer.setCustomer_phone(cursor.getString(cursor.getColumnIndexOrThrow(column_customer_phone)));
            customer.setCustomer_web_synced(cursor.getInt(cursor.getColumnIndexOrThrow(column_customer_web_synced)));
            cursor.close();
        }

        //db.close();
        return customer;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customerList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(customers_table, null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Customer customer = new Customer();
                customer.setCustomer_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_customer_id)));
                customer.setCustomer_business_name(cursor.getString(cursor.getColumnIndexOrThrow(column_customer_business_name)));
                customer.setCustomer_name(cursor.getString(cursor.getColumnIndexOrThrow(column_customer_name)));
                customer.setCustomer_email(cursor.getString(cursor.getColumnIndexOrThrow(column_customer_email)));
                customer.setCustomer_phone(cursor.getString(cursor.getColumnIndexOrThrow(column_customer_phone)));
                customer.setCustomer_web_synced(cursor.getInt(cursor.getColumnIndexOrThrow(column_customer_web_synced)));
                customerList.add(customer);
                cursor.moveToNext();
            }
            cursor.close();
        }

        //db.close();
        return customerList;
    }

    public List<Customer> getUnsyncedCustomers() {
        List<Customer> unsyncedCustomers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the selection criteria
        String selection = column_customer_web_synced + " = ?";
        String[] selectionArgs = {"0"}; // 0 represents unsynced

        Cursor cursor = db.query(customers_table, null, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Customer customer = new Customer();
                customer.setCustomer_id(cursor.getInt(cursor.getColumnIndexOrThrow(column_customer_id)));
                customer.setCustomer_business_name(cursor.getString(cursor.getColumnIndexOrThrow(column_customer_business_name)));
                customer.setCustomer_name(cursor.getString(cursor.getColumnIndexOrThrow(column_customer_name)));
                customer.setCustomer_email(cursor.getString(cursor.getColumnIndexOrThrow(column_customer_email)));
                customer.setCustomer_phone(cursor.getString(cursor.getColumnIndexOrThrow(column_customer_phone)));
                customer.setCustomer_web_synced(cursor.getInt(cursor.getColumnIndexOrThrow(column_customer_web_synced)));
                unsyncedCustomers.add(customer);
                cursor.moveToNext();
            }
            cursor.close();
        }

        //db.close();
        return unsyncedCustomers;
    }

    public boolean updateCustomerSyncStatus(int customerID, int syncStatus) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(column_customer_web_synced, syncStatus);

        int rowsAffected = db.update(customers_table, values, column_customer_id + " = ?",
                new String[]{String.valueOf(customerID)});

       // db.close();
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
}
