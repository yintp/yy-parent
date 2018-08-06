package com.andy.yy.user.dao;

import com.andy.yy.user.entity.UserMessageEntity;
import java.util.List;
import java.util.Map;

public interface UserMessageDAO {
    int insert(UserMessageEntity entity);

    int updateById(UserMessageEntity entity);

    UserMessageEntity selectById(Long id);

    UserMessageEntity selectBy(Map<String, Object> map);

    List<UserMessageEntity> selectListBy(Map<String, Object> map);
}