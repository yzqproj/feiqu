package com.feiqu.quartz.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统的工作
 *
 * @author yanni
 * @date 2021/11/28
 */
@Data
public class SysJob implements Serializable {
    /**
     * 任务ID
     *
     * @mbg.generated
     */
    private Integer jobId;

    /**
     * 任务名称
     *
     * @mbg.generated
     */
    private String jobName;

    /**
     * 任务组名
     *
     * @mbg.generated
     */
    private String jobGroup;

    /**
     * 任务方法
     *
     * @mbg.generated
     */
    private String methodName;

    /**
     * 方法参数
     *
     * @mbg.generated
     */
    private String methodParams;

    /**
     * cron执行表达式
     *
     * @mbg.generated
     */
    private String cronExpression;

    /**
     * 计划执行错误策略（1立即执行 2执行一次 3放弃执行）
     *
     * @mbg.generated
     */
    private String misfirePolicy;

    /**
     * 状态（0正常 1暂停）
     *
     * @mbg.generated
     */
    private String status;

    /**
     * 创建者
     *
     * @mbg.generated
     */
    private String createBy;

    /**
     * 创建时间
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * 更新者
     *
     * @mbg.generated
     */
    private String updateBy;

    /**
     * 更新时间
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * 备注信息
     *
     * @mbg.generated
     */
    private String remark;

    /**
     * 是否并发执行（0允许 1禁止）
     *
     * @mbg.generated
     */
    private String concurrent;

    @Serial
    private static final long serialVersionUID = 1L;


    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SysJob other = (SysJob) that;
        return (this.getJobId() == null ? other.getJobId() == null : this.getJobId().equals(other.getJobId()))
            && (this.getJobName() == null ? other.getJobName() == null : this.getJobName().equals(other.getJobName()))
            && (this.getJobGroup() == null ? other.getJobGroup() == null : this.getJobGroup().equals(other.getJobGroup()))
            && (this.getMethodName() == null ? other.getMethodName() == null : this.getMethodName().equals(other.getMethodName()))
            && (this.getMethodParams() == null ? other.getMethodParams() == null : this.getMethodParams().equals(other.getMethodParams()))
            && (this.getCronExpression() == null ? other.getCronExpression() == null : this.getCronExpression().equals(other.getCronExpression()))
            && (this.getMisfirePolicy() == null ? other.getMisfirePolicy() == null : this.getMisfirePolicy().equals(other.getMisfirePolicy()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getConcurrent() == null ? other.getConcurrent() == null : this.getConcurrent().equals(other.getConcurrent()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getJobId() == null) ? 0 : getJobId().hashCode());
        result = prime * result + ((getJobName() == null) ? 0 : getJobName().hashCode());
        result = prime * result + ((getJobGroup() == null) ? 0 : getJobGroup().hashCode());
        result = prime * result + ((getMethodName() == null) ? 0 : getMethodName().hashCode());
        result = prime * result + ((getMethodParams() == null) ? 0 : getMethodParams().hashCode());
        result = prime * result + ((getCronExpression() == null) ? 0 : getCronExpression().hashCode());
        result = prime * result + ((getMisfirePolicy() == null) ? 0 : getMisfirePolicy().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getConcurrent() == null) ? 0 : getConcurrent().hashCode());
        return result;
    }
}