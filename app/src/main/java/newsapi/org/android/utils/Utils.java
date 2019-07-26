package newsapi.org.android.utils;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    @NonNull
    public static String getSimpleDate(@NonNull String strDate){
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
            DateFormat dateFormat2 = new SimpleDateFormat("EEE, d MMM yyyy", Locale.US);
            Date date =dateFormat.parse(strDate);
            return dateFormat2.format(date);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return "";
    }
}
