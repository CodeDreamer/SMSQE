; RAM disk driver medium information   V3.00    1985  Tony Tebby   QJUMP
; Fast memory version				 2002  Marcel Kilgus
;
; 2002-05-02  3.00  Added scanning loop for TPA address space. Exchanged
;		    16bit division by larger one. 16 bit result registers are
;		    set to $FFFF in case of an overflow.

	section rd

	xdef	rd_occi 	 ; medium information

	include 'dev8_keys_sys'
	include 'dev8_keys_chp'
	include 'dev8_keys_iod'
	include 'dev8_dd_rd_data'
	include 'dev8_mac_assert'
;+++
; This routine gets the RAM disk information and ensures that the physical
; definition block is up to date.
;
;	d0  r	0
;	d1  r	empty / good sectors
;	d2   s
;	d6 c  p drive number / file number
;	a0 c  p channel definition block
;	a2   s	pointer to buffer
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to physical definition
;	a5 c  p pointer to map
;
;	all other registers preserved
;---
rd_occi
	tst.b	rdb_stat(a5)		 ; static?
	bne	rdo_stat		 ; ... yes

	moveq	#0,d2			 ; no sectors
	move.l	a5,-(sp)		 ; keep pointer to map

rdo_mloop
	addq.l	#1,d2
	moveq	#0,d1
rdo_floop
	move.l	rdb_data(a5,d1.w),d0	 ; first sector of file
	beq.s	rdo_nfile
rdo_sloop
	addq.l	#1,d2
	move.l	d0,a2
	assert	rdb_slst,0
	move.l	(a2),d0
	bne.s	rdo_sloop		 ; another sector
rdo_nfile
	addq.w	#4,d1
	cmp.w	#rdb.data,d1
	blt.s	rdo_floop		 ; another file

	move.l	(a5),a5
	move.l	a5,d0
	bne.s	rdo_mloop		 ; another map sector

rdo_free
	moveq	#0,d1			 ; total the free space
	lea	sys_chpf-chp_nxfr(a6),a2
rdo_frloop
	move.l	chp_nxfr(a2),d0 	 ; next
	beq.s	rdo_gap
	add.l	d0,a2
	move.l	chp_len(a2),d0		 ; length
	add.l	d0,d1
	bra.s	rdo_frloop

rdo_gap
	move.l	sys_sbab(a6),d0 	 ; total fsb space
	sub.l	sys_fsbb(a6),d0
	sub.l	#$200,d0		 ; ... less one slave block
	add.l	d0,d1

	lea	sys_tpaf-chp_nxfr(a6),a2
rdo_fm_frloop
	move.l	chp_nxfr(a2),d0 	 ; next
	beq.s	rdo_to_sectors
	add.l	d0,a2
	move.l	chp_len(a2),d0		 ; length
	add.l	d0,d1
	bra.s	rdo_fm_frloop

rdo_to_sectors
;	 divu	 #rdb.len+chp.len,d1	 ; number of whole sectors
	assert	rdb.len+chp.len,$220	 ; need 32b/16b = 32b division
	divu	#$2200,d1		 ; the code is good enough for up
	move.l	d1,d0			 ;   to 512MB ram
	andi.l	#$ffff,d1
	lsl.l	#4,d1			 ; Y = int(X / $2200)
	swap	d0
	andi.l	#$ffff,d0
	divu	#$220,d0
	andi.l	#$ffff,d0
	add.l	d0,d1			 ; Y = Y + int((X mod $2200) / $220)

	add.l	d1,d2			 ; free, total
	move.l	(sp)+,a5		 ; restore map pointer

	bra.s	rdo_setf

; data on fixed drive

rdo_stat
	moveq	#0,d1
	moveq	#0,d2
	move.w	rdb_fsec(a5),d1 	 ; free in d1
	move.w	rdb_tsec(a5),d2 	 ; total in d2

rdo_setf
	move.l	d1,iod_free(a4) 	 ; set free and total
	move.l	d2,iod_totl(a4)

	cmp.l	#$ffff,d1
	ble.s	rdo_free_ok
	moveq	#-1,d1			 ; Overflow, set to max
rdo_free_ok
	cmp.l	#$ffff,d2
	ble.s	rdo_total_ok
	moveq	#-1,d2			 ; Overflow, set to max
rdo_total_ok
	swap	d1
	move.w	d2,d1
	moveq	#0,d0
	rts
	end
