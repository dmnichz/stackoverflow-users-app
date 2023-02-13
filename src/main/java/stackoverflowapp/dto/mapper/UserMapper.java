package stackoverflowapp.dto.mapper;

import stackoverflowapp.dto.ApiUserDto;
import stackoverflowapp.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User parseApiUserResponseDto(ApiUserDto dto) {
        User user = new User();
        user.setExternalId(dto.getUserId());
        user.setName(dto.getDisplayName());
        user.setLocation(dto.getLocation());
        user.setReputation(dto.getReputation());
        user.setAnswerCount(dto.getAnswerCount());
        user.setQuestionCount(dto.getQuestionCount());
        user.setProfileLink(dto.getLink());
        user.setAvatarLink(dto.getProfileImage());
        return user;
    }
}
