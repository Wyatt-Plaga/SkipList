package SKPLIST_A4;

public class SkipList_Node {
  private double value;
  private int level;
  private SkipList_Node[] next;
  
  public SkipList_Node(double value, int height) {
    next = new SkipList_Node[height];
    this.value = value;
    this.level = height;
  }
  
  public void setNext(int height, SkipList_Node node) { 
    next[height] = node;
  }
  public double getValue() { return value; } 
  public SkipList_Node[] getNext() { return next; }
  public SkipList_Node getNext(int level) { return next[level]; }
  public String toString() { return "" + value; }
  
  public int getHeight() {
	  return level;
  }
  
  public boolean hasNext(int level) {
	  if(next[level] == null) {
		  return false;
	  } else {
		  return true;
	  }
  }
  
  public SkipList_Node getFarthestNext(SkipList_Node start) {
	  int i = start.getHeight();
		while (start.getNext(i).getValue() == Double.NaN && i >= 0) {
			i--;
		}
		return start.getNext(i);
  }

  // --------------------------------------------------------------------
  // you may add any other methods you want to get the job done
  // --------------------------------------------------------------------
}