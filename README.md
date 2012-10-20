EECE419Group7
=============

EECE 419 Group 7 Software Project

GIT Repo Setup

1) The first thing you will want to do is download a git client. I am on a windows machine and prefer to use
the cygwin based git bash which can be downloaded here: http://code.google.com/p/msysgit/downloads/list?q=full+installer+official+git

2) Next you will want to clone your repo. This is done by navigating to any directory on your machine (there is a file length
limitation in Windows so I would suggest C: in case our paths grow. Linux and MAC users don't need to worry). Once in 
the directory, execute the following : git clone https://github.com/hamo26/EECE419Group7.git. 

3) Once you have cloned your directory, you chould see the EECE419Group7 folder which will now serve as your repo endpoint.
You will notice a .gitignore file which is analogous to an svn ignore file.
UPDATE: I have updated the .ignore file with files and directories that should not be included for staging.

4) You will mainly be pushing and pulling from the repo. if you execute: git gui, you should see an interface which 
staging your commits and pushing to the repo alot easier than the command line. For more information on rebases, pushes and pull
take a look at this cheatsheet: https://git.wiki.kernel.org/images-git/7/78/Git-svn-cheatsheet.pdf


Setting up Eclipse
==================

1) Download eclipse from: http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/junosr1. 
Please use the specified release.

2) You will need the JRE for the Eclipse (and other things) to run.

3) Run eclipse and go to Window->Install New Software

4) Enter in m2e releases repository - http://download.eclipse.org/technology/m2e/releases in the "Work with:" section.

5) Check the "Maven Integration for Eclipse" and install.

6) After installing, if the you are prompted to restart eclipse, please do so. I have run into problems in the past when I don't.

7) Repeat Step 3 to 6 for the following update sites and plugins:

(JAUTODOC) update site: http://jautodoc.sourceforge.net/update/ - http://jautodoc.sourceforge.net/update/ 

Jautodoc will help automatically document methods. Go to the site for usage details.

(MAVEN ANDROID CONNECTOR) update site: http://rgladwell.github.com/m2e-android/updates/

Should we decide to mavenize our android project. This will be needed.

(MORE UNIT) http://moreunit.sourceforge.net/update-site/

This is handy for creating our junit tests really quickly. CTRL-J on any class for example, will take you to the unit test
 associated with that class or prompt you to create one. There are other neat features in this plugin as well.
 
INSTALLING MAVEN
================


