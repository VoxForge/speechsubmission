package speechrecorder;

import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.io.*;

/**
 * Localizes the speech submission applet in various languages
 *   
 * <h1>License</h1>
 * <p>Copyright (C) 2007-2014 Ken MacLean
 *
 * <p>This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * <p>This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * <p>You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
class Prompts  {
	//todo need to convert to use .po files...
	/*
	 * Class variables
	 */
	private String [] promptList;
    private String [] englishPromptList = { 
		 "rp-01 When the sunlight strikes raindrops in the air,",
		 "rp-02 they act as a prism and form a rainbow.",
		 "rp-03 The rainbow is a division of white light into many beautiful colors.",
		 "rp-04 These take the shape of a long round arch, with its path high above,",
		 "rp-05 and its two ends apparently beyond the horizon.",
		 "rp-06 There is , according to legend, a boiling pot of gold at one end.",
		 "rp-07 People look, but no one ever finds it.",
		 "rp-08 When a man looks for something beyond his reach,",
		 "rp-09 his friends say he is looking for the pot of gold at the end of the rainbow.",
		 "rp-10 Throughout the centuries people have explained the rainbow in various ways.",
		 "rp-11 Some have accepted it as a miracle without physical explanation.",
		 "rp-12 To the Hebrews it was a token that there would be no more universal floods.",
		 "rp-13 The Greeks used to imagine that it was a sign",
		 "rp-14 from the gods to foretell war or heavy rain.",
		 "rp-15 The Norsemen considered the rainbow as a bridge",
		 "rp-16 over which the gods passed from earth to their home in the sky.",
		 "rp-17 Others have tried to explain the phenomenon physically.",
		 "rp-18 Aristotle thought that the rainbow was caused by",
		 "rp-19 reflection of the sun's rays by the rain.",
		 "rp-20 Since then physicists have found that it is not reflection,",
		 "rp-21 but refraction by the raindrops which causes the rainbows.",
		 "rp-22 Many complicated ideas about the rainbow have been formed.",
		 "rp-23 The difference in the rainbow depends considerably upon the size of the drops,",
		 "rp-24 and the width of the colored band increases as the size of the drops increases.",
		 "rp-25 The actual primary rainbow observed is said to be the effect of",
		 "rp-26 super-imposition of a number of bows.",
		 "rp-27 If the red of the second bow falls upon the green of the first,",
		 "rp-28 the result is to give a bow with an abnormally wide yellow band,",
		 "rp-29 since red and green light when mixed form yellow.",
		 "rp-30 This is a very common type of bow, one showing mainly red and yellow,",
		 "rp-31 with little or no green or blue." ,
		 "ar-01 Once there was a young rat named Arthur who never could make up his mind.",
		 "ar-02 Whenever his friends asked him if he would like to go out with them,",
		 "ar-03 he would only answer, \"I don't know;\" he wouldn't say yes or no either.",
		 "ar-04 He would always shirk making a choice. His Aunt Helen said to him,",
		 "ar-05 \"Now look here! No one is going to care for you if you carry on like this.\"",
		 "ar-06 You have no more mind than a blade of grass.",
		 "ar-07 One rainy day the rats heard a great noise in the loft.",
		 "ar-08 The pine rafters were all rotten, so that the barn was rather unsafe.",
		 "ar-09 At last the joists gave way and fell to the ground.",
		 "ar-10 The walls shook, and all the rats' hair stood on end with fear and horror.",
		 "ar-11 \"This won't do,\" said the captain; \"I'll send out scouts to search for a new home.\"",
		 "ar-12 Within five hours the ten scouts came back and said,",
		 "ar-13 We found a stone house where there is room for us all.",
		 "ar-14 There is a kindly horse named Nelly, a cow, a calf, and a garden with an elm tree.",
		 "ar-15 The rats crawled out of their little houses and stood on the floor in a long line.",
		 "ar-16 Just then the old rat saw Arthur. Stop. he ordered coarsely.",
		 "ar-17 \"You are coming, of course.\" \"I'm not certain,\" said Arthur, undaunted,",
		 "ar-18 \"The roof may not come down yet.\"",
		 "ar-19 \"Well,\" said the old rat, \"we can't wait for you to join us. Right about face! March!\"",
		 "ar-20 Arthur stood and watched them hurry away.",
		 "ar-21 \"I think I'll go tomorrow,\" he said calmly to himself, \"but then again I don't know;\"",
		 "ar-22 \"it's so nice and snug here,\".  That night there was a big crash.",
		 "ar-23 In the foggy morning some men with some boys and girls rode up and looked at the barn.",
		 "ar-24 One of them moved a board and saw a rat quite dead, half in and half out of his hole.",
	};
    
    private int numberofPrompts;
    
    private String [][] promptSubset;
    
    /**
     *  
     * determine which prompt list to use based on ISO 639-2 language codes
     * (see http://www.loc.gov/standards/iso639-2/php/code_list.php)
     *  
     * @param numberofPrompts
     * @param Language
     */
	public Prompts(String Language, int numberofPrompts) {
		this.numberofPrompts = numberofPrompts;
		
		if (Language.equalsIgnoreCase("EN")) { // english
//			promptList = getPromptTextFile("prompts/englishPromptList.txt", 1223); // number of prompts needs to be exact or there might be an error on a roll-over
			promptList = getPromptTextFile("en", "prompts/PromptList_en.txt", 1177); // number of prompts needs to be exact or there might be an error on a roll-over

		} else if (Language.equalsIgnoreCase("nl")) { // dutch
			promptList = getPromptTextFile("nl","prompts/PromptList_nl.txt", 947);
		} else if (Language.equalsIgnoreCase("de")) { // german
			promptList = getPromptTextFile("prompts/PromptList_de.txt", 1211);
		} else if (Language.equalsIgnoreCase("ru")) { //russian
			promptList = getPromptTextFile("prompts/PromptList_ru.txt", 81);	
		} else if (Language.equalsIgnoreCase("it")) { // italian
			promptList = getPromptTextFile("it","prompts/PromptList_it.txt", 1185);
		} else if (Language.equalsIgnoreCase("he")) { // hebrew
			promptList = getPromptTextFile("prompts/PromptList_he.txt", 55);	
		} else if (Language.equalsIgnoreCase("pt")) { // portuguese
			promptList = getPromptTextFile("prompts/PromptList_pt.txt", 218);	
		} else if (Language.equalsIgnoreCase("es")) { // spanish
			promptList = getPromptTextFile("es","prompts/PromptList_es.txt", 43);	
		} else if (Language.equalsIgnoreCase("fr")) { // french
			promptList = getPromptTextFile("prompts/PromptList_fr.txt", 865);	
		} else if (Language.equalsIgnoreCase("el")) { // greek
			promptList = getPromptTextFile("el","prompts/PromptList_el.txt", 221);		
		} else if (Language.equalsIgnoreCase("tr")) { // turkish
			promptList = getPromptTextFile("tr","prompts/PromptList_tr.txt", 40 );		
		} else if (Language.equalsIgnoreCase("bg")) { // bulgarian
			promptList = getPromptTextFile("bg","prompts/PromptList_bg.txt", 50 );	
		} else if (Language.equalsIgnoreCase("uk")) { // ukrainian
			promptList = getPromptTextFile("prompts/PromptList_uk.txt", 50);		
		} else if (Language.equalsIgnoreCase("ca")) { // catalan
			promptList = getPromptTextFile("ca", "prompts/PromptList_ca.txt", 40);				
		} else if (Language.equalsIgnoreCase("hr")) { // croatian
			promptList = getPromptTextFile("hr", "prompts/PromptList_hr.txt", 98);
		} else if (Language.equalsIgnoreCase("sq")) { // albanian
			promptList = getPromptTextFile("sq", "prompts/PromptList_sq.txt", 56);			
		} else if (Language.equalsIgnoreCase("fa")) { // persian
			promptList = getPromptTextFile("fa", "prompts/PromptList_fa.txt", 50);
		} else if (Language.equalsIgnoreCase("zh")) { // persian
			promptList = getPromptTextFile("zh", "prompts/PromptList_zh.txt", 50);			
		} else {
	        System.err.println("WARNING getPromptTextFile error accessing prompt file for ["+ Language + "]... using default English prompts");
			promptList = englishPromptList;
		}
		
		promptSubset = new String [2][numberofPrompts];
	    
		Random randomGenerator = new Random();
		int arrayLength = promptList.length - 1;
		int nextPrompt = randomGenerator.nextInt(arrayLength);
		for (int i=0; i<numberofPrompts; i++) {
			if (nextPrompt <= arrayLength) {		
				getPromptLine(nextPrompt, i);
			} else {
				nextPrompt = 0;
				getPromptLine(nextPrompt, i);
			}
			nextPrompt++;
		}
	}
	
	public String [][] getPrompts() {
		return promptSubset;
	}
	
	private void getPromptLine(int nextPrompt, int idx) {
	    String promptID;
	    StringBuffer prompt = new StringBuffer();
	    
		StringTokenizer st = new StringTokenizer(promptList[nextPrompt]);
	    String [] words= new String [st.countTokens()];
		int i = 0;
		while (st.hasMoreTokens()) { 
			words[i] = st.nextToken(); 
			i++; 
		}
		promptID = words[0];
	
	    if (words.length > 0) {
	    	for (i=1; i<words.length; i++) {
	        	prompt.append(words[i]);
	        	prompt.append(" ");
	        }
	    }
	    promptSubset [0][idx] = promptID;
	    promptSubset [1][idx] = prompt.toString();
	}	
	
	private String [] getPromptTextFile(String File, int totalNumberOfPrompts) {
	    String [] words= new String [totalNumberOfPrompts];
		try {
		    InputStream is = getClass().getResourceAsStream(File); 
		    InputStreamReader isr = new InputStreamReader(is,"UTF-8" );
			System.out.println(System.getProperty("line.separator") + "PromptList Character Encoding:" + isr.getEncoding());  // doesn't work ????
		    BufferedReader br = new BufferedReader(isr);
		    String line;
		 	int i = 0;    
		    while ((line = br.readLine()) != null) {
				words[i] = line;
				i++; 
		    }
		  } catch (IOException io) {
	          System.out.println("getPromptTextFile error accessing text file for "+File + "using default prompts");
	          return englishPromptList;
		  }
		  return words;
	}
	
	private String [] getPromptTextFile(String Prefix, String File, int totalNumberOfPrompts) {
	    String [] words= new String [totalNumberOfPrompts];
		try {
		    InputStream is = getClass().getResourceAsStream(File);
		    InputStreamReader isr = new InputStreamReader(is,"UTF-8" );
		    System.out.println(System.getProperty("line.separator") + "PromptList Character Encoding:" + isr.getEncoding());  // doesn't work ????
		    BufferedReader br = new BufferedReader(isr);
		    String line;
		 	int i = 0;    
		    while ((line = br.readLine()) != null) {
				// System.err.println(Prefix + "-" + paddedZeros(i,4) + " " + line); // !!!!!!
				words[i] = Prefix + "-" + paddedZeros(i,4) + " " + line;
				i++; 
		    }
		  } catch (IOException io) {
	          System.err.println("getPromptTextFile error accessing text file for "+File + "using default prompts");
	          return englishPromptList;
		  }
		  return words;
	}
	
	public String paddedZeros(int number, int numLength)
	{
		String s = Integer.toString(number);
		if (s.length() > numLength) {
			return s.substring(0,numLength);
		} else if (s.length() < numLength) { 
		   return "0000000000".substring(0, numLength - s.length ()) + s;
		} else { 
			return s;
		}
	} 

	public int getNumberOfPrompts()
	{
		return this.numberofPrompts;
	}
	
	public void setNumberOfPrompts(int numberofPrompts)
	{
		this.numberofPrompts = numberofPrompts;
	}
}