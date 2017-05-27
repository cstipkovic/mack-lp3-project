package ejb.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import ejb.entities.TimestampAdapter;

@Entity
@Table(name = "tb_posicao")
@XmlRootElement
public class Posicao implements Serializable {

    @Id
    @Column(name = "posicao_id")
    @SequenceGenerator(name = "posicaoGenerator", sequenceName = "posicao_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "posicaoGenerator")
    private int id;
    @Column(name = "login")
    private String login;
    @Column(name = "lat")
    private String lat;
    @Column(name = "timestamp")
    protected Timestamp timestamp;
    @Column(name = "long")
    private String lon;

    public Posicao() {
    }

    public Posicao(int id, String login, String lat, String lon, Timestamp timestamp) {
        this.id = id;
        this.login = login;
        this.lat = lat;
        this.lon = lon;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    @XmlJavaTypeAdapter(TimestampAdapter.class)
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
