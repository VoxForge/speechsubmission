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

	/*
	private static final String	CMUlicenseNotice =  
			"============================" + L +
			"The English prompts used by the VoxForge speech submission applet contain prompts" + L +
			"from the CMU_ARCTIC speech synthesis databases." + L +
			L +		
			"The CMU_ARCTIC databases were constructed at the Language Technologies Institute" + L +
			"at Carnegie Mellon University as phonetically balanced, US English single speaker" + L +
			"databases designed for unit selection speech synthesis research." + L +
			L + 	
			"CMU_ARCTIC license:" + L +		
			L + 			
			"    This voice is free for use for any purpose (commercial or otherwise)" + L +
			"    subject to the pretty light restrictions detailed below." + L +
			L +
			"    ########################################################" + L +
			"    ###                                                                       " + L +
			"    ###                     Carnegie Mellon University                        " + L +
			"    ###                         Copyright (c) 2005                            " + L +
			"    ###                        All Rights Reserved.                           " + L +
			"    ###                                                                       " + L +
			"    ###  Permission to use, copy, modify,  and licence this software and its  " + L +
			"    ###  documentation for any purpose, is hereby granted without fee,        " + L +
			"    ###  subject to the following conditions:                                 " + L +
			"    ###   1. The code must retain the above copyright notice, this list of    " + L +
			"    ###      conditions and the following disclaimer.                         " + L +
			"    ###   2. Any modifications must be clearly marked as such.                " + L +
			"    ###   3. Original authors' names are not deleted.                         " + L +
			"    ###                                                                       " + L +
			"    ###  THE AUTHORS OF THIS WORK DISCLAIM ALL WARRANTIES WITH REGARD TO      " + L +
			"    ###  THIS SOFTWARE, INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY   " + L +
			"    ###  AND FITNESS, IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY         " + L +
			"    ###  SPECIAL, INDIRECT OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES            " + L +
			"    ###  WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN   " + L +
			"    ###  AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION,          " + L +
			"    ###  ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF       " + L +
			"    ###  THIS SOFTWARE.                                                       " + L +
			"    ###                                                                       " + L +
			"    ########################################################" + L +
			"    ###                                                                       " + L +
			"    ###  See http://www.festvox.org/cmu_arctic/ for more details              " + L +
			"    ###                                                                       " + L +
			"    ########################################################"	 + L +
		 	L;				
	*/
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
		
		
	private static final String gplLicense = 
			"Full GPL License:" + L +
			"=========" + L +
			"                   GNU GENERAL PUBLIC LICENSE" + L +
			"                       Version 3, 29 June 2007" + L +
			L +
			" Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>" + L +
			" Everyone is permitted to copy and distribute verbatim copies" + L +
			" of this license document, but changing it is not allowed." + L +
			L +
			"                            Preamble" + L +
			L +
			"  The GNU General Public License is a free, copyleft license for" + L +
			"software and other kinds of works." + L +
			L +
			"  The licenses for most software and other practical works are designed" + L +
			"to take away your freedom to share and change the works.  By contrast," + L +
			"the GNU General Public License is intended to guarantee your freedom to" + L +
			"share and change all versions of a program--to make sure it remains free" + L +
			"software for all its users.  We, the Free Software Foundation, use the" + L +
			"GNU General Public License for most of our software; it applies also to" + L +
			"any other work released this way by its authors.  You can apply it to" + L +
			"your programs, too." + L +
			L +
			"  When we speak of free software, we are referring to freedom, not" + L +
			"price.  Our General Public Licenses are designed to make sure that you" + L +
			"have the freedom to distribute copies of free software (and charge for" + L +
			"them if you wish), that you receive source code or can get it if you" + L +
			"want it, that you can change the software or use pieces of it in new" + L +
			"free programs, and that you know you can do these things." + L +
			L +
			"  To protect your rights, we need to prevent others from denying you" + L +
			"these rights or asking you to surrender the rights.  Therefore, you have" + L +
			"certain responsibilities if you distribute copies of the software, or if" + L +
			"you modify it: responsibilities to respect the freedom of others." + L +
			L +
			"  For example, if you distribute copies of such a program, whether" + L +
			"gratis or for a fee, you must pass on to the recipients the same" + L +
			"freedoms that you received.  You must make sure that they, too, receive" + L +
			"or can get the source code.  And you must show them these terms so they" + L +
			"know their rights." + L +
			L +
			"  Developers that use the GNU GPL protect your rights with two steps:" + L +
			"(1) assert copyright on the software, and (2) offer you this License" + L +
			"giving you legal permission to copy, distribute and/or modify it." + L +
			L +
			"  For the developers' and authors' protection, the GPL clearly explains" + L +
			"that there is no warranty for this free software.  For both users' and" + L +
			"authors' sake, the GPL requires that modified versions be marked as" + L +
			"changed, so that their problems will not be attributed erroneously to" + L +
			"authors of previous versions." + L +
			L +
			"  Some devices are designed to deny users access to install or run" + L +
			"modified versions of the software inside them, although the manufacturer" + L +
			"can do so.  This is fundamentally incompatible with the aim of" + L +
			"protecting users' freedom to change the software.  The systematic" + L +
			"pattern of such abuse occurs in the area of products for individuals to" + L +
			"use, which is precisely where it is most unacceptable.  Therefore, we" + L +
			"have designed this version of the GPL to prohibit the practice for those" + L +
			"products.  If such problems arise substantially in other domains, we" + L +
			"stand ready to extend this provision to those domains in future versions" + L +
			"of the GPL, as needed to protect the freedom of users." + L +
			L +
			"  Finally, every program is threatened constantly by software patents." + L +
			"States should not allow patents to restrict development and use of" + L +
			"software on general-purpose computers, but in those that do, we wish to" + L +
			"avoid the special danger that patents applied to a free program could" + L +
			"make it effectively proprietary.  To prevent this, the GPL assures that" + L +
			"patents cannot be used to render the program non-free." + L +
			L +
			"  The precise terms and conditions for copying, distribution and" + L +
			"modification follow." + L +
			L +
			"                       TERMS AND CONDITIONS" + L +
			L +
			"  0. Definitions." + L +
			L +
			"  \"This License\" refers to version 3 of the GNU General Public License." + L +
			L +
			"  \"Copyright\" also means copyright-like laws that apply to other kinds of" + L +
			"works, such as semiconductor masks." + L +
			L +
			"  \"The Program\" refers to any copyrightable work licensed under this" + L +
			"License.  Each licensee is addressed as \"you\".  \"Licensees\" and" + L +
			"\"recipients\" may be individuals or organizations." + L +
			L +
			"  To \"modify\" a work means to copy from or adapt all or part of the work" + L +
			"in a fashion requiring copyright permission, other than the making of an" + L +
			"exact copy.  The resulting work is called a \"modified version\" of the" + L +
			"earlier work or a work \"based on\" the earlier work." + L +
			L +
			"  A \"covered work\" means either the unmodified Program or a work based" + L +
			"on the Program." + L +
			L +
			"  To \"propagate\" a work means to do anything with it that, without" + L +
			"permission, would make you directly or secondarily liable for" + L +
			"infringement under applicable copyright law, except executing it on a" + L +
			"computer or modifying a private copy.  Propagation includes copying," + L +
			"distribution (with or without modification), making available to the" + L +
			"public, and in some countries other activities as well." + L +
			L +
			"  To \"convey\" a work means any kind of propagation that enables other" + L +
			"parties to make or receive copies.  Mere interaction with a user through" + L +
			"a computer network, with no transfer of a copy, is not conveying." + L +
			L +
			"  An interactive user interface displays \"Appropriate Legal Notices\"" + L +
			"to the extent that it includes a convenient and prominently visible" + L +
			"feature that (1) displays an appropriate copyright notice, and (2)" + L +
			"tells the user that there is no warranty for the work (except to the" + L +
			"extent that warranties are provided), that licensees may convey the" + L +
			"work under this License, and how to view a copy of this License.  If" + L +
			"the interface presents a list of user commands or options, such as a" + L +
			"menu, a prominent item in the list meets this criterion." + L +
			L +
			"  1. Source Code." + L +
			L +
			"  The \"source code\" for a work means the preferred form of the work" + L +
			"for making modifications to it.  \"Object code\" means any non-source" + L +
			"form of a work." + L +
			L +
			"  A \"Standard Interface\" means an interface that either is an official" + L +
			"standard defined by a recognized standards body, or, in the case of" + L +
			"interfaces specified for a particular programming language, one that" + L +
			"is widely used among developers working in that language." + L +
			L +
			"  The \"System Libraries\" of an executable work include anything, other" + L +
			"than the work as a whole, that (a) is included in the normal form of" + L +
			"packaging a Major Component, but which is not part of that Major" + L +
			"Component, and (b) serves only to enable use of the work with that" + L +
			"Major Component, or to implement a Standard Interface for which an" + L +
			"implementation is available to the public in source code form.  A" + L +
			"\"Major Component\", in this context, means a major essential component" + L +
			"(kernel, window system, and so on) of the specific operating system" + L +
			"(if any) on which the executable work runs, or a compiler used to" + L +
			"produce the work, or an object code interpreter used to run it." + L +
			L +
			"  The \"Corresponding Source\" for a work in object code form means all" + L +
			"the source code needed to generate, install, and (for an executable" + L +
			"work) run the object code and to modify the work, including scripts to" + L +
			"control those activities.  However, it does not include the work's" + L +
			"System Libraries, or general-purpose tools or generally available free" + L +
			"programs which are used unmodified in performing those activities but" + L +
			"which are not part of the work.  For example, Corresponding Source" + L +
			"includes interface definition files associated with source files for" + L +
			"the work, and the source code for shared libraries and dynamically" + L +
			"linked subprograms that the work is specifically designed to require," + L +
			"such as by intimate data communication or control flow between those" + L +
			"subprograms and other parts of the work." + L +
			L +
			"  The Corresponding Source need not include anything that users" + L +
			"can regenerate automatically from other parts of the Corresponding" + L +
			"Source." + L +
			L +
			"  The Corresponding Source for a work in source code form is that" + L +
			"same work." + L +
			L +
			"  2. Basic Permissions." + L +
			L +
			"  All rights granted under this License are granted for the term of" + L +
			"copyright on the Program, and are irrevocable provided the stated" + L +
			"conditions are met.  This License explicitly affirms your unlimited" + L +
			"permission to run the unmodified Program.  The output from running a" + L +
			"covered work is covered by this License only if the output, given its" + L +
			"content, constitutes a covered work.  This License acknowledges your" + L +
			"rights of fair use or other equivalent, as provided by copyright law." + L +
			L +
			"  You may make, run and propagate covered works that you do not" + L +
			"convey, without conditions so long as your license otherwise remains" + L +
			"in force.  You may convey covered works to others for the sole purpose" + L +
			"of having them make modifications exclusively for you, or provide you" + L +
			"with facilities for running those works, provided that you comply with" + L +
			"the terms of this License in conveying all material for which you do" + L +
			"not control copyright.  Those thus making or running the covered works" + L +
			"for you must do so exclusively on your behalf, under your direction" + L +
			"and control, on terms that prohibit them from making any copies of" + L +
			"your copyrighted material outside their relationship with you." + L +
			L +
			"  Conveying under any other circumstances is permitted solely under" + L +
			"the conditions stated below.  Sublicensing is not allowed; section 10" + L +
			"makes it unnecessary." + L +
			L +
			"  3. Protecting Users' Legal Rights From Anti-Circumvention Law." + L +
			L +
			"  No covered work shall be deemed part of an effective technological" + L +
			"measure under any applicable law fulfilling obligations under article" + L +
			"11 of the WIPO copyright treaty adopted on 20 December 1996, or" + L +
			"similar laws prohibiting or restricting circumvention of such" + L +
			"measures." + L +
			L +
			"  When you convey a covered work, you waive any legal power to forbid" + L +
			"circumvention of technological measures to the extent such circumvention" + L +
			"is effected by exercising rights under this License with respect to" + L +
			"the covered work, and you disclaim any intention to limit operation or" + L +
			"modification of the work as a means of enforcing, against the work's" + L +
			"users, your or third parties' legal rights to forbid circumvention of" + L +
			"technological measures." + L +
			L +
			"  4. Conveying Verbatim Copies." + L +
			L +
			"  You may convey verbatim copies of the Program's source code as you" + L +
			"receive it, in any medium, provided that you conspicuously and" + L +
			"appropriately publish on each copy an appropriate copyright notice;" + L +
			"keep intact all notices stating that this License and any" + L +
			"non-permissive terms added in accord with section 7 apply to the code;" + L +
			"keep intact all notices of the absence of any warranty; and give all" + L +
			"recipients a copy of this License along with the Program." + L +
			L +
			"  You may charge any price or no price for each copy that you convey," + L +
			"and you may offer support or warranty protection for a fee." + L +
			L +
			"  5. Conveying Modified Source Versions." + L +
			L +
			"  You may convey a work based on the Program, or the modifications to" + L +
			"produce it from the Program, in the form of source code under the" + L +
			"terms of section 4, provided that you also meet all of these conditions:" + L +
			L +
			"    a) The work must carry prominent notices stating that you modified" + L +
			"    it, and giving a relevant date." + L +
			L +
			"    b) The work must carry prominent notices stating that it is" + L +
			"    released under this License and any conditions added under section" + L +
			"    7.  This requirement modifies the requirement in section 4 to" + L +
			"    \"keep intact all notices\"." + L +
			L +
			"    c) You must license the entire work, as a whole, under this" + L +
			"    License to anyone who comes into possession of a copy.  This" + L +
			"    License will therefore apply, along with any applicable section 7" + L +
			"    additional terms, to the whole of the work, and all its parts," + L +
			"    regardless of how they are packaged.  This License gives no" + L +
			"    permission to license the work in any other way, but it does not" + L +
			"    invalidate such permission if you have separately received it." + L +
			L +
			"    d) If the work has interactive user interfaces, each must display" + L +
			"    Appropriate Legal Notices; however, if the Program has interactive" + L +
			"    interfaces that do not display Appropriate Legal Notices, your" + L +
			"    work need not make them do so." + L +
			L +
			"  A compilation of a covered work with other separate and independent" + L +
			"works, which are not by their nature extensions of the covered work," + L +
			"and which are not combined with it such as to form a larger program," + L +
			"in or on a volume of a storage or distribution medium, is called an" + L +
			"\"aggregate\" if the compilation and its resulting copyright are not" + L +
			"used to limit the access or legal rights of the compilation's users" + L +
			"beyond what the individual works permit.  Inclusion of a covered work" + L +
			"in an aggregate does not cause this License to apply to the other" + L +
			"parts of the aggregate." + L +
			L +
			"  6. Conveying Non-Source Forms." + L +
			L +
			"  You may convey a covered work in object code form under the terms" + L +
			"of sections 4 and 5, provided that you also convey the" + L +
			"machine-readable Corresponding Source under the terms of this License," + L +
			"in one of these ways:" + L +
			L +
			"    a) Convey the object code in, or embodied in, a physical product" + L +
			"    (including a physical distribution medium), accompanied by the" + L +
			"    Corresponding Source fixed on a durable physical medium" + L +
			"    customarily used for software interchange." + L +
			L +
			"    b) Convey the object code in, or embodied in, a physical product" + L +
			"    (including a physical distribution medium), accompanied by a" + L +
			"    written offer, valid for at least three years and valid for as" + L +
			"    long as you offer spare parts or customer support for that product" + L +
			"    model, to give anyone who possesses the object code either (1) a" + L +
			"    copy of the Corresponding Source for all the software in the" + L +
			"    product that is covered by this License, on a durable physical" + L +
			"    medium customarily used for software interchange, for a price no" + L +
			"    more than your reasonable cost of physically performing this" + L +
			"    conveying of source, or (2) access to copy the" + L +
			"    Corresponding Source from a network server at no charge." + L +
			L +
			"    c) Convey individual copies of the object code with a copy of the" + L +
			"    written offer to provide the Corresponding Source.  This" + L +
			"    alternative is allowed only occasionally and noncommercially, and" + L +
			"    only if you received the object code with such an offer, in accord" + L +
			"    with subsection 6b." + L +
			L +
			"    d) Convey the object code by offering access from a designated" + L +
			"    place (gratis or for a charge), and offer equivalent access to the" + L +
			"    Corresponding Source in the same way through the same place at no" + L +
			"    further charge.  You need not require recipients to copy the" + L +
			"    Corresponding Source along with the object code.  If the place to" + L +
			"    copy the object code is a network server, the Corresponding Source" + L +
			"    may be on a different server (operated by you or a third party)" + L +
			"    that supports equivalent copying facilities, provided you maintain" + L +
			"    clear directions next to the object code saying where to find the" + L +
			"    Corresponding Source.  Regardless of what server hosts the" + L +
			"    Corresponding Source, you remain obligated to ensure that it is" + L +
			"    available for as long as needed to satisfy these requirements." + L +
			L +
			"    e) Convey the object code using peer-to-peer transmission, provided" + L +
			"    you inform other peers where the object code and Corresponding" + L +
			"    Source of the work are being offered to the general public at no" + L +
			"    charge under subsection 6d." + L +
			L +
			"  A separable portion of the object code, whose source code is excluded" + L +
			"from the Corresponding Source as a System Library, need not be" + L +
			"included in conveying the object code work." + L +
			L +
			"  A \"User Product\" is either (1) a \"consumer product\", which means any" + L +
			"tangible personal property which is normally used for personal, family," + L +
			"or household purposes, or (2) anything designed or sold for incorporation" + L +
			"into a dwelling.  In determining whether a product is a consumer product," + L +
			"doubtful cases shall be resolved in favor of coverage.  For a particular" + L +
			"product received by a particular user, \"normally used\" refers to a" + L +
			"typical or common use of that class of product, regardless of the status" + L +
			"of the particular user or of the way in which the particular user" + L +
			"actually uses, or expects or is expected to use, the product.  A product" + L +
			"is a consumer product regardless of whether the product has substantial" + L +
			"commercial, industrial or non-consumer uses, unless such uses represent" + L +
			"the only significant mode of use of the product." + L +
			L +
			"  \"Installation Information\" for a User Product means any methods," + L +
			"procedures, authorization keys, or other information required to install" + L +
			"and execute modified versions of a covered work in that User Product from" + L +
			"a modified version of its Corresponding Source.  The information must" + L +
			"suffice to ensure that the continued functioning of the modified object" + L +
			"code is in no case prevented or interfered with solely because" + L +
			"modification has been made." + L +
			L +
			"  If you convey an object code work under this section in, or with, or" + L +
			"specifically for use in, a User Product, and the conveying occurs as" + L +
			"part of a transaction in which the right of possession and use of the" + L +
			"User Product is transferred to the recipient in perpetuity or for a" + L +
			"fixed term (regardless of how the transaction is characterized), the" + L +
			"Corresponding Source conveyed under this section must be accompanied" + L +
			"by the Installation Information.  But this requirement does not apply" + L +
			"if neither you nor any third party retains the ability to install" + L +
			"modified object code on the User Product (for example, the work has" + L +
			"been installed in ROM)." + L +
			L +
			"  The requirement to provide Installation Information does not include a" + L +
			"requirement to continue to provide support service, warranty, or updates" + L +
			"for a work that has been modified or installed by the recipient, or for" + L +
			"the User Product in which it has been modified or installed.  Access to a" + L +
			"network may be denied when the modification itself materially and" + L +
			"adversely affects the operation of the network or violates the rules and" + L +
			"protocols for communication across the network." + L +
			L +
			"  Corresponding Source conveyed, and Installation Information provided," + L +
			"in accord with this section must be in a format that is publicly" + L +
			"documented (and with an implementation available to the public in" + L +
			"source code form), and must require no special password or key for" + L +
			"unpacking, reading or copying." + L +
			L +
			"  7. Additional Terms." + L +
			L +
			"  \"Additional permissions\" are terms that supplement the terms of this" + L +
			"License by making exceptions from one or more of its conditions." + L +
			"Additional permissions that are applicable to the entire Program shall" + L +
			"be treated as though they were included in this License, to the extent" + L +
			"that they are valid under applicable law.  If additional permissions" + L +
			"apply only to part of the Program, that part may be used separately" + L +
			"under those permissions, but the entire Program remains governed by" + L +
			"this License without regard to the additional permissions." + L +
			L +
			"  When you convey a copy of a covered work, you may at your option" + L +
			"remove any additional permissions from that copy, or from any part of" + L +
			"it.  (Additional permissions may be written to require their own" + L +
			"removal in certain cases when you modify the work.)  You may place" + L +
			"additional permissions on material, added by you to a covered work," + L +
			"for which you have or can give appropriate copyright permission." + L +
			L +
			"  Notwithstanding any other provision of this License, for material you" + L +
			"add to a covered work, you may (if authorized by the copyright holders of" + L +
			"that material) supplement the terms of this License with terms:" + L +
			L +
			"    a) Disclaiming warranty or limiting liability differently from the" + L +
			"    terms of sections 15 and 16 of this License; or" + L +
			L +
			"    b) Requiring preservation of specified reasonable legal notices or" + L +
			"    author attributions in that material or in the Appropriate Legal" + L +
			"    Notices displayed by works containing it; or" + L +
			L +
			"    c) Prohibiting misrepresentation of the origin of that material, or" + L +
			"    requiring that modified versions of such material be marked in" + L +
			"    reasonable ways as different from the original version; or" + L +
			L +
			"    d) Limiting the use for publicity purposes of names of licensors or" + L +
			"    authors of the material; or" + L +
			L +
			"    e) Declining to grant rights under trademark law for use of some" + L +
			"    trade names, trademarks, or service marks; or" + L +
			L +
			"    f) Requiring indemnification of licensors and authors of that" + L +
			"    material by anyone who conveys the material (or modified versions of" + L +
			"    it) with contractual assumptions of liability to the recipient, for" + L +
			"    any liability that these contractual assumptions directly impose on" + L +
			"    those licensors and authors." + L +
			L +
			"  All other non-permissive additional terms are considered \"further" + L +
			"restrictions\" within the meaning of section 10.  If the Program as you" + L +
			"received it, or any part of it, contains a notice stating that it is" + L +
			"governed by this License along with a term that is a further" + L +
			"restriction, you may remove that term.  If a license document contains" + L +
			"a further restriction but permits relicensing or conveying under this" + L +
			"License, you may add to a covered work material governed by the terms" + L +
			"of that license document, provided that the further restriction does" + L +
			"not survive such relicensing or conveying." + L +
			L +
			"  If you add terms to a covered work in accord with this section, you" + L +
			"must place, in the relevant source files, a statement of the" + L +
			"additional terms that apply to those files, or a notice indicating" + L +
			"where to find the applicable terms." + L +
			L +
			"  Additional terms, permissive or non-permissive, may be stated in the" + L +
			"form of a separately written license, or stated as exceptions;" + L +
			"the above requirements apply either way." + L +
			L +
			"  8. Termination." + L +
			L +
			"  You may not propagate or modify a covered work except as expressly" + L +
			"provided under this License.  Any attempt otherwise to propagate or" + L +
			"modify it is void, and will automatically terminate your rights under" + L +
			"this License (including any patent licenses granted under the third" + L +
			"paragraph of section 11)." + L +
			L +
			"  However, if you cease all violation of this License, then your" + L +
			"license from a particular copyright holder is reinstated (a)" + L +
			"provisionally, unless and until the copyright holder explicitly and" + L +
			"finally terminates your license, and (b) permanently, if the copyright" + L +
			"holder fails to notify you of the violation by some reasonable means" + L +
			"prior to 60 days after the cessation." + L +
			L +
			"  Moreover, your license from a particular copyright holder is" + L +
			"reinstated permanently if the copyright holder notifies you of the" + L +
			"violation by some reasonable means, this is the first time you have" + L +
			"received notice of violation of this License (for any work) from that" + L +
			"copyright holder, and you cure the violation prior to 30 days after" + L +
			"your receipt of the notice." + L +
			L +
			"  Termination of your rights under this section does not terminate the" + L +
			"licenses of parties who have received copies or rights from you under" + L +
			"this License.  If your rights have been terminated and not permanently" + L +
			"reinstated, you do not qualify to receive new licenses for the same" + L +
			"material under section 10." + L +
			L +
			"  9. Acceptance Not Required for Having Copies." + L +
			L +
			"  You are not required to accept this License in order to receive or" + L +
			"run a copy of the Program.  Ancillary propagation of a covered work" + L +
			"occurring solely as a consequence of using peer-to-peer transmission" + L +
			"to receive a copy likewise does not require acceptance.  However," + L +
			"nothing other than this License grants you permission to propagate or" + L +
			"modify any covered work.  These actions infringe copyright if you do" + L +
			"not accept this License.  Therefore, by modifying or propagating a" + L +
			"covered work, you indicate your acceptance of this License to do so." + L +
			L +
			"  10. Automatic Licensing of Downstream Recipients." + L +
			L +
			"  Each time you convey a covered work, the recipient automatically" + L +
			"receives a license from the original licensors, to run, modify and" + L +
			"propagate that work, subject to this License.  You are not responsible" + L +
			"for enforcing compliance by third parties with this License." + L +
			L +
			"  An \"entity transaction\" is a transaction transferring control of an" + L +
			"organization, or substantially all assets of one, or subdividing an" + L +
			"organization, or merging organizations.  If propagation of a covered" + L +
			"work results from an entity transaction, each party to that" + L +
			"transaction who receives a copy of the work also receives whatever" + L +
			"licenses to the work the party's predecessor in interest had or could" + L +
			"give under the previous paragraph, plus a right to possession of the" + L +
			"Corresponding Source of the work from the predecessor in interest, if" + L +
			"the predecessor has it or can get it with reasonable efforts." + L +
			L +
			"  You may not impose any further restrictions on the exercise of the" + L +
			"rights granted or affirmed under this License.  For example, you may" + L +
			"not impose a license fee, royalty, or other charge for exercise of" + L +
			"rights granted under this License, and you may not initiate litigation" + L +
			"(including a cross-claim or counterclaim in a lawsuit) alleging that" + L +
			"any patent claim is infringed by making, using, selling, offering for" + L +
			"sale, or importing the Program or any portion of it." + L +
			L +
			"  11. Patents." + L +
			L +
			"  A \"contributor\" is a copyright holder who authorizes use under this" + L +
			"License of the Program or a work on which the Program is based.  The" + L +
			"work thus licensed is called the contributor's \"contributor version\"." + L +
			L +
			"  A contributor's \"essential patent claims\" are all patent claims" + L +
			"owned or controlled by the contributor, whether already acquired or" + L +
			"hereafter acquired, that would be infringed by some manner, permitted" + L +
			"by this License, of making, using, or selling its contributor version," + L +
			"but do not include claims that would be infringed only as a" + L +
			"consequence of further modification of the contributor version.  For" + L +
			"purposes of this definition, \"control\" includes the right to grant" + L +
			"patent sublicenses in a manner consistent with the requirements of" + L +
			"this License." + L +
			L +
			"  Each contributor grants you a non-exclusive, worldwide, royalty-free" + L +
			"patent license under the contributor's essential patent claims, to" + L +
			"make, use, sell, offer for sale, import and otherwise run, modify and" + L +
			"propagate the contents of its contributor version." + L +
			L +
			"  In the following three paragraphs, a \"patent license\" is any express" + L +
			"agreement or commitment, however denominated, not to enforce a patent" + L +
			"(such as an express permission to practice a patent or covenant not to" + L +
			"sue for patent infringement).  To \"grant\" such a patent license to a" + L +
			"party means to make such an agreement or commitment not to enforce a" + L +
			"patent against the party." + L +
			L +
			"  If you convey a covered work, knowingly relying on a patent license," + L +
			"and the Corresponding Source of the work is not available for anyone" + L +
			"to copy, free of charge and under the terms of this License, through a" + L +
			"publicly available network server or other readily accessible means," + L +
			"then you must either (1) cause the Corresponding Source to be so" + L +
			"available, or (2) arrange to deprive yourself of the benefit of the" + L +
			"patent license for this particular work, or (3) arrange, in a manner" + L +
			"consistent with the requirements of this License, to extend the patent" + L +
			"license to downstream recipients.  \"Knowingly relying\" means you have" + L +
			"actual knowledge that, but for the patent license, your conveying the" + L +
			"covered work in a country, or your recipient's use of the covered work" + L +
			"in a country, would infringe one or more identifiable patents in that" + L +
			"country that you have reason to believe are valid." + L +
			L +
			"  If, pursuant to or in connection with a single transaction or" + L +
			"arrangement, you convey, or propagate by procuring conveyance of, a" + L +
			"covered work, and grant a patent license to some of the parties" + L +
			"receiving the covered work authorizing them to use, propagate, modify" + L +
			"or convey a specific copy of the covered work, then the patent license" + L +
			"you grant is automatically extended to all recipients of the covered" + L +
			"work and works based on it." + L +
			L +
			"  A patent license is \"discriminatory\" if it does not include within" + L +
			"the scope of its coverage, prohibits the exercise of, or is" + L +
			"conditioned on the non-exercise of one or more of the rights that are" + L +
			"specifically granted under this License.  You may not convey a covered" + L +
			"work if you are a party to an arrangement with a third party that is" + L +
			"in the business of distributing software, under which you make payment" + L +
			"to the third party based on the extent of your activity of conveying" + L +
			"the work, and under which the third party grants, to any of the" + L +
			"parties who would receive the covered work from you, a discriminatory" + L +
			"patent license (a) in connection with copies of the covered work" + L +
			"conveyed by you (or copies made from those copies), or (b) primarily" + L +
			"for and in connection with specific products or compilations that" + L +
			"contain the covered work, unless you entered into that arrangement," + L +
			"or that patent license was granted, prior to 28 March 2007." + L +
			L +
			"  Nothing in this License shall be construed as excluding or limiting" + L +
			"any implied license or other defenses to infringement that may" + L +
			"otherwise be available to you under applicable patent law." + L +
			L +
			"  12. No Surrender of Others' Freedom." + L +
			L +
			"  If conditions are imposed on you (whether by court order, agreement or" + L +
			"otherwise) that contradict the conditions of this License, they do not" + L +
			"excuse you from the conditions of this License.  If you cannot convey a" + L +
			"covered work so as to satisfy simultaneously your obligations under this" + L +
			"License and any other pertinent obligations, then as a consequence you may" + L +
			"not convey it at all.  For example, if you agree to terms that obligate you" + L +
			"to collect a royalty for further conveying from those to whom you convey" + L +
			"the Program, the only way you could satisfy both those terms and this" + L +
			"License would be to refrain entirely from conveying the Program." + L +
			L +
			"  13. Use with the GNU Affero General Public License." + L +
			L +
			"  Notwithstanding any other provision of this License, you have" + L +
			"permission to link or combine any covered work with a work licensed" + L +
			"under version 3 of the GNU Affero General Public License into a single" + L +
			"combined work, and to convey the resulting work.  The terms of this" + L +
			"License will continue to apply to the part which is the covered work," + L +
			"but the special requirements of the GNU Affero General Public License," + L +
			"section 13, concerning interaction through a network will apply to the" + L +
			"combination as such." + L +
			L +
			"  14. Revised Versions of this License." + L +
			L +
			"  The Free Software Foundation may publish revised and/or new versions of" + L +
			"the GNU General Public License from time to time.  Such new versions will" + L +
			"be similar in spirit to the present version, but may differ in detail to" + L +
			"address new problems or concerns." + L +
			L +
			"  Each version is given a distinguishing version number.  If the" + L +
			"Program specifies that a certain numbered version of the GNU General" + L +
			"Public License \"or any later version\" applies to it, you have the" + L +
			"option of following the terms and conditions either of that numbered" + L +
			"version or of any later version published by the Free Software" + L +
			"Foundation.  If the Program does not specify a version number of the" + L +
			"GNU General Public License, you may choose any version ever published" + L +
			"by the Free Software Foundation." + L +
			L +
			"  If the Program specifies that a proxy can decide which future" + L +
			"versions of the GNU General Public License can be used, that proxy's" + L +
			"public statement of acceptance of a version permanently authorizes you" + L +
			"to choose that version for the Program." + L +
			L +
			"  Later license versions may give you additional or different" + L +
			"permissions.  However, no additional obligations are imposed on any" + L +
			"author or copyright holder as a result of your choosing to follow a" + L +
			"later version." + L +
			L +
			"  15. Disclaimer of Warranty." + L +
			L +
			"  THERE IS NO WARRANTY FOR THE PROGRAM, TO THE EXTENT PERMITTED BY" + L +
			"APPLICABLE LAW.  EXCEPT WHEN OTHERWISE STATED IN WRITING THE COPYRIGHT" + L +
			"HOLDERS AND/OR OTHER PARTIES PROVIDE THE PROGRAM \"AS IS\" WITHOUT WARRANTY" + L +
			"OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING, BUT NOT LIMITED TO," + L +
			"THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR" + L +
			"PURPOSE.  THE ENTIRE RISK AS TO THE QUALITY AND PERFORMANCE OF THE PROGRAM" + L +
			"IS WITH YOU.  SHOULD THE PROGRAM PROVE DEFECTIVE, YOU ASSUME THE COST OF" + L +
			"ALL NECESSARY SERVICING, REPAIR OR CORRECTION." + L +
			L +
			"  16. Limitation of Liability." + L +
			L +
			"  IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW OR AGREED TO IN WRITING" + L +
			"WILL ANY COPYRIGHT HOLDER, OR ANY OTHER PARTY WHO MODIFIES AND/OR CONVEYS" + L +
			"THE PROGRAM AS PERMITTED ABOVE, BE LIABLE TO YOU FOR DAMAGES, INCLUDING ANY" + L +
			"GENERAL, SPECIAL, INCIDENTAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE" + L +
			"USE OR INABILITY TO USE THE PROGRAM (INCLUDING BUT NOT LIMITED TO LOSS OF" + L +
			"DATA OR DATA BEING RENDERED INACCURATE OR LOSSES SUSTAINED BY YOU OR THIRD" + L +
			"PARTIES OR A FAILURE OF THE PROGRAM TO OPERATE WITH ANY OTHER PROGRAMS)," + L +
			"EVEN IF SUCH HOLDER OR OTHER PARTY HAS BEEN ADVISED OF THE POSSIBILITY OF" + L +
			"SUCH DAMAGES." + L +
			L +
			"  17. Interpretation of Sections 15 and 16." + L +
			L +
			"  If the disclaimer of warranty and limitation of liability provided" + L +
			"above cannot be given local legal effect according to their terms," + L +
			"reviewing courts shall apply local law that most closely approximates" + L +
			"an absolute waiver of all civil liability in connection with the" + L +
			"Program, unless a warranty or assumption of liability accompanies a" + L +
			"copy of the Program in return for a fee." + L +
			L +
			"                     END OF TERMS AND CONDITIONS" + L +
			L +
			"            How to Apply These Terms to Your New Programs" + L +
			L +
			"  If you develop a new program, and you want it to be of the greatest" + L +
			"possible use to the public, the best way to achieve this is to make it" + L +
			"free software which everyone can redistribute and change under these terms." + L +
			L +
			"  To do so, attach the following notices to the program.  It is safest" + L +
			"to attach them to the start of each source file to most effectively" + L +
			"state the exclusion of warranty; and each file should have at least" + L +
			"the \"copyright\" line and a pointer to where the full notice is found." + L +
			L +
			"    <one line to give the program's name and a brief idea of what it does.>" + L +
			"    Copyright (C) <year>  <name of author>" + L +
			L +
			"    This program is free software: you can redistribute it and/or modify" + L +
			"    it under the terms of the GNU General Public License as published by" + L +
			"    the Free Software Foundation, either version 3 of the License, or" + L +
			"    (at your option) any later version." + L +
			L +
			"    This program is distributed in the hope that it will be useful," + L +
			"    but WITHOUT ANY WARRANTY; without even the implied warranty of" + L +
			"    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the" + L +
			"    GNU General Public License for more details." + L +
			L +
			"    You should have received a copy of the GNU General Public License" + L +
			"    along with this program.  If not, see <http://www.gnu.org/licenses/>." + L +
			L +
			"Also add information on how to contact you by electronic and paper mail." + L +
			L +
			"  If the program does terminal interaction, make it output a short" + L +
			"notice like this when it starts in an interactive mode:" + L +
			L +
			"    <program>  Copyright (C) <year>  <name of author>" + L +
			"    This program comes with ABSOLUTELY NO WARRANTY; for details type `show w'." + L +
			"    This is free software, and you are welcome to redistribute it" + L +
			"    under certain conditions; type `show c' for details." + L +
			L +
			"The hypothetical commands `show w' and `show c' should show the appropriate" + L +
			"parts of the General Public License.  Of course, your program's commands" + L +
			"might be different; for a GUI interface, you would use an \"about box\"." + L +
			L +
			"  You should also get your employer (if you work as a programmer) or school," + L +
			"if any, to sign a \"copyright disclaimer\" for the program, if necessary." + L +
			"For more information on this, and how to apply and follow the GNU GPL, see" + L +
			"<http://www.gnu.org/licenses/>." + L +
			L +
			"  The GNU General Public License does not permit incorporating your program" + L +
			"into proprietary programs.  If your program is a subroutine library, you" + L +
			"may consider it more useful to permit linking proprietary applications with" + L +
			"the library.  If this is what you want to do, use the GNU Lesser General" + L +
			"Public License instead of this License.  But first, please read" + L +
			"<http://www.gnu.org/philosophy/why-not-lgpl.html>." + L +
			L;
	
	public static String getlicenseFAQ() {
    	return licenseFAQ;
    }

//	public static String getEN_BlanklicenseNotice() {
//		return  blankLicenseNotice  + L + L + 
//				CMUlicenseNotice;
//    }
	
	public static String getBlanklicenseNotice() {
		return blankLicenseNotice;
    }
	
	public static String getGPLLicense() {
    	return gplLicense;
    }
	
	public static String getLicense() {
		return  licenseFAQ + 
		"E. GPL License Notice which will be included with your submission:" + L+ L + 
		"Copyright (C) <year>  Free Software Foundation" + L +
		 L +
		 blankLicenseNotice +
		"F. Full GPL License: (will also be included with your submission)" + L + L + 
		gplLicense;
    }
	
//	public static String getEN_VFLicense() {
//		return VFabout + L +   
//			CMUGPLpreamble + L + 
//			GPLshort + L + 					
//			CMUlicenseNotice + L + 							
//			Acknowledgments + L + L	+				
//			gplLicense;
//   }
	
	public static String getVFLicense() {
		return VFabout + L +   
//			VFGPLpreamble + L + 
			GPLshort + L + 					
			Acknowledgments + L + L	+				
			gplLicense;		
    }
}