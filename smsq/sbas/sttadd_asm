; SBAS_STTADD - Statement Number to Address and VV  V2.00     1994 Tony Tebby

	section sbas

	xdef	sb_sttadd
	xdef	sb_addstt
	xdef	sb_sttaddx
	xdef	sb_addsttx
	xdef	sb_dstadd
	xdef	sb_adddst

	include 'dev8_keys_sbasic'
	include 'dev8_mac_assert'

;+++
; SBASIC Convert Data Statement to Address.
;
;	d0 cr	data line, statement, item number / address
;	d1    s
;	d2    s
;	d3    s
;	a2    s
;	a6 c  p pointer to SuperBASIC variables
;
;	d0 and status returned -ve if no statement (or off end)
;
;---
sb_dstadd
	move.l	sb_dttbb(a6),d2 	 ; data statememt table
	ble.s	sbsa_nst
	move.l	d2,a2
	move.l	sb_dttbp(a6),d2
	lsr.w	#8,d0			 ; shift out item number
	bgt.s	sbsa_do 		 ; non zero
	addq.w	#1,d0			 ; ensure that we avoid 0
	bra.s	sbsa_do

sbsa_nst
	moveq	#-1,d0			 ; flag no statement (no sentinal)
	rts
;+++
; SBASIC Convert Statement to Address (might be command line or program)
; If the statement is in the program, and there is no program table, D0 and
; status are returned 0.
;
;	d0 cr	statement number / address
;	d1    s
;	d2    s
;	d3    s
;	a2    s
;	a6 c  p pointer to SuperBASIC variables
;
;	d0 and status returned -ve if no statement (or off end)
;	d0 and status returned 0 if in program and no program table
;---
sb_sttaddx
	move.l	sb_sttbb(a6),a2 	 ; statememt table
	move.l	sb_sttbp(a6),d2
	move.l	d0,d3
	swap	d3
	tst.w	d3
	ble.s	sb_sttaddc		 ; command line
	move.l	a2,d3			 ; any table?
	bgt.s	sbsa_do 		 ; yes, real line number in program
	moveq	#0,d0
	rts
;+++
; SBASIC Convert Statement to Address.
;
;	d0 cr	statement number / address
;	d1    s
;	d2    s
;	d3    s
;	a2    s
;	a6 c  p pointer to SuperBASIC variables
;
;	d0 and status returned -ve if no statement (or off end)
;---
sb_sttadd
	move.l	sb_sttbb(a6),a2 	 ; statememt table
	move.l	sb_sttbp(a6),d2
	tst.b	sb_cmdl(a6)		 ; command line?
	beq.s	sbsa_do 		 ; ... no

sb_sttaddc
	move.l	sb_cstbb(a6),a2 	 ; command line statememt table
	move.l	sb_cstbp(a6),d2

sbsa_do
	sub.l	a2,d2			 ; size of (ignoring top sentinal)
	ble.s	sbsa_nst
	assert	st.len,8
	moveq	#$fffffff8,d3		 ; mask of table addresses

ssa_low
	move.l	d2,d1			 ; save old step size
	lsr.l	#1,d2			 ; new step size
	and.l	d3,d2			 ; in 8 byte units
	beq.s	ssa_next		 ; must be next
	assert	st_line,st_stmt-2
	cmp.l	st_line(a2,d2.l),d0	 ; found it yet?
	blo.s	ssa_low 		 ; ... no, below
	beq.s	ssa_this		 ; ... yes

ssa_high
	add.l	d2,a2			 ; upper half required
	sub.l	d2,d1			 ; size of upper half
	move.l	d1,d2
	lsr.l	#1,d2			 ; new step size
	and.l	d3,d2			 ; in 8 byte units
	beq.s	ssa_next		 ; must be next
	assert	st_line,st_stmt-2
	cmp.l	st_line(a2,d2.l),d0	 ; found it yet?
	bhi.s	ssa_high		 ; ... no, above
	blo.s	ssa_low 		 ; ... no, below

ssa_this
	move.l	st_addr(a2,d2.l),d0	 ; line / statement found
	rts

ssa_next
	move.l	st_addr+st.len(a2,d2.l),d0 ; next statement
	rts


;+++
; SBASIC Convert Data Address to Statement.
;
;	d0 cr	address / statement number
;	d1    s
;	d2    s
;	d3    s
;	a2  r	pointer to start of statement
;	a6 c  p pointer to SuperBASIC variables
;
;---
sb_adddst
	move.l	sb_dttbb(a6),a2 	 ; data statememt table
	move.l	sb_dttbp(a6),d2
	bsr.s	sbas_do
	lsl.w	#8,d0			 ; shift up statement number
	rts

;+++
; SBASIC Convert Address to Statement (might be program or command line)
; If the statement is in the program, and there is no program table, D0 and
; status are returned 0.
;
;	d0 cr	address / statement number
;	d1    s
;	d2    s
;	d3    s
;	a2  r	pointer to start of statement
;	a6 c  p pointer to SuperBASIC variables
;
;	d0 and status returned 0 if in program and no program table
;
;---
sb_addsttx
	move.l	sb_sttbb(a6),a2 	 ; statememt table
	move.l	sb_sttbp(a6),d2
	move.l	a2,d3
	beq.s	sb_addsttc		 ; no statement table, must be command
	cmp.l	sb_progb(a6),d0 	 ; address in program?
	blt.s	sb_addsttc		 ; ... no
	cmp.l	sb_progp(a6),d0 	 ; address in program?
	bge.s	sb_addsttc		 ; ... no
	move.l	a2,d3			 ; any table?
	bgt.s	sbas_do 		 ; yes, real statement in program
	moveq	#0,d0
	rts

;+++
; SBASIC Convert Address to Statement.
;
;	d0 cr	address / statement number
;	d1    s
;	d2    s
;	d3    s
;	a2  r	pointer to start of statement
;	a6 c  p pointer to SuperBASIC variables
;
;---
sb_addstt
	move.l	sb_sttbb(a6),a2 	 ; statememt table
	move.l	sb_sttbp(a6),d2
	tst.b	sb_cmdl(a6)		 ; command line?
	beq.s	sbas_do 		 ; ... no

sb_addsttc
	move.l	sb_cstbb(a6),a2 	 ; command line statememt table
	move.l	sb_cstbp(a6),d2

sbas_do
	sub.l	a2,d2			 ; size of (ignoring top sentinal)
	assert	st.len,8
	moveq	#$fffffff8,d3		 ; mask of table addresses

sas_low
	move.l	d2,d1			 ; save old step size
	lsr.l	#1,d2			 ; new step size
	and.l	d3,d2			 ; in 8 byte units
	beq.s	sas_this		 ; must be this
	cmp.l	st_addr(a2,d2.l),d0	 ; found it yet?
	blo.s	sas_low 		 ; ... no, below
	beq.s	sas_this		 ; ... yes

sas_high
	add.l	d2,a2			 ; upper half required
	sub.l	d2,d1			 ; size of upper half
	move.l	d1,d2
	lsr.l	#1,d2			 ; new step size
	and.l	d3,d2			 ; in 8 byte units
	beq.s	sas_this		 ; must be this
	cmp.l	st_addr(a2,d2.l),d0	 ; found it yet?
	bhi.s	sas_high		 ; ... no, above
	blo.s	sas_low 		 ; ... no, below

sas_this
	move.l	st_line(a2,d2.l),d0	 ; line / statement found
	move.l	st_addr(a2,d2.l),a2	 ; address of start of statement
	rts

	end
