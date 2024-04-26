package edu.up.cs301.sorry;

import android.content.Context;
import android.os.Looper;
import android.view.MotionEvent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("edu.up.cs301.counter", appContext.getPackageName());
    }
    @Test
    public void testOnTouch() {
        Looper.prepare();
        ActivityScenario<SorryMainActivity> scenario = ActivityScenario.launch(SorryMainActivity.class);
        SorryHumanPlayer human = new SorryHumanPlayer("Tester");
        //human.setAsGui(scenario);
        SorryPawn result = null;
        SorryPawn testpawn = new SorryPawn(5000, 0);
        GameBoardView gb = new GameBoardView(InstrumentationRegistry.getInstrumentation().getTargetContext(), null);
        for (int i = 0; i < 16; i++) {gb.pawns.add(testpawn);}
        testpawn.location = 1;

        long downtime = 500;
        long eventtime = 500;
        float x = 50 + gb.cellSize/2;
        float y = 50 + gb.cellSize/2;
        // set x and y coords to location 1, this should select testpawn
        MotionEvent motion = MotionEvent.obtain(downtime, eventtime, MotionEvent.ACTION_DOWN, x, y, 0);
        human.onTouch(gb, motion);
        result = gb.currentPawn;
        assert result == testpawn;
    }
}