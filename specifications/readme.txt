Info k překladům v devices.xml:

* Řetězce začínající s "T:" značí, že následuje identifikátor pro překlad. Ten ukazuje do souboru s překladem na položku <string name="...">. Speciální případ je u elementu <values name="...">, kdy name identifikuje v překladovém souboru element <values name="..."> a až v něm jsou konkrétní překlady pro jeho hodnoty.


Další info:

* Speciální element <values> může mít kromě atributu "name" i volitelný atribut "type", který může nabývat hodnot "enum" (výchozí) a "switch". Volba "switch" říká, že se tento modul v GUI zobrazí jako "přepínač". Logicky se dá použít pouze v případech, kdy jsou dostupné pouze 2 hodnoty, kterých může modul nabývat. Pokud bude takto označen modul jiného typu než enum, nebo pokud bude moci nabývat více hodnot, jeho chování bude nedefinováno (pravděpodobně bude ignorován). Dále platí, že první uvedená hodnota bude v GUI zobrazena jako přepínač ve "vypnutém" stavu a druhý uvedená hodnota jako v "zapnutém" stavu. Např.:

	<values name="light" type="switch">
		<value id="0">T:OFF</value>
		<value id="1">T:ON</value>
	</values>
