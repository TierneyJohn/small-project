/**
 * 主体方法逻辑
 *
 * @Author John
 * @Date 2020/8/25下午8:47
 */
var Local = function () {

    /**
     * @see startDiv 初始化start按钮
     * @see game 初始化game对象
     * @see move 保存当前移动方向,默认向左
     * @type {string}
     */
    let startDiv
    let game
    let move = "left"
    /**
     * 时间对象参数
     *
     * @see time 保存当前时间
     * @see count 计数器参数
     * @type {number}
     *
     * @see timer 时间定时器执行对象
     */
    let time = 0
    let count = 0
    let timer = null

    /**
     * 初始化方法
     */
    const init = function () {
        // 绑定document对象
        startDiv = document.getElementById('start')
        // 对start按钮绑定开始游戏事件
        startDiv.onclick = function () {
            start()
        }
    }
    // 默认执行init()方法
    init()

    /**
     * 键盘事件绑定
     *
     * @see move 在执行键盘事件后，绑定当前移动方向
     * @if 如果键盘事件执行方向与当前运动方向相反，则不执行相应事件
     */
    const bindKeyEvent = function () {
        document.onkeydown = function (e) {
            switch (e.keyCode) {
                case 37 : // left
                    if (move !== "right") {
                        game.left()
                        move = "left"
                    }
                    break
                case 38 : // up
                    if (move !== "down") {
                        game.up()
                        move = "up"
                    }
                    break
                case 39 : // right
                    if (move !== "left") {
                        game.right()
                        move = "right"
                    }
                    break
                case 40 : // down
                    if (move !== "up") {
                        game.down()
                        move = "down"
                    }
                    break
                default :
                    break
            }
        }
    }

    /**
     * 计时函数
     * 实现与时间相关的方法
     */
    const timeFunc = function () {
        count++
        if (count % 2 === 0) {
            count = 0
            game.setTime(time++)
        }
        game.setScore()
        if (game.gameOver) {
            gameOver()
        } else {
            keepMove(move)
        }
    }

    /**
     * 移动保持,通过时间函数自动调用移动方法
     *
     * @param move 当前移动方向
     */
    const keepMove = function (move) {
        switch (move) {
            case "left" :
                game.left()
                break
            case "up" :
                game.up()
                break
            case "right" :
                game.right()
                break
            case "down" :
                game.down()
                break
            default :
                break
        }
    }

    /**
     * 开始游戏主方法
     */
    const start = function () {
        /**
         * 获取相应dom对象
         *
         * @type {{scoreDiv: HTMLElement, gameDiv: HTMLElement, timeDiv: HTMLElement}}
         */
        let dom = {
            gameDiv: document.getElementById('game'),
            timeDiv: document.getElementById("time"),
            scoreDiv: document.getElementById("score"),
        }
        game = new Game(dom)
        bindKeyEvent()
        timer = setInterval(timeFunc, 500)
    }

    /**
     * 游戏结束执行事件
     */
    const gameOver = function () {
        clearInterval(timer)
        timer = null
        alert("game over!" + "\n" + "您存活了：" + time + "s    得分为：" + game.count * 10 + "分")
        game = null
    }
}
// 自动执行local方法
Local()