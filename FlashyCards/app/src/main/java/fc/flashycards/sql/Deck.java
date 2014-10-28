package fc.flashycards.sql;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sean on 10/21/14.
 *
 * This class defines a deck object which consists of
 * an ID for SQLite, a name, and a size representing the
 * number of cards in the deck. This class also implements
 * parcelable so it can be transferred between activities
 * with minimal performance overhead using Intent.putExtra()
 */
public class Deck implements Parcelable {

    private int id;
    private String name;
    private int size;

    // constructors
    public Deck() {

    }

    public Deck(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public Deck(int id, String name, int size) {
        this.id = id;
        this.name = name;
        this.size = size;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(int size) { this.size = size; }

    // getters
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int size() {
        return this.size;
    }

    // ------------ Parcel Methods -------------

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    //Write objects data to parcel
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(name);
        out.writeInt(size);
    }

    // this is used to regenerate the deck
    // all Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Deck> CREATOR = new Parcelable.Creator<Deck>() {
        public Deck createFromParcel(Parcel in) {
            return new Deck(in);
        }

        public Deck[] newArray(int size) {
            return new Deck[size];
        }
    };

    //constructor that takes a Parcel and gives you an object populated with it's values
    private Deck(Parcel in) {
        id = in.readInt();
        name = in.readString();
        size = in.readInt();
    }
}
