package com.kx.blog.service.Impl;

import com.alibaba.fastjson.JSON;
import com.kx.blog.domain.ErrorCode;
import com.kx.blog.domain.Result;
import com.kx.blog.domain.entity.SysUser;
import com.kx.blog.service.LoginService;
import com.kx.blog.service.SysUserService;
import com.kx.blog.utils.JWTUtils;
import com.kx.blog.vo.params.LoginParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @description:登录服务
 * @author: Biobang
 * @date: 2022/7/30 20:05
 **/
@Service
public class LoginServiceImpl implements LoginService {
    //加密盐用于加密
    private static final String slat = "kx!@#";
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public Result login(LoginParam loginParam) {
        /**
         * 1. 检查参数是否合法
         * 2. 根据用户名和密码去user表中查询 是否存在
         * 3. 如果不存在 登录失败
         * 4. 如果存在 ，使用jwt 生成token 返回给前端
         * 5. token放入redis当中，redis  token：user信息 设置过期时间（相比来说session会给服务器产生压力，这么做也是为了实现jwt的续签）
         *  (登录认证的时候 先认证token字符串是否合法，去redis认证是否存在)
         */

        //1. 检查参数是否合法

        //获取账号，密码
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        if (StringUtils.isBlank(account)||StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }

        //2. 根据用户名和密码去user表中查询 是否存在
        //3. 如果不存在 登录失败
        String pwd = DigestUtils.md5Hex(password + slat);
        SysUser sysUser = sysUserService.findUser(account,pwd);
        if (sysUser==null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }

        //4. 如果存在 ，使用jwt 生成token 返回给前端
        //5. token放入redis当中，redis  token：user信息 设置过期时间
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("Token_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);

    }

    /**
     * token校验
     * @param token
     * @return
     */
    @Override
    public SysUser checkToken(String token) {
        //如果token为空
        if (StringUtils.isBlank(token)){
            return null;
        }
        //不为空，但解析失败
        Map<String, Object> checkToken = JWTUtils.checkToken(token);
        if (checkToken==null){
            return null;
        }
        //解析成功
        String sysJson = redisTemplate.opsForValue().get("Token_" + token);
        if (StringUtils.isBlank(sysJson)){
            return null;
        }
        SysUser sysUser = JSON.parseObject(sysJson, SysUser.class);
        return sysUser;
    }

    /**
     * 退出登录
     * @param token
     * @return
     */
    @Override
    public Result logout(String token) {
        //后端直接删除redis中的token
        redisTemplate.delete("Token_"+token);
        return Result.success(null);
    }

    /**
     * 用户注册
     * @param loginParam
     * @return
     */
    @Override
    public Result register(LoginParam loginParam) {
        /**
         * 1. 判断参数 是否合法
         * 2. 判断账户是否存在，存在 返回账户已经被注册
         * 3. 不存在，注册用户
         * 4. 生成token
         * 5. 存入redis 并返回
         * 6. 注意 加上事务，一旦中间的任何过程出现问题，注册的用户 需要回滚
         */
        //1. 判断参数 是否合法
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        String nickname = loginParam.getNickname();
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)||StringUtils.isBlank(nickname)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        //2. 判断账户是否存在，存在 返回账户已经被注册
        SysUser sysUser = sysUserService.findUserByAccount(account);
        if (sysUser != null){
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        //3. 不存在，注册用户
        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password+slat));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/user/user_6.png");
        //1 为true
        sysUser.setAdmin(1);
        // 0 为false
        sysUser.setDeleted(0);
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        this.sysUserService.save(sysUser);
        //4. 生成token
        String token = JWTUtils.createToken(sysUser.getId());
        //5. 存入redis 并返回
        redisTemplate.opsForValue().set("Token_"+token,JSON.toJSONString(sysUser),1,TimeUnit.DAYS);
        return Result.success(token);

    }
}
