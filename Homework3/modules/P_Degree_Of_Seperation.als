module Degree_Of_Seperation

abstract sig Person{}

abstract sig SocialNetwork {
	friendship: Person -> Person	
}

one sig Shawn, Cary, Chandan, Buffalo, Sriram, Micah, Steve, Claude, Amanda, Lynn, Matt, Michael, Darryl, Delvin, David, JP extends Person{}

one sig Facebook, Twitter, GooglePlus extends SocialNetwork {}

// A simple function that takes care of symmetry
fun friends[a : Person, b : Person] : set (Person -> Person) {
	{a->b + b->a}
}

// Define all of the friends listed on the homework
fun facebookFriends[] : set (Person -> Person) {
	friends[Shawn, Cary] + friends[Shawn, Chandan] + friends[Shawn, Buffalo] +
		friends[Chandan, Sriram] + friends[Chandan, Micah] + friends[Chandan, Steve]
}

fun twitterFriends[] : set (Person -> Person) {
	friends[Sriram, Claude] + friends[Sriram, Amanda] + friends[Sriram, Lynn] +
		friends[Amanda, Matt] + friends[Amanda, Michael] + friends[Amanda, Shawn]
}

fun googleFriends[] : set (Person -> Person) {
	friends[Cary, Michael] + friends[Cary, Darryl] + friends[Cary, Buffalo] +
		friends[Darryl, Delvin] + friends[Darryl, David] + friends[Darryl, JP]
}

fact allFriendships {
	// For every relation of persons in facebookFriends[], the relation is in Facebook.friendship.
	// If the relation is not in facebookFriends[], the relation is not in Facebook.friendship
	all a, b : Person | {a->b} in facebookFriends[] => {a->b} in Facebook.friendship else {a->b} not in Facebook.friendship

	// Repeat for twitter and google
	all a, b : Person | {a->b} in twitterFriends[] => {a->b} in Twitter.friendship else {a->b} not in Twitter.friendship
	all a, b : Person | {a->b} in googleFriends[] => {a->b} in GooglePlus.friendship else {a->b} not in GooglePlus.friendship
}

// Here, instead of stating that any two people are in the transitive closure
// we say that they must be connected within '6 degrees' by "dotting" all of the friendships with itself 6 times.
pred degreeOfSeparation[a : Person, b : Person] {
	let r = SocialNetwork.friendship |
		a != b => {a->b} in r + r.r + r.r.r + r.r.r.r + r.r.r.r.r + r.r.r.r.r.r
}

// And finally, we run our model for degreeOfSeperation
run degreeOfSeparation
