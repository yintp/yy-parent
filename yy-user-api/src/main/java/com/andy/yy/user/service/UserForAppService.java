package com.andy.yy.user.service;

import com.andy.yy.user.dto.FriendDTO;
import com.andy.yy.user.dto.FriendDetailDTO;
import com.andy.yy.user.dto.MessageDTO;
import com.andy.yy.user.dto.SearchDTO;
import com.andy.yy.user.entity.UserEntity;
import com.andy.yy.user.exception.UserServiceException;

import java.util.List;

/**
 * @author richard
 * @since 2018/1/30 18:31
 */
public interface UserForAppService {
	void register(String account, String pwd) throws UserServiceException;
	UserEntity login(String account, String pwd) throws UserServiceException;
	UserEntity findUserById(Long userId);
	void modifyHeadImg(Long userId, String imgUrl);
	void modifyNickName(Long userId, String nickName);
	void modifyRemark(Long userId, String remark);

	List<SearchDTO> search(String name);
	FriendDetailDTO detailFriend(Long userId, Long friendId);
	List<FriendDTO> queryFriends(Long userId);
	void applyAddFriend(Long userId, Long friendId) throws UserServiceException;
	void acceptFriend(Long userId, Long id) throws UserServiceException;
	void refuseFriend(Long userId,Long id) throws UserServiceException;

	List<MessageDTO> queryOfflineMsgByTo(Long to);
	void doMsg(Long id);
	MessageDTO handlerMsg(MessageDTO msg);
}
