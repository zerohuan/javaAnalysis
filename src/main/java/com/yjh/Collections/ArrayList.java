package com.yjh.Collections;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

/**
 * ArrayList的内部表示通过控制对象数组的访问，数组是不能动态修改大小的，因此根据元素数量/指定容量大小
 * 通过创建新数组，复制元素对象以及像空列表状态的使用一个单例的空数组等优化手段是ArrayList的核心原理
 *
 * 优化：
 * （1）EMPTY_ELEMENTDATA和DEFAULTCAPACITY_EMPTY_ELEMENTDATA；
 * （2）grow的增长机制；
 *
 * Created by yjh on 15-12-11.
 */
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, Serializable {


    //默认初始化容量
    private static final int DEFAULT_CAPACITY = 10;
    /*
     所有Empty数组列表共享一个空数组对象，不仅可以减少重复对象的创建（单例模式应用），还可以标识空列表状态
     我觉得这里是单例模式（区别于享元模式）的原因：
     一是这里只有一个对象；
     二是不仅仅是节省内存，更重要的是起到共享并标识“空数组列表”的作用；
     EMPTY_ELEMENTDATA标识“空”状态，DEFAULTCAPACITY_EMPTY_ELEMENTDATA标识默认状态
     */
    private static final Object[] EMPTY_ELEMENTDATA = {};
    /*
     默认状态创建数组列表实例使用该数组，使用一个单例空数组并且不直接使用EMPTY_ELEMENTDATA而是新键一个空数组的原因是：
     一是用于标识不同的状态，区别于“空数组列表”，DEFAULTCAPACITY_EMPTY_ELEMENTDATA表示数组列表还未插入元素；
     二是等到实际有元素再分配合适大小的数组内存空间，起到延迟加载的作用；
     三是同样单例可以让所有默认状态列表使用一个空数组对象，节省内存；
    */
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
    /*
    使用transient的目的是为了隐藏内部表示
    使用Object的原因：
    （1）Object可以引用所有具体类型的数组对象；
    （2）实际的ArrayList的类型参数对于ArrayList对象本身是不可知的，泛型的本质是擦除，并借助变量，方法，类的签名来进行类型检查和转换;
        因此可以用Object数组存放元素对象，依赖方法和类型参数让编译器和JVM进行类型检查和转换（比如插入checkcast指令等等）；
     */
    transient Object[] elementData;
    //元素个数
    private int size;

    /*
    翻译：
    一些实现可能会保存一些首部信息在数组对象中。
    试图分配一个比MAX_ARRAY_SIZE大的数组可能会造成OOM（Requested array size exceeds VM limit）。
    也就是说一些JVM的实现可能会限制数组大小小于Integer.MAX_VALUE，因此检查是必要的。
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /*
    不同版本的构造器
     */
    //如果指定初始化容量为“0”，使用EMPTY_ELEMENTDATA
    public ArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                    initialCapacity);
        }
    }
    //创建默认初始化容量列表，使用DEFAULTCAPACITY_EMPTY_ELEMENTDATA
    public ArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    //拷贝构造器，注意使用toArray获取要插入元素对象
    public ArrayList(Collection<? extends E> c) {
        elementData = c.toArray();
        if((size = elementData.length) != 0) {
            /*
            注意，toArray返回的Object[]引用，而Object[]可以作为超类引用子类对象比如A[]（假设A是E的子类）
            如果toArray返回是A[]数组对象，那么如果类型B对象（B extends E）可能会引起后续的插入时的ArrayStoreException，读取元素时的ClassCheckException
            因此如果返回的不是Object[]对象，要创建一个Object[]并复制元素引用
             */
            if(elementData.getClass() != Object[].class) {
                elementData = Arrays.copyOf(elementData, size, Objects[].class);
            }
        } else {
            //一个空数组列表，直接使用EMPTY_ELEMENTDATA
            elementData = EMPTY_ELEMENTDATA;
        }
    }

    /*
    ArrayList一些核心方法，辅助方法
     */
    //压缩缓存区大小，最小化存储区域到size相同的大小
    public void trimToSize() {
        modCount++;
        if(size < elementData.length) {
            elementData = (size == 0)
                    ? EMPTY_ELEMENTDATA
                    : Arrays.copyOf(elementData, size);
        }
    }

    //增加容量到指定大小（溢出会抛出OOM），通过这个方法可以打破MAX_ARRAY_SIZE限制
    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;
        //首先尝试将新容量设置为原容量的（3/2）
        //这样做可以避免频繁小幅度的扩张带来的开销，我联想到一个类似的做法是滑动窗口中修改窗口大小的机制
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        //如果尚未达到指定大小（这说明扩张幅度够大），设置为指定大小
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
    }

    //当ArrayList不处于默认状态时，才可能扩展大小为小于DEFAULT_CAPACITY的容量；
    // 否则只有指定大小超过DEFAULT_CAPACITY时才进行扩展；
    //注意这个方法是public，区别于ensureCapacityInternal，这个方法是在外部使用的；
    public void ensureCapacity(int minCapacity) {
        int minExpand = (elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA)
                // any size if not default element table
                ? 0
                // larger than default for default empty table. It's already
                // supposed to be at default size.
                : DEFAULT_CAPACITY;

        if (minCapacity > minExpand) {
            ensureExplicitCapacity(minCapacity);
        }
    }

    //ArrayList内部扩展大小使用此方法，没有上个条件方法限制
    //但如果处于默认状态，扩展大小仍然不能小于DEFAULT_CAPACITY
    private void ensureCapacityInternal(int minCapacity) {
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }

        ensureExplicitCapacity(minCapacity);
    }

    private void ensureExplicitCapacity(int minCapacity) {
        modCount++;

        // overflow-conscious code
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }

    //删除[fromIndex, toIndex)范围的元素
    //操作后将末尾newSize之后的元素置空，防止内存泄漏
    protected void removeRange(int fromIndex, int toIndex) {
        modCount++; //fail-fast
        int numMoved = size - toIndex;
        System.arraycopy(elementData, fromIndex, elementData, toIndex,
                numMoved);

        //清空多余的元素引用
        int newSize = size - (toIndex - fromIndex);
        for(int i = newSize; i < size; i++)
            elementData[i] = null;
        size = newSize;
    }

    //批量删除，complement为false时删除数组缓冲区中集合c包含的元素，true，删除集合c中不包含的元素，可以用实现交，差等集合运算
    //使用复制的方法，而不是调用remove()的方式，减少了元素的重复复制
    //同样要将newSize之外的元素引用置空，防止内存泄漏
    private boolean batchRemove(Collection<?> c, boolean complement) {
        final Object[] elementData = this.elementData;
        int r = 0, w = 0; //不变式，w <= r
        boolean modified = false;
        try {
            for (; r < size; r++) {
                if(c.contains(elementData[r]) == complement) {
                    elementData[w++] = elementData[r];
                }
            }
        } finally {
            //如果有异常抛出，保存好还未处理的数组元素
            if (r != size) {
                System.arraycopy(elementData, r,
                        elementData, w,
                        size - r);
                w += size - r;
            }
            if(w != size) {
                for(int i = w; i < size; i++)
                    elementData[i] = null;
                modCount += size - w;
                size = w;
                modified = true;
            }
        }
        return modified;
    }

    //由调用者检查index是否合法
    private void fastRemove(int index) {
        modCount++;
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,
                    numMoved);
        elementData[--size] = null; // clear to let GC do its work
    }

    /*
    辅助方法，包括：
    （1）检查索引是否合法；
    （2）返回指定位置的元素；
     */
    private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }

    //复用
    @SuppressWarnings("unchecked")
    E elementData(int index) {
        return (E) elementData[index];
    }

    /*
    List实现，覆盖List的重要方法实现
     */
    //获取指定位置的元素对象引用
    @Override
    public E get(int index) {
        rangeCheck(index);
        return elementData(index);
    }

    //注意set方法不是结构性修改，因此并没有modCount++
    @Override
    public E set(int index, E element) {
        rangeCheck(index);
        E oldValue = elementData(index);
        elementData[index] = element;
        return oldValue;
    }

    //各种add方法实现
    @Override
    public boolean add(E e) {
        //检查并扩增大小，该方法会增加修改计数器，并且一次扩展(3/2)大小防止了频繁扩展带来的开销
        ensureCapacityInternal(size + 1);
        elementData[size++] = e;
        return true;
    }

    @Override
    public void add(int index, E element) {
        rangeCheckForAdd(index);

        ensureCapacityInternal(size + 1);
        //将数组缓冲区中，index及之后的结点后移
        System.arraycopy(elementData, index, elementData, index + 1,
                size - index);
        elementData[index] = element;
        size++;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        //获取集合中的元素数组，这样做的好处在于可以自由操作这些元素，而不用影响集合c
        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityInternal(size + numNew); //增加modCount
        System.arraycopy(a, 0, elementData, size, numNew);
        size += numNew;
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        rangeCheckForAdd(index);

        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityInternal(size + numNew); //增加modCount

        //如果是在尾部插入就不需要移动了
        int numMoved = size - index;
        if(numMoved > 0)
            System.arraycopy(elementData, index, elementData, index + numNew,
                    numMoved);

        System.arraycopy(a, 0, elementData, index, numNew);
        size += numNew;
        return true;
    }

    //各种删除的实现
    @Override
    public E remove(int index) {
        rangeCheck(index);

        modCount++; //fail-fast
        E oldValue = elementData(index);

        int numMoved = size - index - 1;
        if(numMoved > 0)
            System.arraycopy(elementData, index + 1, elementData, index,
                    numMoved);
        elementData[--size] = null; //防止内存泄漏

        return oldValue;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (int index = 0; index < size; index++)
                if (elementData[index] == null) {
                    fastRemove(index);
                    return true;
                }
        } else {
            for (int index = 0; index < size; index++)
                if (o.equals(elementData[index])) {
                    fastRemove(index);
                    return true;
                }
        }
        return false;
    }

    //保留差集
    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.nonNull(c);
        return batchRemove(c, false);
    }

    //保留交集
    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.nonNull(c);
        return batchRemove(c, true);
    }

    //置空防止内存泄漏
    @Override
    public void clear() {
        modCount++;

        for(int i = 0; i < size; i++)
            elementData[i] = null;

        size = 0;
    }

    //正向查找，返回元素第一次出现的索引值
    @Override
    public int indexOf(Object o) {
        if(o == null) {
            for (int i = 0; i < size; i++)
                if (elementData[i] == null)
                    return i;
        } else {
            for (int i = 0; i < size; i++)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }

    //反向查找，返回元素最后一次出现的索引值
    @Override
    public int lastIndexOf(Object o) {
        if(o == null) {
            for (int i = size - 1; i >= 0; --i)
                if(elementData[i] == null)
                    return i;
        } else {
            for (int i = size - 1; i >= 0; --i)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    //对数组进行排序
    @Override
    @SuppressWarnings("unchecked")
    public void sort(Comparator<? super E> c) {
        final int expectedModCount = modCount;
        Arrays.sort((E[]) elementData, 0, size, c);
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        modCount++;
    }

    //保护性拷贝
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elementData, size);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        if(a.length < size)
            //必须创建一个与参数类型相同的数组
            return (T[])Arrays.copyOf(elementData, size, a.getClass());
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size)
            a[size] = null; //用来帮助调用者确定集合长度（只有在明确知道集合中没有null元素时才有用）
        return a;
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
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        subListRangeCheck(fromIndex, toIndex, size);
        return new SubList(this, 0, fromIndex, toIndex);
    }

    /*
    序列化支持
    （1）同样写入size和元素对象，避免写入内部表示；
    （2）序列化过程也是用类似乐观锁机制，防止并发修改带来的不一致；
     */
    //所有方法签名和数据域的数字指纹，检查版本是否更新
    private static final long serialVersionUID = 8683452581122892189L;
    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException{
        // Write out element count, and any hidden stuff
        int expectedModCount = modCount;
        s.defaultWriteObject();

        // Write out size as capacity for behavioural compatibility with clone()
        s.writeInt(size);

        // Write out all elements in the proper order.
        for (int i=0; i<size; i++) {
            s.writeObject(elementData[i]);
        }

        //防止序列化过程中应并发修改造成的不一致
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }

    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        elementData = EMPTY_ELEMENTDATA;

        // Read in size, and any hidden stuff
        s.defaultReadObject();

        // Read in capacity
        s.readInt(); // ignored，因为size实际上并不是transient的，也不是static的

        if (size > 0) {
            // be like clone(), allocate array based upon size not capacity
            ensureCapacityInternal(size);

            Object[] a = elementData;
            // Read in all elements in the proper order.
            for (int i=0; i<size; i++) {
                a[i] = s.readObject();
            }
        }
    }

    /*
    克隆支持
     */
    @Override
    public Object clone() {
        try {
            ArrayList<?> v = (ArrayList<?>) super.clone();
            v.elementData = Arrays.copyOf(elementData, size);
            v.modCount = 0;
            return v;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
    }

    /*
    核心内部类
    （1）注意ArrayList和LinkedList不同，Iterator和ListIterator是不同的内部类实现的；
     */
    //AbstractList.Itr的一个优化版本
    private class Itr implements Iterator<E> {
        int cursor;       // 下一个将返回元素的索引位置
        int lastRet = -1; // 最后返回元素的索引within， -1 表示不存在
        int expectedModCount = modCount;

        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        @Override
        public E next() {
            checkForComodification();
            int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            Object[] elementData = ArrayList.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i + 1;
            return (E)elementData[lastRet = i];
        }

        @Override
        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification(); //fail-fast

            try {
                //删除了最后返回位置上的元素，之后的的数组元素“前移”了一位，因此cursor = lastRet
                ArrayList.this.remove(lastRet);
                cursor = lastRet;
                //这时最后返回的元素已被删除，应该置为-1
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    //列表迭代器，可以两个方向迭代
    //继承与Itr从而获得向后迭代的能力
    //同样add在之后lastRet设置为-1，只有执行next才能对其remove
    private class ListItr extends Itr implements ListIterator<E> {
        ListItr(int index) {
            super();
            cursor = index;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        @SuppressWarnings("unchecked")
        public E previous() {
            checkForComodification(); //fail-fast
            int i = cursor - 1;
            if (i < 0)
                throw new NoSuchElementException();
            Object[] elementData = ArrayList.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i;
            return (E) elementData[lastRet = i];
        }

        public void set(E e) {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                ArrayList.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void add(E e) {
            checkForComodification();

            try {
                int i = cursor;
                ArrayList.this.add(i, e);
                cursor = i + 1;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }

    static void subListRangeCheck(int fromIndex, int toIndex, int size) {
        if (fromIndex < 0)
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        if (toIndex > size)
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("fromIndex(" + fromIndex +
                    ") > toIndex(" + toIndex + ")");
    }

    private class SubList extends AbstractList<E> implements RandomAccess {
        private final AbstractList<E> parent;
        private final int parentOffset;
        private final int offset;
        int size;

        SubList(AbstractList<E> parent,
                int offset, int fromIndex, int toIndex) {
            this.parent = parent;
            this.parentOffset = fromIndex;
            this.offset = offset + fromIndex;
            this.size = toIndex - fromIndex;
            this.modCount = ArrayList.this.modCount;
        }

        public E set(int index, E e) {
            rangeCheck(index);
            checkForComodification();
            E oldValue = ArrayList.this.elementData(offset + index);
            ArrayList.this.elementData[offset + index] = e;
            return oldValue;
        }

        public E get(int index) {
            rangeCheck(index);
            checkForComodification();
            return ArrayList.this.elementData(offset + index);
        }

        public int size() {
            checkForComodification();
            return this.size;
        }

        public void add(int index, E e) {
            rangeCheckForAdd(index);
            checkForComodification();
            parent.add(parentOffset + index, e);
            this.modCount = ArrayList.this.modCount;
            this.size++;
        }

        public E remove(int index) {
            rangeCheck(index);
            checkForComodification();
            E result = parent.remove(parentOffset + index);
            this.modCount = ArrayList.this.modCount;
            this.size--;
            return result;
        }

        protected void removeRange(int fromIndex, int toIndex) {
            checkForComodification();
            ArrayList.this.removeRange(parentOffset + fromIndex,
                    parentOffset + toIndex);
            this.modCount = ArrayList.this.modCount;
            this.size -= toIndex - fromIndex;
        }

        public boolean addAll(Collection<? extends E> c) {
            return addAll(this.size, c);
        }

        public boolean addAll(int index, Collection<? extends E> c) {
            rangeCheckForAdd(index);
            int cSize = c.size();
            if (cSize==0)
                return false;

            checkForComodification();
            parent.addAll(parentOffset + index, c);
            this.modCount = ArrayList.this.modCount;
            this.size += cSize;
            return true;
        }

        public Iterator<E> iterator() {
            return listIterator();
        }

        public ListIterator<E> listIterator(final int index) {
            checkForComodification();
            rangeCheckForAdd(index);
            final int offset = this.offset;

            return new ListIterator<E>() {
                int cursor = index;
                int lastRet = -1;
                int expectedModCount = ArrayList.this.modCount;

                public boolean hasNext() {
                    return cursor != SubList.this.size;
                }

                @SuppressWarnings("unchecked")
                public E next() {
                    checkForComodification();
                    int i = cursor;
                    if (i >= SubList.this.size)
                        throw new NoSuchElementException();
                    Object[] elementData = ArrayList.this.elementData;
                    if (offset + i >= elementData.length)
                        throw new ConcurrentModificationException();
                    cursor = i + 1;
                    return (E) elementData[offset + (lastRet = i)];
                }

                public boolean hasPrevious() {
                    return cursor != 0;
                }

                @SuppressWarnings("unchecked")
                public E previous() {
                    checkForComodification();
                    int i = cursor - 1;
                    if (i < 0)
                        throw new NoSuchElementException();
                    Object[] elementData = ArrayList.this.elementData;
                    if (offset + i >= elementData.length)
                        throw new ConcurrentModificationException();
                    cursor = i;
                    return (E) elementData[offset + (lastRet = i)];
                }

                @SuppressWarnings("unchecked")
                public void forEachRemaining(Consumer<? super E> consumer) {
                    Objects.requireNonNull(consumer);
                    final int size = SubList.this.size;
                    int i = cursor;
                    if (i >= size) {
                        return;
                    }
                    final Object[] elementData = ArrayList.this.elementData;
                    if (offset + i >= elementData.length) {
                        throw new ConcurrentModificationException();
                    }
                    while (i != size && modCount == expectedModCount) {
                        consumer.accept((E) elementData[offset + (i++)]);
                    }
                    // update once at end of iteration to reduce heap write traffic
                    lastRet = cursor = i;
                    checkForComodification();
                }

                public int nextIndex() {
                    return cursor;
                }

                public int previousIndex() {
                    return cursor - 1;
                }

                public void remove() {
                    if (lastRet < 0)
                        throw new IllegalStateException();
                    checkForComodification();

                    try {
                        SubList.this.remove(lastRet);
                        cursor = lastRet;
                        lastRet = -1;
                        expectedModCount = ArrayList.this.modCount;
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                public void set(E e) {
                    if (lastRet < 0)
                        throw new IllegalStateException();
                    checkForComodification();

                    try {
                        ArrayList.this.set(offset + lastRet, e);
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                public void add(E e) {
                    checkForComodification();

                    try {
                        int i = cursor;
                        SubList.this.add(i, e);
                        cursor = i + 1;
                        lastRet = -1;
                        expectedModCount = ArrayList.this.modCount;
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                final void checkForComodification() {
                    if (expectedModCount != ArrayList.this.modCount)
                        throw new ConcurrentModificationException();
                }
            };
        }

        public List<E> subList(int fromIndex, int toIndex) {
            subListRangeCheck(fromIndex, toIndex, size);
            return new SubList(this, offset, fromIndex, toIndex);
        }

        private void rangeCheck(int index) {
            if (index < 0 || index >= this.size)
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }

        private void rangeCheckForAdd(int index) {
            if (index < 0 || index > this.size)
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }

        private String outOfBoundsMsg(int index) {
            return "Index: "+index+", Size: "+this.size;
        }

        private void checkForComodification() {
            if (ArrayList.this.modCount != this.modCount)
                throw new ConcurrentModificationException();
        }

//        public Spliterator<E> spliterator() {
//            checkForComodification();
//            return new ArrayListSpliterator<E>(ArrayList.this, offset,
//                    offset + this.size, this.modCount);
//        }
    }


}
