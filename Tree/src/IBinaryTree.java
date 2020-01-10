/**
 * 二叉树数据结构接口
 *
 * @param <T> 任意引用类型数据
 * @author john
 * @version 1.0
 */
public interface IBinaryTree<T extends Comparable<T>> {
    /**
     * 实现数据的添加操作
     * 如果要添加的数据为空，则抛出"要增加的数据为空"异常
     * 如果要添加的数据已存在，则抛出"要添加的数据已存在"异常
     *
     * @param data Comparable<T>类型数据
     */
    void add(Comparable<T> data);

    /**
     * 实现数据的删除操作
     * 如果要删除的数据为空或者根节点为空，或者要删除的数据不存在，则抛出"要删除的数据不存在"异常
     *
     * @param data Comparable<T>类型数据
     */
    void remove(Comparable<T> data);

    /**
     * 实现数据的更新操作
     * 如果更新后的数据为空，则抛出"警告：更新后的数据为空，如要执行删除操作，请使用remove()方法"异常
     * 如果更新前的数据不存在，则抛出"更新前的数据不存在"异常
     * 如果更新后的数据已存在，则抛出"更新的数据已存在"异常
     *
     * @param oldData 更新前数据
     * @param newData 更新后数据
     */
    void update(Comparable<T> oldData, Comparable<T> newData);

    /**
     * 实现数据的查询操作
     *
     * @param data Comparable<T>类型数据
     * @return 如果存在返回true，否则返回false
     */
    boolean query(Comparable<T> data);

    /**
     * 将BinaryTree内的Node数据保存为对象数组
     *
     * @return 如果树不存在返回null，否则返回对象数组
     */
    Object[] toArray();

    /**
     * 清空树
     */
    void clean();
}

class BinaryTreeImpl<T extends Comparable<T>> implements IBinaryTree<T> {
    /*
     * count: 用来保存节点个数
     * root: 用来存放根节点
     * returnData: 用来存放对象数组
     * foot: 用来保存角标
     */
    private static int count = 0;
    private Node root;
    private Object[] returnData;
    private int foot;

    @Override
    public void add(Comparable<T> data) {
        // 判断传入数据是否合法
        if (data == null) {
            // 传入数据为空，抛出异常
            throw new NullPointerException("要增加的数据为空");
        } else if (query(data)) {
            // 传入数据重复，抛出异常
            throw new RuntimeException("要添加的数据已存在");
        } else {
            // 传入数据合法，实例化当前对象
            Node newNode = new Node(data);
            // 判断根节点是否存在
            if (this.root == null) {
                // 根节点不存在，当前节点作为根节点
                this.root = newNode;
            } else {
                // 根节点已存在，调用Node方法判断
                this.root.addNode(newNode);
            }
            // 节点个数更新
            count++;
        }
    }

    @Override
    public void remove(Comparable<T> data) {
        // 判断传入数据是否合法
        if (!query(data)) {
            throw new NullPointerException("要删除的数据不存在");
        } else {
            // 要删除的数据存在，判断要删除的数据是否为根
            if (data.equals(this.root.data)) {
                // 要删除的数据为根
                this.root.removeRoot();
            } else {
                // 要删除的数据不是根，调用Node方法删除
                this.root.queryNode(new Node(data)).removeNode();
            }
            // 节点个数更新
            count--;
        }
    }

    @Override
    public void update(Comparable<T> oldData, Comparable<T> newData) {
        // 判断出入数据是否合法
        if (newData == null) {
            // 传入数据为空抛出异常
            throw new RuntimeException("警告：更新后的数据为空，如要执行删除操作，请使用remove()方法");
        } else if (!(query(oldData))) {
            // 更新前的数据不存在
            throw new NullPointerException("更新前的数据不存在");
        } else if (query(newData)) {
            // 更新后的数据重复
            throw new RuntimeException("更新的数据已存在");
        } else {
            // 输入的数据合法
            if (oldData.equals(root.data)) {
                // 要更新的数据为根数据
                this.root.removeRoot();
            } else {
                // 要更新的数据不是根数据
                this.root.queryNode(new Node(oldData)).removeNode();
            }
            this.root.addNode(new Node(newData));
        }
    }

    @Override
    public boolean query(Comparable<T> data) {
        // 判断传入数据或根节点是否为空
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
        // 判断根节点是否存在
        if (this.root == null) {
            // 根节点不存在，返回空
            return null;
        }
        // 实例化对象数组
        this.returnData = new Object[this.count];
        // 角标清零
        this.foot = 0;
        // 调用Node中的方法
        this.root.toArrayNode();
        return this.returnData;
    }

    @Override
    public void clean() {
        this.root = null;
        count = 0;
    }

    /**
     * 私有内部类Node用来封装要存储的数据
     */
    private class Node {

        /*
         * data: 用来存放数据
         * parent: 用来保存父节点引用
         * left: 用来保存左子节点引用
         * right: 用来保存右子节点引用
         * */
        private Comparable<T> data;
        private Node parent;
        private Node left;
        private Node right;

        private Node(Comparable<T> data) {
            this.data = data;
        }

        /**
         * 找到当前节点的最左节点
         *
         * @return 最左节点
         */
        private Node leftNode() {
            // 判断是否为最小Node
            if (this.left == null) {
                // 左子节点为空，当前节点为最左节点
                return this;
            } else {
                // 左子节点不为空，递归调用找到最左节点
                return this.left.leftNode();
            }
        }

        /**
         * 找到当前节点的最右节点
         *
         * @return 最右节点
         */
        private Node rightNode() {
            // 判断是否为最大Node
            if (this.right == null) {
                // 右子节点为空，当前节点为最右节点
                return this;
            } else {
                // 右子节点不为空，递归调用找到最右节点
                return this.right.rightNode();
            }

        }

        /**
         * 执行节点的添加操作
         * 将要操作的节点至为调用节点下的合适位置
         *
         * @param newNode 要添加的节点
         */
        private void addNode(Node newNode) {
            // 判断传入节点是否为最值节点
            if (newNode.data.compareTo((T) this.leftNode().data) < 0) {
                // 传入节点比当前节点下最左节点小，将传入节点至为最左节点
                newNode.parent = this.leftNode();
                this.leftNode().left = newNode;
            } else if (newNode.data.compareTo((T) this.rightNode().data) >= 0) {
                // 传入节点比当前节点最右节点大，将传入节点至为最右节点
                newNode.parent = this.rightNode();
                this.rightNode().right = newNode;
            } else {
                if (newNode.data.compareTo((T) this.data) < 0) {
                    // 传入的节点比当前节点小,交由当前节点的左子节点判断
                    this.left.addNode(newNode);
                } else {
                    // 传入的数据比当前节点大或相等，交由当前节点的右子节点判断
                    this.right.addNode(newNode);
                }
            }
        }

        /**
         * 执行节点的移除操作
         * <div>
         *  示例1：若被删除节点为叶子节点
         *  设被删除节点为D，,其兄弟节点为B，其父节点为P，其叔叔节点为S，其爷爷节点为Y
         * <p>
         *               Y                       Y
         *             /  \                    /  \
         *           /     \                 /     \
         *         P       S               S       P
         *       /  \     /  \           /  \    /  \
         *     /     \                         /     \
         *    D       B                      B        D
         *          /  \                   /  \
         *            图.a                     图.b
         * <p>
         *     此情况下删除节点D对原有结构不造成影响可直接删除，删除时考虑节点D与节点P的关系即可
         * </div>
         * <div>
         *  示例2：若被删除节点为单分支节点
         *  设被删除节点为D，,其兄弟节点为B，其父节点为P，其子节点为Z
         * <p>
         *               P                       P
         *             /  \                    /  \
         *           /     \                 /     \
         *         D       B               B       D
         *       /        /  \           /  \    /
         *     /                               /
         *    Z                               Z
         *  /  \                            /  \
         *            图.c                     图.d
         * <p>
         *               P                       P
         *             /  \                    /  \
         *           /     \                 /     \
         *         D       B               B       D
         *          \     /  \           /  \       \
         *           \                               \
         *            Z                               Z
         *          /  \                            /  \
         *            图.e                     图.f
         * <p>
         *     此情况下删除节点D，需要将节点D的子节点Z顶替到节点D的位置，顶替时需要考虑节点D与节点P以及节点Z的关系
         * </div>
         * <div>
         *  示例3：若被删除节点为双分支节点
         *  设被删除节点为D，,其兄弟节点为B，其父节点为P，其左子节点为L，右子节点为R
         * <p>
         *               P                       P
         *             /  \                    /  \
         *           /     \                 /     \
         *         D       B               B       D
         *       /  \     /  \           /  \    /  \
         *     /     \                         /     \
         *    L       R                       L       R
         *  /  \    /  \                    /  \    /  \
         *            图.g                     图.h
         * <p>
         *     此情况下删除节点D，如果直接用子节点顶替，情况会比较复杂，因此使用节点R的最左子节点来顶替节点D比较方便
         * </div>
         */
        private void removeNode() {
            // 判断条件
            if (this.left == null && this.right == null) {
                // 满足条件1，当前节点为叶子节点,判断与父节点的连接关系
                if (this.parent.left == this) {
                    // 满足图.a，当前节点为父节点的左子节点
                    this.parent.left = null;
                    this.parent = null;
                } else {
                    // 满足图.b，当前节点为父节点的右子节点
                    this.parent.right = null;
                    this.parent = null;
                }
            } else if (this.left != null && this.right == null) {
                // 满足条件2，当前节点为单分支节点，并且子节点在左侧
                if (this.parent.left == this) {
                    // 满足图.c，当前节点为父节点的左子节点
                    this.parent.left = this.left;
                    this.left.parent = this.parent;
                    this.parent = null;
                    this.left = null;
                } else {
                    // 满足图.d，当前节点为父节点的右子节点
                    this.parent.right = this.left;
                    this.left.parent = this.parent;
                    this.parent = null;
                    this.left = null;
                }
            } else if (this.left == null && this.right != null) {
                // 满足条件2，当前节点为单分支节点，并且子节点在右侧
                if (this.parent.left == this) {
                    // 满足图.e，当前节点为父节点的左子节点
                    this.parent.left = this.right;
                    this.right.parent = this.parent;
                    this.parent = null;
                    this.right = null;
                } else {
                    // 满足图.f，当前节点为父节点的右子节点
                    this.parent.right = this.right;
                    this.right.parent = this.parent;
                    this.parent = null;
                    this.right = null;
                }
            } else {
                // 满足条件3，当前节点为双分支节点，图.g，图.f的执行方法相同
                // 数据值传递
                this.data = this.right.leftNode().data;
                // 删除末端节点
                this.right.leftNode().removeNode();
            }
        }

        /**
         * 执行删除根节点操作
         */
        private void removeRoot() {
            // 对根下节点进行判断
            if (BinaryTreeImpl.this.root.left == null && BinaryTreeImpl.this.root.right == null) {
                // 根下无数据，直接清空根
                BinaryTreeImpl.this.root = null;
            } else if (BinaryTreeImpl.this.root.left != null && BinaryTreeImpl.this.root.right == null) {
                // 根只有左子分支,将根替换为左子节点
                BinaryTreeImpl.this.root = BinaryTreeImpl.this.root.left;
                BinaryTreeImpl.this.root.parent = null;
            } else {
                // 根有右分支,进行数值传递，然后删除末端节点
                BinaryTreeImpl.this.root.data = BinaryTreeImpl.this.root.right.leftNode().data;
                BinaryTreeImpl.this.root.right.leftNode().removeNode();
            }
        }

        /**
         * 在当前节点下找到数据相同的Node
         *
         * @param estimateNode 要进行对比的Node
         * @return 如果找到返回该Node，如果没找到返回null
         */
        private Node queryNode(Node estimateNode) {
            if (estimateNode.data.equals(this.data)) {
                // equals判断相同，找到并返回该Node对象
                return this;
            }
            if (estimateNode.data.compareTo((T) this.data) >= 0) {
                // 要对比的对象数据大于等于当前对象
                if (this.right == null) {
                    // 没有右子节点，返回空
                    return null;
                }
                // 还有右子节点，递归调用判断
                return this.right.queryNode(estimateNode);
            } else {
                // 要对比的对象数据小于当前对象
                if (this.left == null) {
                    // 没有左子节点，返回空
                    return null;
                }
                // 还有左子节点，递归调用判断
                return this.left.queryNode(estimateNode);
            }
        }

        /**
         * 将当前节点下的全部数据以对象数组的形式返回
         */
        private void toArrayNode() {
            // 判断当前节点是否有左子节点
            if (this.left != null) {
                // 当前节点仍有左子节点，递归交由左子节点判断
                this.left.toArrayNode();
            }
            // 当前节点无左子节点，即当前节点为最小节点，保存节点数据
            BinaryTreeImpl.this.returnData[BinaryTreeImpl.this.foot++] = this.data;
            // 判断当前节点是否有右子节点
            if (this.right != null) {
                // 当前节点仍有右子节点，递归交由右子节点判断
                this.right.toArrayNode();
            }
        }
    }
}