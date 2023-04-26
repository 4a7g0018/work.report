package com.yan.work.report.utils;

import java.util.Optional;

public interface ICrudUtil<T> {
    Optional<T> select();
    Boolean insert(Object insertObj);
    Boolean update(String key, Object updateObj);
    Boolean delete(Object key);
}
