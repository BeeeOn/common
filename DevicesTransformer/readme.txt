Kompilace
*********
Pro kompilaci a/nebo vygenerování spustitelného souboru *.jar je nutné použít IntelliJ IDEA a v něm otevřít DevicesTransformer.iml projekt.
Stažení: https://www.jetbrains.com/idea/download/ (vybrat Community Edition)

Alternativně stačí stáhnout DevicesTransformer.jar z Redmine: https://ant-2.fit.vutbr.cz/projects/iot/files

Spuštění
********
Pro spuštění *.jar souboru slouží příkaz:

java -jar DevicesTransformer.jar

Program očekává, že existuje složka (relativně vůči jeho pracovnímu adresáři, tedy odkud je spuštěn) "../specifications/" se soubory devices.xml a dalšími.
DevicesTransformer.jar by tedy měl být umístěn např. jako /DevicesTransformer/DevicesTransformer.jar, a spouštěn ze složky /DevicesTransformer/ výše uvedeným příkazem.

Výstup
******
Program vygeneruje svůj výstup do složky /export/ (relativně vůči jeho pracovnímu adresáři, tedy odkud je spuštěn).

Během svého běhu vypisuje informace na stdout a případné chyby na stderr.