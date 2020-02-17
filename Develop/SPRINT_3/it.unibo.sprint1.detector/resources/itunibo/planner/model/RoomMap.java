package itunibo.planner.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import itunibo.planner.model.RobotState.Direction;
import kotlin.Pair;

public class RoomMap implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static RoomMap singletonRoomMap;
	public static RoomMap getRoomMap() {
		if (singletonRoomMap == null)
			singletonRoomMap = new RoomMap();
		return singletonRoomMap;
	}
	public static void setRoomMap( RoomMap map ) { 
		singletonRoomMap = map;
 	}
	
	private List<ArrayList<Box>> roomMap = new ArrayList<ArrayList<Box>>();
	
	public void reset() {
		roomMap = new ArrayList<ArrayList<Box>>();
		for (int i=0; i<1; i++) {
			roomMap.add(new ArrayList<Box>());
			for (int j=0; j<1; j++) {
				roomMap.get(i).add(null);
			}
		}
		this.put(0, 0, new Box(false, false, true));
	}
	
	private RoomMap() {
		super();
		for (int i=0; i<1; i++) {
			roomMap.add(new ArrayList<Box>());
			for (int j=0; j<1; j++) {
				roomMap.get(i).add(null);
			}
		}
		this.put(0, 0, new Box(false, false, true));
	}
	
	private RoomMap(List<ArrayList<Box>> fromMap) {
		this.roomMap = fromMap;
	}
	
//	public Map<Coordinate, Box> getMapClone() {
//		return new HashMap<>(this.roomMap);
//	}


	public void put(int x, int y, Box box) {
		try {
			roomMap.get(y);
		} catch (IndexOutOfBoundsException e) {
			for (int i=roomMap.size(); i<y; i++) {
				roomMap.add(new ArrayList<Box>());
			}
			roomMap.add(y, new ArrayList<Box>());
		}
		try {
			roomMap.get(y).get(x);
			roomMap.get(y).remove(x);
			roomMap.get(y).add(x, box);
		} catch (IndexOutOfBoundsException e) {
			for (int j=roomMap.get(y).size(); j<x; j++) {
				roomMap.get(y).add(new Box(false, true, false));
			}
			roomMap.get(y).add(x, box);
		}
	}
	
	public boolean isObstacle(int x, int y) {
		try {
			Box box = roomMap.get(y).get(x);
			//System.out.println(" ... RoomMap  isObstacle " + box.isObstacle());
			if  (box == null)
				return false;
			if (box.isObstacle())
				return true;
			else
				return false;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}
	
	public boolean isDirty(int x, int y) {
		try {
			Box box = roomMap.get(y).get(x);
			if  (box == null)
				return true;
			if (box.isDirty())
				return true;
			else
				return false;
		} catch (IndexOutOfBoundsException e) {
			return true;
		}
	}
	
	public boolean canMove(int x, int y, RobotState.Direction direction) {
		switch (direction) {
		case UP: return canMoveUp(x, y);
		case RIGHT: return canMoveRight(x, y);
		case DOWN: return canMoveDown(x, y);
		case LEFT: return canMoveLeft(x, y);
		default: throw new IllegalArgumentException("Not a valid direction");
		}
	}
	
	public boolean canMoveUp(int x, int y) {
		if (y<=0)
			return false;
		try {
			Box box = roomMap.get(y-1).get(x);
			if  (box == null)
				return true;
			if (box.isObstacle())
				return false;
			return true;
		} catch (IndexOutOfBoundsException e) {
			return true;
		}
	}
	
	public boolean canMoveRight(int x, int y) {
		try {
			Box box = roomMap.get(y).get(x+1);
			if  (box == null)
				return true;
			if (box.isObstacle())
				return false;
			return true;
		} catch (IndexOutOfBoundsException e) {
			return true;
		}
	}

	public boolean canMoveDown(int x, int y) {
		try {
			Box box = roomMap.get(y+1).get(x);
			if  (box == null)
				return true;
			if (box.isObstacle())
				return false;
			return true;
		} catch (IndexOutOfBoundsException e) {
			return true;
		}
	}
	
	public boolean canMoveLeft(int x, int y) {
		if (x<=0)
			return false;
		try {
			Box box = roomMap.get(y).get(x-1);
			if  (box == null)
				return true;
			if (box.isObstacle())
				return false;
			return true;
		} catch (IndexOutOfBoundsException e) {
			return true;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (ArrayList<Box> a : roomMap) {
			builder.append("|");
			for (Box b : a) {
				if (b == null)
					builder.append("-, ");
				else if (b.isRobot())
					builder.append("r, ");
				else if (b.isObstacle())
					builder.append("X, ");
				else if (b.isDirty())
					builder.append("0, ");
				else
					builder.append("1, ");
			}
			builder.append("\n");
		}
		return builder.toString();
	}
	
	public int getDimX() {
		int result=0;
		for (int i=0; i<roomMap.size(); i++) {
			if (result<roomMap.get(i).size())
				result = roomMap.get(i).size();
		}
		return result;
	}
	
	public int getDimY() {
		return roomMap.size();
	}
	
	public boolean isFullyExplored() {
		try {
			for (int i = 0; i < roomMap.size(); i++) {
				List<Box> row = roomMap.get(i);
				if (row != null) { 
					for (int j = 0; j < row.size(); j++) {
						Box cell = row.get(j);
						if (cell != null && !cell.isObstacle() && isUnexplored(i,j)) {
							return false;
						}
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			System.out.println(this);
		}
		return true;
	}
	
	private boolean isUnexplored(int x, int y) {
		int newX = x;
		int newY = y;
		
		newX = x - 1;
		if (newX >= 0 && roomMap.get(newX).get(newY) == null) {
			return true;
		}
		
		newX = x + 1;
		if (newX >= roomMap.size() || roomMap.get(newX).get(newY) == null) {
			return true;
		}
		
		newX = x;
		newY = y - 1;
		if (newY >= 0 && roomMap.get(newX).get(newY) == null) {
			return true;
		}
		
		newY = y + 1;
		if (newY >= roomMap.get(newX).size() || roomMap.get(newX).get(newY) == null) {
			return true;
		}
		
		return false;
	}
	
	public Pair<Integer, Integer> getFirstNonExploredPosition() {
		for (int i = 0; i < roomMap.size(); i++) {
			List<Box> row = roomMap.get(i);
			if (row != null) { 
				for (int j = 0; j < row.size(); j++) {
					Box cell = row.get(j);
					
					if (cell != null && cell.isDirty() && isReachable(i,j)) {
						return new Pair<>(j,i);
					}
				}
			}
		}
		return null;
	}
	
	private boolean isReachable(int x, int y) {
		System.out.println("isReachable "+ x + "," + y + " ");

		List<Pair<Integer,Integer>> positions = new ArrayList<>();
		positions.add(new Pair<>(x - 1,y));
		positions.add(new Pair<>(x + 1,y));
		positions.add(new Pair<>(x, y - 1));
		positions.add(new Pair<>(x, y + 1));
		
		for (Pair<Integer, Integer> pos: positions) {
			int row = pos.getFirst();
			int col = pos.getSecond();
			if (row >= 0 && row < roomMap.size() 
					&& col >= 0 && col < roomMap.get(row).size()) {
				Box b = roomMap.get(row).get(col);
				if (b != null && !b.isDirty() && !b.isRobot() && !b.isObstacle()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isClean() {
		for (ArrayList<Box> row : roomMap) {
			for (Box b : row)
				if (b.isDirty())
					return false;
		}
		return true;
	}
	
	public void setObstacles() {
		for (ArrayList<Box> row : roomMap) {
			for (Box b : row) {
				int y = row.indexOf(b);
				int x = roomMap.indexOf(row);
				if (!b.isObstacle() && b.isDirty() && !isReachable(x, y)) {
					b.setDirty(false);
					b.setObstacle(true);
				}
			}
		}
	}
	
	public void setDirty() {
		for (ArrayList<Box> row : roomMap) {
			for (Box b : row) {
				if (!b.isObstacle() && !b.isDirty() && !b.isRobot()) //Robot is always clean
					b.setDirty(true);
			}
		}
	}
	
	public static RoomMap fromString(String map) {
		String[] cleanedMap = map
				.replace("map", "")
				.replaceAll("[|,()]", "")
				.split("\n");
		
		List<ArrayList<Box>> roomMap = new ArrayList<>(); 
		
        for (String line : cleanedMap) {
            ArrayList<Box> l = new ArrayList<>();
            
            for (String ch : line.trim().split(" ")) {
                //System.out.print(ch);
                Box b = null;
                if (ch.equals("r")) {
                    b = new Box(false, false, true);
                } else if  (ch.equals("X")) {
                    b = new Box(true, false, false);
                } else if (ch.equals("0")) {
                    b = new Box(false, true, false);
                } else if (ch.equals("1")) {
                    b = new Box(false, false, false);
                }
                
                l.add(b);
            }
            roomMap.add(l);
        } 
				
		return new RoomMap(roomMap);
	}
	
}