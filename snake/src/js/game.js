/**
 * 游戏类
 *
 * @Author John
 * @Date 2020/8/25下午8:45
 */
class Game {

    /**
     * @see gameDiv game的dom对象
     * @see gameDivs game内部的div数组对象
     * @see timeDiv dom对象
     * @see scoreDiv dom对象
     */
    gameDiv
    gameDivs = []
    timeDiv
    scoreDiv
    /**
     * @see snake snake对象数组，保存所有的snake节点
     * @see nodes 空节点对象数组，保存所有的node节点
     * @see count 保存node节点的数量，它起到一下几个作用：
     * 1. 记录生成节点的数量
     * 2. 记录当前节点的索引
     * 3. 通过该值记录分数信息
     * @see gameOver 定义当前游戏状态,默认为false
     */
    snake = []
    nodes = []
    count = 0
    gameOver = false
    /**
     * @see util 实例化utils类 (当前通过export导出模块的方式有些小问题)
     * @type {Utils}
     */
    util = new Utils()
    /**
     * @see enum 实例化enum类
     * @type {Enum}
     */
    enum = new Enum()

    constructor(dom) {
        this.gameDiv = dom.gameDiv
        this.timeDiv = dom.timeDiv
        this.scoreDiv = dom.scoreDiv
        this._initDiv()
        this._initSnake()
    }

    /**
     * 初始化div矩阵方法
     *
     * @private
     */
    _initDiv() {
        for (let i = 0; i < 20; i++) {
            const divs = []
            for (let j = 0; j < 20; j++) {
                const newNode = document.createElement('div')
                newNode.className = 'none'
                newNode.style.left = 20 * j + 'px'
                newNode.style.top = 20 * i + 'px'
                this.gameDiv.appendChild(newNode)
                divs.push(newNode)
            }
            this.gameDivs.push(divs)
        }
    }

    /**
     * 初始化snake方法
     * 初始化第一个node节点
     *
     * @private
     */
    _initSnake() {
        // 初始化3个snake节点
        const firstSnake = new Snake(10, 10)
        const secondSnake = new Snake(11, 10)
        const thirdSnake = new Snake(12, 10)
        // 节点绑定
        this.util.bindNode(firstSnake, secondSnake)
        this.util.bindNode(secondSnake, thirdSnake)
        // 节点保存在snake数组中
        this.snake.push(firstSnake)
        this.snake.push(secondSnake)
        this.snake.push(thirdSnake)
        // 生成空节点
        this.util.createNode(this.gameDivs, this.snake, this.nodes)
        this._refreshDiv()
    }

    /**
     * 移动操作
     */
    left() {
        /**
         * 在待移动位置创建一个新的snake对象，保存在头snake节点的topNode中
         */
        this.snake[0].topNode =
            new Snake(this.snake[0].index.x - 1, this.snake[0].index.y)
        let result = this.util.canMove(this.gameDivs, this.snake, this.nodes, this.count)
        this._resultJudgment(result)
    }

    up() {
        this.snake[0].topNode =
            new Snake(this.snake[0].index.x, this.snake[0].index.y - 1)
        let result = this.util.canMove(this.gameDivs, this.snake, this.nodes, this.count)
        this._resultJudgment(result)
    }

    right() {
        this.snake[0].topNode =
            new Snake(this.snake[0].index.x + 1, this.snake[0].index.y)
        let result = this.util.canMove(this.gameDivs, this.snake, this.nodes, this.count)
        this._resultJudgment(result)
    }

    down() {
        this.snake[0].topNode =
            new Snake(this.snake[0].index.x, this.snake[0].index.y + 1)
        let result = this.util.canMove(this.gameDivs, this.snake, this.nodes, this.count)
        this._resultJudgment(result)
    }

    /**
     * 刷新显示时间信息
     *
     * @param time 接收local方法中定义的time
     */
    setTime(time) {
        this.timeDiv.innerHTML = time
    }

    /**
     * 刷新显示分数信息
     */
    setScore() {
        this.scoreDiv.innerHTML = this.count * 10
    }

    /**
     * 刷新显示
     *
     * @private
     */
    _refreshDiv() {
        // 将所有节点至为'none'
        this.gameDivs.forEach(divs => {
            divs.forEach(div => {
                div.className = 'none'
            })
        })
        // 遍历snake数组，将snake节点显示为'snake'
        this.snake.forEach(node => {
            this.gameDivs[node.index.y][node.index.x].className = 'snake'
        })
        // 通过count将nodes数组中的当前节点显示为'node'
        this.gameDivs[this.nodes[this.count].index.y][this.nodes[this.count].index.x].className = 'node'
    }

    /**
     * canMove返回结果判断
     *
     * @param result canMove方法返回结果
     * @private
     */
    _resultJudgment(result) {
        switch (result) {
            case this.enum.MOVE_AND_EAT :
                // 计数器累加
                this.count++
                break
            case this.enum.MOVE_NO_EAT :
                break
            case this.enum.NO_MOVE :
                // 无法移动，游戏结束
                this.gameOver = true
                break
            default :
                break
        }
        this._refreshDiv()
    }
}