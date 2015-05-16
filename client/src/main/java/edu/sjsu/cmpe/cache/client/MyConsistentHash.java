package edu.sjsu.cmpe.cache.client;

/**
 * Created by aditi on 06/05/15.
 */
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

public class MyConsistentHash {

    private final HashFunction hashFunction;
    private final int numberOfReplicas;
    private final SortedMap<Integer, String> circle =
        new TreeMap<Integer, String>();

    public MyConsistentHash(HashFunction hashFunction,
                          int numberOfReplicas, Collection<String> nodes) {

        this.hashFunction = hashFunction;
        this.numberOfReplicas = numberOfReplicas;

        for (String node : nodes) {
            add(node);
        }
    }

    public void add(String node) {
        HashCode hashCode;
        Integer mapKey;

        for (int i = 0; i < numberOfReplicas; i++) {
            hashCode = hashFunction.hashString(node.toString() + i);
            mapKey = new Integer(hashCode.asInt());
            circle.put(mapKey,
                    node);
        }
    }

    public void remove(String node) {
        HashCode hashCode;
        Integer mapKey;

        for (int i = 0; i < numberOfReplicas; i++) {
            hashCode = hashFunction.hashString(node.toString() + i);
            mapKey = new Integer(hashCode.asInt());

            circle.remove(mapKey);
        }
    }

    public String get(String key) {
        if (circle.isEmpty()) {
            return null;
        }
        HashCode hashcode = hashFunction.hashString(key);
        Integer mapKey = hashcode.asInt();
        if (!circle.containsKey(mapKey)) {
            SortedMap<Integer, String> tailMap =
                    circle.tailMap(mapKey);
            mapKey = tailMap.isEmpty() ?
                    circle.firstKey() : tailMap.firstKey();
        }
        return circle.get(mapKey);
    }

}
