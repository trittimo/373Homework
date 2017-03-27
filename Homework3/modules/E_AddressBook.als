module AddressBook

sig Name, Addr {}

abstract sig AddressBook {
	contacts: Name -> lone Addr
}

sig Twitter, Facebook extends AddressBook {}

pred show[b1, b2: AddressBook] {
	b1 != b2
	#b1.contacts > 1
	#b2.contacts > 1
}

run show for 3 but 2 AddressBook

pred addContact[b, b': AddressBook, n: Name, a: Addr] {
	b != b'
	some Facebook & b iff some Facebook & b'
	b'.contacts = b.contacts ++ n->a
}

pred addContacts[b, b': AddressBook, c: Name -> lone Addr] {
	b != b'
	some Facebook & b iff some Facebook & b'
	b'.contacts = b.contacts ++ c
}

run addContacts for 4 but 2 AddressBook