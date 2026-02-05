package com.conggua.springboot3.server.designpattern.strategy.login;

import com.conggua.common.base.exception.BusinessException;
import com.conggua.common.base.exception.CommonErrorEnum;
import com.conggua.springboot3.server.model.dto.UserLoginDTO;
import com.conggua.springboot3.server.model.entity.User;
import com.conggua.springboot3.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.Map;
import java.util.Objects;

/**
 * @author ky
 * @description
 * @date 2024-08-23 14:44
 */
@Component("accountPassword")
public class AccountPasswrodLoginStrategy implements LoginStrategy {

    @Autowired
    private UserService userService;

    @Override
    public Map<String, String> execute(UserLoginDTO dto) {
        String account = dto.getAccount(), password = dto.getPassword();
        // 查询账户
        User user = this.getUser(account, password);
        return this.generateToken(user);
    }

    public User getUser(String account, String password) {
        User user = userService.getBy(User::getAccount, account);
        // 校验账号密码以及账号合法性
        this.validateUser(password, user);
        return user;
    }

    private void validateUser(String password, User user) {
        if (Objects.isNull(user)) {
            throw new BusinessException(CommonErrorEnum.ACCOUNT_ERROR);
        }
        password = DigestUtils.md5DigestAsHex((user.getSalt() + password).getBytes());
        if (!Objects.equals(user.getPassword(), password)) {
            throw new BusinessException(CommonErrorEnum.PASSWORD_ERROR);
        }
    }
}
