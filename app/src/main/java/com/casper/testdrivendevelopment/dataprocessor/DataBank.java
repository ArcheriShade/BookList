package com.casper.testdrivendevelopment.dataprocessor;

import android.content.Context;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DataBank {
    private ArrayList<Book> arrayListBooks = new ArrayList<>();
    private Context context;
    private final String BOOK_FILE_NAME="books.txt";
    public DataBank(Context context)
    {
        this.context=context;
    }
    public ArrayList<Book> getBooks() {
        return arrayListBooks;
    }

    public void Save() {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(context.openFileOutput(BOOK_FILE_NAME,Context.MODE_PRIVATE));
            oos.writeObject(arrayListBooks);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Load() {
        ObjectInputStream ois = null;
        arrayListBooks=new ArrayList<>();
        try {
            ois = new ObjectInputStream(context.openFileInput(BOOK_FILE_NAME));
            arrayListBooks = (ArrayList<Book>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}