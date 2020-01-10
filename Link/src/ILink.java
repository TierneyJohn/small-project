/**
 * 链表操作接口
 * 使用该链表是应注意数据类型是否合理重写了equals()和compareTo()方法
 *
 * @param <T> 任意引用数据类型
 * @author john
 * @version 1.0
 */
public interface ILink<T extends Comparable<T>> {

    /**
     * 实现数据的添加
     * 如果要添加的数据不存在，将抛出"添加的数据为空"异常
     * 如果要添加的数据已存在，将抛出"要添加的数据已存在"异常
     *
     * @param data 要增加的数据
     */
    void add(Comparable<T> data);

    /**
     * 实现数据的删除操作
     * 如果输入的数据为空或者要删除的数据不存在，将抛出"要删除的数据不存在"异常
     *
     * @param data 要删除的数据
     */
    void remove(Comparable<T> data);

    /**
     * 实现数据的更新操作
     * 如果更新后的数据为空，将抛出"警告：更新后的数据为空，如要执行删除操作，请使用remove()方法"异常
     * 如果更新前的数据为空或者不存在，将抛出"更新前的数据不存在"异常
     * 如果更新后的数据已存在，将抛出"更新后的数据已存在"异常
     *
     * @param oldData 更新前的数据
     * @param newData 更新后的数据
     */
    void update(Comparable<T> oldData, Comparable<T> newData);

    /**
     * 实现数据的查询操作
     *
     * @param data 要查询的数据
     * @return 如果数据存在返回true，否则返回false
     */
    boolean query(Comparable<T> data);


    /**
     * 实现链表输出
     *
     * @return 链表对象数组, 如果没有值返回空
     */
    Object[] toArray();

    /**
     * 清空链表
     * 暂时不可恢复，清空操作会使原有数据变成垃圾，建议手动清理
     */
    void clean();
}

/**
 * 链表操作接口实现类
 *
 * @param <T> 任意引用数据类型
 */
class LinkImpl<T extends Comparable<T>> implements ILink<T> {
    /*
     * count: 保存节点个数
     * root: 保存根节点
     * returnData: 保存节点数据的数组
     * foot: 记录角标
     * */
    private static int count = 0;
    private Node root;
    private Object[] returnData;
    private int foot = 0;

    @Override
    public void add(Comparable<T> data) {
        // 判断传入数据是否合法
        if (data == null) {
            // 传出数据为空，抛出异常
            throw new NullPointerException("添加的数据为空");
        } else if (query(data)) {
            // 判断传入的数据是否存在，若存在
            throw new RuntimeException("要添加的数据已存在");
        } else {
            // 数据不存在，实例化传入数据，并封装在Node类中
            Node newNode = new Node(data);
            // 判断是否为根节点
            if (this.root == null) {
                // 根节点为空，直接保存为根节点
                this.root = newNode;
            } else {
                // 不是根节点，将增加与root节点进行比较
                if (newNode.data.compareTo((T) this.root.data) < 0) {
                    // 当前节点比根节点小，更换根节点
                    newNode.child = this.root;
                    this.root.parent = newNode;
                    this.root = newNode;
                } else {
                    // 当前节点比根节点大，调用Node中的方法进行数据添加
                    this.root.addNode(newNode);
                }
            }
            // 更新节点个数
            count++;
        }
    }

    @Override
    public void remove(Comparable<T> data) {
        // 判断传入的数据是否合法
        if (!query(data)) {
            // 传入的数据不存在，抛出异常
            throw new NullPointerException("要删除的数据不存在");
        } else {
            // 要删除的数据存在，实例化传入数据，并封装在Node类中
            Node deleteNode = new Node(data);
            // 调用Node中的方法执行删除操作
            this.root.removeNode(deleteNode);
            // 更新节点个数
            count--;
        }
    }

    @Override
    public void update(Comparable<T> oldData, Comparable<T> newData) {
        // 判断传入数据是否合法
        if (newData == null) {
            throw new RuntimeException("警告：更新后的数据为空，如要执行删除操作，请使用remove()方法");
        } else if (!query(oldData)) {
            throw new RuntimeException("更新前的数据不存在");
        } else if (query(newData)) {
            throw new RuntimeException("更新的数据已存在");
        } else {
            // 更新数据合法，实例化传入数据，并封装在Node类中
            // 调用Node中的方法进行更新
            this.root.queryNode(new Node(oldData)).data = newData;
        }
    }

    @Override
    public boolean query(Comparable<T> data) {
        // 判断传入的数据或根节点是否为空
        if (data == null || this.root == null) {
            return false;
        }
        // 实例化传入数据，并封装在Node类中
        Node estimateNode = new Node(data);
        // 调用Node中的方法进行判断
        if (this.root.queryNode(estimateNode) != null) {
            // 有返回值，找到了该对象
            return true;
        }
        // 没有找到该对象
        return false;

    }

    @Override
    public Object[] toArray() {
        // 判断链表中是否有数据
        if (this.root == null) {
            // 链表中没有数据
            return null;
        }
        // 链表中有数据,实例化对象数组
        this.returnData = new Object[this.count];
        // 角标清零
        this.foot = 0;
        // 调用Node中的方法实现遍历
        this.root.toArrayNode();
        return returnData;
    }

    @Override
    public void clean() {
        this.root = null;
        count = 0;
    }

    /**
     * 定义Node内部类，用于保存节点
     */
    private class Node {

        /*
         * data: 节点存放的数据
         * parent:保存父节点
         * child: 保存子节点
         * */
        private Comparable<T> data;
        private Node parent;
        private Node child;

        private Node(Comparable<T> data) {
            this.data = data;
        }

        /**
         * 找到合适的位置添加数据
         *
         * @param newNode 要添加的数据
         */
        private void addNode(Node newNode) {
            if (newNode.data.compareTo((T) this.data) < 0) {
                // 要添加的节点数据比当前节点数据小，执行添加操作，将要添加的节点插入到当前节点与父节点之间
                newNode.parent = this.parent;
                newNode.child = this;
                this.parent.child = newNode;
                this.parent = newNode;
            } else {
                // 要添加的节点数据比当前节点数据大，向后遍历重新判断
                if (this.child == null) {
                    // 没有后续节点,将要添加的节点保存在最后
                    this.child = newNode;
                    newNode.parent = this;
                } else {
                    // 还有后续节点，递归调用
                    this.child.addNode(newNode);
                }
            }

        }

        /**
         * 找到要删除的数据执行删除操作
         *
         * @param deleteNode 要删除的节点
         */
        private void removeNode(Node deleteNode) {
            // 执行数据删除,设置被删除节点的父子节点引用，并将被删除节点的引用至空
            queryNode(deleteNode).parent.child = queryNode(deleteNode).child;
        }

        /**
         * 找到数据相同的Node
         *
         * @param estimateNode 要进行对比的Node
         * @return 如果找到返回该Node，如果没找到返回null
         */
        private Node queryNode(Node estimateNode) {
            // 进行条件判断
            if (estimateNode.data.equals(this.data)) {
                // 返回该节点
                return this;
            }
            // 判断是否还有后续节点
            if (this.child != null) {
                // 还有后续节点,递归调用判断
                return this.child.queryNode(estimateNode);
            }
            // 没有后续节点，返回空
            return null;


        }

        /**
         * 将Node中的数据有序保存在returnData数组中
         */
        private void toArrayNode() {
            // 保存当前数据
            LinkImpl.this.returnData[LinkImpl.this.foot++] = this.data;
            // 遍历至末端节点
            if (this.child != null) {
                // 还有后续节点,递归调用
                this.child.toArrayNode();
            }
        }
    }
}