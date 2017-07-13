import java.util.HashSet;

public class CheckerPiece extends Piece{

	boolean king;
	
	public CheckerPiece(Board b, Location loc, boolean white,boolean k) {
		super(b, loc, white);
		king = k;
	}

	@Override
	public HashSet<Location> getMoveLocations() {
		if(king)
			return getMoveLocationsKing();
		int r = location.row;
		int c = location.col;
		HashSet<Location> locs = new HashSet<Location>();
		int[] ints = new int[]{-1,1};
		for(int i = 0;i<2;i++)
		{
			Location loc = Location.get(r-1,c+ints[i]);
			if(loc.inBounds()&&board.get(loc)==null)
				locs.add(loc);
		}
		return locs;
	}
	
	public HashSet<Location> getMoveLocationsKing()
	{
		int r = location.row;
		int c = location.col;
		HashSet<Location> locs = new HashSet<Location>();
		int[][] ints = new int[][]{{-1,-1,1,1},{-1,1,-1,1}};
		for(int i = 0;i<4;i++)
		{
			Location loc = Location.get(r+ints[0][i],c+ints[1][i]);
			if(loc.inBounds()&&board.get(loc)==null)
				locs.add(loc);
		}
		return locs;
	}
	
	public HashSet<String> getJumpLocations()
	{
		HashSet<String> r = new HashSet<String>();
		if(king)
			fillJumpLocationsKing(r,location+"");
		else
			fillJumpLocations(r,location+"");
		return r;
	}
	
	public void fillJumpLocations(HashSet<String> locs,String current)
	{
		int r = location.row;
		int c = location.col;
		int[] ints = new int[]{-1,1};
		boolean flag = true;
		for(int i = 0;i<2;i++)
		{
			
			Location l = Location.get(r-1,c+ints[i]);
			if(!l.inBounds())
				continue;
			Piece p = board.get(l);
			Location lo = Location.get(r-2,c+ints[i]*2);
			if(!lo.inBounds())
				continue;
			if(p!=null&&p.isEnemy(this)&&board.get(lo)==null)
			{
				flag = false;
				Board b = board.clone();
				b.set(location,null);
				b.set(l,null);
				b.set(lo,new CheckerPiece(b, lo, isWhite, king));
				((CheckerPiece)b.get(lo)).fillJumpLocations(locs,current+" "+lo);
			}
		}
		if(flag)
			locs.add(current);
	}
	
	public void fillJumpLocationsKing(HashSet<String> locs,String current)
	{
		int r = location.row;
		int c = location.col;
		int[][] ints = new int[][]{{-1,-1,1,1},{-1,1,-1,1}};
		boolean flag = true;
		for(int i = 0;i<4;i++)
		{
			Location l = Location.get(r+ints[0][i],c+ints[1][i]);
			if(!l.inBounds())
				continue;
			Piece p = board.get(l);
			Location lo = Location.get(r+ints[0][i]*2,c+ints[1][i]*2);
			if(!lo.inBounds())
				continue;
			if(p!=null&&p.isEnemy(this)&&board.get(lo)==null)
			{
				flag = false;
				Board b = board.clone();
				b.set(location,null);
				b.set(l,null);
				b.set(lo,new CheckerPiece(b, lo, isWhite, king));
				((CheckerPiece)b.get(lo)).fillJumpLocationsKing(locs,current+" "+lo);
			}
		}
		if(flag)
			locs.add(current);
	}

	@Override
	public boolean canMoveTo(Location loc) {
		return getMoveLocations().contains(loc);
	}

	@Override
	public String pieceName() {
		return (isWhite()?"Red":"Black")+(king?" King":" Checker");
	}

	@Override
	public void moveTo(Location loc) {
		if(Math.abs(loc.row-location.row)==2)
			board.set(Location.get((location.row+loc.row)/2,(location.col+loc.col)/2),null);
		board.set(location,null);
		board.set(loc,this);
		setLocation(loc);
	}

	@Override
	public String toString() {
		return pieceName();
	}

}
