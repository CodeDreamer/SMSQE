; DV3 Link in Driver	   V3.00		 1992 Tony Tebby

	section dv3

	xdef	dv3_link
	xdef	dv3_svec

	xdef	dv3_fdvec

	xref	dv3_name
	xref	dv3_chname

	xref	gu_thuse
	xref	gu_thfre
	xref	gu_thzlk
	xref	gu_achpp
	xref	gu_rchp

	include 'dev8_dv3_keys'
	include 'dev8_keys_iod'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_68000'
	include 'dev8_mac_vec'
	include 'dev8_mac_assert'
	include 'dev8_smsq_smsq_base_keys'

d3l_vec
	move.l	a2,a1
	move.w	(a2)+,d0		 ; is there a vector to set?
	beq.s	d3lv_next		 ; ... no
	add.w	d0,a1			 ; relative
	move.l	a1,(a0) 		 ; set it
d3lv_next
	addq.l	#4,a0
	rts

;+++
; This is the routine that is used to link in a new driver
;
;	a3 cr	pointer to initialisation table / pointer to linkage block
;
;	Status return standard
;	This is a clean routine
;---
dv3_link
d3l.reg reg	d1/d2/d3/a0/a1/a2/a5
	movem.l d3l.reg,-(sp)
	moveq	#iod.sqhd,d0
	add.l	(a3)+,d0
	jsr	gu_achpp		 ; allocate linkage block
	bne.l	d3l_exit		 ; ... oops

	addq.l	#iod.sqhd-8,a0
	assert	iod_sqfb+8,iod_sqio+4,0
	move.l	#1<<iod..ssr+1<<iod..sfi+1<<iod..sdl+1<<iod..scn+1<<iod..sfm+1<<iod..sdd,(a0)+
	move.l	#iod.sqio,(a0)+

; From this point on, there is no checking for errors, except when linking in
; the Thing.

	move.l	a3,a2			 ; save our table pointer
	move.l	a0,a3			 ; this is our return value

	lea	ddl_ddlen(a0),a0
	move.l	(a2)+,(a0)+		 ; ddf allocation
	moveq	#3,d1
	move.w	d1,(a0)+		 ; drive usage
	move.l	(a2),(a0)+
	move.w	d1,(a0)+		 ; drive name
	move.l	(a2)+,(a0)+

	move.l	(a2)+,(a0)+		 ; revision / buff / cylhds / sectl

	move.l	(a2)+,(a0)+		 ; medium type / msect / spare

	lea	ddl_poll(a3),a0
	bsr.s	d3l_vec 		 ; add poll vector
	addq.l	#ddl_sched-ddl_poll-4,a0
	bsr.s	d3l_vec 		 ; add scheduler vector

	move.l	a2,a5
	lea	d3l_nbuf,a2		 ; set unbuffered routines table
	tst.b	ddl_buff(a3)		 ; buffered?
	beq.s	d3l_addr		 ; ... yes, set addresses
	lea	d3l_buff,a2		 ; set buffered routines table
	bset	#iod..ssb,3-(iod..ssb>>8)(a3) ; and facility flag
d3l_addr
	addq.l	#ddl_io-ddl_sched-4,a0
	bsr.s	d3l_vec 		 ; add IO vector
	bsr.s	d3l_vec 		 ; add OPEN vector
	bsr.s	d3l_vec 		 ; add CLOSE vector
	bsr.s	d3l_vec 		 ; add flush vector
	addq.l	#ddl_chname-ddl_fslv-4,a0
	lea	dv3_chname,a1
	move.l	a1,(a0)+		 ; set channel name
	bsr.s	d3l_vec 		 ; add format vector

	lea	d3l_lkvec,a2		 ; default routines
	lea	ddl_check(a3),a0
	moveq	#(dlt_lvec-dlt_check)/2,d1 ; set JMP vectors (including last)
	bsr.l	d3l_svec

; now the Thing!!

	move.l	(a5)+,d0		 ; is there a thing?
	beq.s	d3l_nthg
	lea	-2(a5,d0.w),a1		 ; the thing
	move.l	a1,ddl_thadd(a3)

	moveq	#0,d1
	move.b	ddl_rev(a3),d1		 ; convert version number to chars
	divu	#10,d1
	swap	d1
	lsl.w	#8,d1
	lsr.l	#8,d1
	add.l	#'3.00',d1
	lea	ddl_thverid(a3),a0	 ; version ID
	move.l	d1,(a0)+

	swap	d0			 ; now the name
	lea	-4(a5,d0.w),a1
	move.w	(a1)+,d0
	move.w	d0,(a0)+		 ; name length
	bra.s	d3l_tnle
d3l_tnlp
	move.b	(a1)+,(a0)+
d3l_tnle
	dbra	d0,d3l_tnlp

d3l_nthg
	move.l	(a5)+,d4		 ; the master

; Now preset

d3l_preset
	move.w	(a5)+,d0		 ; next preset block
	beq.s	d3l_link
	lea	(a3,d0.w),a0		 ; where to set
	move.w	(a5)+,d0		 ; how much to set
	bne.s	d3l_prle

	move.w	#jmp.l,(a0)+		 ; set jump
	move.l	a5,a1
	add.w	(a5)+,a1
	move.l	a1,(a0)+
	bra.s	d3l_preset

d3l_prlp
	move.b	(a5)+,(a0)+		 ; set this
d3l_prle
	dbra	d0,d3l_prlp
	bra.s	d3l_preset

; Now that the linkage block has been completely set up, we can link everything.

d3l_link
	tst.l	ddl_thadd(a3)		 ; any thing?
	beq.s	d3l_lsrv		 ; ... no

; ********************** possibly here

	lea	ddl_thing(a3),a0	 ; thing
	jsr	gu_thzlk		 ; zap and link
	bne.l	d3l_oops		 ; exit, releasing block

; ********************** or possibly here, we need special zap code
; ********************************

d3l_lsrv
	lea	ddl_ddlk(a3),a0 	 ; start linking in
	tst.l	d4			 ; slave?
	bne.s	d3l_sched		 ; ... yes
	moveq	#sms.lfsd,d0
	trap	#do.sms2
d3l_sched
	tst.l	-(a0)			 ; any scheduler?
	subq.l	#4,a0
	beq.s	d3l_poll		 ; ... no
	moveq	#sms.lshd,d0
	trap	#do.sms2
d3l_poll
	tst.l	-(a0)			 ; any poll?
	subq.l	#4,a0
	beq.s	d3l_super		 ; ... no
	moveq	#sms.lpol,d0
	trap	#do.sms2

; all the linking in, vector setting and hardware accessing is done in S mode

d3l_super
	move.w	sr,d0
	trap	#0
	move.w	d0,-(sp)		 ; supervisor mode now

; first the Thing

	lea	dv3_name,a0		 ; use thing
	jsr	gu_thuse		 ; use it
;***	bne.s	d3l_user		 ; give up and unlink everything
	bne.s	d3l_vectl		 ; this should not be possible!!

	move.l	a1,ddl_dv3(a3)		 ; linkage
	move.l	dv3_lklist(a1),ddl_ndv3(a3) ; next
	move.l	a3,dv3_lklist(a1)	 ; add into list

	jsr	gu_thfre		 ; free thing

; now the master / slave

	tst.l	d4			 ; any master?
	beq.s	d3l_vectl		 ; ... no

	move.l	a3,a2			 ; scan
d3l_master
	move.l	ddl_ndv3(a2),d0 	 ; next linkage
	beq.s	d3l_vectl		 ; ... none
	move.l	d0,a2
	tst.l	ddl_master(a2)		 ; is this a master?
	bne.s	d3l_master		 ; ... no

	cmp.l	ddl_dname+2(a2),d4	 ; is this the master?
	bne.s	d3l_master		 ; ... no

	move.l	a2,ddl_master(a3)	 ; master linkage
	move.l	ddl_slave(a2),ddl_slave(a3) ; next slave
	move.l	a3,ddl_slave(a2)	 ; add into list

; All the vectored linkages

d3l_vectl
	move.w	(a5)+,d0		 ; next vector
	beq.s	d3l_access
	lea	(a3,d0.w),a0		 ; indirect vector here
	move.w	(a5)+,a1
	move.w	a0,(a1) 		 ; set interrupt vector

	move.w	#push,(a0)+		 ; movem.l xxxx,-(sp)
	move.w	(a5)+,(a0)+
	move.w	#lea3,(a0)+		 ; lea xx(pc),a3
	move.l	a3,d0
	sub.l	a0,d0			 ; relative address (negative)
	move.w	d0,(a0)
	move.w	#jmp.l,(a0)+		 ; jump to
	move.l	a5,a1
	add.w	(a5)+,a1
	move.l	a1,(a0) 		 ; code
	bra.s	d3l_vectl


; Finally, any hardware ops

d3l_access
	move.w	(a5)+,d0		 ; next hardware access
	beq.s	d3l_sbuff

	lea	-2(a5,d0.w),a0		 ; routine
	jsr	(a0)
	bra.s	d3l_access

d3l_sbuff
	move.w	(a5)+,d0
	beq.s	d3l_done
	lea	(a3,d0.w),a5
	move.l	a5,ddl_bfbas(a3)	 ; temporary, set buffer base

d3l_done
	jsr	sms.cinvi		 ; invalidate instruction cache
	moveq	#0,d0

d3l_user
	move.w	(sp)+,sr		 ; back to user mode
	tst.l	d0			 ; ... OK?
	beq.s	d3l_exit

; **** if we could fall through here, we would need to zap the thing, and
; **** unlink the drivers. For the moment, however, the error check on using
; **** the DV3 thing is suppressed.

d3l_oops
	move.l	a3,a0			 ; release block
	jsr	gu_rchp

d3l_exit
	movem.l (sp)+,d3l.reg
	rts

;+++
; Set up jump vector table
;
;	d0   s
;	d1 c s	number of vectors - 1
;	a0 c  u where to put the first vector
;	a1   s
;	a2 c  u pointer to default table
;	a5 c  u pointer to vector table
;---
dv3_svec
d3l_svec
d3l_jloop
	move.l	a5,a1
	move.w	(a5)+,d0		 ; next from definition table
	bne.s	d3l_jset
	move.l	a2,a1
	move.w	(a2),d0 		 ; next from defaults
d3l_jset
	move.w	#jmp.l,(a0)+		 ; set jumps
	add.w	d0,a1
	move.l	a1,(a0)+
	clr.w	(a0)+			 ; spare bit

	addq.l	#2,a2			 ; next default
	dbra	d1,d3l_jloop
	rts

d3l_buff		 ; buffered io vectors
	vec	dv3_io
	vec	dv3_open
	vec	dv3_close
	vec	dv3_flush
	vec	dv3_format

d3l_nbuf		 ; unbuffered io vectors
	vec	dv3_ionb
	vec	dv3_opnb
	vec	dv3_clnb
	vec	dv3_ok
	vec	dv3_format

d3l_lkvec		 ; default linkage routines
	vec	dv3_inv  ; check
	vec	dv3_inv  ; direct
	vec	dv3_inv  ; rsect
	vec	dv3_inv  ; rsect
	vec	dv3_inv  ; rsect
	vec	dv3_inv  ; slbfill
	vec	dv3_rts  ; slbupd
	vec	dv3_ok	 ; dflush
	vec	dv3_ok	 ; fflush
	vec	dv3_ok	 ; find slave
	vec	dv3_inv  ; format
	vec	dv3_ok	 ; status
	vec	dv3_rts  ; done
	vec	dv3_ok	 ; zap

dv3_fdvec		 ; format dependent defaults
	vec	dv3_inv  ; medium check
	vec	dv3_inv  ; directory name
	vec	dv3_inv  ; make directory
	vec	dv3_inv  ; fetch directory entry
	vec	dv3_inv  ; fetch directory entry for file
	vec	dv3_inv  ; fetch selected directory entry
	vec	dv3_inv  ; set directory entry for file
	vec	dv3_inv  ; allocate sector
	vec	dv3_inv  ; locate sector
	vec	dv3_inv  ; truncate sectors
	vec	dv3_sload ; scatter load
	vec	dv3_ssave ; scatter save
	vec	dv3_inv  ; select format
	vec	dv3_inv  ; format
	vec	dv3_logphys ; logical to physical translate
	vec	dv3_ok	 ; reset medium information
	vec	dv3_inv  ; set medium name
	vec	dv3_ok	 ; update map

	end
