# Java
Short game I had to make in VU university Java module. Used Java 14 principles like OOP, class abstraction, information hiding(encapsulation), inheritance, static methods and java.awt library strengths(even though it is not designed for game creation).

I used eclipse as IDE.

Controls:<br />
Left, right, up arrows controls player<br />
Space eats other ghosts<br />
Press letter p to open console in order to move using pseudoCode.(there is many bugs, which I will fix in my free time)<br />

PseudoCommands: <br /> <br />
!!!IMPORTATNT after each command you must put value and after value put one space and enter to write a command.<br />

You can find commands in funcion updatePlayerPseudo() in player.java file.(I know bad funcion, should have made it a lot shorter and divided in smaller funcions. First I had to make just game, and after 3 months next task was to make player move with pseudocode. If I knew from the start would have coded different, but didnt have time to redo half of the program)<br />

Example command to clear first level:<br />
teleport 128 3776 <br />
speed 10 <br />
unkillable 10000 <br />
spaceTick 10000 <br />
right 192 <br />
jump 0 <br />
right 400 <br />
left 128 <br />
jumpVEL 15 <br />
jump 0 <br />
left 600 <br />
jumpVEL 10 <br />
right 128 <br />
jump 0 <br />
right 800 <br />
if (collRIGHT)left(60) <br />
jumpVEL 25 <br />
jump 0 <br />
pause 50 <br />
right 128 <br />
pause 50 <br />
speed 1 <br />
jump 0 <br />
left 256 <br />
jump 0 <br />
speed 5 <br />
pause 50 <br />
right 600 <br />
if (collRIGHT)left(64)<br />
jump 0 <br />
pause 25 <br />
left 128 <br />
jumpVEL 20 <br />
gravity 0.001 <br />
speed 1 <br />
jump 0 <br />
pause 100 <br />
left 64 <br />
jumpVEL 12 <br />
jump 0 <br />
right 128 <br />
gravity 0.2 <br />
jump 0 <br />
pause 25 <br />
right 64 <br />
speed 5 <br />
jump 0 <br />
pause 25 <br />
left 192 <br />
speed 3 <br />
for 25 <br />
killEnemy 0<br />
jump 0 <br />
left 360<br />

