; SBAS_CLEAR - Clear Data Areas 	   V2.00       1993   Tony Tebby

	section sbas

	xdef	sb_clrprm		 ; clear parameter frame
	xdef	sb_clrstk		 ; clear just return and arith stack
	xdef	sb_clrcmp		 ; clear compiled bits
	xdef	sb_clrdat		 ; clear data, and stacks
	xdef	sb_clrall		 ; clear program, data and stacks
	xdef	sb_clrval		 ; clear value

	xdef	sb_clnret		 ; clean up unused entres on ret stack

	xdef	sb_inallc		 ; initial allocation
	xdef	sb_inchan		 ; initialise channel table entry
	xdef	sb_setvar		 ; set to variable and preset
	xdef	sb_preset		 ; preset value

	xref	sb_wermess

	xref	sb_redat
	xref	sb_rewrk
	xref	sb_resnt
	xref	sb_resnl
	xref	sb_aldat8
	xref	sb_panic

	xref	gu_rchp
	xref	gu_achp0
	xref	gu_fclos
	xref	gu_clra

	include 'dev8_keys_sbasic'
	include 'dev8_keys_sys'
	include 'dev8_keys_err4'
	include 'dev8_keys_qdos_sms'
	include 'dev8_mac_assert'


sbc_check
	move.l	(a1),d0 		 ; base of buffer
	bmi.s	sbc_rel 		 ; not in stub, release it
	cmp.l	#sb.prstl,d0		 ; below stack?
	bhi.s	sbc_rel 		 ; ... no, release it
	addq.l	#8,a1			 ; next
	rts

sbc_reld
	move.l	(a1),a0
	lea	-sb.dnspr(a6,a0.l),a0	 ; true base to release
	bra.s	sbc_reln

sbc_rel
	move.l	(a1),a0
	add.l	a6,a0			 ; true base to release
sbc_reln
	addq.l	#8,a1			 ; next
	jmp	gu_rchp

;+++
; Clear all SBASIC areas
;---
sb_clrall
	clr.l	sb_wherr(a6)		 ; no when error
	clr.w	sb_cline(a6)		 ; or continue line

	lea	sb_buffb(a6),a1
	bsr.s	sbc_check		 ; only release buffer if not in stub
	addq.l	#sb_srceb-sb_cmdlb,a1
	bsr.s	sbc_rel 		 ; release source
	bsr.s	sbc_rel 		 ; release name table
	bsr.s	sbc_rel 		 ; release name list
	addq.l	#sb_chanb-sb_nmlsb-8,a1
	move.l	(a1),a2
	moveq	#-1,d1
	moveq	#2,d0
sbc_svchn
	move.l	ch_chid(a6,a2.l),-(sp)	 ; save channels 0-2
	cmp.l	sb_chanp(a6),a2
	blt.s	sbc_svcnext
	move.l	d1,(sp) 		 ; mark closed
sbc_svcnext
	add.w	#ch.len,a2
	dbra	d0,sbc_svchn

sbc_clchn
	cmp.l	sb_chanp(a6),a2 	 ; all channels closed?
	bge.s	sbc_rechn
	add.w	#ch.len,a2
	move.l	ch_chid-ch.len(a6,a2.l),a0 ; next channel
	move.w	a0,d0
	bmi.s	sbc_clchn		 ; closed
	jsr	gu_fclos
	bra.s	sbc_clchn

sbc_rechn
	bsr.s	sbc_rel 		 ; release channel table
	bsr.s	sbc_rel 		 ; and return stack
	add.w	#sb_backl-sb_retsb-8,a1
	bsr.s	sbc_reld		 ; backtrack stack
	bsr.s	sbc_reld		 ; graph stack
	bsr.s	sbc_reld		 ; arithmetic stack

	bsr.s	sbc_clrcmp		 ; release compiled bits

	bsr.s	sbc_cldat		 ; clear data area

; re-initialise all areas

	bsr.l	sb_inallc

; restore the channel table

	move.l	sb_chanb(a6),a2
	move.l	8(sp),a0
	bsr.s	sbc_inchan		 ; init chan 0
	move.l	4(sp),a0
	bsr.s	sbc_inchan		 ; init chan 1
	move.l	(sp)+,a0
	bsr.s	sbc_inchan		 ; init chan 2
	move.l	a2,sb_chanp(a6)
	addq.l	#8,sp

	moveq	#sms.info,d0
	trap	#do.sms2
	tst.l	d1			 ; job 0?
	beq.s	sbc_inname		 ; ... yes, copy all names!!!!!
	rts

sbc_inchan
	jsr	gu_clra 		 ; clear window

;+++
; Initialise channel table entry (a6,a2) with channel a0
;
;	d0  s
;	a0 c  p channel ID
;	a2 c  u channel table entry, updated to next
;---
sb_inchan
	add.l	a6,a2
	assert	ch_chid,0
	move.l	a0,(a2)+		 ; set channel ID
	moveq	#ch.len/4-2,d0		 ; ... no need to clear first one
sbi_clch
	clr.l	(a2)+
	dbra	d0,sbi_clch

	move.w	#$80,ch_width-ch.len(a2) ; set width
	sub.l	a6,a2
sbc_rts
	rts

;+++
; Clear out data area
;---
sbc_cldat
	move.l	sb_datal(a6),a1 	 ; release all data area blocks
	clr.l	sb_datal(a6)
sbc_rdat
	move.l	a1,d0			 ; anything to go?
	beq.s	sbc_rts 		 ; ... no
	lea	dt_bbase(a1),a0
	assert	dt_bnext,-4
	move.l	-(a1),a1		 ; save next
	jsr	gu_rchp 		 ; release
	bra.s	sbc_rdat

;+++
; Clear out compilation bits
;---
sb_clrcmp
sbc_clrcmp
	st	sb_edt(a6)		 ; ... must re-compile
	lea	sb_ptokb(a6),a1
	jsr	sb_rewrk		 ; release program tokens
	lea	sb_sttbb(a6),a1
	jsr	sb_rewrk		 ; release statement table
	lea	sb_progb(a6),a1
	jsr	sb_rewrk		 ; release program
	lea	sb_dttbb(a6),a1
	jsr	sb_rewrk		 ; release data statement table
	lea	sb_dtstb(a6),a1
	jmp	sb_rewrk		 ; release data statements


;+++
; Copy all names from global name table
;---
sbc_inname
	move.w	sr,d7
	trap	#0

	move.l	sys_sbab(a0),a5
	lea	sb_offs(a5),a5		 ; SuperBASIC stub

	move.l	sb_nmtbp(a5),d1
	move.l	d1,a3
	move.l	sb_nmtbb(a5),a2 	 ; pointer to global name table
	sub.l	a2,d1			 ; name table room required
	beq.s	sxc_nmend
	jsr	sb_resnt

	add.l	a5,a2			 ; abs pointer
	add.l	a5,a3			 ; abs top
	move.l	sb_nmtbb(a6),a1 	 ; pointer to our name table
	add.l	a6,a1
sxc_ntloop
	move.l	(a2)+,(a1)+		 ; copy name table
	cmp.l	a3,a2
	blt.s	sxc_ntloop

	sub.l	a6,a1
	move.l	a1,sb_nmtbp(a6)

	move.l	sb_nmlsp(a5),d1
	move.l	d1,a3
	move.l	sb_nmlsb(a5),a2 	 ; pointer to global name list
	sub.l	a2,d1			 ; name table room required
	jsr	sb_resnl

	add.l	a5,a2			 ; abs pointer
	add.l	a5,a3			 ; abs top
	move.l	sb_nmlsb(a6),a1 	 ; pointer to our name list
	add.l	a6,a1
sxc_nlloop
	move.b	(a2)+,(a1)+		 copy name list
	cmp.l	a3,a2
	blt.s	sxc_nlloop

	sub.l	a6,a1
	move.l	a1,sb_nmlsp(a6)


sxc_nmend
	move.w	d7,sr			 ; back to normal
	rts

;+++
; Set initial SBASIC allocations
;---
sb_inallc
	lea	sb_buffb(a6),a1
	move.w	#sb.buffb,a0		 ; buffer base
	move.l	a0,(a1)+
	move.l	a0,(a1)+
	add.w	#sb.buffs,a0		 ; command line base
	move.l	a0,(a1)+
	move.l	a0,(a1)+
	move.l	a0,sb_bufft(a6) 	 ; true buffer top
	add.w	#sb.buffs,a0
	move.l	a0,sb_cmdlt(a6)

	bsr.s	sbi_alup		 ; allocate source
	bsr.s	sbi_alup		 ; allocate name table
	bsr.s	sbi_alup		 ; name list

	bsr.s	sbc_aldata1

	move.l	#ch.len*32,d1
	bsr.s	sbi_ald1		 ; allocate channel table
	sub.l	d1,a0
	add.l	a6,a0
	not.l	ch_chid(a0)		 ; preset #0,#1,#2
	not.l	ch_chid+ch.len(a0)
	not.l	ch_chid+ch.len*2(a0)
	bsr.s	sbi_alup		 ; allocate return stack
	addq.l	#8,a1			 ; old line number

	bsr.s	sbi_aldn		 ; allocate backtrack stack
	bsr.l	sbi_aldn		 ; allocate graph stack
;**	bsr.s	sbi_aldn		 ; allocate arithmentic stack

sbi_aldn
	moveq	#sb.alini/2,d1		 ; allocation of initial blocks
	add.w	d1,d1
	move.l	d1,d0
	jsr	gu_achp0
	bne.l	sb_panic
	sub.l	a6,a0			 ; relative address
	add.l	#sb.dnspr,a0		 ; a bit of spare at the limit
	move.l	a0,sb.loffp(a1) 	 ; limit
	add.l	d1,a0
	sub.w	#sb.dnspr*2,a0		 ; and some spare at top
	move.l	a0,(a1)+
	move.l	a0,(a1)+		 ; set base and running pointers
	rts

sbi_alup
	moveq	#sb.alini/2,d1		 ; allocation of initial blocks
	add.w	d1,d1
sbi_ald1
	move.l	d1,d0
	jsr	gu_achp0
	bne.l	sb_panic
	sub.l	a6,a0			 ; relative address
	move.l	a0,(a1)+
	move.l	a0,(a1)+		 ; set base and running pointers
	add.l	d1,a0
	move.l	a0,sb.toffp-4(a1)	 ; and top
	rts

sbc_aldat
	lea	sb_datab(a6),a1 	 ; where to put pointers
sbc_aldata1
	move.l	#sb.aldat,d0
	move.l	d0,d1
	jsr	gu_achp0		 ; allocate data area
	bne.l	sb_panic

	assert	dt_bbase,dt_blen,dt_bnext-4,dt_data-8
	subq.l	#dt_data-dt_bbase,d1
	move.l	d1,(a0)+		 ; (useable) length of block
	addq.l	#4,a0
	move.l	a0,sb_datal(a6)
	move.l	d1,(a0) 		 ; first free space
	sub.w	#sb_frdat-dt_nfree,a0
	sub.l	a6,a0
	move.l	a0,sb_frdat(a6) 	 ; free space pointer

	sub.l	a6,d0
	move.l	d0,(a1)+		 ; base (=-(a6))
	add.l	d0,d1
	move.l	d1,(a1)+		 ; top
	rts

;+++
; Clear data area
; All regs except d0 preserved
;---
sb_clrdat
scd.reg reg	d1/a0/a2/a3
	movem.l scd.reg,-(sp)
	bsr.l	sb_clrstk		 ; clear up stacks
	bsr.l	sbc_cldat		 ; throw away old data

	bsr.s	sbc_aldat		 ; allocate initial data area

	move.l	sb_nmtbb(a6),a3
	bra.s	scd_celp

scd_cloop
	bsr.s	sb_preset

	addq.l	#nt.len,a3
scd_celp
	cmp.l	sb_nmtbp(a6),a3
	blt.s	scd_cloop

	movem.l (sp)+,scd.reg
	rts

;+++
; Set name (a6,a3) (cleared) to variable and preset value
;---
sb_setvar
	movem.l d0/d1/a0/a2,-(sp)
	moveq	#0,d0
	move.w	nt_name(a6,a3.l),d0	 ; name
	add.l	sb_nmlsb(a6),d0
	moveq	#0,d2
	move.b	(a6,d0.l),d2		 ; length of name
	add.l	d2,d0
	move.b	(a6,d0.l),d0		 ; last character
	moveq	#nt.fp,d2		 ; assume fp in d2.l
	sub.b	#'$',d0 		 ; string?
	beq.s	sbsv_str		 ; ... yes
	subq.b	#'%'-'$',d0		 ; integer?
	bne.s	sbsv_do
sbsv_int
	moveq	#nt.in,d2		 ; integer in d2.l
	bra.s	sbsv_do

sbsv_str
	moveq	#nt.st,d2		 ; string in d2.l

sbsv_do
	move.b	d2,nt_vtype(a6,a3.l)	 ; set type
	bsr.s	sbpr_do
	movem.l (sp)+,d0/d1/a0/a2
	rts

;+++
; Preset value for name (a6,a3)
;	d0/d1/a0/a2 smashed
;---
sb_preset
	move.b	nt_nvalp(a6,a3.l),d1
	move.w	#1<<nt.unset+1<<nt.var+1<<nt.arr+1<<nt.rep+1<<nt.for,d0
	btst	d1,d0			 ; variable type?
	beq.s	spr_rts
	tst.w	nt_usetp(a6,a3.l)	 ; null?
	beq.s	spr_rts
sbpr_do
	move.b	#nt.unset,nt_nvalp(a6,a3.l) ; set to unset var

	jsr	sb_aldat8		 ; initial allocation

	move.l	a0,nt_value(a6,a3.l)	 ; normal value

	cmp.b	#nt.fp,nt_vtype(a6,a3.l) ; type?
	blt.s	spr_str
	bgt.s	spr_int

	clr.l	(a0)+			 ; zero float
	move.l	#dt.flfp,(a0)		 ; and flags
	rts

spr_int
	clr.w	(a0)+			 ; zero int
	move.l	#dt.flint,(a0)		 ; and flags
	rts

spr_str
	move.l	#$000800ff,(a0)+	 ; null string flags
	move.l	a0,nt_value(a6,a3.l)
	clr.l	(a0)
spr_rts
	rts

;+++
; Unravel return stack and clear arithmetic stack and clear when error
; All regs except d0 preserved
;---
sb_clrstk
	clr.l	sb_wherr(a6)		 ; no when error
	move.l	sb_arthb(a6),sb_arthp(a6) ; no arithmetic stack now
	move.l	sb_retsp(a6),d0 	 ; return stack
	cmp.l	sb_retsb(a6),d0 	 ; any?
	beq.s	scr_rts 		 ; ... no

scr.reg reg	d1/d2/d3/d4/d5/d7/a0/a1/a2/a3/a4/a5
	movem.l scr.reg,-(sp)
	move.l	#err4.pfcl,d0
	jsr	sb_wermess
	moveq	#0,d7			 ; remove all
	bra.s	scr_do

;+++
; Unravel partially set up entries on return stack.
; All regs except d0 preserved
;---
sb_clnret
	move.l	sb_retsp(a6),d0 	 ; return stack
	cmp.l	sb_retsb(a6),d0 	 ; any?
	beq.s	scr_rts 		 ; ... no

	movem.l scr.reg,-(sp)
	moveq	#-1,d7			 ; remove unset entries from top

scr_do
	move.l	sb_retsp(a6),a1 	 ; return stack

scr_loop
	move.b	rt_type(a6,a1.l),d0	 ; return stack type
	bgt.s	scr_proc		 ; procedure / function frame

	blt.s	scr_rsas		 ; always remove array frame
	tst.b	d7			 ; remove all?
	bne.s	scr_exit		 ; ... no, do not remove goto

scr_rsas
	assert	rt.sasize,rt.gssize
	subq.l	#rt.sasize,a1


scr_next
	cmp.l	sb_retsb(a6),a1 	 ; another frame?
	bgt.s	scr_loop		 ; ... yes

scr_exit
	move.l	a1,sb_retsp(a6) 	 ; frame clear
	movem.l (sp)+,scr.reg
scr_rts
	rts

scr_proc
	move.l	sb_nmtbb(a6),a5

	move.l	rt_def(a6,a1.l),d0	 ; parameters swapped?
	bpl.s	scr_parm		 ; ... no, just clear parameters

	tst.b	d7			 ; clear all
	bne.s	scr_exit		 ; ... no, do not remove swapped frame

	neg.l	d0			 ; address of definition
	move.l	d0,a4			 ; formal parameter definition

	move.l	a5,d2
	move.l	a5,d5
	add.l	rt_local(a6,a1.l),d2	 ; base of locals to clear
	add.l	rt_topn(a6,a1.l),d5	 ; top of locals
	cmp.l	sb_nmtbp(a6),d5 	 ; are parameters at top
	bne.s	scr_lvlend
	move.l	d2,sb_nmtbp(a6) 	 ; ... yes, remove them
	bra.s	scr_lvlend

scr_lvloop
	subq.l	#nt.len,d5
	move.l	a5,a3
	add.w	nt_name(a6,d5.l),a3	 ; local variable
	bsr.l	sb_clrval		 ; clear it
	move.w	nt_usetp(a6,d5.l),nt_usetp(a6,a3.l) ; restore values
	move.l	nt_value(a6,d5.l),nt_value(a6,a3.l)
	clr.w	nt_usetp(a6,d5.l)	  ; scrub entry

scr_lvlend
	cmp.l	d2,d5			 ; bottom of locals yet?
	bgt.s	scr_lvloop		 ; ... no

	move.w	(a4)+,d4		 ; number of formal parameters
	add.w	d4,a4
	add.w	d4,a4			 ; start at the other end
	move.l	d5,d3
	bra.s	scr_swlend

scr_swloop
	subq.l	#nt.len,d3		 ; next pf entry
	move.l	a5,a3
	add.w	-(a4),a3		 ; next formal parameter
	move.w	nt_usetp(a6,d3.l),d0
	move.w	nt_usetp(a6,a3.l),nt_usetp(a6,d3.l)
	move.w	d0,nt_usetp(a6,a3.l)
	move.l	nt_value(a6,d3.l),d0
	move.l	nt_value(a6,a3.l),nt_value(a6,d3.l)
	move.l	d0,nt_value(a6,a3.l)
scr_swlend
	dbra	d4,scr_swloop

scr_parm
	move.l	a5,a3
	add.l	rt_parm(a6,a1.l),a3	 ; base of parameters to clear
	add.l	rt_tparm(a6,a1.l),a5	 ; top of parameters to clear
	bsr.s	sb_clrprm

scr_pnext
	sub.w	#rt.pfsize,a1
	bra.l	scr_next


;+++
; Clear parameter frame
;
;	a3 c  s base of frame rel a6
;	a5 c  s top of frame rel a6
;---
sb_clrprm
	exg	a3,a5
	cmp.l	sb_nmtbp(a6),a3 	 ; are parameters at top
	bne.s	scp_pcend
	move.l	a5,sb_nmtbp(a6) 	 ; ... yes, remove them
	bra.s	scp_pcend

scp_pcloop
	subq.l	#nt.len,a3
	move.l	nt_value(a6,a3.l),d0	 ; any value to return?
	bmi.s	scp_pcnext
	moveq	#0,d2
	move.w	nt_name(a6,a3.l),d2	 ; name parameter?
	bmi.s	scp_pcrval		 ; ... no, release value to data area
	lsl.l	#nt.ishft,d2
	add.l	sb_nmtbb(a6),d2

	cmp.b	#nt.arr,nt_nvalp(a6,d2.l) ; actual param is array?
	beq.s	scp_pcarray		 ; ... yes
	cmp.b	#nt.mcprc,nt_nvalp(a6,d2.l) ; or now an mcproc / fun?
	bge.s	scp_pcnext		 ; ... yes

	move.l	d0,nt_value(a6,d2.l)	 ; reset value of actual parameter
	move.w	#$0f0f,d0
	and.w	nt_usetp(a6,a3.l),d0
	move.w	d0,nt_usetp(a6,d2.l)	  ; reset type (FOR/REP)
	bra.s	scp_pcnext

scp_pcarray
	cmp.b	#nt.arr,nt_nvalp(a6,a3.l) ; parameter is a (sub) array
	bne.s	scp_pcnext		 ; ... no, nothing to release

scp_pcrval
	bsr.s	sb_clrvd0

scp_pcnext
	move.l	#$0000ffff,nt_usetp(a6,a3.l)
scp_pcend
	cmp.l	a5,a3			 ; another parm?
	bgt.s	scp_pcloop		 ; ... yes
scp_rts
	rts

;+++
; Clear value in data area
;
;	d1    s
;	a0    s
;	a3 c  p pointer to name table rel a6
;---
sb_clrval
	move.l	nt_value(a6,a3.l),d0	 ; value to return
	bmi.s	scp_rts
sb_clrvd0
	st	nt_value(a6,a3.l)	 ; neg it
	move.l	d0,a0			 ; this is bit to return
	cmp.b	#nt.arr,nt_nvalp(a6,a3.l) ; var, array or for/rep
	blt.s	scv_var 		 ; var
	bgt.s	scv_forrep

	assert	dt_allc,-4
	move.l	-(a0),d1		 ; length and base
	bne.l	sb_redat
scv_rts
	rts				 ; dummy array!

scv_var
	moveq	#8,d1			 ; assume 8 bytes

	moveq	#$f,d0			 ; parameter type
	and.w	nt_usetp(a6,a3.l),d0
	cmp.w	#nt.st,d0		 ; string
	bgt.l	sb_redat		 ; ... no
	blt.s	scv_rts

	subq.l	#-dt_stalc,a0
	move.w	(a0),d1 		 ; amount to release
	jmp	sb_redat		 ; return data space

scv_forrep
	add.w	#dt_frbase,a0
	move.l	(a0),d1 		 ; allocated size
	bra.l	sb_redat

	end
