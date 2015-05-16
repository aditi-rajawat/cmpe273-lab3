package edu.sjsu.cmpe.cache.client;

import java.util.Collection;
import java.util.concurrent.ConcurrentSkipListSet;

import com.google.common.hash.Funnel;
import com.google.common.hash.HashFunction;

/**
 * Created by aditi on 06/05/15.
 */
public class MyRendezvousHashing {

    private final HashFunction hasher;

    private final Funnel<CharSequence> keyFunnel;

    private final Funnel<CharSequence> nodeFunnel;

    private final ConcurrentSkipListSet<String> ordered;

    public MyRendezvousHashing(HashFunction hasher, Funnel<CharSequence> keyFunnel,
                               Funnel<CharSequence> nodeFunnel, Collection<String> init) {
        this.hasher = hasher;
        this.keyFunnel = keyFunnel;
        this.nodeFunnel = nodeFunnel;
        this.ordered = new ConcurrentSkipListSet<String>(init);
    }

    public boolean remove(String node) {
        return ordered.remove(node);
    }

    public boolean add(String node) {
        return ordered.add(node);
    }

    public String get(String key) {
        long maxValue = Long.MIN_VALUE;
        String max = null;
        for (String node : ordered) {
            long nodesHash = hasher.newHasher()
                    .putObject(key, keyFunnel)
                    .putObject(node, nodeFunnel)
                    .hash().asLong();
            if (nodesHash > maxValue) {
                max = node;
                maxValue = nodesHash;
            }
        }
        return max;
    }
}
