package com.feiqu.quartz.service.impl;


import cn.hutool.core.convert.Convert;
import com.feiqu.common.constant.ScheduleConstants;
import com.feiqu.common.exception.job.TaskException;
import com.feiqu.quartz.mapper.SysJobMapper;
import com.feiqu.quartz.model.SysJob;
import com.feiqu.quartz.model.SysJobExample;
import com.feiqu.quartz.service.SysJobService;
import com.feiqu.quartz.util.ScheduleUtils;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

/**
 * sys impl工作服务
 * SysJobService实现
 * @author yanni
 * @date 2021/11/28
 */
@Service
@Transactional(rollbackFor = Exception.class)
@BaseService
public class SysJobServiceImpl extends BaseServiceImpl<SysJobMapper, SysJob, SysJobExample> implements SysJobService {


    @Resource
    private Scheduler scheduler;

    @Resource
    SysJobMapper sysJobMapper;

    /**
     * 项目启动时，初始化定时器
     */
    @PostConstruct
    public void init() throws TaskException, SchedulerException {
        List<SysJob> jobList = sysJobMapper.selectByExample(new SysJobExample());
        for (SysJob job : jobList) {
            CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, job.getJobId());
            // 如果不存在，则创建
            if (cronTrigger == null) {
                ScheduleUtils.createScheduleJob(scheduler, job);
            } else {
                ScheduleUtils.updateScheduleJob(scheduler, job);
            }
        }
    }

    @Override
    public boolean checkCronExpressionIsValid(String cronExpression) {
        try {
            new CronExpression(cronExpression);
            return true;
        } catch (ParseException var2) {
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(SysJob job) {
        try {
            ScheduleUtils.run(scheduler, selectByPrimaryKey(job.getJobId()));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteJobByIds(String ids) throws SchedulerException {
        Integer[] jobIds = Convert.toIntArray(ids);
        for (Integer jobId : jobIds) {
            SysJob job = sysJobMapper.selectByPrimaryKey(jobId);
            sysJobMapper.deleteByPrimaryKey(jobId);
            ScheduleUtils.deleteScheduleJob(scheduler, job.getJobId());
        }
    }

    @Override
    public void changeStatus(String ids, String status) throws SchedulerException {
        Integer[] jobIds = Convert.toIntArray(ids);
        if (ScheduleConstants.Status.NORMAL.getValue().equals(status)) {
            for (Integer jobId : jobIds) {
                SysJob job = new SysJob();
                job.setJobId(jobId);
                job.setStatus(status);
                sysJobMapper.updateByPrimaryKeySelective(job);
                ScheduleUtils.resumeJob(scheduler, job.getJobId());
            }
        } else if (ScheduleConstants.Status.PAUSE.getValue().equals(status)) {
            for (Integer jobId : jobIds) {
                SysJob job = new SysJob();
                job.setJobId(jobId);
                job.setStatus(status);
                sysJobMapper.updateByPrimaryKeySelective(job);
                ScheduleUtils.pauseJob(scheduler, job.getJobId());
            }
        }
    }
}