module MultiLevelAddress

sig Address {}
sig Name {
	// Multi-level address book
	address:set (Name + Address)
}

pred show {}

run show for 4