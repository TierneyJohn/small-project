/**
 * 主函数
 */

// 游戏数据
var board = new Array();
// 游戏分数
var score = 0;
// 记录事件
var hasConflicted = new Array();

// 触控实现
var startx = 0;
var starty = 0;
var endx = 0;
var endy = 0;

// 主流程
$(document).ready(function() {
	prepareForMobile();
	newgame();
});

// 窗口显示设置
function prepareForMobile() {
	
	// 限定最大边界
	if (documentWidth>500) {
		gridContainerWidth = 500;
		cellSpace = 20;
		cellSideLength = 100;
	}
	
	// 按屏幕比例显示
	$('#grid-container').css('width', gridContainerWidth - 2*cellSpace);
	$('#grid-container').css('height', gridContainerWidth - 2*cellSpace);
	$('#grid-container').css('padding', cellSpace);
	$('#grid-container').css('border-radius', 0.02*gridContainerWidth);
	
	$('.grid-cell').css('width', cellSideLength);
	$('.grid-cell').css('height', cellSideLength);
	$('.grid-cell').css('border-radius', 0.02*cellSideLength);
}

function newgame() {
	// 初始化棋盘格
	init();
	// 随机在两个格子里生成数字
	generateOneNumber();
	generateOneNumber();
}

function init() {
	
	for (var i = 0; i < 4; i++) {
		for (var j = 0; j < 4; j++) {
			// 生成格子
			var gridCell = $('#grid-cell-'+i+"-"+j);
			gridCell.css('top', getPosTop(i,j));
			gridCell.css('left', getPosLeft(i,j));
		}
	}
	for (var i = 0; i < 4; i++) {
		// 生成数值格子
		board[i] = new Array();
		// 生成事件监听器
		hasConflicted[i] = new Array();
		for (var j = 0; j < 4; j++) {
			// 初始化
			board[i][j] = 0;
			hasConflicted[i][j] = false;
		}
	}
	
	updateBoardView();
	
	score = 0;
	$('#score').text(score);
}

// 刷新显示
function updateBoardView() {
	
	$(".number-cell").remove();
	for (var i = 0; i < 4; i++) {
		for (var j = 0; j < 4; j++) {
			$("#grid-container").append('<div class="number-cell" id="number-cell-'+i+'-'+j+'"></div>');
			var theNumberCell = $('#number-cell-'+i+'-'+j);
			
			if (board[i][j] == 0) {
				theNumberCell.css('width', '0px');
				theNumberCell.css('height', '0px');
				theNumberCell.css('top', getPosTop(i,j) + cellSideLength/2);
				theNumberCell.css('left', getPosLeft(i,j) + cellSideLength/2);
			} else {
				theNumberCell.css('width', cellSideLength);
				theNumberCell.css('height', cellSideLength);
				theNumberCell.css('top', getPosTop(i,j));
				theNumberCell.css('left', getPosLeft(i,j));
				theNumberCell.css('background-color', getNumberBackgroundColor(board[i][j]));
				theNumberCell.css('color', getNumberColor(board[i][j]));
				theNumberCell.text(board[i][j]);
			}
			
			hasConflicted[i][j] = false;
		}
	}
	
	$('.number-cell').css('line-height', cellSideLength+'px');
	$('.number-cell').css('font-size', 0.6*cellSideLength+'px');
}

// 随机生成数字
function generateOneNumber() {
	
	// 判断是否能够生成数字
	if (nospace(board)) {
		return false;
	}
	
	// 随机一个位置
	var randx = parseInt(Math.floor(Math.random() *4));
	var randy = parseInt(Math.floor(Math.random() *4));
	
	// 定义计数器，限定次数，优化循环内耗
	var times = 0;
	while (times < 50) {
		if (board[randx][randy] == 0) {
			break;
		}
		// 重复判断
		randx = parseInt(Math.floor(Math.random() *4));
		randy = parseInt(Math.floor(Math.random() *4));
		times++;
	}
	
	// 超出次数，手动寻址
	if (times == 50) {
		for (var i = 0; i < 4; i++) {
			for (var j = 0; j < 4; j++) {
				if (board[i][j] == 0) {
					randx = i;
					randy = j;
				}
			}
		}
	}
	
	// 随机一个数字
	var randNumber = Math.random() < 0.7 ? 2 : 4;
	
	// 在随机位置显示随机数字
	board[randx][randy] = randNumber;
	showNumberWithAnimation(randx, randy, randNumber);
	
	return true;
}

// 监听键盘动作
$(document).keydown(function(event) {
	
	switch (event.keyCode) {
	case 37:// left
		event.preventDefault();
		if (moveLeft()) {
			setTimeout("generateOneNumber()", 210);
			setTimeout("isgameover()", 300);
		}
		break;
	case 38:// up
		event.preventDefault();
		if (moveUp()) {
			setTimeout("generateOneNumber()", 210);
			setTimeout("isgameover()", 300);
		}
		break;
	case 39:// right
		event.preventDefault();
		if (moveRight()) {
			setTimeout("generateOneNumber()", 210);
			setTimeout("isgameover()", 300);
		}
		break;
	case 40:// down
		event.preventDefault();
		if (moveDown()) {
			setTimeout("generateOneNumber()", 210);
			setTimeout("isgameover()", 300);
		}
		break;
	default:// default
		break;
	}
});

/* 
 * 判定触屏状态
 * */
document.addEventListener('touchstart', function(event) {
	startx = event.touches[0].pageX;
	starty = event.touches[0].pageY;
});

document.addEventListener('touchmove', function(event) {
	event.preventDefault();
});

document.addEventListener('touchend', function(event) {
	endx = event.changedTouches[0].pageX;
	endy = event.changedTouches[0].pageY;
	
	// 轨迹判断
	var deltax = endx - startx;
	var deltay = endy - starty;
	
	// 轨迹验证合法
	if (Math.abs(deltax) < 0.3*documentWidth && Math.abs(deltay) < 0.3*documentWidth) {
		return;
	}
	// x
	if (Math.abs(deltax) >= Math.abs(deltay)) {
		
		if(deltax > 0) {
			// move right
			if(moveRight()) {
				setTimeout("generateOneNumber()", 210);
				setTimeout("isgameover()", 300);
			}
		} else {
			// move left
			if (moveLeft()) {
				setTimeout("generateOneNumber()", 210);
				setTimeout("isgameover()", 300);
			}
		}
	} 
	// y
	else {
		
		if (deltay > 0) {
			// move down
			if (moveDown()) {
				setTimeout("generateOneNumber()", 210);
				setTimeout("isgameover()", 300);
			}
		} else {
			// move up
			if (moveUp()) {
				setTimeout("generateOneNumber()", 210);
				setTimeout("isgameover()", 300);
			}
		}
	}
});

// 游戏结束判断
function isgameover(){
	if (nospace(board) && nomove(board)) {
		gameover();
	}
}

// 游戏结束效果
function gameover() {
	alert('game over!');
}

/*
 * 移动操作
 * */
function moveLeft() {
	
	if (!canMoveLeft(board)) {
		return false;
	}
	
	// moveLeft
	for (var i = 0; i < 4; i++) {
		for (var j = 1; j < 4; j++) {
			if(board[i][j] != 0) {
				
				for (var k = 0; k < j; k++) {
					if(board[i][k] == 0 && noBlockHorizontal(i, k, j, board)) {
						// move
						showMoveAnimation(i, j, i, k);
						board[i][k] = board[i][j];
						board[i][j] = 0;
						
						continue;
					} else if (board[i][k] == board[i][j] && noBlockHorizontal(i, k, j, board) && !hasConflicted[i][k]) {
						// move
						showMoveAnimation(i, j, i, k);
						// add
						board[i][k] += board[i][j];
						board[i][j] = 0;
						// add score
						score += board[i][k];
						updateScore(score);
						// change hasConflicted
						hasConflicted[i][k] = true;
						continue;
					}
				}
			}
		}
	}

	setTimeout("updateBoardView()", 200);
	
	return true;
}

function moveUp() {
	
	if(!canMoveUp(board)) {
		return false;
	}
	
	// moveUp
	for (var i = 1; i < 4; i++) {
		for (var j = 0; j < 4; j++) {
			if (board[i][j] != 0) {
				
				for (var k = 0; k < i; k++) {
					if (board[k][j] == 0 && noBlockVertical(j, k, i, board)) {
						// move
						showMoveAnimation(i, j, k, j);
						board[k][j] = board[i][j];
						board[i][j] = 0;
						
						continue;
					} else if (board[k][j] == board[i][j] && noBlockVertical(j, k, i, board) && !hasConflicted[k][j]) {
						// move
						showMoveAnimation(i, j, k, j);
						// add
						board[k][j] += board[i][j];
						board[i][j] = 0;
						// add score
						score += board[k][j];
						updateScore(score);
						// change hasConflicted
						hasConflicted[k][j] = true;
						continue;
					}
				}
			}
		}
	}

	setTimeout("updateBoardView()", 200);
	
	return true;
}

function moveRight() {
	
	if (!canMoveRight(board)) {
		return false;
	}
	
	// moveRight
	for (var i = 0; i < 4; i++) {
		for (var j = 2; j >= 0; j--) {
			if (board[i][j] != 0) {
				
				for (var k = 3; k > j; k--) {
					if (board[i][k] == 0 && noBlockHorizontal(i, j, k, board)) {
						// move
						showMoveAnimation(i, j, i, k);
						board[i][k] = board[i][j];
						board[i][j] = 0;
						
						continue;
					} else if (board[i][k] == board[i][j] && noBlockHorizontal(i, j, k, board) && !hasConflicted[i][k]) {
						// move
						showMoveAnimation(i, j, i, k);
						// add
						board[i][k] += board[i][j];
						board[i][j] = 0;
						// add score
						score += board[i][k];
						updateScore(score);
						// change hasConflicted
						hasConflicted[i][k] = true;
						continue;
					}
				}
			}
		}
	}

	setTimeout("updateBoardView()", 200);
	
	return true;
}

function moveDown() {
	
	if (!canMoveDown(board)) {
		return false;
	}
	
	// moveDown
	for (var i = 2; i >= 0; i--) {
		for (var j = 0; j < 4; j++) {
			if (board[i][j] != 0) {
				
				for (var k = 3; k > i; k--) {
					if (board[k][j] == 0 && noBlockVertical(j, i, k, board)) {
						// move
						showMoveAnimation(i, j, k, j);
						board[k][j] = board[i][j];
						board[i][j] = 0;
						
						continue;
					} else if (board[k][j] == board[i][j] && noBlockVertical(j, i, k, board) && !hasConflicted[k][j]) {
						// move
						showMoveAnimation(i, j, k, j);
						// add
						board[k][j] += board[i][j];
						board[i][j] = 0;
						// add score
						score += board[k][j];
						updateScore(score);
						// change hasConflicted
						hasConflicted[k][j] = true;
						continue;
					}
				}
			}
		}
	}

	setTimeout("updateBoardView()", 200);
	
	return true;
}