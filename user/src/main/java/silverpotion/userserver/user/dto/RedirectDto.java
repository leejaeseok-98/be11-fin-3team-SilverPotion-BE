package silverpotion.userserver.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.core.pagination.sync.PaginatedResponsesIterator;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RedirectDto {
    private String code;
}
