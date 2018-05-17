; SBAS_TABLES - Parser Tables		        1992 Tony Tebby

	section sbas

	xdef	sb_graph
	xdef	sb_expgr

	xdef	sbp_intb

	xdef	sbp_keyt
	xdef	sbp_symt
	xdef	sbp_opt
	xdef	sbp_mont
	xdef	sbp_sept

	xdef	sbp_dkey
	xdef	sbp_skey
	xdef	sbp_nkey

	xdef	sbp_nchr

	include 'dev8_smsq_sbas_parser_keys'

;+++
; Internal byte token table
;---
sbp_intb
	dc.w	0
	dc.w	tkw.end  ; $8101     ; END
	dc.w	tkw.for  ; $8102     ; FOR
	dc.w	tkw.rep  ; $8104     ; REPeat
	dc.w	tkw.if	 ; $8103     ; IF
	dc.w	tkw.sel  ; $8105     ; SELect
	dc.w	tkw.when ; $8106     ; WHEN
	dc.w	tkw.def  ; $8107     ; DEFine
	dc.w	tkw.proc ; $8108     ; PROCedure
	dc.w	tkw.fn	 ; $8109     ; FuNction
	dc.w	tkw.fn	 ; $8109     ; FN
	dc.w	tkw.go	 ; $810a     ; GO
	dc.w	tkw.to	 ; $810b     ; TO
	dc.w	tkw.sub  ; $810c     ; SUB
	dc.w	tkw.err  ; $810e     ; ERRor
	dc.w	0
	dc.w	0
	dc.w	tkw.rest ; $8111     ; RESTORE
	dc.w	tkw.next ; $8112     ; NEXT
	dc.w	tkw.exit ; $8113     ; EXIT
	dc.w	tkw.else ; $8114     ; ELSE
	dc.w	tkw.on	 ; $8115     ; ON
	dc.w	tkw.ret  ; $8116     ; RETurn
	dc.w	tkw.rmdr ; $8117     ; REMAINDER
	dc.w	tkw.data ; $8118     ; DATA
	dc.w	tkw.dim  ; $8119     ; DIM
	dc.w	tkw.loc  ; $811a     ; LOCal
	dc.w	tkw.let  ; $811b     ; LET
	dc.w	tkw.then ; $811c     ; THEN
	dc.w	tkw.step ; $811d     ; STEP
	dc.w	tkw.rem  ; $811e     ; REMark
	dc.w	tkw.mist ; $811f     ; MISTake
	dc.w	0
	dc.w	tkw.lequ ; $8401     ; (LET) =
	dc.w	tkw.coln ; $8402     ; :
	dc.w	tkw.hash ; $8403     ; #
	dc.w	tkw.cmma ; $8404     ; ,
	dc.w	tkw.lpar ; $8405     ; (
	dc.w	tkw.rpar ; $8406     ; )
	dc.w	tkw.lbrc ; $8407     ; {
	dc.w	tkw.rbrc ; $8408     ; }
	dc.w	tkw.spce ; $8409     ; space (significant)
	dc.w	tkw.eol  ; $840a     ; end of line
	dc.w	0
	dc.w	0
	dc.w	0
	dc.w	0
	dc.w	0
	dc.w	0
	dc.w	tkw.plus ; $8501     ; +
	dc.w	tkw.minu ; $8502     ; -
	dc.w	tkw.mulf ; $8503     ; *
	dc.w	tkw.divf ; $8504     ; /
	dc.w	tkw.ge	 ; $8505     ; >=
	dc.w	tkw.gt	 ; $8506     ; >
	dc.w	tkw.apeq ; $8507     ; ==
	dc.w	tkw.eq	 ; $8508     ; =
	dc.w	tkw.ne	 ; $8509     ; <>
	dc.w	tkw.le	 ; $850a     ; <=
	dc.w	tkw.lt	 ; $850b     ; <
	dc.w	tkw.bor  ; $850c     ; ||
	dc.w	tkw.band ; $850d     ; &&
	dc.w	tkw.bxor ; $850e     ; ^^
	dc.w	tkw.pwr  ; $850f     ; ^
	dc.w	tkw.cnct ; $8510     ; &
	dc.w	tkw.or	 ; $8511     ; OR
	dc.w	tkw.and  ; $8512     ; AND
	dc.w	tkw.xor  ; $8513     ; XOR
	dc.w	tkw.mod  ; $8514     ; MOD
	dc.w	tkw.div  ; $8515     ; DIV
	dc.w	tkw.inst ; $8516     ; INSTR

	dc.w	tkw.neg  ; $8601     ; negate
	dc.w	tkw.pos  ; $8602     ; positive!!
	dc.w	tkw.bnot ; $8603     ; ~~
	dc.w	tkw.not  ; $8604     ; NOT

	dc.w	tkw.name ; $8800

	dc.w	tkw.quot ; $8b22     ; delimited by "quotes"
	dc.w	tkw.apst ; $8b27     ; delimited by 'apostrophes'

	dc.w	tkw.text ; $8c00     ; text

	dc.w	tkw.lno  ; $8d00     ; line number
	dc.w	0
	dc.w	tkw.scom ; $8e01     ; separator comma
	dc.w	tkw.scol ; $8e02     ; semicolon
	dc.w	tkw.bsol ; $8e03     ; back solidus
	dc.w	tkw.excl ; $8e04     ; exclamation mark
	dc.w	tkw.sto  ; $8e05     ; separator TO

tabstt	macro
tabcnt	setnum 0
	endm

tablet	macro	name,chars
tabcnt	setnum [tabcnt]+1
tabstr[tabcnt] setstr {tkn_[name] dc.w [.len(chars)],'[chars]'}
	dc.w	tkn_[name]-*
	endm

tabchar macro	name,chars
tabcnt	setnum [tabcnt]+1
tabstr[tabcnt] setstr {tkn_[name] dc.w [.len(chars)],'[chars]'}
	dc.w	tkn_[name]-*
	endm

tabbyte macro	name,byte
tabcnt	setnum [tabcnt]+1
tabstr[tabcnt] setstr {tkn_[name] dc.w 1,[byte]00}
	dc.w	tkn_[name]-*
	endm

tabend	macro
tabcntx setnum	0
tabe	maclab
tabcntx setnum [tabcntx]+1
[tabstr[tabcntx]]
	ifnum [tabcntx] < [tabcnt] goto tabe
	endm

	tabstt
;+++
; keyword token to keyword string
;---
sbp_keyt
	dc.w	0
	tablet	end  END
	tablet	for  FOR
	tablet	if   IF
	tablet	rep  REPeat
	tablet	sel  SELect
	tablet	when WHEN
	tablet	def  DEFine
	tablet	proc PROCedure
	tablet	fun  FuNction
	tablet	go   GO
	tablet	to   TO
	tablet	sub  SUB
	dc.w	0
	tablet	err  ERRor
	dc.w	0
	dc.w	0
	tablet	rest RESTORE
	tablet	next NEXT
	tablet	exit EXIT
	tablet	else ELSE
	tablet	on   ON
	tablet	ret  RETurn
	tablet	rmdr REMAINDER
	tablet	data DATA
	tablet	dim  DIM
	tablet	loc  LOCal
	tablet	let  LET
	tablet	then THEN
	tablet	step STEP
	tablet	rem  REMark
	tablet	mist MISTake

tkn_fn	dc.w	2,'FN'

;+++
; Symbol token to symbol
;---
sbp_symt
	dc.w	0
	tabchar lequ {=}
	tabchar coln {:}
	tabchar hash {#}
	tabchar cmma {,}
	tabchar lpar {(}
	tabchar rpar {)}
	tabbyte lbrc $7b
	tabbyte rbrc $7d
	tabbyte spce $20
	tabbyte eol  $0a

;+++
; Operator token to symbol
;---
sbp_opt
	dc.w	0
	tabchar plus {+}
	tabchar minu {-}
	tabchar mulf {*}
	tabchar divf {/}
	tabchar ge   {>=}
	tabchar gt   {>}
	tabchar apeq {==}
	tabchar eq   {=}
	tabchar ne   {<>}
	tabchar le   {<=}
	tabchar lt   {<}
	tabchar bor  {||}
	tabchar band {&&}
	tabchar bxor {^^}
	tabchar pwr  {^}
	tabchar cnct {&}
	tablet	or   OR
	tablet	and  AND
	tablet	xor  XOR
	tablet	mod  MOD
	tablet	div  DIV
	tablet	inst INSTR

;+++
; Monadic operator token to symbol
;---
sbp_mont
	dc.w	0
	tabchar neg  {-}
	tabchar pos  {+}
	tabchar bnot {~~}
	tablet	not  NOT

;+++
; Separator token to symbol
;---
sbp_sept
	dc.w	0
	tabchar scom {,}
	tabchar scol {;}
	tabchar bsol {\}
	tabchar excl {!}
	tablet	sto  TO

	tabend

ditm	macro	itok,itok1,itok2,abb1,abb2,name1,name2
name.1	setstr	{[name1]}
	ifstr {[name.1]} <> {} goto ditm.2
name.1	setstr	[itok1]
ditm.2	maclab
name.2	setstr	{[name2]}
	ifstr {[name.2]} <> {} goto ditm.set
name.2	setstr	[itok2]
ditm.set maclab

	dc.b	tki.[itok],[abb1]+[abb2]  ; combined token
	dc.b	tki.[itok1],tki.[itok2]   ; individual tokens
	dc.b	[abb1],[abb2]		  ; abbreviation lengths
	dc.w	tkn_[name.1]-*
	dc.w	tkn_[name.2]-*
	endm

item	macro	itok,abb,name
name.	setstr	{[name]}
	ifstr {[name.]} <> {} goto item.set
name.	setstr	[itok]
item.set maclab

	dc.b	tki.[itok],[abb]	  ; token, abbreviation length
	dc.w	tkn_[name.]-*
	endm

;+++
; Double Keyword Table
;---
sbp_dkey
	 ditm	efor,end,for,3,3
	 ditm	eif,end,if,3,2
	 ditm	erep,end,rep,3,3
	 ditm	esel,end,sel,3,3
	 ditm	edef,end,def,3,3
	 ditm	ewhn,end,when,3,4
	 ditm	edef,end,def,3,3
	 ditm	dprc,def,proc,3,4
	 ditm	dfn,def,fn,3,3,def,fun
	 ditm	dfn,def,fn,3,2,def,fn
	 ditm	goto,go,to,2,2
	 ditm	gsub,go,sub,2,3
	 dc.w	0

;+++
; Single Keyword Table
;---
sbp_skey
	item	end,3
	item	for,3
	item	if,2
	item	rep,3
	item	sel,3
	item	when,4
	item	def,3
	item	proc,4
	item	fn,3,fun
	item	fn,2,fn
	item	go,2
	item	to,2
	item	sub,3
	item	err,3
	item	rest,7
	item	next,4
	item	exit,4
	item	else,4
	item	on,2
	item	ret,3
	item	rmdr,9
	item	data,4
	item	dim,3
	item	loc,3
	item	let,3
	item	then,4
	item	step,4
	item	rem,3
	item	mist,4

	item	or,2
	item	and,3
	item	xor,3
	item	mod,3
	item	div,3
	item	inst,5

	item	not,3

;;;;	item	sto,2		  = to

	dc.w	0

;+++
; Not keyword item table
;---
sbp_nkey
	item	apeq,2		 ; == to preempt LET =
	item	lequ,1
	item	coln,1
	item	hash,1
	item	cmma,1
	item	lpar,1
	item	rpar,1
	item	lbrc,1
	item	rbrc,1
	item	spce,1
	item	eol,1

	item	plus,1
	item	minu,1
	item	mulf,1
	item	divf,1
	item	ge,2
	item	gt,2
;;;;	item	apeq,2		  ; defined before lequ
;;;;	item	eq,1		  ; = lequ
	item	ne,2
	item	le,2
	item	lt,1
	item	bor,2
	item	band,2
	item	bxor,2
	item	pwr,1
	item	cnct,1

;;;;	item	neg,1		  ; = plus
;;;;	item	pos,1		  ; = minu
	item	bnot,2

;;;;	item	scom,1		  ; = cmma
	item	scol,1
	item	bsol,1
	item	excl,1

	dc.w	0

	page

graph	macro	graph,next
[.lab]	dc.b	sbg.call,sbg.grph
	dc.w	[graph]-*
	dc.w	[next]-*
	endm

code	macro	code,next
	xref	[code]
[.lab]	dc.b	sbg.call,sbg.code
	dc.w	[code]-*
	dc.w	[next]-*
	endm

option	macro	mini,maxi,subi,next
[.lab]	optn	sbg.optn,[mini],[maxi],[subi],[next]
	endm

nret	macro	mini,maxi,subi,next
[.lab]	optn	sbg.nret,[mini],[maxi],[subi],[next]
	endm

jump	macro	next
[.lab]	dc.b	sbg.optn,0,0,0
	dc.w	[next]-*
	endm

exit	macro	errno
[.lab]	errex	sbg.exit,[errno]
	endm

errexit macro	errno
[.lab]	errex	sbg.err,[errno]
	endm


errex	macro	type,errno
	ifstr	{[errno]} <> {} goto exit.set
errno	setstr	{none}

exit.set maclab
[.lab]	dc.b	[type],sbe.[errno]
	endm

optn	macro	type,mini,maxi,subi,next
	ifstr	{[subi]} <> {} goto optmax
subi	setstr	{null}

optmax	maclab
	ifstr	{[maxi]} <> {} goto optset
maxi	setstr	{[mini]}

optset	maclab
[.lab]	dc.b	[type],tki.[subi],tki.[mini],tki.[maxi]
	dc.w	[next]-*
	endm
;******************************************************************************
;+++
; Main Syntax Graph
;---
sb_graph
	option	def,,,def		 ; DEF
	option	dprc,,,dprc		 ; DEF PROC
	option	dfn,,,dfn		 ; DEF FN

	option	when,,,when		 ; WHEN

sb_statement
	option	rem,,,text		 ; REM

	option	name,,,assign		 ; assignment
	option	name,,,proc		 ; procedure call

	option	end,,,end		 ; END is fairly common#

	option	if,,,if 		 ; IF
	option	else,,,sb_statement	 ; ELSE starts again

	option	sel,,,select		 ; SELect
	option	on,,,on 		 ; ON
	option	lequ,,,on_rmdr

	option	eif,ewhn,,ends		 ; ENDIF, ENDSEL, ENDWHEN

	option	for,,,for		 ; FOR
	option	rep,,,end_nam		 ; REPeat

	option	next,exit,,end_nam	 ; NEXT, EXIT
	option	efor,erep,,end_nam	 ; ENDFOR, ENDREP

	option	dim,,,dim		 ; DIM
	option	loc,,,loc		 ; LOCal

	option	ret,,,ovalue		 ; RETurn
	option	edef,,,end_opt		 ; ENDDEF

	option	goto,gsub,,value	 ; GOTO or GOSUB
	option	go,,,go 		 ; GO
	option	on,,,on_go		 ; a different type of ON

	option	data,,,vlist		 ; DATA

	option	rest,,,ovalue		 ; RESTORE

	option	let,,,let		 ; LET

ends	nret	coln,,,sb_statement	 ; start again
endl	nret	eol,,,ok		 ; finished
	errexit 			 ; non-specific error

vlist	graph	expr,vsep		 ; value list at end of statement
	errexit
vsep	option	cmma,,,vlist
	jump	ends

value	graph	expr,ends		 ; value at end of statement
	errexit

ovalue	graph	expr,ends		 ; optional value at end of statement
	jump	ends

ok	exit				 ; always need an OK

;******************************************************************************

; REM statement

text	code	sb_patxt,endl		 ; REM is the rest of the line


;******************************************************************************

; Assignments and procedure calls (statement starts with name)

; Assignment

let	nret	name,,,assign		 ; LET name
	errexit

assign	option	lpar,,,ass_ind		 ; name(
ass_equ option	lequ,,,value		 ; name =   or name(...) =
	errexit

ass_ind graph	in_rnge,ass_equ 	 ; name(...)
	errexit

; Procedure call

proc	graph	exlist,ends
	exit

;******************************************************************************

; END anything

end	option	if,when,,ends		 ; END IF, END SEL, END WHEN
	option	for,rep,,end_nam	 ; END FOR, END REP
	option	def,,,end_opt		 ; END DEF
	errexit

end_nam option	name,,,ends		 ; must be name
	jump	ends			 ; ... unless it is SBASIC
;;;	   errexit

end_opt option	name,,,ends		 ; might be name
	jump	ends			 ; ... but it does not matter


;******************************************************************************

; IF

if	graph	expr,if_then		 ; should be expression
	errexit

if_then option	then,,,sb_statement	 ; then, start again
	jump	ends			 ; or end of statement


;******************************************************************************

; SELect

select	option	on,,,sel_on		 ; ON is optional
sel_on	option	name,,,sel_equ
	graph	expr,ends

	errexit

sel_equ option	lequ,,,on_rnge		 ; = is followed by range
	jump	ends

on	option	name,,,on_equ		 ; ON followed by name
	errexit
on_equ	option	lequ,,,on_rmdr		 ; must be = then remainder or range
	errexit

on_rmdr option	rmdr,,,ends		 ; remainder is all
on_rnge graph	expr,orng_to		 ; value
	errexit
orng_to option	to,,,orng_up		 ; to
	jump	orng_nx
orng_up graph	expr,orng_nx		 ; value
	errexit
orng_nx option	cmma,,,on_rnge		 ; comma
	jump	ends			 ; or end of statement

;******************************************************************************

; FOR and REPeat

for	option	name,,,for_equ		 ; name
	errexit
for_equ option	lequ,,,for_rng		 ; =
	errexit

for_rng graph	expr,frng_to		 ; value
	errexit
frng_to option	to,,to,frng_up		 ; to
	jump	frng_nx
frng_up graph	expr,frng_st		 ; value
	errexit
frng_st option	step,,,frng_in		 ; step
	jump	frng_nx
frng_in graph	expr,frng_nx		 ; value
	errexit
frng_nx option	cmma,,,for_rng		 ; comma
	jump	ends			 ; or end of statement


;******************************************************************************

; DIM

dim	option	name,,,dim_name 	 ; name
	errexit
dim_name code	sb_dmname,dim_par	 ; set type and check for parenth
	errexit
dim_par option	lpar,,,dim_ind		 ; (
	errexit
dim_ind graph	expr,dim_sep		 ; value
	errexit
dim_sep option	rpar,,,dim_nx		 ; )
	option	cmma,,,dim_ind		 ; , value
	errexit
dim_nx	option	cmma,,,dim		 ; , another name
	jump	ends			 ; or end of statement

;******************************************************************************

; LOCal

loc	option	name,,,loc_par		 ; name
	errexit
loc_par option	lpar,,,loc_ind		 ; (
loc_nx	option	cmma,,,loc		 ; , another name
	jump	ends			 ; or end of statement

loc_ind graph	expr,loc_sep		 ; value
	errexit
loc_sep option	rpar,,,loc_nx		 ; )
	option	cmma,,,loc_ind		 ; , value
	errexit


;******************************************************************************

; GO TO and GO SUB

go	option	to,sub,,value		 ; GO TO and GO SUB
	errexit

on_go	graph	expr,og_go		 ; control value
	errexit
og_go	option	goto,gsub,,vlist	 ; GOTO GOSUB value list
	option	go,,,og_type
	errexit
og_type option	to,sub,,vlist
	errexit

;******************************************************************************

; Procedure and function definition

def	option	proc,,,dprc		 ; DEF PROC
	option	fun,fn,fn,dfn		 ; DEF FN
	errexit bdef

dprc	option	name,,,df_prname	 ; should be name
	errexit bdef

dfn	option	name,,,df_fnname	 ; should be name
	errexit bdef

df_prname code	 sb_prname,df_pars	 ; setup procedure name
	errexit

df_fnname code	 sb_fnname,df_pars	 ; setup function name
	errexit

df_pars option	lpar,,,df_plist 	 ; parameter list?
	jump	ends			 ; ... no

df_plist option name,,,df_psep		 ; next is name
	errexit bdef

df_psep option	cmma,,,df_plist 	 ; , name
	option	rpar,,,ends		 ; done
	errexit bdef

;******************************************************************************

; WHEN

when	option	err,,,ends		 ; when error is all that is implemented
	errexit

;******************************************************************************

;+++
; index list(s)
;---
inlist	option	lpar,,,in_rnge		 ; indices start with parenth
	exit				 ; not an index

in_rnge graph	expr,in_to		 ; index or low range
in_to	option	to,,sto,in_urng 	 ; to becomes separator
in_sep	option	cmma,,scom,in_rnge	 ; comma becomes separator, then range
	option	rpar,,,ok		 ; there must be right parenth
;***	    option  rpar,,,inlist	     ; (..) could be followed by another
	errexit rpar

in_urng graph	expr,in_sep		 ; upper end of range
	jump	in_sep			 ; go to seperator anyway

;+++
; index/function parameter list
;---
iplist	option	lpar,,,ip_prms		 ; function params start with parenth
	errexit 			 ; ... not parameter list

ip_prms graph	exlist,ip_rpar		 ; then we have optional params
ip_rpar option	rpar,,,ok		 ; there must be right parenthesis
	errexit rpar

;+++
; expression list
;---
exlist	option	hash,,,exl_exp		 ; optional hash
	graph	expr,exl_sep		 ; optional expression

exl_sep option	cmma,,scom,exlist	 ; change comma
	option	to,,sto,exlist		 ; change TO
	option	scol,excl,,exlist	 ; any other separator
	exit				 ; or give up

exl_exp graph	expr,exl_sep		 ; hash must be followed by expression
	errexit expr

;+++
; Expression Graph
;---
sb_expgr
expr	option	plus,,pos,expr_v	 ; monadic ops
	option	minu,,neg,expr_v
expr_m
	option	bnot,not,,expr_v

expr_v	option	lpar,,,expr_ex		 ; left par starts an expression
	option	quot,apst,,expr_ps	 ; string can take post op
	option	name,,,expr_lp		 ; function can take parameters
	option	val,,,expr_ps		 ; value can take post op!
	errexit expr			 ; must be error in expression

expr_lp option	lpar,,,expr_pl		 ; function/array takes params/indices?
	jump	expr_op 		 ; ... no, operator next

expr_ps option	lpar,,,expr_in		 ; value so far can be indexed

expr_op option	plus,minu,,expr_m	 ; do not allow --, +-, -+ or ++
	option	mulf,inst,,expr 	 ; *- etc are OK
	option	lequ,,eq,expr		 ; assignment becomes operator
	exit	expr			 ; not recognisable, might be OK

; expression bits

expr_ex graph	expr,expr_rp		 ; expression then right par
	errexit expr

expr_rp option	rpar,,,expr_ps		 ; after sub expr we can have post op
	errexit rpar

expr_in graph	expr,expi_to		 ; index or low range
	option	to,,sto,expi_ur 	 ; to becomes separator
	errexit
expi_to option	to,,sto,expi_ur 	 ; to becomes separator
expi_rp option	rpar,,,expr_ps		 ; there must be right parenth then po
	errexit rpar
expi_ur graph	expr,expi_rp		 ; high range

expr_pl graph	ip_prms,expr_ps 	 ; index/parameter list then post op
	errexit

;+++
; Name character table - +ve is letter or US, NZ is permitted in name
;---
sbp_nchr
  dc.b	$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
  dc.b	$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
  dc.b	$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$ae,$00
  dc.b	$80,$81,$82,$83,$84,$85,$86,$87,$88,$89,$00,$00,$00,$00,$00,$00

  dc.b	$00,$01,$02,$03,$04,$05,$06,$07,$08,$09,$0a,$0b,$0c,$0d,$0e,$0f
  dc.b	$10,$11,$12,$13,$14,$15,$16,$17,$18,$19,$1a,$00,$00,$00,$00,$5f
  dc.b	$00,$01,$02,$03,$04,$05,$06,$07,$08,$09,$0a,$0b,$0c,$0d,$0e,$0f
  dc.b	$10,$11,$12,$13,$14,$15,$16,$17,$18,$19,$1a,$00,$00,$00,$00,$00

  dc.b	$20,$21,$22,$23,$24,$25,$26,$27,$28,$29,$2a,$2b,$2c,$2d,$2e,$2f
  dc.b	$30,$31,$32,$33,$34,$35,$36,$37,$38,$39,$3a,$3b,$3c,$00,$00,$00
  dc.b	$20,$21,$22,$23,$24,$25,$26,$27,$28,$29,$2a,$2b,$4c,$4d,$4e,$4f
  dc.b	$50,$51,$52,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00

  dc.b	$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
  dc.b	$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
  dc.b	$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00
  dc.b	$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00,$00

	end
