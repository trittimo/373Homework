// Don't use string type: create sig Str

sig Object {
	ref : Int
}

one sig Null extends Object {}
{ref = 0}

fact NullnessReference {
	all object : (Object - Null) | object.ref != 0
}

fact ReferenceEquality {
	all a, b : Object | a.ref = b.ref <=> a = b
}

sig Str {}

sig Member extends Object {
	name : Str,
	ssn : Int,
	roseId : Int
}

pred Object.equals[o: Object] {
	this in Object - Member => this.equalsObject[o]
	else this in Member => this.equalsMember[o]
}

pred Object.equalsObject[o: Object] {
	this = o
}

pred Member.equalsMember[o: Object] {
	o in Member and this.name = o.name and (this.ssn = o.ssn or this.roseId = o.roseId)
}

assert Reflexivity {
	all a : (Object - Null) | a.equals[a]
}

assert Symmetry {
	all a, b : (Object - Null) | a.equals[b] <=> b.equals[a]
}

assert Transitivity {
	all a, b, c : (Object - Null) | a.equals[b] and b.equals[c] => a.equals[c]
}

assert Nullity {
	no a : (Object - Null) | a.equals[Null]
}

check Reflexivity for 2
check Symmetry for 3
check Transitivity for 4
check Nullity for 2
