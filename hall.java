package SeatAllocator;

public class hall
{
	private String hname;
	private int benchcount;

	public hall(String name , int benchcount)
	{
		this.hname=name;
		this.benchcount=benchcount;
	}
	public String getHname() { return hname; }
    public int getBenchCount() { return benchcount; }
}
