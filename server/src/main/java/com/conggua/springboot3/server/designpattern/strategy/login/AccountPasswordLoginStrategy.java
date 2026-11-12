package com.conggua.springboot3.server.designpattern.strategy.login;

import com.conggua.common.base.exception.BusinessException;
import com.conggua.common.base.exception.CommonErrorEnum;
import com.conggua.springboot3.server.constant.UserTypeEnum;
import com.conggua.springboot3.server.model.dto.UserLoginDTO;
import com.conggua.springboot3.server.model.entity.User;
import com.conggua.springboot3.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * @author ky
 * @description
 * @date 2024-08-23 14:44
 */
@Component("accountPassword")
public class AccountPasswordLoginStrategy implements LoginStrategy {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

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
        // 后台登录口仅允许后台用户
        if (!UserTypeEnum.ADMIN.getType().equals(user.getType())) {
            throw new BusinessException(CommonErrorEnum.NO_AUTH_ERROR, "该账号无权登录后台");
        }
        if (Boolean.FALSE.equals(user.getStatus())) {
            throw new BusinessException(CommonErrorEnum.FORBIDDEN_ERROR);
        }
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        if (!matches) {
            throw new BusinessException(CommonErrorEnum.PASSWORD_ERROR);
        }
    }
}
