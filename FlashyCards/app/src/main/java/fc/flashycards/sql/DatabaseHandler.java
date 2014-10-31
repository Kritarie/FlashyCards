package fc.flashycards.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sean on 10/21/14.
 *
 * This class is the API for interfacing with the local SQLite database.
 * It consists of two tables: a table of decks and a table of cards.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "deckManager";

    // Table Names
    private static final String TABLE_DECKS = "decks";
    private static final String TABLE_CARDS = "cards";

    // Shared Table Column names
    private static final String KEY_ID = "id";

    // Decks Table Column names
    private static final String KEY_NAME = "name";
    private static final String KEY_SIZE = "size";

    // Cards Table Column names
    private static final String KEY_DECK = "deckId";
    private static final String KEY_FRONT = "front";
    private static final String KEY_BACK = "back";
    private static final String KEY_WEIGHT = "weight";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DECKS_TABLE = "CREATE TABLE " + TABLE_DECKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_NAME + " TEXT,"
                + KEY_SIZE + " INTEGER" + ")";
        db.execSQL(CREATE_DECKS_TABLE);
        String CREATE_CARDS_TABLE = "CREATE TABLE " + TABLE_CARDS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_DECK + " INTEGER," + KEY_FRONT + " TEXT,"
                + KEY_BACK + " TEXT," + KEY_WEIGHT + " INTEGER" + ")";
        db.execSQL(CREATE_CARDS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DECKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS);

        // Create tables again
        onCreate(db);
    }

    // Adding new deck
    public void addDeck(Deck deck) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, deck.getName()); // Deck name
        values.put(KEY_SIZE, deck.size()); // Deck size

        // Inserting Row
        db.insert(TABLE_DECKS, null, values);
        db.close(); // Closing database connection
    }

    // Adding new card to deck
    public void addCard(Card card, Deck deck) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DECK, card.getDeckId()); // Deck this card belongs to
        values.put(KEY_FRONT, card.getFront()); // Front of card
        values.put(KEY_BACK, card.getBack()); // Back of card
        values.put(KEY_WEIGHT, card.getWeight()); // card weight

        // Inserting Row
        db.insert(TABLE_CARDS, null, values);
        // Update Deck
        updateDeck(deck);
        db.close(); // Closing database connection
    }

    // Getting single deck
    public Deck getDeck(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DECKS, new String[] { KEY_ID,
                        KEY_NAME, KEY_SIZE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor == null) return null;
        cursor.moveToFirst();

        // return contact
        return new Deck(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Integer.parseInt(cursor.getString(2)));
    }

    // Getting all decks
    public ArrayList<Deck> getAllDecks() {
        ArrayList<Deck> deckList = new ArrayList<Deck>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DECKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Deck deck = new Deck();
                deck.setId(Integer.parseInt(cursor.getString(0)));
                deck.setName(cursor.getString(1));
                deck.setSize(Integer.parseInt(cursor.getString(2)));
                // Adding contact to list
                deckList.add(deck);
            } while (cursor.moveToNext());
        }

        // return contact list
        return deckList;
    }

    // Getting all cards in deck
    public ArrayList<Card> getAllCards(int deckId) {
        ArrayList<Card> cardList = new ArrayList<Card>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CARDS +
                            " WHERE " + KEY_DECK + "=" + deckId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Card card = new Card();
                card.setId(Integer.parseInt(cursor.getString(0)));
                card.setDeckId(deckId);
                card.setFront(cursor.getString(2));
                card.setBack(cursor.getString(3));
                card.setWeight(Integer.parseInt(cursor.getString(4)));
                // Adding card to list
                cardList.add(card);
            } while (cursor.moveToNext());
        }

        // return card list
        return cardList;
    }

    // Getting decks count
    public int getDecksCount() {
        String countQuery = "SELECT  * FROM " + TABLE_DECKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Updating single deck
    public int updateDeck(Deck deck) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, deck.getName());
        values.put(KEY_SIZE, deck.size());

        // updating row
        return db.update(TABLE_DECKS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(deck.getId()) });
    }

    // Updating single card
    public int updateCard(Card card) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DECK, card.getDeckId());
        values.put(KEY_FRONT, card.getFront());
        values.put(KEY_BACK, card.getBack());
        values.put(KEY_WEIGHT, card.getWeight());

        // updating row
        return db.update(TABLE_CARDS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(card.getId()) });
    }

    // Deleting single deck
    public void deleteDeck(Deck deck) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Card> cards = getAllCards(deck.getId());
        //Delete all cards under this deck
        for (Card card : cards) {
            deleteCard(card);
        }
        //Now delete the deck
        db.delete(TABLE_DECKS, KEY_ID + " = ?",
                new String[] { String.valueOf(deck.getId()) });
        db.close();
    }

    public void deleteCard(Card card) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CARDS, KEY_ID + " = ?",
                new String[] { String.valueOf(card.getId())});
    }
}
