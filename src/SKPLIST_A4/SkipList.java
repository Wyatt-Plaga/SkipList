package SKPLIST_A4;

import java.util.Arrays;
import java.util.Random;

public class SkipList implements SkipList_Interface {
	private SkipList_Node root;
	private final Random rand;
	private double probability;
	//private const int MAXHEIGHT = 30; // the most links that a data cell may contain
	boolean end = false;

	public SkipList(int maxHeight) {
		root = new SkipList_Node(Double.NaN, maxHeight);
		rand = new Random();
		probability = 0.5;
	}

	@Override
	public void setSeed(long seed) {
		rand.setSeed(seed);
	}

	@Override
	public void setProbability(double probability) {
		this.probability = probability;
	}

	private boolean flip() {
		// use this where you "roll the dice"
		// call it repeatedly until you determine the level
		// for a new node
		return rand.nextDouble() < probability;
	}
	
	public int getRandomLevel() {
		int level = 1;
		while(flip()) {
			level++;
		}
		if (level >= max()) {
			level = max() - 1;
			}
		return level;
	}

	@Override
	public SkipList_Node getRoot() {
		return root;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		int levels;
		for (levels = 0; levels < root.getNext().length && root.getNext(levels) != null; levels++)
			;

		StringBuilder[] sbs = new StringBuilder[levels];

		for (int i = 0; i < sbs.length; i++) {
			sbs[i] = new StringBuilder();
			sbs[i].append("level ").append(i).append(":");
		}

		SkipList_Node cur = root;

		while (cur.getNext(0) != null) {
			cur = cur.getNext(0);
			for (int i = levels - 1; i >= cur.getNext().length; i--) {
				sbs[i].append("\t");
			}
			for (int i = cur.getNext().length - 1; i >= 0; i--) {
				if (cur.getNext(i) == null) {
					levels--;
				}
				sbs[i].append("\t").append(cur.getValue());
			}
		}

		for (int i = sbs.length - 1; i >= 0; i--) {
			sb.append(sbs[i]).append("\n");
		}

		return sb.toString();
	}

	@Override
	public boolean insert(double value) {
		if(find(value) != null) {
			return false;
		}
		SkipList_Node toInsert = new SkipList_Node(value, getRandomLevel());
		SkipList_Node marker = getRoot();
		int i = toInsert.getHeight() - 1;
		while(i>=0) {
			while(marker.hasNext(i) && marker.getNext(i).getValue() < value) {
				marker = marker.getNext(i);
			}
			linkMiddle(marker, toInsert, i);
			i--;
		}
		return true;
	}

	@Override
	public boolean remove(double value) {
		if(find(value) == null) {
			return false;
		}
		SkipList_Node marker = getRoot();
		int i = find(value).getHeight() - 1;
		while(i>=0) {
			while(marker.hasNext(i) && marker.getNext(i).getValue() < value) {
				marker = marker.getNext(i);
			}
			unlinkMiddle(marker, find(value), i);
			i--;
		}
		return true;
	}

	@Override
	public boolean contains(double value) {
		if(size() == 0) {
			return false;
		}
		SkipList_Node marker = getRoot();
		int i = max() - 1;
		while(i>=0) {
			while(marker.hasNext(i) && marker.getNext(i).getValue() <= value) {
				marker = marker.getNext(i);
			}
			i--;
		}
		if(marker.getValue() == value) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public double findMin() {
		if (size() == 0) {
			return Double.NaN;
		} else {
			return getRoot().getNext(0).getValue();
		}
	}

	@Override
	public double findMax() {
		if (size() == 0) {
			return Double.NaN;
		} else {
			end = false;
			SkipList_Node marker = getRoot();
			while (!end) {
				marker = getFarthestNext(marker);
			}
			return marker.getValue();
		}
	}

	@Override
	public boolean empty() {
		if (size() != 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void clear() {
		int i = max() - 1;
		while(i>=0) {
			getRoot().setNext(i, null);
			i--;
		}
	}

	@Override
	public int size() {
		int size = 0;
		SkipList_Node marker = getRoot();
		while (marker.hasNext(0)) {
			marker = marker.getNext(0);
			size++;
		}
		return size;
	}

	@Override
	public int level() {
		if(size() == 0) {
			return -1;
		}
		int i = max() - 1;
		while (!getRoot().hasNext(i)) {
			i--;
		}
		return i;
	}

	@Override
	public int max() {
		return root.getHeight();
	}

	// ---------------------------------------------------------
	// student code follows
	// implement the methods of the interface
	// ---------------------------------------------------------

	public SkipList_Node getFarthestNext(SkipList_Node start) {
		int i = start.getHeight() - 1;
		while (i >= 0 && !start.hasNext(i)) {
			i--;
		}
		if (i == -1) {
			end = true;
			return start;
		}
		return start.getNext(i);
	}
	
	public void unlinkMiddle(SkipList_Node before, SkipList_Node middle, int level) {
		before.setNext(level, middle.getNext(level));
	}
	
	public void linkMiddle(SkipList_Node before, SkipList_Node middle, int level) {
		middle.setNext(level, before.getNext(level));
		before.setNext(level, middle);
	}
	
	public SkipList_Node find(double value) {
		if(size() == 0) {
			return null;
		}
		SkipList_Node marker = getRoot();
		int i = max() - 1;
		while(i>=0) {
			while(marker.hasNext(i) && marker.getNext(i).getValue() <= value) {
				marker = marker.getNext(i);
			}
			i--;
		}
		if(marker.getValue() == value) {
			return marker;
		} else {
			return null;
		}
	}
}