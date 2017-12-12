#! /bin/bash
VERSION="0.1"
sudo curl -L -o /usr/local/bin/leffe.jar https://github.com/Max-Meldrum/leffe/releases/download/v$VERSION/leffe-$VERSION.jar
echo "alias leffe='java -jar /usr/local/bin/leffe.jar'" >> $HOME/.bashrc
echo "Installation done, to set stocks, see $HOME/.leffe/stocks"
bash
