package rest.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

public class ClienteRestNovoUsuario {
    
    public static void main(String[] args) throws IOException {
        String machine = "172.16.2.81";
        String udata = "<usuario><nome>MackTest</nome><login>mackTest@mack.br</login>";
        udata = udata + "<sobrenome>JR</sobrenome><hash>HASH</hash></usuario>";
        HttpClient cliente = HttpClients.createDefault();
        HttpPut httpput = new HttpPut("http://" + machine + ":8080/AppFrontController/LP3Rest/lp3/novousuario");
        StringEntity se = new StringEntity(udata, ContentType.TEXT_XML);
        httpput.setEntity(se);
        HttpResponse response = cliente.execute(httpput);
        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream instream = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            System.out.println(out.toString());
            reader.close();
            instream.close();
        }
    }
}
