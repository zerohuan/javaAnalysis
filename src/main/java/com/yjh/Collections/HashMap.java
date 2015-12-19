package com.yjh.Collections;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Created by yjh on 15-12-12.
 */
public class HashMap<K,V> extends AbstractMap<K,V>
        implements Map<K,V>, Cloneable, Serializable {
    /*
    重要值
     */
    //默认初始化容量，HashMap容量必须是2的幂次方
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16
    //最大容量不得超过1<<30
    static final int MAXIMUM_CAPACITY = 1 << 30;
    //默认装载因子，0.75是权衡空间和时间开销后考虑
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    //超过这个阈值将使用红黑树组织桶中的结点，而不是链表
    static final int TREEIFY_THRESHOLD = 8;
    static final int UNTREEIFY_THRESHOLD = 6;
    //只有表的大小超过这个阈值，桶才可以被转换成树而不是链表（为超过这个值时，应该使用resize）
    //这个值是TREEIFY_THRESHOLD的4倍，以便resizing和treeification之间产生冲突
    static final int MIN_TREEIFY_CAPACITY = 64;

    /*
    属性
     */
    //延迟加载，长度总为2的幂次方
    transient Node<K,V>[] table;
    //键值对缓存，它们的映射关系集合保存在entrySet中，即使Key在外部修改导致hashCode变化，缓存中还可以找到映射关系
    transient Set<Map.Entry<K,V>> entrySet;
    //键值对数量
    transient int size;
    //fail-fast
    transient int modCount;
    //下一次resize的阈值 (capacity * load factor)
    int threshold;
    //装载因子
    final float loadFactor;

    //视图
    transient volatile Set<K>        keySet;
    transient volatile Collection<V> values;

    /*
    构造器
     */
    //传入指定初始化容量，将计算好threshold的值，第一次放入元素时分配threshold大小的数组
    public HashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                    initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                    loadFactor);
        this.loadFactor = loadFactor;
        //此时table还未分配到内存，threshold就是将要分配的数组大小
        this.threshold = tableSizeFor(initialCapacity);
    }
    public HashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }
    public HashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
    }
    public HashMap(Map<? extends K, ? extends V> m) {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        putMapEntries(m, false);
    }

    /*
    核心辅助方法，内部表示实现
     */
    //找到最小大于cap的2的幂次方
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    //计算key的hash值，这里在hashCode基础上做了一次“高位向低位传播”
    //因为计算索引值是(cap - 1) & hash，当cap小于等于16时高位将无法起作用
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    //将指定Map中的所有键值对添加进来
    final void putMapEntries(Map<? extends K, ? extends V> m, boolean evict) {
        int s = m.size();
        if(s > 0) {
            //如果table为空，需要计算初始大小
            if(table == null) {
                //加1.0F是因为之后转型为int会省去小数部分
                float ft = ((float)s / loadFactor) + 1.0F;
                int t = (ft < (float)MAXIMUM_CAPACITY) ?
                        (int)ft : MAXIMUM_CAPACITY;
                if(t > threshold)
                    threshold = tableSizeFor(t);
            }
            //如果table不为空要，s大于原先的阈值，先进行一次扩展
            else if(s > threshold)
                resize();
            for(Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
                K key = e.getKey();
                V value = e.getValue();
                putVal(hash(key), key, value, false, evict);
            }
        }
    }

    //重新分配table：初始化或者扩展为原来2倍大小
    //这个方法包括了再散列的过程，由于HashMap的容量（table的大小）是二的幂次方膨胀增长的
    final Node<K,V>[] resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        //重新创建table数组
        //一，原数组不为空
        if(oldCap > 0) {
            //如果oldCap已经为最大容量
            if(oldCap >= MAXIMUM_CAPACITY) {
                threshold = MAXIMUM_CAPACITY;
                return oldTab;
            } else if((newCap = oldCap << 1) <= MAXIMUM_CAPACITY &&
                    oldCap > DEFAULT_INITIAL_CAPACITY)
                threshold = oldThr << 1; //增加阈值
        }
        //原数组为空，oldThr不为空，扩展为oldThr大小
        else if(oldThr > 0)
            newCap = oldThr;
        //原数组为空，oldThr为空，全部使用默认值
        else {
            //全部使用默认值
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_INITIAL_CAPACITY * loadFactor);
        }
        //检查设置新阈值
        if(newThr == 0) {
            float ft = (float)newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                    (int)ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        //Node[]不具备类型检查的能力，因此要通过强制类型转换
        //另外，不能创建参数化类型的数组
        @SuppressWarnings({"rawtypes","unchecked"})
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];

        table = newTab;
        if(oldTab != null) {
            //将就数组中的元素迁移到新数组当中
            for(int j = 0; j < oldCap; j++) {
                Node<K,V> e;
                if((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if(e.next == null)
                        //桶中只有一个元素，不可能是TreeNode直接放入新表的指定位置
                        newTab[e.hash & (newCap - 1)] = e;
//                    else if(e instanceof TreeNode) {
//
//                    }
                    else {
                        /*
                        桶中存在一个链表，需要将链表重新整理到新表当中，因为newCap是oldCap的两倍
                        所以原节点的索引值要么和原来一样，要么就是原(索引+oldCap)
                        和JDK 1.7中实现不同这里不存在rehash，直接使用原hash值
                        JDK 1.7中resize过程是在链表头插入，这里是在链表尾插入
                        */
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;
                        do {
                            if((e.hash & oldCap) == 0) {
                                //和原索引值一样
                                if(loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            } else {
                                if(hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = e.next) != null);
                        if(loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if(hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }

    /*
    添加，查找和删除无疑是最核心的操作
    （1）桶中有元素，首先检查第一个元素，因为树结构必须大于2个节点，再分类型检查；
    （2）注意使用hash比较，利用短路；
    （3）对于key为null情况，也是此方法统一处理，通过hash函数得到hash值是0；
     */
    //向散列表添加元素的主要方法
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        Node<K,V>[] tab; Node<K,V> p; int n, i;
        //如果是第一次添加元素
        if((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        //对应索引位置的桶是空的，直接创建新节点填入
        if((p = tab[(i = (n - 1) & hash)]) == null)
            tab[i] = newNode(hash, key, value, null);
        else {
            /*
            桶中已经有节点了，分情况处理：
            （1）该key已经在该桶中了，并且是第一个节点；
            （2）该桶是树结构的；
            （3）链表结构；
             */
            Node<K,V> e; K k;
            //首先检查第一个节点
            if(p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
            else if(p instanceof TreeNode) //putTreeVal会执行modCount++因此e赋值为它相当于在桶中原本就存在该key
                e = null;//e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            else {
                for(int binCount = 0; ; binCount++) {
                    //如果桶中没有该key的节点
                    if((e = p.next) == null) {
                        //注意此时e == null，因此会执行之后modCount++操作
                        p.next = newNode(hash, key, value, null);
                        //检查是否要转化为红黑树结构
                        if(binCount >= TREEIFY_THRESHOLD - 1)
                            ;
                        break;
                    }
                    if(e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            //如果键为key的节点在桶中，不需要
            if(e != null) {
                V oldValue = e.value;
                if(!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e);
                return oldValue;
            }
        }
        ++modCount;
        //添加后检查是否达到阈值，达到需要进行resize
        if (++size > threshold)
            resize();
        //指向回调，evict用来区别是否处于创建过程，false表示正在创建
        afterNodeInsertion(evict);
        return null;
    }

    //根据hash值和key查找节点
    final Node<K,V> getNode(int hash, Object key) {
        Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
        //table不为空，并且桶中存在节点
        if((tab = table) != null && (n = tab.length) > 0 &&
                (first = tab[(n - 1) & hash]) != null) {
            //总是检查第一个节点的原因：无论是树结构还是链表，都可以方便的检查第一个节点，树结构的节点数必然大于1
            //先检查hash，利用好短路特性
            if(first.hash == hash &&
                    ((k = first.key) == key || (key != null && key.equals(k))))
                return first;
            //多于一个节点，继续检查
            if((e = first.next) != null) {
                if(first instanceof TreeNode)
                    ;//return (TreeNode)first.getTreeNode(hast, key);
                do {
                    if(e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
    }
    //删除节点
    final Node<K,V> removeNode(int hash, Object key, Object value,
                               boolean matchValue, boolean movable) {
        Node<K,V>[] tab; Node<K,V> p; int n, index;
        if((tab = table) != null && (n = tab.length) > 0 &&
                (p = tab[index = (n - 1) & hash]) != null) {
            Node<K,V> node = null, e; K k; V v;
            if(p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k))))
                node = p;
            else if((e = p.next) != null) {
                if(p instanceof TreeNode)
                    ;//node = ((TreeNode<K,V>)p).getTreeNode(hash, key);
                else {
                    do {
                        if(e.hash == hash &&
                                ((k = e.key) == key || (key != null && key.equals(k)))) {
                            node = e;
                            break;
                        }
                        p = e;
                    } while ((e = e.next) != null);
                }
            }
            if(node != null && (!matchValue || (v = node.value) == value ||
                    (value != null && value.equals(v)))) {
                if (node instanceof TreeNode)
                    ;//((TreeNode<K,V>)node).removeTreeNode(this, tab, movable);
                else if (node == p) //当node是链表第一个节点
                    tab[index] = node.next;
                else
                    p.next = node.next;
                ++modCount;
                --size;
                afterNodeRemoval(node);
                return node;
            }
        }
        return null;
    }


    /*
    LinkedHashMap支持，LinkedHashMap可覆盖这些方法
     */
    //创建普通非树节点
    Node<K,V> newNode(int hash, K key, V value, Node<K,V> next) {
        return new Node<>(hash, key, value, next);
    }
    //将树结点转换为普通节点
    Node<K,V> replacementNode(Node<K,V> p, Node<K,V> next) {
        return new Node<>(p.hash, p.key, p.value, next);
    }

    // Callbacks to allow LinkedHashMap post-actions
    void afterNodeAccess(Node<K,V> p) { }
    void afterNodeInsertion(boolean evict) { }
    void afterNodeRemoval(Node<K,V> p) { }

    /*
    Map实现
     */
    @Override
    public V put(K key, V value) {
        return putVal(hash(key), key, value, false, false);
    }

    @Override
    public V get(Object key) {
        Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
    }

    @Override
    public V remove(Object key) {
        Node<K,V> e;
        return (e = removeNode(hash(key), key, null, false, true)) == null ?
                null : e.value;
    }

    @Override
    public void clear() {
        Node<K,V>[] tab;
        modCount++;
        if((tab = table) != null && size > 0) {
            size = 0;
            for(int i = 0; i < tab.length; ++i)
                tab[i] = null;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return getNode(hash(key), key) != null;
    }

    //没有给出key，也就不知道hash，无法使用常数时间查找，只能遍历
    //可以先根据table是否为null，size是否大于0先确定是否有必要遍历查找
    @Override
    public boolean containsValue(Object value) {
        Node<K,V>[] tab; V v;
        if((tab = table) != null && size > 0) {
            for(int i = 0; i < tab.length; ++i) {
                for(Node<K,V> e = tab[i]; e != null; e = e.next) {
                    if((v = e.value) == value ||
                            (value != null && value.equals(v)))
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new EntrySet();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        putMapEntries(m, true);
    }

    @Override
    public Set<K> keySet() {
        Set<K> ks;
        return (ks = keySet) == null ? (keySet = new KeySet()) : ks;
    }

    @Override
    public Collection<V> values() {
        Collection<V> vs;
        return (vs = values) == null ? (values = new Values()) : vs;
    }

    //1.8新方法
    @Override
    public V putIfAbsent(K key, V value) {
        return putVal(hash(key), key, value, true, true);
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? defaultValue : e.value;
    }

    @Override
    public boolean remove(Object key, Object value) {
        return removeNode(hash(key), key, value, true, true) != null;
    }

    @Override
    public V replace(K key, V value) {
        Node<K,V> e;
        if((e = getNode(hash(key), key)) != null) {
            V oldValue = e.value;
            e.value = value;
            //执行回调
            afterNodeAccess(e);
            return oldValue;
        }
        return null;
    }

    @Override
    public boolean replace(K key, V oldValue, V value) {
        Node<K,V> e; V v;
        if((e = getNode(hash(key), key)) != null &&
                ((v = e.value) == oldValue || (v != null && v.equals(oldValue)))) {
            e.value = value;
            afterNodeAccess(e);
            return true;
        }
        return false;
    }


    /*
    序列化支持
     */
    private static final long serialVersionUID = 362498820763181265L;
    //同样为了隐藏内部细节，只写入容量，装载因子，size，key和value
    final void internalWriteEntries(java.io.ObjectOutputStream s) throws IOException {
        Node<K,V>[] tab;
        if(size > 0 && (tab = table) != null) {
            for(int i = 0; i < size; ++i) {
                for(Node<K,V> e = tab[i]; e != null; e = e.next) {
                    s.writeObject(e.key);
                    s.writeObject(e.value);
                }
            }
        }
    }

    //这两个方法可以在HashSet序列化是调用
    final float loadFactor() { return loadFactor; }
    final int capacity() {
        return (table != null) ? table.length :
                (threshold > 0) ? threshold :
                        DEFAULT_INITIAL_CAPACITY;
    }

    private void writeObject(java.io.ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        int buckets = capacity();
        s.writeInt(buckets);
        s.writeInt(size);
        internalWriteEntries(s);
    }

    private void readObject(java.io.ObjectInputStream s) throws IOException {
        
    }

    void reinitialize() {
        table = null;
        entrySet = null;
        keySet = null;
        values = null;
        modCount = 0;
        threshold = 0;
        size = 0;
    }




    /*
      核心内部类实现
    */
    //结点类
    static class Node<K,V> implements Map.Entry<K,V> {
        final K key;
        final int hash;
        V value;
        Node<K,V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.next = next;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if(o == this) return true;
            if(o instanceof Node<?,?>) {
                Map.Entry<?,?> e = (Map.Entry<?,?>)o;
                if (Objects.equals(key, e.getKey()) &&
                        Objects.equals(value, e.getValue()))
                    return true;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(key.hashCode()) ^ Objects.hashCode(value.hashCode());
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }
    }

    //红黑树节点
    static final class TreeNode<K,V> extends LinkedListEntry<K,V> {
        TreeNode(int hash, K key, V val, Node<K,V> next) {
            super(hash, key, val, next);
        }
    }

    static class LinkedListEntry<K,V> extends Node<K,V> {
        LinkedListEntry<K,V> before, after;
        LinkedListEntry(int hash, K key, V value, Node<K, V> next) {
            super(hash, key, value, next);
        }
    }

    /*
    Set视图遍历操作Map
    不允许添加
     */
    //EntrySet，节点集合
    final class EntrySet extends AbstractSet<Map.Entry<K,V>> {
        @Override
        public boolean remove(Object o) {
            if(o instanceof Map.Entry) {
                Map.Entry<?,?> e = (Map.Entry<?,?>)o;
                Object key = e.getKey();
                Object value = e.getValue();
                return removeNode(hash(key), key, value, true, true) != null;
            }
            return false;
        }

        @Override
        public void clear() {
            HashMap.this.clear();
        }

        @Override
        public boolean contains(Object o) {
            if(o instanceof Map.Entry) {
                Map.Entry<?,?> e = (Map.Entry<?,?>)o;
                Object key = e.getKey();
                Node<K,V> candidate = getNode(hash(key), key);
                return candidate != null && candidate.equals(e);
            }
            return false;
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public int size() {
            return size;
        }
    }

    //KeySet，以Set视图操作Map
    final class KeySet extends AbstractSet<K> {
        @Override
        public final boolean remove(Object key) {
            return removeNode(hash(key), key, null, false, true) != null;
        }
        @Override
        public final boolean contains(Object key) {
            return containsKey(key);
        }
        @Override
        public final void clear() {
            HashMap.this.clear();
        }
        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }
        @Override
        public int size() {
            return size;
        }
    }

    /*
    value集合，不能通过它进行删除
     */
    final class Values extends AbstractCollection<V> {
        @Override
        public void clear() {
            HashMap.this.clear();
        }

        @Override
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean contains(Object o) {
            return containsValue(o);
        }
    }

    /*
    各种迭代器
     */
    //HashMap的节点迭代器:
    //大致顺序是现在桶数组中找到一个不为空的桶，迭代这个桶中的元素，当这个桶迭代完了，寻找下一个不为空继续迭代
    //模板方法设计模式，Iterator中remove和hasNext方法并没有设计参数类型，这也能看出Iterator设计的思路
    //Set和Map的迭代器不能add只能遍历和删除，List的迭代器ListIterator可以添加，双向遍历，删除；
    abstract class HashIterator {
        Node<K,V> next; //下一个指向的节点引用
        Node<K,V> current; //最后返回的节点引用
        int expectedModCount; //fail-fast
        int index; //当前的索引位置

        HashIterator() {
            expectedModCount = modCount;
            Node<K,V>[] t = table;
            next = current = null;
            index = 0;
            //找到第一个节点
            if(t != null)
                do {} while(index < t.length && (next = t[index++]) == null);
        }

        public void remove() {
            Node<K,V> p = current;
            if(p == null)
                throw new IllegalStateException();
            if (modCount != expectedModCount) //fail-fast
                throw new ConcurrentModificationException();
            current = null;
            K k = p.key;
            removeNode(hash(k), k, null, false, false); //会修改modCount
            expectedModCount = modCount;
        }

        public boolean hasNext() {
            return next != null;
        }

        final Node<K,V> nextNode() {
            Node<K,V>[] t;
            Node<K,V> e = next;
            if (modCount != expectedModCount) //fail-fast
                throw new ConcurrentModificationException();
            if (e == null)
                throw new NoSuchElementException();
            //当桶中元素已经迭代完，寻找下一个不为空的桶
            if((current = e).next == null && (t = table) != null) {
                do {} while (index < t.length && (next = t[index++]) == null);
            }
            return e;
        }
    }

    //key迭代器
    final class KeyIterator extends HashIterator
            implements Iterator<K> {
        @Override
        public K next() {
            return nextNode().key;
        }
    }
    //value迭代器
    final class ValueIterator extends HashIterator
            implements Iterator<V> {
        @Override
        public V next() {
            return nextNode().value;
        }
    }
    //节点迭代器
    final class EntryIterator extends HashIterator
            implements Iterator<Map.Entry<K,V>> {
        @Override
        public Entry<K, V> next() {
            return nextNode();
        }
    }


    public static void main(String[] args) {
        List<String> s = new ArrayList<>();
        List s1 = s;
        s1.add(1);

        
    }
}
