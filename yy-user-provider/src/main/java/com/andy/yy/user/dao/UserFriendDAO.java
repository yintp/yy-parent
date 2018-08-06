package com.andy.yy.user.dao;

import com.andy.yy.user.entity.UserFriendEntity;
import java.util.List;
import java.util.Map;

public interface UserFriendDAO {
    int insert(UserFriendEntity entity);

    int updateById(UserFriendEntity entity);

    UserFriendEntity selectById(Long id);

    UserFriendEntity selectBy(Map<String, Object> map);

    List<UserFriendEntity> selectListBy(Map<String, Object> map);
}