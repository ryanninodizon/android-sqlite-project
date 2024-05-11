# Simple Android using SQLite project

This simple project has CRUD functionality and uses Room-Jetpack to work with local storage.

Originally, it was for my son's work where he needed to deliver items to every house in our village. He had a paper with a list of house information and after delivering to each house, he had to manually cross out the names and move on to the next house. So, I decided to create an application for his phone to eliminate the need for pen and paper. 

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

**Interface/Service**: app/src/main/java/com/example/saintworkchecklist/data/ItemDao.java
```java
@Dao
public interface ItemDao {
    @Query("SELECT * FROM saintdb_Sheet1")
    LiveData<List<Item>> getAllItems();

    @Insert
    void insertItem(Item item);

    @Delete
    void deleteItem(Item item);

    @Update
    void update(Item item);
}
```
**Repository:** app/src/main/java/com/example/saintworkchecklist/ItemRepository.java

```java
public class ItemRepository {
    private ItemDao itemDao;
    private LiveData<List<Item>> allItems;

    public ItemRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        itemDao = db.itemDao();
        allItems = (LiveData<List<Item>>) itemDao.getAllItems();
    }

    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }

    public void insertItem(Item item) {
        new InsertAsyncTask(itemDao).execute(item);
    }

    public void deleteItem(Item item) {
        new DeleteAsyncTask(itemDao).execute(item);
    }
     ------------------------------

```
**ViewModel:** app/src/main/java/com/example/saintworkchecklist/viewmodel/ItemViewModel.java

```java
public class ItemViewModel extends AndroidViewModel {

    private ItemRepository repository;
    private LiveData<List<Item>> allItems;

    public ItemViewModel(@NonNull Application application) {
        super(application);
        repository = new ItemRepository(application);
        allItems = repository.getAllItems();
    }

    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }

    public void updateItemStatus(Item item) {
        repository.updateItem(item);
    }
}
```
