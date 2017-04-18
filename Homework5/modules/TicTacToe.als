module TicTacToe

open util/ordering[GameState]

abstract sig Marker {}
one sig Cross extends Marker {}
one sig Nought extends Marker {}

abstract sig Cell {}
one sig C0, C1, C2,
			 C3, C4, C5,
			 C6, C7, C8 extends Cell {}


sig Board {
	grid : Cell -> lone Marker
}

// The state of the game at a given time
sig GameState {
  board : one Board,
  turn : one Marker
}


// True if the given cells are occupied by the given type of Marker
pred Board.IsMarker[cells : set Cell, t : Marker] {
	all c : cells | c.(this.grid) = t
}

// True if the board contains the given cell
pred Board.Contains[c : Cell] {
	c in Marker.~(this.grid)
}

// True if the given marker has three in a row
pred GameState.Win[t : Marker] {
	let bd = this.board |
		// Rows
		bd.IsMarker[C0 + C1 + C2, t] or
		bd.IsMarker[C3 + C4 + C5, t] or
		bd.IsMarker[C6 + C7 + C8, t] or
		
		// Columns
		bd.IsMarker[C0 + C3 + C6, t] or
		bd.IsMarker[C1 + C4 + C7, t] or
		bd.IsMarker[C2 + C5 + C8, t] or

		// Diagonals
		bd.IsMarker[C0 + C4 + C8, t] or
		bd.IsMarker[C2 + C4 + C6, t]
}

// Specifies the board is fully filled (i.e. the game is definitively over)
pred GameState.End[] {
	all c : Cell | this.board.Contains[c]
}

// True if the board is fully filled and there is no winner
pred GameState.Draw[t : Marker] {
	this.End[]
	no t : Marker | this.Win[t]
}

// Returns the next turn for the state
fun GameState.NextTurn[] : Marker {
	this.turn = Cross => Nought
	else Cross
}

// Specifies what the board should look like initially
// Should just have a board with an empty grid
pred GameState.Init[t : Marker] {
	no this.board.grid
	this.turn = t
}


pred Progress[g, g' : Cell -> lone Marker] {
	#(g' - g) = 1 and no (g - g')
}


// Specifies what a transition from one state to another looks like
// The only difference should be a single additional piece in g'
// The type of the new piece should correspond to the turn
pred Transition[s, s' : GameState] {
	s'.turn = s.NextTurn[]
	let g = s.board.grid, g' = s'.board.grid |
		(Progress[g, g'] and Cell.(g' - g) = s'.turn) or
		s.End[]
}


pred Trace[] {
	first.Init[Cross]
	all s : (GameState - last) |
		let s' = s.next |
			Transition[s, s']
}

run Trace for exactly 9 GameState, exactly 9 Board

run Win for exactly 1 GameState, 1 Board
run Draw for exactly 1 GameState, 1 Board
