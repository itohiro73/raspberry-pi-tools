package jp.itohiro.raspitools.stats.collector;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class StatsCollectorTest {
    @Test
    public void testInit(){
        StatsCollector statsCollector = new StatsCollector();

        assertNotNull(statsCollector);
    }
}
