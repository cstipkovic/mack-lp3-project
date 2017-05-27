package ejb.entities;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TimestampAdapter extends XmlAdapter<String, Timestamp> {

    @Override
    public String marshal(Timestamp v) throws Exception {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(v);
        return DatatypeConverter.printDateTime(c);
    }

    @Override
    public Timestamp unmarshal(String v) throws Exception {
        Timestamp timestamp = new Timestamp(DatatypeConverter.parseDateTime(v).getTimeInMillis());
        return timestamp;
    }
}
