package newsapi.org.android.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NewsDao {
    @Query("SELECT * FROM news")
    List<News> getAll();


    @Insert
    void insertAll(News... news);

    @Delete
    void delete(News news);
}
