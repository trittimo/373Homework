module LeaderElection
open util/ordering[Time] as T0
open util/ordering[Process] as P0

sig Time {}


sig Process {
	succ: Process,
	toSend: Process->Time,
	elected: set Time
}

// All processes are reachable from any process by following the successor
fact Ring {all p: Process | Process in p.^succ}
pred init [t: Time] {all p: Process | p.toSend.t = p}

// Describe the allowed state transitions
// Don't allow any id's assigned to a process to be assigned to any preceding process
pred step[t, t': Time, p: Process] {
	let from = p.toSend, to = p.succ.toSend |
		some id: from.t {
			from.t' = from.t - id
			to.t' = to.t + (id - P0/prevs[p.succ])
		}
}

// A helper predicate asserting that the times of toSend are the same
pred skip[t, t': Time, p: Process] {p.toSend.t = p.toSend.t'}

// Mandate that at most one leader gets elected, and that some leader is eventually elected
fact Traces {
	init[T0/first[]]
	all t: Time - T0/last[] | let t' = T0/next[t] |
		all p: Process |
			step[t, t', p] or step[t, t', succ.p] or skip[t, t', p]
}

// Elect some leader from the processes
// At first, there is no process elected
// At other times, the elected processes are the processes which just recieved an identifier
fact DefineElected {
	no elected.T0/first[]
	all t: Time - T0/first[] |
		elected.t = 
			{p: Process | p in p.toSend.t - p.toSend.(T0/prev[t])}
}

assert AtMostOneElected {lone elected.Time}
check AtMostOneElected for 3 Process, 7 Time

// Ensure that time is progressive (i.e. there is no skipping)
pred progress[] {
	all t: Time - T0/last[] | let t' = T0/next[t] |
		some Process.toSend.t =>
			some p: Process | not skip[t, t', p]
}

// Ensure that at least one of the processes is elected as the leader
assert AtLeastOneElected {
progress [] => some elected.Time
}
check AtLeastOneElected for 3 Process, 7 Time


// Ensure that it is possible to get a path without a loop in it
// (i.e. there are no two processes such that the toSend times are the same)
pred looplessPath[] {no disj t, t': Time | toSend.t = toSend.t'}
run looplessPath for 13 Time, 3 Process


pred show { some elected }
run show for 3 Process, 4 Time

assert AtLeastOneElectedWithAnIssue {
	some t: Time | some elected.t
}

check AtLeastOneElectedWithAnIssue for 3 but 7 Time
