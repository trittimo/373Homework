module Professor

sig Dept { }

sig Prof { 
	dept: set Dept
}

sig Course {
	taughtBy: some Prof,
	ownedBy: lone Dept
}
fact {
	all c: Course | #c.taughtBy = 1 => c.ownedBy = c.taughtBy.dept else c.ownedBy = none
}

fact {
	all p: Prof | some p.dept
}
	
pred show[] {
	#Prof = 3
	#Dept = 2
	#Course = 2
}
	
run show for 4 but 2 Dept