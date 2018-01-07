# RemoteYoutubePlay
Launch Youtube streams from phone to your windows PC

# Features
+ Opens a hardcoded music stream in your default browser
+ Can set volume of your PC

# Planned stuff
+ Ability to put multiple streams in a list and chose wich one to launch
+ Configuring PC address

# How to setup
Launch RemoteListener.py from PyhtonServer folder.
 App should work, but address of pc is hardcoded right now to 192.168.0.11


# How it works
1. App sends a HTTP request to open a webbrowser with specified url
2. Python HTTP server receives requests and opens the browser
3. Volume is also set via HTTP requests and uses pycaw library.


