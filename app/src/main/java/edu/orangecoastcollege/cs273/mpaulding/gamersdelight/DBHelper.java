package edu.orangecoastcollege.cs273.mpaulding.gamersdelight;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

class DBHelper extends SQLiteOpenHelper {

    //TASK 1: DEFINE THE DATABASE VERSION, NAME AND TABLE NAME
    static final String DATABASE_NAME = "GamersDelight";
    private static final String DATABASE_TABLE = "Games";
    private static final int DATABASE_VERSION = 1;


    //TASK 2: DEFINE THE FIELDS (COLUMN NAMES) FOR THE TABLE
    private static final String KEY_FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_RATING = "rating";
    private static final String FIELD_IMAGE_NAME = "image_name";


    public DBHelper(Context context){
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase database){

        String table = "CREATE TABLE " + DATABASE_TABLE + "(" + KEY_FIELD_ID + "INTEGER PRIMARY KEY" +
                " AUTOINCREMENT, " + FIELD_NAME + "TEXT" + FIELD_DESCRIPTION + " TEXT," + FIELD_RATING + "REAL, " +
                FIELD_IMAGE_NAME + "TEXT" + ")";
        database.execSQL(table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          int oldVersion,
                          int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS" + DATABASE_TABLE);
        onCreate(database);
    }

    //********** DATABASE OPERATIONS:  ADD, GETALL, EDIT, DELETE

    public void addGame(Game game) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FIELD_DESCRIPTION, game.getDescription());
        values.put(FIELD_NAME, game.getName());
        values.put(FIELD_RATING, game.getRating());
        values.put(FIELD_IMAGE_NAME, game.getImageName());

        db.insert(DATABASE_TABLE, null, values);
        db.close();
    }

    public ArrayList<Game> getAllGames() {
        ArrayList<Game> gameList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor results = db.query(DATABASE_TABLE, null, null, null, null, null, null);

        if (results.moveToFirst()) {
            do {
                int id = results.getInt(0);
                String description = results.getString(1);
                String name = results.getString(2);
                float rating = results.getFloat(3);
                String imageName = results.getString(4);

                gameList.add(new Game(name, description, rating, imageName));

            } while (results.moveToNext());
        }
        db.close();
        results.close();
        return gameList;
    }

    public void deleteAllGames()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE, null, null);
        db.close();
    }

    public void updateGame(Game game){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FIELD_NAME, game.getName());
        values.put(FIELD_DESCRIPTION, game.getDescription());
        values.put(FIELD_RATING, game.getRating());
        values.put(FIELD_IMAGE_NAME, game.getImageName());

        db.update(DATABASE_TABLE, values, KEY_FIELD_ID + "=?", new String[] {String.valueOf(game.getId())});
        db.close();
    }

    public Game getGame(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor results = db.query(DATABASE_TABLE, null, KEY_FIELD_ID + "=?", new String[] {String.valueOf(id)},
                null, null, null, null);

        if (results != null) {
            results.moveToFirst();
            Game game = new Game(results.getInt(0), results.getString(1), results.getString(2),
                    results.getFloat(3), results.getString(4));
            return game;
        }
        db.close();
        results.close();
        return null;
    }





}
