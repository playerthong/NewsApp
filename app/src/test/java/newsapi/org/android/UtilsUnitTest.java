package newsapi.org.android;

import android.util.Log;

import org.junit.Test;

import newsapi.org.android.utils.Utils;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UtilsUnitTest {

    @Test
    public void testConvertToSimpleDate() {
        String simpleDate=Utils.getSimpleDate("2019-07-25T02:32:07Z");
        assertEquals("Thu, 25 Jul 2019", simpleDate);
    }

}