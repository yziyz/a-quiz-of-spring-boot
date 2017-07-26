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
    /**
     * 查询用户名中含有指定关键字的用户
     * @param keyword 关键字
     * @return 含有关键字的用户
     */
    @Query(value = "SELECT * FROM users WHERE user_name LIKE '%' || :keyword || '%'", nativeQuery = true)
    List<User> searchByUserName(@Param("keyword") final String keyword);

    /**
     * 由指定的用户名获取对象
     * @param userName 指定的用户名
     * @return 指定的用户名对应的用户对象
     */
    User getByUserName(final String userName);

    /**
     * 查询指定的用户名userName是否存在
     * 参考：https://stackoverflow.com/questions/7471625/fastest-check-if-row-exists-in-postgresql
     *
     * @param userName 指定的用户名userName
     * @return 若存在，返回true，否则返回false
     */
    @Query(value = "SELECT EXISTS(SELECT 1 FROM users WHERE user_name = :userName LIMIT 1)", nativeQuery = true)
    boolean existByUserName(@Param("userName") final String userName);

    /**
     * 排除用户名excludedName的前提下，查询指定的用户名userName是否存在
     * @param userName 指定的用户名userName
     * @param excludedName 需要排除的用户名excludedName
     * @return 若存在，返回true，否则返回false
     */
    @Query(value = "SELECT EXISTS(SELECT 1 FROM users WHERE user_name = :userName AND user_name != :excludedName LIMIT 1)", nativeQuery = true)
    boolean existByUserNameAndExcludedName(@Param("userName") final String userName,
                                           @Param("excludedName") final String excludedName);

    /**
     * 删除指定用户名对应的用户
     *
     * @param userName 指定用户名
     */
    @Transactional
    @Modifying
    void deleteByUserName(final String userName);
}
