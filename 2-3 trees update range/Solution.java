import java.util.ArrayList;
import java.util.Scanner;

public class Solution {
	public static void main(String args[]){
		
		Scanner input = new Scanner(System.in);
		TwoThreeTree tree23 = new TwoThreeTree();
		int count=input.nextInt();

		while(count>0){
			int type=input.nextInt();
			
			if(type==1){
				//insert
				String key=input.next();
				int value=input.nextInt();
				twothree.insert(key, value, tree23);
			}else if(type==2){
				//increase
				String key1=input.next();
				String key2=input.next();
				int k=input.nextInt();
				String smallKey=null,bigKey=null;
				if(key1.compareTo(key2)>0){
					smallKey=key2;
					bigKey=key1;
				}else if(key1.compareTo(key2)<=0){
					smallKey=key1;
					bigKey=key2;
				}
				twothree.updateRange(" ",tree23.root,smallKey,bigKey,tree23.height,k);

				
			}else{
				//print
				String key=input.next();
				System.out.println(twothree.search(key, (InternalNode)tree23.root, tree23.height,0));
			}

		
			count--;
		}
		input.close();
	}
}


class twothree {

	   static void insert(String key, int value, TwoThreeTree tree) {
	   // insert a key value pair into tree (overwrite existing value
	   // if key is already present)

	      int h = tree.height;

	      if (h == -1) {
	          LeafNode newLeaf = new LeafNode();
	          newLeaf.guide = key;
	          newLeaf.value = value;
	          tree.root = newLeaf; 
	          tree.height = 0;
	      }
	      else {
	         WorkSpace ws = doInsert(key, value, tree.root, h);

	         if (ws != null && ws.newNode != null) {
	         // create a new root

	            InternalNode newRoot = new InternalNode();
	            if (ws.offset == 0) {
	               newRoot.child0 = ws.newNode; 
	               newRoot.child1 = tree.root;
	            }
	            else {
	               newRoot.child0 = tree.root; 
	               newRoot.child1 = ws.newNode;
	            }
	            resetGuide(newRoot);
	            tree.root = newRoot;
	            tree.height = h+1;
	         }
	      }
	   }

	   static WorkSpace doInsert(String key, int value, Node p, int h) {
	   // auxiliary recursive routine for insert

	      if (h == 0) {
	         // we're at the leaf level, so compare and 
	         // either update value or insert new leaf

	         LeafNode leaf = (LeafNode) p; //downcast
	         int cmp = key.compareTo(leaf.guide);

	         if (cmp == 0) {
	            leaf.value = value; 
	            return null;
	         }

	         // create new leaf node and insert into tree
	         LeafNode newLeaf = new LeafNode();
	         newLeaf.guide = key; 
	         newLeaf.value = value;

	         int offset = (cmp < 0) ? 0 : 1;
	         // offset == 0 => newLeaf inserted as left sibling
	         // offset == 1 => newLeaf inserted as right sibling

	         WorkSpace ws = new WorkSpace();
	         ws.newNode = newLeaf;
	         ws.offset = offset;
	         ws.scratch = new Node[4];

	         return ws;
	      }
	      else {
	         InternalNode q = (InternalNode) p; // downcast
	         int pos;
	         WorkSpace ws;
	         
	         q.child0.value+=q.value;
	         q.child1.value+=q.value;
	         if(q.child2!=null){
	        	 q.child2.value+=q.value;
	        	 
	         }
	         q.value=0;
	         if (key.compareTo(q.child0.guide) <= 0) {
	            pos = 0; 
	            ws = doInsert(key, value, q.child0, h-1);
	            
	         }
	         else if (key.compareTo(q.child1.guide) <= 0 || q.child2 == null) {
	            pos = 1;
	            ws = doInsert(key, value, q.child1, h-1);
	         }
	         else {
	            pos = 2; 
	            ws = doInsert(key, value, q.child2, h-1);
	         }

	         if (ws != null) {
	            if (ws.newNode != null) {
	               // make ws.newNode child # pos + ws.offset of q

	               int sz = copyOutChildren(q, ws.scratch);
	               insertNode(ws.scratch, ws.newNode, sz, pos + ws.offset);
	               if (sz == 2) {
	                  ws.newNode = null;
	                  ws.guideChanged = resetChildren(q, ws.scratch, 0, 3);
	               }
	               else {
	                  ws.newNode = new InternalNode();
	                  ws.offset = 1;
	                  resetChildren(q, ws.scratch, 0, 2);
	                  resetChildren((InternalNode) ws.newNode, ws.scratch, 2, 2);
	               }
	            }
	            else if (ws.guideChanged) {
	               ws.guideChanged = resetGuide(q);
	            }
	         }

	         return ws;
	      }
	   }

	   static int search(String key, InternalNode node,int height,int total){
		 if (height>0){
			 if(key.compareTo(node.child0.guide)<=0){
				 total+=node.value;
				 return search(key,(InternalNode)node.child0,height-1,total);
			 }else if(key.compareTo(node.child1.guide)<=0||node.child2==null){
				 total+=node.value;
				 return search(key,(InternalNode)node.child1,height-1,total);
			 }else{
				 total+=node.value;
				 return search(key,(InternalNode)node.child2,height-1,total);
			 }
			 
			 
		 }else{
			 //height = 0
			 if (key.equals(node.guide)){
				 return ((LeafNode)node).value+total;
			 }else{
				 return -1;
			 }
			 
		 }
		   
	   }

	   public static void updateRange(String low,Node node,String start, String end,int height,int k){
		   if(height==0){
			   if(node.guide.compareTo(end)<=0&&node.guide.compareTo(start)>=0){
				   node.value+=k;
			   }
		   }else{
		  
			   if(start.compareTo(low)<=0&&end.compareTo(node.guide)>=0){
				   
				   node.value+=k;
				   
			   }else if(end.compareTo(low)>0&&(start.compareTo(node.guide)<=0)){ 
				   
				   InternalNode temp=(InternalNode)node;
	
				   updateRange(low,temp.child0,start,end,height-1,k);
			   
				   updateRange(temp.child0.guide,temp.child1,start,end,height-1,k);
				   
				  
				   if(temp.child2!=null){
						   
					updateRange(temp.child1.guide,temp.child2,start,end,height-1,k);
					 
				   }
			   } 
		   }
	   }
	   
	  
	   static int copyOutChildren(InternalNode q, Node[] x) {
	   // copy children of q into x, and return # of children

	      int sz = 2;
	      x[0] = q.child0; x[1] = q.child1;
	      if (q.child2 != null) {
	         x[2] = q.child2; 
	         sz = 3;
	      }
	      return sz;
	   }

	   static void insertNode(Node[] x, Node p, int sz, int pos) {
	   // insert p in x[0..sz) at position pos,
	   // moving existing extries to the right

	      for (int i = sz; i > pos; i--)
	         x[i] = x[i-1];

	      x[pos] = p;
	   }

	   static boolean resetGuide(InternalNode q) {
	   // reset q.guide, and return true if it changes.

	      String oldGuide = q.guide;
	      if (q.child2 != null)
	         q.guide = q.child2.guide;
	      else
	         q.guide = q.child1.guide;

	      return q.guide != oldGuide;
	   }


	   static boolean resetChildren(InternalNode q, Node[] x, int pos, int sz) {
	   // reset q's children to x[pos..pos+sz), where sz is 2 or 3.
	   // also resets guide, and returns the result of that

	      q.child0 = x[pos]; 
	      q.child1 = x[pos+1];

	      if (sz == 3) 
	         q.child2 = x[pos+2];
	      else
	         q.child2 = null;

	      return resetGuide(q);
	   }
	}


	class Node {
	   String guide;
	   int value;
	   // guide points to max key in subtree rooted at node
	}

	class InternalNode extends Node {
	   Node child0, child1, child2;
	   // child0 and child1 are always non-null
	   // child2 is null iff node has only 2 children
	}

	class LeafNode extends InternalNode {
	   // guide points to the key

	}

	class TwoThreeTree {
	   Node root;
	   int height;

	   TwoThreeTree() {
	      root = null;
	      height = -1;
	   }
	}

	class WorkSpace {
	// this class is used to hold return values for the recursive doInsert
	// routine

	   Node newNode;
	   int offset;
	   boolean guideChanged;
	   Node[] scratch;
	}


