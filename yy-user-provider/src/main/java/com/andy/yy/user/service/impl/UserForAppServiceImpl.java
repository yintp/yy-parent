package com.andy.yy.user.service.impl;

import com.andy.yy.base.interceptor.AuthorityContext;
import com.andy.yy.base.log.LoggerUtils;
import com.andy.yy.base.redis.RedisKey;
import com.andy.yy.base.redis.RedisManager;
import com.andy.yy.base.utils.ChineseUtil;
import com.andy.yy.base.utils.MD5Util;
import com.andy.yy.base.utils.RobotUtil;
import com.andy.yy.base.utils.UUIDUtil;
import com.andy.yy.user.dto.FriendDTO;
import com.andy.yy.user.dto.FriendDetailDTO;
import com.andy.yy.user.dto.MessageDTO;
import com.andy.yy.user.dto.SearchDTO;
import com.andy.yy.user.entity.UserEntity;
import com.andy.yy.user.entity.UserFriendEntity;
import com.andy.yy.user.entity.UserMessageEntity;
import com.andy.yy.user.enums.FriendStatusEnum;
import com.andy.yy.user.enums.MessageStatusEnum;
import com.andy.yy.user.enums.MessageTypeEnum;
import com.andy.yy.user.enums.UserStatusEnum;
import com.andy.yy.user.exception.UserExceptionCode;
import com.andy.yy.user.exception.UserServiceException;
import com.andy.yy.user.service.UserForAppService;
import com.andy.yy.user.service.UserInnerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author richard
 * @since 2018/2/6 15:48
 */
@Service("userForAppService")
public class UserForAppServiceImpl implements UserForAppService {

	private static final LoggerUtils logger = LoggerUtils.newLogger(UserInnerServiceImpl.class);

	@Autowired
	private UserInnerService userInnerService;

	@Override
	public void register(String account, String pwd) throws UserServiceException {
		if (StringUtils.isEmpty(account)) {
			throw new UserServiceException(UserExceptionCode.ACCOUNT_NOT_NULL);
		}
		if (StringUtils.isEmpty(pwd)) {
			throw new UserServiceException(UserExceptionCode.PWD_NOT_NULL);
		}
		boolean isMatch = Pattern.matches("^[A-Za-z0-9]{1,16}$", account);
		if (!isMatch) {
			throw new UserServiceException(UserExceptionCode.ACCOUNT_NOT_MATCH);
		}
		UserEntity userEntity = userInnerService.findUserByAccount(account);
		if (userEntity != null) {
			throw new UserServiceException(UserExceptionCode.ACCOUNT_EXIST);
		}
		UserEntity user = new UserEntity();
		user.setAccount(account);
		user.setStatus(UserStatusEnum.NORMAL.getValue());
		user.setCreateTime(new Date());
		user.setNickName(RobotUtil.randomNickName());
		user.setHeadImg("http://m.yy-happy.com/image/default.jpg");
		String salt = UUIDUtil.uuid();
		user.setSalt(salt);
		String md5Pwd = MD5Util.encode(pwd + salt, "UTF-8");
		user.setPwd(md5Pwd);
		Long id = userInnerService.saveUser(user);
		if (id <= 0) {
			throw new UserServiceException(UserExceptionCode.REGISTER_FAIL);
		}
	}

	@Override
	public UserEntity login(String account, String pwd) throws UserServiceException {
		if (StringUtils.isEmpty(account)) {
			throw new UserServiceException(UserExceptionCode.ACCOUNT_NOT_NULL);
		}
		if (StringUtils.isEmpty(pwd)) {
			throw new UserServiceException(UserExceptionCode.PWD_NOT_NULL);
		}
		UserEntity user = userInnerService.findUserByAccount(account);
		if (user == null) {
			throw new UserServiceException(UserExceptionCode.USER_NOT_EXSIT);
		}
		if (UserStatusEnum.DISABLED.getValue().equals(user.getStatus())) {
			throw new UserServiceException(UserExceptionCode.ACCOUNT_DISABLED);
		}
		String salt = user.getSalt();
		String md5Pwd = MD5Util.encode(pwd + salt, "UTF-8");
		if (!user.getPwd().equals(md5Pwd)) {
			throw new UserServiceException(UserExceptionCode.PWD_ERROR);
		}
		String token = UUIDUtil.upperUuid();
		UserEntity modifyUser = new UserEntity();
		modifyUser.setId(user.getId());
		modifyUser.setToken(token);
		modifyUser.setUpdateTime(new Date());
		int count = userInnerService.modifyUser(modifyUser);
		if (count > 0) {
			RedisManager.delete(RedisKey.TOKEN + user.getToken());
			AuthorityContext authorityContext = new AuthorityContext();
			authorityContext.setUserId(user.getId());
			authorityContext.setUserName(user.getNickName());
			RedisManager.set(RedisKey.TOKEN + token, authorityContext);
		}
		return userInnerService.findUserById(user.getId());
	}

	@Override
	public UserEntity findUserById(Long userId) {
		logger.info("userId: {}", userId);
		return userInnerService.findUserById(userId);
	}

	@Override
	public void modifyHeadImg(Long userId, String imgUrl) {
		if (StringUtils.isEmpty(imgUrl)) {
			throw new UserServiceException(UserExceptionCode.HEAD_IMAGE_URL_NULL);
		}
		if (imgUrl.length() > 128) {
			throw new UserServiceException(UserExceptionCode.HEAD_IMAGE_URL_TOO_LANG);
		}
		UserEntity user = userInnerService.findUserById(userId);
		if (user != null) {
			UserEntity modifyUser = new UserEntity();
			modifyUser.setId(userId);
			modifyUser.setHeadImg(imgUrl);
			userInnerService.modifyUser(modifyUser);
		}
	}

	@Override
	public void modifyNickName(Long userId, String nickName) {
		if (StringUtils.isEmpty(nickName)) {
			throw new UserServiceException(UserExceptionCode.NICK_NAME_NULL);
		}
		if (nickName.length() > 24) {
			throw new UserServiceException(UserExceptionCode.NICK_NAME_TOO_LANG);
		}
		UserEntity user = userInnerService.findUserById(userId);
		if (user != null) {
			UserEntity modifyUser = new UserEntity();
			modifyUser.setId(userId);
			modifyUser.setNickName(nickName);
			userInnerService.modifyUser(modifyUser);
		}
	}

	@Override
	public void modifyRemark(Long userId, String remark) {
		if (remark != null && remark.length() > 255) {
			throw new UserServiceException(UserExceptionCode.HEAD_IMAGE_URL_TOO_LANG);
		}
		UserEntity user = userInnerService.findUserById(userId);
		if (user != null) {
			UserEntity modifyUser = new UserEntity();
			modifyUser.setId(userId);
			modifyUser.setRemark(remark==null?"":remark);
			userInnerService.modifyUser(modifyUser);
		}
	}

	@Override
	public List<SearchDTO> search(String name) {
		List<SearchDTO> result = new ArrayList<>();
		List<UserEntity> users = userInnerService.queryUser(name);
		if (!CollectionUtils.isEmpty(users)) {
			for (UserEntity user : users) {
				if (user != null) {
					SearchDTO dto = new SearchDTO();
					dto.setId(user.getId());
					dto.setAccount(user.getAccount());
					dto.setNickName(user.getNickName());
					dto.setHeadImg(user.getHeadImg());
					dto.setRemark(user.getRemark());
					result.add(dto);
				}
			}
		}
		return result;
	}

	@Override
	public FriendDetailDTO detailFriend(Long userId, Long friendId) {
		FriendDetailDTO dto = new FriendDetailDTO();
		UserEntity friend = userInnerService.findUserById(friendId);
		if (friend != null) {
			boolean isFriend = userInnerService.isFriend(userId, friendId);
			String remark = null;
			if (isFriend) {
				UserFriendEntity userFriend = userInnerService.findPassFriend(userId, friendId);
				if (userFriend != null) {
					remark = userFriend.getFriendRemark();
				}
			}
			dto.setRemark(StringUtils.isEmpty(remark)?friend.getNickName():remark);
			dto.setAccount(friend.getAccount());
			dto.setFriend(isFriend);
			dto.setHeadImg(friend.getHeadImg());
			dto.setId(friendId);
			dto.setNickName(friend.getNickName());
		}
		return dto;
	}

	@Override
	public void applyAddFriend(Long userId, Long friendId) throws UserServiceException {
		if (userId == null || friendId == null) {
			throw new UserServiceException(UserExceptionCode.PARAMS_NOT_NULL);
		}
		if (userId ==  friendId) {
			throw new UserServiceException(UserExceptionCode.YOUSELF);
		}
		UserEntity from = userInnerService.findUserById(userId);
		UserEntity to = userInnerService.findUserById(friendId);
		if (from == null || to == null) {
			throw new UserServiceException(UserExceptionCode.USER_NOT_EXSIT);
		}
		boolean isFriend = userInnerService.isFriend(userId, friendId);
		if (isFriend) {
			throw new UserServiceException(UserExceptionCode.ALREADY_FRIEND);
		}
		boolean isAudit = userInnerService.isAuditFriend(userId, friendId);
		if (isAudit) {
			return;
		}
		UserFriendEntity friend = new UserFriendEntity();
		friend.setCreateTime(new Date());
		friend.setFriendId(friendId);
		friend.setUserId(userId);
		friend.setStatus(FriendStatusEnum.AUDIT.getValue());
		Long id = userInnerService.saveFriend(friend);
		if (id <= 0) {
			throw new UserServiceException(UserExceptionCode.APPLY_ADD_FRIEND_FAIL);
		}
	}

	@Override
	public void acceptFriend(Long userId, Long friendId) throws UserServiceException {
		if (userId == null || friendId == null) {
			throw new UserServiceException(UserExceptionCode.PARAMS_NOT_NULL);
		}
		if (userId ==  friendId) {
			throw new UserServiceException(UserExceptionCode.YOUSELF);
		}
		UserEntity from = userInnerService.findUserById(userId);
		UserEntity to = userInnerService.findUserById(friendId);
		if (from == null || to == null) {
			throw new UserServiceException(UserExceptionCode.USER_NOT_EXSIT);
		}
		boolean isFriend = userInnerService.isFriend(userId, friendId);
		if (isFriend) {
			throw new UserServiceException(UserExceptionCode.ALREADY_FRIEND);
		}
		UserFriendEntity friend = userInnerService.findAuditFriend(userId, friendId);
		if (friend == null) {
			throw new UserServiceException(UserExceptionCode.NOT_ADD_FRIEND);
		}
		UserFriendEntity modifyFriend = new UserFriendEntity();
		modifyFriend.setId(friend.getId());
		modifyFriend.setStatus(FriendStatusEnum.PASS.getValue());
		modifyFriend.setUpdateTime(new Date());
		userInnerService.modifyFriend(modifyFriend);
		// 新增一条好友记录（互为好友）
		UserFriendEntity saveFriend = new UserFriendEntity();
		saveFriend.setStatus(FriendStatusEnum.PASS.getValue());
		saveFriend.setUserId(friendId);
		saveFriend.setFriendId(userId);
		saveFriend.setCreateTime(new Date());
		userInnerService.saveFriend(saveFriend);
	}

	@Override
	public void refuseFriend(Long userId, Long friendId) throws UserServiceException {
		if (userId == null || friendId == null) {
			throw new UserServiceException(UserExceptionCode.PARAMS_NOT_NULL);
		}
		UserFriendEntity friend = userInnerService.findAuditFriend(userId, friendId);
		if (friend == null) {
			throw  new UserServiceException(UserExceptionCode.NOT_ADD_FRIEND);
		}
		UserFriendEntity modifyFriend = new UserFriendEntity();
		modifyFriend.setId(friend.getId());
		modifyFriend.setStatus(FriendStatusEnum.REFUSE.getValue());
		modifyFriend.setUpdateTime(new Date());
		userInnerService.modifyFriend(modifyFriend);
	}

	@Override
	public List<MessageDTO> queryOfflineMsgByTo(Long to) {
		if (to == null) {
			return new ArrayList<>();
		}
		List<UserMessageEntity> msgs = userInnerService.queryMsgByTo(to);
		final List<MessageDTO> result = new ArrayList<>();
		if (!CollectionUtils.isEmpty(msgs)) {
			msgs.forEach(msg->{
				MessageDTO dto = new MessageDTO();
				dto.setContent(msg.getContent());
				dto.setFrom(msg.getFrom());
				dto.setId(msg.getId());
				dto.setTo(msg.getTo());
				dto.setType(msg.getType());
				result.add(dto);
			});
		}
		return result;
	}

	@Override
	public void doMsg(Long id) {
		UserMessageEntity msg = new UserMessageEntity();
		msg.setId(id);
		msg.setStatus(MessageStatusEnum.DO.getValue());
		userInnerService.modifyMessage(msg);
	}

	@Override
	public MessageDTO handlerMsg(MessageDTO msg) {
		MessageDTO result = new MessageDTO();
		if (msg != null) {
			// 保存消息
			String type = msg.getType();
			UserMessageEntity message = new UserMessageEntity();
			if (MessageTypeEnum.MSG.getValue().equals(type)) {
				message.setType(MessageTypeEnum.MSG.getValue());
			} else if (MessageTypeEnum.ADD_FRIEND.getValue().equals(type)) {
				message.setType(MessageTypeEnum.ADD_FRIEND.getValue());
			} else if (MessageTypeEnum.PASS_FRIEND.getValue().equals(type)) {
				message.setType(MessageTypeEnum.PASS_FRIEND.getValue());
			}else if (MessageTypeEnum.REFUSE_FRIEND.getValue().equals(type)) {
				message.setType(MessageTypeEnum.REFUSE_FRIEND.getValue());
			}
			message.setFrom(msg.getFrom());
			message.setTo(msg.getTo());
			message.setContent(msg.getContent());
			message.setStatus(MessageStatusEnum.NOT.getValue());
			message.setCreateTime(new Date());
			Long id = userInnerService.saveMessage(message);
			if (id > 0) {
				result.setId(id);
				result.setContent(msg.getContent());
				result.setType(message.getType());
				result.setFrom(msg.getFrom());
				result.setTo(msg.getTo());
				return result;
			}
		}
		return null;
	}

	@Override
	public List<FriendDTO> queryFriends(Long userId) {
		List<FriendDTO> result = new ArrayList<>();
		if (userId == null) {
			return result;
		}
		List<FriendDTO> sortList = new ArrayList<>();
		List<FriendDTO> otherList = new ArrayList<>();
		List<UserFriendEntity> friends = userInnerService.queryFriend(userId);
		if (!CollectionUtils.isEmpty(friends)) {
			for (UserFriendEntity friend : friends) {
				if (friend != null) {
					UserEntity friendUser = userInnerService.findUserById(friend.getFriendId());
					if (friendUser != null) {
						String remark = StringUtils.isNotEmpty(friend.getFriendRemark())?friend.getFriendRemark():friendUser.getNickName();
						FriendDTO dto = new FriendDTO();
						dto.setFriendId(friend.getFriendId());
						dto.setName(remark);
						dto.setHeadImg(friendUser.getHeadImg());
						if (isChineseOrEnglish(remark)) {
							dto.setC(ChineseUtil.getFirstChar(remark));
							sortList.add(dto);
						} else {
							dto.setC("#");
							otherList.add(dto);
						}
					}
				}
			}
		}
		sortList.sort(Comparator.comparing(FriendDTO::getC));
		sortList.addAll(otherList);
		return sortList;
	}

	private boolean isChineseOrEnglish(String string) {
		if (StringUtils.isEmpty(string)) {
			return false;
		}
		int n = (int)string.charAt(0);
		if((19968 <= n && n < 40869)) {
			return true;
		}
		Character c = string.charAt(0);
		if (Character.isLetter(c)) {
			return true;
		}
		return false;
	}
}
