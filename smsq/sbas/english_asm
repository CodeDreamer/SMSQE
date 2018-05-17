; SuperBASIC error messages

	section language

	xdef	sbp_errm
	xdef	sb_erset
	xdef	sb_ermess
	xdef	sb_ernimp

	xdef	met_pfcl
	xdef	met_atln

	include 'dev8_keys_err'

emess	macro	 text
	xdef	[.lab]
[.lab]	dc.w	[.len(text)]+1
	dc.b   '[text]',$a
	ds.w   0
	endm

;+++
; Table of SuperBASIC error messages
;---
sbp_errm
	dc.w	0
	dc.w	sbe_iexp-*
	dc.w	sbe_lpar-*
	dc.w	sbe_rpar-*
	dc.w	sbe_lno-*
	dc.w	sbe_bstr-*
	dc.w	sbe_edef-*
	dc.w	sbe_idef-*

sbe_iexp emess	{syntax error in expression}
sbe_lpar emess	{missing left parenthesis}
sbe_rpar emess	{missing right parenthesis}
sbe_lno  emess	{error in line number}
sbe_bstr emess	{bad string: missing delimiter}
sbe_edef emess	{incorrect procedure or function definition}
sbe_idef emess	{procedure or function definition not allowed here}

sbe_bdef emess	{DEFines may not be within other clauses}
sbe_bedf emess	{misplaced END DEFine}
sbe_bloc emess	{misplaced LOCal}
sbe_bret emess	{RETurn not in procedure or function}
sbe_rcwh emess	{WHEN clauses may not be nested}
sbe_endw emess	{misplaced END WHEN}
sbe_else emess	{misplaced ELSE}
sbe_endi emess	{misplaced END IF}
sbe_xstr emess	{program structures nested too deeply, my brain aches}
sbe_inif emess	{incomplete IF clause}
sbe_insl emess	{incomplete SELect clause}
sbe_indf emess	{incomplete DEFine}
sbe_inwh emess	{incomplete WHEN clause}
sbe_bfor emess	{unacceptable loop variable}
sbe_unlp emess	{unable to find an open loop}
sbe_blvr emess	{undefined loop control variable}
sbe_bsel emess	{incorrectly structured SELect clause}
sbe_ends emess	{misplaced END SELect}
sbe_dtcl emess	{DATA in command line has no meaning}
sbe_brds emess	{unacceptable parameters for READ}
sbe_recr emess	{SBASIC cannot perform READs within DATA expressions}
sbe_eod  emess	{end of DATA}
sbe_bprc emess	{unknown procedure}
sbe_bref emess	{unknown function or array}
sbe_bdim emess	{only arrays may be dimensioned}
sbe_pdim emess	{procedure and function parameters may not be dimensioned}
sbe_dmng emess	{SBASIC cannot put up with negative dimensions}
sbe_dmov emess	{dimensional overflow - you cannot be serious!}
sbe_eind emess	{error in index list}
sbe_xind emess	{too many indexes}
sbe_asar emess	{cannot assign to sub-array}
sbe_iind emess	{unacceptable array index list}
sbe_inor emess	{array index out of range}
sbe_narr emess	{only arrays or strings may be indexed}
sbe_basg emess	{assignment can only be to a variable or array element}
sbe_mist emess	{MISTake in program}
sbe_wher emess	{during when processing}

sb_ernimp
	moveq	#err.nimp,d0
	rts

sb_ermess
	pea	sbe_ermes
sb_erset
	tas	(sp)
	move.l	(sp)+,d0
	rts

sbe_ermes emess  {fatal error in SBASIC interpreter}

met_pfcl emess	{PROC/FN cleared}
	xdef	met_atln
met_atln dc.w	8,'At line '
	end
