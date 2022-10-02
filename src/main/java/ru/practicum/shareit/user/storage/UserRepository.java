package ru.practicum.shareit.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //@Query("UPDATE users SET name = ?1, email = ?2 where id = ?3")
    //User updateUserById(String name, String email, Long id);
}
