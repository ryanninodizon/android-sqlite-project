package com.example.saintworkchecklist.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "saintdb_Sheet1")
public class Item {
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getStreetName() {
        return StreetName;
    }

    public void setStreetName(String streetName) {
        StreetName = streetName;
    }

    public Integer getStreetNumber() {
        return StreetNumber;
    }

    public void setStreetNumber(Integer streetNumber) {
        StreetNumber = streetNumber;
    }

    public String FirstName;
    public String LastName;
    public String StreetName;
    public Integer StreetNumber;

    public Integer isDone;

    public Integer isDone() {
        return isDone;
    }

    public void setDone(Integer done) {
        isDone = done;
    }

    public Item(String FirstName, String LastName, String StreetName, Integer StreetNumber, int isDone) {
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.StreetName = StreetName;
        this.StreetNumber = StreetNumber;
        this.isDone = isDone;
    }
}

