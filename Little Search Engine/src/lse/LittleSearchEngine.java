package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	public static void main(String[] args) throws FileNotFoundException{
		
		LittleSearchEngine cutetwo = new LittleSearchEngine();
		HashMap<String, Occurrence> cute = new HashMap<String, Occurrence>();
		cutetwo.makeIndex("docs2.txt", "noisewords.txt");
		System.out.println(cutetwo.keywordsIndex.size());
		
		
		
		
	}
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;

	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		HashMap<String, Occurrence> newHashMap = new HashMap<String, Occurrence>();
		Scanner docs = new Scanner(new File(docFile));//scanner to read the file
		while(docs.hasNext()){
			String wordAtcurrent = docs.next();//get current word
			wordAtcurrent = getKeyword(wordAtcurrent);//either null or the stripped word
			 if(wordAtcurrent==null){//if it is null, that means it is a noise word
					continue;
				}
			 else if(newHashMap.containsKey(wordAtcurrent)){
					newHashMap.get(wordAtcurrent).frequency++;
				}else{
					newHashMap.put(wordAtcurrent, new Occurrence(docFile,1));
			}
		}
		//close the scanner
		docs.close();
		return newHashMap;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 * kws --> keywords
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		/** COMPLETE THIS METHOD **/
		//up until this point, keywordsIndex is empty
		for(String s : kws.keySet()){
			if(keywordsIndex.containsKey(s)==false){
				ArrayList<Occurrence> a = new ArrayList<Occurrence>();
				//get the Occurrenece of they key
				Occurrence occAtkey = kws.get(s);
				//add said Occurrence to the arraylist
				a.add(occAtkey);
				//insert
				//put the arraylist occurrence into keywordsIndex
				insertLastOccurrence(a);
				keywordsIndex.put(s, a);
			}
			else{
				//retrieve said arraylist
				keywordsIndex.get(s).add(kws.get(s));
				insertLastOccurrence(keywordsIndex.get(s));
				//insert the word into said arraylist
			}
		}
		return;
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		
		/** COMPLETE THIS METHOD **/
		
		for(int i = 0; i < word.length()-1;i++){
			char first = word.charAt(i);
			char second = word.charAt(i+1);
			
			if((Character.isLetter(first)==false) && (Character.isLetter(second)==true)){
				return null;
			}
		}
		//strip all punctuation
		word = word.toLowerCase();
		word = word.replaceAll("\\~", "");
		word = word.replaceAll("\\.", "");
		word = word.replaceAll("\\,", "");
		word = word.replaceAll("\\!", "");
		word = word.replaceAll("\\?", "");
		word = word.replaceAll("\\:", "");
		word = word.replaceAll("\\;", "");
		
		if(word.equals("")==false && word.equals(" ")==false && (noiseWords.contains(word)==false)){
			return word;
		}
		else{
			return null;
		}
		
		
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		if(occs.size()==1){
			return null;
		}
		else{
		ArrayList<Integer> midpoints = new ArrayList<Integer>();
		int low = 0;
		int high = occs.size()-2;
		int lastFrequency = occs.get(occs.size()-1).frequency;
		int middle=0;
		while(low <= high){
			 middle = (low + high)/2;
			midpoints.add(middle);
			
			if(occs.get(middle).frequency>lastFrequency){
				low = middle+1;
			}
			else if(lastFrequency > occs.get(middle).frequency){
				high = middle-1;
			}else if(lastFrequency == occs.get(middle).frequency){
				break;
			}
		}
		//EDGE CASE --> YOU NEED TO ADD THE INDEX of MIDDLE TO MORE THAN ONE BECAUSE YOU NEED TO ADD TO THE END OF THE LIST
		//INCREMENT MIDDLE TO BE PAST LOW SO IT DOESNT OVERWRITE LOW'S POSITION
		if(occs.get(middle).frequency>lastFrequency) {
			middle++;
		}
		if(occs.get(middle).frequency==lastFrequency){
			middle++;
		}
		occs.add(middle, occs.get(occs.size()-1));
		occs.remove(occs.size()-1);
		return midpoints;
		}
	}
	
	private ArrayList<Occurrence> sortArrayListOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		if(occs.size()==1){
			return occs;
		}
		else{
		int low = 0;
		int high = occs.size()-2;
		int lastFrequency = occs.get(occs.size()-1).frequency;
		int middle=0;
		while(low <= high){
			 middle = (low + high)/2;
			if(occs.get(middle).frequency>lastFrequency){
				low = middle+1;
			}
			else if(lastFrequency > occs.get(middle).frequency){
				high = middle-1;
			}else if(lastFrequency == occs.get(middle).frequency){
				
				break;
			}
		}
		//EDGE CASE --> YOU NEED TO ADD THE INDEX of MIDDLE TO MORE THAN ONE BECAUSE YOU NEED TO ADD TO THE END OF THE LIST
		//INCREMENT MIDDLE TO BE PAST LOW SO IT DOESNT OVERWRITE LOW'S POSITION
		if(occs.get(middle).frequency>lastFrequency) {
			middle++;
		}
		if(occs.get(middle).frequency==lastFrequency){
			middle++;
		}
		occs.add(middle, occs.get(occs.size()-1));
		occs.remove(occs.size()-1);
		
		}
		return occs;
	}

	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	
	
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, returns null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		ArrayList<String> finalStringList = new ArrayList<String>();

		ArrayList<String> frequencyList = new ArrayList<String>();
		if(keywordsIndex.containsKey(kw1)==false && keywordsIndex.containsKey(kw2)==false){
			return null;
		}else if(keywordsIndex.containsKey(kw1)==false && keywordsIndex.containsKey(kw2)==true){
			for(int j = 0; j < 5;j++){
				if(j==keywordsIndex.get(kw2).size()) {
					break;
				}
				frequencyList.add(keywordsIndex.get(kw2).get(j).document);
			}
			return frequencyList;
		}else if(keywordsIndex.containsKey(kw2)==false && keywordsIndex.containsKey(kw1)==true){
			for(int j= 0; j < 5;j++){
				if(j==keywordsIndex.get(kw1).size()){
					break;
				}
				frequencyList.add(keywordsIndex.get(kw1).get(j).document);
			}
			return frequencyList;
		}else{
			ArrayList<Occurrence> FirstKeywordsList = keywordsIndex.get(kw1);
			ArrayList<Occurrence> SecondKeywordsList = keywordsIndex.get(kw2);
			//System.out.println(kw1 +": " +  FirstKeywordsList);
			//System.out.println(kw2 + ": "+ SecondKeywordsList);
			
			ArrayList<Occurrence> smallerList;
			ArrayList<Occurrence> biggerList;
			
			if(FirstKeywordsList.size() > SecondKeywordsList.size()){
				
				smallerList = SecondKeywordsList;
				
				biggerList = FirstKeywordsList;
			}else if(SecondKeywordsList.size() > FirstKeywordsList.size()){
				smallerList = FirstKeywordsList;
				biggerList = SecondKeywordsList;
			}else{
				//doesn't matter
				biggerList = SecondKeywordsList;
				smallerList = FirstKeywordsList;
			}
			
			
			//smallerList --> Arraylist with less indexes
			//biggerList --> Arraylest with more indexes
			//System.out.println();
			//System.out.println("bigger list: " + biggerList);
			//System.out.println("smaller list: " + smallerList);
			
			//now create an Iterator to iterate through the small list
			
			Iterator smallItr = smallerList.iterator();
			Iterator bigItr = biggerList.iterator();
			while(smallItr.hasNext()){
				Occurrence smallOccurrence =(Occurrence) smallItr.next();
				String smallOccurrenceDocument = smallOccurrence.document;
				int smallOccurrenceFrequency = smallOccurrence.frequency;
				while(bigItr.hasNext()){
					Occurrence bigOccurrence = (Occurrence) bigItr.next();
					String bigOccurrenceDocument = bigOccurrence.document;
					int bigOccurrenceFrequency = bigOccurrence.frequency;
					
					
					if(bigOccurrence.document.equals(smallOccurrence.document)){
						if(bigOccurrence.frequency > smallOccurrence.frequency){
							smallItr.remove();
						}else{
							bigItr.remove();
						}
					}
				}
				bigItr = biggerList.iterator();
			}
			//System.out.println();
			//System.out.println("bigger list: " + biggerList);
			//System.out.println("smaller list: " + smallerList);
			smallItr = smallerList.iterator();
			bigItr = biggerList.iterator();
			
			
			//now create a new ArrayList of Occurrences
			ArrayList<Occurrence> finalFrequencyList = new ArrayList<Occurrence>();
			
			while(bigItr.hasNext()){
				Occurrence o = (Occurrence) bigItr.next();
				finalFrequencyList.add(o);
				//System.out.println(finalFrequencyList);
				
				
				
			}
			
			while(smallItr.hasNext()){
				Occurrence o = (Occurrence) smallItr.next();
				finalFrequencyList.add(o);
				finalFrequencyList = sortArrayListOccurrence(finalFrequencyList);

			}
			
			
			//System.out.println(finalFrequencyList);
			
			//finalFrequencyList is populated now just get a string value
			
			
			
			for(int i = 0; i < 5; i++){
				if(i==finalFrequencyList.size()) {
					break;
				}
				finalStringList.add(finalFrequencyList.get(i).document);
				
			}
		}
		
		
		return finalStringList;
	
	}
}
