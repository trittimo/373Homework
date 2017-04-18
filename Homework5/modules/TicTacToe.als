module TicTacToe

abstract sig Marker {}
one sig Cross extends Marker {}
one sig Nought extends Marker {}


// Represents the board
// a-i correspond to the Marker who placed each piece
sig Board {
  a, b, c,
  d, e, f,
  g, h, i : lone Marker
}

// The state of the game at a given time
one sig GameState {
  board : one Board,
  turn : one Marker
}

pred Win[s : GameState, t : Marker] {
  let bd = s.board |
	(bd.a = t and bd.e = t and bd.i = t) or
	(bd.g = t and bd.e = t and bd.c = t) or
	(bd.a = t and bd.b = t and bd.c = t) or
	(bd.d = t and bd.e = t and bd.f = t) or
	(bd.g = t and bd.h = t and bd.i = t) or
	(bd.a = t and bd.d = t and bd.g = t) or
	(bd.b = t and bd.e = t and bd.h = t) or
	(bd.c = t and bd.f = t and bd.i = t)
}

run Win for exactly 1 GameState, 1 Board,  2 Marker

pred CompleteBoard[bd : Board] {
	some bd.a and
	some bd.b and
	some bd.c and
	some bd.d and
	some bd.e and
	some bd.f and
	some bd.g and
	some bd.h and
	some bd.i
}

pred Draw[s : GameState] {
	no m : Marker | Win[s, m]
	CompleteBoard[s.board]
}

run Draw for exactly 1 GameState, 1 Board, 2 Marker

fun NextTurn[s : GameState] : Marker {
	s.turn = Cross => Nought
	else Cross
}

run NextTurn for exactly 1 GameState, 1 Board, 2 Marker

pred Init[s : GameState, t : Marker] {
	let bd = s.board |
		no bd.a and
		no bd.b and
		no bd.c and
		no bd.d and
		no bd.e and
		no bd.f and
		no bd.g and
		no bd.h and
		no bd.i
}

//fun MarkerCount[bd : Board] : Int {
	
//}

pred Transition[s, s' : GameState] {
	s'.turn = NextTurn[s]
	
}
