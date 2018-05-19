; WMAN system palette	V1.02			2002 Marcel Kilgus
;
; 2005-03-25  1.01  Fixed error return of wm.jbpal (MK)
; 2017-12-13  1.02  Moved config to own file to reduce QDOS binary size (MK)

	xdef	wm_setsp
	xdef	wm_getsp
	xdef	wm_getspentry
	xdef	wm_initsp
	xdef	wm_jbpal

	xdef	wsp_winbd
	xdef	wsp_winbg
	xdef	wsp_winfg
	xdef	wsp_winmg
	xdef	wsp_titlebg
	xdef	wsp_titletextbg
	xdef	wsp_titlefg
	xdef	wsp_litemhigh
	xdef	wsp_litemavabg
	xdef	wsp_litemavafg
	xdef	wsp_litemselbg
	xdef	wsp_litemselfg
	xdef	wsp_litemunabg
	xdef	wsp_litemunafg
	xdef	wsp_infwinbd
	xdef	wsp_infwinbg
	xdef	wsp_infwinfg
	xdef	wsp_infwinmg
	xdef	wsp_subinfbd
	xdef	wsp_subinfbg
	xdef	wsp_subinffg
	xdef	wsp_subinfmg
	xdef	wsp_appbd
	xdef	wsp_appbg
	xdef	wsp_appfg
	xdef	wsp_appmg
	xdef	wsp_apphigh
	xdef	wsp_appiavabg
	xdef	wsp_appiavafg
	xdef	wsp_appiselbg
	xdef	wsp_appiselfg
	xdef	wsp_appiunabg
	xdef	wsp_appiunafg
	xdef	wsp_scrbar
	xdef	wsp_scrbarsec
	xdef	wsp_scrbararr
	xdef	wsp_buthigh
	xdef	wsp_butbd
	xdef	wsp_butbg
	xdef	wsp_butfg
	xdef	wsp_hintbd
	xdef	wsp_hintbg
	xdef	wsp_hintfg
	xdef	wsp_hintmg
	xdef	wsp_errbg
	xdef	wsp_errfg
	xdef	wsp_errmg
	xdef	wsp_shaded
	xdef	wsp_3ddark
	xdef	wsp_3dlight
	xdef	wsp_vertfill
	xdef	wsp_subtitbg
	xdef	wsp_subtittxtbg
	xdef	wsp_subtitfg
	xdef	wsp_mindexbg
	xdef	wsp_mindexfg
	xdef	wsp_separator

	xref	wmc_error
	xref	gu_achpp

	include 'dev8_ee_wman_data'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_sys'
	include 'dev8_keys_con'
	include 'dev8_keys_err'
	include 'dev8_keys_jcb'
	include 'dev8_mac_assert'

	section wman

; some system palette defines
wsp.low 	equ	0			; lowest entry
wsp.high	equ	56			; highest entry
wsp.entries	equ	wsp.high-wsp.low+1	; number of entries per palette
wsp.palettes	equ	4			; number of palettes
wsp.sizes	equ	2*wsp.entries		; size of one palette
wsp.size	equ	wsp.palettes*wsp.sizes	; size of all palettes

; palette block header
wpb.jobtab	equ	0			; rel ptr to job table
wpb.pal1	equ	2			; rel ptr to palettes
wpb.pal2	equ	4
wpb.pal3	equ	6
wpb.pal4	equ	8
wpb.size	equ	10			; size of block

; job table entry
wjb.tag 	equ	0			; job tag
wjb.pal 	equ	3			; palette for this job
wjb.ownpal	equ	4			; pointer to job's own palette
wjb.size	equ	8			; size of block
wjb..size	equ	3			; shift for block size

; ptr to all default blocks
wsp_tables
		dc.w	wsp_table1-*
		dc.w	wsp_table2-*
		dc.w	wsp_table3-*
		dc.w	wsp_table4-*

; Main, configurable palette
wsp_table1
wsp_winbd	dc.w	0		; Main window
wsp_winbg	dc.w	$07
wsp_winfg	dc.w	0
wsp_winmg	dc.w	$02
wsp_titlebg	dc.w	$5c		; Title bar
wsp_titletextbg dc.w	$07
wsp_titlefg	dc.w	$00
wsp_litemhigh	dc.w	0		; Loose item (within main window)
wsp_litemavabg	dc.w	$07
wsp_litemavafg	dc.w	0
wsp_litemselbg	dc.w	$04
wsp_litemselfg	dc.w	0
wsp_litemunabg	dc.w	$07
wsp_litemunafg	dc.w	$04
wsp_infwinbd	dc.w	$04		; Information window
wsp_infwinbg	dc.w	$07
wsp_infwinfg	dc.w	0
wsp_infwinmg	dc.w	$02
wsp_subinfbd	dc.w	$07		; Information win in another inf win
wsp_subinfbg	dc.w	$04
wsp_subinffg	dc.w	0
wsp_subinfmg	dc.w	$07
wsp_appbd	dc.w	$04		; Application (menu) window
wsp_appbg	dc.w	$07
wsp_appfg	dc.w	$00
wsp_appmg	dc.w	$02
wsp_apphigh	dc.w	$00
wsp_appiavabg	dc.w	$07
wsp_appiavafg	dc.w	0
wsp_appiselbg	dc.w	$04
wsp_appiselfg	dc.w	0
wsp_appiunabg	dc.w	$07
wsp_appiunafg	dc.w	$04
wsp_scrbar	dc.w	$07		; Scroll/pan in app window
wsp_scrbarsec	dc.w	0
wsp_scrbararr	dc.w	$04
wsp_buthigh	dc.w	0		; QPAC2 button frame colours
wsp_butbd	dc.w	$04
wsp_butbg	dc.w	$07
wsp_butfg	dc.w	0
wsp_hintbd	dc.w	$07		; Popup help colours
wsp_hintbg	dc.w	$04
wsp_hintfg	dc.w	$07
wsp_hintmg	dc.w	$02
wsp_errbg	dc.w	$02		; Error colours
wsp_errfg	dc.w	$07
wsp_errmg	dc.w	$04
wsp_shaded	dc.w	$1f		; See device selection in QPAC2 files
wsp_3ddark	dc.w	$02		; The colours used for 3D borders
wsp_3dlight	dc.w	$07
wsp_vertfill	dc.w	$9f		; See device window in QPAC2 files (->)
wsp_subtitbg	dc.w	$5c		; Title bar that is not main title
wsp_subtittxtbg dc.w	$07
wsp_subtitfg	dc.w	$00
wsp_mindexbg	dc.w	$07		; Menu window index (e.g. A B C D ...)
wsp_mindexfg	dc.w	$00
wsp_separator	dc.w	$04		; Seperator line. Must fit to InfWin


; Red/black
wsp_table2
	dc.w	$07,$00,$07,$04,$52,$00,$07,$07
	dc.w	$00,$07,$02,$07,$00,$02,$02,$00
	dc.w	$07,$04,$00,$02,$07,$00,$02,$00
	dc.w	$07,$04,$07,$00,$07,$02,$07,$00
	dc.w	$02,$00,$07,$02,$07,$02,$00,$07
	dc.w	$00,$02,$00,$04,$04,$00,$02,$10
	dc.w	$04,$00,$90,$52,$00,$07,$00,$02
	dc.w	$02

; White/red
wsp_table3
	dc.w	$00,$07,$00,$04,$6a,$07,$00,$00
	dc.w	$07,$00,$02,$00,$07,$02,$02,$07
	dc.w	$00,$04,$07,$02,$00,$07,$02,$07
	dc.w	$00,$04,$00,$07,$00,$02,$00,$07
	dc.w	$02,$07,$00,$02,$00,$02,$07,$00
	dc.w	$07,$02,$07,$04,$04,$07,$02,$2f
	dc.w	$04,$07,$af,$6a,$07,$00,$07,$02
	dc.w	$02

; Red/green
wsp_table4
	dc.w	$07,$00,$07,$02,$64,$00,$07,$07
	dc.w	$00,$07,$04,$07,$00,$04,$04,$00
	dc.w	$07,$02,$00,$04,$07,$00,$04,$00
	dc.w	$07,$02,$07,$00,$07,$04,$07,$00
	dc.w	$04,$00,$07,$04,$07,$04,$00,$07
	dc.w	$00,$04,$00,$02,$02,$00,$04,$20
	dc.w	$02,$00,$a0,$64,$00,$07,$02,$04
	dc.w	$04


;+++
;  Vector $7C							WM.SETSP
;
;	Set system palette entries
;
;  Call parameters			Return parameters
;
;  D1.w start index			D1   preserved
;  D2.w number of elements		D2   preserved
;  D3.w palette number			D3+  all preserved
;
;  A0					A0   preserved
;  A1	pointer to palette entries / 0	A1   preserved
;  A2					A2   preserved
;  A3					A3   preserved
;  A4					A4   preserved
;  A5	not used by any routine
;  A6	not used by any routine
;
;  Error returns:
;	IPAR	Illegal index number / invalid number of elements
;
; Set the entries of the system palette to the values in the buffer,
; beginning with the index in D1 (counting from 0) and ending with the
; index D1 + D2 - 1.
;
; If A1 = 0 then the entries are taken out of the default table. Otherwise
; the buffer must hold an array of words with the colour values of the
; different items. The colour format is the standard WMAN colour format as
; described elsewhere.
;---

saveregs reg	 d1-d2/a0-a1
wm_setsp
	movem.l saveregs,-(sp)
	bsr.s	wm_checkranges
	bne.s	wsp_rts
	move.l	a1,d0			; buffer given?
	bne.s	wsp_set

	lea	wsp_tables,a1		; default values
	adda.w	d3,a1
	adda.w	d3,a1
	adda.w	(a1),a1
	adda.w	d1,a1			; one entry is 16 bit
	adda.w	d1,a1
wsp_set
	bsr	wm_getspaddress
	bne.s	wsp_nc
	adda.w	d1,a0
	adda.w	d1,a0
	bra.s	wsp_copye
wsp_copy
	move.w	(a1)+,(a0)+		; word entries
wsp_copye
	dbf	d2,wsp_copy
	moveq	#0,d0
wsp_rts
	movem.l (sp)+,saveregs
	rts

wsp_nc
	moveq	#err.nc,d0
	movem.l (sp)+,saveregs
	rts

wm_checkranges
	cmp.w	#wsp.palettes-1,d3
	bhi.s	wm_iparr

	cmpi.w	#wsp.high,d1
	bhi.s	wm_iparr
	move.w	d1,d0
	add.w	d2,d0
	bcs.s	wm_iparr		; Overflow (D2 negative)?
	cmpi.w	#wsp.entries,d0
	bhi.s	wm_iparr		; D1 + D2 > max entries?
	moveq	#0,d0
	rts
wm_iparr
	moveq	#err.ipar,d0
	rts

;+++
;  Vector $80							WM.GETSP
;
;	Read system palette entries
;
;  Call parameters			Return parameters
;
;  D1.w start index			D1.w preserved
;  D2.w number of elements  / -1	D2   preserved / item count
;  D3.w palette number			D3+  all preserved
;
;  A0					A0   preserved
;  A1	pointer to entry buffer 	A1   preserved
;  A2					A2   preserved
;  A3					A3   preserved
;  A4					A4   preserved
;  A5	not used by any routine
;  A6	not used by any routine
;
;  Error returns:
;	IPAR	Illegal index number / invalid number of elements
;
; Copies entries of the system palette into the given buffer, beginning with
; the index in D1 (counting from 0) and ending with the index D1 + D2 - 1. The
; buffer must be big enough to hold all requested entries.
;
; If D1 is given as -1 the function just returns the number of items held in
; the system palette. This can increase when more items get defined in new
; WMAN version. This is guaranteed to be below 256.
;---

wm_getsp
	cmp.w	#-1,d2
	bne.s	wgp_get
	move.w	#wsp.entries,d2 	; only return item count
	moveq	#0,d0
	rts

wgp_get
	movem.l saveregs,-(sp)
	bsr.s	wm_checkranges
	bne.s	wgp_rts

	bsr.s	wm_getspaddress
	bne.s	wsp_nc
	adda.w	d1,a0
	adda.w	d1,a0
	bra.s	wgp_copye
wgp_copy
	move.w	(a0)+,(a1)+		; word entries
wgp_copye
	dbf	d2,wgp_copy
	moveq	#0,d0
wgp_rts
	movem.l (sp)+,saveregs
	rts

;+++
; Get single system palette entry
;
;	d1 cr	index / entry
;---
wm_getspentry
	cmpi.w	#wsp.high,d1		; out of range?
	bhi	wmc_error

	move.l	a0,-(sp)
	lea	wsp_table1,a0
	bsr.s	wm_getjobpalette
	adda.w	d1,a0
	adda.w	d1,a0
	move.w	(a0),d1
	move.l	(sp)+,a0
	rts

;+++
; Get system palette address
;
;	d3 c  p palette number
;	a0  r	palette address
;---
wm_getspaddress
	movem.l d0/d1,-(sp)
	bsr.s	wm_getspvec
	adda.w	#wpb.pal1,a0
	adda.w	d3,a0
	adda.w	d3,a0
	adda.w	(a0),a0
	movem.l (sp)+,d0/d1
	rts

;+++
; Get pointer to system palette block
;
;	d1  r	current job ID
;	a0  r	pointer to palette
;---
wm_getspvec
	movem.l d2,-(sp)
	moveq	#sms.info,d0
	trap	#1
	move.l	sys_clnk(a0),a0 	; console linkage
	move.l	pt_wdata(a0),a0 	; WMAN data
	move.l	wd_syspal(a0),a0	; system palette
	movem.l (sp)+,d2
	tst.l	d0
	rts

;+++
; Get system palette address for this job
;
;	a0  r	palette address
;---
wm_getjobpalette
	movem.l d0-d3/a1,-(sp)
	bsr.s	wm_getspvec		; also returns job ID
	moveq	#0,d3			; default to first palette

	move.l	a0,a1			; ptr to system palette block
	move.l	d1,d2			; job tag / job ID
	swap	d2
	assert	wpb.jobtab,0
;	 adda.w  #wpb.jobtab,a1
	adda.w	(a1),a1
	lsl.w	#wjb..size,d1
	adda.w	d1,a1
	cmp.w	wjb.tag(a1),d2		; compare job tag
	bne.s	wmgjp_calc		; no entry for use, take default

	move.b	wjb.pal(a1),d3		; get palette number from table
	cmp.b	#-1,d3			; own palette?
	bne.s	wmgjp_calc		; no, go ahead
	move.l	wjb.ownpal(a1),d0
	beq.s	wmgjp_calc		; own not set, take default
	move.l	d0,a0
	bra.s	wmgjp_rts

wmgjp_calc
	adda.w	#wpb.pal1,a0
	adda.w	d3,a0
	adda.w	d3,a0
	adda.w	(a0),a0
wmgjp_rts
	movem.l (sp)+,d0-d3/a1
	rts

;+++
;  Vector $90							WM.JBPAL
;
;	Set system palette number of job
;
;  Call parameters			Return parameters
;
;  D1.l job ID / -1			D1   preserved
;  D2					D2   preserved
;  D3.w palette number / -1		D3+  all preserved
;
;  A0					A0   preserved
;  A1	ptr to job palette or 0 (D3=-1) A1   preserved
;  A2					A2   preserved
;  A3					A3   preserved
;  A4					A4   preserved
;  A5	not used by any routine
;  A6	not used by any routine
;
;  Error returns:
;	IJOB	Illegal job ID
;	IPAR	Illegal palette number
;
; Sets the active system palette for the given job.
;---
wm_jbpal
	movem.l d1-d2/d4/a0,-(sp)
	moveq	#err.ipar,d0
	cmp.w	#-1,d3			; palette number in range?
	blt.s	wmjp_rts
	cmp.w	#wsp.palettes,d3
	bge.s	wmjp_rts

	move.l	d1,d4
	moveq	#sms.info,d0
	trap	#1
	cmp.l	#-1,d4			; need current job ID?
	bne.s	wmjp_givenid
	move.l	d1,d4			; yes, take it
wmjp_givenid
	cmp.w	sys_jbtp(a0),d4 	; job number to large?
	bhi.s	wmjp_ijob		; ... yes

	move.l	sys_jbtb(a0),a0 	; job table
	lsl.w	#2,d4			; to job table entry size
	move.l	(a0,d4.w),a0
	move.w	jcb_tag(a0),d1
	move.l	d4,d2
	swap	d2			; tag of given job ID
	cmp.w	d2,d1
	bne.s	wmjp_ijob		; must match

	bsr	wm_getspvec
	assert	wpb.jobtab,0
;	 adda.w  #wsp.jobtab,a0
	adda.w	(a0),a0
	lsl.w	#wjb..size-2,d4
	adda.w	d4,a0
	cmp.w	wjb.tag(a0),d2		; already an entry for this job?
	beq.s	wmjp_noreset
	move.w	d2,wjb.tag(a0)		; new entry, save tag
	clr.l	wjb.ownpal(a0)		; clear ptr to job's own palette
wmjp_noreset
	move.b	d3,wjb.pal(a0)		; save number
	bpl.s	wmjp_rts		; if not own job palette, exit
	move.l	a1,d0			; ptr to palette given?
	beq.s	wmjp_rts		; no, exit
	move.l	a1,wjb.ownpal(a0)
	moveq	#0,d0
wmjp_rts
	movem.l (sp)+,d1-d2/d4/a0
	rts

wmjp_ijob
	moveq	#err.ijob,d0
	bra.s	wmjp_rts

;+++
; Allocate and initialise system palette with values from config block
;
;	d0  r	error code
;	a0  r	pointer to allocated area
;
;	status return standard
;---
wm_initsp
	movem.l d1-d3/a1,-(sp)
	moveq	#sms.info,d0
	trap	#1
	move.l	sys_jbtt(a0),d0
	sub.l	sys_jbtb(a0),d0 	; size of job table
	lsl.l	#wjb..size-2,d0 	; shift to have size of own job table

	add.l	#wsp.size+wpb.size,d0	; room for palette and header
	jsr	gu_achpp
	bne.s	wmi_rts

; fill in pointers
	move.w	#wpb.size-wpb.pal1,wpb.pal1(a0)
	move.w	#wpb.size-wpb.pal2+1*wsp.sizes,wpb.pal2(a0)
	move.w	#wpb.size-wpb.pal3+2*wsp.sizes,wpb.pal3(a0)
	move.w	#wpb.size-wpb.pal4+3*wsp.sizes,wpb.pal4(a0)
	move.w	#wpb.size-wpb.jobtab+4*wsp.sizes,wpb.jobtab(a0)  ; jobs are last

	move.l	a0,-(sp)
	adda.l	#wpb.size,a0
	lea	wsp_table1,a1
	move.w	#wsp.size/2-1,d0
wmi_loop
	move.w	(a1)+,(a0)+
	dbf	d0,wmi_loop

	move.l	(sp)+,a0
	moveq	#0,d0
wmi_rts
	movem.l (sp)+,d1-d3/a1
	rts

	end
