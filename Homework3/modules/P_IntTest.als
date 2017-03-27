module IntTest

one sig IntTest {
	value: let range = {y: Int | y >= -2 and y =< 2} | sum x: range | x
}
	
pred show[] {}
	
run show for 1
