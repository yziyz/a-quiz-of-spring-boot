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
     *
     * @param keyword 关键字
     * @return 含有关键字的用户
     */
    @Query(value = "SELECT * FROM users WHERE user_name LIKE '%' || :keyword || '%'", nativeQuery = true)
    List<User> searchByUserName(@Param("keyword") final String keyword);

    /**
     * 获取指定UUID对应的用户
     *
     * @param uuid 指定的UUID
     * @return 指定UUID对应的用户
     */
    User getByUuid(@Param("uuid") final String uuid);

    /**
     * 根据指定的城市名称关键词查询用户
     *
     * @param keyword 指定的城市名称关键词
     * @return 用户城市名称匹配关键词的用户
     */
    @Query(value = "SELECT id, uuid, user_name, email, password, users.city_code FROM users, cities WHERE users.city_code = cities.city_code AND cities.city_name LIKE '%' || :keyWord || '%'",
            nativeQuery = true)
    List<User> searchByCityName(@Param("keyWord") final String keyword);

    /**
     * 查询指定的用户名是否存在
     * 参考：https://stackoverflow.com/questions/7471625/fastest-check-if-row-exists-in-postgresql
     *
     * @param userName 指定的用户名
     * @return 若存在，返回true，否则返回false
     */
    @Query(value = "SELECT EXISTS(SELECT 1 FROM users WHERE user_name = :userName LIMIT 1)", nativeQuery = true)
    boolean existByUserName(@Param("userName") final String userName);

    /**
     * 查询指定的用户UUID是否存在
     *
     * @param uuid 指定的用户UUID
     * @return 若存在，返回true，否则返回false
     */
    @Query(value = "SELECT EXISTS(SELECT 1 FROM users WHERE uuid = :uuid LIMIT 1)", nativeQuery = true)
    boolean existByUuid(@Param("uuid") final String uuid);

    /**
     * 排除uuid对应的用户名的前提下，查询指定的用户名userName是否存在
     *
     * @param userName 指定的用户名userName
     * @param uuid     需要排除的用户uuid
     * @return 若存在，返回true，否则返回false
     */
    @Query(value = "SELECT EXISTS(SELECT 1 FROM users WHERE user_name = :userName AND uuid != :uuid LIMIT 1)",
            nativeQuery = true)
    boolean existByUserNameAndExcludedUuid(@Param("userName") final String userName,
                                           @Param("uuid") final String uuid);

    /**
     * 删除指定用户名对应的用户
     *
     * @param uuid 指定UUID
     */
    @Transactional
    @Modifying
    void deleteByUuid(final String uuid);
}
