; Open DOS	    V1.01     2000   Marcel Kilgus
;
; 2019-04-07  1.01  Adapted for new QPC2v5 DOS driver

	section dos

	xdef	dos_open

	xref	iou_achb
	xref	iou_rchb

	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_chn'
	include 'dev8_keys_iod'
	include 'dev8_keys_hdr'
	include 'dev8_smsq_qpc_dos_data'
	include 'dev8_smsq_qpc_keys'

dos_open
	move.l	a1,a4
	move.b	chn_drid(a0),iod_drid(a4) ; keep drive ID
	moveq	#0,d2
	move.b	iod_dnum(a4),d2 	; drive number
	move.w	d2,chn_drnr(a0) 	; drive number
	move.l	a1,chn_ddef(a0) 	; set definition pointer

	lea	iod_mnam(a4),a1
;	 move.l  #'    ',d1		 ; first fill it up with spaces
;	 move.l  d1,(a1)
;	 move.l  d1,4(a1)
;	 move.w  d1,8(a1)
;	 moveq	 #10,d1 		 ; maximum length
;	 bsr	 dos_getdevicename
	move.w	d2,d1			; drive number
	dc.w	qpc.dminf		; fill iod_mnam

	moveq	#0,d0
	move.b	chn_accs(a0),d0
	ext.w	d0
	add.w	d0,d0
	add.w	open_tab(pc,d0.w),d0
	jmp	open_tab(pc,d0.w)

	dc.w	delete_file-*
open_tab
	dc.w	old_file-*
	dc.w	old_file_shared-*
	dc.w	new_file-*
	dc.w	over_file-*
	dc.w	open_dir-*

delete_file
	lea	chn_name(a0),a1
	dc.w	qpc.ddele+1
	rts

new_file
	lea	chn_name(a0),a1
	dc.w	qpc.donew+1
	bne.s	dos_error
	move.l	d1,chn_hand(a0) ; DOS File handle

	move.l	#hdr.len,chn_fpos(a0)
	move.l	#hdr.len,chn_feof(a0)
	rts

over_file
	lea	chn_name(a0),a1
	dc.w	qpc.doovr+1
	bne.s	dos_error
	move.l	d1,chn_hand(a0) ; DOS File handle

	move.l	#hdr.len,chn_fpos(a0)
	move.l	#hdr.len,chn_feof(a0)
	rts

old_file_shared
	lea	chn_name(a0),a1
	dc.w	qpc.dopin+1
	beq.s	old_file_success
	cmp.l	#err.fdnf,d0
	bne.s	dos_error

	dc.w	qpc.dfatr+1	; get file attribute
	bmi.s	dos_error
	andi.b	#16,d0		; 16 = directory
	bne.s	open_dir
	moveq	#err.fdnf,d0	; no directory, return fnf
	rts

old_file
	lea	chn_name(a0),a1
	dc.w	qpc.dopen+1
	bne.s	dos_error
old_file_success
	move.l	d1,chn_hand(a0) ; DOS File handle

	move.l	#hdr.len,chn_fpos(a0)
	dc.w	qpc.dsize+2	; Get file size in D2
	add.l	#hdr.len,d2
	move.l	d2,chn_feof(a0)
dos_error
	rts

open_dir
	move.b	#4,chn_accs(a0) ; In case the call went over ofs
	lea	chn_name(a0),a1
	dc.w	qpc.dodir+1
	bne.s	dos_error
	move.l	d1,chn_hand(a0)

	move.l	#hdr.len,chn_fpos(a0)
	dc.w	qpc.dsize+2	; Get file size in D2
	add.l	#hdr.len,d2
;	 lsl.l	 #7,d2
;	 lsr.w	 #7,d2
	move.l	d2,chn_feof(a0)
	moveq	#0,d0
	rts

err_nc
	moveq	#err.nc,d0
	rts

	end
