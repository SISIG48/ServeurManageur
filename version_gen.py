import time
import random

car = ["a","b","c","d","e","f","g","h","i","j","k","l","m","o","p","q","r","s","t","u","v","x","y","z","1","2","3","4","5","6","7","8","9","&","é","\"","\\","²"]
gen = input("version>").replace(" ", "_") + "?key:"
i = 0
max = random.random() * 95
while i < max:
    gen = gen + car[int(random.random() * 38)]
    i = i + 1
print(gen)
fichier = open("version.dll", "w")
fichier.write(gen)
fichier.close