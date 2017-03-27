module Degree_Of_Seperation


// Models the super set of all persons
abstract sig Person{}

// Models the super set of all social networks
abstract sig SocialNetwork{
	// Models the friendship relation between two friends
	// for all of the social networks
	friendship: Person -> Person	
}

// Here is how you can model individual persons. Note that the set 
// Person = {Shawn, Cary, Chandan, Buffalo} with this declaration.
// TODO: Model other CSSE faculty and staffs as well based on the question.
one sig Shawn, Cary, Chandan, Buffalo extends Person{}
 
// Let's model all of the social networks
one sig Facebook, Twitter, GooglePlus extends SocialNetwork {}

// TODO: Complete the partial model
