# HackHunt
Server and client for the last iteration of the Netbyte HackHunt game. HackHunt has been a beginner-friendly CTF for the past 10 years, and has maintained the same format more or less the whole time. The general concept is that each player maintains a number of servies, and need to protect (patch) these while attacking (exploit) the servies of other players. Exploist and patches are made by solving challenges (advisories).

For a brief intro to how to play the game, see http://hackhunt.net/ for as long as it's available (who knows when I'll retire it).

This contains the source code for both client and server, along with a bunch of the challenge descriptions in the database. Some of these are original, some are blatant rip-offs from excisting CTF's (added by others than me, I swear...) and for some the files will still be available while they won't be for others. So use it as an example of what the content needs to look like, rather than as ready-made challenges/exercises.

This repo also has a all-in-one package, RC 1.3, extract it, read the README's and you'll be ready to go in 5.


None of this code is particularly safe or efficient, and was only ever designed to run in semi-trusted environments and for very short periods of time. If you need a fun project to tear appart to find security issues, this might be a good candidate :-) It also violates a number of security best practices (most of them are intentional), so feel free to fix/improve these before using it.

Feel free to use this project for anything you want, but if you plan to make money off of it, do get in touch with me to discuss fair compensation (and remember that most of the challenges are "borrowed" from various places, so you'll most likely be violating their terms of service).
