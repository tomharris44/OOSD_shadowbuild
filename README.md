# OOSD_shadowbuild
Simple java game built using the Slick2D library.

User controls units and buildings to most efficiently mine the "unobtainium" found on a map. Various strategies can be used to most effectively achieve the objective.

## Controls
W,A,S,D - move the gameview around the map.

Left Click - select a unit/building by pressing on or near them. Can also be used to deselect a unit/building by clicking elsewhere on the map.

Right Click - move a selected unit to the specified location on the map.

{1,2,3} keys - depending on the functionality of a unit/building, train or build a unit/building in the current location.

## Unit details
Scout - used to explore the map. Move fastest of the units.

Engineer - used to mine the resources on the map. Mine a resource by right clicking on the resource while the engineer is selected. The engineer will return to the closest Command Centre to drop off the resources.

Builder - able to construct factories. Create a factory by pressing the '1' key while a Builder is selected.

Truck - able to create a Command Centre, while being destroyed in the process. Create a Command Centre by pressing the '1' key while a Truck is selected.

## Building details
Command Centre - able to spawn Scouts, Builders and Engineers. Create these units by pressing '1', '2' or '3' key respectively while a Command Centre is selected. Cost to create a Scout is 5 Metal, to create a Builder is 10 Metal and to create a Engineer is 20 Metal.

Factory - able to spawn trucks. Create a truck by pressing the '1' key while a Factory is selected. Cost to create a Truck is 100 Metal.

Pylon - can be activated when a unit comes close to it. Increase Engineer carry capacity by 1 when activated.

## Resource details
Metal - resource used to construct and train buildings/units.

Unobtainium - valued resource in the game. Main objective is to "obtain" this.
