package com.yjh.Collections;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * 学习LinkedList源码
 * Created by yjh on 15-12-9.
 */
public class MLinkedList<E> extends AbstractSequentialList<E>
        implements List<E>, Deque<E>, Serializable, Cloneable {
    private transient int size = 0;
    private transient Node<E> first = null;
    private transient Node<E> last = null;

    public MLinkedList() {
    }

    public MLinkedList(Collection<? extends E> collection) {
        this();
        addAll(collection);
    }

    /*
    辅助方法，用包私有权限，核心的链表操作
     */
    //在头端插入
    private void linkFirst(E e) {
        final Node<E> f = first;
        final Node<E> newNode = new Node<>(e, null, f);
        first = newNode;
        if(f == null) {
            //原链表为空时last指针也要指向新结点
            last = newNode;
        } else {
            f.prev = newNode;
        }
        ++size;
        //增加修改次数
        ++modCount;
    }
    //在尾端插入
    void linkLast(E e) {
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(e, l, null);
        last = newNode;
        if(l == null) {
            //原链表为空时first指针也要执行新结点
            first = newNode;
        } else {
            l.next = newNode;
        }
        ++size;
        ++modCount;
    }
    //在指定结点前插入
    void linkBefore(E e, Node<E> succ) {
        final Node<E> pred = succ.prev;
        final Node<E> newNode = new Node<>(e, succ.prev, succ);
        succ.prev = newNode;
        if(pred == null) {
            first = newNode;
        } else {
            pred.next = newNode;
        }
        ++size;
        ++modCount;
    }
    //删除第一个结点，由调用者判断是否有头结点
    private E unlinkFirst(Node<E> f) {
        final E element = f.item;
        final Node<E> next = f.next;
        f.next = null; //删除之后置为null，不影响被引用对象的垃圾回收
        f.item = null;
        first = next; //first指针指向next
        if(next == null)
            last = null;
        else
            next.prev = null;
        --size; //数量减少
        ++modCount; //修改计数增加
        return element;
    }
    //删除最后一个结点
    private E unlinkLast(Node<E> l) {
        final E element = l.item;
        final Node<E> prev = l.prev;
        l.item = null; //置空不影响原被引用对象的垃圾回收
        l.prev = null;
        last = prev;
        if(prev == null)
            first = null;
        else
            prev.next = null;
        --size;
        ++modCount;
        return element;
    }
    //删除链表中某个结点
    E unlink(Node<E> x) {
        final E element = x.item;
        final Node<E> prev = x.prev;
        final Node<E> next = x.next;
        x.item = null; //置空，不影响垃圾回收
        if(prev == null)
            first = next;
        else {
            prev.next = next;
            x.prev = null;
        }
        if(next == null)
            last = prev;
        else {
            next.prev = prev;
            x.next = null;
        }
        --size;
        ++modCount;
        return element;
    }

    //获取指定位置的结点，通过index是否小于size/2进行优化
    Node<E> node(int index) {
        //如果小于size / 2从头开始搜索，否则从尾开始搜索
        if(index < (size >> 1)) {
            Node<E> x = first;
            for(int i = 0; i < index; ++i)
                x = x.next;
            return x;
        } else {
            Node<E> x = last;
            for(int i = size - 1; i >= 0; --i)
                x = x.prev;
            return x;
        }
    }

    //检查索引是否合法
    private boolean isElementIndex(int index) {
        return index >= 0 && index < size;
    }
    //检查索引是否合法，不合法抛出异常
    private void checkElementIndex(int index) {
        if(isElementIndex(index))
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }
    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }
    //检查待插入位置的索引值是否合法
    private boolean isPositionIndex(int index) {
        return index >= 0 && index <= size;
    }
    //检查待插入位置的索引值是否合法，不合法抛出异常
    private void checkPositionIndex(int index) {
        if(isPositionIndex(index))
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /*
    List的实现，覆盖核心方法
     */
    //添加结点，在表尾
    @Override
    public boolean add(E e) {
        linkLast(e);
        return true;
    }

    //在指定位置添加结点
    @Override
    public void add(int index, E element) {
        //检查插入位置是否合法
        checkPositionIndex(index);

        if(index == size) //排除为空的可能
            linkLast(element);
        else { //index < size时
            linkBefore(element, node(index));
        }
    }

    //将一个集合中元素添加在指定位置之后，注意双向链表中指针值的设置
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        //检验插入位置是否合法
        checkPositionIndex(index);

        //将这个集合中的保存到新数组中，再进行操作，这是一种“安全”的做法
        //使用toArray的目的是为了调用者可以自由的操作这个数组，而不会影响原来的集合
        //返回的是对象数组，因为将集合类本身并不知道具体是什么类型，因此无法以最具体的类型声明数组
        //调用此方法也意味着我们要进行强制类型转换，不过可以像addAll这样通过参数的类型参数来限制保证正确的类型
        Object[] a = c.toArray();
        int numNew = a.length; //arraylength指令（乱入一记字节码指令）
        if(numNew == 0)
            return false;

        //声明两个变量（指针）用来辅助设置插入过程中前后结点的指针值
        //保存succ最后修改这个结点的prev指针（如果不为空的话）
        Node<E> pred, succ;
        //插入位置正好是末尾，无法通过node()查找定位到
        //否则找到index位置的结点，并确定pred的值
        if(index == size) {
            pred = last;
            succ = null;
        } else {
            succ = node(index);
            pred = succ.prev;
        }

        for(Object o : a) {
            @SuppressWarnings("unchecked") E element = (E)o;
            Node<E> newNode = new Node<>(element, pred, succ);
            if(pred == null) //在表头插入，设置frist值
                first = newNode;
            else
                pred.next = newNode;
            pred = newNode;
        }

        //设置后续结点的prev
        if(succ == null) //在末尾插入修改last的指向
            last = pred;
        else {
            pred.next = succ;
            succ.prev = pred;
        }

        size += numNew;
        ++modCount;

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addAll(size, c);
        return true;
    }

    @Override
    public E get(int index) {
        //检验索引值是否合法，不合法抛出异常
        checkElementIndex(index);
        return node(index).item;
    }

    //设置并返回旧值
    @Override
    public E set(int index, E element) {
        //检验索引值是否合法，不合法抛出异常
        checkElementIndex(index);
        Node<E> x = node(index);
        E oldVal = x.item;
        x.item = element;
        return oldVal;
    }

    @Override
    public E remove(int index) {
        checkElementIndex(index);
        return unlink(node(index));
    }

    @Override
    public boolean remove(Object o) {
        if(o == null) {
            for(Node<E> x = first; x != null; x = x.next) {
                if(x.item == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            for(Node<E> x = first; x != null; x = x.next) {
                if(o.equals(x.item)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }

    //清空链表，尤其注意置空，让结点之间，结点和元素之间不在互相引用的存在堆中，让它们“独立”的接受垃圾回收
    @Override
    public void clear() {
        for(Node<E> x = first; x != null;) {
            Node<E> next = x.next;
            //置空，不要影响原来被引用对象的垃圾回收
            x.item = null;
            x.next = null;
            x.prev = null;
            x = next;
        }
        first = last = null;
        size = 0;
        ++modCount;
    }

    //查找指定对象第一次出现的位置
    @Override
    public int indexOf(Object o) {
        int index = 0;
        if(o == null) {
            for(Node<E> x = first; x != null; x = x.next) {
                if(x.item == null)
                    return index;
                index++;
            }
        } else {
            for(Node<E> x = first; x != null; x = x.next) {
                if(o.equals(x.item))
                    return index;
                index++;
            }
        }
        return -1;
    }

    //查找指定对象最后一次出现的位置
    @Override
    public int lastIndexOf(Object o) {
        int index = 0;

        if(o == null) {
            for(Node<E> x = last; x != null; x = x.prev) {
                if(x.item == null)
                    return index;
                index++;
            }
        } else {
            for(Node<E> x = last; x != null; x = x.prev) {
                if(o.equals(x.item))
                    return index;
                index++;
            }
        }

        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    //注意contains，indexOf，lastIndexOf方法接受的都是Object
    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    //列表迭代器
    @Override
    public ListIterator<E> listIterator(int index) {
        checkPositionIndex(index);
        return new ListItr(index);
    }

    //保存为数组，这里只能返回Object，因为不能通过泛型参数创建对象/数组对象
    //通过toArray方法可以自由操作该集合内元素而不影响该集合
    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for(Node<E> x = first; x != null; x = x.next) {
            result[i++] = x.item;
        }
        return result;
    }

    //toArray的泛型方法版本，相对于toArray()来说可以进行必要的类型检查，但是创建数组的责任交给了调用者
    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        //检查长度是否能够存放所有元素
        if(a.length < size) //小于size重新通过反射创建
            a = (T[])java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);

        int i = 0;
        //通过Object数组引用，可以避开泛型参数的限制，数组对象本身可以检查存入的类型是否正确，不正确保存ArrayStoreException
        Object[] result = a;
        for(Node<E> x = first; x != null; x = x.next) {
            result[i++] = x.item;
        }

        //如果数组长度大于集合长度，size位置置空
        if(a.length > size) {
            a[size] = null;
        }

        return a;
    }

    /*
    AbstractSequentialList的实现
     */
    //逆序迭代器
    @Override
    public Iterator<E> descendingIterator() {
        return new DescendingIterator();
    }

    private class DescendingIterator implements Iterator<E> {
        private final ListItr itr = new ListItr(size());
        public boolean hasNext() {
            return itr.hasPrevious();
        }
        public E next() {
            return itr.previous();
        }
        public void remove() {
            itr.remove();
        }
    }

    /*
        Deque的实现，双端队列
         */
    @Override
    public void addFirst(E e) {
        linkFirst(e);
    }

    @Override
    public void addLast(E e) {
        linkLast(e);
    }

    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E removeFirst() {
        final Node<E> f = first;
        if(f == null)
            throw new NoSuchElementException("first is not exist.");
        return unlinkFirst(f);
    }

    @Override
    public E removeLast() {
        final Node<E> l = last;
        if(l == null)
            throw new NoSuchElementException("last is not exist.");
        return unlinkLast(l);
    }

    //poll~方法抛出异常，为空时返回null
    @Override
    public E pollFirst() {
        final Node<E> f = first;
        return f == null ? null : unlinkFirst(f);
    }

    @Override
    public E pollLast() {
        final Node<E> l = last;
        return l == null ? null : unlinkLast(l);
    }

    //get~方法抛出异常
    @Override
    public E getFirst() {
        final Node<E> f = first;
        if(f == null)
            throw new NoSuchElementException();
        return f.item;
    }

    @Override
    public E getLast() {
        final Node<E> l = last;
        if(l == null)
            throw new NoSuchElementException();
        return l.item;
    }

    @Override
    public E peekFirst() {
        final Node<E> f = first;
        return f == null ? null : f.item;
    }

    @Override
    public E peekLast() {
        final Node<E> l = last;
        return l == null ? null : l.item;
    }

    //删除队列中第一个该对象的引用
    @Override
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    //删除队列中最后一个该对象的引用
    @Override
    public boolean removeLastOccurrence(Object o) {
        if(o == null) {
            for(Node<E> x = last; x != null; x = x.prev) {
                if(x.item == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            for(Node<E> x = last; x != null; x = x.prev) {
                if(o.equals(x.item)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }

    //入队操作，链表尾部插入，不抛出异常
    @Override
    public boolean offer(E e) {
        return add(e);
    }

    //出队操作，不抛出异常
    @Override
    public E poll() {
        final Node<E> f = first;
        return f == null ? null : unlinkFirst(f);
    }

    //获取表头元素，栈的操作，不抛出异常
    @Override
    public E peek() {
        final Node<E> f = first;
        return (f == null) ? null : f.item;
    }

    //出队删除，检查抛出异常
    @Override
    public E remove() {
        return removeFirst();
    }

    //获取表头元素，抛出异常
    @Override
    public E element() {
        return getFirst();
    }

    //入栈，表头添加，栈的操作
    @Override
    public void push(E e) {
        addFirst(e);
    }

    //出栈，表头删除，栈的操作
    @Override
    public E pop() {
        return removeFirst();
    }

    /*
    Cloneable支持
     */
    @Override
    public Object clone() {
        MLinkedList<E> clone = superClone();
        clone.first = clone.last = null;
        clone.size = 0;
        clone.modCount = 0;

        for(Node<E> x = first; x != null; x = x.next) {
            clone.add(x.item);
        }

        return clone;
    }

    @SuppressWarnings("unchecked")
    private MLinkedList<E> superClone() {
        try {
            return (MLinkedList<E>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    /*
    序列化支持
     */
    //序列化版本号，是数据域和方法签名的数字指纹（SHA）
    private static final long serialVersionUID = 876323262645176354L;

    //代替默认的序列化过程，这里值写入了元素对象和元素集合大小
    //在JDK1.7之前使用的并不是Node而是Entry，也是保存了元素对象而不是Entry，这样的考虑解决了LinkedList的兼容问题
    //这也是为什么用transient修饰size，first，last域的原因
    //一方面是隐藏内部表示信息，一方面节省了开销
    private void writeObject(java.io.ObjectOutputStream s)
            throws IOException {
        //写入包括魔数，序列化格式版本号，包括所有对象的类型和（非静态和非transient）数据域
        //每个对象包括一个序列号
        //相同对象重复出现将被视为对这个对象序列号的引用
        s.defaultWriteObject();

        s.writeInt(size);

        for(Node<E> x = first; x != null; x = x.next) {
            s.writeObject(x.item);
        }
    }

    //反序列化过程，同样读取size和元素对象，重新“构建”链表
    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream s)
            throws IOException, ClassNotFoundException {
        s.defaultReadObject();

        int size = s.readInt();

        for(int i = 0; i < size; i++) {
            linkLast((E)s.readObject());
        }
    }

    /*
    核心内部类实现：Node，ListIterator
     */
    static class Node<E> {
        private E item;
        private Node<E> prev;
        private Node<E> next;

        public Node(E item, Node<E> previous, Node<E> next) {
            this.item = item;
            this.next = next;
            this.prev = previous;
        }
    }

    //非静态私有内部类
    private class ListItr implements ListIterator<E> {
        //lastReturned保存最后返回的结点引用，当add，remove结构性修改后将这个值置空用于标识结构结构已修改
        private Node<E> lastReturned;
        private Node<E> next;
        private int nextIndex;
        //每个迭代器保存创建时modCount，维护自己的modCount防止因为并发修改造成的不一致
        private int expectedModCount = modCount;

        ListItr(int index) {
            next = (index == size) ? null : node(index);
            nextIndex = index;
        }

        @Override
        public void add(E e) {
            //每次结构性修改之前必须先检查modCount是否同步，保证同时只有一个ListIterator可以修改链表
            checkForComodification();
            //lastReturned置空，防止删除之前的结点
            lastReturned = null;
            if(next == null)
                linkLast(e);
            else
                linkBefore(e, next);
            nextIndex++;
            expectedModCount++;
        }

        @Override
        public boolean hasNext() {
            return nextIndex < size;
        }

        @Override
        public E next() {
            //检查修改计数
            checkForComodification();
            if(!hasNext())
                throw new NoSuchElementException();
            lastReturned = next;
            next = next.next;
            nextIndex++;
            return lastReturned.item;
        }

        @Override
        public boolean hasPrevious() {
            //nextIndex时显然不能有prev
            return nextIndex > 0;
        }

        //前一个结点，此时next和lastReturned值一致
        @Override
        public E previous() {
            checkForComodification();
            if(!hasPrevious())
                throw new NoSuchElementException();
            nextIndex--;
            lastReturned = next = (next == null) ? last : next.prev;
            return lastReturned.item;
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }

        //删除最后返回的结点
        @Override
        public void remove() {
            //检查修改计数
            checkForComodification();
            if(lastReturned == null)
                throw new IllegalStateException();

            Node<E> lastNext = lastReturned.next;
            unlink(lastReturned);
            //如果next和lastReturned（这种情况在previous执行后出现），要修改next值
            if(next == lastReturned)
                next = lastNext; //虽然删除了一个结点，但是next向后移动了一位，因此nextIndex值不需要修改
            else
                nextIndex--;
            lastReturned = null;
            expectedModCount++;
        }

        //修改最后返回结点的值
        @Override
        public void set(E e) {
            if(lastReturned == null)
                throw new IllegalStateException();
            checkForComodification();
            lastReturned.item = e;
        }

        //检查是否并发修改
        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }
}
