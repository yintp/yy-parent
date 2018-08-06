package com.andy.yy.user.service;

import com.andy.yy.user.entity.UserEntity;
import com.andy.yy.user.entity.UserFriendEntity;
import com.andy.yy.user.entity.UserMessageEntity;
import com.andy.yy.user.enums.FriendStatusEnum;
import com.andy.yy.user.exception.UserServiceException;

import java.util.List;

/**
 * @author richard
 * @since 2018/1/30 18:38
 */
public interface UserInnerService {
	Long saveUser(UserEntity user) throws UserServiceException;
	int modifyUser(UserEntity user) throws UserServiceException;
	UserEntity findUserByAccount(String account);
	UserEntity findUserById(Long userId);
	List<UserEntity> queryUser(String params);

	Long saveFriend(UserFriendEntity userFriend) throws UserServiceException;
	int modifyFriend(UserFriendEntity userFriend) throws UserServiceException;
	List<UserFriendEntity> queryFriend(Long userId);
	UserFriendEntity findFriendById(Long id);
	boolean isFriend(Long userId, Long friendId);
	boolean isAuditFriend(Long userId, Long friendId);
	UserFriendEntity findAuditFriend(Long userId, Long friendId);
	UserFriendEntity findPassFriend(Long userId, Long friendId);

	List<UserMessageEntity> queryMsgByTo(Long to);
	int modifyMessage(UserMessageEntity message);
	Long saveMessage(UserMessageEntity message);
}
