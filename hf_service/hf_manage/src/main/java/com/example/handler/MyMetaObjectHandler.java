package com.example.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.example.aop.UserContext;
import com.example.util.LocalDateTimeUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * @ClassName MyMetaObjectHandler
 * @Author sheng.lin
 * @Date 2025/7/22
 * @Version 1.0
 * @修改记录
 **/
/*
 * projectName: healthyFood
 * copyright(c) ©2003-2024 Talebase. All Rights Reserved.
 */

/**
 * MetaObjectHandler 接口的主要作用是自动填充实体类中的公共字段，例如创建时间、更新时间、创建人、修改人等。当执行插入（Insert）或更新（Update）操作时，
 * MyBatis-Plus 会自动调用实现了该接口的类，为实体类中的指定字段赋予默认值，从而避免在每个业务方法中手动设置这些公共字段。
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {



    /**
     * mybatis-plus在进行insert时会去维护
     * @param metaObject
     * @Author sheng.lin
     * @Date 2025/7/22
     * @return: void
     * @Version  1.0
     * @修改记录
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "creatorId", Integer.class, UserContext.getLocalUserId());
        this.strictInsertFill(metaObject, "createTime", Long.class, LocalDateTimeUtils.toMillis(LocalDateTimeUtils.now()));
    }

    /**
     * mybatis-plus在进行update时会去维护
     * @param metaObject
     * @Author sheng.lin
     * @Date 2025/7/22
     * @return: void
     * @Version  1.0
     * @修改记录
     */
    @Override
    public void updateFill(MetaObject metaObject) {
//        this.strictUpdateFill(metaObject, "updaterId", Long.class, UserContext.getUserId());
//        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}

