; Prototpe PIPE driver	V2.01	  Tony Tebby

	section pipe

	xdef	pipe_io

	xref	pipe_name

	include 'dev8_keys_qu'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_err'
	include 'dev8_keys_k'
	include 'dev8_iod_pipe_data'
	include 'dev8_mac_assert'

;+++
; PIPE IO routine
;
; NOTE that this routine uses IO BUFFER definitions for the Queue pointers
; rather than the QU definitions.
;
;	d0 cr	IO key / status
;	d1 cr	IO parameter
;	d2 c	IO parameter (could be extended or set to 15)
;	d3   s
;	a1 cr	IO parameter
;	a2  r	pointer to pointers to IO queues
;	a3/a4	scratch
;	a6 c  p pointer to sysvar
;
;	status returns standard
;
;---
pipe_io
	tst.b	pic_dirf(a0)		; directory?
	bmi.l	pio_dir
	cmp.b	#iob.smul,d0		; simple byte io?
	bhi.s	pio_fs			; ... no, try filing system
	add.w	d0,d0
	move.w	pio_btab(pc,d0.w),d0
	jmp	pio_btab(pc,d0.w)
pio_btab
	dc.w	pio_tbyt-pio_btab
	dc.w	pio_fbyt-pio_btab
	dc.w	pio_flin-pio_btab
	dc.w	pio_fmul-pio_btab
	dc.w	pio_ipar-pio_btab
	dc.w	pio_sbyt-pio_btab
	dc.w	pio_smul-pio_btab
	dc.w	pio_smul-pio_btab

pio_fs
	sub.b	#iof.shdr,d0		 ; set header
	beq.s	pio_shdr
	subq.b	#iof.rhdr-iof.shdr,d0	 ; read header
	beq.s	pio_rhdr
	subq.b	#iof.load-iof.rhdr,d0	 ; load file
	beq.l	pio_load
	subq.b	#iof.save-iof.load,d0	 ; save file
	beq.l	pio_save

pio_ipar
	moveq	#err.ipar,d0
pio_rts
	rts

pio_rhdr		; read header
	moveq	#15,d2			 ; read fifteen bytes
	tst.w	d1			 ; any read yet
	bne.l	pio_load		 ; ... yes, fetch multiple
	bsr.s	pio_tbyt		 ; test for $ff
	bne.s	pio_rts
	addq.b	#1,d1			 ; was it $ff
	bne.s	pio_ipar		 ; ... no, not header
	bsr.s	pio_gbyt		 ; ... yes, fetch byte (and ignore it)
	moveq	#1,d1			 ; one read
	bra.l	pio_gmul		 ; get the rest

pio_shdr		; set header
	moveq	#15,d2			 ; send 15 bytes
	tst.w	d1			 ; any sent yet?
	bne.l	pio_save		 ; ... yes, carry on
	st	d1
	bsr.s	pio_sbyt		 ; send flag byte
	sf	d1
	bne.s	pio_rts
	moveq	#1,d1			 ; one byte gone now
	bra.l	pio_pmul		 ; send the rest

pio_tbyt		; test byte
	move.l	pic_qin(a0),d0		 ; queue pointer
	beq.s	pio_ipar		 ; ... none
	move.l	d0,a2

	move.l	qu_nexto(a2),a3 	 ; next to get
	cmp.l	qu_nexti(a2),a3
	beq.s	pio_cnc 		 ; nothing to come

	cmp.l	qu_endq(a2),a3		 ; off end?
	blt.s	pio_tqu 		 ; ... no
	lea	qu_strtq(a2),a3 	 ; ... yes, start again
pio_tqu
	move.b	(a3)+,d1		 ; get byte out
	moveq	#0,d0
	rts

pio_fbyt		; fetch byte
	move.l	pic_qin(a0),d0		 ; queue pointer
	beq.s	pio_ipar		 ; ... none
	move.l	d0,a2
pio_gbyt
	move.l	qu_nexto(a2),a3 	 ; next get
	cmp.l	qu_nexti(a2),a3
	beq.s	pio_cnc 		 ; nothing to come

	cmp.l	qu_endq(a2),a3		 ; off end?
	blt.s	pio_gqu 		 ; ... no
	lea	qu_strtq(a2),a3 	 ; ... yes, start again
pio_gqu
	move.b	(a3)+,d1		 ; get byte out
	move.l	a3,qu_nexto(a2)
pio_ok
	moveq	#0,d0
	rts

pio_ncn
	add.l	a1,d1			 ; set quantity transferred
pio_cnc
	move.b	qu_eoff(a2),d0		 ; nothing more, is it end of file?
	bmi.s	pio_eof
pio_nc
	moveq	#err.nc,d0
	rts
pio_eof
	moveq	#err.eof,d0
	rts


pio_sbyt	       ; send byte
	move.l	pic_qout(a0),d0 	 ; queue pointer
	beq.l	pio_ipar		 ; ... none
	move.l	d0,a2
pio_pbyt
	move.l	qu_nexti(a2),a3 	 ; next place to put
	cmp.l	qu_endq(a2),a3		 ; off end?
	blt.s	pio_pqu 		 ; ... no
	lea	qu_strtq(a2),a3 	 ; ... yes, start again
	cmp.l	qu_nexto(a2),a3 	 ; buffer full?
	beq.s	pio_nc			 ; ... yes
pio_pqu
	move.b	d1,(a3)+		 ; put byte in
	cmp.l	qu_nexto(a2),a3
	beq.s	pio_nc
	move.l	a3,qu_nexti(a2) 	 ; OK, update pointer
	moveq	#0,d0
	rts


pio_flin		; fetch line
	ext.l	d2
	move.l	pic_qin(a0),d0		 ; queue pointer
	beq.l	pio_ipar		 ; ... none
	move.l	d0,a2

pio_glin
	sub.l	d1,d2			 ; amount to come
	sub.l	a1,d1			 ; prepare count
	move.l	d2,d3
	ble.s	pio_bfn 		 ; ... none

	moveq	#k.nl,d6		 ; newline check

	move.l	qu_nexto(a2),a3 	 ; next to get
	move.l	qu_nexti(a2),d7 	 ; total bytes in middle
	sub.l	a3,d7
	beq.s	pio_ncn 		 ; nothing to come
	bgt.s	pio_gl3 		 ; ... case 3, no wrap possible

	move.l	qu_endq(a2),d7		 ; end
	sub.l	a3,d7			 ; bytes to end
	beq.s	pio_gl2 		 ; nothing from this bit

	cmp.l	d3,d7			 ; more than a buffer full?
	blt.s	pio_gl1 		 ; ... no
	move.l	d3,d7			 ; ... yes, limit to buffer

pio_gl1
	sub.l	d7,d3			 ; amount left to fill buffer
	subq.w	#1,d7
pio_gl1l
	move.b	(a3)+,(a1)
	cmp.b	(a1)+,d6		 ; end of line?
	dbeq	d7,pio_gl1l
	beq.s	pio_oko
	move.l	a3,qu_nexto(a2) 	 ; update output byte pointer

	tst.w	d3			 ; any more to fetch?
	ble.s	pio_bfn 		 ; ... no
pio_gl2
	lea	qu_strtq(a2),a3 	 ; second bit
	move.l	qu_nexti(a2),d7
	sub.l	a3,d7			 ; bytes in second bit
	ble.l	pio_ncn
pio_gl3
	cmp.l	d3,d7			 ; more than buffer full?
	blt.s	pio_gl2s		 ; ... no
	move.l	d3,d7			 ; ... yes, limit
	ble.s	pio_bfn 		 ; ... none

pio_gl2s				 ; case 2, start of queue
	sub.l	d7,d3			 ; remaining buffer not fillable
	subq.w	#1,d7
pio_gl2l
	move.b	(a3)+,(a1)
	cmp.b	(a1)+,d6		 ; end of line?
	dbeq	d7,pio_gl2l
	beq.s	pio_oko

	move.l	a3,qu_nexto(a2) 	 ; update output byte pointer
	tst.l	d3			 ; any more buffer left?
	bne.l	pio_ncn 		 ; ... yes
pio_bfn
	add.l	a1,d1
	moveq	#err.bffl,d0
	rts

pio_oko
	move.l	a3,qu_nexto(a2) 	 ; update output byte pointer
	add.l	a1,d1			 ; set count
	moveq	#0,d0
	rts


pio_fmul		; fetch multiple / load
	ext.l	d2
pio_load
	move.l	pic_qin(a0),d0		 ; queue pointer
	beq.l	pio_ipar		 ; ... none
	move.l	d0,a2

pio_gmul
	moveq	#0,d6			 ; no second bit to get
	move.l	d2,d3			 ; amount to get
	sub.l	d1,d3			 ; less amount already fetched
	ble.l	pio_ok
	move.l	qu_nexto(a2),a3 	 ; next to get
	move.l	qu_nexti(a2),d7 	 ; total bytes in middle
	sub.l	a3,d7
	beq.l	pio_cnc 		 ; nothing to come
	bgt.s	pio_gm3 		 ; ... case 3, no wrap possible

	move.l	qu_endq(a2),d7		 ; end
	sub.l	a3,d7			 ; bytes to end
	cmp.l	d3,d7			 ; more than a buffer full?
	bge.s	pio_gm1 		 ; ... yes, no wrap required

	lea	qu_strtq(a2),a4 	 ; second bit
	move.l	qu_nexti(a2),d6
	sub.l	a4,d6			 ; bytes in second bit
	sub.l	d7,d3			 ; amount required from this bit
	cmp.l	d3,d6			 ; enough
	bge.s	pio_gm2 		 ; ... yes, case 2, copy all
	bra.s	pio_gm

pio_gm3 				 ; case 3, no wrap possible
	cmp.l	d3,d7			 ; enough room
	bge.s	pio_gm1 		 ; ... yes, this becomes case 1
	bra.s	pio_gm

pio_gm2 				 ; case 2
	move.l	d3,d6			 ; all from first, this much from second
	bra.s	pio_gm

pio_gmr
	move.l	a4,a3			 ; do second bit
	moveq	#0,d6			 ; ... and not again
	bra.s	pio_gm

pio_gm1 				 ; case 1, all one bit
	move.l	d3,d7			 ; get just this much from second bit

pio_gm
	add.l	d7,d1			 ; copy first bit

	move.l	a3,d3			 ; check for odd address
	move.l	a1,d4
	or.l	d3,d4
	lsr.b	#1,d4
	bcs.s	pio_gm1e		 ; ... odd, byte at a time

	moveq	#32,d3
	moveq	#$1c,d4 		 ; mask of long words in 16 bytes
	and.w	d7,d4
	sub.l	d4,d7			 ; gives n*32 + 0..3
	neg.w	d4
	asr.w	#1,d4			 ; +0, 4, 8 .... bytes
	jmp	pio_gmxe(pc,d4.w)

pio_gmxl
	move.l	(a3)+,(a1)+		 ; 32 bytes at a time
	move.l	(a3)+,(a1)+
	move.l	(a3)+,(a1)+
	move.l	(a3)+,(a1)+
	move.l	(a3)+,(a1)+
	move.l	(a3)+,(a1)+
	move.l	(a3)+,(a1)+
	move.l	(a3)+,(a1)+
pio_gmxe
	sub.l	d3,d7			 ; 32 more?
	bge.s	pio_gmxl		 ; ... yes
	add.l	d3,d7			 ; ... no not that many

	bra.s	pio_gm1e
pio_gm1l
	move.b	(a3)+,(a1)+
pio_gm1e
	subq.l	#1,d7
	bge.s	pio_gm1l

	move.l	d6,d7			 ; second section
	bgt	pio_gmr 		 ; ... yes, repeat

	move.l	a3,qu_nexto(a2) 	 ; ... no, update pointer
	cmp.l	d1,d2			 ; all gone?
	bne.l	pio_cnc

	moveq	#0,d0			 ; ... ok
	rts				 ; d0 set


pio_smul	       ; send multiple / save
	ext.l	d2
pio_save
	move.l	pic_qout(a0),d0 	 ; queue pointer
	beq.l	pio_ipar		 ; ... none
	move.l	d0,a2

pio_pmul
	moveq	#0,d6			 ; no second bit to put
	move.l	d2,d3			 ; amount to put
	sub.l	d1,d3			 ; less amount already sent
	move.l	qu_nexti(a2),a3 	 ; next to put
	move.l	qu_nexto(a2),d7 	 ; total room in middle
	sub.l	a3,d7
	subq.l	#1,d7			 ; less the one spare
	bge.s	pio_pm3 		 ; ... case 3, no wrap possible

	move.l	qu_endq(a2),d7		 ; end
	sub.l	a3,d7			 ; room to end
	cmp.l	d3,d7			 ; enough?
	bge.s	pio_pm1 		 ; ... case 1, no wrap necessary

	lea	qu_strtq(a2),a4 	 ; second bit
	move.l	qu_nexto(a2),d6
	sub.l	a4,d6
	subq.l	#1,d6			 ; ... room at start
	sub.l	d7,d3			 ; amount required from this bit
	cmp.l	d3,d6			 ; enough
	bge.s	pio_pm2 		 ; ... yes, case 2, copy all
	bra.s	pio_pnr 		 ; ... no enough room

pio_pm3 				 ; case 3, no wrap possible
	cmp.l	d3,d7			 ; enough room
	bge.s	pio_pm1 		 ; ... yes, this becomes case 1

pio_pnr
	moveq	#err.nc,d0		 ; ... will be NC in any case
	tst.l	pin_nin-pin_qu(a2)	 ; named (shared) pipe?
	beq.s	pio_pm			 ; ... no we can put a bit
	rts

pio_pm2 				 ; case 2
	move.l	d3,d6			 ; all from first, this much from second
	bra.s	pio_pm0

pio_pmr
	move.l	a4,a3			 ; do second bit
	moveq	#0,d6			 ; ... and not again
	bra.s	pio_pm

pio_pm1 				 ; case 1, all one bit
	move.l	d3,d7			 ; get just this much from second bit

pio_pm0
	moveq	#0,d0			 ; it will be ok

pio_pm
	add.l	d7,d1			 ; copy first bit

	move.l	a3,d3			 ; check for odd address
	move.l	a1,d4
	or.l	d3,d4
	lsr.b	#1,d4
	bcs.s	pio_pm1e		 ; ... odd, byte at a time

	moveq	#32,d3
	moveq	#$1c,d4 		 ; mask of long words in 16 bytes
	and.w	d7,d4
	sub.l	d4,d7			 ; gives n*32 + 0..3
	neg.w	d4
	asr.w	#1,d4			 ; +0, 4, 8 .... bytes
	jmp	pio_pmxe(pc,d4.w)

pio_pmxl
	move.l	(a1)+,(a3)+		 ; 32 bytes at a time
	move.l	(a1)+,(a3)+
	move.l	(a1)+,(a3)+
	move.l	(a1)+,(a3)+
	move.l	(a1)+,(a3)+
	move.l	(a1)+,(a3)+
	move.l	(a1)+,(a3)+
	move.l	(a1)+,(a3)+
pio_pmxe
	sub.l	d3,d7			 ; 32 more?
	bge.s	pio_pmxl		 ; ... yes
	add.l	d3,d7			 ; ... no not that many

	bra.s	pio_pm1e
pio_pm1l
	move.b	(a1)+,(a3)+
pio_pm1e
	subq.l	#1,d7
	bge.s	pio_pm1l

	move.l	d6,d7			 ; second section
	bgt	pio_pmr 		 ; ... yes, repeat
	move.l	a3,qu_nexti(a2) 	 ; ... no, update pointer
	rts				 ; d0 set

; ****** DIRECTORY entries

pio_dir
	cmp.b	#iob.fmul,d0		 ; simple fetch multiple bytes io?
	bne.s	pid_fs			 ; ... no, try filing system

	cmp.w	#$40,d2 		 ; 40 byte entries only
	bne	pio_ipar

	move.l	pic_ptnm(a0),a2 	 ; pointer
	move.l	(a2)+,d4
	beq	pio_eof 		 ; end of list
	move.l	a2,pic_ptnm(a0) 	 ; save it

	lea	pil_list(a3),a2

pid_look
	assert	pin_link,0
	move.l	(a2),d0 		 ; next name
	beq.s	pid_empty
	move.l	d0,a2
	cmp.l	d0,d4			 ; the name we want?
	bne.s	pid_look

	moveq	#$40-pin_qu-qu_strtq,d0  ; 'file length' adjust
	add.l	pin_qu+qu_endq(a2),d0
	sub.l	a2,d0
	move.l	d0,(a1)+
	clr.w	(a1)+
	clr.l	(a1)+
	clr.l	(a1)+
	lea	pin_name(a2),a4
	moveq	#(pin.name+2-1)/4,d3
pid_cname
	move.l	(a4)+,(a1)+
	dbra	d3,pid_cname

	moveq	#($40-pin.name-2-14)/2,d3

pid_eret
	moveq	#$40,d1 		 ; return
	moveq	#0,d0

pid_clear
	move.w	d0,(a1)+		 ; clear the rest
	dbra	d3,pid_clear
	rts

pid_empty
	moveq	#($40-2)/2,d3		 ; empty return
	bra.s	pid_eret

; DIR fs calls

pid_fs
	sub.b	#iof.rhdr,d0		 ; read header
	beq.s	pid_rhdr
	sub.b	#iof.minf-iof.rhdr,d0	 ; medium info
	bne	pio_ipar

; DIR medium info

	move.l	pic_nrnm+2(a0),d1
	move.w	pic_nrnm+2(a0),d1
	lea	pipe_name,a4
	move.l	(a4)+,(a1)+
	move.l	(a4)+,(a1)+
	move.w	(a4)+,(a1)+

	moveq	#0,d0
	rts

; DIR read header

pid_rhdr
	move.l	pic_nrnm(a0),d1 	 ; number of items
	lsl.l	#6,d1			 ; length of list
	move.l	d1,(a1)+		 ; file length
	move.w	#$00ff,(a1)+		 ; directory
	clr.l	(a1)+
	clr.l	(a1)+
	moveq	#14,d1			 ; return
	moveq	#0,d0
	rts

	end
