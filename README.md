@gracehu532

Introduction
-------------

NutriGlass is an application for the Google Glass that provides an augmented reality approach to nutrition tracking. Developed by Grace Hu, Abby Olivier, and Lily Xie for CS320 Tangible User Interfaces at Wellesley College, fall 2014.

Implementation
-------------

NutriGlass uses the Metaio SDK to perform 2D image recognition of food and the Glass Development Kit to track and display information about the user's health and nutrition. Metaio uses Glass' camera view to recognize pre-loaded images using its own computer vision algorithm. The recognized image cues the display of a card containing the user's nutritional information, which keeps track of what percentage of each food group the user has consumed that day as per the USDA's health guidelines.

Related Resources
-------------

The Metaio SDK is a platform independent augmented reality solution with a tracking engine and state-of-the-art rendering.

https://dev.metaio.com/sdk/

The Glass Development Kit (GDK) is an add-on to the Android SDK that lets you build Glassware that runs directly on Glass. We used GDK 4.2.2, the version compatible with Metaio.

https://developers.google.com/glass/develop/gdk/
