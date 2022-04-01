# Raytracer Refactor Project - Group 17
## Project goals

The purpose of this repository is to modernise the existing Raytracer repo created by Idris such that it follows modern
software development conventions and best practices.  

There are a few key ways this will be done.
1. Utilise Maven to manage project dependencies, validation, **testing**, and deployment.
2. Update the source code to take advantage of new Java features up to Java 17.
3. Add javadoc comments where appropriate for accessibility
4. Creation of a web app/gui/api for other programmers to interface with the application

Throughout this process notes will be taken of key hurdles that had to be overcome in order to write a 1000 word report
on the refactoring process.

Task list spreadsheet can be found [here](https://docs.google.com/spreadsheets/d/1ZkH4KjB4Q07HFu4nux2-Y60r-MSp3mcFQKpKC1xbres/edit#gid=0).

## Project state
The project is now done. There are three ways of utilising raytracer.  
1. The original scene description files, reformatted to be more user-friendly (see Scene(File file) constructor javadoc)
2. Taking advantage of our programmatic API in another application
3. Using our GUI to create and render a scene (Run Main.java with no command line arguments)

## demo and misc folders
### demo
Contains two executable java classes which make use of our RayTracer and Scene API methods to render a ray traced scene.  
- Demo 1 is aesthetically pleasing making heavy use of Spheres.  
- Demo 2 showcases the Disc, Triangle and Cylinder classes.

### misc
Contains the original scene description files from when we first downloaded the repository plus three new scene description
files created by our group.  
All scene description files have been reformatted to a more user-friendly format.  

Also contains some texture files for some of the pigment classes.

## API
Our API is built across two classes RayTracer and Scene. It is possible to fully define a scene just using the Scene
class thanks to its static methods.  
To see all the available methods you'll have to open the Scene.java source code, but a brief overview is:  
- Set a new Camera
- Set an AmbientLight
- Add a Shape, or create and predefined shapes
- Add a Light, or define a new light
- Create existing Pigments and Finishes
- Reset all the above to their default states

RayTracer then takes a Scene as argument and draws the ray traced Scene to a file. See the Demo classes in demo for an
example of this.