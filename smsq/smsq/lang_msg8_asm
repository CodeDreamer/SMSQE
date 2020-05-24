; SMSQ_LANG_MSG8  Messages Tables - Group 8  V2.1   1994  Tony Tebby
;
; 2020-04-13  2.1  Added Spanish messages (MK)

	section language

	xdef	smsq_msg8

	xref	smsq_msgc

	include 'dev8_keys_ldm'
	include 'dev8_mac_text'

; SMSQ group 8 message tables

smsq_msg8
	dc.w	ldm.msgt,8,44	 ; English
	dc.w	6
	dc.l	msg_eng-*

	dc.w	ldm.msgt,8,49	 ; German
	dc.w	6
	dc.l	msg_deu-*

	dc.w	ldm.msgt,8,39	 ; Italian
	dc.w	6
	dc.l	msg_ita-*

	dc.w	ldm.msgt,8,33	 ; French
	dc.w	6
	dc.l	msg_fra-*

	dc.w	ldm.msgt,8,34	 ; Spanish
	dc.w	smsq_msgc-*
	dc.l	msg_esp-*


; message tables

msg_eng dc.w	44
	dc.w	eng_fmtq-msg_eng
	dc.w	eng_abrt-msg_eng
	dc.w	eng_grab-msg_eng
	dc.w	eng_llrc-msg_eng
	dc.w	eng_ynch-msg_eng
	dc.w	eng_yn-msg_eng
	dc.w	eng_ynaq-msg_eng
	dc.w	eng_to-msg_eng
	dc.w	eng_exst-msg_eng
	dc.w	eng_ovrw-msg_eng
	dc.w	eng_scts-msg_eng
	dc.w	eng_jobh-msg_eng
	dc.w	eng_nabt-msg_eng
	dc.w	eng_infl-msg_eng

eng_fmtq mkstr	{To FORMAT the disk, press }
eng_abrt mkstr	{******* ABORTED *******}
eng_grab mkstr	{Working memory allocation (kilobytes)> }
eng_llrc mkstr	{last line recall}
eng_ynch mkstr	{YNAQ}
eng_yn	 mkstr	{..Y or N? }
eng_ynaq mkstr	{..Y/N/A/Q? }
eng_to	 mkstr	{ TO }
eng_exst mkstr	{ exists, }
eng_ovrw mkstr	{OK to overwrite}
eng_scts mkstr	{ sectors\}
eng_jobh mkstr	{Job Tag    Owner Priority Job-Name\}
eng_nabt mkstr	{net aborted\}
eng_infl mkstr	{K in files\}

msg_deu dc.w	49
	dc.w	deu_fmtq-msg_deu
	dc.w	deu_abrt-msg_deu
	dc.w	deu_grab-msg_deu
	dc.w	deu_llrc-msg_deu
	dc.w	deu_ynch-msg_deu
	dc.w	deu_yn-msg_deu
	dc.w	deu_ynaq-msg_deu
	dc.w	deu_to-msg_deu
	dc.w	deu_exst-msg_deu
	dc.w	deu_ovrw-msg_deu
	dc.w	deu_scts-msg_deu
	dc.w	deu_jobh-msg_deu
	dc.w	deu_nabt-msg_deu
	dc.w	deu_infl-msg_deu

deu_fmtq mkstr	{Zum FORMATIEREN der Platte, dr‡cke }
deu_abrt mkstr	{******* ABGEBROCHEN *******}
deu_grab mkstr	{Arbeitsspeichergr„œe (Kilobytes)> }
deu_llrc mkstr	{Letzte Zeile zur‡ckerhalten}
deu_ynch mkstr	{JNAQ}
deu_yn	 mkstr	{..J oder N? }
deu_ynaq mkstr	{..J/N/A/Q? }
deu_to	 mkstr	{ ZU }
deu_exst mkstr	{ gibt es schon, }
deu_ovrw mkstr	{OK zum ‡berschreiben}
deu_scts mkstr	{ Sektoren\}
deu_jobh mkstr	{Job Tag Besitzer Priorit€t\}
deu_nabt mkstr	{Net abgebrochen\}
deu_infl mkstr	{K in Dateien\}

msg_fra dc.w	33
	dc.w	fra_fmtq-msg_fra
	dc.w	fra_abrt-msg_fra
	dc.w	fra_grab-msg_fra
	dc.w	fra_llrc-msg_fra
	dc.w	fra_ynch-msg_fra
	dc.w	fra_yn-msg_fra
	dc.w	fra_ynaq-msg_fra
	dc.w	fra_to-msg_fra
	dc.w	fra_exst-msg_fra
	dc.w	fra_ovrw-msg_fra
	dc.w	fra_scts-msg_fra
	dc.w	fra_jobh-msg_fra
	dc.w	fra_nabt-msg_fra
	dc.w	fra_infl-msg_fra

fra_fmtq mkstr	{Pour FORMATER le disque, appuyez sur }
fra_abrt mkstr	{******* INTERROMPU *******}
fra_grab mkstr	{Allocation de mƒmoire de travail (en kilo-octets)> }
fra_llrc mkstr	{rappel de la ligne prƒcƒdente}
fra_ynch mkstr	{ONTQ}
fra_yn	 mkstr	{..O ou N? }
fra_ynaq mkstr	{..O/N/T/Q? }
fra_to	 mkstr	{ VERS }
fra_exst mkstr	{ existe dƒj, }
fra_ovrw mkstr	{je l'ƒcrase}
fra_scts mkstr	{ secteurs\}
fra_jobh mkstr	{Job ƒtiq. prop. prioritƒ\}
fra_nabt mkstr	{Net interrompu\}
fra_infl mkstr	{K en fichiers\}

msg_ita dc.w	39
	dc.w	ita_fmtq-msg_ita
	dc.w	ita_abrt-msg_ita
	dc.w	ita_grab-msg_ita
	dc.w	ita_llrc-msg_ita
	dc.w	ita_ynch-msg_ita
	dc.w	ita_yn-msg_ita
	dc.w	ita_ynaq-msg_ita
	dc.w	ita_to-msg_ita
	dc.w	ita_exst-msg_ita
	dc.w	ita_ovrw-msg_ita
	dc.w	ita_scts-msg_ita
	dc.w	ita_jobh-msg_ita
	dc.w	ita_nabt-msg_ita
	dc.w	ita_infl-msg_ita

ita_fmtq mkstr	{Per FORMATtare il disco, premere }
ita_abrt mkstr	{******* TERMINATO *******}
ita_grab mkstr	{Allocazione di memoria di lavoro (kilobytes)> }
ita_llrc mkstr	{richiamo ultima linea digitata}
ita_ynch mkstr	{SNTA}
ita_yn	 mkstr	{..Si o No? }
ita_ynaq mkstr	{..Si/No/Tutti/Abbandona? }
ita_to	 mkstr	{ a }
ita_exst mkstr	{ esiste, }
ita_ovrw mkstr	{OK per riscrivere}
ita_scts mkstr	{ settori\}
ita_jobh mkstr	{ID Tag  ID-Prop. Priorit Nome-Job\}
ita_nabt mkstr	{net interrotta\}
ita_infl mkstr	{K nei files\}
 
msg_esp dc.w	39
	dc.w	esp_fmtq-msg_esp
	dc.w	esp_abrt-msg_esp
	dc.w	esp_grab-msg_esp
	dc.w	esp_llrc-msg_esp
	dc.w	esp_ynch-msg_esp
	dc.w	esp_yn-msg_esp
	dc.w	esp_ynaq-msg_esp
	dc.w	esp_to-msg_esp
	dc.w	esp_exst-msg_esp
	dc.w	esp_ovrw-msg_esp
	dc.w	esp_scts-msg_esp
	dc.w	esp_jobh-msg_esp
	dc.w	esp_nabt-msg_esp
	dc.w	esp_infl-msg_esp

esp_fmtq mkstr	{Para formatear el disco, pulse }                   
esp_abrt mkstr	{******* INTERRUMPIDO *******}
esp_grab mkstr	{Reserva de memoria de trabajo (KiB) > }
esp_llrc mkstr	{recuperar la ™ltima l“nea}
esp_ynch mkstr	{SNTC}
esp_yn	 mkstr	{? (S/N) }
esp_ynaq mkstr	{? (S/N/T/C) }
esp_to	 mkstr	{ A }
esp_exst mkstr	{ existe, }
esp_ovrw mkstr	{´sobreescribir}
esp_scts mkstr	{ sectores\}
esp_jobh mkstr	{Tarea Etiq. Prop. Prior.\}
esp_nabt mkstr	{red interrumpida\}
esp_infl mkstr	{KiB en ficheros\}
 
	ds.w	0

	end
