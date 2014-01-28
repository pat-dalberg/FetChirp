#### FetChirp

FetChirp is an application for Google Glass that gets recent public tweets near the users 
current location. The name is a portmanteaux of 'fetch' and 'chirp'.

#### Usage

Either sideload the prebuilt apk [available here] (https://drive.google.com/file/d/0B59-w0gvh0SeUml6OWt3N3M2a00/edit?usp=sharing), or build it from this repository and 
install it as usual. Once the app is installed it is available in the voice menu as 
'get nearby tweets'. The app goes to a settings screen. Tap once to use the default 
parameters (5 mile radius, 30 tweets returned), or tap with two fingers to go into the 
settings menu. In the settings menu select either 'change radius' or 'change count', and 
then swipe on the touchpad to adjust. Tap to set the parameter, and tap again to begin 
searching. As you are scrolling through tweets you can tap on an individual tweet to bring 
up menu to bring up the authors twitter profile, profiles of other twitter users mentioned
in the tweet, search on hashtatgs in the tweet, and browse to URLs mentioned in the tweet.
Swipe down to exit the app.


#### Building

To build Fetchirp yourself, clone this repo into your workspace,

git clone git://github.com/pat-dalberg/Fetchirp.git

sign up and go to https://dev.twitter.com/apps , create a new application, and copy the 
Consumer key and Consumer secret into the variables with the same name in AppConstants.

Obtain the following libraries and add them to the libs folder in your project:

* [Ion] (http://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=com.koushikdutta.async&a=androidasync&v=LATEST)

* [AndroidAsync] (http://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=com.koushikdutta.ion&a=ion&v=LATEST)

* [GSON] (https://code.google.com/p/google-gson/downloads/list)    

