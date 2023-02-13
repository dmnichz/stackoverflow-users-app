package stackoverflowapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ApiResponseUserDto {
    private ApiUserDto[] items;
    @JsonProperty("has_more")
    private boolean hasMore;
}
