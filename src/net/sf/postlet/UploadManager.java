package net.sf.postlet;
/*  Copyright (C) 2005 Simon David Rycroft

	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA. */

import java.io.File;
import java.io.IOException;
import java.net.*;

// Note, the upload manager extends Thread so that the GUI is
// still responsive, and updates.

public class UploadManager extends Thread {
	
	File [] files;
	PostletInterface main;
	URL destination;
	private int maxThreads = 5;
	String fileFieldName;
	
	/** Creates a new instance of Upload */
	public UploadManager(File [] f, PostletInterface m, URL d, String ffn){
		files = f;
		main = m;
		destination = d;
		fileFieldName = ffn;
	}
	
	public UploadManager(File [] f, PostletInterface m, URL d, int max, String ffn){
		try {
			if (max>5 || max < 1)
				max = 5;
			maxThreads = max;
		}
		catch (NullPointerException npe){
			maxThreads = 5;// Leave the maxThreads as default
		}
		files = f;
		main = m;
		destination = d;
		fileFieldName = ffn;
	}
	
	public void run() {
		UploadThread u[] = new UploadThread[files.length];
		for(int i=0; i<files.length; i+=maxThreads) {
			//UploadThread u = new UploadThread(destination,files[i], main);
			//u.upload();
			int j=0;
			while(j<maxThreads && (i+j)<files.length)
			{
				try{
					u[i+j] = new UploadThread(destination,files[i+j],  main, fileFieldName);
					u[i+j].start();}
				catch(UnknownHostException uhe) {System.out.println("*** UnknownHostException: UploadManager ***");}
				catch(IOException ioe)		  {System.out.println("*** IOException: UploadManager ***");}
				j++;
			}
			// wait for the last one to started to finish (means there may be others running still FIXME!
			while(u[i+j-1].isAlive()){;}
		}
	}

	@SuppressWarnings("unused")
	private void urlFailure(){
		// Output a message explaining that the URL has failed.
		// This should stop all the threads!
        System.err.println("urlFailure ");

	}
}
