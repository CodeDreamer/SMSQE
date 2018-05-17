; Background graphics I/O code	v1.02			2016 Marcel Kilgus
;
; 2005-11-17  1.00  Initial release (MK)
; 2006-03-22  1.01  Additional checks for QDOS PE variant (MK)
; 2016-03-31  1.02  Fixed crash for slightly overlapping windows (MK)

	section driver

	xdef	pt_bginit
	xdef	pt_bgprocs
	xdef	pt_bgio
	xdef	pt_bgrefresh
	xdef	pt_bgctl

	xref	cn_io
	xref	pti_do
	xref	pti_hide
	xref	pt_do_con
	xref	pt_wdef
	xref	pt_tstov
	xref	pt_mblock
	xref.s	pt.spxlw	; shift pixel to long word
	xref.s	pt.rpxlw	; round up pixel to long word (2^spxlw-1)
	xref.l	pt.samsk	; origin mask in save area (QL mode)
	xref	gu_achpp

	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_qdos_pt'
	include 'dev8_keys_con'
	include 'dev8_keys_qlv'
	include 'dev8_mac_proc'

bg.areas equ	60		; We can handle this many requests at once

; Background I/O data structure
bd_strt equ	0		; long	Start of list
bd_end	equ	4		; long	End of list
bd_free equ	8		; long	Free space after last item in list
bd_flag equ	12		; byte	Background processing enabled?
bd.size equ	16		;	Size of structure

; List item
bdi_p1	equ	0		; long	Upper left point
bdi_p2	equ	4		; long	Lower right point
bdi_chn equ	8		; long	Primary channel
bdi.sze equ	12		;	Size of structure

;+++
; Background I/O initialisation
;
; Reserves memory and sets up the I/O region buffer
;
; a3  CON driver linkage
;---
bgi.reg reg	a0-a3/a6/d1-d3/d7
pt_bginit
	movem.l bgi.reg,-(sp)
	move.l	#bd.size+bg.areas*bdi.sze,d0
	jsr	gu_achpp		; allocate enough memory
	bne.s	bgi_rts 		; oh, bugger
	move.l	a0,pt_bgdat(a3) 	; our data
	lea	bd.size(a0),a1		; start of list
	move.l	a1,bd_strt(a0)
	move.l	a1,bd_free(a0)
	adda.w	#bg.areas*bdi.sze,a1	; end of list
	move.l	a1,bd_end(a0)
	sf	bd_flag(a0)		; disabled by default

	moveq	#0,d0
bgi_rts
	movem.l (sp)+,bgi.reg
	rts

pt_bgprocs
	proc_stt
	proc_ref PE_BGON
	proc_ref PE_BGOFF
	proc_end
	proc_stt
	proc_end

;+++
; PE_BGOFF - Disable background updating
;---
pe_bgoff
	moveq	#0,d4
	bra.s	pe_bgchange

;+++
; PE_BGON - Enable background updating
;---
pe_bgon
	moveq	#-1,d4
pe_bgchange
	cmpa.l	a3,a5
	bne.s	err_ipar
	bsr.s	os_check
	beq.s	err_nimp		; OS not compatible
	move.l	sys_clnk(a0),a0
	move.l	pt_bgdat(a0),a0
	move.b	d4,bd_flag(a0)
	moveq	#0,d0
	rts
err_nimp
	moveq	#err.nimp,d0
	rts
err_ipar
	moveq	#err.ipar,d0
	rts

;+++
;  PV_BGCTL	$24
;
;  Call parameters			Return parameters
;  D0					D0   standard error code
;  D1	-1 read, 0 disable, 1 enable	D1   0 disabled, >0 enabled
;  D2	0				D2   preserved
;  D3					D3   preserved
;
;  A0					A0   preserved
;  A1					A1   preserved
;  A2					A2   preserved
;  A3	pointer to CON linkage block	A3   preserved
;---
pt_bgctl
	tst.l	d2
	bne.s	err_ipar
	move.l	a0,-(sp)
	bsr.s	os_check
	beq.s	err_nimp		; OS not compatible
	move.l	pt_bgdat(a3),a0
	tst.l	d1
	bmi.s	pbc_read
	beq.s	pbc_disable

	st	bd_flag(a0)
	bra.s	pbc_read
pbc_disable
	sf	bd_flag(a0)
pbc_read
	moveq	#0,d1
	move.b	bd_flag(a0),d1
pbc_exit
	move.l	(sp)+,a0
	moveq	#0,d0
	rts

;+++
; Check that OS is background I/O compatible
;
; d0	0 = not compatible
; a0	ptr to sysvars
;---
os_check
	movem.l d1-d3/a1-a3,-(sp)
	moveq	#sms.info,d0
	trap	#1
	andi.l	#$ff00ffff,d2		; QDOS version
	cmpi.l	#$31003832,d2		; at least 1.82 (Minerva!)
	bcs.s	not_compatible
	moveq	#1,d0
not_compatible
	movem.l (sp)+,d1-d3/a1-a3
	tst.l	d0
	rts

;+++
; Main background CON I/O handler
;---
pt_bgio
	move.l	pt_bgdat(a3),a4
	tst.b	bd_flag(a4)		; background processing enabled?
	beq.s	err_nc			; no!

	lea	bg_table,a4
	moveq	#0,d4
	move.b	(a4,d0.w),d4
	lea	bgjump_table(pc,d4.w),a4
	add.w	(a4),a4
	jmp	(a4)

err_nc
	moveq	#err.nc,d0
	rts

; Different background I/O handler values. The values correspond to the
; offsets in bgjump_table!
nc	equ	0		; return err.nc
dc	equ	2		; direct cn_io
dp	equ	4		; direct pt_io
cn	equ	6		; wrap cn_io
pt	equ	8		; wrap pt_io
ou	equ	10		; wrap pt_io, restore whole outline
cu	equ	12		; wrap cn_io if cur visible, direct otherwise
nl	equ	14		; wrap cn_io if newline is pending, direct ow
df	equ	16		; iow.defb, iow.defw

bgjump_table
	dc.w	err_nc-*
	dc.w	cn_io-*
	dc.w	pbg_pti_do-*
	dc.w	pbg_wrap-*
	dc.w	pbg_wrap-*
	dc.w	pbg_wrap-*
	dc.w	pbg_cn_cursor-*
	dc.w	pbg_cn_newline-*
	dc.w	pbg_def-*

; Table with handlers for the different TRAP#3 functions
;
; The table has been created with a lot of thought and even more tests,
; alter with EXTREME care!
bg_table
	dc.b	nc,nc,nc,nc,nc,cn,cn,cn 	; $00
	dc.b	nc,cn,nl,nl,df,df,cn,cn 	; $08
	dc.b	cu,cu,cu,cu,cu,cu,cu,cu 	; $10
	dc.b	cn,cn,cn,cn,nc,nc,cn,cn 	; $18
	dc.b	cn,cn,cn,cn,cn,dc,cn,dc 	; $20
	dc.b	dc,dc,dc,dc,dc,pt,cn,cn 	; $28
	dc.b	cn,cn,cn,cn,dc,dc,dc,nc 	; $30
	dc.b	nc,nc,nc,nc,nc,nc,nc,nc 	; $38
	dc.b	nc,nc,nc,nc,nc,nc,nc,nc 	; $40
	dc.b	nc,nc,nc,nc,nc,nc,nc,nc 	; $48
	dc.b	pt,pt,pt,pt,pt,pt,pt,pt 	; $50
	dc.b	pt,pt,pt,pt,pt,pt,pt,nc 	; $58
	dc.b	pt,pt,nc,nc,nc,nc,nc,nc 	; $60
	dc.b	nc,nc,nc,dp,dp,pt,pt,pt 	; $68
	dc.b	pt,nc,pt,pt,pt,nc,pt,pt 	; $70
	dc.b	nc,nc,nc,nc,dp,dp,nc,ou 	; $78

; Stack frame
s_oscrb equ	0			; Original sd_sbase
s_olinl equ	4			; Original sd_linel
s_oxmin equ	6			; Original sd_xmin/sd_ymin
s_xsize equ	10			; Copy of sd_xsize/sd_ysize
s_borwd equ	14			; Copy of sd_borwd
s_prim	equ	16			; Primary window of channel
s_flag	equ	20			; Key from bg_table
s_regs	equ	22			; Register save area
s.regs	reg	d0-d2/a0/a2-a3		; Registers to save
s_size	equ	s_regs+6*4		; Structure length

;+++
; Wrap I/O call
;
; Re-directs the screen information in the cdb to the save area of the
; channel's primary window. Puts window region into the asynchronous
; update list afterwards.
;
; d0	function code / error return
; d4	bg_table key
;---
pbg_wrap
	suba.w	#s_size,sp
	move.b	d4,s_flag(sp)
	move.l	sd_scrb(a0),s_oscrb(sp)
	move.w	sd_linel(a0),s_olinl(sp)
	move.l	sd_xmin(a0),s_oxmin(sp)
	move.w	sd_borwd(a0),s_borwd(sp)
	move.l	sd_xsize(a0),s_xsize(sp)

	move.l	a0,a4
	btst	#sd..prwn,sd_prwin(a0)	; primary?
	bne.s	pbg_gotprimary		; yes
	move.l	sd_pprwn(a0),a4 	; get primary window
	add.w	#sd.extnl,a4
pbg_gotprimary
	move.l	a4,s_prim(sp)		; save primary window address
	move.l	sd_xhito(a4),d4
	sub.l	d4,sd_xmin(a0)		; make origin relative to save area
	and.l	#pt.samsk,d4		; QL mode save area offset
	add.l	d4,sd_xmin(a0)		; include that in calculations
	move.l	sd_wsave(a4),sd_scrb(a0) ; save area is virtual screen
	move.w	sd_xhits(a4),d4 	; save area width (pixels)
	moveq	#pt.spxlw,d5		;
	add.w	#pt.rpxlw,d4		; round up to...
	lsr.w	d5,d4			; ...width in long words
	addq.w	#1,d4			; one spare
	lsl.w	#2,d4			; now bytes
	move.w	d4,sd_linel(a0) 	; line length in save area

	cmp.b	#cn,s_flag(sp)		; Only CON I/O
	bne.s	pbg_ptrio		; ...no

	jsr	cn_io			; do CON I/O
	bra.s	pbg_allio

pbg_ptrio
	move.l	sd_xouto(a4),d4 	; also adapt outline for PTR I/O
	sub.l	d4,sd_xouto(a0)
	bsr	pt_do_con		; do actual I/O call
	add.w	#sd.extnl,a0		; will have been changed by pt_do_con
	move.l	s_prim(sp),a4
	move.l	sd_xhito(a4),d4 	; restore origins (might have been
	add.l	d4,sd_xouto(a0) 	;   altered by call, therefore add)

pbg_allio
	movem.l s.regs,s_regs(sp)	; save working registers

	cmp.b	#ou,s_flag(sp)		; update whole outline?
	bne.s	pbg_normalwin		; no, just update window contents
	move.l	sd_xouto(a0),d2 	; yes, update whole outline
	move.l	d2,d1
	add.l	sd_xouts(a0),d1
	bra.s	pbg_addarea

pbg_normalwin
	move.l	s_oxmin(sp),d2		; upper left corner
	move.l	d2,d1
	add.l	s_xsize(sp),d1		; lower right corner + (1,1)
	move.w	s_borwd(sp),d4		; original border width
	add.w	d4,d4			; twice as thick in X direction
	swap	d4
	move.w	s_borwd(sp),d4
	sub.l	d4,d2			; increase area by border width
	add.l	d4,d1
pbg_addarea
	move.l	s_prim(sp),a2		; primary window
	jsr	pt_addupdarea

	movem.l s_regs(sp),s.regs
	move.l	s_prim(sp),a4
	move.l	sd_xhito(a4),d4 	; restore sd_xmin (might have been
	add.l	d4,sd_xmin(a0)		;   altered by border call)
	and.l	#pt.samsk,d4		; QL mode save area offset
	sub.l	d4,sd_xmin(a0)		; do away with it again
	move.w	s_olinl(sp),sd_linel(a0) ; restore other values
	move.l	s_oscrb(sp),sd_scrb(a0)
	adda.w	#s_size,sp
	tst.l	d0
pbg_exit
	rts

;+++
; Just do pti_do
;---
pbg_pti_do
	addq.w	#4,sp			; forget return address
	jmp	pti_do

;+++
; Wrap cn_io if cursor is visible, do directly otherwise
;---
pbg_cn_cursor
	tst.b	sd_curf(a0)		; cursor visible?
	ble	cn_io			; ... no, just do it
	moveq	#cn,d4			; ... yes, do wrapped CON call
	bra	pbg_wrap

;+++
; Wrap cn_io if newline is pending, do directly otherwise
;---
pbg_cn_newline
	moveq	#cn,d4			; wrapped call is default
	tst.b	sd_nlsta(a0)		; newline pending?
	bne	pbg_wrap		; ... yes, do wrapped call
	bra	cn_io			; ... no, do normal I/O

;+++
; iow.defb, iow.defw handler
;---
pbg_def
	move.l	a0,a4
	btst	#sd..prwn,sd_prwin(a0)	; primary?
	bne.s	pdef_gotprimary 	; yes
	move.l	sd_pprwn(a0),a4 	; get primary window
	add.w	#sd.extnl,a4
pdef_gotprimary
	btst	#sd..well,sd_behav(a4)	; is it well behaved?
	beq	err_nc			; no? we can only do well behaved ones
	cmp.w	#iow.defb,d0
	beq.s	pdef_bdr		; only do border call

	clr.b	pt_schfg(a3)		; something moved
	tst.b	sd_curf(a0)		; cursor visible?
	bgt.s	pdef_hidecur		; ... yes, hide it
	jsr	pt_wdef(pc)		; ... no, just do wdef
	beq.s	pdef_bdr		; now do border
pdef_rts
	rts

pdef_hidecur
	movem.w d1/d2,-(sp)
	moveq	#iow.dcur,d0		; disable cursor
	bsr	pt_bgio
	jsr	pt_wdef(pc)		; do wdef ourselves
	move.l	d0,-(sp)		; keep error code
	moveq	#iow.ecur,d0		; re-enable cursor
	bsr	pt_bgio
	move.l	(sp)+,d0		; restore error code
	movem.w (sp)+,d1/d2
	bne.s	pdef_rts
pdef_bdr
	moveq	#iow.defb,d0		; do the border as with the old code
	clr.b	pt_schfg(a3)		; border changed
	moveq	#cn,d4			; do normal CON call
	bra	pbg_wrap

;+++
; Add area of specific window into the internal update queue for later refresh
;
; d1  window lower right corner + (1,1)
; d2  window upper left corner
; a2  channel definition block of primary window
; a3  CON driver linkage
;---
pt_addupdarea
; First check whether window is actually in window pile! It might be hidden
; by means like the Button_Sleep thing!
	lea	pt_head-sd_prwlt(a3),a4 ; go through all primaries
pau_pwloop
	move.l	sd_prwlt(a4),a4 	; next channel
	move.l	a4,d0			; end of linked list?
	beq.s	pau_exit		; ... yes, it's not there!
	addq.l	#-sd_prwlt,a4		; set channel block address

	cmp.l	a4,a2			; our window?
	bne.s	pau_pwloop		; no, check next

; Okay, window is in pile, add it to update list if it's not already there.
; Overlapping entries in the list are possible, but rather unlikely!
	move.l	pt_bgdat(a3),d0 	; our internal data
	beq.s	pau_exit		; very very bad!
	move.l	d0,a4
	move.l	bd_strt(a4),a0		; start of list
	move.l	bd_end(a4),a3		; end of list
	move.l	bd_free(a4),d4		; first free space
	suba.w	#bdi.sze,a0
pau_loop
	adda.w	#bdi.sze,a0
	cmp.l	a0,a3			; buffer full?
	beq.s	pau_exit		; yes, just exit
	cmp.l	a0,d4			; checked all existing items?
	beq.s	pau_enter		; yes, add our new data
	cmp.l	bdi_p1(a0),d2		; check that update area is not the same
	bne.s	pau_loop		; not same, check next
	cmp.l	bdi_p2(a0),d1
	bne.s	pau_loop
	cmp.l	bdi_chn(a0),a2		; channel also the same? (very likely..)
	bne.s	pau_loop		; no?? Ah well, check next
	bra.s	pau_exit		; entry already in list!

pau_enter
	move.l	d2,(a0)+		; add update area to list
	move.l	d1,(a0)+
	move.l	a2,(a0)+
	move.l	a0,bd_free(a4)		; set new free space pointer
pau_exit
	rts

;+++
; Refresh screen using internal update queue
;
; a3  CON driver linkage
;---
rfbg.reg reg	d0-d7/a0-a6
pt_bgrefresh
	movem.l rfbg.reg,-(sp)
	move.l	pt_bgdat(a3),d0 	; our internal data
	beq.s	prf_exit		; not set up yet
	move.l	d0,a1
	move.l	bd_strt(a1),a0		; start of list
	move.l	bd_free(a1),a1		; free space pointer (end of list)
	cmp.l	a0,a1
	beq.s	prf_exit		; early exit for empty list
	move.l	pt_scren(a3),-(sp)	; screen base
	clr.l	-(sp)			; leave dest base empty for now
	clr.l	-(sp)
	move.w	pt_scinc(a3),2(sp)	; screen increment
	clr.l	-(sp)			; same for dest increment
	move.l	sp,a6			; data block
	move.b	pt_dmode(a3),d7 	; current display mode
prf_loop
	move.l	(a0)+,d2		; upper left point
	move.l	(a0)+,d1		; lower right point
	move.l	(a0)+,a2		; channel that got updated
	moveq	#0,d4
	move.w	sd_xhits(a2),d4 	; save area width (pixels)
	moveq	#pt.spxlw,d5		;
	add.w	#pt.rpxlw,d4		; round up to...
	lsr.w	d5,d4			; ...width in long words
	addq.w	#1,d4			; one spare
	lsl.w	#2,d4			; now bytes
	move.l	d4,(a6) 		; line length in save area
	move.l	sd_wsave(a2),8(a6)	; source memory

	movem.l d7/a0-a1/a6,-(sp)
	bsr.s	pt_copyareas		; refresh this area
	movem.l (sp)+,d7/a0-a1/a6

	cmpa.l	a1,a0			; end of list?
	bcs.s	prf_loop		; ...not yet
prf_end
	add.w	#16,sp			; release data block
	move.l	pt_bgdat(a3),a0
	move.l	bd_strt(a0),bd_free(a0) ; list now empty again
prf_exit
	movem.l (sp)+,rfbg.reg
	rts

;+++
; Copy areas that are not overlapped by other windows from the save area to
; the screen. Uses a recursive divide and conquer algorithm.
;
; d1  window lower right corner + (1,1)
; d2  window upper left corner
; d7  current display mode
; a2  channel definition block of primary window
; a3  CON driver linkage
; a6  parameter block:
; +00 row increment of source area
; +04 row increment of destination area
; +08 base address of source area
; +0C base address of destination area
;---
pt_copyareas
	lea	pt_head-sd_prwlt(a3),a0 ; go through all primaries
pca_pwloop
	move.l	sd_prwlt(a0),a0 	; next channel
	move.l	a0,d0			; end of linked list?
	beq.s	pca_copy		; ... yes, copy rectangle
	addq.l	#-sd_prwlt,a0		; set channel block address

	cmp.b	sd_wmode(a0),d7 	; correct mode?
	bne.s	pca_pwloop		; ... no
	cmp.l	a0,a2			; our window?
	beq.s	pca_copy		; yes, copy area now

; okay, some window is up in Z order from ours
;;;	   bsr	   pt_tstov		   ; overlapping? (uses sd_xouts!)
	bsr	pt_tstov_hit		; we're slicing hit, so check ov there
	bne.s	pca_pwloop		; ...no

;;;	   movem.l sd_xouts(a0),d3/d4	   ; d3=size, d4=origin
	movem.l sd_xhits(a0),d3/d4	; d3=size, d4=origin
	add.l	d4,d3			; d3=bottom right, d4=top left
	cmp.w	d4,d2
	bcc.s	pca_chk1
; do strip above overlapping window
chk.reg reg	a0/d1-d4
	movem.l chk.reg,-(sp)
	move.w	d4,d1			; limit Y to top of other window
	bsr.s	pca_pwloop		; start new scan with new limits!
	movem.l (sp)+,chk.reg
pca_chk1
	cmp.w	d3,d1
	bls.s	pca_chk2
; do strip below overlapping window
	movem.l chk.reg,-(sp)
	move.w	d3,d2			; area to paint is below overlap window
	bsr.s	pca_pwloop		; start new scan with new limits!
	movem.l (sp)+,chk.reg

; limit new strips to Y area of overlapping window
pca_chk2
	cmp.w	d4,d2
	bcc.s	pca_chk3
	move.w	d4,d2
pca_chk3
	cmp.w	d3,d1
	bls.s	pca_chk4
	move.w	d3,d1
pca_chk4
	movem.l chk.reg,-(sp)
	swap	d2
	swap	d4
	cmp.w	d4,d2
	bcc.s	pca_chk5
; do strip left of overlapping window
	swap	d1
	move.w	d4,d1
	swap	d1
	swap	d2
	swap	d4
	bsr.s	pca_pwloop		; start new scan with new limits!
pca_chk5
	movem.l (sp)+,chk.reg
	swap	d1
	swap	d3
	cmp.w	d3,d1
	bls.s	pca_chkend
; do strip right of overlapping window
	swap	d2
	move.w	d3,d2
	swap	d1
	swap	d2
	swap	d3
	bsr	pca_pwloop		; start new scan with new limits!
pca_chkend
	rts

pca_copy
pca.reg reg	a2/a3/a6
	movem.l pca.reg,-(sp)
	bsr.s	pca_checkptr		; d1 = max-coords, d2 = min-coords
	sub.l	d2,d1			; now size of area to move
	move.l	d2,d3			; screen origin
	move.l	sd_xhito(a2),d0
	sub.l	d0,d2			; now origin within save area
	and.l	#pt.samsk,d0		; save area offset (QL mode only)
	add.l	d0,d2
	movem.l (a6),a2-a5		; row increments and base addresses
	jsr	pt_mblock
	movem.l (sp)+,pca.reg
	rts

;+++
; Check for pointer sprite overlapp and remove pointer if necessary
;---
pca_checkptr
pchkreg reg	d1-d2/a4
	tst.b	pt_pstat(a3)		; is it visible?
	bne.s	pca_chkexit		; ... no

	movem.l pchkreg,-(sp)
	move.l	pt_psprt(a3),a4 	; point at sprite
	move.l	pt_pos(a3),d5		; this is where its origin is
	sub.l	pto_org(a4),d5		; so this is its screen origin
	moveq	#$fffffff0,d0
	swap	d0
	and.l	d0,d5			; origin of save area
	move.l	#$001f0000,d4		; round up width (and add one)
	add.l	d5,d4			; TT special !!!
	and.l	d0,d4
	add.l	pto_size(a4),d4 	; and how far across it goes
	move.l	d4,d0
	swap	d0
	cmp.w	pt_ssizx(a3),d0 	; off right?
	blt.s	pca_ovchk
	ext.l	d5			; ... check whole screen width
pca_ovchk
	bsr.s	pt_testov		; is it in our window?
	movem.l (sp)+,pchkreg
	ble.s	pca_chkexit		; ... no

	moveq	#pt.supio,d4		; ... yes, suppress for io timeout
	bsr.l	pti_hide		; ... and hide it
pca_chkexit
	rts

;+++
; Test for overlap
;
; d1  max1
; d2  min1
; d4  max2
; d5  min2
;---
pt_testov
	bsr.s	ptst_lim		; check limits
	ble.s	ptst_rts		; no overlap
ptst_lim
	swap	d1
	swap	d2
	swap	d4
	swap	d5
	cmp.w	d2,d4			; compare window xmin with xmax
	ble.s	ptst_rts		; it's greater, so no overlap
	cmp.w	d5,d1			; and compare with xmin
ptst_rts
	rts

;+++
; This is a copy of pt_tstov, but it uses sd_xhit instead of sd_xout
;---
pt_tstov_hit
	bsr.s	chk_lim 		; check the x limits
	ble.s	not_ovs 		; no overlap
	addq.l	#2,a0			; point to window's y co-ordinates
	bsr.s	chk_lim 		; check those
	subq.l	#2,a0			; get cdb back again
	ble.s	not_over
	moveq	#0,d0			; flag overlap
	bra.s	exit			; and exit
not_ovs
	swap	d1
	swap	d2
not_over
	moveq	#-1,d0			; flag no overlap
exit
	rts

chk_lim
	swap	d1
	swap	d2
	move.w	sd_xhito(a0),d0
	cmp.w	d0,d1			; compare window xmin with xmax
	ble.s	ex_chk			; it's greater, so no overlap
	add.w	sd_xhits(a0),d0 	; get window xmax
	cmp.w	d2,d0			; and compare with xmin
ex_chk
	rts

	end
