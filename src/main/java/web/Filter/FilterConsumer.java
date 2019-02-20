package web.Filter;

import lombok.Data;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.function.Consumer;

@Data
public class FilterConsumer implements Consumer<SearchCriteria> {

    private Predicate predicate;
    private CriteriaBuilder builder;
    private Root r;

    public FilterConsumer(Predicate predicate, CriteriaBuilder builder, Root r) {
        this.predicate = predicate;
        this.builder = builder;
        this.r = r;
    }

    @Override
    public void accept(SearchCriteria param) {
                predicate = builder.and(predicate, builder.equal(
                        r.get(param.getKey()), param.getValue()));
    }
}