module LinearSolver

one sig x,y in Int{}

pred solve[] {
	x = 7.sub[y].sub[y]
	y = x.add[x].sub[4]
}
	
run solve for 8 Int
