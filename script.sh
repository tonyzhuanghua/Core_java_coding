#!/bin/sh
# $Id$
#
# This script can be used to start WebLogic Server. This script ensures
# that the server is started using the config.xml file found in this
# directory and that the CLASSPATH is set correctly. This script contains
# the following variables:
#
# WL_HOME        - The root directory of your WebLogic Server
#                  installation
# JAVA_HOME      - Determines the version of Java used to start
#                  WebLogic Server. This variable must point to the
#                  root directory of a JDK installation and will be set
#                  for you by the WebLogic Server installer.
#                  See the WebLogic platform support
#                  page (http://e-docs.bea.com/wls/platforms/index.html)
#                  for an up-to-date list of supported JVMs on your platform.
# JAVA_OPTIONS   - Java command-line options for running the server.
#
# jDriver for Oracle users: This script assumes that native libraries required
# for jDriver for Oracle have been installed in the proper location and that
# your os specific library path variable (i.e. LD_LIBRARY_PATH/solaris,
# SHLIB_PATH/hpux, etc...) has been set appropriately.  Also note that this
# script defaults to the oci816_8 version of the shared libraries. If this is
# not the version you need, please adjust the library path variable
# accordingly.

# For additional information, refer to Installing and Setting up WebLogic
# Server (http://e-docs.bea.com/wls/docs61/install/index.html).

# Set user-defined variables.
JAVA_HOME=/opt/bea/jdk131
WL_HOME=/opt/bea/wlserver6.1
JAVA_OPTIONS="-ms64m -mx256m"

JAR_STORE=/home/BSS1/uif/jar
UIF_HOME=/home/BSS1/uif
JAR_LIB=$JAR_STORE/log4j.jar
JAR_LIB=$JAR_LIB:$JAR_STORE/tibrvj.jar
JAR_LIB=$JAR_LIB:$JAR_STORE/Maverick4.jar
JAR_LIB=$JAR_LIB:$JAR_STORE/TIBRepoClient4.jar
JAR_LIB=$JAR_LIB:$JAR_STORE/dom4j-full-1.1.jar
JAR_LIB=$JAR_LIB:$UIF_HOME/cc/cfg

CLASSPATH=$WL_HOME:$WL_HOME/lib/weblogic_sp.jar:$WL_HOME/lib/weblogic.jar:$JAR_LIB


PATH=$WL_HOME/bin:$JAVA_HOME/jre/bin:$JAVA_HOME/bin:$PATH
echo
echo "***************************************************"
echo "*  To start WebLogic Server, use the password     *"
echo "*  assigned to the system user.  The system       *"
echo "*  username and password must also be used to     *"
echo "*  access the WebLogic Server console from a web  *"
echo "*  browser.                                       *"
echo "***************************************************"

# Set WLS_PW equal to your system password for no password prompt server startup.
WLS_PW=weblogic3

# Set Production Mode.  When set to true, the server starts up in production mode.  When
# set to false, the server starts up in development mode.  The default is false.
#STARTMODE=true
STARTMODE=false

java $JAVA_OPTIONS -classpath $CLASSPATH -Dweblogic.Domain=callcentre01 -Dweblogic.Name=weblogicDaemon -Dbea.home=/opt/bea -Dweblogic.management.password=$WLS_PW -Dweblogic.ProductionModeEnabled=$STARTMODE -Djava.security.policy==/opt/bea/wlserver6.1/lib/weblogic.policy weblogic.Server

