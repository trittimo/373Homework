module Degree_Of_Seperation

abstract sig Person{}

// I changed this to allow a set of friends, which reduced the amount of later work
abstract sig SocialNetwork {
	friendship: Person -> some Person	
}

// This simply is a list of all the people in our model
one sig Shawn, Cary, Chandan, Buffalo, Sriram, Micah, Steve, Claude, Amanda, Lynn, Matt, Michael, Darryl, Delvin, David, JP extends Person{}

// And a list of the social networks in the model
one sig Facebook, Twitter, GooglePlus extends SocialNetwork {}

// Here I manually specify who is friends with whom, and through which social network
fact allFriendships {
	{Shawn->{Cary + Chandan + Buffalo} + Chandan->{Sriram + Micah + Steve}} in Facebook.friendship
    // Below we specify that the transverse of the above is in the relation, so if I'm friends with you, you're friends with me
	~{Shawn->{Cary + Chandan + Buffalo} + Chandan->{Sriram + Micah + Steve}} in Facebook.friendship

	// And repeat for twitter
	{Sriram->{Claude + Amanda + Lynn} + Amanda->{Matt + Michael + Shawn}} in Twitter.friendship
	~{Sriram->{Claude + Amanda + Lynn} + Amanda->{Matt + Michael + Shawn}} in Twitter.friendship
	
	// ... and for Google+
	{Cary->{Michael + Darryl + Buffalo} + Darryl->{Delvin + David + JP}} in GooglePlus.friendship
	~{Cary->{Michael + Darryl + Buffalo} + Darryl->{Delvin + David + JP}} in GooglePlus.friendship
}

// We specify that you can't directly be friends with yourself
fact irreflexive {
	no p : Person | {p->p} in SocialNetwork.friendship
}

// Here, instead of stating that any two people are in the transitive closure
// we say that they must be connected within '6 degrees' by "dotting" all of the friendships with itself 6 times.
fact degreeOfSeparation {
	all a, b : Person | {a->b} in (SocialNetwork.friendship).(SocialNetwork.friendship).(SocialNetwork.friendship).(SocialNetwork.friendship).(SocialNetwork.friendship).(SocialNetwork.friendship).(SocialNetwork.friendship)
}

pred show[] {}

// And finally, we run our model for degreeOfSeperation
run show
