module Thing

sig Thing, OtherThing {}

// Try experimenting with other multiplicity such as
// one->one, one->many, many->one, many->many
pred relationMult[r: Thing some -> some OtherThing] {}

run relationMult for 3
