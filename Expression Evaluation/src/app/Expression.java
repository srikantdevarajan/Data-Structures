private static void buildTrie(TrieNode rootChild, TrieNode newNode, String[] words){
		TrieNode ptr = rootChild;
		String firstWord = words[ptr.substr.wordIndex].substring(ptr.substr.startIndex,ptr.substr.endIndex+1);
		String secondWord =words[newNode.substr.wordIndex].substring(newNode.substr.startIndex, newNode.substr.endIndex+1);
		System.out.println("NEW FUNCTION CALL");
		String fullPtrWord = words[ptr.substr.wordIndex];
		int ptrStripStartIndex = getStartingSubNode(firstWord,fullPtrWord);
		int ptrStripEndIndex = words[ptr.substr.wordIndex].length()-1;
		String fullnewNodeWord = words[newNode.substr.wordIndex];
		int newNodeStripStartIndex = getStartingSubNode(secondWord,fullnewNodeWord);
		int newNodeStripEndIndex = words[ptr.substr.wordIndex].length()-1;
		if(secondWord.length()>0 || firstWord.length()>0){
			newNodeStripStartIndex = getStartingSubNode(secondWord,fullnewNodeWord);
			newNodeStripEndIndex = words[newNode.substr.wordIndex].length()-1;
		}
		System.out.println("ptr input: " + words[ptr.substr.wordIndex].substring(ptrStripStartIndex,ptrStripEndIndex+1));
		System.out.println("newNode input: "+ words[newNode.substr.wordIndex].substring(newNodeStripStartIndex,newNodeStripEndIndex+1));
		int comparisonValue = checkEquality(firstWord,secondWord);
		System.out.println();
		System.out.println("comparison value" + comparisonValue);
		 if(firstWord.length()==comparisonValue){
			int splitPoint = findSplitPoint(firstWord,secondWord)-1;
			//System.out.println(splitPoint);
			int getNewNodeEnd = secondWord.length()-1;
			TrieNode restNewNode = new TrieNode(new Indexes(newNode.substr.wordIndex, (short) (splitPoint), (short) getNewNodeEnd),null,null);
			String word = words[restNewNode.substr.wordIndex].substring(restNewNode.substr.startIndex, getNewNodeEnd+1);
			//System.out.println(word);
			if(ptr.firstChild==null){
				ptr.firstChild= restNewNode;
			}else{
				ptr = ptr.firstChild;
				buildTrie(ptr,restNewNode,words);
			}
		}
		 else if(comparisonValue > 0){
			if(firstWord.length()!=comparisonValue){
				System.out.println("HELLO");
				int splitPoint = findSplitPoint(firstWord,secondWord)-1;
				System.out.println("SPLIT POINT" + splitPoint);
				System.out.println(firstWord.charAt(splitPoint));
				System.out.println(secondWord.charAt(splitPoint));
				TrieNode ptrRest = new TrieNode(new Indexes(newNode.substr.wordIndex, (short) newNodeStripStartIndex, (short) newNodeStripEndIndex),null,null);
				TrieNode newPtrNode = new TrieNode(new Indexes(ptr.substr.wordIndex, (short) ptrStripStartIndex, (short) ptrStripEndIndex),null,null);
				if(ptr.firstChild!=null){
					newPtrNode.firstChild = ptr.firstChild;
				}
				ptr.substr.endIndex=(short) splitPoint;
				newPtrNode.substr.startIndex = (short) (ptr.substr.endIndex+1);
				ptrRest.substr.startIndex = (short) (ptr.substr.endIndex+1);
				ptr.firstChild=newPtrNode;
				ptr = ptr.firstChild;
				buildTrie(ptr,ptrRest,words);
			}
		}
		else if(comparisonValue ==0){
			if(ptr.sibling==null){
				ptr.sibling = newNode;
			}else{
				buildTrie(ptr.sibling,newNode,words);
			}
		}
		
	}