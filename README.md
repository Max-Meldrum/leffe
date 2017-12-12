# Leffe

When I am hakking away on my laptop, I don't wanna spend time on either logging into my Nordnet account or opening up several tabs to check how my stocks are going. 
With Leffe, I can easily access this from the terminal. 

While coding this, I was enjoying a beer of Leffe, hence the name.

Installing
```
$ ./install.sh

```

Example config
```
# Custom name, link to stock on Avanza
THQ Nordic, https://www.avanza.se/aktier/om-aktien.html/707695/thq-nordic-b
Salt X, https://www.avanza.se/aktier/om-aktien.html/556359/saltx-technology-holding-b
Eyeonid, https://www.avanza.se/aktier/om-aktien.html/692793/eyeonid-group
Scout Gaming Group, https://www.avanza.se/aktier/om-aktien.html/808453/scout-gaming-group
HiQ, https://www.avanza.se/aktier/om-aktien.html/5433/hiq-international
Bure Equity, https://www.avanza.se/aktier/om-aktien.html/5277/bure-equity
```

Leffe scrapes data from Avanza, so Avanza links is a requirement.


Running
```
# Just leffe will run with the config file found at $HOME/.leffe/stocks
$ leffe
# Or you can specify a file
$ leffe <file>

```

![alt text](https://github.com/Max-Meldrum/leffe/blob/master/img/run.png?raw=true "Leffe")





