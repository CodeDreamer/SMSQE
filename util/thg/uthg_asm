* Use a Thing				v0.01   Feb 1988  J.R.Oakley  QJUMP
*
	section thing
*
	include 'dev8_mac_assert'
	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_thg'
*
	xref	th_chkjb
	xref	th_find
	xref	th_addu
	xref	th_exitd3
*
	xdef	th_uthg
*+++
* Find a thing, given its name, mark it as used by the job given, and
* return the thing's address.
*
*	Registers:
*		Entry				Exit
*	D0					0, ITNF, IJOB
*	D1	Job ID or -1			Job ID
*	D2					result
*	D3					version
*	A0	name of thing (>=1 chars)	preserved
*	A1					pointer to Thing
*	A2					pointer to linkage or returned
*	A2/A3					smashed
*	A6	pointer to system variables	preserved
*---
th_uthg
	jsr	th_find(pc)		 ; find the Thing
	bne.s	thu_exit		 ; ...oops
	jsr	th_chkjb(pc)		 ; is the Job ID OK?
	bne.s	thu_exit		 ; no, bother
	move.l	a1,a2			 ; keep base of linkage block
	move.l	th_thing(a1),a1 	 ; point to thing itself
	btst	#tht..lst,thh_type(a1)	 ; list of things?
	beq.s	thu_ok			 ; ... no
	tst.l	d2			 ; any id given?
	beq.s	thu_ok			 ; ... no
thh_lkid
	cmp.l	thh_exid(a1),d2 	 ; this ID?
	beq.s	thu_ok			 ; ... yes
	move.l	thh_next(a1),d0 	 ; ... no, try the next
	add.l	d0,a1			 ; set next address
	bne.s	thh_lkid
	moveq	#err.nimp,d0
	bra.s	thu_exit

thu_ok
	move.l	a1,-(sp)		 ; save thing pointer
	move.l	a2,a1			 ; linkake
	jsr	th_addu(pc)		 ; add Job to list of those using Thing
	move.l	th_verid(a1),d3 	 ; return version ID
	move.l	(sp)+,a1		 ; restore thing pointer
thu_exit
	jmp	th_exitd3(pc)
*
	end
