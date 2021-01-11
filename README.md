# Java
Short game I had to make in VU university Java module. Used Java 14 principles like OOP, class abstraction, information hiding(encapsulation), inheritance, static methods and java.awt library strengths(even though it is not designed for game creation).

I used eclipse IDE

Controls:
Left, right, up arrows controls player
Space eats other ghosts
Press letter p to open console in order to move using pseudoCode.(there is many bugs, which I will fix in my free time)

PseudoCommands:
!!!IMPORTATNT after each command you must put value and after value put one space and enter to write a command.

You can find commands in funcion updatePlayerPseudo() in player.java file.(I know bad funcion, should have made it a lot shorter and divided in smaller funcions. First I had to make just game, and after 3 months next task was to make player move with pseudocode. If I knew from the start would have coded different, but didnt have time to redo half of the program)

Example command to clear first level:
teleport 128 3776 
speed 10 
unkillable 10000 
spaceTick 10000 
right 192 
jump 0 
right 400 
left 128 
jumpVEL 15 
jump 0 
left 600 
jumpVEL 10 
right 128 
jump 0 
right 800 
if (collRIGHT)left(60) 
jumpVEL 25 
jump 0 
pause 50 
right 128 
pause 50 
speed 1 
jump 0 
left 256 
jump 0 
speed 5 
pause 50 
right 600 
if (collRIGHT)left(64)
jump 0 
pause 25 
left 128 
jumpVEL 20 
gravity 0.001 
speed 1 
jump 0 
pause 100 
left 64 
jumpVEL 12 
jump 0 
right 128 
gravity 0.2 
jump 0 
pause 25 
right 64 
speed 5 
jump 0 
pause 25 
left 192 
speed 3 
for 25 
killEnemy 0
jump 0 
left 360

