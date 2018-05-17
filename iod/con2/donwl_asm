;	Do a new line			V2.00   1998 Tony Tebby
;
;	This routine is called when a pending newline is activated, by
;	changing the character size, sending more characters, activating
;	the cursor or reading its position.  The pending newline flag is
;	set when there is no more room for characters on the current line,
;	or a line feed character is printed.  It is cleared by this
;	routine, or by a whole area clear, or a cursor positioning call.
;
;	Registers:
;		Entry				Exit
;	A0	pointer to cdb			preserved
;
	section con
;
	include 'dev8_keys_con'
;
	xref	cn_scral
;
	xdef	cn_dopnl
	xdef	cn_donl
;
nlreg	reg	d0-d7/a0/a1

cn_dopnl
	moveq	#0,d0
	tst.b	sd_nlsta(a0)		 ; is there a pending newline?
	bne.s	cn_donl 		 ; yes
	rts				 ; no
;
cn_donl
	movem.l nlreg,-(sp)
	tst.b	sd_sflag(a0)		 ; possible?
	beq.s	cnn_do			 ; ... yes
	blt.s	cnn_exit		 ; ... no, never

	move.w	sd_yinc(a0),d1		 ; new bottom edge of cursor...
	move.w	sd_ypos(a0),d0		 ; for cursor outside window
	add.w	d1,d0
	move.w	d0,sd_ypos(a0)		 ; move down
	bmi.s	cnn_slst		 ; outside top of window
	add.w	d1,d0
	cmp.w	sd_ysize(a0),d0
	bgt.s	cnn_slst
	clr.b	sd_sflag(a0)		 ; in window now
	bra.s	cnn_slst

cnn_do
	move.w	sd_yinc(a0),d1		 ; new bottom edge of cursor...
	move.w	d1,d0
	add.w	d0,d0
	add.w	sd_ypos(a0),d0		  ; ...would be this
	cmp.w	sd_ysize(a0),d0 	  ; ...past bottom of active area
	bgt.s	cnn_scrl		  ; it is past, scroll instead

	add.w	d1,sd_ypos(a0)		  ; move down
	bra.s	cnn_slst		  ; and set to start of line
;
cnn_scrl
	neg.w	d1			  ; move upwards
	jsr	cn_scral(pc)		  ; scroll whole area
;
cnn_slst
	clr.w	sd_xpos(a0)		  ; set cursor to left hand edge
	clr.b	sd_nlsta(a0)		  ; no pending newline now
cnn_exit
	movem.l (sp)+,nlreg
cnn_exns
	rts				  ; and exit
;
	end
