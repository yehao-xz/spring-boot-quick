package quick.dynamic.datasource.model;

import lombok.Data;

import java.io.Serializable;

/**
 * User
 *
 * @author yehao
 * @date 2021/8/19
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String password;

    private Integer isDelete;

    private String createdAt;

    private String updatedAt;

    private Integer age;
}
