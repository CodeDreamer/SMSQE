; SBAS_LANG  Language Dependent Modules  V2.02	 1994	Tony Tebby
;
; 2005-04-12  2.01  Some german text corrected (wl)
; 2020-04-13  2.02  Added Spanish messages (MK)

	xref	smsq_end

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_ldm'
	include 'dev8_mac_text'

	section header

header_base
	dc.l	msg_base-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-msg_base	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	0			 ; main level
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	16,'SBASIC Messages '
	dc.l	'    '
	dc.w	$200a

	section base
msg_base
	lea	sbas_err4,a1		 ; link in group 4 error messages
	moveq	#sms.lldm,d0
	trap	#do.sms2
	rts

sbas_err4
	dc.w	ldm.msgt,4,44	 ; English
	dc.w	6
	dc.l	msg_eng-*

	dc.w	ldm.msgt,4,49	 ; German
	dc.w	6
	dc.l	msg_deu-*

	dc.w	ldm.msgt,4,33	 ; French
	dc.w	6
	dc.l	msg_fra-*

	dc.w	ldm.msgt,4,39	 ; Italian
	dc.w	6
	dc.l	msg_ita-*

	dc.w	ldm.msgt,4,34	 ; Spanish
	dc.w	0
	dc.l	msg_esp-*

; message  tables

msg_eng dc.w	44
	dc.w	eng_iexp-msg_eng
	dc.w	eng_lpar-msg_eng
	dc.w	eng_rpar-msg_eng
	dc.w	eng_lno-msg_eng
	dc.w	eng_bstr-msg_eng
	dc.w	eng_edef-msg_eng
	dc.w	eng_idef-msg_eng
	dc.w	eng_bdef-msg_eng
	dc.w	eng_bedf-msg_eng
	dc.w	eng_bloc-msg_eng
	dc.w	eng_bret-msg_eng
	dc.w	eng_rcwh-msg_eng
	dc.w	eng_endw-msg_eng
	dc.w	eng_else-msg_eng
	dc.w	eng_endi-msg_eng
	dc.w	eng_xstr-msg_eng
	dc.w	eng_inif-msg_eng
	dc.w	eng_insl-msg_eng
	dc.w	eng_indf-msg_eng
	dc.w	eng_inwh-msg_eng
	dc.w	eng_bfor-msg_eng
	dc.w	eng_unlp-msg_eng
	dc.w	eng_blvr-msg_eng
	dc.w	eng_bsel-msg_eng
	dc.w	eng_ends-msg_eng
	dc.w	eng_dtcl-msg_eng
	dc.w	eng_brds-msg_eng
	dc.w	eng_recr-msg_eng
	dc.w	eng_eod-msg_eng
	dc.w	eng_bprc-msg_eng
	dc.w	eng_bref-msg_eng
	dc.w	eng_bdim-msg_eng
	dc.w	eng_pdim-msg_eng
	dc.w	eng_dmng-msg_eng
	dc.w	eng_dmov-msg_eng
	dc.w	eng_eind-msg_eng
	dc.w	eng_xind-msg_eng
	dc.w	eng_asar-msg_eng
	dc.w	eng_iind-msg_eng
	dc.w	eng_inor-msg_eng
	dc.w	eng_narr-msg_eng
	dc.w	eng_basg-msg_eng
	dc.w	eng_mist-msg_eng
	dc.w	eng_wher-msg_eng
	dc.w	eng_pfcl-msg_eng
	dc.w	eng_atln-msg_eng
	dc.w	eng_fatl-msg_eng

eng_iexp mkstr	{syntax error in expression\}
eng_lpar mkstr	{missing left parenthesis\}
eng_rpar mkstr	{missing right parenthesis\}
eng_lno  mkstr	{error in line number\}
eng_bstr mkstr	{bad string: missing delimiter\}
eng_edef mkstr	{incorrect procedure or function definition\}
eng_idef mkstr	{procedure or function definition not allowed here\}
eng_bdef mkstr	{DEFines may not be within other clauses\}
eng_bedf mkstr	{misplaced END DEFine\}
eng_bloc mkstr	{misplaced LOCal\}
eng_bret mkstr	{RETurn not in procedure or function\}
eng_rcwh mkstr	{WHEN clauses may not be nested\}
eng_endw mkstr	{misplaced END WHEN\}
eng_else mkstr	{misplaced ELSE\}
eng_endi mkstr	{misplaced END IF\}
eng_xstr mkstr	{program structures nested too deeply, my brain aches\}
eng_inif mkstr	{incomplete IF clause\}
eng_insl mkstr	{incomplete SELect clause\}
eng_indf mkstr	{incomplete DEFine\}
eng_inwh mkstr	{incomplete WHEN clause\}
eng_bfor mkstr	{unacceptable loop variable\}
eng_unlp mkstr	{unable to find an open loop\}
eng_blvr mkstr	{undefined loop control variable\}
eng_bsel mkstr	{incorrectly structured SELect clause\}
eng_ends mkstr	{misplaced END SELect\}
eng_dtcl mkstr	{DATA in command line has no meaning\}
eng_brds mkstr	{unacceptable parameters for READ\}
eng_recr mkstr	{SBASIC cannot perform READs within DATA expressions\}
eng_eod  mkstr	{end of DATA\}
eng_bprc mkstr	{unknown procedure\}
eng_bref mkstr	{unknown function or array\}
eng_bdim mkstr	{only arrays may be dimensioned\}
eng_pdim mkstr	{procedure and function parameters may not be dimensioned\}
eng_dmng mkstr	{SBASIC cannot put up with negative dimensions\}
eng_dmov mkstr	{dimensional overflow - you cannot be serious!\}
eng_eind mkstr	{error in index list\}
eng_xind mkstr	{too many indexes\}
eng_asar mkstr	{cannot assign to sub-array\}
eng_iind mkstr	{unacceptable array index list\}
eng_inor mkstr	{array index out of range\}
eng_narr mkstr	{only arrays or strings may be indexed\}
eng_basg mkstr	{assignment can only be to a variable or array element\}
eng_mist mkstr	{MISTake in program\}
eng_wher mkstr	{during when processing\}
eng_pfcl mkstr	{PROC/FN cleared\}
eng_atln mkstr	{At line }
eng_fatl mkstr	{fatal error in SBASIC interpreter\}

msg_deu dc.w	49
	dc.w	deu_iexp-msg_deu
	dc.w	deu_lpar-msg_deu
	dc.w	deu_rpar-msg_deu
	dc.w	deu_lno-msg_deu
	dc.w	deu_bstr-msg_deu
	dc.w	deu_edef-msg_deu
	dc.w	deu_idef-msg_deu
	dc.w	deu_bdef-msg_deu
	dc.w	deu_bedf-msg_deu
	dc.w	deu_bloc-msg_deu
	dc.w	deu_bret-msg_deu
	dc.w	deu_rcwh-msg_deu
	dc.w	deu_endw-msg_deu
	dc.w	deu_else-msg_deu
	dc.w	deu_endi-msg_deu
	dc.w	deu_xstr-msg_deu
	dc.w	deu_inif-msg_deu
	dc.w	deu_insl-msg_deu
	dc.w	deu_indf-msg_deu
	dc.w	deu_inwh-msg_deu
	dc.w	deu_bfor-msg_deu
	dc.w	deu_unlp-msg_deu
	dc.w	deu_blvr-msg_deu
	dc.w	deu_bsel-msg_deu
	dc.w	deu_ends-msg_deu
	dc.w	deu_dtcl-msg_deu
	dc.w	deu_brds-msg_deu
	dc.w	deu_recr-msg_deu
	dc.w	deu_eod-msg_deu
	dc.w	deu_bprc-msg_deu
	dc.w	deu_bref-msg_deu
	dc.w	deu_bdim-msg_deu
	dc.w	deu_pdim-msg_deu
	dc.w	deu_dmng-msg_deu
	dc.w	deu_dmov-msg_deu
	dc.w	deu_eind-msg_deu
	dc.w	deu_xind-msg_deu
	dc.w	deu_asar-msg_deu
	dc.w	deu_iind-msg_deu
	dc.w	deu_inor-msg_deu
	dc.w	deu_narr-msg_deu
	dc.w	deu_basg-msg_deu
	dc.w	deu_mist-msg_deu
	dc.w	deu_wher-msg_deu
	dc.w	deu_pfcl-msg_deu
	dc.w	deu_atln-msg_deu
	dc.w	deu_fatl-msg_deu

deu_iexp mkstr	{Syntax-Fehler im Ausdruck\}
deu_lpar mkstr	{Linke Klammer fehlt\}
deu_rpar mkstr	{Rechte Klammer fehlt\}
deu_lno  mkstr	{fehlerhafte Zeilennummer\}
deu_bstr mkstr	{String-Begrenzer fehlt\}
deu_edef mkstr	{falsche Definition einer Prozedur oder Funktion\}
deu_idef mkstr	{Prozedur- oder Funktion-Definition hier nicht erlaubt\}
deu_bdef mkstr	{DEFines dárfen nicht innerhalb Strukturen stehen\}
deu_bedf mkstr	{END DEFine darf hier nicht stehen\}
deu_bloc mkstr	{LOCal darf hier nicht stehen\}
deu_bret mkstr	{RETurn ist nicht innerhalb Prozedur oder Funktion\}
deu_rcwh mkstr	{WHEN Strukturen dárfen nicht verschachtelt sein\}
deu_endw mkstr	{END WHEN darf hier nicht stehen\}
deu_else mkstr	{ELSE darf hier nicht stehen\}
deu_endi mkstr	{END IF darf hier nicht stehen\}
deu_xstr mkstr	{Strukturen zu tief verschachtelt\}
deu_inif mkstr	{unvollstÄndige IF Struktur\}
deu_insl mkstr	{unvollstÄndige SELect Struktur\}
deu_indf mkstr	{unvollstÄndiges DEFine\}
deu_inwh mkstr	{unvollstÄndige WHEN Struktur\}
deu_bfor mkstr	{unerlaubte Schleifen-Variable\}
deu_unlp mkstr	{kann keine offene Schleife finden\}
deu_blvr mkstr	{undefinierte Schleifen-Variable\}
deu_bsel mkstr	{falsch strukturiertes SELect\}
deu_ends mkstr	{END SELect darf hier nicht stehen\}
deu_dtcl mkstr	{DATA in Befehlszeile wird ignoriert\}
deu_brds mkstr	{unerlaubte Parameter fár READ\}
deu_recr mkstr	{SBASIC kann keine READs innerhalb DATAs ausfáhren\}
deu_eod  mkstr	{Ende von DATA\}
deu_bprc mkstr	{unbekannte Prozedur\}
deu_bref mkstr	{unbekannte Funktion oder Feld\}
deu_bdim mkstr	{nur Felder dárfen dimensioniert werden\}
deu_pdim mkstr	{Prozedur- oder Funktion-Parameter dárfen nicht dimensioniert werden\}
deu_dmng mkstr	{SBASIC mag keine negativen Dimensionen\}
deu_dmov mkstr	{Dimensions-ßberlauf\}
deu_eind mkstr	{Fehler in Index-Liste\}
deu_xind mkstr	{zu viele Indizes\}
deu_asar mkstr	{kann nicht auf Teil-Feld zuweisen\}
deu_iind mkstr	{fehlerhafte Feld-Index-Liste\}
deu_inor mkstr	{Feld-Index auúerhalb Bereich\}
deu_narr mkstr	{nur Felder oder Strings dárfen indiziert werden\}
deu_basg mkstr	{Zuweisungen nur an Variable oder Feld-Element\}
deu_mist mkstr	{MISTake - Fehler im Programm\}
deu_wher mkstr	{wÄhrend WHEN-Bearbeitung\}
deu_pfcl mkstr	{PROC/FN gelÑscht\}
deu_atln mkstr	{In Zeile }
deu_fatl mkstr	{schwerwiegender Fehler im SBASIC-Interpreter\}

msg_fra dc.w	33
	dc.w	fra_iexp-msg_fra
	dc.w	fra_lpar-msg_fra
	dc.w	fra_rpar-msg_fra
	dc.w	fra_lno-msg_fra
	dc.w	fra_bstr-msg_fra
	dc.w	fra_edef-msg_fra
	dc.w	fra_idef-msg_fra
	dc.w	fra_bdef-msg_fra
	dc.w	fra_bedf-msg_fra
	dc.w	fra_bloc-msg_fra
	dc.w	fra_bret-msg_fra
	dc.w	fra_rcwh-msg_fra
	dc.w	fra_endw-msg_fra
	dc.w	fra_else-msg_fra
	dc.w	fra_endi-msg_fra
	dc.w	fra_xstr-msg_fra
	dc.w	fra_inif-msg_fra
	dc.w	fra_insl-msg_fra
	dc.w	fra_indf-msg_fra
	dc.w	fra_inwh-msg_fra
	dc.w	fra_bfor-msg_fra
	dc.w	fra_unlp-msg_fra
	dc.w	fra_blvr-msg_fra
	dc.w	fra_bsel-msg_fra
	dc.w	fra_ends-msg_fra
	dc.w	fra_dtcl-msg_fra
	dc.w	fra_brds-msg_fra
	dc.w	fra_recr-msg_fra
	dc.w	fra_eod-msg_fra
	dc.w	fra_bprc-msg_fra
	dc.w	fra_bref-msg_fra
	dc.w	fra_bdim-msg_fra
	dc.w	fra_pdim-msg_fra
	dc.w	fra_dmng-msg_fra
	dc.w	fra_dmov-msg_fra
	dc.w	fra_eind-msg_fra
	dc.w	fra_xind-msg_fra
	dc.w	fra_asar-msg_fra
	dc.w	fra_iind-msg_fra
	dc.w	fra_inor-msg_fra
	dc.w	fra_narr-msg_fra
	dc.w	fra_basg-msg_fra
	dc.w	fra_mist-msg_fra
	dc.w	fra_wher-msg_fra
	dc.w	fra_pfcl-msg_fra
	dc.w	fra_atln-msg_fra
	dc.w	fra_fatl-msg_fra

fra_iexp mkstr	{erreur de syntaxe dans l'expression\}
fra_lpar mkstr	{manque parenthêse gauche\}
fra_rpar mkstr	{manque parenthêse droite\}
fra_lno  mkstr	{erreur ç la ligne numÉro\}
fra_bstr mkstr	{manque marqueur limite de chaïne\}
fra_edef mkstr	{mauvaise dÉfinition d'une procÉdure ou fonction\}
fra_idef mkstr	{dÉfinition d'une fonction ou procÉdure non permise ici\}
fra_bdef mkstr	{DEFines ne peuvent se trouver dans d'autres structures\}
fra_bedf mkstr	{END DEFine n'est pas ç sa place ici\}
fra_bloc mkstr	{LOCal n'est pas ç sa place ici\}
fra_bret mkstr	{RETurn ne se trouve pas dans une fonction ou procÉdure\}
fra_rcwh mkstr	{des structures WHEN ne peuvent ëtre emboïtÉes \}
fra_endw mkstr	{END WHEN n'est pas ç sa place ici\}
fra_else mkstr	{ELSE n'est pas ç sa place ici\}
fra_endi mkstr	{END IF n'est pas ç sa place ici\}
fra_xstr mkstr	{les structures sont trop emboïtÉes, àa me fait mal au créne\}
fra_inif mkstr	{structure IF incomplête\}
fra_insl mkstr	{structure SELECT incomplête\}
fra_indf mkstr	{structure DEFINE incomplête\}
fra_inwh mkstr	{structure WHEN incomplête\}
fra_bfor mkstr	{variable de contròle boucle inacceptable\}
fra_unlp mkstr	{aucune boucle ouverte ne peut ëtre trouvÉe\}
fra_blvr mkstr	{la variable de contròle boucle est indÉfinie\}
fra_bsel mkstr	{SELECT mal structurÉ\}
fra_ends mkstr	{END SELect n'est pas ç sa place ici\}
fra_dtcl mkstr	{DATA dans une ligne de commande n'a pas de sens\}
fra_brds mkstr	{paramêtre inacceptable pour READ\}
fra_recr mkstr	{SBASIC ne peut effectuer des READs dans des expressions DATA\}
fra_eod  mkstr	{fin de DATA\}
fra_bprc mkstr	{procÉdure inconnue\}
fra_bref mkstr	{fonction ou tableau inconnus\}
fra_bdim mkstr	{on ne peut dimensionner que des tableaux\}
fra_pdim mkstr	{les paramêtres des procÉdures et fonctions ne peuvent ëtre dimensionnÉs\}
fra_dmng mkstr	{SBASIC ne sait comment traiter des dimensions nÉgatives\}
fra_dmov mkstr	{dÉpassement de dimension - soyons sÉrieux!\}
fra_eind mkstr	{erreur dans la liste d'indexage\}
fra_xind mkstr	{trop d'indices\}
fra_asar mkstr	{impossible d'assigner ç un sous-tableau\}
fra_iind mkstr	{liste d'indices dans tableau inacceptable\}
fra_inor mkstr	{indice tableau hors limites\}
fra_narr mkstr	{on peut indexer uniquement des tableaux ou chaïnes\}
fra_basg mkstr	{assignation uniquement vers une variable ou un ÉlÉment d'un tableau\}
fra_mist mkstr	{MISTake - Erreur de programmation\}
fra_wher mkstr	{pendant le traitement de when\}
fra_pfcl mkstr	{PROC/FN effacÉe\}
fra_atln mkstr	{A la ligne }
fra_fatl mkstr	{erreur fatale dans l'interprÉteur SBASIC\}

msg_ita dc.w	39
	dc.w	ita_iexp-msg_ita
	dc.w	ita_lpar-msg_ita
	dc.w	ita_rpar-msg_ita
	dc.w	ita_lno-msg_ita
	dc.w	ita_bstr-msg_ita
	dc.w	ita_edef-msg_ita
	dc.w	ita_idef-msg_ita
	dc.w	ita_bdef-msg_ita
	dc.w	ita_bedf-msg_ita
	dc.w	ita_bloc-msg_ita
	dc.w	ita_bret-msg_ita
	dc.w	ita_rcwh-msg_ita
	dc.w	ita_endw-msg_ita
	dc.w	ita_else-msg_ita
	dc.w	ita_endi-msg_ita
	dc.w	ita_xstr-msg_ita
	dc.w	ita_inif-msg_ita
	dc.w	ita_insl-msg_ita
	dc.w	ita_indf-msg_ita
	dc.w	ita_inwh-msg_ita
	dc.w	ita_bfor-msg_ita
	dc.w	ita_unlp-msg_ita
	dc.w	ita_blvr-msg_ita
	dc.w	ita_bsel-msg_ita
	dc.w	ita_ends-msg_ita
	dc.w	ita_dtcl-msg_ita
	dc.w	ita_brds-msg_ita
	dc.w	ita_recr-msg_ita
	dc.w	ita_eod-msg_ita
	dc.w	ita_bprc-msg_ita
	dc.w	ita_bref-msg_ita
	dc.w	ita_bdim-msg_ita
	dc.w	ita_pdim-msg_ita
	dc.w	ita_dmng-msg_ita
	dc.w	ita_dmov-msg_ita
	dc.w	ita_eind-msg_ita
	dc.w	ita_xind-msg_ita
	dc.w	ita_asar-msg_ita
	dc.w	ita_iind-msg_ita
	dc.w	ita_inor-msg_ita
	dc.w	ita_narr-msg_ita
	dc.w	ita_basg-msg_ita
	dc.w	ita_mist-msg_ita
	dc.w	ita_wher-msg_ita
	dc.w	ita_pfcl-msg_ita
	dc.w	ita_atln-msg_ita
	dc.w	ita_fatl-msg_ita

ita_iexp mkstr	{Errore di sintassi nell'espressione\}
ita_lpar mkstr	{Manca la parentesi sinistra\}
ita_rpar mkstr	{Manca la parentesi destra\}
ita_lno  mkstr	{Errore nella linea numero\}
ita_bstr mkstr	{Stringa errata : manca il delimitatore\}
ita_edef mkstr	{Procedura o definizione di funzione incorretta\}
ita_idef mkstr	{Procedura o definizione di funzione quî non permessa\}
ita_bdef mkstr	{DEFines non puó essere inserito tra altre clausole\}
ita_bedf mkstr	{Errata posizione per END DEFine\}
ita_bloc mkstr	{Errata posizione per LOCal\}
ita_bret mkstr	{RETurn non ê in una procedura o funzione\}
ita_rcwh mkstr	{La clusola WHEN non puo essere annidata\}
ita_endw mkstr	{Errata posizione per END WHEN\}
ita_else mkstr	{Errata posizione per ELSE\}
ita_endi mkstr	{Errata posizione per END IF\}
ita_xstr mkstr	{Program structures nested too deeply, my brain aches\}
ita_inif mkstr	{Clausola IF incompleta\}
ita_insl mkstr	{Clausola SELect incompleta\}
ita_indf mkstr	{DEFine incompleto\}
ita_inwh mkstr	{Clausola WHEN incompleta\}
ita_bfor mkstr	{Inaccettabile variabile di loop\}
ita_unlp mkstr	{Non sono in grado di trovare un loop aperto\}
ita_blvr mkstr	{Variabile di controllo per il loop indefinita\}
ita_bsel mkstr	{Clausola SELect strutturata incorrettamente\}
ita_ends mkstr	{Errata posizione per END SELect\}
ita_dtcl mkstr	{DATA nel comando non ha nessun significato\}
ita_brds mkstr	{Parametro inaccettabile per READ\}
ita_recr mkstr	{SBASIC non puó eseguire un READ senza le strutture DATA\}
ita_eod  mkstr	{Fine di DATA\}
ita_bprc mkstr	{Procedura sconosciuta\}
ita_bref mkstr	{Funzione o array sconosciuti\}
ita_bdim mkstr	{Solo gli array possono essere dimensionati\}
ita_pdim mkstr	{I parametri delle procedure e delle funzioni non possono essere dimensionati\}
ita_dmng mkstr	{SBASIC non puó gestire dimensioni negative\}
ita_dmov mkstr	{Le dimensioni hanno superato il limite - si prega di essere seri !\}
ita_eind mkstr	{Errori nella lista di indici\}
ita_xind mkstr	{Troppi indici\}
ita_asar mkstr	{Non posso assegnare ad un sub-array\}
ita_iind mkstr	{Lista di indici array inaccettabile\}
ita_inor mkstr	{Indici dell'array fuori limite\}
ita_narr mkstr	{Solo gli array o le stringhe possono essere indicizzate\}
ita_basg mkstr	{L'assegnazione puó essere solo ad una variabile o ad un elemento di un array\}
ita_mist mkstr	{MISTake (errore) in un programma\}
ita_wher mkstr	{durante il processo\}
ita_pfcl mkstr	{PROC/FN inizializzata\}
ita_atln mkstr	{Alla linea }
ita_fatl mkstr	{Errore fatale nell'interprete SBASIC\}

msg_esp dc.w	34
	dc.w	esp_iexp-msg_esp
	dc.w	esp_lpar-msg_esp
	dc.w	esp_rpar-msg_esp
	dc.w	esp_lno-msg_esp
	dc.w	esp_bstr-msg_esp
	dc.w	esp_edef-msg_esp
	dc.w	esp_idef-msg_esp
	dc.w	esp_bdef-msg_esp
	dc.w	esp_bedf-msg_esp
	dc.w	esp_bloc-msg_esp
	dc.w	esp_bret-msg_esp
	dc.w	esp_rcwh-msg_esp
	dc.w	esp_endw-msg_esp
	dc.w	esp_else-msg_esp
	dc.w	esp_endi-msg_esp
	dc.w	esp_xstr-msg_esp
	dc.w	esp_inif-msg_esp
	dc.w	esp_insl-msg_esp
	dc.w	esp_indf-msg_esp
	dc.w	esp_inwh-msg_esp
	dc.w	esp_bfor-msg_esp
	dc.w	esp_unlp-msg_esp
	dc.w	esp_blvr-msg_esp
	dc.w	esp_bsel-msg_esp
	dc.w	esp_ends-msg_esp
	dc.w	esp_dtcl-msg_esp
	dc.w	esp_brds-msg_esp
	dc.w	esp_recr-msg_esp
	dc.w	esp_eod-msg_esp
	dc.w	esp_bprc-msg_esp
	dc.w	esp_bref-msg_esp
	dc.w	esp_bdim-msg_esp
	dc.w	esp_pdim-msg_esp
	dc.w	esp_dmng-msg_esp
	dc.w	esp_dmov-msg_esp
	dc.w	esp_eind-msg_esp
	dc.w	esp_xind-msg_esp
	dc.w	esp_asar-msg_esp
	dc.w	esp_iind-msg_esp
	dc.w	esp_inor-msg_esp
	dc.w	esp_narr-msg_esp
	dc.w	esp_basg-msg_esp
	dc.w	esp_mist-msg_esp
	dc.w	esp_wher-msg_esp
	dc.w	esp_pfcl-msg_esp
	dc.w	esp_atln-msg_esp
	dc.w	esp_fatl-msg_esp

esp_iexp mkstr	{error de sintaxis en expresiñn\}
esp_lpar mkstr	{falta abrir parÉntesis\}
esp_rpar mkstr	{falta cerrar parÉntesis\}
esp_lno  mkstr	{error en nômero de lìnea\}
esp_bstr mkstr	{cadena malformada: falta delimitador\}
esp_edef mkstr	{definiciñn de procedimiento o funciñn incorrecta\}
esp_idef mkstr	{definiciñn de procedimiento o funciñn no permitida aquì\}
esp_bdef mkstr	{DEFine no puede estar dentro de otras clåusulas\}
esp_bedf mkstr	{END DEFine mal situado\}
esp_bloc mkstr	{LOCal mal situado\}
esp_bret mkstr	{RETurn no estå en procedimiento o funciñn\}
esp_rcwh mkstr	{clåusula WHEN no puede estar anidada\}
esp_endw mkstr	{END WHEN mal situado\}
esp_else mkstr	{ELSE mal situado\}
esp_endi mkstr	{END IF mal situado\}
esp_xstr mkstr	{estructuras de programa anidadas demasiado profundamente\}
esp_inif mkstr	{clåusula IF incompleta\}
esp_insl mkstr	{clåusula SELect incompleta\}
esp_indf mkstr	{DEFine incompleto\}
esp_inwh mkstr	{clåusula WHEN incompleta\}
esp_bfor mkstr	{variable de bucle inaceptable\}
esp_unlp mkstr	{incapaz de encontrar un bucle abierto\}
esp_blvr mkstr	{variable de control de bucle no definida\}
esp_bsel mkstr	{clåusula SELect estructurada incorrectamente\}
esp_ends mkstr	{END SELect mal situado\}
esp_dtcl mkstr	{DATA no tiene significado en la lìnea de comandos\}
esp_brds mkstr	{paråmetros para READ inaceptables\}
esp_recr mkstr	{SBASIC no puede realizar READ dentro de expresiones DATA\}
esp_eod  mkstr	{fin de DATA\}
esp_bprc mkstr	{procedimiento desconocido\}
esp_bref mkstr	{funciñn o matriz desconocida\}
esp_bdim mkstr	{sñlo las matrices pueden ser dimensionadas\}
esp_pdim mkstr	{los paråmetros de procedimientos y funciones no pueden ser dimensionados\}
esp_dmng mkstr	{SBASIC no puede trabajar con dimensiones negativas\}
esp_dmov mkstr	{desbordamiento dimensional\}
esp_eind mkstr	{error en lista de ìndices\}
esp_xind mkstr	{demasiados ìndices\}
esp_asar mkstr	{imposible asignar a sub-matriz\}
esp_iind mkstr	{lista de ìndices de la matriz inaceptable\}
esp_inor mkstr	{ìndice de matriz fuera de rango\}
esp_narr mkstr	{sñlo matrices o cadenas pueden tener ìndice\}
esp_basg mkstr	{la asignaciñn sñlo es posible a una variable o elemento de una matriz\}
esp_mist mkstr	{lìnea mal formada (MISTake) en programa\}
esp_wher mkstr	{durante proceso de WHEN\}
esp_pfcl mkstr	{estado interno PROC/FN inicializado\}
esp_atln mkstr	{En la lìnea }
esp_fatl mkstr	{error fatal en el intÉrprete SBASIC\}                                          

	 ds.w	 0

	end
