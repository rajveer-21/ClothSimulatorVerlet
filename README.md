# ClothSimulatorVerlet
A cloth simulator using verlet integration and with the usage of wind and gravity!

# A GUI based cloth sim using verlet integration
Uses Java's inbuilt Swing and AWT packages for rendering lines and points to form a mesh which is used to denote a piece of cloth. I've integrated the usage of gravity and wind(0.5f units per iteration each with wind being a randomized variable) to simulate realistic movement of the cloth. 

**Constraint Class ->**
 Defines spring variables between points on the mesh which are responsible for causing streching and contraction of the points when they are not pinned. It produces a percentage for multiplication with the offset of the x and y axis of the moved particles. Allows the particles affected by the movement to be equally moved and changed without bias.

**Particle Class ->**
 Defines individual particles on the mesh which have x and y coordinates which are respectively affected by wind, gravity and movement due to mouse events.

**Mouse Based Events ->**
 Main driver behind various changes on the mesh, press and drag on unpinned particles to watch them bounce around due to their constraints and pinned particles to change the underlying structure of the entire mesh.

Have fun simulating! Regards.
