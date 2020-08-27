/**
 * 工具类
 * 保存相关逻辑方法
 *
 * @Author John
 * @Date 2020/8/25下午10:42
 */
class Utils {

    /**
     * 实例化enum对象
     *
     * @type {Enum}
     */
    enum = new Enum()

    /**
     * 节点绑定
     *
     * @param topNode 父节点
     * @param nextNode 子节点
     */
    bindNode(topNode, nextNode) {
        topNode.nextNode = nextNode
        nextNode.topNode = topNode
    }

    /**
     * 移动判断
     *
     * @param gameDivs div矩阵
     * @param snake snake对象数组
     * @param nodes node对象数组
     * @param count count计数
     * @returns {number}
     * NO_MOVE:0 判断失败，不能执行移动操作；
     * MOVE_NO_EAT:1 移动成功但是没有执行eat操作；
     * MOVE_AND_EAT:2 移动成功且成功执行eat操作
     */
    canMove(gameDivs, snake, nodes, count) {
        // 保存待移动节点的index值
        const index = snake[0].topNode.index
        if (!gameDivs[index.y]) {
            return this.enum.NO_MOVE
        }
        if (!gameDivs[index.y][index.x]) {
            return this.enum.NO_MOVE
        }
        if (gameDivs[index.y][index.x].className === 'snake') {
            return this.enum.NO_MOVE
        }
        /**
         * 所有条件判断成功，可以执行移动操作
         */
        const temp = this._eat(gameDivs, snake, nodes, count)
        // 不论eat执行成功与否，都执行move操作
        this._move(snake)
        if (temp) {
            // eat操作执行成功
            return this.enum.MOVE_AND_EAT
        } else {
            // eat执行失败
            return this.enum.MOVE_NO_EAT
        }
    }

    /**
     * 生成随机节点
     *
     * @param gameDivs gameDivs对象
     * @param snake snake数组
     * @param nodes node数组
     */
    createNode(gameDivs, snake, nodes) {
        const newNode = this._createRandomSnake(snake)
        gameDivs[newNode.index.y][newNode.index.x].className = 'node'
        nodes.push(newNode)
    }

    /**
     * 生成随机snake对象
     *
     * @param snake snake对象数组
     * @returns {Snake}
     * @private
     */
    _createRandomSnake(snake) {
        const x = Math.ceil(Math.random() * 20) - 1;
        const y = Math.ceil(Math.random() * 20) - 1;
        const newSnake = new Snake(x, y)
        snake.forEach(node => {
            if (this._indexJudgment(node, newSnake)) {
                return this._createRandomSnake(snake)
            }
        })
        return newSnake
    }

    /**
     * 节点遍历的移动操作
     *
     * @param snake snake对象数组
     * @private
     */
    _move(snake) {
        for (let i = snake.length - 1; i > 0; i--) {
            snake[i].topNode = snake[i - 1].topNode
            snake[i].index = snake[i - 1].index
        }
        snake[0].index = snake[0].topNode.index
    }

    /**
     * "吃"空节点操作
     *
     * @param gameDivs gameDiv对象数组
     * @param snake snake对象数组
     * @param nodes node对象数组
     * @param count 当前记录值
     * @returns {boolean} 执行结果
     * @private
     */
    _eat(gameDivs, snake, nodes, count) {
        /**
         * 判断待移动节点和当前空节点坐标值
         */
        const temp = this._indexJudgment(snake[0].topNode, nodes[count])
        if (!temp) {
            // 不是相同节点，直接返回false
            return false
        }
        /**
         * 节点坐标相同，执行eat操作
         */
        // 将当前空节点添加到snake对象数组的尾部
        snake.push(nodes[count])
        // 将当前空节点与snake尾节点进行绑定
        this.bindNode(snake[snake.length - 1], nodes[count])
        // 创建新的空节点对象
        this.createNode(gameDivs, snake, nodes)
        return true
    }

    /**
     * 节点index判断
     *
     * @param firstNode 第一个节点
     * @param secondNode 第二个节点
     * @returns {boolean|boolean} 是否相同
     * @private
     */
    _indexJudgment(firstNode, secondNode) {
        return firstNode.index.x === secondNode.index.x
            && firstNode.index.y === secondNode.index.y
    }
}