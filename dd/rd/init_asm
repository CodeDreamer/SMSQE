; RAM Disk disk initialisation	V2.02	 1985	Tony Tebby   QJUMP

	section rd

	xdef	rd_init

	xref.l	rd_vers 	; get version

	xref	ut_procdef
	xref	iou_ddst
	xref	iou_ddlk

;***	xref	rd_chek 	; check all slave blocks read
;***	xref	rd_flsh 	; flush all buffers
	xref	rd_occi 	; get occupancy information
	xref	iou_load	; load
	xref	iou_save	; save
	xref	rd_trnc 	; truncate
	xref	rd_lcbf 	; locate buffer
	xref	rd_albf 	; locate / allocate buffer
;***	xref	rd_upbf 	; mark buffer updated
	xref	rd_alfs 	; allocate first sector
	xref	rd_ckop 	; check medium for open operation
	xref	rd_fdrv 	; format drive
;***	xref	rd_rsec 	; read sector
;***	xref	rd_wsec 	; write sector

	include 'dev8_keys_iod'
	include 'dev8_mac_proc'
	include 'dev8_dd_rd_data'

;+++
; Initialise RAM_USE and DD linkage and get device going.
;
;	status return standard
;---
rd_init
	lea	proctab,a1
	jsr	ut_procdef
	lea	rd_vect,a3		 ; set up dd linkage
	jsr	iou_ddst
	jmp	iou_ddlk

rd_vect
	dc.l	rdd_end     ; length of linkage
	dc.l	rdd.plen    ; length of physical definition
	dc.w	3,'RAM0'    ; device name (usage)
	dc.w	3,'RAM0'    ; device name (real)

null	dc.w	0	    ; external interrupt server
	dc.w	0	    ; polling server
	dc.w	0	    ; scheduler server

	dc.w	0	    ; io
	dc.w	0	    ; open
	dc.w	0	    ; close
	dc.w	0	    ; forced slaving
	dc.w	0	    ; dummy
	dc.w	0	    ; dummy
	dc.w	0	    ; format

	dc.w	0	    ; check all slave blocks read
	dc.w	0	    ; flush all buffers
	dc.w	rd_occi-*   ; get occupancy information
	dc.w	iou_load-*  ; load
	dc.w	iou_save-*  ; save
	dc.w	rd_trnc-*   ; truncate
	dc.w	rd_lcbf-*   ; locate buffer
	dc.w	rd_albf-*   ; locate / allocate buffer
	dc.w	0	    ; mark buffer updated
	dc.w	rd_alfs-*   ; allocate first sector
	dc.w	rd_ckop-*   ; check medium for open operation
	dc.w	rd_fdrv-*   ; format drive
	dc.w	0	    ; read sector
	dc.w	0	    ; write sector
	dc.w	-1

	section procs
proctab
	proc_stt
	proc_def RAM_USE
	proc_end
	proc_stt
	proc_end
	end
