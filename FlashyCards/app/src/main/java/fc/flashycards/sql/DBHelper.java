package fc.flashycards.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sean on 10/21/14.
 */

public class DBHelper extends SQLiteOpenHelper {

    // Logcat cat
    private static final String LOG = "DBHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "deckManager";

    // Table Names
    private static final String TABLE_CARDS = "cards";
    private static final String TABLE_DECKS = "decks";
    private static final String TABLE_CARDS_DECKS = "cards_decks";

    // Common column names
    private static final String KEY_ID = "id";

    // Cards Table - column names
    private static final String KEY_CARD_FRONT = "front";
    private static final String KEY_CARD_BACK = "back";

    // Decks Table - column names
    private static final String KEY_DECK_NAME = "deck_name";
    private static final String KEY_DECK_SIZE = "deck_size";

    // Cards_Decks Table - column names
    private static final String KEY_CARD_ID = "card_id";
    private static final String KEY_DECK_ID = "deck_id";

    // Table Create Statements
    // cards table create statement
    private static final String CREATE_TABLE_CARDS = "CREATE TABLE "
            + TABLE_CARDS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CARD_FRONT
            + " TEXT," + KEY_CARD_BACK + " TEXT" + ")";

    // deck table create statement
    private static final String CREATE_TABLE_DECK = "CREATE TABLE " + TABLE_DECKS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DECK_NAME + " TEXT,"
            + KEY_DECK_SIZE + " INTEGER" + ")";

    // cards_deck table create statement
    private static final String CREATE_TABLE_CARDS_DECK = "CREATE TABLE "
            + TABLE_CARDS_DECKS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_CARD_ID + " INTEGER," + KEY_DECK_ID + " INTEGER" + ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_CARDS);
        db.execSQL(CREATE_TABLE_DECK);
        db.execSQL(CREATE_TABLE_CARDS_DECK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DECKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS_DECKS);

        // create new tables
        onCreate(db);
    }

    // ------------ Card Table Methods ------------

    public long createCard(Card card, long deck_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CARD_FRONT, card.getFront());
        values.put(KEY_CARD_BACK, card.getBack());

        // insert row
        long card_id = db.insert(TABLE_CARDS, null, values);

        createCardDeck(card_id, deck_id);

        return card_id;
    }

    public Card getCard(long todo_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CARDS + " WHERE "
                + KEY_ID + " = " + todo_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Card card = new Card();
        card.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        card.setFront((c.getString(c.getColumnIndex(KEY_CARD_FRONT))));
        card.setBack(c.getString(c.getColumnIndex(KEY_CARD_BACK)));

        return card;
    }

    // Get all the cards in the specified deck
    public List<Card> getAllCardsByDeck(String deck_name) {
        List<Card> cards = new ArrayList<Card>();

        String selectQuery = "SELECT  * FROM " + TABLE_CARDS + " td, "
                + TABLE_DECKS + " tg, " + TABLE_CARDS_DECKS + " tt WHERE tg."
                + KEY_DECK_NAME + " = '" + deck_name + "'" + " AND tg." + KEY_ID
                + " = " + "tt." + KEY_DECK_ID + " AND td." + KEY_ID + " = "
                + "tt." + KEY_CARD_ID;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Card card = new Card();
                card.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                card.setFront((c.getString(c.getColumnIndex(KEY_CARD_FRONT))));
                card.setBack(c.getString(c.getColumnIndex(KEY_CARD_BACK)));

                // adding to card list
                cards.add(card);
            } while (c.moveToNext());
        }

        return cards;
    }

    public int updateCard(Card card) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CARD_FRONT, card.getFront());
        values.put(KEY_CARD_BACK, card.getBack());

        // updating row
        return db.update(TABLE_CARDS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(card.getId()) });
    }

    public void deleteCard(long card_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CARDS, KEY_ID + " = ?",
                new String[] { String.valueOf(card_id) });
    }

    // ------------ Deck Table Methods ------------

    public long createDeck(Deck deck) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DECK_NAME, deck.getName());
        values.put(KEY_DECK_SIZE, deck.size());

        // insert row
        return db.insert(TABLE_DECKS, null, values);
    }

    public List<Deck> getAllDecks() {
        List<Deck> decks = new ArrayList<Deck>();
        String selectQuery = "SELECT  * FROM " + TABLE_DECKS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Deck d = new Deck();
                d.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                d.setName(c.getString(c.getColumnIndex(KEY_DECK_NAME)));

                // adding to tags list
                decks.add(d);
            } while (c.moveToNext());
        }

        return decks;
    }

    public int updateDeck(Deck deck) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DECK_NAME, deck.getName());

        // updating row
        return db.update(TABLE_DECKS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(deck.getId()) });
    }

    public void deleteDeck(Deck deck) {
        SQLiteDatabase db = this.getWritableDatabase();

        // get all cards in this deck
        List<Card> allDeckCards = getAllCardsByDeck(deck.getName());

        // delete all cards
        for (Card card : allDeckCards) {
            deleteCard(card.getId());
        }

        // now delete the tag
        db.delete(TABLE_DECKS, KEY_ID + " = ?",
                new String[] { String.valueOf(deck.getId()) });
    }

    // ------------ DeckCard Table Methods ------------

    //Assign a card to a deck
    public long createCardDeck(long card_id, long deck_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CARD_ID, card_id);
        values.put(KEY_DECK_ID, deck_id);

        long id = db.insert(TABLE_CARDS_DECKS, null, values);

        return id;
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
