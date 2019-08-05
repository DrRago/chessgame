# Chess Game
## System Requirements
- any OS you can install maven on
- maven 3.x

## Execute the project
- ``mvn install``
- ``mvn tomcat7:run``

**Note:** There will be displayed some warnings, because a certificate could not be found. Tomcat runs anyway and listens with this configuration on port 8002.

The websocket recognizes by itself whether it should use ``wss:\\`` or ``ws:\\``.