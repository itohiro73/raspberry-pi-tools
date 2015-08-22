package jp.itohiro.raspitools.stats.receiver;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class StatsReceiverTest {
    @Test
    public void testInit(){
        StatsReceiver statsReceiver = new StatsReceiver();

        assertNotNull(statsReceiver);
    }
}
