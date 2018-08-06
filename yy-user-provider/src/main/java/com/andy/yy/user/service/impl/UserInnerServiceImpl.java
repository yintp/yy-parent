package com.andy.yy.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.andy.yy.base.db.DataSourceKey;
import com.andy.yy.base.log.LoggerUtils;
import com.andy.yy.user.dao.UserDAO;
import com.andy.yy.user.dao.UserFriendDAO;
import com.andy.yy.user.dao.UserMessageDAO;
import com.andy.yy.user.entity.UserEntity;
import com.andy.yy.user.entity.UserFriendEntity;
import com.andy.yy.user.entity.UserMessageEntity;
import com.andy.yy.user.enums.FriendStatusEnum;
import com.andy.yy.user.enums.MessageStatusEnum;
import com.andy.yy.user.exception.UserExceptionCode;
import com.andy.yy.user.exception.UserServiceException;
import com.andy.yy.user.service.UserInnerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author richard
 * @since 2018/1/30 21:04
 */
@Service("userInnerService")
public class UserInnerServiceImpl implements UserInnerService {

	private static final LoggerUtils logger = LoggerUtils.newLogger(UserInnerServiceImpl.class);

	@Autowired
	private UserDAO userDAO;
	@Autowired
	private UserFriendDAO userFriendDAO;
	@Autowired
	private UserMessageDAO userMessageDAO;

	@Override
	public Long saveUser(UserEntity user) throws UserServiceException {
		logger.info("user: {}", JSON.toJSONString(user));
		if (user == null) {
			throw new UserServiceException(UserExceptionCode.PARAMS_NOT_NULL);
		}
		userDAO.insert(user);
		return user.getId();
	}

	@Override
	public int modifyUser(UserEntity user) throws UserServiceException {
		if (user == null || user.getId() == null) {
			throw new UserServiceException(UserExceptionCode.PARAMS_NOT_NULL);
		}
		return userDAO.updateById(user);
	}

	@DataSourceKey("slave")
	@Override
	public UserEntity findUserByAccount(String account) {
		if (StringUtils.isEmpty(account)) {
			return null;
		}
		Map<String, Object> params = new HashMap<>();
		params.put("account", account);
		return userDAO.selectBy(params);
	}

	@DataSourceKey("slave")
	@Override
	public UserEntity findUserById(Long userId) {
		if (userId == null) {
			return null;
		}
		return userDAO.selectById(userId);
	}

	@DataSourceKey("slave")
	@Override
	public List<UserEntity> queryUser(String params) {
		if (StringUtils.isEmpty(params)) {
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("search", params);
		return userDAO.selectListBy(map);
	}

	@Override
	public Long saveFriend(UserFriendEntity userFriend) throws UserServiceException {
		logger.info("user: {}", JSON.toJSONString(userFriend));
		if (userFriend == null) {
			throw new UserServiceException(UserExceptionCode.PARAMS_NOT_NULL);
		}
		userFriendDAO.insert(userFriend);
		return userFriend.getId();
	}

	@Override
	public int modifyFriend(UserFriendEntity userFriend) throws UserServiceException {
		if (userFriend == null || userFriend.getId() == null) {
			throw new UserServiceException(UserExceptionCode.PARAMS_NOT_NULL);
		}
		return userFriendDAO.updateById(userFriend);
	}

	@Override
	public List<UserFriendEntity> queryFriend(Long userId) {
		List<UserFriendEntity> frends = new ArrayList<>();
		if (userId == null) {
			return frends;
		}
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("status", FriendStatusEnum.PASS.getValue());
		return userFriendDAO.selectListBy(params);
	}

	@Override
	public UserFriendEntity findFriendById(Long id) {
		return userFriendDAO.selectById(id);
	}

	@Override
	public boolean isFriend(Long userId, Long friendId) {
		if (userId == null || friendId == null) {
			return false;
		}
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("friendId", friendId);
		params.put("status", FriendStatusEnum.PASS.getValue());
		UserFriendEntity friend = userFriendDAO.selectBy(params);
		if (friend != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isAuditFriend(Long userId, Long friendId) {
		if (userId == null || friendId == null) {
			return false;
		}
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("friendId", friendId);
		params.put("status", FriendStatusEnum.AUDIT.getValue());
		UserFriendEntity friend = userFriendDAO.selectBy(params);
		if (friend != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public UserFriendEntity findAuditFriend(Long userId, Long friendId) {
		if (userId == null || friendId == null) {
			return null;
		}
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("friendId", friendId);
		params.put("status", FriendStatusEnum.AUDIT.getValue());
		UserFriendEntity friend = userFriendDAO.selectBy(params);
		return friend;
	}

	@Override
	public UserFriendEntity findPassFriend(Long userId, Long friendId) {
		if (userId == null || friendId == null) {
			return null;
		}
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("friendId", friendId);
		params.put("status", FriendStatusEnum.PASS.getValue());
		UserFriendEntity friend = userFriendDAO.selectBy(params);
		return friend;
	}

	@Override
	public List<UserMessageEntity> queryMsgByTo(Long to) {
		List<UserMessageEntity> result = new ArrayList<>();
		if (to == null) {
			return result;
		}
		Map<String, Object> params = new HashMap<>();
		params.put("to", to);
		params.put("status", MessageStatusEnum.NOT.getValue());
		return userMessageDAO.selectListBy(params);
	}

	@Override
	public int modifyMessage(UserMessageEntity message) {
		return userMessageDAO.updateById(message);
	}

	@Override
	public Long saveMessage(UserMessageEntity message) {
		if (message == null) {
			return 0L;
		}
		userMessageDAO.insert(message);
		return message.getId();
	}
}
