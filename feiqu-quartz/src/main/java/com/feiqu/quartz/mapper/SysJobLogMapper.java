package com.feiqu.quartz.mapper;

import com.feiqu.quartz.model.SysJobLog;
import com.feiqu.quartz.model.SysJobLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysJobLogMapper {
    /**
     * 计算实例
     *
     * @param example 例子
     * @return long
     */
    long countByExample(SysJobLogExample example);

    /**
     * 删除实例
     *
     * @param example 例子
     * @return int
     */
    int deleteByExample(SysJobLogExample example);

    /**
     * 按主键删除
     *
     * @param jobLogId 工作日志id
     * @return int
     */
    int deleteByPrimaryKey(Integer jobLogId);

    /**
     * 插入
     *
     * @param record 记录
     * @return int
     */
    int insert(SysJobLog record);

    /**
     * 插入选择性
     *
     * @param record 记录
     * @return int
     */
    int insertSelective(SysJobLog record);

    /**
     * 选择的例子
     *
     * @param example 例子
     * @return {@link List}<{@link SysJobLog}>
     */
    List<SysJobLog> selectByExample(SysJobLogExample example);

    SysJobLog selectByPrimaryKey(Integer jobLogId);

    int updateByExampleSelective(@Param("record") SysJobLog record, @Param("example") SysJobLogExample example);

    int updateByExample(@Param("record") SysJobLog record, @Param("example") SysJobLogExample example);

    int updateByPrimaryKeySelective(SysJobLog record);

    int updateByPrimaryKey(SysJobLog record);
}