package monepla.co.jp.cacom;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        Date date = new Date ("2016/07/30");
        ArrayList<String> list = new ArrayList<> ();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyyMM");
        String created = simpleDateFormat.format (date);
        String now = simpleDateFormat.format (new Date ());
        for (int yy = Integer.parseInt (created.substring (2,4)); yy <= Integer.parseInt (now.substring (2,4));yy++) {
            int start;
            int end;
            if (yy == Integer.parseInt (created.substring (2,4))) {
                start = Integer.parseInt (created.substring (5));
            } else {
                start = 1;
            }
            if (yy == Integer.parseInt (now.substring (2,4))) {
                end = Integer.parseInt (now.substring (5));
            } else {
                end = 12;
            }

            System.out.print (String.valueOf (start)+"\n");
            for (int mm = start; mm <= end; mm++) {
                list.add(String.valueOf (yy) + String.format ("%02d",mm));
            }

        }
        assertEquals(4, 2 + 2);
    }
}