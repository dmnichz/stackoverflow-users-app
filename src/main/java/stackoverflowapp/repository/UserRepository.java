package stackoverflowapp.repository;

import stackoverflowapp.model.User;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByExternalIdIn(Set<Long> externalIds);

    List<User> findAllByLocationIn(Set<String> locations);
}
