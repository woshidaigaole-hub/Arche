package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mapper 接口 = 操作数据库的"手"
 * 继承 BaseMapper<User> 后，MyBatis-Plus 会自动提供：
 *   insert(user)  /  deleteById(id)  /  updateById(user)  /  selectById(id)  /  selectList(条件)
 * 不需要写一行 SQL！（除非复杂查询）
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
