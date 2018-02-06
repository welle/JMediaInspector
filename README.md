# JMediaInspector [![Build Status](https://travis-ci.org/welle/JMediaInspector.svg?branch=master)](https://travis-ci.org/welle/JMediaInspector) [![Quality Gate](https://sonarcloud.io/api/badges/gate?key=aka.jmediainspector:JMediaInspector)](https://sonarcloud.io/dashboard/index/aka.jmediainspector:JMediaInspector) #

JMediaInspector is a application to manage your movies and series TV library.

Plex part:
- Search for linked files


TODO:

Plex part:
- Search unlinked files
- Search wrong links
- Export to HTML structures

Media part:
- Search files with filters (audio/video codecs, bitrate, resolution; multi; etc)
- Search for missing files in collections
- Statistiques (audio/video)
- Export searching for couchpotato/trakt.tv/sonaar/radarr
- Search for duplicates files

### Version

Go to [my maven repository](https://github.com/welle/maven-repository) to get the latest version.

## Notes
Need the eclipse-external-annotations-m2e-plugin: 

p2 update site to install this from: http://www.lastnpe.org/eclipse-external-annotations-m2e-plugin-p2-site/ (The 404 is normal, just because there is no index.html; it will work in Eclipse.)