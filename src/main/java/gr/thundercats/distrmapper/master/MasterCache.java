package gr.thundercats.distrmapper.master;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;


public class MasterCache<T> {

    private final int size;
    private Queue<Integer> cachedHashes;
    private HashMap<Integer, T> cachedData;

    public MasterCache(int size) {
        this.size = size;
        this.cachedHashes = new ArrayDeque<>();
        this.cachedData = new HashMap<>();
    }

    public void cachePush(T object, int hash) {

        if (!cachedHashes.contains(hash))
            cachedHashes.add(hash);
        maintainCacheSize();

        cachedData.put(hash, object);
    }

    public T cacheRequest(int hash) {
        return cachedData.get(hash);
    }

    private void maintainCacheSize() {
        while (cachedHashes.size() > size) {
            cachedData.remove(cachedHashes.poll());
        }
    }
}