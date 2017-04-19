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
	all c : cells | t in c.(this.grid)
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

// True if the board is filled (i.e. the game is definitively over)
pred GameState.Filled[] {
	all c : Cell | this.board.Contains[c]
}

// True if the board is fully filled and there is no winner
pred GameState.Draw[] {
	this.Filled[]
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


// True if s has one more piece than 'this'
pred GameState.HasNewPiece[s : GameState] {
	let g = this.board.grid, g' = s.board.grid |
		#(g' - g) = 1 and no (g - g')
}

fun GameState.GetNewPiece[s : GameState] : Marker {
	let g = this.board.grid, g' = s.board.grid |
		Cell.(g' - g)
}

// Specifies what a transition from one state to another looks like
// The only difference should be a single additional piece in g'
// The type of the new piece should correspond to the turn of s'.turn
pred Transition[s, s' : GameState] {
	s'.turn = s.NextTurn[]
	// If the game is over, there should be no progression
	s.Win[Cross] or s.Win[Nought] or s.Draw[] => s' = s
	
	// Otherwise, the board should progress
	else s.HasNewPiece[s'] and s.GetNewPiece[s'] in s'.turn
}

// Trace the execution
pred Trace[t : Marker] {
	first.Init[t]
	all s : (GameState - last) |
		let s' = s.next |
			Transition[s, s']
}

// Winning trace
pred WinningTrace[] {
	Trace[Cross]
	last.Win[Cross] or last.Win[Nought]
}

pred DrawTransition[s, s' : GameState] {
	s'.turn = s.NextTurn[]
	s.Draw[] => s' = s
	else s.HasNewPiece[s'] and s.GetNewPiece[s'] in s'.turn
	not s.Win[Cross] and not s.Win[Nought]
	not s'.Win[Cross] and not s'.Win[Nought]
}

pred DrawTrace[t : Marker] {
	first.Init[t]
	all s : (GameState - last) |
		let s' = s.next |
			DrawTransition[s, s']
}

run WinningTrace for 6 GameState, 6 Board
run DrawTrace for 10 GameState, 10 Board
run Trace for exactly 10 GameState, exactly 10 Board
run Win for exactly 1 GameState, 1 Board
run Draw for exactly 1 GameState, 1 Board
