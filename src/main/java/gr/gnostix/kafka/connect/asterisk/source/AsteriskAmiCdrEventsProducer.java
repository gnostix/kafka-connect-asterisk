package gr.gnostix.kafka.connect.asterisk.source;

import org.apache.kafka.connect.source.SourceRecord;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.event.ManagerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by gnostix on 15/5/2018.
 */
public class AsteriskAmiCdrEventsProducer implements ManagerEventListener {
    private static final Logger logger = LoggerFactory.getLogger(AsteriskAmiSourceConnector.class);
    private ConcurrentLinkedQueue<SourceRecord> queueRecords;
    private String topic;

    public AsteriskAmiCdrEventsProducer(ConcurrentLinkedQueue<SourceRecord> queueRecords, String topic) {
        this.queueRecords = queueRecords;
        this.topic = topic;
    }

    public void onManagerEvent(ManagerEvent event) {
        SourceRecord record = new SourceRecord(null, null, topic, null, null, null,
                null, event.toString(), System.currentTimeMillis());
        queueRecords.add(record);

        // just print received events
        logger.info("--->logger " + event.toString());

    }

}
