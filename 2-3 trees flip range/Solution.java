import java.util.ArrayList;
import java.util.Scanner;

public class Solution {
	public static void main(String args[]){
		Scanner input = new Scanner(System.in);
		TwoThreeTree tree23 = new TwoThreeTree();
		int count=input.nextInt();

		while(count>0){
			String key=input.next();
			int value=input.nextInt();
			twothree.insert(key,value,tree23);
			count--;
		}
		//System.out.println("finish counting");
		count=input.nextInt();
		while(count>0){
			//System.out.println("--------");
			String key1=input.next();
			String key2=input.next();
			boolean sameString=false;
			//ArrayList<Node> path1=new ArrayList<Node>();
			String smallKey=null;
			String bigKey=null;
			if(key1.compareTo(key2)>0){
				smallKey=key2;
				bigKey=key1;
			}else if(key1.compareTo(key2)<=0){
				smallKey=key1;
				bigKey=key2;
			}
			twothree.searchRange(tree23.root,smallKey,bigKey,tree23.height);
			
			//System.out.println("finish round");
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

	   static int search(String key, InternalNode node,int height){

		 if (height>0){
			 if(key.compareTo(node.child0.guide)<=0){
				 return search(key,(InternalNode)node.child0,height-1);
			 }else if(key.compareTo(node.child1.guide)<=0||node.child2==null){
				 return search(key,(InternalNode)node.child1,height-1);
			 }else{
				 return search(key,(InternalNode)node.child2,height-1);
			 }
			 
			 
		 }else{
			 //height = 0
			 if (key.equals(node.guide)){
				 return ((LeafNode)node).value;
			 }else{
				 return -1;
			 }
			 
		 }
		   
	   }
	   //use commonNode to find the node to begin take the path 
	   public static void searchRange(Node node,String start, String end,int height){
		   if(height==0){
			   if(node.guide.compareTo(end)<=0&&node.guide.compareTo(start)>=0){
				   LeafNode temp=(LeafNode)node;
				   System.out.println(node.guide+" "+temp.value);
			   }
		   }else{
			   InternalNode temp=(InternalNode)node;

			   if(start.compareTo(temp.child0.guide)<=0){
				   
				   searchRange(temp.child0,start,end,height-1);
			   }
			   if(start.compareTo(temp.child1.guide)<=0&&end.compareTo(temp.child0.guide)>0){
				   
				   searchRange(temp.child1,start,end,height-1);
			   }
			   
			   if(temp.child2!=null){
				   if(start.compareTo(temp.child2.guide)<=0&&end.compareTo(temp.child1.guide)>0){
					   
					   searchRange(temp.child2,start,end,height-1);
				   }
			   }
		   }  
	   }
	   
	   //to accomodate the middle case 
	   static int searchSmall(String key, InternalNode node,int height,ArrayList<Node> path,String key2,boolean commonNode){

			 if (height>0){
				 if(key.compareTo(node.child0.guide)<=0){
					 if(commonNode){
						 //if false, then it still on the same path as the big key
						 //if true, then begin track path
						 if(node.child2!=null){
							 path.add(node.child2);
						 }	
						 path.add(node.child1);
									 
					 }
					 				 
					 if(key2.compareTo(node.child0.guide)>0){
						 //larger than the node while key 1 is smaller
						 if(!commonNode&&key2.compareTo(node.child1.guide)>0&&node.child2!=null){
							 //middle case //include 
							 path.add(node.child1);
						 }
						 commonNode=true;
					 }
					 
					 if(height==1){
						 //when height=1, the next search method will reach out to leaf
						 //however, we need to include the item that we search
						 //include the case of it searches a general a-z case.
						 path.add(node.child0);
						 if(key.compareTo(node.child0.guide)<0&&key2.compareTo(node.child0.guide)<0){
							 path.clear();
							 return -1;
							 //two strings are both smaller than the smallest element in the array
							 
						 }
					 }
					 return searchSmall(key,(InternalNode)node.child0,height-1,path,key2,commonNode);
				 }else if(key.compareTo(node.child1.guide)<=0||node.child2==null){
					 
					 if(commonNode&&node.child2!=null){
						 path.add(node.child2);
					 }
					 if(height==1){
						 //when height=1, the next search method will reach out to leaf
						 //however, we need to include the item that we search
						 //include the case of it searches a general a-z case.
						 if(key.compareTo(node.child1.guide)<0&&(commonNode==false)){
							 
						 }else{
							 path.add(node.child1);
						 }
						 if(key.compareTo(node.child1.guide)<0&&key2.compareTo(node.child1.guide)<0){
							 path.clear();
							 
						 }
					 }
					 if(key2.compareTo(node.child1.guide)>0){
						 commonNode=true;
					 }
					 return searchSmall(key,(InternalNode)node.child1,height-1,path,key2,commonNode);
				 }else{
					 if(height==1){
						 //when height=1, the next search method will reach out to leaf
						 //however, we need to include the item that we search
						 //include the case of it searches a general a-z case.
						 
						 if(key.compareTo(node.child2.guide)<0&&commonNode){
							 
						 }else{
							 path.add(node.child2);
						 }
						 if(key.compareTo(node.child2.guide)<0&&key2.compareTo(node.child2.guide)<0){
							 path.clear();
							 
						 }
					 }
					 //path.add(node.child2);
					 return searchSmall(key,(InternalNode)node.child2,height-1,path,key2,commonNode);
				 }
				 
				 
			 }else{
				 //height = 0
				 if (key.equals(node.guide)){
					 
					 return ((LeafNode)node).value;
				 }else{
					 return -1;
				 }
				 
			 }
			   
		   }

	   static int searchBig(String key, InternalNode node,int height,ArrayList<Node> path, String Key2,boolean commonNode){
			 if (height>0){
				 if(key.compareTo(node.child0.guide)<=0){
					 if(height==1){
						 //when height=1, the next search method will reach out to leaf
						 //however, we need to include the item that we search
						 //include the case of it searches a general a-z case.
						 

						 if(key.compareTo(node.child0.guide)<0){
							 
						 }else{
							 path.add(node.child0);
						 }
						 
						 if(key.compareTo(node.child0.guide)<0&&(commonNode==false)){
							 path.clear();
							 return -1;
						 }
					 }
					 return searchBig(key,(InternalNode)node.child0,height-1,path,Key2,commonNode);
				 }else if(key.compareTo(node.child1.guide)<=0||node.child2==null){
					 if(commonNode){
						 path.add(node.child0);
					 }
					 
					 if(height==1){
						 //when height=1, the next search method will reach out to leaf
						 //however, we need to include the item that we search
						 //include the case of it searches a general a-z case.
						 
						 if(key.compareTo(node.child0.guide)>0&&key.compareTo(node.child1.guide)<0){
							 
						 }else{
							 path.add(node.child1);
						 }
						 if(key.compareTo(node.child1.guide)<0&&Key2.compareTo(node.child1.guide)<0&&Key2.compareTo(node.child0.guide)>0){
							 path.clear();
						 }
					 }
					 
					 if(Key2.compareTo(node.child0.guide)<=0){
						 commonNode=true;
						 
					 }
					 return searchBig(key,(InternalNode)node.child1,height-1,path,Key2,commonNode);
				 }else{
					 if(commonNode){
						 path.add(node.child0);
						 path.add(node.child1);
					 }
					 if(height==1){
						 //when height=1, the next search method will reach out to leaf
						 //however, we need to include the item that we search
						 //include the case of it searches a general a-z case.
						 if(key.compareTo(node.child2.guide)<0){
							 
						 }else{
							 path.add(node.child2);
						 }
						 if(key.compareTo(node.child2.guide)<0&&Key2.compareTo(node.child2.guide)<0&&Key2.compareTo(node.child1.guide)>0){
							 path.clear();
							 
						 }
						 if(key.compareTo(node.child2.guide)>0&&Key2.compareTo(node.child2.guide)>0){
							 path=null;
							 
						 }
					 }
					 if(Key2.compareTo(node.child1.guide)<=0){
						 commonNode=true;
					 }
					 return searchBig(key,(InternalNode)node.child2,height-1,path,Key2,commonNode);
				 }
				 
				 
			 }else{
				 //height = 0
				 if (key.equals(node.guide)){
					 return ((LeafNode)node).value;
				 }else{
					 return -1;
				 }
				 
			 }
			   
		   }

	   static void printOutput(InternalNode node){
		   boolean leaf=false;
		   
		   if(node.child0!=null){
			   printOutput((InternalNode)node.child0);
		   }else{
			   LeafNode x=(LeafNode)node;
			   leaf=true;
			   System.out.println(x.guide+" "+x.value);
		   }
		   
		   if(!leaf){
			   if(node.child1!=null){
				   printOutput((InternalNode)node.child1);
			   }
		   
		   
			   if(node.child2!=null){
				   printOutput((InternalNode)node.child2);
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
