# Simple Android using SQLite project

This simple project has CRUD functionality and uses Room-Jetpack to work with local storage.

This is the file where the connection to SQLite was established.
> **app/src/main/java/com/example/saintworkchecklist/data/AppDatabase.java**

```java
@Database(entities = {Item.class}, version = 6, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "data.sqlite";//change your SQLite file name and should be saved in assets folder
    private static volatile AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .createFromAsset(DATABASE_NAME)
                    .build();
        }
        return instance;
    }

    public abstract ItemDao itemDao();
}
```
