;	Recolour a window	v0.01   1991  Tony Tebby
;
; 2009-06-06  0.01  Fixed mode 8 (MK)
;
;	QL Colour
;
;	Registers:
;		Entry				Exit
;	D0					0
;	D1-D7					smashed
;	A0	CDB				preserved
;	A1	recolour list			smashed
;	A2-A5					smashed
;
;	All other registers preserved
;
	section con

	include 'dev8_keys_con'
	include 'dev8_keys_sys'

	xdef	cn_recol

cn_recol
	move.b	pt_dmode(a3),d4 	 ; get mode byte now (a3 is used later)
	movem.w sd_xmin(a0),d0-d3
	move.l	sd_scrb(a0),a3
	mulu	sd_linel(a0),d1
	add.l	d1,a3			 ; start of line
	move.l	d0,d1
	lsr.w	#3,d1			 ; start pixel -> words
	add.w	d1,d1			 ; -> bytes
	add.w	d1,a3			 ; start address
	not.w	d0			 ; bit number
	and.w	#7,d0			 ; 0 to 7

	mulu	sd_linel(a0),d3 	 ; ... number of lines
	lea	(a3,d3.l),a2		 ; off end line

	cmp.b	#ptm.ql8,d4		 ; which mode are we in?
	beq.s	cnr_8row

cnr_4row
	move.l	a3,a4			 ; running pointers
	move.w	d0,d4			 ; pixel number
	move.w	d0,d5
	addq.w	#8,d5
	move.w	d2,d6			 ; pixel counter

cnr_4wrd
	move.w	(a4),d1 		 ; our word

cnr_4pix
	bclr	d5,d1
	bne.s	cnr_44			 ; 4 or 7
	bclr	d4,d1
	bne.s	cnr_42			 ; 2

	move.b	(a1),d7 		 ; = 0
	bra.s	cnr_4spx

cnr_42
	move.b	2(a1),d7		 ; = 2
	bra.s	cnr_4spx

cnr_44
	bclr	d4,d1
	bne.s	cnr_47

	move.b	4(a1),d7		 ; = 4
	bra.s	cnr_4spx

cnr_47
	move.b	7(a1),d7		 ; = 7

cnr_4spx
	lsr.w	#2,d7
	bcc.s	cnr_4hb
	bset	d4,d1			 ; set lower pixel
cnr_4hb
	lsr.w	#1,d7
	bcc.s	cnr_4npx		 ; set higher pixel
	bset	d5,d1

cnr_4npx
	subq.w	#1,d6
	ble.s	cnr_4nrw

	subq.w	#1,d5			 ; next pixel
	subq.w	#1,d4
	bge.s	cnr_4pix		 ; there is one

	move.w	d1,(a4)+		 ; save word
	moveq	#15,d5			 ; and re-start pixel count
	moveq	#7,d4
	bra.s	cnr_4wrd

cnr_4nrw
	move.w	d1,(a4)
	add.w	sd_linel(a0),a3 	 ; next row
	cmp.l	a2,a3
	blt.s	cnr_4row

	bra.l	cnr_ok


cnr_8row
	move.l	a3,a4			 ; running pointers
	move.w	d0,d3
	subq.w	#1,d3
	move.w	d0,d4
	move.w	d0,d5
	addq.w	#8,d5
	move.w	d2,d6			 ; pixel counter

cnr_8wrd
	move.w	(a4),d1 		 ; our word

cnr_8pix
	bclr	d5,d1
	bne.s	cnr_84			 ; 4 - 7
	bclr	d4,d1
	bne.s	cnr_82			 ; 2 - 3

	bclr	d3,d1
	bne.s	cnr_81

	move.b	(a1),d7 		 ; = 0
	bra.s	cnr_8spx

cnr_81
	move.b	1(a1),d7		 ; = 1
	bra.s	cnr_8spx

cnr_82
	bclr	d3,d1
	bne.s	cnr_83

	move.b	2(a1),d7		 ; = 2
	bra.s	cnr_8spx

cnr_83
	move.b	3(a1),d7		 ; = 3
	bra.s	cnr_8spx

cnr_84
	bclr	d4,d1
	bne.s	cnr_86

	bclr	d3,d1
	bne.s	cnr_85

	move.b	4(a1),d7		 ; = 4
	bra.s	cnr_8spx

cnr_85
	move.b	5(a1),d7		 ; = 5
	bra.s	cnr_8spx

cnr_86
	bclr	d3,d1
	bne.s	cnr_87

	move.b	6(a1),d7		 ; = 6
	bra.s	cnr_8spx

cnr_87
	move.b	7(a1),d7		 ; = 7

cnr_8spx
	lsr.w	#1,d7
	bcc.s	cnr_8mb
	bset	d3,d1			 ; set lower bit
cnr_8mb
	lsr.w	#1,d7
	bcc.s	cnr_8hb
	bset	d4,d1			 ; set middle bit
cnr_8hb
	lsr.w	#1,d7
	bcc.s	cnr_8npx		 ; set higher bit
	bset	d5,d1

cnr_8npx
	subq.w	#2,d6
	ble.s	cnr_8nrw

	subq.w	#2,d5			 ; next pixel
	subq.w	#2,d4
	subq.w	#2,d3
	bge.s	cnr_8pix		 ; there is one

	move.w	d1,(a4)+		 ; save word
	moveq	#15,d5			 ; and re-start pixel count
	moveq	#7,d4
	moveq	#6,d3
	bra.s	cnr_8wrd

cnr_8nrw
	move.w	d1,(a4)
	add.w	sd_linel(a0),a3 	 ; next row
	cmp.l	a2,a3
	blt	cnr_8row

cnr_ok
	moveq	#0,d0
	rts
	end
