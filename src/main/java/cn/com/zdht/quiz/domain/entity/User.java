package cn.com.zdht.quiz.domain.entity;

import cn.com.zdht.quiz.common.util.HashUtil;
import cn.com.zdht.quiz.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author 袁臻
 * 7/25/17
 * 参考文档：https://stackoverflow.com/questions/29771730/postgres-error-in-batch-insert-relation-hibernate-sequence-does-not-exist-po
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "users", indexes = {@Index(name = "user_name_index", columnList = "user_name")})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid", unique = true, nullable = false)
    private String uuid;

    @Column(name = "user_name", unique = true, nullable = false)
    private String userName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Embedded
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_code", nullable = false)
    private City city;

    /**
     * 空构造方法
     */
    public User() {
    }

    /**
     * 根据给定的UserDTO和City对象构造用户对象
     *
     * @param userDTO 一个UserDTO对象
     * @param city    一个City对象
     */
    public User(final UserDTO userDTO, final City city) {
        //生成随机的UUID
        this.uuid = UUID.randomUUID().toString();
        this.userName = userDTO.getUserName();
        this.email = userDTO.getEmail();
        //使用Guava库的Hashing处理密码
        this.password = HashUtil.hashSha384(userDTO.getPassword());
        this.city = city;
    }
}