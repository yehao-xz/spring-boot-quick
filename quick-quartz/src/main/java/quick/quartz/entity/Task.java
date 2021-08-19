package quick.quartz.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务实体类
 *
 * @author yehao
 * @date 2021/8/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务类
     */
    private String jobClass;

    /**
     * cron表达式
     */
    private String cronExpression;

    /**
     * 是否启用 0暂停 1启用
     */
    private Integer isEnable;

    /**
     * 执行时间
     */
    private Date jobTime;

    /**
     * 任务类别
     */
    private String type;

    /**
     * 说明
     */
    private String remark;

    /**
     * 创建时间
     */
    private String createdAt;

    /**
     * 修改时间
     */
    private String updatedAt;


    public Task(String jobClass, String cronExpression) {
        this.jobClass = jobClass;
        this.cronExpression = cronExpression;
    }
}
