

public class Location {

	public final int row,col;
	private static Location[][] locs = new Location[8][8];
    private Location(int r, int c) 
    {
    	row = r;
    	col = c;
    }
    
    public boolean inBounds()
    {
    	return inBounds(row,col);    
    }
    
    public static boolean inBounds(int row,int col)
    {
    	return Math.min(row,col)>=0&&Math.max(row,col)<8;    
    }
    
    public static Location get(int r, int c)
    { 	
    	if(!inBounds(r,c))
    		return new Location(r,c);
    	if(locs[r][c] == null)
    		locs[r][c] = new Location(r,c);
    	return locs[r][c];
    }
    
    public static Location parseLocation(String str)
    {
    	return Location.get(str.charAt(0)-97,Integer.parseInt(str.substring(2))-1);
    }
    
    public String toString()
    {
    	return (char)(row+97)+" "+(col+1);
    }
}