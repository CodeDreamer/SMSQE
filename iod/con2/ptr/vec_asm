; CON driver vectored routines table  V1.04		2003  Marcel Kilgus
;
; 2003-05-13  1.01  added pt_size   (wl)
; 2004-04-01  1.02  added pt_cursp  (wl)
; 2006-01-04  1.03  added pt_bgctl  (MK)
; 2013-04-28  1.04  added pt_cmmblk (wl)

	section driver

	xdef	pv_table

	xref	pt_fspr
	xref	pt_sspr
	xref.l	pt.vers
	xref	pt_mblock
	xref.s	pt.spxlw
	xref.s	pt.rpxlw
	xref	pt_cursp
	xref	pt_bgctl
	xref	pt_cmbblk

	include 'dev8_keys_con'

; Table entries are 6 bytes long. This way we can also handle really large jumps
pv_table
	jmp	pt_pinf 		; $00 - pv_pinf
	nop
	jmp	pt_fspr 		; $06 - pv_fspr
	nop
	jmp	pt_sspr 		; $0c - pv_sspr
	nop
	jmp	pt_size 		; $12 - pv_size
	nop
	jmp	pt_mblock		; $18 - pv_mblk
	nop
	jmp	pt_cursp		; $1e - pv_cursp
	nop
	jmp	pt_bgctl		; $24 - pv_bgctl
	nop
	jmp	pt_cmbblk		; $2a - pv_cmbblk
	nop


;+++
;  PV_PINF	 $00
;
;  Call parameters			Return parameters
;  D1					D1   pointer version number
;  D2					D2   preserved
;  D3					D3   preserved
;
;  A0					A0   preserved
;  A1					A1   pointer to WMAN
;  A2					A2   preserved
;  A3	pointer to CON linkage block	A3   preserved
;---
pt_pinf
	move.l	#pt.vers,d1		; set pointer version number
	move.l	pt_wman(a3),a1		; and interface vector
	moveq	#0,d0
	rts

;+++
; PV_SIZE	$10
;
;  Call Parameters			return parameters
;  D0					D0   pt.spxlw | pt.rpxlw
;  D1					D1   preserved
;  D2					D2   preserved
;  D3					D3   preserved
;
;  A0					A0   preserved
;  A1					A1   preserved
;  A2					A2   preserved
;  A3	pointer to CON linkage block	A3   preserved
;----
pt_size
	move.w	#pt.spxlw,d0
	swap	d0
	move.w	#pt.rpxlw,d0
	rts

	end
