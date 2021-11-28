package com.feiqu.system.service.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.JobTalkMapper;
import com.feiqu.system.model.JobTalk;
import com.feiqu.system.model.JobTalkExample;
import com.feiqu.system.pojo.response.JobTalkUserDetail;
import com.feiqu.system.service.JobTalkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* JobTalkService实现
* Created by cwd on 2017/10/28.
*/
@Service
@Transactional
@BaseService

public class JobTalkServiceImpl extends BaseServiceImpl<JobTalkMapper, JobTalk, JobTalkExample> implements JobTalkService {

    private static Logger _log = LoggerFactory.getLogger(JobTalkServiceImpl.class);

    @Resource
    JobTalkMapper jobTalkMapper;

    public List<JobTalkUserDetail> selectWithUserByExample(JobTalkExample example) {
        return jobTalkMapper.selectWithUserByExample(example);
    }
}