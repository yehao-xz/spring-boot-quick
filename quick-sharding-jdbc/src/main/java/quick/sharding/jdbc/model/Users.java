package quick.sharding.jdbc.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户类
 *
 * @author yehao
 * @date 2021/8/19
 */
@Data
@TableName("user")
public class Users {

    @TableId
    private Long id;

    private String name;

    private String password;

    private Integer isDelete;

    private String createdAt;

    private String updatedAt;

    private Integer age;

}
