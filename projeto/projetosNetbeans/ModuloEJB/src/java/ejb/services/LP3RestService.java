package ejb.services;

import ejb.beans.PosicaoBean;
import ejb.beans.UsuarioBean;
import java.io.StringReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import ejb.entities.Posicao;
import ejb.entities.Usuario;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;

import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

@Stateless
@LocalBean
@Path("/lp3/")
public class LP3RestService {

    @EJB
    UsuarioBean usuarioBean;
    @EJB
    PosicaoBean posicaoBean;

    @Path("/novousuario")
    @PUT
    @Consumes(MediaType.TEXT_XML)
    public void novoUsuario(String usuarioXml) {
        System.out.println(usuarioXml);
        JAXBContext jc;
        try {
            jc = JAXBContext.newInstance(Usuario.class);
            Unmarshaller u = jc.createUnmarshaller();
            StringReader reader = new StringReader(usuarioXml);
            Usuario usuario = (Usuario) u.unmarshal(reader);
            usuarioBean.criaUsuario(usuario);
        } catch (JAXBException ex) {
            Logger.getLogger(LP3RestService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Path("/novaposicao")
    @PUT
    @Consumes(MediaType.TEXT_XML)
    public void novaPosicao(String posicaoXml) {
        System.out.println(posicaoXml);
        JAXBContext jc;
        try {
            jc = JAXBContext.newInstance(Posicao.class);
            Unmarshaller u = jc.createUnmarshaller();
            StringReader reader = new StringReader(posicaoXml);
            Posicao posicao = (Posicao) u.unmarshal(reader);
            posicaoBean.save(posicao);
        } catch (JAXBException ex) {
            Logger.getLogger(LP3RestService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @GET
    @Path("/posicoes/{login}")
    @Produces({"application/xml", "application/json"})
    public List<Posicao> listaPosicoes(@PathParam("login") final String login) {
        return posicaoBean.list(login);
    }

    @GET
    @Path("/infouseravatar/{login}")
    @Produces({MediaType.TEXT_HTML})
    public String infoUserAvatar(@PathParam("login") final String login) {
        String sRet = null;
        List<Usuario> u = usuarioBean.list();
        
        for (Usuario usuario : u) {
            if (usuario.getLogin().equalsIgnoreCase(login)) {
                sRet = "<b>" + usuario.getAvatar() + "</b>";
                String[] str = acessoApi(usuario.getAvatar());
                sRet = sRet + "<br> <p><i>" + str[0] + "</i></p>";
                sRet = sRet + "<br> <img width=100 height=100 src=\"" + str[1] + "." + str[2] + "\">";
            }
        }
        
        return sRet;
    }

    private String[] acessoApi(String personagem) {
        String apikey = "a1dad26c83fae24953141d8935cf29ee";

        String privatekey = "eb2a7e0c724c44b8d99c3a93e561922c695c4c3a";
        String urlbase = "http://gateway.marvel.com/v1/public/characters";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyyhhmmss");
        String ts = sdf.format(date);
        String hashStr = MD5(ts + privatekey + apikey);
        String uri;
        String name = personagem;
        uri = urlbase + "?nameStartsWith=" + name + "&ts=" + ts + "&apikey=" + apikey + "&hash=" + hashStr;
        try {
            HttpClient cliente = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(uri);
//            HttpHost proxy = new HttpHost("172.16.0.10", 3128, "http");
//            RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
//            httpget.setConfig(config);
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
                String[] str = parseJson(out.toString());
                System.out.println(out.toString());
                reader.close();
                instream.close();
                return str;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String[] parseJson(String json) {
        JSONObject jo = new JSONObject(json);
        org.json.JSONArray results = jo.getJSONObject("data").getJSONArray("results");
        JSONObject result = (JSONObject) results.get(0);
        String str[] = new String[3];
        str[0] = result.getString("description");
        str[1] = result.getJSONObject("thumbnail").getString("path");
        str[2] = result.getJSONObject("thumbnail").getString("extension");
        return str;
    }

    private String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}
