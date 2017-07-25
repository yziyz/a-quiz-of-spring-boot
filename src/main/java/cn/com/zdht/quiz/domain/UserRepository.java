package cn.com.zdht.quiz.domain;

import cn.com.zdht.quiz.domain.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 袁臻
 * 7/25/17
 */
public interface UserRepository extends CrudRepository<User, Integer> {
    @Query(value = "SELECT * FROM user WHERE user_name = :userName LIMIT 1;", nativeQuery = true)
    User getByUserName(@Param("userName") final String userName);

    @Query(value = "SELECT * FROM user WHERE user_name LIKE CONCAT('%', :keyword, '%');", nativeQuery = true)
    List<User> getListByKeyWordOfUserName(@Param("keyword") final String keyword);

    @Transactional
    @Modifying
    @Query(value = "UPDATE user SET user_name = :newName, email = :email, password = :password, city_code = :cityCode WHERE user_name = :oldName", nativeQuery = true)
    int updateUserByUserName(@Param("oldName") final String oldName,
                             @Param("newName") final String newName,
                             @Param("email") final String email,
                             @Param("password") final String password,
                             @Param("cityCode") final String cityCode);
}
