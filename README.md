
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

DEV ENVIRONMENT
===============

Tools needed:

Setting up Eclipse
==================

1) Download eclipse from: http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/junosr1. 
Please use the specified release.

2) You will need the JRE for the Eclipse (and other things) to run.

3) Run eclipse and go to Window->Install New Software
