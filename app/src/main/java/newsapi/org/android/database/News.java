package newsapi.org.android.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class News {
    @PrimaryKey
    @NonNull
    String title;
    String publishedAt;
    String data;
}
