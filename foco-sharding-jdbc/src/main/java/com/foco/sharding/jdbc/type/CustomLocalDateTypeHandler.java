package com.foco.sharding.jdbc.type;

import cn.hutool.core.util.StrUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author lucoo
 * @version 1.0.0
 * @Description TODO
 * @date 2021-07-14 11:40
 */
//定义转换器支持的JAVA类型
@MappedTypes(LocalDate.class)
//定义转换器支持的数据库类型
@MappedJdbcTypes(value = JdbcType.DATE, includeNullJdbcType = true)
public class CustomLocalDateTypeHandler extends BaseTypeHandler<LocalDate> {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDate parameter, JdbcType jdbcType)
            throws SQLException {
        if (parameter != null) {
            ps.setString(i, dateTimeFormatter.format(parameter));
        }
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String target = rs.getString(columnName);
        if (StrUtil.isEmpty(target)) {
            return null;
        }
        return LocalDate.parse(target, dateTimeFormatter);
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String target = rs.getString(columnIndex);
        if (StrUtil.isEmpty(target)) {
            return null;
        }
        return LocalDate.parse(target, dateTimeFormatter);
    }

    @Override
    public LocalDate getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String target = cs.getString(columnIndex);
        if (StrUtil.isEmpty(target)) {
            return null;
        }
        return LocalDate.parse(target, dateTimeFormatter);
    }
}
