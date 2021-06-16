-------------------------------------------
Minecraft Mod: Better Directions (IPASS opdracht)
-------------------------------------------
Voor mijn IPASS opdracht heb ik een minecraft mod geschreven die gebruik maakt
van het A* Pathfinding algoritme. Deze mod laat je waypoints zetten op meerder plekken,
waarna je daar een weg naartoe kunt vragen waar je ook bent.


Hoe werkt deze mod?
==============================

Deze mod werkt heel simpel, je hoeft simpelweg op 'B' te drukken om een waypoint te zetten.
Dan druk je op 'V' om een lijst van je waypoint te kunnen zien, als je er dan op 1 drukt,
dan zal deze mod de snelste weg voor je zoeken waarbij hij ook rekening houdt met eventuele obstakels,
zoals een ravijn of een hoge berg. Deze weg zal hij aan je laten zien d.m.v pijlen op de grond.

Als je je weg hebt gevonden en je wilt de pijlen niet meer zien, dan druk je simpelweg op 'N',
en dan verdwijnen de pijlen weer.

KEYBINDINGS:
    V = see Nodes & Path
    B = Open Screen to set a waypoint
    N = Open Screen to see and/or delete waypoints, you can also press a button to start the pathfinding to that waypoint
    M = (W.I.P.) Toggle path

Algoritme:
=========================

Deze mod maakt gebruik van het A* algoritme om de snelste weg te vinden, alleen daarvoor
heeft hij wel 'nodes' nodig tussen de speler en de waypoint om die te kunnen vinden. De mod genereert deze nodes
in een chunk zodra deze voor de eerste keer geladen wordt, en slaat ze dan op in een Capability in de chunk zelf.
(Een Capability is voor Minecraft een manier om data op te slaan voor specifieke objecten, en Chunks zijn daar 1 van)

(Een chunk is 16x16)
Hoeveel 'nodes' er gegenereert worden hangt af van de gebruiker, standaard staat dit op 16 'nodes' per chunk.
Er kan/kunnen minimaal 16 en maximaal 256 (16x16) 'nodes' worden gegenereert per chunk. Hoe meer 'nodes' er
worden gegenereert hoe beter (maar ook langzamer) het algoritme zal werken.

Code:
=========================

De code staat in 'src/main/java/com/spacialnightmare/betterdirections' (verdeeld over meerdere packages)
In 'src/main/resources/assets' staan eventuele files die nodig zijn voor de mod.
In 'src/main/java/com/spacialnightmare/betterdirections/pathfinding' staat het A* algoritme uitgewerkt.

Bronnen:
=======================
https://mcforge.readthedocs.io/en/latest/
        - Officiele documentatie voor MinecraftForge

https://forge.gemwire.uk/wiki/Main_Page
        - Onofficiele documentatie voor MinecraftForge

https://www.udemy.com/course/make-a-minecraft-mod-minecraft-modding-for-beginners-116/
        - Had ik lang geleden al een keer gebruikt, maar hier legt hij de basis heel goed uit voor MinecraftForge

https://www.youtube.com/watch?v=-L-WgKMFuhE&ab_channel=SebastianLague
        - Legt heel goed het A* Algoritme uit
