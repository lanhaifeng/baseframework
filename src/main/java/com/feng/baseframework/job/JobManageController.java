package com.feng.baseframework.job;

import com.feng.baseframework.constant.ResultEnum;
import com.feng.baseframework.exception.Assert;
import com.feng.baseframework.exception.BusinessException;
import com.feng.baseframework.model.DataResult;
import com.feng.baseframework.util.DataResultUtil;
import org.quartz.JobKey;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * 管理quartz的job控制器
 *
 * @author lanhaifeng
 * @version 1.0
 * @apiNote 时间:2022/11/26 0:30创建:JobManageController
 * @since 1.0
 */
@RestController
@RequestMapping("/jobManage")
public class JobManageController {
    private final SchedulerManager schedulerManager;

    public JobManageController(SchedulerManager schedulerManager) {
        this.schedulerManager = schedulerManager;
    }

    @PostMapping("activeJob")
    public DataResult<Boolean> activeJob(@RequestBody @NotNull SchedulerJob schedulerJob) {
        Assert.state(schedulerJob.validate(), BusinessException::new, ResultEnum.PARAM_ILLEGAL_ERROR);
        return DataResultUtil.success(schedulerManager.activeJob(schedulerJob));
    }

    @PostMapping("/pauseJob")
    public DataResult<Boolean> pauseJob(@RequestBody @NotNull JobKey jobKey){
        return DataResultUtil.success(schedulerManager.pauseJob(jobKey));
    }

    @PostMapping("/resumeJob")
    public DataResult<Boolean> resumeJob(@RequestBody @NotNull JobKey jobKey){
        return DataResultUtil.success(schedulerManager.resumeJob(jobKey));
    }

    @PostMapping("/deleteJob")
    public DataResult<Boolean> deleteJob(@RequestBody @NotNull JobKey jobKey){
        return DataResultUtil.success(schedulerManager.deleteJob(jobKey));
    }

}
