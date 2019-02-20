package web.Filter;

import org.springframework.stereotype.Repository;
import web.domain.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class Filter  {

    @PersistenceContext
    private EntityManager entityManager;


    public List<Transaction> searchTrans(List<SearchCriteria> params) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Transaction> query = builder.createQuery(Transaction.class);
        Root r = query.from(Transaction.class);

        Predicate predicate = builder.conjunction();

        FilterConsumer searchConsumer =
                new FilterConsumer(predicate, builder, r);
        params.stream().forEach(searchConsumer);
        predicate = searchConsumer.getPredicate();
        query.where(predicate);

        List<Transaction> result = entityManager.createQuery(query).getResultList();
        return result;
    }
    public void save(Transaction entity) {
        entityManager.persist(entity);
    }
}