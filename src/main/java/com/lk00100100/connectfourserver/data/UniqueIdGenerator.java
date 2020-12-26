package com.lk00100100.connectfourserver.data;

import java.util.concurrent.atomic.AtomicInteger;

class UniqueIdGenerator {

    //todo: for now, work with one computer. replace later

    private AtomicInteger currentId;

    UniqueIdGenerator() {
        currentId = new AtomicInteger(0);
    }

    int getNewId() {
        return currentId.incrementAndGet();
    }
}
