all : dist/josm

dist/josm-custom.jar :
	ant

#
dist/josm :dist/josm-custom.jar
	gcj-4.4 -O4 -o dist/josm --main=org.openstreetmap.josm.gui.MainApplication dist/josm-custom.jar


#gcj-4.4 --main=org.openstreetmap.josm.gui.MainApplication dist/josm-custom.jar 