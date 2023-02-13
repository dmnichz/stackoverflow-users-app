package stackoverflowapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ApiUserDto {
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("display_name")
    private String displayName;
    private Integer reputation;
    @JsonProperty("answer_count")
    private Integer answerCount;
    @JsonProperty("question_count")
    private Integer questionCount;
    private String location;
    private String link;
    @JsonProperty("profile_image")
    private String profileImage;
}
