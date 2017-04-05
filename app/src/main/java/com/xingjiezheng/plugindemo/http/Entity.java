package com.xingjiezheng.plugindemo.http;

import android.text.TextUtils;

import org.json.JSONObject;

import java.io.Serializable;

public abstract class Entity implements Serializable {

    /**
     * 数据实体构造器, 提供实例化方法.
     *
     * @param <T> 具体的子类数据实体对象
     * @author DengZhaoyong
     * @version 1.0.0
     * @date 2012-9-13
     */
    public interface Builder<T extends Entity> {
        /**
         * 根据JSON数据创建实体对象.
         *
         * @param jsonObject 创建实体的JSON数据
         * @return
         */
        public T create(JSONObject jsonObject);
    }


    /**
     * 是否是同一个对象的判断条件
     */
    public abstract String[] uniqueKey();

    @Override
    public boolean equals(Object o) {
        String[] uniqueKey = uniqueKey();
        if (uniqueKey != null && uniqueKey.length > 0) {
            Entity user = (Entity) o;
            String[] modelKey = user.uniqueKey();
            for (int i = 0; i < uniqueKey.length; i++) {
                String unique = uniqueKey[i];
                if (TextUtils.isEmpty(unique) || !unique.equals(modelKey[i])) {
                    return super.equals(o);
                }
            }
            return true;
        } else {
            return super.equals(o);
        }

    }

    @Override
    public int hashCode() {
        String[] uniqueKey = uniqueKey();
        if (uniqueKey != null && uniqueKey.length > 0) {
            int result = 0;
            for (int i = 0; i < uniqueKey.length; i++) {
                String unique = uniqueKey[i];
                if (!TextUtils.isEmpty(unique)) {
                    result = 31 * result + unique.hashCode();
                }
            }
            if (result == 0) {
                return super.hashCode();
            } else {
                return result;
            }
        } else {
            return super.hashCode();
        }
    }

}
