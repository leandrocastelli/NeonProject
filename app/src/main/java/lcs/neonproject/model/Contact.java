package lcs.neonproject.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.volley.toolbox.StringRequest;

/**
 * Created by Leandro on 9/24/2016.
 */

public class Contact implements Model,Parcelable{

    public Contact(String id, String name, String phoneNumber, int profilePicture) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profilePicture = profilePicture;
    }

    public String getId() {
        return id;

    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(int profilePicture) {
        this.profilePicture = profilePicture;
    }

    String id;
    String name;
    String phoneNumber;
    int profilePicture;


    public Contact(Parcel in){
        String[] data = new String[4];

        in.readStringArray(data);
        this.id = data[0];
        this.name = data[1];
        this.phoneNumber = data[2];
        this.profilePicture = Integer.parseInt(data[2]);
    }
    @Override
    public Contact getContact() {
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {this.id,
                this.name,
                this.phoneNumber,
                ((Integer)this.profilePicture).toString()});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}
