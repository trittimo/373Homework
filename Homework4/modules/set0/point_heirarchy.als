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
	all x, y : Object | x.ref = y.ref <=> x = y
}

sig Point extends Object {
	x : Int,
	y : Int
}


pred Object.equals[o: Object] {
	this in Object - Point => this.equalsObject[o]
	else this in Point => this.equalsPoint[o]
}

pred Object.equalsObject[o: Object] {
	this = o
}

pred Point.equalsPoint[o: Object] {
	o = Null or (
		o != Null and
		o in Point and// - SubTypeOfPoint
		this.x = o.x and this.y = o.y
	) => this = o
//	o = Null => this = o
//	this.x = o.x and this.y = o.y => this = o
}

assert Reflexivity {
	all x : Object - Null | x.equals[x]
}

assert Symmetry {
	all x, y : Object - Null | x.equals[y] <=> y.equals[x]
}

assert Transitivity {
	all x, y, z : Object - Null | x.equals[y] and y.equals[z] => x.equals[z]
}

assert Nullity {
	no x : Object - Null | x.equals[Null]
}

check Reflexivity for 2
check Symmetry for 3
check Transitivity for 4
check Nullity for 2
