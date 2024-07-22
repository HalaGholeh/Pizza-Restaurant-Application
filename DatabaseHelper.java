package com.example.a1201418_1200435_project;

import static android.service.controls.ControlsProviderService.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "PizzaHub.db";
    private static final int DATABASE_VERSION = 1;

    // Define table name and column names
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE_NUMBER = "phone_number";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_IS_ADMIN = "is_admin";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PROFILE_PICTURE = "profile_picture";

    public static final String TABLE_PIZZA = "pizza";
    public static final String COLUMN_PIZZA_ID = "id";
    public static final String COLUMN_PIZZA_NAME = "name";
    public static final String COLUMN_PIZZA_CATEGORY = "category";
    public static final String COLUMN_PIZZA_PRICE = "price";
    public static final String COLUMN_PIZZA_SIZE = "size";

    public static final String TABLE_FAVORITES = "favorites";
    public static final String COLUMN_FAVORITE_ID = "id";
    public static final String COLUMN_FAVORITE_USER_EMAIL = "user_email";
    public static final String COLUMN_FAVORITE_PIZZA_ID = "pizza_id";

    public static final String TABLE_ORDERS = "orders";
    public static final String COLUMN_ORDER_ID = "order_id";
    public static final String COLUMN_ORDER_USER_EMAIL = "user_email";
    public static final String COLUMN_ORDER_PIZZA_ID = "pizza_id";
    public static final String COLUMN_ORDER_QUANTITY = "quantity";
    public static final String COLUMN_EXTRA_SAUCE = "extra_sauce";
    public static final String COLUMN_EXTRA_CHEESE = "extra_cheese";
    public static final String COLUMN_MIX_WITH_BEEF = "mix_with_beef";
    public static final String COLUMN_OPTIONAL = "optional";
    public static final String COLUMN_ORDER_DATE = "order_date";
    public static final String COLUMN_ORDER_TOTAL_PRICE = "order_price";

    public static final String TABLE_OFFERS = "offers";
    public static final String COLUMN_OFFER_ID = "offer_id";
    public static final String COLUMN_OFFER_PERIOD = "offer_period";
    public static final String COLUMN_OFFER_START_DATE = "offer_start_date";

    public static final String COLUMN_OFFER_DISCOUNT = "offer_discount";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_EMAIL + " TEXT PRIMARY KEY UNIQUE NOT NULL,"
            + COLUMN_PHONE_NUMBER + " TEXT NOT NULL CHECK(LENGTH(" + COLUMN_PHONE_NUMBER + ") = 10),"
            + COLUMN_FIRST_NAME + " TEXT CHECK(LENGTH(" + COLUMN_FIRST_NAME + ") >= 3),"
            + COLUMN_LAST_NAME + " TEXT CHECK(LENGTH(" + COLUMN_LAST_NAME + ") >= 3),"
            + COLUMN_GENDER + " TEXT,"
            + COLUMN_IS_ADMIN + " INTEGER NOT NULL CHECK(" + COLUMN_IS_ADMIN + " IN (0, 1)),"
            + COLUMN_PASSWORD + " TEXT CHECK(LENGTH(" + COLUMN_PASSWORD + ") >= 8),"
            + COLUMN_PROFILE_PICTURE + " TEXT)";

    private static final String TABLE_PIZZA_CREATE =
            "CREATE TABLE " + TABLE_PIZZA + " (" +
                    COLUMN_PIZZA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PIZZA_NAME + " TEXT, " +
                    COLUMN_PIZZA_CATEGORY + " TEXT, " +
                    COLUMN_PIZZA_PRICE + " REAL, " +
                    COLUMN_PIZZA_SIZE + " TEXT" +
                    ");";

    private static final String TABLE_FAVORITES_CREATE =
            "CREATE TABLE " + TABLE_FAVORITES + " (" +
                    COLUMN_FAVORITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FAVORITE_USER_EMAIL + " TEXT, " +
                    COLUMN_FAVORITE_PIZZA_ID + " INTEGER, " +
                    "FOREIGN KEY(" + COLUMN_FAVORITE_USER_EMAIL + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_EMAIL + "), " +
                    "FOREIGN KEY(" + COLUMN_FAVORITE_PIZZA_ID + ") REFERENCES " + TABLE_PIZZA + "(" + COLUMN_PIZZA_ID + ")" +
                    ");";

    private static final String TABLE_ORDERS_CREATE =
            "CREATE TABLE " + TABLE_ORDERS + " (" +
                    COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ORDER_USER_EMAIL + " TEXT, " +
                    COLUMN_ORDER_PIZZA_ID + " INTEGER, " +
                    COLUMN_ORDER_QUANTITY + " INTEGER, " +
                    COLUMN_EXTRA_SAUCE + " INTEGER, " +
                    COLUMN_EXTRA_CHEESE + " INTEGER, " +
                    COLUMN_MIX_WITH_BEEF + " INTEGER, " +
                    COLUMN_OPTIONAL + " TEXT, " +
                    COLUMN_ORDER_DATE + " TEXT, " +
                    COLUMN_ORDER_TOTAL_PRICE + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_ORDER_USER_EMAIL + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_EMAIL + "), " +
                    "FOREIGN KEY(" + COLUMN_ORDER_PIZZA_ID + ") REFERENCES " + TABLE_PIZZA + "(" + COLUMN_PIZZA_ID + ")" +
                    ");";

    private static final String TABLE_OFFER_CREATE =
            "CREATE TABLE " + TABLE_OFFERS + " (" +
                    COLUMN_OFFER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PIZZA_ID + " INTEGER, " +
                    COLUMN_OFFER_PERIOD + " INTEGER, " +
                    COLUMN_OFFER_START_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    COLUMN_OFFER_DISCOUNT + " REAL, " +
                    "FOREIGN KEY (" + COLUMN_PIZZA_ID + ") REFERENCES " + TABLE_PIZZA + "(" + COLUMN_PIZZA_ID + ")" +
                    ");";




    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(TABLE_PIZZA_CREATE);
        db.execSQL(TABLE_FAVORITES_CREATE);
        db.execSQL(TABLE_ORDERS_CREATE);
        db.execSQL(TABLE_OFFER_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertPizzaTypes(ArrayList<String> pizzaTypes, ArrayList<String> pizzaCategories) {
        SQLiteDatabase db = this.getWritableDatabase();
        Random random = new Random();

        for (int i = 0; i < pizzaTypes.size(); i++) {
            String type = pizzaTypes.get(i);
            String category = pizzaCategories.get(i);

            double smallPrice = 5.0 + (15.0 - 5.0) * random.nextDouble();
            double mediumPrice = 10.0 + (20.0 - 10.0) * random.nextDouble();
            double largePrice = 15.0 + (25.0 - 15.0) * random.nextDouble();

            int smallpriceInt = (int) smallPrice;
            int mediumPriceInt = (int) mediumPrice;
            int largePriceInt = (int) largePrice;

            double finalPriceSmall = smallpriceInt + 0.99;
            double finalPriceMedium = mediumPriceInt + 0.99;
            double finalPriceLarge = largePriceInt + 0.99;

            if (!pizzaExists(db, type, "Small")) {
                addPizzaToDb(db, type, category, finalPriceSmall, "Small");
            }
            if (!pizzaExists(db, type, "Medium")) {
                addPizzaToDb(db, type, category, finalPriceMedium, "Medium");
            }
            if (!pizzaExists(db, type, "Large")) {
                addPizzaToDb(db, type, category, finalPriceLarge, "Large");
            }
        }
        db.close();
    }

    public ArrayList<Pizza> getPizzaDetailsByName(String pizzaName) {
        ArrayList<Pizza> pizzaDetailsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PIZZA + " WHERE " + COLUMN_PIZZA_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{pizzaName});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PIZZA_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PIZZA_NAME));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PIZZA_CATEGORY));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PIZZA_PRICE));
                String size = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PIZZA_SIZE));

                Pizza pizza = new Pizza(id, name, category, price, size);
                pizzaDetailsList.add(pizza);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return pizzaDetailsList;
    }

    public boolean addSpecialOffer(int pizzaId, double discount, int period){
        SQLiteDatabase db= getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put(COLUMN_PIZZA_ID, pizzaId);
            values.put(COLUMN_OFFER_PERIOD, period);
            values.put(COLUMN_OFFER_DISCOUNT, discount);
            db.insert(TABLE_OFFERS, null, values);
            db.close();

            return true;
        }catch(Exception e){
            return false;
        }
    }

    public ArrayList<Offer> getAllSpecialOffers() {
        ArrayList<Offer> offers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + TABLE_OFFERS;
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_OFFER_ID));
                    int pizzaId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PIZZA_ID));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OFFER_START_DATE));
                    int period = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_OFFER_PERIOD));
                    double discount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_OFFER_DISCOUNT));
                    offers.add(new Offer(id, date, period, discount, pizzaId));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return offers;
    }



    public ArrayList<Pizza> getDistinctPizzaTypes() {
        ArrayList<Pizza> pizzaTypes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT DISTINCT " + COLUMN_PIZZA_NAME + " FROM " + TABLE_PIZZA;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String pizzaType = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PIZZA_NAME));
                Pizza pizza = new Pizza(pizzaType, null, 0.0, null);
                pizzaTypes.add(pizza);
            }
            cursor.close();
        }

        db.close();
        return pizzaTypes;
    }

    private boolean pizzaExists(SQLiteDatabase db, String name, String size) {
        String query = "SELECT COUNT(*) FROM " + TABLE_PIZZA + " WHERE " + COLUMN_PIZZA_NAME + " = ? AND " + COLUMN_PIZZA_SIZE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{name, size});
        if (cursor != null && cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            cursor.close();
            return count > 0;
        }
        return false;
    }

    private void addPizzaToDb(SQLiteDatabase db, String name, String category, double price, String size) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PIZZA_NAME, name);
        values.put(COLUMN_PIZZA_CATEGORY, category);
        values.put(COLUMN_PIZZA_PRICE, price);
        values.put(COLUMN_PIZZA_SIZE, size);
        db.insert(TABLE_PIZZA, null, values);
    }

    public ArrayList<Pizza> getAllPizzas() {
        ArrayList<Pizza> pizzas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_PIZZA;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PIZZA_NAME));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PIZZA_CATEGORY));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PIZZA_PRICE));
                String size = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PIZZA_SIZE));
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PIZZA_ID));
                Pizza pizza = new Pizza(name, category, price, size);
                pizzas.add(pizza);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        Log.d("DatabaseHelper", "Number of pizzas retrieved: " + pizzas.size());
        return pizzas;
    }

    public void addToFavorites(String userEmail, Pizza pizza) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FAVORITE_USER_EMAIL, userEmail);
        values.put(COLUMN_FAVORITE_PIZZA_ID, pizza.getId());
        db.insert(TABLE_FAVORITES, null, values);
        db.close();
    }

    public boolean insertUser(User user) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_EMAIL, user.getEmail());
        contentValues.put(COLUMN_PHONE_NUMBER, user.getPhoneNumber());
        contentValues.put(COLUMN_FIRST_NAME, user.getFirstName());
        contentValues.put(COLUMN_LAST_NAME, user.getLastName());
        contentValues.put(COLUMN_GENDER, user.getGender());
        contentValues.put(COLUMN_IS_ADMIN, user.getIsAdmin() ? 1 : 0);
        contentValues.put(COLUMN_PASSWORD, user.getPassword());

        long result = sqLiteDatabase.insert(TABLE_USERS, null, contentValues);
        sqLiteDatabase.close();

        return result != -1;
    }

    public boolean authenticateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String[] projection = {COLUMN_PASSWORD};
            String selection = COLUMN_EMAIL + " = ?";
            String[] selectionArgs = {email};

            cursor = db.query(TABLE_USERS, projection, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                String hashedPassword = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
                String hashedInputPassword = PasswordHash.hashPassword(password);

                if (hashedInputPassword != null && hashedInputPassword.equals(hashedPassword)) {
                    return true;
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return false;
    }

    public boolean addOrder(String userEmail, int pizzaId, int quantity, boolean extraSauce, boolean extraCheese, boolean mixWithBeef, String optional, String orderDate, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_USER_EMAIL, userEmail);
        values.put(COLUMN_ORDER_PIZZA_ID, pizzaId);
        values.put(COLUMN_ORDER_QUANTITY, quantity);
        values.put(COLUMN_EXTRA_SAUCE, extraSauce ? 1 : 0);
        values.put(COLUMN_EXTRA_CHEESE, extraCheese ? 1 : 0);
        values.put(COLUMN_MIX_WITH_BEEF, mixWithBeef ? 1 : 0);
        values.put(COLUMN_OPTIONAL, optional);
        values.put(COLUMN_ORDER_DATE, orderDate);
        values.put(COLUMN_ORDER_TOTAL_PRICE, price);

        long result = db.insert(TABLE_ORDERS, null, values);
        db.close();

        return result != -1;
    }

    public ArrayList<Order> getOrdersByUserEmail(String userEmail) {
        ArrayList<Order> ordersList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ORDERS + " WHERE " + COLUMN_ORDER_USER_EMAIL + " = ?", new String[]{userEmail});

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setOrderId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID)));
                order.setUserEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_USER_EMAIL)));
                order.setPizzaId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PIZZA_ID)));
                order.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_QUANTITY)));
                order.setExtraSauce(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXTRA_SAUCE)) == 1);
                order.setExtraCheese(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXTRA_CHEESE)) == 1);
                order.setMixWithBeef(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MIX_WITH_BEEF)) == 1);
                order.setOptional(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTIONAL)));
                order.setOrderDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DATE)));
                order.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_TOTAL_PRICE)));
                ordersList.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return ordersList;
    }

    public List<Favorite> getUserFavorites(String userEmail) {
        List<Favorite> favoritesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {COLUMN_FAVORITE_ID, COLUMN_FAVORITE_PIZZA_ID};
        String selection = COLUMN_FAVORITE_USER_EMAIL + " = ?";
        String[] selectionArgs = {userEmail};

        Cursor cursor = db.query(TABLE_FAVORITES, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int favoriteId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FAVORITE_ID));
                int pizzaId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FAVORITE_PIZZA_ID));
                Favorite favorite = new Favorite(favoriteId, userEmail, pizzaId);
                favoritesList.add(favorite);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return favoritesList;
    }

    public boolean removeFavorite(Favorite favorite) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = false;

        try {
            String whereClause = COLUMN_FAVORITE_ID + " = ? AND " + COLUMN_FAVORITE_USER_EMAIL + " = ?";
            String[] whereArgs = {String.valueOf(favorite.getFavoriteId()), favorite.getUserEmail()};

            int rowsDeleted = db.delete(TABLE_FAVORITES, whereClause, whereArgs);

            if (rowsDeleted > 0) {
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return success;
    }

    public boolean checkOldPassword(String currentPassword, String userEmail){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_PASSWORD},
                COLUMN_EMAIL + "=?", new String[]{userEmail}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String storedPassword = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
            cursor.close();
            return storedPassword.equals(currentPassword);
        }

        if (cursor != null) {
            cursor.close();
        }

        return false;
    }

    public boolean updateUserProfile(String email, String phoneNumber, String firstName, String lastName, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            contentValues.put(COLUMN_PHONE_NUMBER, phoneNumber);
        }

        if (firstName != null && !firstName.isEmpty()) {
            contentValues.put(COLUMN_FIRST_NAME, firstName);
        }

        if (lastName != null && !lastName.isEmpty()) {
            contentValues.put(COLUMN_LAST_NAME, lastName);
        }

        if (newPassword != null && !newPassword.isEmpty()) {
            contentValues.put(COLUMN_PASSWORD, newPassword);
        }

        int rowsAffected = db.update(TABLE_USERS, contentValues, COLUMN_EMAIL + " = ?", new String[]{email});

        db.close();

        return rowsAffected > 0;
    }

    public User getUserByEmail(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        Cursor cursor = null;

        try {
            String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?";
            cursor = db.rawQuery(selectQuery, new String[]{userEmail});

            if (cursor != null && cursor.moveToFirst()) {
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER));
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENDER));
                boolean isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_ADMIN)) == 1;
                String passwordHash = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));

                user = new User(userEmail, phoneNumber, firstName, lastName, gender, isAdmin, passwordHash);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return user;
    }

    public boolean changeProfilePicture(String imageUri, String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean isUpdated = false;

        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COLUMN_PROFILE_PICTURE},
                DatabaseHelper.COLUMN_EMAIL + " = ?",
                new String[]{userEmail},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_PROFILE_PICTURE, imageUri);

            int rowsAffected = db.update(DatabaseHelper.TABLE_USERS,
                    values,
                    DatabaseHelper.COLUMN_EMAIL + " = ?",
                    new String[]{userEmail});

            isUpdated = rowsAffected > 0;
        } else {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_EMAIL, userEmail);
            values.put(DatabaseHelper.COLUMN_PROFILE_PICTURE, imageUri);

            long newRowId = db.insert(DatabaseHelper.TABLE_USERS, null, values);
            isUpdated = newRowId != -1;
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return isUpdated;
    }

    public String getProfileImage(String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        String profileImageUri = null;

        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COLUMN_PROFILE_PICTURE},
                DatabaseHelper.COLUMN_EMAIL + " = ?",
                new String[]{userEmail},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            profileImageUri = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PROFILE_PICTURE));
            cursor.close();
        }

        db.close();
        return profileImageUri;
    }

    public boolean isAdmin(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String[] projection = {COLUMN_IS_ADMIN};
            String selection = COLUMN_EMAIL + " = ?";
            String[] selectionArgs = {email};

            cursor = db.query(TABLE_USERS, projection, selection, selectionArgs,null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_ADMIN));
                return isAdmin == 1;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error checking if user is admin", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return false;
    }

    public static String getColumnNameFirstName() {
        return COLUMN_FIRST_NAME;
    }

    public static String getColumnNameLastName() {
        return COLUMN_LAST_NAME;
    }

    public static String getColumnNamePassword() {
        return COLUMN_PASSWORD;
    }

    public static String getColumnNamePhoneNumber() {
        return COLUMN_PHONE_NUMBER;
    }

    public static String getTableUsers() {
        return TABLE_USERS;
    }

    public static String getColumnIsAdmin() {
        return COLUMN_IS_ADMIN;
    }

    public static String getColumnEmail() {
        return COLUMN_EMAIL;
    }

    public boolean checkIfEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=?", new String[]{email});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public Cursor getUserByEmailA(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {
                getColumnNameFirstName(),
                getColumnNameLastName(),
                getColumnNamePassword(),
                getColumnNamePhoneNumber()
        };
        String selection = getColumnEmail() + " = ?";
        String[] selectionArgs = {email};
        return db.query(getTableUsers(), columns, selection, selectionArgs, null, null, null);
    }

    public void initializeAdminUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        String adminPasswordHashed = PasswordHash.hashPassword("123456789A");
        User admin = new User("admin@gmail.com", "0597344150", "Admin", "admin", "Female", true, adminPasswordHashed);
        boolean success = insertUser(admin);
        if (success) {
            Log.d(TAG, "Admin user created successfully");
        } else {
            Log.e(TAG, "Failed to create admin user");
        }
        db.close();
    }

    public ArrayList<Order> getAllOrders() {
        ArrayList<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT o.*, u.first_name, u.last_name " +
                "FROM " + TABLE_ORDERS + " o " +
                "JOIN " + TABLE_USERS + " u " +
                "ON o." + COLUMN_ORDER_USER_EMAIL + " = u." + COLUMN_EMAIL;

        Cursor cursor = db.rawQuery(query, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID));
                    String userEmail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_USER_EMAIL));
                    String firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
                    String lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
                    int pizzaId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PIZZA_ID));
                    int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_QUANTITY));
                    int extraSauce = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXTRA_SAUCE));
                    int extraCheese = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXTRA_CHEESE));
                    int mixWithBeef = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MIX_WITH_BEEF));
                    String optional = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTIONAL));
                    String orderDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DATE));
                    double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_TOTAL_PRICE));

                    Order order = new Order(id, userEmail, firstName, lastName, pizzaId, quantity, extraSauce == 1, extraCheese == 1, mixWithBeef == 1, optional, orderDate, price);
                    orders.add(order);
                } while (cursor.moveToNext());
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            db.close();
        }

        return orders;
    }
    public int getPizzaIdByNameAndSize(String name, String size) {
        int pizzaId = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_PIZZA_ID + " FROM " + TABLE_PIZZA + " WHERE " + COLUMN_PIZZA_NAME + " = ? AND " + COLUMN_PIZZA_SIZE + " = ?", new String[]{name, size});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                pizzaId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PIZZA_ID));
            }
            cursor.close();
        }
        db.close();
        return pizzaId;
    }

    public Double getPizzaPrice(int pizzaId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Double price = null;
        Cursor cursor = db.query(TABLE_PIZZA, new String[]{"price"},"id = ?", new String[]{String.valueOf(pizzaId)},null,null,null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
            }
            cursor.close();
        }
        return price;
    }
    public String getPizzaNameById(int pizzaId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String name = "";
        Cursor cursor = db.query(TABLE_PIZZA, new String[]{"name"},"id = ?", new String[]{String.valueOf(pizzaId)},null,null,null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            }
            cursor.close();
        }
        return name;
    }
    public ArrayList<Pizza> getPizzaOrderInfo() {
        ArrayList<Pizza> pizzaInfoList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT p." + COLUMN_PIZZA_NAME + ", " +
                "COUNT(o." + COLUMN_ORDER_ID + ") AS order_count, " +
                "SUM(o." + COLUMN_ORDER_TOTAL_PRICE + ") AS total_income " +
                "FROM " + TABLE_PIZZA + " p " +
                "JOIN " + TABLE_ORDERS + " o " +
                "ON p." + COLUMN_PIZZA_ID + " = o." + COLUMN_ORDER_PIZZA_ID + " " +
                "GROUP BY p." + COLUMN_PIZZA_NAME;

        Cursor cursor = db.rawQuery(query, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    String pizzaName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PIZZA_NAME));
                    int orderCount = cursor.getInt(cursor.getColumnIndexOrThrow("order_count"));
                    double totalIncome = cursor.getDouble(cursor.getColumnIndexOrThrow("total_income"));

                    Pizza pizzaInfo = new Pizza(pizzaName, orderCount, totalIncome);
                    pizzaInfoList.add(pizzaInfo);
                } while (cursor.moveToNext());
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            db.close();
        }

        return pizzaInfoList;
    }


}
