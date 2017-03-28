module Degree_Of_Seperation

abstract sig Person{}

abstract sig SocialNetwork {
	friendship: Person -> set Person	
}

one sig Shawn, Cary, Chandan, Buffalo, Sriram, Micah, Steve, Claude, Amanda, Lynn, Matt, Michael, Darryl, Delvin, David, JP extends Person{}

one sig Facebook, Twitter, GooglePlus extends SocialNetwork {}

fact allFriendships {
	{Shawn->{Cary + Chandan + Buffalo} + Chandan->{Sriram + Micah + Steve}} in Facebook.friendship
	~{Shawn->{Cary + Chandan + Buffalo} + Chandan->{Sriram + Micah + Steve}} in Facebook.friendship

	{Sriram->{Claude + Amanda + Lynn} + Amanda->{Matt + Michael + Shawn}} in Twitter.friendship
	~{Sriram->{Claude + Amanda + Lynn} + Amanda->{Matt + Michael + Shawn}} in Twitter.friendship
	
	{Cary->{Michael + Darryl + Buffalo} + Darryl->{Delvin + David + JP}} in GooglePlus.friendship
	~{Cary->{Michael + Darryl + Buffalo} + Darryl->{Delvin + David + JP}} in GooglePlus.friendship
}

fact irreflexive {
	no p : Person | {p->p} in SocialNetwork.friendship
}

pred transitive[a: Person, b: Person] {
	all a: Person, b: Person | {a->b} in ^(SocialNetwork.friendship)
}

/*pred degreeOfSeperation[friend: Friend] {
	all f: Friend | f in friend.^friendsWith
}*/

pred show[] {}

run transitive for exactly 6 Person
