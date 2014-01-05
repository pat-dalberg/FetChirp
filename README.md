#### FetChirp

FetChirp is an application for Google Glass that gets recent public tweets near the users 
current location. The name is a portmanteaux of 'fetch' and 'chirp'.

#### Usage

Either sideload the prebuilt apk [available here] (https://drive.google.com/file/d/0B59-w0gvh0SeUml6OWt3N3M2a00/edit?usp=sharing), or build it from this repository and 
install it as usual. Once the app is installed it is available in the voice menu as 
'get nearby tweets'. While the location is being retrieved you may interrupt the retrieval
by tapping on the touchpad and then adjust the radius from which tweets will be retrieved
by swiping forward and back. Tap again when the desired radius is selected to resume
retrieval with you updated radius. When your location is obtained the progress spinner 
will be replaced by a set of cards containing recent nearby tweets. Slide to view the 
cards, and swipe down to exit the app.

#### Building

To build Fetchirp yourself, clone this repo into your workspace,

git clone git://github.com/pat-dalberg/Fetchirp.git

sign up and go to https://dev.twitter.com/apps , create a new application, and copy the 
Consumer key and Consumer secret into the variables with the same name in AppConstants.

Obtain the following libraries and add them to the libs folder in your project:

* [Ion] (http://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=com.koushikdutta.async&a=androidasync&v=LATEST)

* [AndroidAsync] (http://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=com.koushikdutta.ion&a=ion&v=LATEST)

* [GSON] (https://code.google.com/p/google-gson/downloads/list)    

