; Console input        V2.00	 1994	Tony Tebby
; 2003-09-28		2.01	bugfix for cursor move if one-line wdw (wl)
; 3004-03-27		2.02	compatible with new cursor toggle (wl)
	section con

	xdef	cn_test 	; test input
	xdef	cn_fbyt 	; fetch byte
	xdef	cn_fmul 	; fetch multiple
	xdef	cn_flin 	; fetch line
	xdef	cn_edln 	; edit line

	xref	cn_sbyte
	xref	cn_smulc
	xref	cn_scnrw
	xref	cn_scral
	xref	cn_scprw
	xref	cn_clrbt
	xref	cn_clrcr
	xref	cn_dopnl
	xref	cn_curtg
	xref	cn_cksize_p

	xref	cv_cttab
	xref	gu_thjmp
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_config_keys'
	include 'dev8_keys_con'
	include 'dev8_keys_qlv'
	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_k'
	include 'dev8_mac_assert'

; set queue pointer

cni_squ
	tst.l	sd_keyq(a0)		; any keyboard queue?
	ble.s	cni_ipar		; ... no
	lea	sd_keyq(a0),a2		; ... yes, set it
	tst.w	d3			; re-entry?
	bne.s	cni_ok			; ... yes
	move.l	a2,sys_ckyq(a6) 	; set pointer to keyboard queue!!!
cni_ok
	moveq	#0,d0
	rts

cni_ipar
	moveq	#err.ipar,d0
	rts

	page

; test input

cn_test
	bsr.s	cni_squ 		; set queue
	bne.s	cni_rts1
	move.w	ioq.test,a4
	jmp	(a4)			; and test

; fetch byte

cn_fbyt
	bsr.s	cni_squ 		; set queue
	bne.s	cni_rts1
	move.w	ioq.gbyt,a4
	jmp	(a4)			; and fetch byte

; fetch multiple bytes

cn_fmul
	tst.w	d2			; anything to do
	blt.s	cni_ipar
	beq.s	cni_ok			; ... no

	bsr.s	cni_squ 		; set queue
	bne.s	cni_rts1
	move.w	d1,d4			; bytes so far
	move.w	ioq.gbyt,a4
bfm_loop
	jsr	(a4)			; fetch byte
	bne.s	bfm_done
	move.b	d1,(a1)+		; transfer it
	addq.w	#1,d4			; one more
	cmp.w	d4,d2
	bne.s	bfm_loop		; try again

bfm_done
	move.w	d4,d1
cni_rts1
	rts
	page

;	line edit ops

;	a1 cr	pointer to end of characters
;	a2   s	pointer to current cursor position
;	a4   s	pointer to start of buffer
;	a5   s	pointer to end of buffer

; fetch byte

efbreg	reg	a2/a4
cne_fbyt
	movem.l efbreg,-(sp)
	lea	sd_keyq(a0),a2		; set queue
	move.w	ioq.gbyt,a4
	jsr	(a4)			; and fetch byte
	movem.l (sp)+,efbreg
cne_rts1
	rts

cne_orng
	moveq	#err.orng,d0
	rts



; Remove the cursor

cne_remc
	tst.b	d7			; is cursor visible?
	ble.s	cne_rts1		; ... no
	clr.b	d7			; not now
	move.b	#-1,sd_curf(a0) 	; show it
	jmp	cn_curtg(pc)

; find the buffer length for one line

cne_bufln
	moveq	#-1,d4			; buffer full flag
	moveq	#0,d2
	move.w	sd_xsize(a0),d2 	; x size
	sub.w	sd_xpos(a0),d2		; x pixel position
	divu	sd_xinc(a0),d2		; nr of chars
	swap	d1			; cursor position
	add.w	d1,d2			; buffer length
	swap	d1
	rts


; edit line

;	d1	msw  cursor position / lsw number of chars in buffer
;	d2	>=0  buffer length   <0 fill one line of window only
;	d3	0 for first call, else -1
;	a1	pointer after last char in buffer

volreg0 reg	d0/d1/d2/d7/a1/a2/a3/a4/a5
volreg	reg	d1/d2/d7/a1/a2/a3/a4/a5
vol_d1	equ	0

cn_edln
	bclr	#31,d1			; clear throwaway flag
	sne	d7
	ror.l	#1,d7			; keep flag in msb
	move.w	#$8000,d7		; edit in word


; fetch line  (d7=0)

cn_flin

cn_fled
	moveq	#0,d4			; buffer full flag
	bsr.l	cni_squ 		; set queue and test
	bne.s	cni_rts1		; (this sets a2 to keyboard queue)
	move.b	sd_curf(a0),d7		; set cursor status
	tst.b	sd_nlsta(a0)		; newline required?
	beq.s	cn_first		; ... no, check first entry

	bsr.s	cne_remc		; remove cursor
	jsr	cn_dopnl		; do pending newline


;	d1	msw  cursor position / lsw number of chars in buffer
;	d2	>=0  buffer length   <0 fill one line of window only
;	d3	0 for first call, else -1
;	d7	some kind of flag (fetch line, edit line)
;	a0	channel defn block
;	a1	pointer after last char in buffer
;	a2	pointer to keyboard queue

cn_first
	tst.w	d3			; first entry?
	bne.s	cne_setp		; ... no

	tst.b	sd_sflag(a0)		; wdw size OK ?
	beq.s	cne_buff		; ... yes
	bgt.s	cne_orng		; ... cursor out of window
	jsr	cn_cksize_p		; re-check size
	blo.s	cne_orng		; ... no
cne_buff
	tst.w	d2			; any buffer at all?
	beq.l	cne_bffl		; ... no, leave with error
	bgt.s	cne_wichar		; ... yes, normal buffer
	bsr	cne_bufln		; ... no but calculate length


;	d1	msw  cursor position / lsw number of chars in buffer
;	d2	buffer length
;	d3	0 for first call, else -1
;	d7	some kind of flag (fetch line, edit line)
;	a0	channel defn block
;	a1	pointer after last char in buffer
;	a2	pointer to keyboard queue

cne_wichar
	tst.l	d1			; any characters at all?
	beq.s	cne_setp		; ... no
	cmp.w	d2,d1			; too many?
	bhs.l	cne_bffl		; ... yes, leave with error

	bsr.s	cne_remc		; remove cursor

	movem.l d1/d2/d7,-(sp)		; save registers
	tst.l	d7
	bmi.s	cne_sisln		; throwaway
	tst.w	(sp)			; not throwaway, cursor 0 is goto end!
	bne.s	cne_sisln
	move.w	d1,(sp) 		; cursor at end
cne_sisln
	move.w	d1,d2			; line length
	swap	d1
	sub.w	d1,d2			; characters after cursor
	sub.w	d2,a1			; character under cursor
	moveq	#0,d1
	bsr.l	cn_smulc		; send rest of line

	move.w	(sp),d1 		; required cursor position
	lea	(a1,d1.w),a2		; end+cursor
	sub.w	2(sp),a2		; less length is where we want it
	move.l	a1,d2			; where it is now
	bsr.l	cne_movc		; ... restore it
	movem.l (sp)+,d1/d2/d7		; restore registers

cne_setp
	move.l	d1,d3			; save buffer position
	bsr	cne_fbyt		; get the first character into D1
	bne.l	cne_exd3		; ooops!
	tst.w	d2			; buffer length set?
	bgt.s	cne_setb		; this shouldn't be necessary here anymore!
	bsr	cne_bufln
cne_setb
	move.l	a1,a4			; set start of buffer...
	sub.w	d3,a4			; ...now


	swap	d3
	lea	(a4,d3.w),a2		; set cursor position
	lea	(a4,d2.w),a5		; set end of buffer


;	d1	msw  cursor position / lsw char got
;	d2	buffer length
;	d3	msw  cursor position / lsw number of chars in buffer
;	d7	some kind of flag (fetch line, edit line)
;	a0	channel defn block
;	a1	pointer after last char in buffer
;	a2	cursor position
;	a4	start of buffer
;	a5	end of buffer

	bsr	cne_remc		; remove cursor

; throw away if at start of line and first char printable

	tst.l	d7			; throwaway flag set?
	bpl.s	cne_tbyte		; ... no, so treat char

	bclr	#31,d7			; clear throwaway flag

	cmp.b	#k.enter,d1		; enter?
	beq.l	cne_done		; ... yes, so we're done!

cne_eprint
	cmp.b	#' ',d1 		;  printable? (There is a table of these!!
	blo.s	cne_tbyte		; not printable, do normal edit
	cmp.b	#k.maxch,d1
	bhi.s	cne_tbyte		; ... ditto

; Edit starts with printable char, throw buffer away

	movem.l volreg,-(sp)
	bsr.l	cn_clrcr		; clear right
	bsr.l	cn_clrbt		; and bottom
	movem.l (sp)+,volreg		; restore character
	move.l	a4,a1			; no line
	move.l	a4,a2			; cursor position
	bra.s	cne_char		; insert character


cne_loop
;	 cmp.l	 a5,a1			 ; buffer full?
;	 bge.l	 cne_bffl

	bsr.l	cne_fbyt		; get next character
	bne.l	cne_exit

; here we have:
;	d1	msw  cursor position / lsw char got
;	d2	buffer length
;	d3	msw  cursor position / lsw number of chars in buffer
;	d7	some kind of flag (fetch line, edit line)
;	a0	channel defn block
;	a1	pointer after last char in buffer
;	a2	cursor position
;	a4	start of buffer
;	a5	end of buffer
cne_tbyte
	move.l	a2,d2			; save current cursor position
	move.b	d1,d0
	subq.b	#k.bspce,d0		; is it backspace?
	beq.l	cne_dleft		; ... yes, delete character
	subq.b	#k.tab-k.bspce,d0	; is it TAB
	beq.l	cne_tright		; ... yes, tab right
	subq.b	#k.enter-k.tab,d0	; is it enter?
	beq.s	cne_done		; ... yes, done
	cmp.b	#k.esc,d1		; is it escape?
	beq.s	cne_dned		; ... yes, done
	cmp.b	#' ',d1 		; is it a control character?
	blo.s	cne_loop		; ... yes, forget it
	cmp.b	#k.maxch,d1		; is it special?
	bhi.s	cne_cursor		; ... yes, check for cursor keys
cne_char
	move.l	a1,d0			;  how many characters to the right of the cursor
	sub.l	a2,d0
	move.l	a1,a2			; move characters up
	addq.l	#1,a1			; ... no, one more character
	cmp.l	a5,a1			; buffer full?
	blt.s	cne_mupe		; ... no
	tst.l	d4			; fill line?
	bpl	cne_bffl		; ... no
	subq.l	#1,a1			; ... yes, ignore character
	sub.l	d0,a2
	bra.s	cne_loop

cne_mupl
	move.b	-(a2),1(a2)
cne_mupe
	dbra	d0,cne_mupl

	move.l	a2,d2
	move.b	d1,(a2)+		; put character in
	moveq	#0,d0			; say no blank at end
	bra.l	cne_wend		; and write end of line

cne_cursor
	moveq	#-k.up,d0
	assert	k.maxch+1,k.left,k.right-8,k.up-16
	add.b	d1,d0			; is it left or right
	bcc.s	cne_lr			; ... yes
	beq.s	cne_dned		; ... no, up
	subq.b	#k.down-k.up,d0 	; is it down?
	beq.s	cne_dned
	subq.b	#k.cdn-k.down,d0	; or shift down
	beq.l	cne_dline
	addq.b	#-k.stab,d1		; is it shift tab?
	beq.l	cne_tleft
	subq.b	#3,d1
	cmp.b	sms.conf+sms_kstuf,d1
	beq	stuffhk
	bra.s	cne_loop		; ... no, not recog

cne_dned
	tst.w	d7			; is it fetch line
	bpl.s	cne_loop		; yes, only enter is done
cne_done
	movem.l volreg,-(sp)
	move.l	a2,d2			; current position
	move.l	a1,a2			; cursor at end
	move.b	d1,(a1) 		; set terminator
	bsr.l	cne_movc
	bne.s	cne_enter		; not at beginning of line
	cmp.l	a1,a4			; beginning of buffer
	bne.s	cne_addl		; ... no
cne_enter
	moveq	#k.enter,d1		; set end of line
	bsr.l	cn_sbyte
cne_addl
	movem.l (sp)+,volreg
	addq.l	#1,a1
	moveq	#0,d0
cne_exit
	move.l	a2,d3			; set cursor position
	sub.l	a4,d3
	swap	d3
	move.l	a1,d2			; length of line
	sub.l	a4,d2
	move.w	d2,d3
cne_exd3
	move.l	d3,d1
	tst.l	d7			; first char throwaway flag set?
	bpl.s	cne_scur
	bset	#31,d1			; ... yes, set d1 flag
cne_scur
	moveq	#err.nc,d3		; not complete?
	cmp.l	d0,d3
	beq.s	cne_ecur
	clr.b	sd_curf(a0)		; clear cursor status
	tst.b	d7			; was it visible before?
	bgt.s	cne_tcur		; yes, toggle it
cne_rts
	rts

cne_bffl
	moveq	#err.bffl,d0		; buffer full
	bra.s	cne_exit

cne_ecur
	tst.b	d7			; is it already visible or invisible?
	bne.s	cne_rts 		; yes
	move.b	#1,sd_curf(a0)		; visible now
cne_tcur
	bra.l	cn_curtg		; toggle it

; left and right

cne_lr
	moveq	#-1,d1
	move.b	d0,d1
	add.b	d0,d1			; index table
	move.w	cne_table(pc,d1.w),d1
	jmp	cne_table(pc,d1.w)

	dc.w	cne_cleft-cne_table
	dc.w	cne_toleft-cne_table
	dc.w	cne_dleft-cne_table
	dc.w	cne_dtoleft-cne_table
	dc.w	cne_wleft-cne_table
	dc.w	cne_loop-cne_table
	dc.w	cne_dwleft-cne_table
	dc.w	cne_loop-cne_table
	dc.w	cne_cright-cne_table
	dc.w	cne_toright-cne_table
	dc.w	cne_dright-cne_table
	dc.w	cne_dtoright-cne_table
	dc.w	cne_wright-cne_table
	dc.w	cne_loop-cne_table
	dc.w	cne_dwright-cne_table
	dc.w	cne_loop-cne_table
cne_table

cne_wleft
	bsr.l	cne_lwback
	bra.l	cne_setc

cne_tleft
	subq.l	#1,a2
	move.l	a2,d0
	sub.l	a4,d0
	and.w	#$fff8,d0
	lea	(a4,d0.w),a2		; set new position
	cmp.l	a4,a2			; is it at lhs?
	bge.l	cne_setc
cne_toleft
	move.l	a4,a2
	bra.l	cne_setc



; here we have :
;	d1	msw  cursor position / lsw char got
;	d2	cursor position
;	d3	msw  cursor position / lsw number of chars in buffer
;	d7	some kind of flag (fetch line, edit line)
;	a0	channel defn block
;	a1	pointer after last char in buffer
;	a2	cursor position
;	a4	start of buffer
;	a5	end of buffer
cne_cleft
	cmp.l	a4,a2			; is it at lhs?
	ble.l	cne_loop		; yes, do nothing
	subq.l	#1,a2			; move left
	bra.l	cne_setc		; new cursor position

cne_wright
	bsr.l	cne_lwfwd
	bra.l	cne_setc

cne_tright
	addq.l	#8,a2
	move.l	a2,d0
	sub.l	a4,d0
	and.w	#$f8,d0
	lea	(a4,d0.w),a2		; set new position
	cmp.l	a1,a2			; is it at rhs?
	ble.l	cne_setc

cne_toright
	move.l	a1,a2
	bra.l	cne_setc

cne_cright
	cmp.l	a1,a2			; is it at rhs?
	bge.l	cne_loop
	addq.l	#1,a2			; move right
	bra.s	cne_setc

cne_dtoleft
	move.l	a4,a2			; delete to here
	bra.s	cne_dwldo

cne_dline
	move.l	a4,a2			; go to left
	bsr.s	cne_movc

cne_dtoright
	move.l	a1,d2			; end of line
	sub.l	a2,d2			; where we are
	ble	cne_loop
	bra.s	cne_drdo

cne_dwleft
	bsr.l	cne_lwback		; find where to delete to
cne_dwldo
	move.l	d2,d0
	sub.w	a2,d0			; amount to delete
	move.w	d0,-(sp)
	bsr.s	cne_movc
	move.w	(sp)+,d2
	bra.s	cne_drdo

cne_dwright
	bsr.l	cne_lwfwd		; delete word to right
	exg	a2,d2
	sub.l	a2,d2			; amount to delete
	bra.s	cne_drdo

cne_dleft
; here we have :
;	d1	msw  cursor position / lsw char got
;	d2	cursor position
;	d3	msw  cursor position / lsw number of chars in buffer
;	d7	some kind of flag (fetch line, edit line)
;	a0	channel defn block
;	a1	pointer after last char in buffer
;	a2	cursor position
;	a4	start of buffer
;	a5	end of buffer

	cmp.l	a4,a2			; is it at lhs?
	ble.l	cne_loop		; yes, so do nothing
	subq.l	#1,a2			; move left, then delete
	bsr.s	cne_movc

cne_dright
	moveq	#1,d2			; delete this many
cne_drdo
	sub.w	d2,a1			; line one shorter
	move.l	a1,d0			; set number of characters to move down
	move.l	a2,a1
	sub.l	a1,d0
	blt.l	cne_loop		; none, don't move

	bra.s	cne_mdle
cne_mdlp
	move.b	(a1,d2.w),(a1)+
cne_mdle
	dbra	d0,cne_mdlp

	moveq	#0,d0
	move.w	d2,d0			; save delete count
	bra.s	cne_msle
cne_mslp
	move.b	#' ',(a1)+
cne_msle
	dbra	d2,cne_mslp

	sub.l	d0,a1			; the real end
	move.l	a2,d2			; write from here

cne_wend
	movem.l volreg0,-(sp)
	exg	a1,d2			; write to end of line
	sub.l	a1,d2			; from d2
	add.w	d0,d2			; and include empty bits at end
	moveq	#0,d1
	bsr.l	cn_smulc
	movem.l (sp)+,volreg0

	move.l	a1,d2			; set current cursor position
	add.l	d0,d2			; including spaces at end

;	d1	msw  cursor position / lsw char got
;	d2	(old) cursor position
;	d3	msw  cursor position / lsw number of chars in buffer
;	d7	some kind of flag (fetch line, edit line)
;	a0	channel defn block
;	a1	pointer after last char in buffer
;	a2	new cursor position
;	a4	start of buffer
;	a5	end of buffer
cne_setc
	pea	cne_loop(pc)		; set jump address

; this code for moving the cursor will only work if the line is limited to
; one screen line or the characters ar aligned to a multiple character
; boundary

cne_movc
	clr.b	sd_nlsta(a0)		; throw away new line
	move.w	sd_xinc(a0),d4		; cursor size
	moveq	#0,d5			; window size
	move.w	sd_xsize(a0),d5
	divu	d4,d5
	moveq	#0,d6			; cursor position
	move.w	sd_xpos(a0),d6
	divu	d4,d6

	sub.l	a2,d2			; distance to move
	sub.w	d2,d6			; new cursor position
	blt.s	cne_crup		; move up
	cmp.w	d5,d6			; off edge?
	bge.s	cne_crdn		; yes

	muls	d4,d2			; cursor move
	sub.w	d2,sd_xpos(a0)
	rts


cne_crdn
	clr.w	sd_xpos(a0)
	bsr.l	cn_scnrw		; move down
	beq.s	cne_nxrdn

	movem.l a1/a2/d4/d5/d6/d7,-(sp)
	move.w	sd_yinc(a0),d1		; scroll it
	neg.w	d1
	bsr.l	cn_scral
	movem.l (sp)+,a1/a2/d4/d5/d6/d7

cne_nxrdn
	sub.w	d5,d6
	cmp.w	d5,d6			; off edge?
	blt.s	cne_crok		; no
	bra.s	cne_crdn

; here the cursor must move 'up'
cne_crup
	move.w	sd_ysize(a0),d0 	; window y size
	move.w	sd_yinc(a0),d1		; cursor y size
	add.w	d1,d1			; for cursor to be able to move up...
	sub.w	d1,d0			; ... wdw must be at least twice cur y size
	bge.s	cne_crupok		: - .... it is

; now we must clear all of the window and print that part of the buffer
; that fits in the wdw
; we must thus know how many chars fit in the wdw, to calculate the
; start of the buffer we must print from
; calculate where in the buffer the new line starts
; here we have:
;	d1	msw  cursor position / lsw char got
;	d2	distance to move
;	d3	msw  cursor position / lsw number of chars in buffer
;	d4	sd_xinc = cursor xsize
;	d5	nbr of chars that fit in wdw
;	d6	new cursor pos in wdw (in chars!)
;	d7	some kind of flag (fetch line, edit line)
;	a0	channel defn block
;	a1	pointer after last char in buffer
;	a2	new cursor position (in buffer)
;	a4	start of buffer
;	a5	end of buffer

	move.l	a2,d0			; where cursor is right now
	sub.w	d5,d0			; where it should be
	bge.s	cne_cup
	move.l	a4,d0			; point to start of string
cne_cup
	movem.l volreg0,-(sp)
	move.l	d0,a1
	move.w	d5,d2
	moveq	#0,d1
	bsr.l	cn_smulc		; send all of these!
	movem.l (sp)+,volreg0
	move.w	d5,d6
	bra.s	cne_crok
cne_crupok
	clr.w	sd_xpos(a0)
	bsr.l	cn_scprw		; move up
	add.w	d5,d6
	blt.s	cne_crup		; still off edge

cne_crok
	mulu	d4,d6			; new cursor position
	move.w	d6,sd_xpos(a0)		; *** sets ccr to Z at start of line
	rts

; look forward - starting at (a2) looks for start of next word (letter or digit)

cne_lwfwd
	move.l	a0,d0			; save channel
	moveq	#k.other,d3		; non letter / digit
	lea	cv_cttab,a0
	moveq	#0,d1

cne_lfnld     ; look for non-letter / digit
	cmp.l	a1,a2			; at end?
	bge.s	cne_lwend		; ... yes
	move.b	(a2)+,d1		; next character
	cmp.b	(a0,d1.w),d3		; non letter / digit
	bne.s	cne_lfnld		; ... no

cne_lfld      ; look for letter / digit
	cmp.l	a1,a2			; at end?
	bge.s	cne_lwend		; ... yes
	move.b	(a2)+,d1		; next character
	cmp.b	(a0,d1.w),d3		; non letter / digit
	beq.s	cne_lfld		; ... yes

	subq.l	#1,a2

cne_lwend
	move.l	d0,a0			; restore channel
	rts

; look back - starting at (a2) looks for start of previous word (letter or digit)

cne_lwback
	move.l	a0,d0			; save channel
	moveq	#k.other,d3		; non letter / digit
	lea	cv_cttab,a0
	moveq	#0,d1

cne_lbld      ; look for letter / digit
	cmp.l	a4,a2			; at start?
	ble.s	cne_lwend		; ... yes
	move.b	-(a2),d1		; next character
	cmp.b	(a0,d1.w),d3		; non letter / digit
	beq.s	cne_lbld		; ... yes

cne_lbnld     ; look for non-letter / digit
	cmp.l	a4,a2			; at start?
	ble.s	cne_lwend		; ... yes
	move.b	-(a2),d1		; next character
	cmp.b	(a0,d1.w),d3		; non letter / digit
	bne.s	cne_lbnld		; ... no

	addq.l	#1,a2			; start of word
	bra.s	cne_lwend


; stuff current line into hk buffer
hk.stbuf equ	 $002c			; long	  hotkey stuff buffer
vreg0	reg	d0-d7/a0-a6
stuffhk movem.l vreg0,-(sp)		; keep some regs
	move.l	d3,d2
	swap	d2			; number of chars to stuff
	tst.w	d2			; are there any?
	beq.s	stufout 		; .... no!
	lea	hkn,a0			; name of hotkey thing
	moveq	#$28,d0 		; use thing
	moveq	#-1,d1			; for myself
	moveq	#-1,d3			; i've got all the time in the world
	jsr	gu_thjmp		; do it
	tst.l	d0
	bne.s	stufout 		; oops...
	move.l	hk.stbuf(a1),a2 	; vector to use
	move.l	a1,a3			; needs ptr to linkage block in a3
	move.l	a4,a1			; point to string
	jsr	(a2)			; stuff string pointed to by a1
un_use	lea	hkn,a0			; and free thing again
	moveq	#$29,d0
	moveq	#-1,d1
	moveq	#-1,d3
	jsr	gu_thjmp
stufout movem.l (sp)+,vreg0
	bra	cne_loop

hkn	DC.W	6,'HOTKEY'


	end
