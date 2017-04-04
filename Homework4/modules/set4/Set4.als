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

sig Point extends Object {
	x : Int,
	y : Int
}

sig PolarPoint extends Point {}
sig Point3D extends Point {
	z : Int
}

pred Object.equals[o: Object] {
	this in Object - Point => this.equalsObject[o]
	else this in Point - Point3D => this.equalsPoint[o]
	else this in Point3D => this.equalsPoint3D[o]
}

pred Object.equalsObject[o: Object] {
	this = o
}

pred Point.equalsPoint[o: Object] {
	(o in Point and this.x = o.x and this.y = o.y)
}

pred Object.symmEquals[p: Object] {
	this.equals[p] and p.equals[this]
}

pred Point3D.equalsPoint3D[o: Object] {
	this.x = o.x and this.y = o.y and this.z = o.z
}

assert Reflexivity {
	all a : (Object - Null) | a.symmEquals[a]
}

assert Symmetry {
	all a, b : (Object - Null) | a.symmEquals[b] <=> b.symmEquals[a]
}

assert Transitivity {
	all a, b, c : (Object - Null) | a.symmEquals[b] and b.symmEquals[c] => a.symmEquals[c]
}

assert Nullity {
	no a : (Object - Null) | a.symmEquals[Null]
}

check Reflexivity for 2
check Symmetry for 3
check Transitivity for 4
check Nullity for 2
