package edu.sjsu.cmpe.cache.repository;

import edu.sjsu.cmpe.cache.domain.Entry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by aditi on 10/05/15.
 */
public class ChronicleMapCache implements CacheInterface  {

    private final ConcurrentMap<Long, Entry> chronicleMap;

    public ChronicleMapCache(ConcurrentMap<Long, Entry> initMap){
        this.chronicleMap = initMap;
    }

    @Override
    public Entry save(Entry newEntry) {
        checkNotNull(newEntry, "newEntry instance must not be null");
        chronicleMap.put(newEntry.getKey(), newEntry);
        System.out.println("Entry saved in the cache!!");
        return newEntry;
    }

    @Override
    public Entry get(Long key) {
        checkArgument(key > 0,
                "Key was %s but expected greater than zero value", key);
        return chronicleMap.get(key);
    }

    @Override
    public List<Entry> getAll() {
        return new ArrayList<Entry>(chronicleMap.values());
    }
}
