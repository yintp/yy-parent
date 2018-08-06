package com.andy.yy.user.dao;

import com.andy.yy.user.entity.UserEntity;
import java.util.List;
import java.util.Map;

public interface UserDAO {
    int insert(UserEntity entity);

    int updateById(UserEntity entity);

    UserEntity selectById(Long id);

    UserEntity selectBy(Map<String, Object> map);

    List<UserEntity> selectListBy(Map<String, Object> map);
}