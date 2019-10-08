BOB
====

#### Summary
Jam game made in 72 hours, made with [LibGDX](https://github.com/libgdx/libgdx) and [Artemis-ODB](https://github.com/junkdog/artemis-odb). Tested for desktop and html5 via gwt.

This particular game uses odb 2.1 fluid entities!

![](https://static.jam.vg/raw/64d/z/283b9.png)

#### Note about the web build.
Didn't get the web version done before submission hour so had to rig a few things at the last moment, specifically how the shader operates is a bit rushed. 

#### Grab what you need!
Artemis Entity System takes a way a lot of the lifecycle management, and composition is a natural match for a time constrained Jam. While made in a rush, I hope this code gives you an idea or two how to benefit from using an entity system in your project.

#### Usage details
While the meat is with peeking at the systems and components, you might like to compile the game!
The project was created on top of [libgdx-artemis-quickstart](https://github.com/DaanVanYperen/libgdx-artemis-quickstart), which uses gradle for build automation, and snapshot versions of artemis and libgdx. ```gradlew desktop:run``` should run the game. See the [libgdx wiki](https://github.com/libgdx/libgdx/wiki) for how to use gradle with your ide.

#### License summary
Code, graphics and sound are under a separate license. Feel free to do whatever with the code, the graphics however are copyrighted.