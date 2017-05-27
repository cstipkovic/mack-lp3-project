package ejb.beans;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ejb.entities.Posicao;

@Stateless
@LocalBean
public class PosicaoBean {

    @PersistenceContext(unitName = "DerbyPU")
    private EntityManager em;

    public void save(Posicao p) {
        em.persist(p);
    }

    public List<Posicao> list(String login) {
        Query query = em.createQuery("FROM Posicao p where p.login='" + login + "'");
        List<Posicao> list = query.getResultList();
        return list;
    }
}
