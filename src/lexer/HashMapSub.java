package lexer;

import java.util.*;

/**
 * Created by hzg on 2016/11/21.
 */
//用LinkedHashMap而不用HashMap是因为LinkedHashMap会按顺序put
//用IdentityHashMap可以重复put
public class HashMapSub<K, V> extends LinkedHashMap {
    @Override
    public String toString() {
        Iterator<Map.Entry<K, V>> i = entrySet().iterator();
        if (!i.hasNext())
            return "{}";

        StringBuilder sb = new StringBuilder();
        for (; ; ) {
            Map.Entry<K, V> e = i.next();
            K key = e.getKey();
            V value = e.getValue();
            sb.append(key == this ? "(this Map)" : key);
            sb.append("\t\t\t\t\t");
            sb.append(value == this ? "(this Map)" : value);
            if (!i.hasNext())
                return sb.toString();
            sb.append("\r\n").append(' ');
        }
    }
}
