package ly.utils;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 这个mapper不能被扫描到
 * @param <T>
 */
public interface MyMapper<T> extends Mapper<T> , MySqlMapper<T> {
}
