package com.feiqu.web.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.*;
import com.feiqu.common.utils.DESUtils;
import com.feiqu.common.utils.SpringUtils;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.constant.IconUrlConfig;
import com.feiqu.framwork.support.cache.CacheManager;
import com.feiqu.framwork.util.*;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.*;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.MessageUserDetail;
import com.feiqu.system.pojo.response.SimThoughtDTO;
import com.feiqu.system.pojo.response.ThoughtWithUser;
import com.feiqu.system.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.jeesuite.filesystem.FileSystemClient;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 import javax.annotation.Resource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.commands.JedisCommands;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

/**
 * @author cwd
 * @create 2017-09-16:19
 **/
@Controller
@RequestMapping("u")
public class UserController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    /*@Resource
    FSProviderSpringFacade provider;*/

    @Resource
    private ThoughtService thoughtService;

    @Resource
    private ArticleService articleService;

    @Resource
    private WebUtil webUtil;
    @Resource
    private CMessageService messageService;
    @Resource
    private UploadImgRecordService uploadImgRecordService;
    @Resource
    private JavaMailSenderImpl mailSender;
    @Resource
    private UserActivateService userActivateService;
    @Resource
    private UserFollowService userFollowService;
    @Resource
    private FqUserService userService;
    @Resource
    private FqThirdPartyService fqThirdPartyService;
    @Resource
    private FqLabelService fqLabelService;
    @Resource
    private FqVisitRecordService visitRecordService;
    @Resource
    private FqCollectService fqCollectService;
    //????????????
    @Resource
    private FqUserPayWayService fqUserPayWayService;

    @GetMapping("upLevel")
    public String upLevel(Model model){
        FqUserCache fqUserCache = getCurrentUser();
        int month = DateUtil.thisMonth()+1;
        int lastMonth = month == 1? 12 : month-1;
        JedisCommands commands = JedisUtil.me();
        Double score = commands.zscore(CommonConstant.FQ_ACTIVE_USER_SORT+month,fqUserCache.getId().toString());
        Double lastMonthScore = commands.zscore(CommonConstant.FQ_ACTIVE_USER_SORT+lastMonth,fqUserCache.getId().toString());
        Double totolScore = (score == null?0:score)+(lastMonthScore==null?0:lastMonthScore);
        model.addAttribute("totolScore",totolScore);
        return "/user/upLevel";
    }

    @PostMapping("upLevel")
    @ResponseBody
    public BaseResult upLevelGo(){
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = getCurrentUser();
            if(fqUserCache == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            int originLevel = fqUserCache.getLevel() == null?1:fqUserCache.getLevel();
            int needActiveNum = 0;
            switch (originLevel){
                case 1:needActiveNum = 300;break;
                case 2:needActiveNum = 800;break;
                case 3:needActiveNum = 1500;break;
                case 4:needActiveNum = 3000;break;
                default:needActiveNum = 300;break;
            }

            int month = DateUtil.thisMonth()+1;
            int lastMonth = month == 1? 12 : month-1;
            JedisCommands commands = JedisUtil.me();
            Double score = commands.zscore(CommonConstant.FQ_ACTIVE_USER_SORT+month,fqUserCache.getId().toString());
            Double lastMonthScore = commands.zscore(CommonConstant.FQ_ACTIVE_USER_SORT+lastMonth,fqUserCache.getId().toString());
            Double totolScore = (score == null?0:score)+(lastMonthScore==null?0:lastMonthScore);
            if(totolScore < needActiveNum){
                result.setResult(ResultEnum.PARAM_ERROR);
                result.setMessage("???????????????");
                return result;
            }
            FqUser toUpdate = new FqUser();
            toUpdate.setId(fqUserCache.getId());
            toUpdate.setLevel(originLevel+1);
            userService.updateByPrimaryKeySelective(toUpdate);
            CacheManager.refreshUserCacheByUid(fqUserCache.getId());
            commands.zadd(CommonConstant.FQ_ACTIVE_USER_SORT+month,totolScore.intValue()-needActiveNum,fqUserCache.getId().toString());
            result.setData(originLevel+1);
        } catch (Exception e) {
            result.setResult(ResultEnum.FAIL);
            logger.error("????????????",e);
        }finally {
             
        }
        return result;
    }

    @RequestMapping("/manage")
    public String manage(Model model){
        FqUserCache fqUserCache = getCurrentUser();
        if(fqUserCache == null){
            return USER_LOGIN_REDIRECT_URL;
        }
        if(fqUserCache.getRole() != 1){
            return GENERAL_ERROR_URL;
        }
        return "/user/manage";
    }
    @RequestMapping("/manage/list")
    @ResponseBody
    public Object list(Integer page){
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(page,CommonConstant.DEAULT_PAGE_SIZE);
            FqUserExample example = new FqUserExample();
            example.setOrderByClause("create_time desc");
            List<FqUser> users = userService.selectByExample(example);
            PageInfo pageInfo = new PageInfo(users);
            result.setData(pageInfo);
        } catch (Exception e) {
            logger.error("??????????????????",e);
            result.setCode("1");
            result.setMessage("??????????????????");
        }
        return result;
    }

    @RequestMapping("/manage/changeStatus")
    @ResponseBody
    public Object changeStatus(Integer userId,Integer status){
        BaseResult result = new BaseResult();
        try {
            FqUserCache currentUser = getCurrentUser();
            if(currentUser == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if(currentUser.getRole() != 1){
                result.setResult(ResultEnum.USER_NOT_AUTHORIZED);
                return result;
            }
            logger.info("????????????????????????????????????{}?????????????????????{}????????????{}",currentUser.getId(),userId,status);
            FqUser user = userService.selectByPrimaryKey(userId);
            if(UserRoleEnum.ADMIN_USER_ROLE.getValue().equals(user.getRole())){
                result.setResult(ResultEnum.FAIL);
                return result;
            }
            user.setStatus(status);
            userService.updateByPrimaryKey(user);
        } catch (Exception e) {
            logger.error("????????????????????????",e);
            result.setCode("1");
            result.setMessage("????????????????????????");
        }
        return result;
    }

    @RequestMapping("/manage/adminRole")
    @ResponseBody
    public Object adminRole(Integer role,Integer userId){
        BaseResult result = new BaseResult();
        try {
            FqUserCache currentUser = getCurrentUser();
            if(currentUser == null){
                return USER_LOGIN_REDIRECT_URL;
            }
            if(currentUser.getRole() != 1){
                return GENERAL_ERROR_URL;
            }
            logger.info("????????????????????????????????????{}?????????????????????{}????????????{}",currentUser.getId(),userId,role);
            FqUser user = userService.selectByPrimaryKey(userId);
            if(UserRoleEnum.ADMIN_USER_ROLE.getValue().equals(user.getRole())){
                result.setResult(ResultEnum.FAIL);
                return result;
            }
            user.setRole(role);
            userService.updateByPrimaryKey(user);
        } catch (Exception e) {
            logger.error("?????????????????????????????????",e);
            result.setCode("1");
            result.setMessage("?????????????????????????????????");
        }
        return result;
    }

    @GetMapping("set")
    public String set(HttpServletRequest request, HttpServletResponse response,Model model) {
        FqUserCache fqUser = webUtil.currentUser(request, response);
        if(fqUser == null){
            return login(CommonConstant.DOMAIN_URL+request.getRequestURI(),request);
        }
        boolean qqBind = false,sinaBind = false;
        FqThirdPartyExample example = new FqThirdPartyExample();
        example.createCriteria().andUserIdEqualTo(fqUser.getId());
        List<FqThirdParty> thirdParties = fqThirdPartyService.selectByExample(example);
        for(FqThirdParty p : thirdParties){
            if("QQ".equals(p.getProvider())){
                qqBind = true;
            }else if("SINA".equals(p.getProvider())){
                sinaBind = true;
            }
        }
        FqUserPayWayExample fqUserPayWayExample = new FqUserPayWayExample();
        fqUserPayWayExample.createCriteria().andUserIdEqualTo(fqUser.getId()).andDelFlagEqualTo(YesNoEnum.NO.getValue());
        List<FqUserPayWay> fqUserPayWays = fqUserPayWayService.selectByExample(fqUserPayWayExample);
        if(CollectionUtil.isNotEmpty(fqUserPayWays)){
            Optional<FqUserPayWay> alipayWayOptional = fqUserPayWays.stream().filter(e->e.getPayWay().equals(PayWayEnum.ALIPAY.getPayWay())).findFirst();
            if(alipayWayOptional.isPresent()){
                FqUserPayWay alipayPayway = alipayWayOptional.get();
                model.addAttribute("alipayImgUrl",alipayPayway.getPayImgUrl());
            }
            Optional<FqUserPayWay> wechatWayOptional = fqUserPayWays.stream().filter(e->e.getPayWay().equals(PayWayEnum.WECHAT_PAY.getPayWay())).findFirst();
            if(wechatWayOptional.isPresent()){
                FqUserPayWay alipayPayway = wechatWayOptional.get();
                model.addAttribute("wechatPayImgUrl",alipayPayway.getPayImgUrl());
            }
        }
        request.setAttribute("qqBind",qqBind);
        request.setAttribute("sinaBind",sinaBind);
        return "/user/set";
    }

    @PostMapping("uploadIcon")
    @ResponseBody
    public Object uploadIcon(HttpServletRequest request, HttpServletResponse response, MultipartFile file) {
        BaseResult result = new BaseResult();
        String fileName = "";
        File localFile = null;
        try {
            long size = file.getSize();
            int limit = 300 * 1024;//???????????????50kb=>100kb=>300
            if (size > limit) {
                result.setResult(ResultEnum.FILE_TOO_LARGE);
                result.setMessage("??????????????????????????????300KB");
                return result;
            }
            FqUserCache user = webUtil.currentUser(request,response);

            if (user == null) {
                result.setResult(ResultEnum.FAIL);
                return result;
            }
            String picUrl;
            Date now = new Date();
            String time = DateFormatUtils.format(now,"yyyy-MM-dd");
            String path = CommonConstant.FILE_UPLOAD_TEMP_PATH+File.separator+time;
            File fileParent = new File(path);
            if(!fileParent.exists()){
                fileParent.mkdir();
            }
//            String path = CommonConstant.ICON_UPLOAD_PATH + File.separator + time;
            String extName = FileUtil.extName(file.getOriginalFilename());
            List<String> picExtList = CommonConstant.picExtList;
            if(!picExtList.contains(extName)){
                result.setResult(ResultEnum.PIC_EXTNAME_NOT_RIGHT);
                return result;
            }
            fileName = CommonConstant.FILE_NAME_PREFIX + DateUtil.format(now, "yyyyMMddHHmmss") + "." + extName;
            localFile = new File(fileParent, fileName);
            //MultipartFile?????????????????????
            file.transferTo(localFile);

            //????????????md5
            String picMd5 = DigestUtil.md5Hex(localFile);
            //??????md5?????????????????????
            UploadImgRecordExample imgRecordExample = new UploadImgRecordExample();
            imgRecordExample.createCriteria().andPicMd5EqualTo(picMd5).andPicSizeEqualTo(size);
            UploadImgRecord uploadImgRecord = uploadImgRecordService.selectFirstByExample(imgRecordExample);
            if (uploadImgRecord != null && StringUtils.isNotBlank(uploadImgRecord.getPicUrl())) {
                picUrl = uploadImgRecord.getPicUrl();
            } else {
//                picUrl = provider.upload(fileName, localFile);//qiniu
                picUrl = FileSystemClient.getClient("aliyun").upload("icon/"+fileName,localFile);
//                aliyunOssClient.putObject(CommonConstant.ALIYUN_OSS_BUCKET_NAME,"icon/"+fileName,file.getInputStream());
                picUrl+="?x-oss-process=image/resize,m_fixed,h_160,w_160";
                uploadImgRecord = new UploadImgRecord(picUrl, picMd5, new Date(), WebUtil.getIP(request), user.getId(), size);
                uploadImgRecordService.insert(uploadImgRecord);
            }
            FqUser toUpdateUser = new FqUser();
            toUpdateUser.setIcon(picUrl);
            toUpdateUser.setId(user.getId());
            userService.updateByPrimaryKeySelective(toUpdateUser);
            user.setIcon(picUrl);
            CacheManager.refreshUserCacheByUser(user);
            CommonUtils.addActiveNum(user.getId(),ActiveNumEnum.UPDATE_ICON.getValue());
            result.setData(picUrl);
        } catch (Exception e) {
            logger.error("??????icon??????", e);
            result.setCode(CommonConstant.SYSTEM_ERROR_CODE);
            return result;
        }
        return result;
    }

    @GetMapping("/login")
    public String login(String redirectSuccessUrl, HttpServletRequest request) {
        request.setAttribute("redirectSuccessUrl", redirectSuccessUrl);
        return "/login";
    }

    @ResponseBody
    @PostMapping(value = "/login")
    public BaseResult dologin(HttpServletRequest request, FqUser user, HttpServletResponse response,
                              String remember, String redirectSuccessUrl, String verifyCode) {
        BaseResult result = new BaseResult();
        try {
            String ip = WebUtil.getIP(request);
            logger.info("login:?????????????????????{},ip:{}",user.toString(),ip);
            if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            if(StringUtils.isNotEmpty(verifyCode) && !verifyCode.equals(request.getSession().getAttribute("code"))){
                result.setResult(ResultEnum.VERIFY_CODE_NOT_CORRECT);
                return result;
            }
            FqUserExample FqUserExample = new FqUserExample();
            FqUserExample.createCriteria().andUsernameEqualTo(user.getUsername())
                    .andPasswordEqualTo(DigestUtil.md5Hex(user.getPassword()));
            FqUser userDB = userService.selectFirstByExample(FqUserExample);
            if (userDB != null) {
                if(UserStatusEnum.FROZEN.getValue().equals(userDB.getStatus())){
                    result.setResult(ResultEnum.USER_FROZEN);
                    return result;
                }
                WebUtil.loginUser(request, response, userDB, "on".equals(remember));
                result.setResult(ResultEnum.SUCCESS);
                if (StringUtils.isNotBlank(redirectSuccessUrl)) {
                    result.setData(redirectSuccessUrl);
                }else {
                    result.setData(CommonConstant.DOMAIN_URL+"/u/"+userDB.getId()+"/home");
                }
                return result;
            } else {
                result.setResult(ResultEnum.USERNAME_OR_PASSWORD_ERROR);
                logger.info("???????????????");
                return result;
            }
        } catch (Exception e) {
            logger.error("login error", e);
        }
        return result;
    }

    @GetMapping("logout")
    public String logout(HttpServletResponse response) {
        CookieUtil.removeCookie(response, CommonConstant.USER_ID_COOKIE);
        return "redirect:/u/login";
    }

    @GetMapping("register")
    public String register(HttpServletRequest request) {
        try {
            String ip = WebUtil.getIP(request);
            String address = CommonUtils.getRegionByIp(ip);
            request.setAttribute("address", address);
        } catch (Exception e) {
            logger.error("????????????????????????", e);
        }
        return "/register";
    }

    @PostMapping("register")
    @ResponseBody
    public Object registerDo(HttpServletRequest request, FqUser user, HttpServletResponse response, String verifyCode) {
        BaseResult result = new BaseResult();
        logger.info("registerDo:???????????????{}",user.toString());
        try {
            if(!Validator.isEmail(user.getUsername())){
                result.setResult(ResultEnum.EMAIL_NOT_CORRECT);
                return result;
            }
            if (user.getPassword().length() < 6 || user.getPassword().length() > 20) {
                result.setResult(ResultEnum.PASSWORD_LENGTH_ERROR);
                return result;
            }
            if ("123456".equals(user.getPassword())) {
                result.setResult(ResultEnum.PASSWORD_TOO_SIMPLE);
                return result;
            }
            if(StringUtils.isNotEmpty(verifyCode) && !verifyCode.equals(request.getSession().getAttribute("code"))){
                result.setResult(ResultEnum.VERIFY_CODE_NOT_CORRECT);
                return result;
            }
            FqUserExample FqUserExample = new FqUserExample();
            FqUserExample.createCriteria().andUsernameEqualTo(user.getUsername().trim());
            FqUser userDBUsername = userService.selectFirstByExample(FqUserExample);
            if (userDBUsername != null) {
                result.setResult(ResultEnum.USERNAME_EXIST);
                return result;
            }
            FqUser toRegister = new FqUser();
            toRegister.setNickname(StrUtil.cleanBlank(user.getNickname()));
            toRegister.setUsername(user.getUsername().trim());
            toRegister.setPassword(user.getPassword().trim());
            if(StringUtils.isBlank(user.getNickname())){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            FqUserExample.clear();
            FqUserExample.createCriteria().andNicknameEqualTo(user.getNickname());
            FqUser userDBNickname = userService.selectFirstByExample(FqUserExample);
            if (userDBNickname != null) {
                result.setResult(ResultEnum.NICKNAME_EXIST);
                return result;
            }
            Date now = new Date();
            toRegister.setCreateIp(WebUtil.getIP(request));
            toRegister.setCreateTime(now);
            toRegister.setPassword(DigestUtil.md5Hex(toRegister.getPassword()));
            if (StringUtils.isBlank(user.getIcon())) {
                toRegister.setIcon(IconUrlConfig.icons.get(new Random().nextInt(IconUrlConfig.size())));
            }
            toRegister.setIsMailBind(YesNoEnum.NO.getValue());
            toRegister.setQudouNum(CommonConstant.INIT_QUDOU_NUM);
            toRegister.setLevel(1);//??????1J
            toRegister.setStatus(UserStatusEnum.NORMAL.getValue());
            userService.insert(toRegister);
            String token = UUID.fastUUID().toString(true);
            UserActivate userActivate = new UserActivate(toRegister.getId(),token,now);
            userActivateService.insert(userActivate);

            //todo delete
            //MailMessage mailMessage = new SimpleMailMessage();
            ThreadPoolTaskExecutor executor = SpringUtils.getBean("threadPoolTaskExecutor");
            executor.execute(() -> {
                String htmlContent = getEmailHtml(user.getNickname(),token);
                final MimeMessage mimeMessage = mailSender.createMimeMessage();
                try {
                    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "GBK");
                    helper.setFrom(mailSender.getUsername());
                    //????????????
                    helper.setSubject("????????????");
                    //??????????????????????????????
                    helper.setTo(user.getUsername());
                    helper.setText(htmlContent, true);

                } catch (MessagingException e) {
                    logger.error("??????{}?????????????????? ",user.getUsername());
                }
                mailSender.send(mimeMessage);
                CMessage message = new CMessage();
                message.setPostUserId(-1);
                message.setCreateTime(now);
                message.setDelFlag(YesNoEnum.NO.getValue());
                message.setReceivedUserId(toRegister.getId());
                message.setType(MsgEnum.OFFICIAL_MSG.getValue());
                message.setContent("???????????????????????????????????????????????????????????????????????????????????????????????????????????????qq????????????632118669,??????????????? "+ DateUtil.formatDateTime(now));
                messageService.insert(message);
            }
            );
//????????????????????????????????????
            WebUtil.loginUser(request,response,toRegister,true);
        } catch (MailException e) {
            logger.error("????????????{} ",user.getUsername());
            result.setResult(ResultEnum.FAIL);
        }

        return result;
    }

    @GetMapping("activate")
    public String activate(String token, HttpServletRequest request, HttpServletResponse response) {
        FqUserCache currUser = webUtil.currentUser(request,response);
        if(currUser == null){
            return "/error/activateNotLogin";
        }
        if(StringUtils.isNotBlank(token)){
            UserActivateExample example = new UserActivateExample();
            example.createCriteria().andTokenEqualTo(token);
            UserActivate userActivate = userActivateService.selectFirstByExample(example);
            if(userActivate != null && userActivate.getUserId().equals(currUser.getId())){
                currUser.setIsMailBind(YesNoEnum.YES.getValue());
                userService.updateByPrimaryKeySelective(currUser.transferFqUser());
                logger.info("{} ??????????????????",currUser.getUsername());
                CacheManager.refreshUserCacheByUser(currUser);
            }
        }
        return "/user/activate";
    }

    @GetMapping("reSendEmail")
    @ResponseBody
    public Object reSendEmail(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        FqUserCache currUser = webUtil.currentUser(request,response);
        if(!Validator.isEmail(currUser.getUsername())){
            result.setResult(ResultEnum.EMAIL_NOT_CORRECT);
            return result;
        }
        UserActivateExample example = new UserActivateExample();
        example.createCriteria().andUserIdEqualTo(currUser.getId());
        UserActivate userActivate = userActivateService.selectFirstByExample(example);
        String htmlContent = "";
        if(userActivate == null){
            String token = RandomUtil.randomString(32);
            userActivate = new UserActivate(currUser.getId(),token,new Date());
            userActivateService.insert(userActivate);
            htmlContent = getEmailHtml(currUser.getNickname(),token);
        }else {
            htmlContent = getEmailHtml(currUser.getNickname(),userActivate.getToken());
        }

        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "GBK");
            helper.setFrom(mailSender.getUsername());
            //????????????
            helper.setSubject("????????????");
            //??????????????????????????????
            helper.setTo(currUser.getUsername());
            helper.setText(htmlContent, true);

        } catch (MessagingException e) {
            logger.error("??????{}?????????????????? ",currUser.getUsername());
            result.setResult(ResultEnum.FAIL);
        }

        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) SpringContextUtil.getBean("threadPoolTaskExecutor");
        executor.execute(() -> mailSender.send(mimeMessage));
        return result;
    }

    @ResponseBody
    @PostMapping("updatePass")
    public Object updatePass(FqUser queryUser, HttpServletRequest request, HttpServletResponse response) {
        BaseResult baseResult = new BaseResult();
        FqUserCache currUser = webUtil.currentUser(request,response);
        if(currUser == null){
            baseResult.setResult(ResultEnum.USER_NOT_LOGIN);
            return baseResult;
        }
        if(!currUser.getId().equals(queryUser.getId())){
            baseResult.setResult(ResultEnum.USER_NOT_SAME);
            return baseResult;
        }
        if(queryUser.getPassword().length() < 6){
            baseResult.setResult(ResultEnum.PASSWORD_LENGTH_ERROR);
            return baseResult;
        }
        FqUser toUpdate = new FqUser();
        toUpdate.setId(queryUser.getId());
        toUpdate.setPassword(DigestUtil.md5Hex(queryUser.getPassword()));
        userService.updateByPrimaryKeySelective(toUpdate);
        return baseResult;
    }

    @GetMapping("forget")
    public String forget(HttpServletRequest request, String key){
        if(StringUtils.isNotBlank(key)){
            request.setAttribute("key",key);
            return "/user/passReset";
        }
        return "/user/passForget";
    }

    @PostMapping("resetPass")
    @ResponseBody
    public Object resetPass(HttpServletRequest request, String key, String password, String verifyCode){
        BaseResult result = new BaseResult();
        if(StringUtils.isBlank(key) || StringUtils.isBlank(verifyCode)){
            result.setResult(ResultEnum.PARAM_NULL);
            return result;
        }
        String username = "";
        try {
            if(!verifyCode.equals(request.getSession().getAttribute("code"))){
                result.setResult(ResultEnum.VERIFY_CODE_NOT_CORRECT);
                return result;
            }

            // 2.??????key
            String info = null;
            // cookie ??????
            String secret = CommonConstant.FORGET_PASSWORD_SECRET;
            try {
                info = new DESUtils(secret).decryptString(key);
            } catch (RuntimeException e) {
                // ignore
            }
            String[] userInfo = info.split("~");
            username = userInfo[0];
            String beginTime   = userInfo[1];
            long lastTime = System.currentTimeMillis()-Long.parseLong(beginTime);
            logger.info("?????????????????????{}???",lastTime/1000);
            if(lastTime/1000 > 30*60){
                result.setResult(ResultEnum.TIME_EXPIRED);
                return result;
            }
            FqUser fqUser = new FqUser();
            fqUser.setPassword(DigestUtil.md5Hex(password));
            FqUserExample example = new FqUserExample();
            example.createCriteria().andUsernameEqualTo(username);
            userService.updateByExampleSelective(fqUser,example);
        } catch (Exception e) {
            logger.error("resetPass ?????????????????????"+username,e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @PostMapping("forget")
    @ResponseBody
    public Object doForget(HttpServletRequest request, HttpServletResponse response, String username, String verifyCode){
        BaseResult result = new BaseResult();
        try {
            if(!Validator.isEmail(username)){
                result.setResult(ResultEnum.EMAIL_NOT_CORRECT);
                return result;
            }
            if(!request.getSession().getAttribute("code").equals(verifyCode)){
                result.setResult(ResultEnum.VERIFY_CODE_NOT_CORRECT);
                return result;
            }
            FqUserExample example = new FqUserExample();
            example.createCriteria().andUsernameEqualTo(username);
            FqUser fqUser = userService.selectFirstByExample(example);
            if(fqUser == null){
                result.setResult(ResultEnum.USER_NOT_FOUND);
                return result;
            }

            long now = System.currentTimeMillis();

            StringBuilder passForgetBuilder = new StringBuilder()
                    .append(username).append("~")
                    .append(now);

            // cookie ??????
            String secret = CommonConstant.FORGET_PASSWORD_SECRET;
            // ??????cookie
            String key = new DESUtils(secret).encryptString(passForgetBuilder.toString());
            String htmlContent = getForgetEmailHtml(fqUser.getNickname(),key);
            final MimeMessage mimeMessage = mailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "GBK");
                helper.setFrom(mailSender.getUsername());
                //????????????
                helper.setSubject("????????????");
                //??????????????????????????????
                helper.setTo(username);
                helper.setText(htmlContent, true);
            } catch (MessagingException e) {
                logger.error("??????{}?????????????????? ",username);
            }

            ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) SpringContextUtil.getBean("threadPoolTaskExecutor");
            executor.execute(() -> mailSender.send(mimeMessage));
        } catch (Exception e) {
           logger.error("?????????????????????????????????username:{}",username);
           result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @ResponseBody
    @PostMapping("updateUserInfo")
    public Object updateUserInfo(FqUser queryUser, HttpServletRequest request, HttpServletResponse response) {
        BaseResult baseResult = new BaseResult();
        FqUserCache currUser = webUtil.currentUser(request,response);
        if(currUser == null){
            baseResult.setResult(ResultEnum.USER_NOT_LOGIN);
            return baseResult;
        }
        if(!currUser.getId().equals(queryUser.getId())){
            baseResult.setResult(ResultEnum.USER_NOT_SAME);
            return baseResult;
        }
        if(!Validator.isEmail(queryUser.getUsername())){
            baseResult.setResult(ResultEnum.EMAIL_NOT_CORRECT);
            return baseResult;
        }
        if(StringUtils.isBlank(queryUser.getNickname().trim())){
            baseResult.setResult(ResultEnum.PARAM_NULL);
            return  baseResult;
        }
        if(StringUtils.length(queryUser.getCity()) > 20){
            baseResult.setResult(ResultEnum.PARAM_ERROR);
            baseResult.setMessage("??????????????????20?????????");
            return  baseResult;
        }
        FqUserExample example = new FqUserExample();
        example.createCriteria().andNicknameEqualTo(queryUser.getNickname().trim()).andIdNotEqualTo(queryUser.getId());
        FqUser userDB = userService.selectFirstByExample(example);
        if (null != userDB) {
            baseResult.setResult(ResultEnum.NICKNAME_EXIST);
            return baseResult;
        }
        example.clear();
        example.createCriteria().andUsernameEqualTo(queryUser.getUsername().trim()).andIdNotEqualTo(queryUser.getId());
        userDB = userService.selectFirstByExample(example);
        if (null != userDB) {
            baseResult.setResult(ResultEnum.USERNAME_EXIST);
            return baseResult;
        }
        if(!queryUser.getUsername().equals(currUser.getUsername())){
            queryUser.setIsMailBind(YesNoEnum.NO.getValue());
        }
        FqUser toUpdate = new FqUser();
        toUpdate.setId(queryUser.getId());
        toUpdate.setUsername(queryUser.getUsername());
        toUpdate.setNickname(queryUser.getNickname());
        toUpdate.setBirth(queryUser.getBirth());
        toUpdate.setCity(queryUser.getCity());
        toUpdate.setEducation(queryUser.getEducation());
        toUpdate.setSchool(queryUser.getSchool());
        toUpdate.setSex(queryUser.getSex());
        toUpdate.setIsSingle(queryUser.getIsSingle());
        toUpdate.setSign(queryUser.getSign());
        userService.updateByPrimaryKeySelective(toUpdate);
        FqUser fqUserNew = userService.selectByPrimaryKey(queryUser.getId());
        CacheManager.refreshUserCacheByUser(new FqUserCache(fqUserNew));
        return baseResult;
    }

    //??????
    @PostMapping("postMsg")
    @ResponseBody
    public Object postMsg(CMessage message, HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        logger.info("??????:???????????????{}",message.toString());
        try {
            Assert.notNull(message,"?????????????????????");
            FqUserCache currUser = webUtil.currentUser(request,response);
            if(currUser == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if(StringUtils.isBlank(message.getContent())){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            message.setPostUserId(currUser.getId());
            message.setCreateTime(new Date());
            message.setType(MsgEnum.FRIEND_MSG.getValue());
            message.setDelFlag(YesNoEnum.NO.getValue());
            messageService.insert(message);
        } catch (Exception e) {
           logger.error("postMsg error",e);
           result.setMessage(e.getMessage());
            result.setCode(CommonConstant.SYSTEM_ERROR_CODE);
        }
        return result;
    }

    @PostMapping("/clearMsgs")
    @ResponseBody
    public Object clearMsgs(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = null;
        try {
            result = new BaseResult();
            FqUserCache currUser = webUtil.currentUser(request,response);
            if(currUser == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            logger.info("clearMsgs:?????????????????????????????????{}",currUser.getNickname());
            CMessageExample example = new CMessageExample();
            example.createCriteria().andReceivedUserIdEqualTo(currUser.getId());
            CMessage message = new CMessage();
            message.setDelFlag(YesNoEnum.YES.getValue());
            int count = messageService.updateByExampleSelective(message, example);
            result.setData(count);
        } catch (Exception e) {
            logger.error("??????????????????",e);
        }
        return result;
    }

    @GetMapping("msgs")
    public String msgs(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(defaultValue = "0") Integer pageIndex,
                       @RequestParam(defaultValue = "10") Integer pageSize) {
            FqUserCache user = webUtil.currentUser(request, response);
            if(user == null){
                return login(CommonConstant.DOMAIN_URL+request.getRequestURI(),request);
            }
            CMessageExample messageExample = new CMessageExample();
            messageExample.setOrderByClause("create_time desc");
            messageExample.createCriteria().andReceivedUserIdEqualTo(user.getId())
                    .andDelFlagEqualTo(YesNoEnum.NO.getValue());
            PageHelper.startPage(pageIndex, pageSize);
            List<MessageUserDetail> messages = messageService.selectMyMsgsByMessage(messageExample);
            PageInfo page = new PageInfo(messages);
            request.setAttribute("count", page.getTotal());
            request.setAttribute("messages", messages);
            request.setAttribute("pageIndex", pageIndex);
            request.setAttribute("pageSize", pageSize);
            return "/user/msgs";
    }

    @PostMapping("msgsCount")
    @ResponseBody
    public Object msgsCount(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        FqUserCache user = webUtil.currentUser(request, response);
        if(user == null){
            result.setData(0);
            return result;
        }
        CMessageExample example = new CMessageExample();
        example.createCriteria().andReceivedUserIdEqualTo(user.getId())
                .andDelFlagEqualTo(YesNoEnum.NO.getValue()).andIsReadNotEqualTo(YesNoEnum.YES.getValue());
        Integer count = messageService.countByExample(example);
        result.setData(count);
        return result;
    }

    //???????????????
    @GetMapping("/{pUserId}/peopleIndex")
    public String peopleIndex(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer pUserId) {
        FqUserCache user = webUtil.currentUser(request, response);
        if(user == null){
            return login(CommonConstant.DOMAIN_URL+request.getRequestURI(),request);
        }
        if (user.getId().equals(pUserId)) {
            return myIndex(request,response);
        }
        FqUser oUser = userService.selectByPrimaryKey(pUserId);
        if (oUser == null) {
            request.setAttribute("msg", "error");
            return "/error";
        }
        request.setAttribute("oUser", oUser);
        request.setAttribute("user", user);

        FqVisitRecord visitRecord = new FqVisitRecord();
        visitRecord.setVisitedUserId(pUserId);
        visitRecord.setVisitUserId(user.getId());
        visitRecord.setVisitTime(new Date());
        visitRecord.setDelFlag(YesNoEnum.NO.getValue());
        visitRecordService.insert(visitRecord);

        PageHelper.startPage(0, 10);
        ThoughtExample thoughtExample = new ThoughtExample();
        thoughtExample.setOrderByClause("create_time desc");
        thoughtExample.createCriteria().andUserIdEqualTo(pUserId).andDelFlagEqualTo(YesNoEnum.NO.getValue());
        List<Thought> thoughts = thoughtService.selectByExample(thoughtExample);
        request.setAttribute("thoughts", thoughts);
        PageHelper.startPage(0, 10);
        ArticleExample articleExample = new ArticleExample();
        articleExample.setOrderByClause("create_time desc");
        articleExample.createCriteria().andUserIdEqualTo(pUserId).andDelFlagEqualTo(YesNoEnum.NO.getValue());
        List<Article> articles = articleService.selectByExample(articleExample);
        request.setAttribute("articles", articles);
        UserFollowExample followExample = new UserFollowExample();
        followExample.createCriteria().andFollowerUserIdEqualTo(user.getId()).andFollowedUserIdEqualTo(oUser.getId())
                .andDelFlagEqualTo(YesNoEnum.NO.getValue());
        UserFollow userFollow = userFollowService.selectFirstByExample(followExample);
        request.setAttribute("follow",userFollow != null);
        return "/user/peopleIndex";
    }

    @GetMapping("/center")
    public String center(HttpServletRequest request, HttpServletResponse response) {
        FqUserCache user = webUtil.currentUser(request, response);
        if(user == null){
            return login(CommonConstant.DOMAIN_URL+request.getRequestURI(),request);
        }
        return "/user/center";
    }

    //??????????????????????????????
    @GetMapping("/{uid}/home")
    public String home(HttpServletRequest request, HttpServletResponse response,
                       @PathVariable Integer uid, @RequestParam(defaultValue = "1") Integer p, String order) {
        try {
            FqUserCache user = webUtil.currentUser(request, response);
            if(user == null){
                return login(CommonConstant.DOMAIN_URL+request.getRequestURI(),request);
            }
            logger.info("?????????{}??????????????????",user.getNickname());
            ThoughtWithUser topThought = null;
            Jedis jedis=JedisUtil.me();
            if(org.apache.commons.lang3.StringUtils.isNotEmpty(jedis.get(CommonConstant.THOUGHT_TOP_LIST))){
                topThought = JSON.parseObject(jedis.get(CommonConstant.THOUGHT_TOP_LIST),ThoughtWithUser.class);
            };
            request.setAttribute("topThought",topThought);
            if (user.getId().equals(uid)) {
                PageHelper.startPage(p, 20);
                ThoughtExample example = new ThoughtExample();
                if("zan".equals(order)){
                    example.setOrderByClause("like_count desc");
                }else {
                    example.setOrderByClause("create_time desc");
                }
                example.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue());
                List<ThoughtWithUser> thoughts = thoughtService.getThoughtWithUser(example);

                List<Integer> list = fqCollectService.selectTopicIdsByTypeAndUid(TopicTypeEnum.THOUGHT_TYPE.getValue(),uid);
                JedisCommands commands = JedisUtil.me();
                if(CollUtil.isNotEmpty(thoughts)){
                    String key = CacheManager.getCollectKey(TopicTypeEnum.THOUGHT_TYPE.name(),uid);
                    long redisCount = commands.scard(key);
                    if(redisCount == 0){
                        for(Integer tid : list){
                            commands.sadd(key, tid.toString());
                        }
                        commands.expire(key,24*60*60L);
                    }
                    for(ThoughtWithUser thoughtWithUser : thoughts){
                        if(StringUtils.isNotEmpty(thoughtWithUser.getPicList())){
                            thoughtWithUser.setPictures(Arrays.asList(thoughtWithUser.getPicList().split(",")));
                        }
                        thoughtWithUser.setCollected(commands.sismember(key,thoughtWithUser.getId().toString()));
                    }
                }
                PageInfo page = new PageInfo(thoughts);
                request.setAttribute("thoughtList",thoughts);

                String followKey = CommonConstant.FQ_FOLLOW_PREFIX+user.getId();
                String followStr = commands.get(followKey);
                int fansCount;
                int followCount;
                if(StringUtils.isEmpty(followStr)){
                    UserFollowExample followExample = new UserFollowExample();
                    followExample.createCriteria().andFollowedUserIdEqualTo(uid).andDelFlagEqualTo(YesNoEnum.NO.getValue());
                    fansCount = userFollowService.countByExample(followExample);
                    followExample.clear();
                    followExample.createCriteria().andFollowerUserIdEqualTo(uid).andDelFlagEqualTo(YesNoEnum.NO.getValue());
                    followCount = userFollowService.countByExample(followExample);
                    commands.set(followKey,fansCount+"|"+followCount);
                    commands.expire(followKey, 24*60*60L);
                }else {
                    String[] ff = followStr.split("\\|");
                    fansCount = Integer.valueOf(ff[0]);
                    followCount = Integer.valueOf(ff[1]);
                }
                List<SimThoughtDTO> sim7ThoughtDTOS = Lists.newArrayList();
                String hotThoughts = commands.get(CommonConstant.SEVEN_DAYS_HOT_THOUGHT_LIST);
                if(StringUtils.isNotEmpty(hotThoughts)){
                    sim7ThoughtDTOS =  JSON.parseArray(hotThoughts, SimThoughtDTO.class);
                }
                request.setAttribute("sevenDaysT",sim7ThoughtDTOS);
                request.setAttribute("fansCount",fansCount);
                request.setAttribute("followCount",followCount);
                request.setAttribute("count",page.getTotal());
                request.setAttribute("p",p);
                request.setAttribute("pageSize",20);
                request.setAttribute("activeUserList",CommonConstant.FQ_ACTIVE_USER_LIST);
            }else {
                return peopleIndex(request,response,uid);
            }
        }catch (Exception e){
            request.setAttribute(CommonConstant.SYSTEM_ERROR_CODE,"?????????");
            return GENERAL_CUSTOM_ERROR_URL;
        }finally {
             
        }
        return "/user/home";
    }

    @GetMapping("/myIndex")
    public String myIndex(HttpServletRequest request, HttpServletResponse response) {
        FqUserCache user = webUtil.currentUser(request, response);
        if(user == null){
            return login(CommonConstant.DOMAIN_URL+request.getRequestURI(),request);
        }
        PageHelper.startPage(0, 10);
        ThoughtExample thoughtExample = new ThoughtExample();
        thoughtExample.setOrderByClause("create_time desc");
        thoughtExample.createCriteria().andUserIdEqualTo(user.getId()).andDelFlagEqualTo(YesNoEnum.NO.getValue());
        List<Thought> thoughts = thoughtService.selectByExample(thoughtExample);
        request.setAttribute("thoughts", thoughts);
        PageHelper.startPage(0, 10);
        ArticleExample articleExample = new ArticleExample();
        articleExample.setOrderByClause("create_time desc");
        articleExample.createCriteria().andUserIdEqualTo(user.getId()).andDelFlagEqualTo(YesNoEnum.NO.getValue());
        List<Article> articles = articleService.selectByExample(articleExample);
        request.setAttribute("articles", articles);
        return "/user/myIndex";
    }

    private String getEmailHtml(String nickname,String token){
        return "<html><head>" +
                "<base target=\"_blank\">" +
                "<style type=\"text/css\">" +
                "::-webkit-scrollbar{ display: none; }" +
                "</style>" +
                "<style id=\"cloudAttachStyle\" type=\"text/css\">" +
                "#divNeteaseBigAttach, #divNeteaseBigAttach_bak{display:none;}" +
                "</style>" +
                "</head>" +
                "<body tabindex=\"0\" role=\"listitem\">" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 600px; border: 1px solid #ddd; border-radius: 3px; color: #555; font-family: 'Helvetica Neue Regular',Helvetica,Arial,Tahoma,'Microsoft YaHei','San Francisco','????????????','Hiragino Sans GB',STHeitiSC-Light; font-size: 12px; height: auto; margin: auto; overflow: hidden; text-align: left; word-break: break-all; word-wrap: break-word;\"> <tbody style=\"margin: 0; padding: 0;\"> <tr style=\"background-color: #393D49; height: 60px; margin: 0; padding: 0;\"> <td style=\"margin: 0; padding: 0;\"> <div style=\"color: #5EB576; margin: 0; margin-left: 30px; padding: 0;\"><a style=\"font-size: 14px; margin: 0; padding: 0; color: #5EB576; " +
                "text-decoration: none;\" href=\"http://www.flyfun.site/\" target=\"_blank\">flyfun??????</a></div> </td> </tr> " +
                "<tr style=\"margin: 0; padding: 0;\"> <td style=\"margin: 0; padding: 30px;\"> <p style=\"line-height: 20px; margin: 0; margin-bottom: 10px; padding: 0;\"> " +
                "?????????<em style=\"font-weight: 700;\">" + nickname + "</em>??? </p> <p style=\"line-height: 2; margin: 0; margin-bottom: 10px; padding: 0;\"> ???????????????<em>flyfun??????</em>?????????????????????????????????????????? </p> " +
                "<div style=\"\"> <a href=\""+CommonConstant.DOMAIN_URL+"/u/activate?token="+token+"\" style=\"background-color: #009E94; color: #fff; display: inline-block; height: 32px; line-height: 32px; margin: 0 15px 0 0; padding: 0 15px; text-decoration: none;\" target=\"_blank\">??????????????????</a> </div> <p style=\"line-height: 20px; margin-top: 20px; padding: 10px; background-color: #f2f2f2; font-size: 12px;\"> ????????????????????????????????????????????????????????????????????????????????????????????????????????? </p> </td> </tr> <tr style=\"background-color: #fafafa; color: #999; height: 35px; margin: 0; padding: 0; text-align: center;\"> <td style=\"margin: 0; padding: 0;\">????????????????????????????????????</td> </tr> </tbody> </table>" +
                "<style type=\"text/css\">" +
                "body{font-size:14px;font-family:arial,verdana,sans-serif;line-height:1.666;padding:0;margin:0;overflow:auto;white-space:normal;word-wrap:break-word;min-height:100px}" +
                "td, input, button, select, body{font-family:Helvetica, 'Microsoft Yahei', verdana}" +
                "pre {white-space:pre-wrap;white-space:-moz-pre-wrap;white-space:-pre-wrap;white-space:-o-pre-wrap;word-wrap:break-word;width:95%}" +
                "th,td{font-family:arial,verdana,sans-serif;line-height:1.666}" +
                "img{ border:0}" +
                "header,footer,section,aside,article,nav,hgroup,figure,figcaption{display:block}" +
                "blockquote{margin-right:0px}" +
                "</style>" +
                "<style id=\"ntes_link_color\" type=\"text/css\">a,td a{color:#064977}</style>" +
                "</body></html>";
    }

    private String getForgetEmailHtml(String nickname, String key) {
        return "<html><head>" +
                "<base target=\"_blank\">" +
                "<style type=\"text/css\">" +
                "::-webkit-scrollbar{ display: none; }" +
                "</style>" +
                "<style id=\"cloudAttachStyle\" type=\"text/css\">" +
                "#divNeteaseBigAttach, #divNeteaseBigAttach_bak{display:none;}" +
                "</style>" +
                "</head>" +
                "<body tabindex=\"0\" role=\"listitem\">" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 600px; border: 1px solid #ddd; border-radius: 3px; color: #555; font-family: 'Helvetica Neue Regular',Helvetica,Arial,Tahoma,'Microsoft YaHei','San Francisco','????????????','Hiragino Sans GB',STHeitiSC-Light; font-size: 12px; height: auto; margin: auto; overflow: hidden; text-align: left; word-break: break-all; word-wrap: break-word;\"> <tbody style=\"margin: 0; padding: 0;\"> <tr style=\"background-color: #393D49; height: 60px; margin: 0; padding: 0;\"> <td style=\"margin: 0; padding: 0;\"> <div style=\"color: #5EB576; margin: 0; margin-left: 30px; padding: 0;\"><a style=\"font-size: 14px; margin: 0; padding: 0; color: #5EB576; " +
                "text-decoration: none;\" href=\"http://www.flyfun.site/\" target=\"_blank\">flyfun??????</a></div> </td> </tr> " +
                "<tr style=\"margin: 0; padding: 0;\"> <td style=\"margin: 0; padding: 30px;\"> <p style=\"line-height: 20px; margin: 0; margin-bottom: 10px; padding: 0;\"> " +
                "?????????<em style=\"font-weight: 700;\">" + nickname + "</em>??? </p> <p style=\"line-height: 2; margin: 0; margin-bottom: 10px; padding: 0;\"> ??????30?????????????????????????????? </p> " +
                "<div style=\"\"> <a href=\""+CommonConstant.DOMAIN_URL+"/u/forget?key="+key+"\" style=\"background-color: #009E94; color: #fff; display: inline-block; height: 32px; line-height: 32px; margin: 0 15px 0 0; padding: 0 15px; text-decoration: none;\" target=\"_blank\">??????????????????</a> </div> <p style=\"line-height: 20px; margin-top: 20px; padding: 10px; background-color: #f2f2f2; font-size: 12px;\"> ???????????????????????????????????????????????????????????????</p> </td> </tr> <tr style=\"background-color: #fafafa; color: #999; height: 35px; margin: 0; padding: 0; text-align: center;\"> <td style=\"margin: 0; padding: 0;\">????????????????????????????????????</td> </tr> </tbody> </table>" +
                "<style type=\"text/css\">" +
                "body{font-size:14px;font-family:arial,verdana,sans-serif;line-height:1.666;padding:0;margin:0;overflow:auto;white-space:normal;word-wrap:break-word;min-height:100px}" +
                "td, input, button, select, body{font-family:Helvetica, 'Microsoft Yahei', verdana}" +
                "pre {white-space:pre-wrap;white-space:-moz-pre-wrap;white-space:-pre-wrap;white-space:-o-pre-wrap;word-wrap:break-word;width:95%}" +
                "th,td{font-family:arial,verdana,sans-serif;line-height:1.666}" +
                "img{ border:0}" +
                "header,footer,section,aside,article,nav,hgroup,figure,figcaption{display:block}" +
                "blockquote{margin-right:0px}" +
                "</style>" +
                "<style id=\"ntes_link_color\" type=\"text/css\">a,td a{color:#064977}</style>" +
                "</body></html>";
    }
}
