; Gold Card initialise QL Mode Displays        1992  Tony Tebby  QJUMP

	section init

	xdef	gl_procv
	xdef	gl_disp_size

	xref	gu_tpadd
	xref	gu_thini
	xref	ut_procdef

	include 'dev8_keys_aurora'
	include 'dev8_keys_con'
	include 'dev8_keys_sys'
	include 'dev8_keys_qdos_sms'
	include 'dev8_iod_bim_dddb_keys'
	include 'dev8_keys_chn'
	include 'dev8_keys_iod'
	include 'dev8_keys_err'
	include 'dev8_keys_thg'
	include 'dev8_mac_proc'
	include 'dev8_mac_thg'
	include 'dev8_mac_basic'
	include 'dev8_mac_assert'


	section exten

;+++
; initialise QVME Thing and procs
;---
gl_procv
	lea	glv_defs,a1		 ; Thing definition
	jsr	gu_thini

	lea	glv_xprocs,a1
	jsr	gu_tpadd

	lea	glv_procs,a1
	jmp	ut_procdef

glv_defs dc.l	th_name+2+4
	dc.l	glv_thing-*
	dc.l	'2.00'
glv_tnam dc.w	4,'QVME'


;******************************************************************************
;
; Here is the code to re-initialise the Aurora display
;
;******************************************************************************

glv_xprocs
	prc_stt glv_tnam
	prc_def SIZE,Disp_size
	prc_def RATE,Disp_rate
	prc_def BLNK,Disp_blank
	prc_end

glv_procs
	proc_stt
	proc_ref DISP_SIZE
	proc_ref DISP_RATE
	proc_ref DISP_BLANK
	proc_ref DISP_INVERSE
	proc_end
	proc_stt
	proc_ref DISP_TYPE
	proc_end

	proc_thg QVME
	fun40_thg QVME

disp_size proc	SIZE
disp_type fun40 TYPE

disp_rate
disp_blank
disp_inverse
disp_do
	moveq	#0,d0
	rts

glv_psiz
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
glv_prat
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
glv_pblnk
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
glv_uwrd
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
	 dc.w	0

glv_rword dc.w thp.ret+thp.swrd
	 dc.w	0

glv_thing
;+++
; invert display
;     (a1)     pointer to 0/1
;---
glv_invr thg_extn INVR,glv_type,glv_uwrd
	bra.s	disp_do

;+++
; find display type = 0
;     4(a1)	pointer to value
;---
glv_type thg_extn TYPE,glv_size,glv_rword
	moveq	#sms.xtop,d0
	trap	#do.smsq
	moveq	#0,d0
	move.b	sys_mtyp(a6),d0 ; display type in most sig 3 bits
	lsr.b	#5,d0
	move.b	glv_ttab(pc,d0.w),d0
	move.l	a1,-(sp)
	move.l	4(a1),a1
	move.w	d0,(a1) 	; return it
	move.l	(sp)+,a1
	moveq	#0,d0
	rts

glv_ttab dc.b	0,4,1,6,2,5,3,7

;+++
; Gold Card Aurora size change
;	(a1)	x
;      4(a1)	y
;	then rate
;	then blank
;---
glv_size thg_extn SIZE,glv_frrt,glv_psiz
glv.reg reg	d1/d2/d3/d4/a2/a4/a5
	moveq	#sms.xtop,d0
	trap	#1
	movem.l glv.reg,-(sp)
	moveq	#$ffffffe0,d0		 ; display type in most sig 3 bits
	and.b	sys_mtyp(a6),d0
	cmp.b	#sys.maur,d0		 ; Aurora?
	bne.s	glv_exok		 ; ... no
	lea	glv_au_ssel,a2		 ; size select table
	movem.l (a1),d0/d2		 ; size required
	moveq	#-1,d1
qlv_slook
	addq.l	#1,d1
	cmp.w	(a2)+,d0		 ; next size
	bhs.s	qlv_slook

	add.l	d2,d2			 ; twice height
	cmp.l	d0,d2			 ; is Y more than 1/2 x?
	shi	d2			 ; ... yes

	bsr.s	glv_size_set

glv_exok
	movem.l (sp)+,glv.reg
	moveq	#0,d0
	rts

glv_au_sizes
	dc.w	512
	dc.w	640
	dc.w	768
	dc.w	1024
glv_au_ssel
	dc.w	592
	dc.w	704
	dc.w	880
	dc.w	-1
;+++
; Set size d1 = width key, d2 set for 4:3, a6 = sysvar
; returns screen size in d3/d4 (for init where channel 0 id not open)
; smashes d0,d1,a4
;---
gl_disp_size
glv_size_set
	moveq	#1<<sysqm..8,d0 	 ; mode bit
	assert	sysqm..8,em..m0
	and.b	sys_qlmr(a6),d0 	 ; in the same place !!!!!
	or.b	d1,d0			 ; resolution

	add.w	d1,d1			 ; index by 2s
	move.w	glv_au_sizes(pc,d1.w),d3 ; X
	move.w	d3,d4			 ; Y
	lsr.w	#1,d4			 ; ... 2:1
	tst.b	d2			 ; 4:3?
	beq.s	glv_setr		 ; ... no
	add.w	d3,d4
	lsr.w	#1,d4
	bset	#em..ar,d0		 ; add ratio bit
glv_setr
	move.b	d0,emcr 		 ; set display mode
	move.b	d0,sys_hstt(a6) 	 ; set hardware status

	moveq	#%10101,d0		 ; jumper bits
	and.b	emcr,d0
	roxr.w	#2,d0
	ror.w	#1,d0
	lsr.b	#1,d0
	rol.w	#2,d0
	add.w	d0,d0
	move.w	glv_au_hmax(pc,d0.w),d0  ; max y size
	cmp.w	d0,d4
	bls.s	glv_sets
	move.l	d0,d4
glv_sets
	move.l	sys_chtb(a6),a4
	move.l	(a4),a4 		 ; channel 0 base address
	move.l	a4,d0
	ble.s	glv_setx

	move.l	chn_drvr(a4),a4 	 ; pointer to driver linkage
	lea	-iod_iolk(a4),a4
	move.l	a4,d0			 ; any linkage?
	bmi.s	glv_setx		 ; ... no

	movem.w d3/d4,pt_ssizx(a4)	 ; x,y,size
	move.l	pt_clink(a4),a4 	 ; pointer to console linkage
	movem.w d3/d4,bm_xscrs(a4)	 ; x,y,size

glv_setx
	rts

;+++
; Gold Card Aurora set frame rate, line rate
;
;	(a1)	frame rate
;      4(a1)	line rate
;---
glv_frrt thg_extn RATE,glv_blnk,glv_prat
	bra	disp_do

;+++
; Gold Card Aurora set blank
;
;	(a1)	x blank
;      4(a1)	y blank
;---
glv_blnk thg_extn BLNK,,glv_pblnk
	bra	disp_do


glv_au_hmax
	dc.w	288	   max ql height, non-interlaced
	dc.w	576	   max ql height, interlaced
	dc.w	576	   max vga
	dc.w	768	   max vga, intl
	dc.w	576	   max svga
	dc.w	768	   max svga, intl
	dc.w	768	   max multisynch
	dc.w	768	   max multisynch (diagnostic = 960 displayed)

	end
