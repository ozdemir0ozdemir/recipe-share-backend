package ozdemir0ozdemir.recipeshare.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.Collection;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PagedResponse<T> {

    private Response<T> response;

    private int page;
    private int pageSize;
    private int totalElementCount;
    private int totalPageCount;


    @JsonProperty(value = "isFirstPage")
    public boolean isFirstPage() {
        return this.page == 1;
    }

    @JsonProperty(value = "isLastPage")
    public boolean isLastPage() {
        return this.page == totalPageCount;
    }

    @JsonProperty(value = "hasPrevious")
    public boolean hasPrevious() {
        return this.page > 1;
    }

    @JsonProperty(value = "hasNext")
    public boolean hasNext() {
        return this.page < totalPageCount;
    }

    // Static Factories
    public static <TPage, TData extends Collection<TPage>> PagedResponse<TData> of(TData data,
                                                                                   Page<TPage> page,
                                                                                   String message) {
        return new PagedResponse<>(
                Response.ofSucceeded(data, message),
                page.getNumber() + 1,
                page.getSize(),
                page.getNumberOfElements(),
                page.getTotalPages()
        );
    }


}

