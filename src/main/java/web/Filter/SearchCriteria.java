package web.Filter;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data

public class SearchCriteria {
    public SearchCriteria(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    String key;
    Object value;
}
