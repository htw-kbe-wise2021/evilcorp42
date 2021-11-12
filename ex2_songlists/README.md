Um diese Application starten zu können müssen Sie in der Datei (application.properties) im Verzeichnis (src/main/resources/application.properties)
die Variabeln auf dem eigenen System hinterlegen.

Für Windows:

Diese Benutzervariabeln müssen in die Windowsumgebung eingebunden werden.
Hierfür musss man die "Umgebungsvariabeln" von Windows öffnen.
Bei Benutzervariabeln muss ein neuer Eintrag hinzugefügt werden für die Benutzer-, Passwort- und Weblinkdaten hinzugefügt werden.
Dafür werden folgende Benutzervariabeln benötigt:
kbe_herokuDB_Uebung_Path
kbe_herokuDB_Uebung_Benutzer
kbe_herokuDB_Uebung_PW
Hier wird der jeweilige Wert mit Übergeben um ein Zugriff auf die eigene Datenbank zu ermöglichen.