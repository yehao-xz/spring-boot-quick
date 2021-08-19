package quick.dynamic.datasource.mapper;

import org.apache.ibatis.annotations.Param;
import quick.dynamic.datasource.interfaces.TargetDataSource;
import quick.dynamic.datasource.model.User;

import java.util.List;

/**
 * UserMapper
 *
 * @author yehao
 * @date 2021/8/19
 */
public interface UserMapper {

    @TargetDataSource(dataSourceName = "ds1")
    User findById(@Param("id") Long id);

    @TargetDataSource(dataSourceName = "ds2")
    List<User> findAllUser();

    void ageAddOne(@Param("id") Long id);
}
