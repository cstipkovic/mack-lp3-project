package ejb.rest;

import ejb.services.UsuarioRestService;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/services")
public class AppRest extends Application {

    private Set<Object> services;

    public AppRest() {
        services = new HashSet<Object>();
        services.add(new UsuarioRestService());
    }
}
