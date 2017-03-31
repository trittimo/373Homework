package hw4.set5;

// Extra problem for 3 members teams
public class Member {
	private String name;
	private int ssn;
	private int roseId;
	
	public Member(String name, int roseId, int ssn) {
		this.name = name;
		this.roseId = roseId;
		this.ssn = ssn;
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof Member)) 
			return false;
		
		Member other = (Member)o;
		
		if(!this.name.equals(other.name))
			return false;

		return this.ssn == other.ssn || 
				this.roseId == other.roseId;
	}
}
