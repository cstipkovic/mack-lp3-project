package rest.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

public class ClienteRestPosicoesUsuario {

    public static void main(String[] args) throws IOException {
        String machine = "172.16.2.81";
        HttpClient cliente = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://"+ machine +":8080/AppFrontController/LP3Rest/lp3/posicoes/mackTest@mack.br");
        HttpResponse response = cliente.execute(httpget);
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
