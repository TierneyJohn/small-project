/**
 * 蛇模型类
 *
 * @Author John
 * @Date 2020/8/25下午8:47
 */
class Snake {
    /**
     * @see topNode 保存上一节点对象，如果当前节点为头节点，则保存预移动节点
     * @see nextNode 保存下一节点对象，如果当前节点为尾节点，则为空
     * @see index 保存自身所在坐标
     */
    topNode
    nextNode
    index = {
        x: 0,
        y: 0
    }

    constructor(x, y) {
        this.index.x = x
        this.index.y = y
    }
}