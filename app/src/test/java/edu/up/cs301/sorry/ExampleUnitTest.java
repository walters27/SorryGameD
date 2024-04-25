package edu.up.cs301.sorry;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        ArrayList<Integer> location = new ArrayList<>();
        location.add(4);
        location.add(5);
        location.add(6);
        location.remove(location.lastIndexOf(4));
        assert location.get(0) == 5;

    }
}