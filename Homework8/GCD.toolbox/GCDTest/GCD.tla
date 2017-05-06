-------------------------------- MODULE GCD --------------------------------
EXTENDS Integers

Divides(p, n) == \E q \in Int : n = q * p

DivisorsOf(n) == {p \in Int : Divides(p, n)}

SetMax(S) ==
    CHOOSE i \in S : \A j \in S : i >= j

GCD(m, n) ==
    SetMax(DivisorsOf(m) \cap DivisorsOf(n))

=============================================================================
\* Modification History
\* Last modified Sat May 06 18:13:21 EDT 2017 by trittimo
\* Created Sat May 06 17:57:03 EDT 2017 by trittimo
