package com.feiqu.quartz.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统的工作日志
 *
 * @author yanni
 * @date 2021/11/28
 */
@Data
public class SysJobLog implements Serializable {
    /**
     * 任务日志ID
     *
     * @mbg.generated
     */
    private Integer jobLogId;

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
     * 日志信息
     *
     * @mbg.generated
     */
    private String jobMessage;

    /**
     * 执行状态（0正常 1失败）
     *
     * @mbg.generated
     */
    private String status;

    /**
     * 异常信息
     *
     * @mbg.generated
     */
    private String exceptionInfo;

    /**
     * 创建时间
     *
     * @mbg.generated
     */
    private Date createTime;

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
        SysJobLog other = (SysJobLog) that;
        return (this.getJobLogId() == null ? other.getJobLogId() == null : this.getJobLogId().equals(other.getJobLogId()))
            && (this.getJobName() == null ? other.getJobName() == null : this.getJobName().equals(other.getJobName()))
            && (this.getJobGroup() == null ? other.getJobGroup() == null : this.getJobGroup().equals(other.getJobGroup()))
            && (this.getMethodName() == null ? other.getMethodName() == null : this.getMethodName().equals(other.getMethodName()))
            && (this.getMethodParams() == null ? other.getMethodParams() == null : this.getMethodParams().equals(other.getMethodParams()))
            && (this.getJobMessage() == null ? other.getJobMessage() == null : this.getJobMessage().equals(other.getJobMessage()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getExceptionInfo() == null ? other.getExceptionInfo() == null : this.getExceptionInfo().equals(other.getExceptionInfo()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getJobLogId() == null) ? 0 : getJobLogId().hashCode());
        result = prime * result + ((getJobName() == null) ? 0 : getJobName().hashCode());
        result = prime * result + ((getJobGroup() == null) ? 0 : getJobGroup().hashCode());
        result = prime * result + ((getMethodName() == null) ? 0 : getMethodName().hashCode());
        result = prime * result + ((getMethodParams() == null) ? 0 : getMethodParams().hashCode());
        result = prime * result + ((getJobMessage() == null) ? 0 : getJobMessage().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getExceptionInfo() == null) ? 0 : getExceptionInfo().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }
}