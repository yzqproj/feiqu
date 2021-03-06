package com.feiqu.web.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.util.CommonUtils;
import com.feiqu.framwork.util.JedisUtil;
import com.feiqu.framwork.util.WebUtil;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.FqUser;
import com.feiqu.system.model.FqUserExample;
import com.feiqu.system.model.UploadImgRecord;
import com.feiqu.system.model.UploadImgRecordExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.KeyValue;
import com.feiqu.system.service.FqUserService;
import com.feiqu.system.service.UploadImgRecordService;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.jeesuite.filesystem.FileSystemClient;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.commands.JedisCommands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2017/10/23.
 */
@Controller
@RequestMapping("api")
public class CommonController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

    /*@Resource
    FSProviderSpringFacade provider;*/
    @Resource
    private FqUserService fqUserService;
    @Resource
    private WebUtil webUtil;
    @Resource
    private UploadImgRecordService uploadImgRecordService;

    @ResponseBody
    @GetMapping("getRegionByIp")
    public String getRegionByIp(String ip){

        return CommonUtils.getRegionByIp(ip)+"-------"+CommonUtils.getFullRegionByIp(ip);
    }

    /**
     * ????????????????????? ?????? ????????????
     * @param request
     * @param response
     * @param file
     * @param picNum ???????????????????????????
     * @return
     */
    @PostMapping("/upload")
    @ResponseBody
    public Object upload(HttpServletRequest request, HttpServletResponse response, MultipartFile file,
                         @RequestParam(required = false) Integer picNum){
        BaseResult result = new BaseResult();
        File localFile = null;
        String fileName = "";
        try {
            if(picNum != null && picNum >= 9){
                result.setResult(ResultEnum.SYSTEM_ERROR);
                result.setMessage("??????????????????????????????9??????");
                return result;
            }
            FqUserCache fqUser = webUtil.currentUser(request,response);
            if(fqUser == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            long size = file.getSize();
            if(size >  10000 * 1024){
                result.setResult(ResultEnum.FILE_TOO_LARGE);
                result.setMessage("??????????????????????????????10M");
                return result;
            }
            Date now = new Date();
            String time = DateFormatUtils.format(now,"yyyy-MM-dd");
            String path = CommonConstant.FILE_UPLOAD_TEMP_PATH+File.separator+time;
            File fileParent = new File(path);
            if(!fileParent.exists()){
                fileParent.mkdir();
            }
            String picUrl;
            String extName = FileUtil.extName(file.getOriginalFilename()).toLowerCase();
            List<String> picExtList = CommonConstant.picExtList;
            if(!picExtList.contains(extName)){
                result.setResult(ResultEnum.PIC_URL_NOT_RIGHT);
                return result;
            }
            String fileNameFormat = DateUtil.format(now,"yyyyMMddHHmmss")+RandomUtil.randomNumbers(2);
            fileName = CommonConstant.FILE_NAME_PREFIX+ fileNameFormat+"."+extName;
            localFile = new File(fileParent,fileName);
            //MultipartFile?????????????????????
            file.transferTo(localFile);
            //????????????300kb ??????????????? ???
            if(size >  300 * 1024){
                Thumbnails.of(localFile).scale(1).outputQuality(0.3).toFile(localFile);
            }
            String picMd5 = DigestUtil.md5Hex(localFile);
            //??????md5?????????????????????
            UploadImgRecordExample imgRecordExample = new UploadImgRecordExample();
            imgRecordExample.createCriteria().andPicMd5EqualTo(picMd5).andPicSizeEqualTo(size);
            UploadImgRecord uploadImgRecord = uploadImgRecordService.selectFirstByExample(imgRecordExample);
            if(uploadImgRecord != null && StringUtils.isNotBlank(uploadImgRecord.getPicUrl())){
                picUrl = uploadImgRecord.getPicUrl();
            }else {
                picUrl = FileSystemClient.getPublicClient().upload(fileName, localFile);
                picUrl+=CommonConstant.QINIU_PIC_STYLE_NAME;
                /*CommonConstant.aliossFsProvider.upload(new UploadObject(localFile));
                picUrl = CommonConstant.ALIOSS_URL_PREFIX+"/"+fileName;*/
//                picUrl = CommonConstant.DOMAIN_URL+"upload/"+time+"/"+fileName;
                uploadImgRecord = new UploadImgRecord(picUrl,picMd5, now, WebUtil.getIP(request),fqUser.getId(), size);
                uploadImgRecordService.insert(uploadImgRecord);
            }
            result.setResult(ResultEnum.SUCCESS);
            result.setData(picUrl);
            logger.info("??????{}:????????????????????????{}",fqUser.getNickname(),picUrl);
        } catch (Exception e) {
            logger.error("??????????????????",e);
            result.setResult(ResultEnum.SYSTEM_ERROR);
        }finally {
            if (localFile != null) {
                boolean delete = localFile.delete();
                if(!delete){
                    logger.error("????????????????????????,????????????{}",fileName);
                }
            }
        }
        return result;
    }

    @PostMapping("uploadVideo")
    @ResponseBody
    public Object uploadVideo(HttpServletRequest request, HttpServletResponse response, MultipartFile file){
        BaseResult result = new BaseResult();
        String fileName = "";
        File localFile = null;
        try {
            FqUserCache fqUser = webUtil.currentUser(request,response);
            if(fqUser == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if(fqUser.getLevel() < 3){
                result.setResult(ResultEnum.PARAM_ERROR);
                result.setMessage("??????????????????3??????");
                return result;
            }
            long size = file.getSize();
            if(size >  20 * 1024 * 1024){
                result.setResult(ResultEnum.FILE_TOO_LARGE);
                result.setMessage("????????????????????????????????????20M");
                return result;
            }
            Date now = new Date();
            String time = DateFormatUtils.format(now,"yyyy/MM/dd");
            String path = request.getSession().getServletContext().getRealPath("upload")+File.separator+time;
            String videoUrl;
            String extName = FileUtil.extName(file.getOriginalFilename());
            fileName = CommonConstant.FILE_NAME_PREFIX+ DateUtil.format(new Date(),"yyyyMMddHHmmss")+"."+extName;
            localFile = new File(path,fileName);
            if(!localFile.exists()){
                localFile.mkdirs();
            }
            //MultipartFile?????????????????????
//            file.transferTo(localFile);

            String videoMd5 = DigestUtil.md5Hex(file.getBytes());
            //??????md5?????????????????????
            UploadImgRecordExample imgRecordExample = new UploadImgRecordExample();
            imgRecordExample.createCriteria().andPicMd5EqualTo(videoMd5).andPicSizeEqualTo(size);
            UploadImgRecord uploadImgRecord = uploadImgRecordService.selectFirstByExample(imgRecordExample);
            if(uploadImgRecord != null && StringUtils.isNotBlank(uploadImgRecord.getPicUrl())){
                videoUrl = uploadImgRecord.getPicUrl();
            }else {
                videoUrl = FileSystemClient.getClient("aliyun").upload("video/"+fileName,localFile);
//                aliyunOssClient.putObject(CommonConstant.ALIYUN_OSS_BUCKET_NAME,"video/"+fileName,file.getInputStream());
//                videoUrl = CommonConstant.ALIOSS_URL_PREFIX+"/video/"+fileName;
                uploadImgRecord = new UploadImgRecord(videoUrl,videoMd5,new Date(), WebUtil.getIP(request),fqUser.getId(), size);
                uploadImgRecordService.insert(uploadImgRecord);
            }
            result.setResult(ResultEnum.SUCCESS);
            result.setData(videoUrl);
        } catch (IOException e) {
            logger.error("??????????????????",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @PostMapping("findActiveUserNames")
    @ResponseBody
    public Object findActiveUserNames(){
        BaseResult result = new BaseResult();
        try {int month = DateUtil.thisMonth()+1;
            JedisCommands commands = JedisUtil.me();
            String key = "activeUserNames"+month;
            Set<String> activeNames = commands.smembers(key);
            if(CollectionUtil.isEmpty(activeNames)){
                Set<String> userIds =commands.zrevrange(CommonConstant.FQ_ACTIVE_USER_SORT+month,0,9);
                if(CollectionUtils.isNotEmpty(userIds)){
                    List<Integer> userIdList = Lists.newArrayList();
                    for(String userId : userIds){
                        userIdList.add(Integer.valueOf(userId));
                    }
                    FqUserExample example = new FqUserExample();
                    example.createCriteria().andIdIn(userIdList);
                    List<FqUser> fqUsers = fqUserService.selectByExample(example);
                    List<String> names = fqUsers.stream().map(FqUser::getNickname).collect(Collectors.toList());
                    result.setData(names);
                    commands.sadd(key,names.toArray(new String[names.size()]));
                    commands.expire(key,7*24*60*60);
                }
            }else {
                result.setData(activeNames);
            }
        } catch (Exception e) {
            logger.error("??????????????????????????????",e);
        } finally {
             
        }
        return result;
    }

    @PostMapping("findUsersByName")
    @ResponseBody
    public Object findUsersByName(String username){
        BaseResult result = new BaseResult();
        try {
            if(StringUtils.isEmpty(username)){
                return result;
            }
            List<KeyValue> simUser = Lists.newArrayList();
            FqUserExample example = new FqUserExample();
            FqUserExample.Criteria criteria1=  example.createCriteria();
            criteria1.andUsernameLike("%"+username+"%");
            FqUserExample.Criteria criteria2=  example.createCriteria();
            criteria2.andNicknameLike("%"+username+"%");
            example.or(criteria2);
            PageHelper.startPage(1,10,false);
            List<FqUser> fqUsers = fqUserService.selectByExample(example);
            if(CollectionUtils.isNotEmpty(fqUsers)){
                fqUsers.forEach(fqUser -> {
                    KeyValue keyValue = new KeyValue();
                    keyValue.setKey(fqUser.getId().toString());
                    keyValue.setValue(fqUser.getUsername()+"("+fqUser.getNickname()+")");
                    simUser.add(keyValue);
                });
            }
            result.setData(simUser);
        } catch (Exception e) {
            logger.error("????????????????????????",e);
        } finally {
             
        }
        return result;
    }
}
