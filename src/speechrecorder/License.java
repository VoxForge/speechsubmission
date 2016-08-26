package speechrecorder;

public class License {
	/** OS independent line separator */
	private static final String L =  System.getProperty("line.separator");
	
	private static final String licenseFAQ = "A. What is Copyright?:"+ L + L+ 
			"Copyright is a set of exclusive rights regulating the use of a particular expression " + L + 
		    "of an idea.  At its most general, it is literally 'the right to copy' an original creation." + L + 
		    "By default, only the creator of a work has the right to copy or modify that work. " + L +
		    "For more information, see <http://en.wikipedia.org/wiki/Copyright>." + L +
		    L +
		    "B. What is an Assignment of Copyright?:"+ L + L +
		    "An assignment of Copyright is a transfer of the copyright in a work from one party " + L +
		    "to another.  For more information, see " + L +
		    "<http://en.wikipedia.org/wiki/Copyright#Transfer_and_licensing>." + L +
		    L +
		    "C. What is a License?:"+ L + L +
			"A license is a grant of permission to do something one could not otherwise do. " + L +
			"Without a license, a third party cannot copy, modify or distribute something protected by" + L +
		    "Copyright.  However, the creator of a work can give a third party a license permitting them " + L +
		    "to copy, modify or distribute that work." + L +
		    "For more information, see <http://en.wikipedia.org/wiki/License>." + L +
			L +
		    "D. What is GPL?:"+ L + L +
			"GPL refers to a type of open source license called the 'GNU General Public License'." + L +
			"In general terms, Copyright protects a *creator's* right to control copies and changes to" + L +
			"a work, whereas the GPL license protects a *user's* right to copy and change a work." + L +
			L;

	private static final String VFabout =  
			"VoxForge Speech Submission Application v0.3" + L +
	  		"============================" + L +
			"Allows a user to record their speech and upload it to the VoxForge server" + L + 
	  	    "so that it can be incorporated into the VoxForge speech corpus and used" + L + 
			"in the creation of GPL acoustic models." + L + 
			L +
			"====" + L;

//	private static final String	VFGPLpreamble = "This program is Copyright (C) 2007-2016  VoxForge" + L ;
		
//	private static final String	CMUGPLpreamble = "Unless otherwise indicated, this program is Copyright (C) 2007-2016  VoxForge" + L;		
		
	private static final String	GPLshort = 
			"This program is Copyright (C) 2007-2016  VoxForge" + L + L +
			"This program is free software: you can redistribute it " + L +
			"and/or modify them under the terms of the GNU General " + L +
			"Public License as published by the Free Software Foundation, either " + L +
			"version 3 of the License, or (at your option) any later version." + L +
			L +
			"This program is distributed in the hope that it will be useful," + L +
			"but WITHOUT ANY WARRANTY; without even the implied warranty of" + L +
			"MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the" + L +
			"GNU General Public License for details." + L +
			L +
			"You should have received a copy of the GNU General Public License" + L +
			"along with these files.  If not, see <http://www.gnu.org/licenses/>." + L +
			L;

	private static final String Acknowledgments =			
			"Acknowledgments:" + L + 
			"===========" + L + 
			"This Java Applet also incorporates other open-source code and data:"+ L +
			"  Patch Contributions:"+ L +
			"   - ConfigReader and supporting code modifications (c) 2014 Joseph Lewis"+ L +			
			"  Other Projects:"+ L +
			"   - MoodleSpeex,               (c) 2006 Dan Stowell"+ L +
			"   - JavaSoundDemo,           (c) Sun Microsystems"+ L +
			"Please see the licences stored in the \"copyrights\" folder in the " + L +
			"speechsubmission jar file for details."+ L + L +
			"The English prompts used by the VoxForge speech submission applet were "+ L +
			"derived from CMU's version of the public domain Enron Email Dataset (https://www.cs.cmu.edu/~./enron/)" +
			L ;

		// used for creating Copyright file to be included in a submission
	private static final String blankLicenseNotice = 
		    "These files are free software: you can redistribute them and/or modify" + L +
			"them under the terms of the GNU General Public License as published by" + L +
			"the Free Software Foundation, either version 3 of the License, or" + L +
			"(at your option) any later version." + L +
			L +
			"These files are distributed in the hope that they will be useful," + L +
			"but WITHOUT ANY WARRANTY; without even the implied warranty of" + L +
			"MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the" + L +
			"GNU General Public License for more details." + L +
			L +
			"You should have received a copy of the GNU General Public License" + L +
			"along with these files.  If not, see <http://www.gnu.org/licenses/>." + L +
			L;
		
	public static String getlicenseFAQ() {
    	return licenseFAQ;
    }


	public static String getBlanklicenseNotice() {
		return blankLicenseNotice;
    }
	
	public static String getLicense() {
		return  licenseFAQ + 
		"E. GPL License Notice which will be included with your submission:" + L+ L + 
		"Copyright (C) <year>  Free Software Foundation" + L +
		 L +
		 blankLicenseNotice;
    }
	
	public static String getVFLicense() {
		//return VFabout + L +   
		//	GPLshort + L + 					
		//	Acknowledgments + L + L	+				
		//	gplLicense;		
		return VFabout + L +   
				GPLshort + L + 					
				Acknowledgments;	
    }
}