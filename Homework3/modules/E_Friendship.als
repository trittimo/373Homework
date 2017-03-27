module Friendship

// Six degree of separation
sig Friend {
	friendsWith: set Friend
}
// A Friend is not friends with itself
fact {
	no f: Friend | f in f.friendsWith
}

// The supplied friend is friends with all friends in the Friend set
pred degreeOfSeperation[friend: Friend] {
	all f: Friend | f in friend.^friendsWith
}

run degreeOfSeperation for exactly 6 Friend
