package edu.sjsu.cmpe.cache.client;

import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

public class Client {

    public static void main(String[] args) throws Exception {
        System.out.println("Starting Cache Client...");
        CacheServiceInterface cache;
        String bucket;
        Funnel<CharSequence> strFunnel = Funnels.stringFunnel();

        String dataValues[] = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};

        Collection<String> nodes = new ArrayList<String>();
        nodes.add("3000");
        nodes.add("3001");
        nodes.add("3002");

        HashFunction hashFunction = Hashing.md5();

        // Run only one type of hashing at one time as data stored by both types of
        // hashing is the same and server nodes are also same. So, for clear
        // understanding of distinction between both types of hashing,
        // recommended to run one type of hashing only at a time

        // Consistent Hashing
//        MyConsistentHash consistentHash = new MyConsistentHash(
//            hashFunction, 1, nodes
//        );
//
//        for(int i=1; i<= dataValues.length; i++){
//            bucket = consistentHash.get(Integer.toString(i));
//
//            System.out.println("Bucket no is "+ bucket);
//            cache = new DistributedCacheService(
//                    "http://localhost:"+bucket);
//
//            cache.put(Long.parseLong(Integer.toString(i)), dataValues[i-1]);
//            System.out.println("put("+Integer.toString(i)+" => "+ dataValues[i-1]+")");
//
//            String value = cache.get(Long.parseLong(Integer.toString(i)));
//            System.out.println("get("+Integer.toString(i)+") => " + value);
//
//            System.out.println("Existing Cache Client...");
//        }


        //Rendezvous Hashing
       MyRendezvousHashing myRendezvousHashing = new MyRendezvousHashing(
               hashFunction, strFunnel, strFunnel, nodes
       );

        for(int i=1; i<= dataValues.length; i++){
            bucket = myRendezvousHashing.get(Integer.toString(i));

            System.out.println("Bucket no is "+ bucket);
            cache = new DistributedCacheService(
                    "http://localhost:"+bucket);

            cache.put(Long.parseLong(Integer.toString(i)), dataValues[i-1]);
            System.out.println("put("+Integer.toString(i)+" => "+ dataValues[i-1]+")");

            String value = cache.get(Long.parseLong(Integer.toString(i)));
            System.out.println("get("+Integer.toString(i)+") => " + value);

            System.out.println("Existing Cache Client...");
        }

    }


}
