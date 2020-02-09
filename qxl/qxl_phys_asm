; QXL: this module handles accesses to physical devices
; set up to read sector
; 2006.10.01	1.01	use 32 bits transfer (BC)

qxl_read_phys:
	not	WORD PTR phd_rlen
	mov	WORD PTR phd_rcmd,ax	; flag action to be performed and set phys device
	lodsw
	mov	phd_rsern,ax		; set parameters
	lodsw
	xchg	ah,al
	mov	phd_rsectms,ax
	lodsw
	xchg	ah,al
	mov	phd_rsectls,ax
	jmp	qxl_rxm_loop

        ASSUME es:DGROUP

qxl_write_phys:
	not	WORD PTR phd_wlen
	mov	WORD PTR phd_wcmd,ax	; flag action to be performed and set phys device
	lodsw
	mov	phd_wsern,ax		; set parameters
	lodsw
	xchg	ah,al
	mov	phd_wsectms,ax
	lodsw
	xchg	ah,al
	mov	phd_wsectls,ax

	mov 	di, OFFSET phd_wdata
	mov	cx, 80h                 ; transfer $80 double words

        rep movsd

	jmp	qxl_rxm_loop

qxl_format_phys:
	not	WORD PTR phd_wlen
	mov	WORD PTR phd_wcmd,ax	; flag action to be performed and set phys device
	lodsw
	mov	phd_wsern,ax		; set parameters - transaction
	lodsw
	xchg	ah,al
	mov	phd_wsectms,ax		; number of sectors
	lodsw
	xchg	ah,al
	mov	phd_wsectls,ax

	lodsw
	lodsw					 ; skip identifier for the moment

	jmp	qxl_rxm_loop

; read sector

        ASSUME es:DGROUP

qxl_do_rphys:
qrp_retry:
	mov	bp, OFFSET phd_rcmd
	call	qxl_phys_parm		; set up for read call
	jz	qrp_fail80

	js	qrp_file			; read from file

	mov	ax,0201h	      	; read one sector
	mov	bx, OFFSET phd_rdata   	; into return message
; push dx
; push cx
; push bx
; mov	si, OFFSET nul
; mov  di,sp
; call deb_buff
; pop	bx
; pop	cx
; pop	dx

 	int	013h
	jnc	qrp_done			; OK
	cmp	ah,006h			; disk changed
	jz	qrp_retry			; ... yes, retry (might as well start from the beginning!
	jmp	qrp_failah			; ... no, real error

qrp_file:
	mov	ax,3F00h			; read from file
	mov	cx,512		      ; one sector
	mov	dx, OFFSET phd_rdata	; into return message
	int	21h
	jc	qrp_failah

qrp_done:
	mov	WORD PTR phd_rcmd, qxm_physr ; OK
	mov	cx,(4+512)/2		; long message

qrp_smess:
	mov	ax, OFFSET phd_rlen
	call	qxl_smess			; send message
	jc	qrp_smess
	retn

qrp_fail80:
	mov	al,080h			; general failure code
	jmp	qrp_fail
qrp_failah:
	xor	al,al
	sub	al,ah				; error code (positive) was in ah
qrp_fail:
	mov	phd_rcmd, qxm_rpfail	; failure
	mov	phd_rerror, al		; error code
	mov	cx,4/2			; short message
	jmp	qrp_smess

; write sector / format

        ASSUME es:DGROUP

qxl_do_wphys:
	cmp	phd_wcmd, qxm_fdriv     ; really format?
	je	qxl_do_format
qwp_retry:
	mov	bp, OFFSET phd_wcmd
	call	qxl_phys_parm		; set up for write call
	jz	qwp_fail80

	js	qwp_file			; write to file

	mov	ax,0301h	      	; write one sector
	mov	bx, OFFSET phd_wdata   	; from message area
; push dx
; push cx
; push bx
; mov	si, OFFSET nul
; mov  di,sp
; call deb_buff
; pop	bx
; pop	cx
; pop	dx

 	int	013h
	jnc	qwp_done			; OK
	cmp	ah,006h			; disk changed
	jz	qwp_retry			; ... yes, retry (might as well start from the beginning!
	jmp	qwp_failah			; ... no, real error

qwp_file:
	mov	ax,4000h			; write to file
	mov	cx,512		      ; one sector
	mov	dx, OFFSET phd_wdata	; from message area
	int	21h
	jc	qwp_failah

qwp_done:
	mov	WORD PTR phd_wcmd, qxm_physw ; OK

qwp_smess:
	mov	cx,4/2			; short message
	mov	ax, OFFSET phd_wlen
	call	qxl_smess			; send message
	jc	qwp_smess
	retn

qwp_fail80:
	mov	al,080h			; general failure code
	jmp	qwp_fail
qwp_failah:
	xor	al,al
	sub	al,ah				; error code (positive) was in ah
qwp_fail:
	mov	phd_wcmd, qxm_wpfail	; failure
	mov	phd_werror, al		; error code
	jmp	qwp_smess

; A useful little routine for getting physical device parameters
; If the disk cannot be found, the call returns Z
; For physical disks the status is returned NS with
;	    ch/cl	track and sector
;	    dh/dl	head and drive	  . . . . suitable for INT 13h
; For a QXL.WIN file the status is returned S with
;	    bx	handle
;	    and 	the file position set

qxl_phys_parm:
; mov	si, OFFSET nul
; mov  di, OFFSET phd_rsectls
; call deb_buff

	mov	dl,phd_drive[bp]		; get drive number
	test	dl,dl				; physical or file?
	js	qpp_file			; ... file
	mov	al,dl				; keep for flag
	dec	dl				; drives numbered from 0
	mov	ch, phd_sectms[bp]
	mov	cl, phd_sectls[bp]
	mov	dh, phd_sectls + 1[bp]
	test	al,al
	retn

qpp_file:
	call	qxl_set_physid    ; find file
	jz	qpp_fret

	mov	ch, phd_sectms[bp]	; msbyte of three byte sector number
	mov	cl, phd_sectls + 1[bp]
	mov	dh, phd_sectls[bp]
	xor	dl,dl					; zero at the bottom
	add	dx,dx
	adc	cx,cx					; byte address of 512 byte sector
	push	bx
	push	si
	mov	ax,04200h				; set file position
	int	21h
	mov	bl,-1					; set flag
	test	bl,bl
	pop	si
	pop	bx
qpp_fret:
	retn

; routine which tries to find the file for the disk dl
; returns BX ID or zero, status NZ or Z

qxl_set_physid:
	push	di
	push	si
	push	bp
	and	dx,0007fh		 ; word pointer less MSB
	mov	bp,dx		        ; index drive table by bytes
	mov	dl,drf_drive[bp]        ; extract drive letter
	mov	drf_name,dl             ; set name
	add	bp,bp		        ; index ID table by words
	mov	bx, drf_ID[bp]
	test	bx,bx		       ; is there an ID?
	jnz	qsp_idret		 ; ... yes
	mov	dx, OFFSET drf_name
	mov	ax, 03D02h	       ; open read/write
	push	bp
	int	21h
	pop	bp
	jnc	qsp_idset	       ; OK, set ID
	xor	bx,bx			 ; failed
	jmp	qsp_idret

qsp_idset:
	mov	bx,ax		       ; set return
	mov	drf_ID[bp], bx
	test	bx,bx
qsp_idret:
	pop	bp
	pop	si
	pop	di
	retn


; Finally, at the end, we have the format code

        ASSUME es:DGROUP
qxl_do_format:
; push	si
; mov	si, OFFSET rtext     ;###
; call	deb_wtxt	     ;###
; mov	di, OFFSET rbuff    ;###
; mov	si, OFFSET null	    ;###
; call	deb_str		    ;###
; pop	si

	xor   ax,ax
	mov	WORD PTR phd_wdata,ax
	mov	WORD PTR phd_wdata+2,ax ; clear sector count

	mov	dl,phd_wdrive     ; drive
	test	dl,dl			; file or device
	js	qfm_file		; file
	cmp	dl,02h		; A or B?
	jg	qfm_ffail 	      ; ... no
	dec	dl			; 0 or 1


; the following code reformats a single density or double density floppy disk.
; It may only work if the diskette has been previously formatted using DOS.

qfm_flp:
	xor	ax,ax		     ; reset drive
	push	dx
	int	013h
	pop	dx

	xor	dh,dh		     ; side 0
	xor	cx,cx		     ; track 0
	mov	BYTE PTR phd_wstrk,9 ; assume DD
	call	qfm_vtrack	     ; warm up
	call	qfm_vtrack

	mov	BYTE PTR phd_wstrk,18 ; try HD
	call	qfm_vtrack
	jz	qfm_flpchk	     ; format and verify OK
	call	qfm_vtrack
	jz	qfm_flpchk
	mov	BYTE PTR phd_wstrk,9 ; try DD
	call	qfm_vtrack
	jz	qfm_flpchk
	call	qfm_vtrack
	jnz	qfm_ffail

qfm_flpchk:
	mov	ax,ds
	mov	es,ax
qfm_flploop:
	mov	dh,1		     ; check side 1
	call	qfm_vtrack
	js	qfm_fok	     ; bad track, look no further
	jnz	qfm_ffail	     ; very bad
	xor	dh,dh
	call	qfm_vtrack
	js	qfm_fok	     ; bad track, look no further
	jnz	qfm_ffail	     ; very bad

	inc	ch		     ; next track
	mov	BYTE PTR phd_wntrk, ch ; this many tracks verified
	cmp	ch,80            ; at end?
	jb	qfm_flploop	     ; ... not yet
	jmp	qfm_fok

qfm_vtrack:
	mov	al, BYTE PTR phd_wstrk
	mov	ah, 4
	push	di
	push	dx
	push	cx
	mov	cl,1	    ; start reading from one
	int	013h
	pop	cx
	pop	dx
	pop	di
	jc	qfm_vferr
	xor	ax,ax
	retn

qfm_vferr:
	cmp	ah,004h	     ; sector not found?
	je	qfm_fferr	     ; ... yes
	cmp	ah,010h 	     ; crc error?
	jne	qfm_fffatal	     ; ... no
qfm_fferr:
;	push	di		     ;###
;	mov	si, OFFSET vatext    ;###
;	mov	BYTE PTR debuf2+2,ah ;###
;	mov	al,drf_sect	     ;###
;	mov	BYTE PTR debuf2+3,al ;###
;	mov	di, OFFSET debuf2   ;###
;	call	deb_str		    ;###
;	pop	di		    ;###

	mov	ax,0ffffh		 ; verification error
	test	ax,ax
	retn
qfm_fffatal:
;	push	di		     ;###
;	mov	si, OFFSET fatext    ;###
;	mov	BYTE PTR debuf2+2,ah ;###
;	mov	al, drf_sect	     ;###
;	mov	BYTE PTR debuf2+3,al ;###
;	mov	di, OFFSET debuf2   ;###
;	call	deb_str		    ;###
;	pop	di		    ;###

	mov	ax,1			 ; fatal error
	test	ax,ax
	retn

qfm_file:
	and	dx,0007fh		 ; word pointer less MSB
	mov	bp,dx		        ; index drive table by bytes
	mov	dl,drf_drive[bp]        ; extract drive letter
	mov	drf_name,dl             ; set name
	add	bp,bp		        ; index ID table by words
	push    bp			; keep it
	mov	bx, drf_ID[bp]
	mov	drf_ID[bp],0	 ; remove ID

	test	bx,bx		       ; is there an ID?
	jz	qfm_delete		 ; ... no

	mov	ax,03e00h	      ; close file
	int	21h

qfm_delete:
	mov	dx, OFFSET drf_name
	mov	ax,04100h	      ; delete file
	int	21h

	mov	dx, OFFSET drf_name
	xor	cx,cx
	mov	ax,03c00h	      ; create file
	int	21h
	pop	bp

	jc	qfm_ffail	      ; could not create file

	mov	bx,ax
	mov	cx,WORD PTR phd_wsectls +1 ; sectors *256

qfm_fsectl:
	push	di
	push	cx
	push	bx
	push	bp
	mov	cx,04000h		      ; write 32 sectors
	xor	dx,dx
	mov	ax,04000h
	int	21h
	pop	bp
	pop	bx
	pop	cx
	pop	di
	cmp	ax,04000h			; 32 sectors written?
	jne	qfm_fclose	            ; ... no, failed
	add	BYTE PTR phd_wdata+3,20h ; ... yes, count sectors
	jnz	qfm_fsectl

	add	BYTE PTR phd_wdata+2,1	; count 256 sectors (256 sector loop)
	jnz	qfm_fsectel
	add	BYTE PTR phd_wdata+1,1	; count 65536 sectors
qfm_fsectel:
	loop	qfm_fsectl	      	; and carry on

	mov	drf_ID[bp], bx	      ; file ID

qfm_fok:
	xor	ax,ax				; no error

qfm_smess:
	mov	phd_wcmd, qxm_drivf	; drive formated
	mov	phd_werror, al		; error code
	mov	ax, WORD PTR phd_wdata ; set sector count
	mov	WORD PTR phd_wdata+4, ax
	mov	ax, WORD PTR phd_wdata + 2
	mov	WORD PTR phd_wdata+6, ax

	mov	cx,12/2			; standard message
	mov	ax, OFFSET phd_wlen
	call	qxl_smess			; send message
	jc	qfm_smess
	retn

qfm_fclose:
	mov	ax,03e00h	            ; close file
	int	21h
	mov	dx, OFFSET drf_name
	mov	ax,04100h		      ; delete file
	int	21h
qfm_ffail:
	mov	al,080h			; general failure code
	jmp	qfm_smess

