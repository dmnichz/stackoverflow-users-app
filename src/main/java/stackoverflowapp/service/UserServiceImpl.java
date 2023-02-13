package stackoverflowapp.service;

import stackoverflowapp.dto.ApiResponseTagDto;
import stackoverflowapp.dto.ApiResponseUserDto;
import stackoverflowapp.dto.ApiTagDto;
import stackoverflowapp.dto.ApiUserDto;
import stackoverflowapp.dto.mapper.UserMapper;
import stackoverflowapp.model.RequiredLocation;
import stackoverflowapp.model.Tag;
import stackoverflowapp.model.User;
import stackoverflowapp.repository.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Value("${get-all-users-by-reputation-over-223.url-prefix}")
    private String getUsersUrl;
    @Value("${get-all-user-tags-by-external-id.url-prefix}")
    private String getUserTagsUrlPrefix;
    @Value("${get-all-user-tags-by-external-id.url}")
    private String getUserTagsUrl;
    @Value("${users-saved.message}")
    private String messageUsersSavedToDB;
    private final Set<String> tags;
    private final Set<String> locations;
    private final HttpClient httpClient;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    {
        tags = new HashSet<>();
        for (Tag tag : Tag.values()) {
            tags.add(tag.getValue());
        }
        locations = new HashSet<>();
        for (RequiredLocation requiredLocation : RequiredLocation.values()) {
            locations.add(requiredLocation.getValue());
        }
    }

    public UserServiceImpl(HttpClient httpClient,
                           UserRepository userRepository,
                           UserMapper userMapper) {
        this.httpClient = httpClient;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public void syncExternalUsers() {
        ApiResponseUserDto apiResponseUserDto;
        for (int pageNumber = 1; ; pageNumber++) {
            apiResponseUserDto = httpClient.get(getUsersUrl
                            + pageNumber, ApiResponseUserDto.class);
            if (apiResponseUserDto.isHasMore()) {
                saveUserDtosToDB(apiResponseUserDto);
            } else {
                System.out.println(messageUsersSavedToDB);
                break;
            }
        }
    }

    @Override
    public void receiveRequiredUsers() {
        List<User> usersWithNecessaryLocations = userRepository.findAllByLocationIn(locations);
        ApiResponseTagDto dto;
        List<String> resultUserList = new ArrayList<>();
        for (User user : usersWithNecessaryLocations) {
            if (null != user.getAnswerCount() && user.getAnswerCount() > 0) {
                dto = httpClient.get(getUserTagsUrlPrefix
                        + user.getExternalId()
                        + getUserTagsUrl, ApiResponseTagDto.class);
                if (tagIsPresentInUserTags(dto)) {
                    resultUserList.add(userDataOutput(user, dto));
                }
            }
        }
        resultUserList.forEach(System.out::println);
    }

    private void saveUserDtosToDB(ApiResponseUserDto dto) {
        Map<Long, ApiUserDto> externalDtos = Arrays.stream(dto.getItems())
                .collect(Collectors.toMap(ApiUserDto::getUserId, Function.identity()));
        Set<Long> externalIds = externalDtos.keySet();
        List<User> existingUsers = userRepository
                .findAllByExternalIdIn(externalIds);
        Map<Long, User> existingUsersWithIds = existingUsers.stream()
                .collect(Collectors.toMap(User::getExternalId, Function.identity()));
        Set<Long> existingIds = existingUsersWithIds.keySet();
        externalIds.removeAll(existingIds);
        List<User> usersToSave = externalIds
                .stream()
                .map(i -> userMapper.parseApiUserResponseDto(externalDtos.get(i)))
                .collect(Collectors.toList());
        userRepository.saveAll(usersToSave);
    }

    private boolean tagIsPresentInUserTags(ApiResponseTagDto dto) {
        List<String> tagsList = userTags(dto);
        for (String tag : tags) {
            if (tagsList.contains(tag)) {
                return true;
            }
        }
        return false;
    }

    private List<String> userTags(ApiResponseTagDto dto) {
        return Arrays.stream(dto.getItems())
                .map(ApiTagDto::getName)
                .collect(Collectors.toList());
    }

    private String userDataOutput(User user, ApiResponseTagDto dto) {
        final String fieldDelimiter = "; ";
        final String tagDelimiter = ", ";
        StringBuilder sb = new StringBuilder();
        sb.append(user.getName()).append(fieldDelimiter);
        sb.append(user.getLocation()).append(fieldDelimiter);
        sb.append(user.getAnswerCount()).append(fieldDelimiter);
        sb.append(user.getQuestionCount()).append(fieldDelimiter);
        sb.append(String.join(tagDelimiter, userTags(dto).toString())).append(fieldDelimiter);
        sb.append(user.getProfileLink()).append(fieldDelimiter);
        sb.append(user.getAvatarLink());
        return sb.toString();
    }
}
