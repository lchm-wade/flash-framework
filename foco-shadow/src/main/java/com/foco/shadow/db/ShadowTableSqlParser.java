package com.foco.shadow.db;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.SqlInfo;
import com.baomidou.mybatisplus.core.toolkit.TableNameParser;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.util.ArrayList;
import java.util.List;
/**
 * @author zachary
 * @version 1.0.0
 * @description 影子表sql解析器
 * @date 2021/11/1
 */
@Slf4j
@Setter
@Getter
public class ShadowTableSqlParser implements ISqlParser {
    private IShadowTableNameHandler shadowTableNameHandler;

    public ShadowTableSqlParser(IShadowTableNameHandler tableNameHandler) {
        this.shadowTableNameHandler = tableNameHandler;
    }

    @Override
    public SqlInfo parser(MetaObject metaObject, String sql) {
        TableNameParser parser = new TableNameParser(sql);
        List<TableNameParser.SqlToken> names = new ArrayList<>();
        parser.accept(names::add);
        StringBuilder builder = new StringBuilder();
        int last = 0;
        for (TableNameParser.SqlToken name : names) {
            int start = name.getStart();
            if (start != last) {
                builder.append(sql, last, start);
                String value = name.getValue();
                if (shadowTableNameHandler != null) {

                    builder.append(shadowTableNameHandler.genShadowTableName(metaObject, sql, value));
                } else {
                    builder.append(value);
                }
            }
            last = name.getEnd();
        }
        if (last != sql.length()) {
            builder.append(sql.substring(last));
        }
        return SqlInfo.of(builder.toString());
    }
}
