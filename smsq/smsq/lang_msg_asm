; SMSQ_LANG_MSG  Message Tables   V2.1	 1994	Tony Tebby
;
; 2020-04-13  2.1  Added Spanish messages (MK)

	section language

	xdef	smsq_msg

	xref	smsq_msg8

	include 'dev8_keys_ldm'
	include 'dev8_mac_text'

; SMSQ message tables

smsq_msg
	dc.w	ldm.msgt,0,44	 ; English
	dc.w	6
	dc.l	msg_eng-*

	dc.w	ldm.msgt,0,49	 ; German
	dc.w	6
	dc.l	msg_deu-*

	dc.w	ldm.msgt,0,39	 ; Italian
	dc.w	6
	dc.l	msg_ita-*

	dc.w	ldm.msgt,0,33	 ; French
	dc.w	6
	dc.l	msg_fra-*

	dc.w	ldm.msgt,0,34	 ; Spanish
	dc.w	smsq_msg8-*
	dc.l	msg_esp-*

; message  tables

msg_eng dc.w	44
	dc.w	eng_nc-msg_eng
	dc.w	eng_ijob-msg_eng
	dc.w	eng_imem-msg_eng
	dc.w	eng_orng-msg_eng
	dc.w	eng_bffl-msg_eng
	dc.w	eng_ichn-msg_eng
	dc.w	eng_fdnf-msg_eng
	dc.w	eng_fex-msg_eng
	dc.w	eng_fdiu-msg_eng
	dc.w	eng_eof-msg_eng
	dc.w	eng_drfl-msg_eng
	dc.w	eng_inam-msg_eng
	dc.w	eng_trns-msg_eng
	dc.w	eng_fmtf-msg_eng
	dc.w	eng_ipar-msg_eng
	dc.w	eng_mchk-msg_eng
	dc.w	eng_iexp-msg_eng
	dc.w	eng_ovfl-msg_eng
	dc.w	eng_nimp-msg_eng
	dc.w	eng_rdo-msg_eng
	dc.w	eng_isyn-msg_eng
	dc.w	eng_noms-msg_eng
	dc.w	eng_accd-msg_eng

eng_nc	 mkstr	{incomplete\}
eng_ijob mkstr	{invalid Job ID\}
eng_imem mkstr	{insufficient memory\}
eng_orng mkstr	{value out of range\}
eng_bffl mkstr	{buffer full\}
eng_ichn mkstr	{invalid channel ID\}
eng_fdnf mkstr	{not found\}
eng_fex  mkstr	{already exists\}
eng_fdiu mkstr	{is in use\}
eng_eof  mkstr	{end of file\}
eng_drfl mkstr	{medium is full\}
eng_inam mkstr	{invalid name\}
eng_trns mkstr	{transmission error\}
eng_fmtf mkstr	{format failed\}
eng_ipar mkstr	{invalid parameter\}
eng_mchk mkstr	{medium check failed\}
eng_iexp mkstr	{error in expression\}
eng_ovfl mkstr	{arithmetic overflow\}
eng_nimp mkstr	{not implemented\}
eng_rdo  mkstr	{write protected\}
eng_isyn mkstr	{invalid syntax\}
eng_noms mkstr	{unknown message\}
eng_accd mkstr	{access denied\}

msg_deu dc.w	49
	dc.w	deu_nc-msg_deu
	dc.w	deu_ijob-msg_deu
	dc.w	deu_imem-msg_deu
	dc.w	deu_orng-msg_deu
	dc.w	deu_bffl-msg_deu
	dc.w	deu_ichn-msg_deu
	dc.w	deu_fdnf-msg_deu
	dc.w	deu_fex-msg_deu
	dc.w	deu_fdiu-msg_deu
	dc.w	deu_eof-msg_deu
	dc.w	deu_drfl-msg_deu
	dc.w	deu_inam-msg_deu
	dc.w	deu_trns-msg_deu
	dc.w	deu_fmtf-msg_deu
	dc.w	deu_ipar-msg_deu
	dc.w	deu_mchk-msg_deu
	dc.w	deu_iexp-msg_deu
	dc.w	deu_ovfl-msg_deu
	dc.w	deu_nimp-msg_deu
	dc.w	deu_rdo-msg_deu
	dc.w	deu_isyn-msg_deu
	dc.w	deu_noms-msg_deu
	dc.w	deu_accd-msg_deu

deu_nc	 mkstr	{unterbrochen\}
deu_ijob mkstr	{ungáltige Job ID\}
deu_imem mkstr	{zu wenig freier Speicher\}
deu_orng mkstr	{Wert auúerhalb Bereich\}
deu_bffl mkstr	{Puffer voll\}
deu_ichn mkstr	{ungáltige Kanal ID\}
deu_fdnf mkstr	{nicht gefunden\}
deu_fex  mkstr	{existiert bereits\}
deu_fdiu mkstr	{wird schon benutzt\}
deu_eof  mkstr	{Datei-Ende\}
deu_drfl mkstr	{Medium ist voll\}
deu_inam mkstr	{ungáltiger Name\}
deu_trns mkstr	{ßbertragungs-Fehler\}
deu_fmtf mkstr	{Formatier-Fehler\}
deu_ipar mkstr	{ungáltiger Parameter\}
deu_mchk mkstr	{fehlerhafter DatentrÄger\}
deu_iexp mkstr	{Fehler im Ausdruck\}
deu_ovfl mkstr	{arithmetischer ßberlauf\}
deu_nimp mkstr	{nicht implementiert\}
deu_rdo  mkstr	{schreibgeschátzt\}
deu_isyn mkstr	{Syntax-Fehler\}
deu_noms mkstr	{unbekannte Meldung\}
deu_accd mkstr	{Zugriff verweigert\}

msg_fra dc.w	33
	dc.w	fra_nc-msg_fra
	dc.w	fra_ijob-msg_fra
	dc.w	fra_imem-msg_fra
	dc.w	fra_orng-msg_fra
	dc.w	fra_bffl-msg_fra
	dc.w	fra_ichn-msg_fra
	dc.w	fra_fdnf-msg_fra
	dc.w	fra_fex-msg_fra
	dc.w	fra_fdiu-msg_fra
	dc.w	fra_eof-msg_fra
	dc.w	fra_drfl-msg_fra
	dc.w	fra_inam-msg_fra
	dc.w	fra_trns-msg_fra
	dc.w	fra_fmtf-msg_fra
	dc.w	fra_ipar-msg_fra
	dc.w	fra_mchk-msg_fra
	dc.w	fra_iexp-msg_fra
	dc.w	fra_ovfl-msg_fra
	dc.w	fra_nimp-msg_fra
	dc.w	fra_rdo-msg_fra
	dc.w	fra_isyn-msg_fra
	dc.w	fra_noms-msg_fra
	dc.w	fra_accd-msg_fra

fra_nc	 mkstr	{opÉration incomplête\}
fra_ijob mkstr	{ID Job non valable\}
fra_imem mkstr	{hors capacitÉ mÉmoire\}
fra_orng mkstr	{valeur hors limites\}
fra_bffl mkstr	{tampon plein\}
fra_ichn mkstr	{ID canal non valable\}
fra_fdnf mkstr	{est introuvable\}
fra_fex  mkstr	{existe dÉja\}
fra_fdiu mkstr	{est utilisÉ par ailleurs\}
fra_eof  mkstr	{fin de fichier\}
fra_drfl mkstr	{disque plein\}
fra_inam mkstr	{nom inadmissible\}
fra_trns mkstr	{erreur de transmission\}
fra_fmtf mkstr	{erreur dans le formatage\}
fra_ipar mkstr	{paramêtre non valable\}
fra_mchk mkstr	{erreur de support\}
fra_iexp mkstr	{erreur dans l'expression\}
fra_ovfl mkstr	{dÉbordement arithmÉtique\}
fra_nimp mkstr	{àa n'existe pas\}
fra_rdo  mkstr	{protection en Écriture\}
fra_isyn mkstr	{syntaxe non valable\}
fra_noms mkstr	{message inconnu\}
fra_accd mkstr	{accês interdit\}

msg_ita dc.w	  39
	dc.w	ita_nc-msg_ita
	dc.w	ita_ijob-msg_ita
	dc.w	ita_imem-msg_ita
	dc.w	ita_orng-msg_ita
	dc.w	ita_bffl-msg_ita
	dc.w	ita_ichn-msg_ita
	dc.w	ita_fdnf-msg_ita
	dc.w	ita_fex-msg_ita
	dc.w	ita_fdiu-msg_ita
	dc.w	ita_eof-msg_ita
	dc.w	ita_drfl-msg_ita
	dc.w	ita_inam-msg_ita
	dc.w	ita_trns-msg_ita
	dc.w	ita_fmtf-msg_ita
	dc.w	ita_ipar-msg_ita
	dc.w	ita_mchk-msg_ita
	dc.w	ita_iexp-msg_ita
	dc.w	ita_ovfl-msg_ita
	dc.w	ita_nimp-msg_ita
	dc.w	ita_rdo-msg_ita
	dc.w	ita_isyn-msg_ita
	dc.w	ita_noms-msg_ita
	dc.w	ita_accd-msg_ita

ita_nc	 mkstr	{Operazione incompleta !\}
ita_ijob mkstr	{Job ID non valido\}
ita_imem mkstr	{Memoria insufficente\}
ita_orng mkstr	{Valore fuori limite\}
ita_bffl mkstr	{Buffer pieno\}
ita_ichn mkstr	{ID di canale invalido\}
ita_fdnf mkstr	{Non trovato\}
ita_fex  mkstr	{Gia' esistente\}
ita_fdiu mkstr	{ê in uso\}
ita_eof  mkstr	{fine del file\}
ita_drfl mkstr	{Unitç piena\}
ita_inam mkstr	{Nome invalido\}
ita_trns mkstr	{Errore di trasmissione\}
ita_fmtf mkstr	{Formattazione fallita\}
ita_ipar mkstr	{Parametro invalido\}
ita_mchk mkstr	{Verifica unitç fallita\}
ita_iexp mkstr	{Errore nell' espressione\}
ita_ovfl mkstr	{Overflow aritmetico\}
ita_nimp mkstr	{Non ancora implementato\}
ita_rdo  mkstr	{Protetto in scrittura\}
ita_isyn mkstr	{Sintassi invalida\}
ita_noms mkstr	{Messaggio sconosciuto\}
ita_accd mkstr	{Accesso negato\}

msg_esp dc.w	34
	dc.w	esp_nc-msg_esp
	dc.w	esp_ijob-msg_esp
	dc.w	esp_imem-msg_esp
	dc.w	esp_orng-msg_esp
	dc.w	esp_bffl-msg_esp
	dc.w	esp_ichn-msg_esp
	dc.w	esp_fdnf-msg_esp
	dc.w	esp_fex-msg_esp
	dc.w	esp_fdiu-msg_esp
	dc.w	esp_eof-msg_esp
	dc.w	esp_drfl-msg_esp
	dc.w	esp_inam-msg_esp
	dc.w	esp_trns-msg_esp
	dc.w	esp_fmtf-msg_esp
	dc.w	esp_ipar-msg_esp
	dc.w	esp_mchk-msg_esp
	dc.w	esp_iexp-msg_esp
	dc.w	esp_ovfl-msg_esp
	dc.w	esp_nimp-msg_esp
	dc.w	esp_rdo-msg_esp
	dc.w	esp_isyn-msg_esp
	dc.w	esp_noms-msg_esp
	dc.w	esp_accd-msg_esp

esp_nc	 mkstr	{no completado\}
esp_ijob mkstr	{tarea invålida\}
esp_imem mkstr	{sin memoria\}
esp_orng mkstr	{fuera de rango\}
esp_bffl mkstr	{memoria intermedia llena\}
esp_ichn mkstr	{canal no abierto\}
esp_fdnf mkstr	{no encontrado\}
esp_fex  mkstr	{ya existe\}                 
esp_fdiu mkstr	{en uso\}
esp_eof  mkstr	{fin de fichero\}
esp_drfl mkstr	{medio lleno\}
esp_inam mkstr	{nombre invålido\}
esp_trns mkstr	{error de transmisiñn\}
esp_fmtf mkstr	{fallo en formateo\}
esp_ipar mkstr	{paråmetro invålido\}
esp_mchk mkstr	{medio incorrecto\}
esp_iexp mkstr	{expresiñn errñnea\}
esp_ovfl mkstr	{desbordamiento aritmÉtico\}
esp_nimp mkstr	{no implementado\}
esp_rdo  mkstr	{sñlo lectura\}
esp_isyn mkstr	{error de sintaxis\}
esp_noms mkstr	{mensaje desconocido\}
esp_accd mkstr	{acceso denegado\}

	ds.w	0

	end
