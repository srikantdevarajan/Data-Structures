package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	private static int checkEquality(String firstWord, String secondWord){
		int comparisonValue = 0;
		if(firstWord.length() > secondWord.length()){
			for(int i =0; i < secondWord.length();i++){
				//check comparison
				if(firstWord.charAt(i) != secondWord.charAt(i)){
					break;
				}
				if(firstWord.charAt(i) == secondWord.charAt(i)){
					comparisonValue++;
					
				}
			}
		}else if(secondWord.length() > firstWord.length()){
			for(int i =0; i < firstWord.length();i++){
				if(firstWord.charAt(i) != secondWord.charAt(i)){
					break;
				}
				if(firstWord.charAt(i)==secondWord.charAt(i)){
					comparisonValue++;
				}
			}
		}else{
			for(int i = 0; i < secondWord.length();i++){
				if(firstWord.charAt(i) != secondWord.charAt(i)){
					break;
				}
				if(secondWord.charAt(i)==firstWord.charAt(i)){
					comparisonValue++;
				}
			}
		}
		return comparisonValue;
	}
	private static int getPrefixNum(String biggerWord, String smallerWord){
		int getFirstofSmaller = smallerWord.charAt(0);
		int prefixNumber = 0;
		for(int i = 0; i < smallerWord.length();i++){
			if(biggerWord.charAt(i) != getFirstofSmaller){
				prefixNumber++;
			}else{
				break;
			}
		}
		return prefixNumber;
	}
	public static TrieNode buildTrie(String[] allWords) {
		TrieNode root;
		root = new TrieNode(null,null,null);
		TrieNode rootChild = new TrieNode(new Indexes(0,(short)0,(short) (allWords[0].length()-1)),null,null);
		root.firstChild = rootChild;
		for(int i = 1; i < allWords.length;i++){
			TrieNode newNode = new TrieNode(new Indexes(i,(short)0,(short) (allWords[i].length()-1)),null,null);
			buildTrie(root.firstChild,newNode, allWords);
		}
		return root;
	}
	private static void buildTrie(TrieNode rootChild, TrieNode toAdd, String[] words){
		TrieNode ptr = rootChild;
		String ptrWord = words[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex+1);
		String toAddWord = words[toAdd.substr.wordIndex].substring(toAdd.substr.startIndex, toAdd.substr.endIndex+1);
		int ptrSize = (ptr.substr.endIndex-ptr.substr.startIndex)+1;
		int comparisonValue = checkEquality(ptrWord,toAddWord);
		TrieNode restofPtr;
		int ptrWordIndex = getPrefixNum(words[ptr.substr.wordIndex],ptrWord);
		int getToAddWordIndex = getPrefixNum(words[toAdd.substr.wordIndex], toAddWord);
		int toAddWordSize = (toAdd.substr.endIndex-toAdd.substr.startIndex)+1;
		if(comparisonValue==0){
			if(ptr.sibling==null){
				ptr.sibling=toAdd;
				return;
			}else{
				ptr=ptr.sibling;
				buildTrie(ptr,toAdd,words);
				return;
			}
		}else if(comparisonValue>0){
			if(ptrSize==comparisonValue){
				toAdd.substr.startIndex = (short) (comparisonValue+getToAddWordIndex);
				ptr = ptr.firstChild;
				buildTrie(ptr,toAdd,words);
				return;
				//edge case
			}else if(ptrSize==2 && toAddWordSize==2 && comparisonValue==1){
				
				restofPtr=new TrieNode(new Indexes(ptr.substr.wordIndex, (short) (ptr.substr.endIndex), (short) (ptr.substr.endIndex)),null,null);
				ptr.substr.endIndex = (short)(ptr.substr.endIndex-1);
				toAdd.substr.startIndex = (short) (ptr.substr.endIndex+1);
				if(ptr.firstChild!=null){
					restofPtr.firstChild = ptr.firstChild;
					ptr.firstChild = restofPtr;
					ptr = ptr.firstChild;
				}else{
					ptr.firstChild = restofPtr;
					ptr = ptr.firstChild;
				}
				buildTrie(ptr,toAdd,words);
				return;
			}
			restofPtr = new TrieNode(new Indexes(ptr.substr.wordIndex, (short) (ptrWordIndex+comparisonValue), (short) ptr.substr.endIndex), null,null);
			ptr.substr.endIndex = (short) ((ptrWordIndex+comparisonValue)-1);
			toAdd.substr.startIndex = (short) (comparisonValue + getToAddWordIndex);
			
			if(ptr.firstChild!=null){
				restofPtr.firstChild = ptr.firstChild;
				ptr.firstChild = restofPtr;
				ptr = ptr.firstChild;
			}else{
				ptr.firstChild = restofPtr;
				ptr = ptr.firstChild;
			}
			buildTrie(ptr,toAdd,words);
		}
		return;
	}
	public static ArrayList<TrieNode> completionList(TrieNode root,String[] allWords, String prefix) {
		ArrayList<TrieNode> ret = new ArrayList<TrieNode>();
		TrieNode ptr = root.firstChild;
		TrieNode targetPtr = null;
		while(ptr!=null){
			String ptrWord = allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex+1);
			int comparisonValue = checkEquality(ptrWord,prefix);
			if(comparisonValue<prefix.length() && comparisonValue>0){
				prefix = prefix.substring(comparisonValue, prefix.length());
				if(ptr.firstChild!=null){
					ptr = ptr.firstChild;
					continue;
				}
			}else if(comparisonValue ==0){	
				ptr = ptr.sibling;
			}else if(comparisonValue == prefix.length()){
				targetPtr = ptr;
				break;
			}
		}
		if(targetPtr==null){
			return null;
		}
		if(targetPtr.firstChild==null){
			ret.add(targetPtr);
			return ret;
		}else{
			return completionList(targetPtr,allWords,ret);
		}
	}
	private static ArrayList<TrieNode> completionList(TrieNode rootPtr,String[] allWords,ArrayList<TrieNode> ret){
		TrieNode ptr;
		if(rootPtr.firstChild!=null){
			 ptr = rootPtr.firstChild;
		}else{
			 ptr = rootPtr;
		}
		while(ptr!=null){
			if(ptr.firstChild!=null){
				ret = addLeafNodes(ptr.firstChild,allWords,ret);
				ptr = ptr.sibling;
			}else if(ptr.firstChild==null){
				ret.add(ptr);
				ptr = ptr.sibling;
			}
		}
		return ret;
	}
	private static ArrayList<TrieNode> addLeafNodes(TrieNode leafNode, String[] allWords, ArrayList<TrieNode> ret){
		TrieNode leafPtr = leafNode;
		while(leafPtr!=null){
			if(leafPtr.firstChild!=null){
				ret = addLeafNodes(leafPtr.firstChild,allWords,ret);
			}else{
				ret.add(leafPtr);
			}
			leafPtr = leafPtr.sibling;
		}
		return ret;
	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }